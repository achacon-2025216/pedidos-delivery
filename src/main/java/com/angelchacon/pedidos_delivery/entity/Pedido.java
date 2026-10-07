package com.angelchacon.pedidos_delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repartidor_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Usuario repartidor; // Nullable al crearse

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DetallePedido> detalles = new ArrayList<>();

    public enum EstadoPedido {
        PENDIENTE, EN_PREPARACION, EN_CAMINO, ENTREGADO, CANCELADO
    }

    @PrePersist
    public void prePersist() {
        this.fechaPedido = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoPedido.PENDIENTE;
        }
        if (this.costoEnvio == null) {
            this.costoEnvio = new BigDecimal("20.00"); // Envío fijo Q20.00
        }
    }
}