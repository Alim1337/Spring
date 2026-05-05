package com.alim.spring_demo.dto;

import com.alim.spring_demo.entity.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestCreate {

    @NotNull(message = "Recipient type is required")
    private RecipientType recipientType; // REGISTERED, EMAIL_ONLY, MANUAL

    // Used when recipientType = REGISTERED
    private Long customerId;

    // Used when recipientType = EMAIL_ONLY or MANUAL
    private String recipientName;
    private String recipientPhone;
    private String recipientEmail; // required for EMAIL_ONLY

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotBlank(message = "Dropoff address is required")
    private String dropoffAddress;

    @NotBlank(message = "Item description is required")
    private String itemDescription;

    @NotNull(message = "Price is required")
    private Double price;
}