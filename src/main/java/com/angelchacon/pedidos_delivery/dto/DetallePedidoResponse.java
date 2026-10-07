package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.DetallePedido;

import java.math.BigDecimal;

public record DetallePedidoResponse(Long productoId, String productoNombre, Integer cantidad,
                                    BigDecimal precioUnitario, BigDecimal subtotal) {

    public static DetallePedidoResponse from(DetallePedido d) {
        return new DetallePedidoResponse(d.getProducto().getId(), d.getProducto().getNombre(),
                d.getCantidad(), d.getPrecioUnitario(), d.getSubtotal());
    }
}