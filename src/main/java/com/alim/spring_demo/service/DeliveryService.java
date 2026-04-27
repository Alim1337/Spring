package com.alim.spring_demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alim.spring_demo.dto.DeliveryRequestCreate;
import com.alim.spring_demo.entity.Customer;
import com.alim.spring_demo.entity.DeliveryRequest;
import com.alim.spring_demo.entity.DeliveryStatus;
import com.alim.spring_demo.entity.DriverProfile;
import com.alim.spring_demo.entity.Role;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.exception.InvalidOperationException;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.CustomerRepository;
import com.alim.spring_demo.repository.DeliveryRequestRepository;
import com.alim.spring_demo.repository.DriverProfileRepository;
import com.alim.spring_demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final CustomerRepository customerRepository;
    private final DeliveryRequestRepository deliveryRepository;
    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;

    // BUSINESS: create a delivery request
    public DeliveryRequest createDelivery(DeliveryRequestCreate req, String businessEmail) {
        User business = getUserByEmail(businessEmail);

        Customer customer = customerRepository.findById(req.getCustomerId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Customer not found with id: " + req.getCustomerId()));

        User customerUser = userRepository.findByEmail(customer.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found for customer email: " + customer.getEmail()));

        if (customerUser.getRole() != Role.CUSTOMER) {
            throw new InvalidOperationException("Target user is not a customer");
        }

        DeliveryRequest delivery = new DeliveryRequest();
        delivery.setBusiness(business);
        delivery.setCustomer(customerUser);
        delivery.setPickupAddress(req.getPickupAddress());
        delivery.setDropoffAddress(req.getDropoffAddress());
        delivery.setItemDescription(req.getItemDescription());
        delivery.setPrice(req.getPrice());

        return deliveryRepository.save(delivery);
    }

    // BUSINESS: get all their deliveries
    public List<DeliveryRequest> getBusinessDeliveries(String email) {
        User business = getUserByEmail(email);
        return deliveryRepository.findByBusiness(business);
    }

    // CUSTOMER: get their incoming deliveries
    public List<DeliveryRequest> getCustomerDeliveries(String email) {
        User customer = getUserByEmail(email);
        return deliveryRepository.findByCustomer(customer);
    }

    // DRIVER: get all available (PENDING) deliveries
    public List<DeliveryRequest> getAvailableDeliveries() {
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING);
    }

    // DRIVER: get their accepted deliveries
    public List<DeliveryRequest> getDriverDeliveries(String email) {
        User driver = getUserByEmail(email);
        return deliveryRepository.findByDriver(driver);
    }

    // DRIVER: accept a delivery
    public DeliveryRequest acceptDelivery(Long deliveryId, String driverEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User driver = getUserByEmail(driverEmail);

        if (delivery.getStatus() != DeliveryStatus.PENDING) {
            throw new InvalidOperationException("Delivery is no longer available");
        }

        DriverProfile profile = driverProfileRepository.findByUser(driver)
            .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));

        if (!profile.isAvailable()) {
            throw new InvalidOperationException("You already have an active delivery");
        }

        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.ACCEPTED);
        delivery.setAcceptedAt(LocalDateTime.now());

        profile.setAvailable(false);
        driverProfileRepository.save(profile);

        return deliveryRepository.save(delivery);
    }

    // DRIVER: update delivery status
    public DeliveryRequest updateStatus(Long deliveryId,
                                        DeliveryStatus newStatus,
                                        String driverEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User driver = getUserByEmail(driverEmail);

        if (!delivery.getDriver().getId().equals(driver.getId())) {
            throw new InvalidOperationException(
                "You are not assigned to this delivery");
        }

        delivery.setStatus(newStatus);

        if (newStatus == DeliveryStatus.PICKED_UP) {
            delivery.setPickedUpAt(LocalDateTime.now());
        }
        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(LocalDateTime.now());
            driverProfileRepository.findByUser(driver).ifPresent(p -> {
                p.setAvailable(true);
                p.setTotalDeliveries(p.getTotalDeliveries() + 1);
                driverProfileRepository.save(p);
            });
        }

        return deliveryRepository.save(delivery);
    }

    // CUSTOMER: rate a delivery
    public DeliveryRequest rateDelivery(Long deliveryId, int rating, String customerEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User customer = getUserByEmail(customerEmail);

        if (!delivery.getCustomer().getId().equals(customer.getId())) {
            throw new InvalidOperationException("This is not your delivery");
        }
        if (delivery.getStatus() != DeliveryStatus.DELIVERED) {
            throw new InvalidOperationException("Can only rate delivered orders");
        }
        if (rating < 1 || rating > 5) {
            throw new InvalidOperationException("Rating must be between 1 and 5");
        }

        delivery.setRating(rating);

        if (delivery.getDriver() != null) {
            driverProfileRepository.findByUser(delivery.getDriver()).ifPresent(p -> {
                double newRating = ((p.getRating() * p.getTotalDeliveries()) + rating)
                    / (p.getTotalDeliveries() + 1);
                p.setRating(Math.round(newRating * 10.0) / 10.0);
                driverProfileRepository.save(p);
            });
        }

        return deliveryRepository.save(delivery);
    }

    // DRIVER: update location
    public void updateLocation(String driverEmail, Double lat, Double lng) {
        User driver = getUserByEmail(driverEmail);
        driverProfileRepository.findByUser(driver).ifPresent(p -> {
            p.setCurrentLatitude(lat);
            p.setCurrentLongitude(lng);
            driverProfileRepository.save(p);
        });
    }

    private DeliveryRequest getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Delivery not found with id: " + id));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found: " + email));
    }
}