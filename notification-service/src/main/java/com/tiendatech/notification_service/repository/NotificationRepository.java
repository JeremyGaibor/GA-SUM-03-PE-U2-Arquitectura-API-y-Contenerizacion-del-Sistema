package com.tiendatech.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendatech.notification_service.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}