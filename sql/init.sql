-- InvManager - Base de datos
-- SQLite 3

PRAGMA foreign_keys = ON;

-- Borrar tablas si existen (orden inverso por las FK)
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS proveedor;

-- Categorías
CREATE TABLE categoria (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

-- Proveedores
CREATE TABLE proveedor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    contacto TEXT,
    telefono TEXT,
    email TEXT
);

-- Productos
CREATE TABLE producto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    precio REAL NOT NULL CHECK (precio > 0),
    stock_actual INTEGER NOT NULL CHECK (stock_actual >= 0),
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    categoria_id INTEGER,
    proveedor_id INTEGER,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id) ON DELETE RESTRICT
);

-- Datos de prueba
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Electrónica', 'Dispositivos electrónicos'),
    ('Papelería', 'Material de oficina'),
    ('Alimentación', 'Productos alimentarios');

INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES
    ('Distribuciones García S.L.', 'Ana García', '961234567', 'pedidos@dgarcia.es'),
    ('Importaciones Mediterráneo', 'Luis Pérez', '963987654', 'contacto@impmed.es');

INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id, proveedor_id) VALUES
    ('Teclado inalámbrico', 'Teclado Bluetooth recargable', 29.95, 15, 5, 1, 1),
    ('Ratón óptico USB', 'Ratón ergonómico', 9.50, 3, 10, 1, 1),
    ('Cuaderno A4 rayado', 'Cuaderno espiral 200 hojas', 4.25, 50, 20, 2, 2),
    ('Bolígrafo azul', 'Pack 10 bolígrafos', 3.00, 8, 15, 2, 2),
    ('Café molido 250g', 'Café natural tueste medio', 5.75, 25, 10, 3, 2);