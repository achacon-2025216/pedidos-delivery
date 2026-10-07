package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Comercio;

public record ComercioResponse(Long id, String nombre, Comercio.Categoria categoria,
                               String direccion, Boolean abierto) {

    public static ComercioResponse from(Comercio c) {
        return new ComercioResponse(c.getId(), c.getNombre(), c.getCategoria(),
                c.getDireccion(), c.getAbierto());
    }
}