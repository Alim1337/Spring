package com.alim.spring_demo.dto;

import lombok.Data;

@Data
public class DriverRequest {
    private String name;
    private String phone;
    private boolean available;
}