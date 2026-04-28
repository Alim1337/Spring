package com.alim.spring_demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trackingCode;

    @ManyToOne
    @JoinColumn(name = "business_user_id", nullable = false)
    private User business;

    @ManyToOne
    @JoinColumn(name = "customer_user_id", nullable = false)
    private User customer;

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

    private Integer rating;
}