package com.alim.spring_demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.alim.spring_demo.entity.Role;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@delivery.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@delivery.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("System");
            admin.setPhone("0000000000");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Admin created: admin@delivery.com / admin123");
        }
    }
}