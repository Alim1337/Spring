package com.alim.spring_demo.mapper;

import com.alim.spring_demo.dto.DriverRequest;
import com.alim.spring_demo.dto.DriverResponse;
import com.alim.spring_demo.entity.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public Driver toEntity(DriverRequest request) {
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setAvailable(request.isAvailable());
        return driver;
    }

    public DriverResponse toResponse(Driver driver) {
        DriverResponse response = new DriverResponse();
        response.setId(driver.getId());
        response.setName(driver.getName());
        response.setPhone(driver.getPhone());
        response.setAvailable(driver.isAvailable());
        return response;
    }
}