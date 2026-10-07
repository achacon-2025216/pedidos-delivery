package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.dto.ComercioRequest;
import com.angelchacon.pedidos_delivery.dto.ComercioResponse;
import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.repository.ComercioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComercioService {

    private final ComercioRepository comercioRepository;

    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    @Transactional(readOnly = true)
    public List<ComercioResponse> listarActivos(Comercio.Categoria categoria) {
        List<Comercio> comercios = (categoria == null)
                ? comercioRepository.findByAbiertoTrue()
                : comercioRepository.findByAbiertoTrueAndCategoria(categoria);
        return comercios.stream().map(ComercioResponse::from).toList();
    }

    @Transactional
    public ComercioResponse crear(ComercioRequest request) {
        Comercio comercio = new Comercio();
        comercio.setNombre(request.nombre());
        comercio.setCategoria(request.categoria());
        comercio.setDireccion(request.direccion());
        comercio.setAbierto(request.abierto() == null || request.abierto());
        return ComercioResponse.from(comercioRepository.save(comercio));
    }
}