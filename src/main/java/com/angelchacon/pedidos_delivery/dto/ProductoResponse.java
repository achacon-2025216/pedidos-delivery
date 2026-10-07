package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Producto;

import java.math.BigDecimal;

public record ProductoResponse(Long id, Long comercioId, String nombre, BigDecimal precio,
                               Integer stock, Boolean disponible) {

    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(p.getId(), p.getComercio().getId(), p.getNombre(),
                p.getPrecio(), p.getStock(), p.getDisponible());
    }
}