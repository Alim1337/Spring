package com.alim.spring_demo.dto;

import java.time.LocalDateTime;

import com.alim.spring_demo.entity.DeliveryStatus;
import com.alim.spring_demo.entity.RecipientType;

import lombok.Data;

@Data
public class DeliveryRequestResponse {
    private Long id;
    private String trackingCode;
    private String businessName;

    // Recipient info (works for all 3 types)
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private RecipientType recipientType;

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