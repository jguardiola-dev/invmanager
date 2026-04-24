-- InvManager - Script SQL
-- Nota: la aplicacion crea las tablas automaticamente al arrancar.
-- Este fichero se incluye solo como documentacion del esquema.

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS movimiento_stock;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS proveedor;

-- Categorias
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

-- Movimientos de stock (entradas y salidas)
CREATE TABLE movimiento_stock (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    producto_id INTEGER NOT NULL,
    tipo TEXT NOT NULL CHECK (tipo IN ('ENTRADA', 'SALIDA')),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    fecha TEXT NOT NULL,
    motivo TEXT,
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
);

-- Datos de prueba
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Electronica', 'Dispositivos electronicos'),
    ('Papeleria', 'Material de oficina'),
    ('Alimentacion', 'Productos alimentarios');

INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES
    ('Distribuciones Garcia S.L.', 'Ana Garcia', '961234567', 'pedidos@dgarcia.es'),
    ('Importaciones Mediterraneo', 'Luis Perez', '963987654', 'contacto@impmed.es');

INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id, proveedor_id) VALUES
    ('Teclado inalambrico', 'Teclado Bluetooth recargable', 29.95, 15, 5, 1, 1),
    ('Raton optico USB', 'Raton ergonomico', 9.50, 3, 10, 1, 1),
    ('Cuaderno A4 rayado', 'Cuaderno espiral 200 hojas', 4.25, 50, 20, 2, 2),
    ('Boligrafo azul', 'Pack 10 boligrafos', 3.00, 8, 15, 2, 2),
    ('Cafe molido 250g', 'Cafe natural tueste medio', 5.75, 25, 10, 3, 2);