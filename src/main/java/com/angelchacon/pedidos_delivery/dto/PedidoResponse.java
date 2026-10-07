package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(Long id, Long clienteId, Long repartidorId, LocalDateTime fechaPedido,
                             BigDecimal costoEnvio, BigDecimal montoTotal, Pedido.EstadoPedido estado,
                             List<DetallePedidoResponse> detalles) {

    public static PedidoResponse from(Pedido p) {
        return new PedidoResponse(
                p.getId(),
                p.getCliente().getId(),
                p.getRepartidor() != null ? p.getRepartidor().getId() : null,
                p.getFechaPedido(),
                p.getCostoEnvio(),
                p.getMontoTotal(),
                p.getEstado(),
                p.getDetalles().stream().map(DetallePedidoResponse::from).toList());
    }
}