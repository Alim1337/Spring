package com.alim.spring_demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // Broadcast delivery status update to all subscribers
    public void broadcastDeliveryUpdate(Long deliveryId, String status,
                                         Double driverLat, Double driverLng) {
        messagingTemplate.convertAndSend(
            "/topic/delivery/" + deliveryId,
            Map.of(
                "deliveryId", deliveryId,
                "status", status,
                "driverLat", driverLat != null ? driverLat : 0,
                "driverLng", driverLng != null ? driverLng : 0,
                "timestamp", System.currentTimeMillis()
            )
        );
    }

    // Broadcast to a specific user
    public void notifyUser(String email, String message, String type) {
        messagingTemplate.convertAndSend(
            "/topic/user/" + email.replace("@", "-").replace(".", "-"),
            Map.of(
                "message", message,
                "type", type,
                "timestamp", System.currentTimeMillis()
            )
        );
    }
}