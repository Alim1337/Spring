package com.alim.spring_demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alim.spring_demo.dto.CustomerResponse;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.CustomerRepository;
import com.alim.spring_demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

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

    // Get current logged in user info
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "phone", user.getPhone(),
            "role", user.getRole().name()
        ));
    }

    // Update current user basic info
    @PutMapping("/me")
    public ResponseEntity<Map<String, Object>> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (body.containsKey("firstName")) user.setFirstName(body.get("firstName"));
        if (body.containsKey("lastName")) user.setLastName(body.get("lastName"));
        if (body.containsKey("phone")) user.setPhone(body.get("phone"));

        userRepository.save(user);

        // update localStorage firstName hint
        return ResponseEntity.ok(Map.of(
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "phone", user.getPhone(),
            "email", user.getEmail(),
            "role", user.getRole().name()
        ));
    }
}