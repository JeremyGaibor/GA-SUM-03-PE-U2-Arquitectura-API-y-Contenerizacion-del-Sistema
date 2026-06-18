package com.tiendatech.notification_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String evento;
    private String mensaje;
    private LocalDateTime fecha;
}