package com.angelchacon.pedidos_delivery.controller;

import com.angelchacon.pedidos_delivery.dto.EstadoRequest;
import com.angelchacon.pedidos_delivery.dto.PedidoRequest;
import com.angelchacon.pedidos_delivery.dto.PedidoResponse;
import com.angelchacon.pedidos_delivery.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request,
                                                Authentication authentication) {
        PedidoResponse respuesta = pedidoService.crear(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(Authentication authentication) {
        return ResponseEntity.ok(pedidoService.misPedidos(authentication.getName()));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PedidoResponse>> disponibles() {
        return ResponseEntity.ok(pedidoService.disponibles());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> actualizarEstado(@PathVariable Long id,
                                                           @Valid @RequestBody EstadoRequest request,
                                                           Authentication authentication) {
        return ResponseEntity.ok(
                pedidoService.actualizarEstado(id, request.estado(), authentication.getName()));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(pedidoService.cancelar(id, authentication.getName()));
    }
}