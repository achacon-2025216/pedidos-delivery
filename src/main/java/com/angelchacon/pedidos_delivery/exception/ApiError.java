package com.angelchacon.pedidos_delivery.exception;

import java.time.LocalDateTime;

public record ApiError(int status, String error, String mensaje, LocalDateTime timestamp) {

    public static ApiError of(int status, String error, String mensaje) {
        return new ApiError(status, error, mensaje, LocalDateTime.now());
    }
}