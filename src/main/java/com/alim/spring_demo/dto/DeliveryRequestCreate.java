package com.alim.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeliveryRequestCreate {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @NotBlank(message = "Dropoff address is required")
    private String dropoffAddress;

    @NotBlank(message = "Item description is required")
    private String itemDescription;

    @NotNull(message = "Price is required")
    private Double price;
}