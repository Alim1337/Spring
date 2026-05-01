package com.alim.spring_demo.mapper;

import org.springframework.stereotype.Component;

import com.alim.spring_demo.dto.DeliveryRequestResponse;
import com.alim.spring_demo.entity.DeliveryRequest;
import com.alim.spring_demo.repository.BusinessProfileRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryMapper {

    private final BusinessProfileRepository businessProfileRepository;

    public DeliveryRequestResponse toResponse(DeliveryRequest d) {
        DeliveryRequestResponse res = new DeliveryRequestResponse();
        res.setId(d.getId());
        res.setTrackingCode(d.getTrackingCode());

        // business name from profile or fallback to user name
        String businessName = businessProfileRepository
            .findByUser(d.getBusiness())
            .map(bp -> bp.getBusinessName())
            .orElse(d.getBusiness().getFirstName()
                + " " + d.getBusiness().getLastName());
        res.setBusinessName(businessName);

        // recipient — registered customer or manual info
        if (d.getCustomer() != null) {
            res.setCustomerName(d.getCustomer().getFirstName()
                + " " + d.getCustomer().getLastName());
            res.setCustomerPhone(d.getCustomer().getPhone());
            res.setCustomerEmail(d.getCustomer().getEmail());
            res.setCustomerRegistered(true);
        } else {
            res.setCustomerName(d.getRecipientName());
            res.setCustomerPhone(d.getRecipientPhone());
            res.setCustomerEmail(d.getRecipientEmail());
            res.setCustomerRegistered(false);
        }

        // driver
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