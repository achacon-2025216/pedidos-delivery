-- Insertar Usuarios de prueba (evitando duplicados si ya existen)
INSERT INTO usuarios (id, nombre, direccion, telefono, email, password, rol)
VALUES
(1, 'Administrador General', 'Zona 1, Ciudad de Guatemala', '5555-1111', 'admin@delivery.com', '$2a$10$DxJ.3P8q5VlK0hQ1K6fL2eRz9sF8xGv8hYqJ4Wp5tN1m3bC8v7z2y', 'ADMIN'),
(2, 'Repartidor Rápido', 'Zona 4, Ciudad de Guatemala', '5555-2222', 'repartidor@delivery.com', '$2a$10$DxJ.3P8q5VlK0hQ1K6fL2eRz9sF8xGv8hYqJ4Wp5tN1m3bC8v7z2y', 'REPARTIDOR'),
(3, 'Cliente Frecuente', 'Zona 10, Ciudad de Guatemala', '5555-3333', 'cliente@delivery.com', '$2a$10$DxJ.3P8q5VlK0hQ1K6fL2eRz9sF8xGv8hYqJ4Wp5tN1m3bC8v7z2y', 'CLIENTE')
ON CONFLICT (id) DO NOTHING;

-- Insertar Comercio de prueba
INSERT INTO comercios (id, nombre, categoria, direccion, abierto)
VALUES
(1, 'Pollo Kinal', 'RESTAURANTE', 'Zona 7, Kinal', true)
ON CONFLICT (id) DO NOTHING;

-- Insertar Productos de prueba
INSERT INTO productos (id, comercio_id, nombre, precio, stock, disponible)
VALUES
(1, 1, 'Menú Pollo Frijoles', 45.00, 25, true),
(2, 1, 'Refresco Natural', 10.00, 50, true)
ON CONFLICT (id) DO NOTHING;