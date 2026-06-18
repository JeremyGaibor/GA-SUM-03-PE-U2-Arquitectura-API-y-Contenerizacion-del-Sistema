package com.tiendatech.notification_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tiendatech.notification_service.dto.NotificationRequest;
import com.tiendatech.notification_service.dto.NotificationResponse;
import com.tiendatech.notification_service.model.Notification;
import com.tiendatech.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse crear(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setEvento(request.getEvento());
        notification.setMensaje(request.getMensaje());
        notification.setFecha(LocalDateTime.now());

        Notification guardada = notificationRepository.save(notification);

        return toResponse(guardada);
    }

    public List<NotificationResponse> listar() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getEvento(),
                notification.getMensaje(),
                notification.getFecha()
        );
    }
}