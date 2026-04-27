package com.alim.spring_demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessProfileRequest {

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business address is required")
    private String businessAddress;

    private String businessPhone;
    private String description;
}