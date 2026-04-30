// Full updated DeliveryService.java
package com.alim.spring_demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
    private final NotificationService notificationService;

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
        delivery.setTrackingCode(generateTrackingCode());

        DeliveryRequest saved = deliveryRepository.save(delivery);

        // notify customer
        notificationService.send(customerUser,
            "📦 New delivery incoming: " + req.getItemDescription(),
            "NEW_DELIVERY");

        return saved;
    }

    public List<DeliveryRequest> getBusinessDeliveries(String email) {
        User business = getUserByEmail(email);
        return deliveryRepository.findByBusiness(business);
    }

    public List<DeliveryRequest> getCustomerDeliveries(String email) {
        User customer = getUserByEmail(email);
        return deliveryRepository.findByCustomer(customer);
    }

    public List<DeliveryRequest> getAvailableDeliveries() {
        return deliveryRepository.findByStatus(DeliveryStatus.PENDING);
    }

    public List<DeliveryRequest> getDriverDeliveries(String email) {
        User driver = getUserByEmail(email);
        return deliveryRepository.findByDriver(driver);
    }

    public DeliveryRequest acceptDelivery(Long deliveryId, String driverEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User driver = getUserByEmail(driverEmail);

        if (delivery.getStatus() != DeliveryStatus.PENDING) {
            throw new InvalidOperationException("Delivery is no longer available");
        }

        DriverProfile profile = driverProfileRepository.findByUser(driver)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Driver profile not found — please complete your setup first"));

        if (!profile.isAvailable()) {
            throw new InvalidOperationException("You already have an active delivery");
        }

        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.ACCEPTED);
        delivery.setAcceptedAt(LocalDateTime.now());

        profile.setAvailable(false);
        driverProfileRepository.save(profile);

        DeliveryRequest saved = deliveryRepository.save(delivery);

        // notify customer and business
        notificationService.send(delivery.getCustomer(),
            "🚗 " + driver.getFirstName() + " accepted your delivery and is on the way!",
            "DELIVERY_ACCEPTED");
        notificationService.send(delivery.getBusiness(),
            "✅ Driver " + driver.getFirstName() + " accepted delivery #" + deliveryId,
            "DELIVERY_ACCEPTED");

        return saved;
    }

    public DeliveryRequest updateStatus(Long deliveryId,
                                        DeliveryStatus newStatus,
                                        String driverEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User driver = getUserByEmail(driverEmail);

        if (!delivery.getDriver().getId().equals(driver.getId())) {
            throw new InvalidOperationException("You are not assigned to this delivery");
        }

        delivery.setStatus(newStatus);

        if (newStatus == DeliveryStatus.PICKED_UP) {
            delivery.setPickedUpAt(LocalDateTime.now());
            notificationService.send(delivery.getCustomer(),
                "📦 Your package has been picked up and is heading your way!",
                "PICKED_UP");
        }

        if (newStatus == DeliveryStatus.ON_THE_WAY) {
            notificationService.send(delivery.getCustomer(),
                "🛵 Your delivery is on the way! Get ready to receive it.",
                "ON_THE_WAY");
        }

        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(LocalDateTime.now());
            driverProfileRepository.findByUser(driver).ifPresent(p -> {
                p.setAvailable(true);
                p.setTotalDeliveries(p.getTotalDeliveries() + 1);
                driverProfileRepository.save(p);
            });
            notificationService.send(delivery.getCustomer(),
                "🎉 Your delivery has arrived! Don't forget to rate your driver.",
                "DELIVERED");
            notificationService.send(delivery.getBusiness(),
                "✅ Delivery #" + deliveryId + " completed successfully!",
                "DELIVERED");
        }

        if (newStatus == DeliveryStatus.CANCELLED) {
            notificationService.send(delivery.getCustomer(),
                "❌ Your delivery #" + deliveryId + " was cancelled.",
                "CANCELLED");
        }

        return deliveryRepository.save(delivery);
    }

    public DeliveryRequest cancelDelivery(Long deliveryId, String businessEmail) {
        DeliveryRequest delivery = getDeliveryById(deliveryId);
        User business = getUserByEmail(businessEmail);

        if (!delivery.getBusiness().getId().equals(business.getId())) {
            throw new InvalidOperationException("This is not your delivery");
        }
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new InvalidOperationException("Cannot cancel a delivered order");
        }
        if (delivery.getStatus() == DeliveryStatus.CANCELLED) {
            throw new InvalidOperationException("Already cancelled");
        }

        if (delivery.getDriver() != null) {
            driverProfileRepository.findByUser(delivery.getDriver()).ifPresent(p -> {
                p.setAvailable(true);
                driverProfileRepository.save(p);
            });
            notificationService.send(delivery.getDriver(),
                "❌ Delivery #" + deliveryId + " was cancelled by the business.",
                "CANCELLED");
        }

        notificationService.send(delivery.getCustomer(),
            "❌ Your delivery was cancelled by the sender.",
            "CANCELLED");

        delivery.setStatus(DeliveryStatus.CANCELLED);
        return deliveryRepository.save(delivery);
    }

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
            notificationService.send(delivery.getDriver(),
                "⭐ You received a " + rating + "/5 rating for delivery #" + deliveryId,
                "RATED");
        }

        return deliveryRepository.save(delivery);
    }

    public void updateLocation(String driverEmail, Double lat, Double lng) {
        User driver = getUserByEmail(driverEmail);
        driverProfileRepository.findByUser(driver).ifPresent(p -> {
            p.setCurrentLatitude(lat);
            p.setCurrentLongitude(lng);
            driverProfileRepository.save(p);
        });
    }

    public DeliveryRequest trackByCode(String code) {
        return deliveryRepository.findByTrackingCode(code)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No delivery found with tracking code: " + code));
    }

    private String generateTrackingCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder("DLV-");
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
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