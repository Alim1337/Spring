package com.alim.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestCreate {

    // ALL recipient fields are optional
    private Long customerId;          // registered customer
    private String recipientName;     // unregistered recipient name
    private String recipientEmail;    // unregistered recipient email
    private String recipientPhone;    // unregistered recipient phone

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotBlank(message = "Dropoff address is required")
    private String dropoffAddress;

    @NotBlank(message = "Item description is required")
    private String itemDescription;

    @NotNull(message = "Price is required")
    private Double price;
}