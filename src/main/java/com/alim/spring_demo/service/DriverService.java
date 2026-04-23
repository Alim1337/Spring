package com.alim.spring_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alim.spring_demo.entity.Driver;
import com.alim.spring_demo.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

    // ✅ FIX: variable name should start lowercase
    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver existing = getDriverById(id);

        // ✅ FIX: match your model (name, phone, available)
        existing.setName(updatedDriver.getName());
        existing.setPhone(updatedDriver.getPhone());
        existing.setAvailable(updatedDriver.isAvailable());

        return driverRepository.save(existing);
    }

    public void deleteDriver(Long id) {
        getDriverById(id); // ensure exists
        driverRepository.deleteById(id);
    }
}