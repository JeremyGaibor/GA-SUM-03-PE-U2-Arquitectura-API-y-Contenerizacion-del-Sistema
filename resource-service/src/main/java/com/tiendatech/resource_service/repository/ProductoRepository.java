package com.tiendatech.resource_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendatech.resource_service.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();
}