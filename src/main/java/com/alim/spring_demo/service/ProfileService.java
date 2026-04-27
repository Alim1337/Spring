package com.alim.spring_demo.service;

import org.springframework.stereotype.Service;

import com.alim.spring_demo.dto.BusinessProfileRequest;
import com.alim.spring_demo.dto.DriverProfileRequest;
import com.alim.spring_demo.entity.BusinessProfile;
import com.alim.spring_demo.entity.DriverProfile;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.BusinessProfileRepository;
import com.alim.spring_demo.repository.DriverProfileRepository;
import com.alim.spring_demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final BusinessProfileRepository businessProfileRepository;

    public DriverProfile saveDriverProfile(String email, DriverProfileRequest req) {
        User user = getUserByEmail(email);
        DriverProfile profile = driverProfileRepository.findByUser(user)
            .orElse(new DriverProfile());
        profile.setUser(user);
        profile.setVehicleType(req.getVehicleType());
        profile.setVehiclePlate(req.getVehiclePlate());
        return driverProfileRepository.save(profile);
    }

    public DriverProfile getDriverProfile(String email) {
        User user = getUserByEmail(email);
        return driverProfileRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));
    }

    public BusinessProfile saveBusinessProfile(String email, BusinessProfileRequest req) {
        User user = getUserByEmail(email);
        BusinessProfile profile = businessProfileRepository.findByUser(user)
            .orElse(new BusinessProfile());
        profile.setUser(user);
        profile.setBusinessName(req.getBusinessName());
        profile.setBusinessAddress(req.getBusinessAddress());
        profile.setBusinessPhone(req.getBusinessPhone());
        profile.setDescription(req.getDescription());
        return businessProfileRepository.save(profile);
    }

    public BusinessProfile getBusinessProfile(String email) {
        User user = getUserByEmail(email);
        return businessProfileRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Business profile not found"));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
    }
}