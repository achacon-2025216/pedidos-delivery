package com.angelchacon.pedidos_delivery.controller;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.service.ComercioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private final ComercioService comercioService;

    public ComercioController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    // GET: Listar todos los comercios
    @GetMapping
    public ResponseEntity<List<Comercio>> listarComercios() {
        return ResponseEntity.ok(comercioService.obtenerTodos());
    }

    // PUT: Actualizar un comercio existente por su ID
    @PutMapping("/{id}")
    public ResponseEntity<Comercio> actualizarComercio(@PathVariable Long id, @RequestBody Comercio comercioDetalles) {
        Comercio comercioActualizado = comercioService.actualizar(id, comercioDetalles);
        if (comercioActualizado != null) {
            return ResponseEntity.ok(comercioActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE: Eliminar un comercio por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComercio(@PathVariable Long id) {
        boolean eliminado = comercioService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // Retorna un 204 No Content si se borró con éxito
        }
        return ResponseEntity.notFound().build(); // Retorna un 404 si el ID no existe
    }
}