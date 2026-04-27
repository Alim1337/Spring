package com.alim.spring_demo.entity;

public enum DeliveryStatus {
    PENDING,        // created, waiting for driver
    ACCEPTED,       // driver accepted
    PICKED_UP,      // driver picked up items
    ON_THE_WAY,     // driver heading to customer
    DELIVERED,      // completed
    CANCELLED       // cancelled
}