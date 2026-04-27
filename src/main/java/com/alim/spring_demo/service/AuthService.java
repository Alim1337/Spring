package com.alim.spring_demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alim.spring_demo.dto.AuthResponse;
import com.alim.spring_demo.dto.LoginRequest;
import com.alim.spring_demo.dto.RegisterRequest;
import com.alim.spring_demo.entity.Customer;
import com.alim.spring_demo.entity.Role;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.exception.DuplicateResourceException;
import com.alim.spring_demo.repository.CustomerRepository;
import com.alim.spring_demo.repository.UserRepository;
import com.alim.spring_demo.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository; // ← ajouté
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException(
                "Email already registered: " + request.getEmail());
        }

        if (request.getRole() == Role.ADMIN) {
            throw new DuplicateResourceException("Cannot register as ADMIN");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        userRepository.save(user);

        // ← auto-créer un Customer si le rôle est CUSTOMER
        if (request.getRole() == Role.CUSTOMER) {
            Customer customer = new Customer();
            customer.setName(request.getFirstName() + " " + request.getLastName());
            customer.setEmail(request.getEmail());
            customer.setPhone(request.getPhone());
            customer.setAddress(""); // à compléter plus tard dans le profil
            customerRepository.save(customer);
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(
            token,
            user.getEmail(),
            user.getRole().name(),
            user.getFirstName(),
            user.getLastName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(
            token,
            user.getEmail(),
            user.getRole().name(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}