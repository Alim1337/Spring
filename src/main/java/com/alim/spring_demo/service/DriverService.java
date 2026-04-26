package com.alim.spring_demo.service;

import com.alim.spring_demo.entity.Driver;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver not found with id: " + id));
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver existing = getDriverById(id);
        existing.setName(updatedDriver.getName());
        existing.setPhone(updatedDriver.getPhone());
        existing.setAvailable(updatedDriver.isAvailable());
        return driverRepository.save(existing);
    }

    public void deleteDriver(Long id) {
        getDriverById(id);
        driverRepository.deleteById(id);
    }
}