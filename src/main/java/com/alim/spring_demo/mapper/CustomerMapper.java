package com.alim.spring_demo.mapper;

import com.alim.spring_demo.dto.CustomerRequest;
import com.alim.spring_demo.dto.CustomerResponse;
import com.alim.spring_demo.entity.Customer;
import org.springframework.stereotype.Component;

@Component  // Spring manages this as a bean so we can inject it
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        return response;
    }
}