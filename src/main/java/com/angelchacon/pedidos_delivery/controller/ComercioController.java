package com.angelchacon.pedidos_delivery.controller;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.repository.ComercioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private final ComercioRepository comercioRepository;

    public ComercioController(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    // 1. GET: Listar todos
    @GetMapping
    public List<Comercio> listarComercios() {
        return comercioRepository.findAll();
    }

    // 2. GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Comercio> obtenerComercioPorId(@PathVariable Long id) {
        return comercioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST: Crear un nuevo registro
    @PostMapping
    public ResponseEntity<Comercio> crearComercio(@RequestBody Comercio comercio) {
        return ResponseEntity.ok(comercioRepository.save(comercio));
    }

    // 4. PUT: Actualizar un registro existente
    @PutMapping("/{id}")
    public ResponseEntity<Comercio> actualizarComercio(@PathVariable Long id, @RequestBody Comercio comercioDetalles) {
        return comercioRepository.findById(id).map(comercioExistente -> {
            // Actualiza los campos necesarios según tu entidad Comercio
            comercioExistente.setNombre(comercioDetalles.getNombre());
            // comercioExistente.setDireccion(comercioDetalles.getDireccion()); // Agrega los campos que tenga tu entidad

            Comercio actualizado = comercioRepository.save(comercioExistente);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE: Eliminar un registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComercio(@PathVariable Long id) {
        if (comercioRepository.existsById(id)) {
            comercioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}