package com.alim.spring_demo.dto;

import java.time.LocalDateTime;

import com.alim.spring_demo.entity.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String customerName;
    private String driverName;      // null if not assigned yet
    private String deliveryAddress;
    private OrderStatus status;
    private LocalDateTime createdAt;
}