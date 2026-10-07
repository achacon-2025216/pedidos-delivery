package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.dto.ProductoRequest;
import com.angelchacon.pedidos_delivery.dto.ProductoResponse;
import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.entity.Producto;
import com.angelchacon.pedidos_delivery.exception.ResourceNotFoundException;
import com.angelchacon.pedidos_delivery.repository.ComercioRepository;
import com.angelchacon.pedidos_delivery.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ComercioRepository comercioRepository;

    public ProductoService(ProductoRepository productoRepository, ComercioRepository comercioRepository) {
        this.productoRepository = productoRepository;
        this.comercioRepository = comercioRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorComercio(Long comercioId) {
        if (!comercioRepository.existsById(comercioId)) {
            throw new ResourceNotFoundException("Comercio no encontrado: " + comercioId);
        }
        return productoRepository.findByComercioId(comercioId).stream()
                .map(ProductoResponse::from)
                .toList();
    }

    @Transactional
    public ProductoResponse crear(Long comercioId, ProductoRequest request) {
        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio no encontrado: " + comercioId));

        Producto producto = new Producto();
        producto.setComercio(comercio);
        producto.setNombre(request.nombre());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setDisponible(request.disponible() == null || request.disponible());
        return ProductoResponse.from(productoRepository.save(producto));
    }
}