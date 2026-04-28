package com.alim.spring_demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alim.spring_demo.dto.CustomerResponse;
import com.alim.spring_demo.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;

    // Business uses this to find customers to send deliveries to
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getCustomers() {
        return ResponseEntity.ok(
            customerRepository.findAll()
                .stream()
                .map(c -> {
                    CustomerResponse r = new CustomerResponse();
                    r.setId(c.getId());
                    r.setName(c.getName());
                    r.setEmail(c.getEmail());
                    r.setPhone(c.getPhone());
                    r.setAddress(c.getAddress());
                    return r;
                })
                .toList()
        );
    }
}