package com.tiendatech.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

    @NotBlank(message = "El evento es obligatorio")
    private String evento;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}