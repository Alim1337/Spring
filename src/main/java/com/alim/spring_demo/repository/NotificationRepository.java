package com.alim.spring_demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alim.spring_demo.entity.Notification;
import com.alim.spring_demo.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    long countByUserAndReadFalse(User user);
}