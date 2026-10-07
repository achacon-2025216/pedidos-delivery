package com.angelchacon.pedidos_delivery.service;

import com.angelchacon.pedidos_delivery.dto.ItemPedidoRequest;
import com.angelchacon.pedidos_delivery.dto.PedidoRequest;
import com.angelchacon.pedidos_delivery.dto.PedidoResponse;
import com.angelchacon.pedidos_delivery.entity.DetallePedido;
import com.angelchacon.pedidos_delivery.entity.Pedido;
import com.angelchacon.pedidos_delivery.entity.Pedido.EstadoPedido;
import com.angelchacon.pedidos_delivery.entity.Producto;
import com.angelchacon.pedidos_delivery.entity.Usuario;
import com.angelchacon.pedidos_delivery.exception.InsufficientStockException;
import com.angelchacon.pedidos_delivery.exception.InvalidStatusException;
import com.angelchacon.pedidos_delivery.exception.ResourceNotFoundException;
import com.angelchacon.pedidos_delivery.repository.PedidoRepository;
import com.angelchacon.pedidos_delivery.repository.ProductoRepository;
import com.angelchacon.pedidos_delivery.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PedidoService {

    private static final BigDecimal COSTO_ENVIO = new BigDecimal("20.00");

    // Flujo estricto: cada estado solo puede avanzar al siguiente
    private static final Map<EstadoPedido, EstadoPedido> SIGUIENTE_ESTADO = Map.of(
            EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION,
            EstadoPedido.EN_PREPARACION, EstadoPedido.EN_CAMINO,
            EstadoPedido.EN_CAMINO, EstadoPedido.ENTREGADO
    );

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoRepository productoRepository,
                         UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Crea el pedido. Si algún producto no tiene stock suficiente se lanza una
     * RuntimeException y @Transactional revierte TODO (incluido el stock ya descontado).
     * El total se calcula siempre en el servidor con el precio actual del producto.
     */
    @Transactional
    public PedidoResponse crear(PedidoRequest request, String emailCliente) {
        Usuario cliente = buscarUsuario(emailCliente);

        // Une productos repetidos y los ordena por id (evita deadlocks al bloquear filas)
        Map<Long, Integer> cantidades = new TreeMap<>();
        for (ItemPedidoRequest item : request.items()) {
            cantidades.merge(item.productoId(), item.cantidad(), Integer::sum);
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setCostoEnvio(COSTO_ENVIO);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        BigDecimal totalProductos = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entrada : cantidades.entrySet()) {
            Producto producto = productoRepository.findByIdForUpdate(entrada.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado: " + entrada.getKey()));
            int cantidad = entrada.getValue();

            if (!Boolean.TRUE.equals(producto.getDisponible())) {
                throw new InsufficientStockException(
                        "El producto '" + producto.getNombre() + "' no está disponible");
            }
            if (producto.getStock() < cantidad) {
                throw new InsufficientStockException(
                        "Stock insuficiente para '" + producto.getNombre()
                                + "'. Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
            }

            producto.setStock(producto.getStock() - cantidad);

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);
            pedido.getDetalles().add(detalle);

            totalProductos = totalProductos.add(subtotal);
        }

        pedido.setMontoTotal(totalProductos.add(COSTO_ENVIO));
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> misPedidos(String emailCliente) {
        Usuario cliente = buscarUsuario(emailCliente);
        return pedidoRepository.findByClienteIdOrderByFechaPedidoDesc(cliente.getId())
                .stream().map(PedidoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> disponibles() {
        return pedidoRepository.findByEstadoInOrderByFechaPedidoAsc(
                        List.of(EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.EN_CAMINO))
                .stream().map(PedidoResponse::from).toList();
    }

    @Transactional
    public PedidoResponse actualizarEstado(Long pedidoId, EstadoPedido nuevoEstado, String email) {
        Pedido pedido = buscarPedido(pedidoId);
        Usuario usuario = buscarUsuario(email);
        EstadoPedido actual = pedido.getEstado();

        if (SIGUIENTE_ESTADO.get(actual) != nuevoEstado) {
            throw new InvalidStatusException(
                    "Transición no permitida: " + actual + " -> " + nuevoEstado);
        }

        // El primer repartidor que mueve el pedido queda asignado
        if (usuario.getRol() == Usuario.Rol.REPARTIDOR && pedido.getRepartidor() == null) {
            pedido.setRepartidor(usuario);
        }

        pedido.setEstado(nuevoEstado);
        return PedidoResponse.from(pedido);
    }

    @Transactional
    public PedidoResponse cancelar(Long pedidoId, String email) {
        Pedido pedido = buscarPedido(pedidoId);
        Usuario usuario = buscarUsuario(email);

        boolean esAdmin = usuario.getRol() == Usuario.Rol.ADMIN;
        if (!esAdmin && !pedido.getCliente().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Solo puedes cancelar tus propios pedidos");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new InvalidStatusException(
                    "Solo se puede cancelar un pedido PENDIENTE. Estado actual: " + pedido.getEstado());
        }

        // Restaura el stock de cada producto del pedido
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = productoRepository.findByIdForUpdate(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado: " + detalle.getProducto().getId()));
            producto.setStock(producto.getStock() + detalle.getCantidad());
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        return PedidoResponse.from(pedido);
    }

    private Usuario buscarUsuario(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + email));
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }
}