package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.config.JwtService;
import com.angelchacon.pedidos_delivery.dto.JwtResponse;
import com.angelchacon.pedidos_delivery.dto.LoginRequest;
import com.angelchacon.pedidos_delivery.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UsuarioRepository usuarioRepository,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public JwtResponse authenticate(LoginRequest request) {
        // Si las credenciales son incorrectas lanza BadCredentialsException (se responde 401)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        var jwtToken = jwtService.generateToken(org.springframework.security.core.userdetails.User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build());

        return JwtResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}