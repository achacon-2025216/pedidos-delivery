package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.dto.RegisterRequest;
import com.angelchacon.pedidos_delivery.dto.UsuarioResponse;
import com.angelchacon.pedidos_delivery.entity.Usuario;
import com.angelchacon.pedidos_delivery.exception.DuplicateResourceException;
import com.angelchacon.pedidos_delivery.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email " + request.email());
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setDireccion(request.direccion());
        usuario.setTelefono(request.telefono());
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(Usuario.Rol.CLIENTE); // rol predeterminado
        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }
}