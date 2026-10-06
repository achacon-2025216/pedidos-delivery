package com.angelchacon.pedidos_delivery.repository;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    List<Comercio> findByAbiertoTrue();
    List<Comercio> findByCategoriaAndAbiertoTrue(Comercio.Categoria categoria);
}