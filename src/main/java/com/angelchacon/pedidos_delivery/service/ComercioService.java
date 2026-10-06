package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.entity.Comercio;
import com.angelchacon.pedidos_delivery.repository.ComercioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {

    private final ComercioRepository comercioRepository;

    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    public List<Comercio> obtenerTodos() {
        return comercioRepository.findAll();
    }

    public Comercio actualizar(Long id, Comercio detalles) {
        Optional<Comercio> comercioOpt = comercioRepository.findById(id);
        if (comercioOpt.isPresent()) {
            Comercio comercio = comercioOpt.get();
            comercio.setNombre(detalles.getNombre());
            comercio.setCategoria(detalles.getCategoria());
            comercio.setDireccion(detalles.getDireccion());
            comercio.setAbierto(detalles.getAbierto());
            return comercioRepository.save(comercio);
        }
        return null;
    }

    public boolean eliminar(Long id) {
        if (comercioRepository.existsById(id)) {
            comercioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}