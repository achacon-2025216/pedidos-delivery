package com.angelchacon.pedidos_delivery.repository;

import com.angelchacon.pedidos_delivery.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByComercioIdAndDisponibleTrue(Long comercioId);
}