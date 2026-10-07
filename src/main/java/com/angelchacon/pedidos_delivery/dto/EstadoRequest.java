package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Pedido;
import jakarta.validation.constraints.NotNull;

public record EstadoRequest(@NotNull Pedido.EstadoPedido estado) {
}