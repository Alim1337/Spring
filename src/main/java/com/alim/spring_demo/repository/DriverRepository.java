package com.alim.spring_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alim.spring_demo.entity.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}