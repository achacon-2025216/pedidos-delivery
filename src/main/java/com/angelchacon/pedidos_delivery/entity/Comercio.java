package com.angelchacon.pedidos_delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Producto> productos;

    public enum Categoria {
        RESTAURANTE, SUPERMERCADO, FARMACIA
    }
}