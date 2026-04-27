package com.alim.spring_demo.mapper;

import org.springframework.stereotype.Component;

import com.alim.spring_demo.dto.DeliveryRequestResponse;
import com.alim.spring_demo.entity.DeliveryRequest;

@Component
public class DeliveryMapper {

    public DeliveryRequestResponse toResponse(DeliveryRequest d) {
        DeliveryRequestResponse res = new DeliveryRequestResponse();
        res.setId(d.getId());
        res.setBusinessName(d.getBusiness().getFirstName()
            + " " + d.getBusiness().getLastName());
        res.setCustomerName(d.getCustomer().getFirstName()
            + " " + d.getCustomer().getLastName());
        res.setCustomerPhone(d.getCustomer().getPhone());
        res.setDriverName(d.getDriver() != null
            ? d.getDriver().getFirstName() + " " + d.getDriver().getLastName()
            : null);
        res.setDriverPhone(d.getDriver() != null
            ? d.getDriver().getPhone() : null);
        res.setPickupAddress(d.getPickupAddress());
        res.setDropoffAddress(d.getDropoffAddress());
        res.setItemDescription(d.getItemDescription());
        res.setPrice(d.getPrice());
        res.setStatus(d.getStatus());
        res.setCreatedAt(d.getCreatedAt());
        res.setAcceptedAt(d.getAcceptedAt());
        res.setPickedUpAt(d.getPickedUpAt());
        res.setDeliveredAt(d.getDeliveredAt());
        res.setRating(d.getRating());
        return res;
    }
}