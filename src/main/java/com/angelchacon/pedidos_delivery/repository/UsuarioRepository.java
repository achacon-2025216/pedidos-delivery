package com.angelchacon.pedidos_delivery.repository;

import com.angelchacon.pedidos_delivery.entity.Usuario; // o entity según cómo lo nombraste
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}