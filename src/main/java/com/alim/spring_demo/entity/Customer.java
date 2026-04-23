package com.alim.spring_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity                         // tells Spring: map this class to a DB table
@Table(name = "customers")      // the table will be named "customers"
@Data                           // Lombok: generates getters, setters, toString
@NoArgsConstructor              // Lombok: generates empty constructor
@AllArgsConstructor             // Lombok: generates constructor with all fields
public class Customer {

    @Id                                                 // this is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;
}