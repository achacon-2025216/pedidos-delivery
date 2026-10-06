package com.angelchacon.pedidos_delivery.controller;

import com.angelchacon.pedidos_delivery.dto.JwtResponse;
import com.angelchacon.pedidos_delivery.dto.LoginRequest;
import com.angelchacon.pedidos_delivery.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}