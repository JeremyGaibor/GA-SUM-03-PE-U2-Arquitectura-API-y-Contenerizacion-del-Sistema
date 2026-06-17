package com.tiendatech.resource_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductoResponse {

    private Long id;
    private String nombre;
    private String categoria;
    private String marca;
    private BigDecimal precio;
    private Integer stock;
    private LocalDate fechaRegistro;
    private Boolean activo;
}