package com.angelchacon.pedidos_delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comercios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Boolean abierto = true;

    public enum Categoria {
        RESTAURANTE, SUPERMERCADO, FARMACIA
    }
}