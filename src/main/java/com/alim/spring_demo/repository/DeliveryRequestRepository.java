package com.alim.spring_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alim.spring_demo.entity.DeliveryRequest;
import com.alim.spring_demo.entity.DeliveryStatus;
import com.alim.spring_demo.entity.User;

@Repository
public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {
    List<DeliveryRequest> findByBusiness(User business);
    List<DeliveryRequest> findByCustomer(User customer);
    List<DeliveryRequest> findByDriver(User driver);
    List<DeliveryRequest> findByStatus(DeliveryStatus status);
}