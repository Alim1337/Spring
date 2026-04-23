package com.alim.spring_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alim.spring_demo.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring reads this method name and generates the SQL automatically:
    // SELECT * FROM orders WHERE customer_id = ?
    List<Order> findByCustomerId(Long customerId);
}