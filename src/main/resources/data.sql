-- ======================================================================
-- Datos iniciales (se ejecuta en cada arranque; es idempotente)
-- Contraseñas (BCrypt):  admin123 | repartidor123 | cliente123
-- ======================================================================

-- Usuarios: sin id (lo genera la BD). Si el email ya existe, se actualizan datos y contraseña.
INSERT INTO usuarios (nombre, direccion, telefono, email, password, rol)
VALUES
('Administrador General', 'Zona 1, Ciudad de Guatemala', '5555-1111', 'admin@delivery.com',
 '$2a$10$QmW89eNWoi2gKkPcie7vSOoCiuY1aoO6MUicFhC4mWexMmQganpp.', 'ADMIN'),
('Repartidor Rápido', 'Zona 4, Ciudad de Guatemala', '5555-2222', 'repartidor@delivery.com',
 '$2a$10$VopkosDdD7tv7AERrKT1h.6VCnMInPTn1NXnsoL5MVU9nVLkQyfe6', 'REPARTIDOR'),
('Cliente Frecuente', 'Zona 10, Ciudad de Guatemala', '5555-3333', 'cliente@delivery.com',
 '$2a$10$RYR6lVkb0PDkQSEflnGO5OhKD1xo4LctV66LXCCPvl0g8auv97mVa', 'CLIENTE')
ON CONFLICT (email) DO UPDATE
SET nombre = EXCLUDED.nombre,
    direccion = EXCLUDED.direccion,
    telefono = EXCLUDED.telefono,
    password = EXCLUDED.password,
    rol = EXCLUDED.rol;

-- Comercio de prueba
INSERT INTO comercios (id, nombre, categoria, direccion, abierto)
VALUES (1, 'Pollo Kinal', 'RESTAURANTE', 'Zona 7, Kinal', true)
ON CONFLICT (id) DO NOTHING;

-- Productos de prueba
INSERT INTO productos (id, comercio_id, nombre, precio, stock, disponible)
VALUES
(1, 1, 'Menú Pollo Frijoles', 45.00, 25, true),
(2, 1, 'Refresco Natural', 10.00, 50, true)
ON CONFLICT (id) DO NOTHING;

-- Ajusta las secuencias para que los INSERT de la app no choquen con los ids manuales
SELECT setval(pg_get_serial_sequence('comercios', 'id'), (SELECT COALESCE(MAX(id), 1) FROM comercios));
SELECT setval(pg_get_serial_sequence('productos', 'id'), (SELECT COALESCE(MAX(id), 1) FROM productos));