package com.alim.spring_demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alim.spring_demo.dto.DeliveryRequestCreate;
import com.alim.spring_demo.dto.DeliveryRequestResponse;
import com.alim.spring_demo.entity.DeliveryStatus;
import com.alim.spring_demo.mapper.DeliveryMapper;
import com.alim.spring_demo.service.DeliveryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    // BUSINESS: create delivery
    @PostMapping
    public ResponseEntity<DeliveryRequestResponse> create(
            @Valid @RequestBody DeliveryRequestCreate req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            deliveryMapper.toResponse(
                deliveryService.createDelivery(req, userDetails.getUsername())));
    }

    // BUSINESS: my deliveries
    @GetMapping("/my-business")
    public ResponseEntity<List<DeliveryRequestResponse>> getBusinessDeliveries(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deliveryService.getBusinessDeliveries(userDetails.getUsername())
                .stream().map(deliveryMapper::toResponse).toList());
    }

    // CUSTOMER: my incoming deliveries
    @GetMapping("/my-deliveries")
    public ResponseEntity<List<DeliveryRequestResponse>> getCustomerDeliveries(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deliveryService.getCustomerDeliveries(userDetails.getUsername())
                .stream().map(deliveryMapper::toResponse).toList());
    }

    // DRIVER: available deliveries to accept
    @GetMapping("/available")
    public ResponseEntity<List<DeliveryRequestResponse>> getAvailable() {
        return ResponseEntity.ok(
            deliveryService.getAvailableDeliveries()
                .stream().map(deliveryMapper::toResponse).toList());
    }

    // DRIVER: my active deliveries
    @GetMapping("/my-active")
    public ResponseEntity<List<DeliveryRequestResponse>> getDriverDeliveries(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deliveryService.getDriverDeliveries(userDetails.getUsername())
                .stream().map(deliveryMapper::toResponse).toList());
    }

    // DRIVER: accept a delivery
    @PatchMapping("/{id}/accept")
    public ResponseEntity<DeliveryRequestResponse> accept(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deliveryMapper.toResponse(
                deliveryService.acceptDelivery(id, userDetails.getUsername())));
    }

    // DRIVER: update status
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryRequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        DeliveryStatus status = DeliveryStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(
            deliveryMapper.toResponse(
                deliveryService.updateStatus(id, status, userDetails.getUsername())));
    }

    // CUSTOMER: rate delivery
    @PostMapping("/{id}/rate")
    public ResponseEntity<DeliveryRequestResponse> rate(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            deliveryMapper.toResponse(
                deliveryService.rateDelivery(
                    id, body.get("rating"), userDetails.getUsername())));
    }

    // DRIVER: update location
    @PatchMapping("/location")
    public ResponseEntity<Void> updateLocation(
            @RequestBody Map<String, Double> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        deliveryService.updateLocation(
            userDetails.getUsername(),
            body.get("latitude"),
            body.get("longitude"));
        return ResponseEntity.ok().build();
    }
}