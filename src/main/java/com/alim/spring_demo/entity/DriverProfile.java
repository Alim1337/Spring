package com.alim.spring_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehiclePlate;
    private boolean available = true;
    private double rating = 0.0;
    private int totalDeliveries = 0;
    private Double currentLatitude;
    private Double currentLongitude;
}