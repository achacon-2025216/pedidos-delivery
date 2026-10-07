package com.angelchacon.pedidos_delivery.repository;

import com.angelchacon.pedidos_delivery.entity.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "repartidor", "detalles", "detalles.producto"})
    List<Pedido> findByClienteIdOrderByFechaPedidoDesc(Long clienteId);

    @EntityGraph(attributePaths = {"cliente", "repartidor", "detalles", "detalles.producto"})
    List<Pedido> findByEstadoInOrderByFechaPedidoAsc(Collection<Pedido.EstadoPedido> estados);
}