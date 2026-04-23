package com.alim.spring_demo.service;

import com.alim.spring_demo.entity.Customer;
import com.alim.spring_demo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // use Mockito — no Spring context needed
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;  // fake repository

    @InjectMocks
    private CustomerService customerService;        // real service with fake repo injected

    @Test
    void getCustomerById_returnsCustomer_whenExists() {
        // Arrange — set up what the fake repo will return
        Customer customer = new Customer(1L, "Alim", "alim@test.com", "0555", "Algiers");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act — call the real method
        Customer result = customerService.getCustomerById(1L);

        // Assert — check the result
        assertThat(result.getName()).isEqualTo("Alim");
        assertThat(result.getEmail()).isEqualTo("alim@test.com");
    }

    @Test
    void getCustomerById_throwsException_whenNotFound() {
        // Arrange — fake repo returns nothing
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Assert — calling the service throws our RuntimeException
        assertThatThrownBy(() -> customerService.getCustomerById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void createCustomer_savesAndReturnsCustomer() {
        Customer input = new Customer(null, "Sara", "sara@test.com", "0666", "Oran");
        Customer saved = new Customer(1L, "Sara", "sara@test.com", "0666", "Oran");

        when(customerRepository.save(input)).thenReturn(saved);

        Customer result = customerService.createCustomer(input);

        assertThat(result.getId()).isEqualTo(1L);
        verify(customerRepository, times(1)).save(input); // verify save was called once
    }
}