package com.alim.spring_demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alim.spring_demo.dto.BusinessProfileRequest;
import com.alim.spring_demo.dto.DriverProfileRequest;
import com.alim.spring_demo.entity.BusinessProfile;
import com.alim.spring_demo.entity.DriverProfile;
import com.alim.spring_demo.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/driver")
    public ResponseEntity<DriverProfile> saveDriverProfile(
            @Valid @RequestBody DriverProfileRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            profileService.saveDriverProfile(userDetails.getUsername(), req));
    }

    @GetMapping("/driver")
    public ResponseEntity<DriverProfile> getDriverProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            profileService.getDriverProfile(userDetails.getUsername()));
    }

    @PostMapping("/business")
    public ResponseEntity<BusinessProfile> saveBusinessProfile(
            @Valid @RequestBody BusinessProfileRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            profileService.saveBusinessProfile(userDetails.getUsername(), req));
    }

    @GetMapping("/business")
    public ResponseEntity<BusinessProfile> getBusinessProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
            profileService.getBusinessProfile(userDetails.getUsername()));
    }
}