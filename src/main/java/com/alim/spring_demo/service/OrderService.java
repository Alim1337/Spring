package com.alim.spring_demo.service;

import com.alim.spring_demo.entity.Order;
import com.alim.spring_demo.entity.OrderStatus;
import com.alim.spring_demo.repository.CustomerRepository;
import com.alim.spring_demo.repository.DriverRepository;
import com.alim.spring_demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public Order createOrder(Long customerId, String deliveryAddress) {
        // We fetch the real Customer object from DB first
        // We can't just accept a Customer in the request body — the client
        // should only send an ID, not a whole nested object
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        // status defaults to PENDING, createdAt defaults to now() — set in entity

        return orderRepository.save(order);
    }

    public Order assignDriver(Long orderId, Long driverId) {
        Order order = getOrderById(orderId);

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

        order.setDriver(driver);
        order.setStatus(OrderStatus.PREPARING);

        // Mark driver as unavailable
        driver.setAvailable(false);
        driverRepository.save(driver);

        return orderRepository.save(order);
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);

        // If delivered or cancelled, free up the driver
        if (newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELLED) {
            if (order.getDriver() != null) {
                order.getDriver().setAvailable(true);
                driverRepository.save(order.getDriver());
            }
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public void deleteOrder(Long id) {
        getOrderById(id);
        orderRepository.deleteById(id);
    }
}