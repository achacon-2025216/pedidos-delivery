package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComercioRequest(
        @NotBlank String nombre,
        @NotNull Comercio.Categoria categoria,
        @NotBlank String direccion,
        Boolean abierto
) {
}