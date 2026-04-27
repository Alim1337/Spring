package com.alim.spring_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the business that created this delivery
    @ManyToOne
    @JoinColumn(name = "business_user_id", nullable = false)
    private User business;

    // the customer receiving the delivery
    @ManyToOne
    @JoinColumn(name = "customer_user_id", nullable = false)
    private User customer;

    // the driver assigned (null until accepted)
    @ManyToOne
    @JoinColumn(name = "driver_user_id")
    private User driver;

    @Column(nullable = false)
    private String pickupAddress;

    @Column(nullable = false)
    private String dropoffAddress;

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime acceptedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;

    // customer rates the delivery after completion
    private Integer rating;
}