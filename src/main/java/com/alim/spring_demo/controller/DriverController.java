package com.alim.spring_demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alim.spring_demo.dto.DriverRequest;
import com.alim.spring_demo.dto.DriverResponse;
import com.alim.spring_demo.mapper.DriverMapper;
import com.alim.spring_demo.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        List<DriverResponse> response = driverService.getAllDrivers()
                .stream()
                .map(driverMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(
                driverMapper.toResponse(
                        driverService.getDriverById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(
            @RequestBody DriverRequest request) {

        var driver = driverMapper.toEntity(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        driverMapper.toResponse(
                                driverService.createDriver(driver)
                        )
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long id,
            @RequestBody DriverRequest request) {

        var driver = driverMapper.toEntity(request);

        return ResponseEntity.ok(
                driverMapper.toResponse(
                        driverService.updateDriver(id, driver)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}