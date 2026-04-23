package com.alim.spring_demo.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long customerId;
    private String deliveryAddress;
}