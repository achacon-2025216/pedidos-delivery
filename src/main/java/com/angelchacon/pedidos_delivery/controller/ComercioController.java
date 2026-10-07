package com.angelchacon.pedidos_delivery.controller;

import com.angelchacon.pedidos_delivery.dto.ComercioRequest;
import com.angelchacon.pedidos_delivery.dto.ComercioResponse;
import com.angelchacon.pedidos_delivery.dto.ProductoRequest;
import com.angelchacon.pedidos_delivery.dto.ProductoResponse;
import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.service.ComercioService;
import com.angelchacon.pedidos_delivery.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comercios")
public class ComercioController {

    private final ComercioService comercioService;
    private final ProductoService productoService;

    public ComercioController(ComercioService comercioService, ProductoService productoService) {
        this.comercioService = comercioService;
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ComercioResponse>> listar(
            @RequestParam(required = false) Comercio.Categoria categoria) {
        return ResponseEntity.ok(comercioService.listarActivos(categoria));
    }

    @PostMapping
    public ResponseEntity<ComercioResponse> crear(@Valid @RequestBody ComercioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comercioService.crear(request));
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<ProductoResponse>> listarProductos(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.listarPorComercio(id));
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<ProductoResponse> agregarProducto(@PathVariable Long id,
                                                            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(id, request));
    }
}