package com.angelchacon.pedidos_delivery.dto;

import com.angelchacon.pedidos_delivery.entity.Usuario;

public record UsuarioResponse(Long id, String nombre, String email, Usuario.Rol rol) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol());
    }
}