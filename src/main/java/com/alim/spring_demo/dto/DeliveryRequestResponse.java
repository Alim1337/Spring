package com.alim.spring_demo.dto;

import java.time.LocalDateTime;

import com.alim.spring_demo.entity.DeliveryStatus;

import lombok.Data;

@Data
public class DeliveryRequestResponse {
    private Long id;
    private String businessName;
    private String customerName;
    private String customerPhone;
    private String driverName;
    private String driverPhone;
    private String pickupAddress;
    private String dropoffAddress;
    private String itemDescription;
    private Double price;
    private DeliveryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private Integer rating;
}