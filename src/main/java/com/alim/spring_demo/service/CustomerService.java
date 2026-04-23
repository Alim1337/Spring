package com.alim.spring_demo.service;

import com.alim.spring_demo.entity.Customer;
import com.alim.spring_demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                    // tells Spring: this is a service bean, manage it
@RequiredArgsConstructor    // Lombok: generates constructor-based injection
public class CustomerService {

    // Spring injects this automatically — you never call new CustomerRepository()
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existing = getCustomerById(id);  // reuse our method above
        existing.setName(updatedCustomer.getName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhone(updatedCustomer.getPhone());
        existing.setAddress(updatedCustomer.getAddress());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        getCustomerById(id);  // will throw if not found
        customerRepository.deleteById(id);
    }
}