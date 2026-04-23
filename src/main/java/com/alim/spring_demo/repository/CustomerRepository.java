package com.alim.spring_demo.repository;

import com.alim.spring_demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // JpaRepository gives you: save(), findById(), findAll(), deleteById()...
    // all for FREE — you write zero SQL
}