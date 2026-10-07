package com.angelchacon.pedidos_delivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String nombre,
        @NotBlank String direccion,
        @NotBlank String telefono,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password
) {
}