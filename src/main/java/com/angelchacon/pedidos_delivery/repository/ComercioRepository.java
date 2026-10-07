package com.angelchacon.pedidos_delivery.repository;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    List<Comercio> findByAbiertoTrue();

    List<Comercio> findByAbiertoTrueAndCategoria(Comercio.Categoria categoria);
}