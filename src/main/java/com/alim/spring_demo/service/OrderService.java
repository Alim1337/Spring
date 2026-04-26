package com.alim.spring_demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alim.spring_demo.entity.Order;
import com.alim.spring_demo.entity.OrderStatus;
import com.alim.spring_demo.exception.InvalidOperationException;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.CustomerRepository;
import com.alim.spring_demo.repository.DriverRepository;
import com.alim.spring_demo.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // new paginated version
    public Page<Order> getAllOrdersPaged(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + id));
    }

    public Order createOrder(Long customerId, String deliveryAddress) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + customerId));
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(deliveryAddress);
        return orderRepository.save(order);
    }

    public Order assignDriver(Long orderId, Long driverId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED ||
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException(
                "Cannot assign driver to a " + order.getStatus() + " order");
        }

        var driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with id: " + driverId));

        if (!driver.isAvailable()) {
            throw new InvalidOperationException(
                "Driver " + driver.getName() + " is not available");
        }

        order.setDriver(driver);
        order.setStatus(OrderStatus.PREPARING);
        driver.setAvailable(false);
        driverRepository.save(driver);
        return orderRepository.save(order);
    }

    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);
        order.setStatus(newStatus);
        if (newStatus == OrderStatus.DELIVERED ||
            newStatus == OrderStatus.CANCELLED) {
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