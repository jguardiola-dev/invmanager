-- ============================================================================
-- InvManager - Script de inicialización de base de datos
-- Motor: SQLite 3
-- Autor: Jorge Guardiola Tomás
-- ============================================================================

-- Activar claves foráneas (necesario en cada conexión en SQLite)
PRAGMA foreign_keys = ON;

-- ----------------------------------------------------------------------------
-- Limpieza de tablas existentes (orden inverso a creación por FK)
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS proveedor;

-- ----------------------------------------------------------------------------
-- Tabla: categoria
-- ----------------------------------------------------------------------------
CREATE TABLE categoria (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

-- ----------------------------------------------------------------------------
-- Tabla: proveedor
-- ----------------------------------------------------------------------------
CREATE TABLE proveedor (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre    TEXT NOT NULL,
    contacto  TEXT,
    telefono  TEXT,
    email     TEXT
);

-- ----------------------------------------------------------------------------
-- Tabla: producto
-- FK con ON DELETE RESTRICT: la BD bloquea borrar categoría/proveedor
-- con productos asociados. La aplicación gestiona el borrado mediante
-- transacción JDBC (UPDATE productos SET FK=NULL + DELETE padre).
-- ----------------------------------------------------------------------------
CREATE TABLE producto (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre        TEXT NOT NULL,
    descripcion   TEXT,
    precio        REAL NOT NULL CHECK (precio > 0),
    stock_actual  INTEGER NOT NULL CHECK (stock_actual >= 0),
    stock_minimo  INTEGER NOT NULL DEFAULT 0 CHECK (stock_minimo >= 0),
    categoria_id  INTEGER,
    proveedor_id  INTEGER,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id) ON DELETE RESTRICT
);

-- ----------------------------------------------------------------------------
-- Datos de prueba
-- ----------------------------------------------------------------------------

-- Categorías (3)
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Electrónica',   'Dispositivos electrónicos y accesorios'),
    ('Papelería',     'Material de oficina y escritura'),
    ('Alimentación',  'Productos alimentarios no perecederos');

-- Proveedores (2)
INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES
    ('Distribuciones García S.L.', 'Ana García',  '961234567', 'pedidos@dgarcia.es'),
    ('Importaciones Mediterráneo', 'Luis Pérez',  '963987654', 'contacto@impmed.es');

-- Productos (5, dos con stock bajo para probar alerta visual)
INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id, proveedor_id) VALUES
    ('Teclado inalámbrico',  'Teclado Bluetooth con batería recargable', 29.95,  15, 5,  1, 1),
    ('Ratón óptico USB',     'Ratón ergonómico con scroll',               9.50,   3, 10, 1, 1),
    ('Cuaderno A4 rayado',   'Cuaderno espiral 200 hojas',                4.25,  50, 20, 2, 2),
    ('Bolígrafo azul',       'Pack de 10 bolígrafos tinta azul',          3.00,   8, 15, 2, 2),
    ('Café molido 250g',     'Café natural tueste medio',                 5.75,  25, 10, 3, 2);

-- ----------------------------------------------------------------------------
-- Verificación
-- ----------------------------------------------------------------------------
SELECT 'Tablas creadas:' AS info;
SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;

SELECT 'Productos insertados:' AS info;
SELECT COUNT(*) AS total FROM producto;

SELECT 'Productos con stock bajo:' AS info;
SELECT nombre, stock_actual, stock_minimo FROM producto WHERE stock_actual < stock_minimo;