package com.alim.spring_demo.mapper;

import org.springframework.stereotype.Component;

import com.alim.spring_demo.dto.OrderResponse;
import com.alim.spring_demo.entity.Order;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomer().getName());
        response.setDriverName(
            order.getDriver() != null ? order.getDriver().getName() : null
        );
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}