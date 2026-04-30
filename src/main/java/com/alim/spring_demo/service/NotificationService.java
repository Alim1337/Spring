package com.alim.spring_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alim.spring_demo.entity.Notification;
import com.alim.spring_demo.entity.User;
import com.alim.spring_demo.exception.ResourceNotFoundException;
import com.alim.spring_demo.repository.NotificationRepository;
import com.alim.spring_demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void send(User user, String message, String type) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setType(type);
        notificationRepository.save(n);
    }

    public List<Notification> getForUser(String email) {
        User user = getUserByEmail(email);
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public long getUnreadCount(String email) {
        User user = getUserByEmail(email);
        return notificationRepository.countByUserAndReadFalse(user);
    }

    public void markAllRead(String email) {
        User user = getUserByEmail(email);
        List<Notification> notifications =
            notificationRepository.findByUserOrderByCreatedAtDesc(user);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}