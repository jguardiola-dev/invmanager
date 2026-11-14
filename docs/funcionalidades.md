# Funcionalidades

Análisis funcional de InvManager 2: lo que una pequeña empresa de distribución o de comercio necesita para llevar su inventario, sus compras, sus ventas y sus cobros y pagos, y en qué punto está cada cosa.

- **Incluida** quiere decir que se puede usar desde una pantalla de la aplicación.
- **Prevista** quiere decir que no está hecha. La línea explica qué haría falta y, si ya existe alguna pieza, cuál.

Al final, [Hacia un ERP](#hacia-un-erp) repasa los módulos que convertirían el programa en un sistema de gestión completo y dónde se engancharía cada uno.

## Arranque y acceso

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Asistente de primer uso | Incluida | La primera vez ofrece «Empezar con datos de demostración» o «Empezar con mi empresa». |
| Datos de demostración | Incluida | Una empresa ficticia, Suministros Levante, S.L., con unos 180 productos y seis meses de historia, y los usuarios `demo`, `almacen` y `consulta`. |
| Pantalla de acceso | Incluida | Usuario y contraseña. Varias personas se turnan en el mismo ordenador, cada una con su perfil. |
| Cerrar sesión | Incluida | Botón en el menú lateral. |
| Cambiar la propia contraseña | Prevista | Hoy la cambia un administrador desde Ajustes. Faltaría la opción en el menú de cada usuario, pidiendo la contraseña actual; el servicio que la cambia ya existe. |
| Bloqueo tras varios intentos fallidos | Prevista | El acceso ya responde igual tanto si falla el usuario como la contraseña; faltaría contar los intentos y esperar un tiempo. |
| Cierre de sesión por inactividad | Prevista | Un temporizador en la ventana principal que vuelva a la pantalla de acceso. |

## Catálogo de productos

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Ficha de producto | Incluida | Referencia, código de barras, nombre, descripción, categoría, unidad, IVA, proveedor habitual y su referencia, precio de venta, mínimo, máximo, ubicación y notas. |
| Referencia propuesta | Incluida | Al dar de alta se propone la siguiente libre con el formato P00001. |
| Referencias y códigos de barras únicos | Incluida | No se repiten, sin distinguir mayúsculas en la referencia. |
| Stock inicial en el alta | Incluida | Se indica la cantidad y el almacén, y entra como saldo inicial en el libro. |
| Coste | Incluida | Se puede escribir a mano mientras no hay stock; con stock lo calcula el programa como precio medio ponderado. |
| Margen | Incluida | La ficha enseña el margen sobre el precio de venta y el valor del stock a coste. |
| Nivel de stock | Incluida | Cada producto aparece como sin stock, bajo mínimo, correcto o sobre máximo. |
| Categorías con color | Incluida | El color se elige entre ocho muestras para distinguirlas en los informes. Borrar una categoría deja sus productos sin categoría. |
| Unidades de medida | Incluida | Unidad, caja, pack, kilo, litro y metro de serie, cada una con sus decimales (de 0 a 3). Se añaden más en Ajustes. |
| Tipos de IVA | Incluida | 21 %, 10 %, 4 % y exento de serie, con uno predeterminado. Se editan en Ajustes. |
| Archivar en lugar de borrar | Incluida | Un producto con movimientos o documentos no se borra: se archiva, deja de ofrecerse y conserva su historia. |
| Filtros de la lista | Incluida | Búsqueda por texto, categoría, nivel de stock y ver archivados. |
| Exportar la lista | Incluida | A Excel (con números y fechas como tales) o a CSV, tal como se ve filtrada y ordenada. |
| Imagen del producto | Prevista | La tabla ya tiene la columna y hay una carpeta reservada para imágenes; falta elegirla en la ficha y enseñarla. |
| Conversión entre unidades | Prevista | Comprar por cajas y vender por unidades. Haría falta un factor de conversión por producto y aplicarlo en las líneas de los documentos. |
| Varios proveedores por producto | Prevista | Hoy hay un proveedor habitual con su referencia. Haría falta una tabla de proveedores por producto con su precio y su plazo. |
| Variantes (talla, color) | Prevista | Ver [Hacia un ERP](#hacia-un-erp). |
| Kits y packs | Prevista | Un producto compuesto de otros que, al venderse, descuenta sus componentes. Se apoyaría en las listas de materiales de fabricación. |
| Historial de precios | Prevista | El libro guarda el coste de cada entrada, pero no los cambios del precio de venta. |

## Clientes y proveedores

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Ficha única de tercero | Incluida | Una misma empresa puede ser cliente, proveedor o las dos cosas, sin duplicarla. |
| Datos de contacto | Incluida | Razón social, nombre comercial, NIF, persona de contacto, correo, teléfono, dirección, código postal, población, provincia, país y notas. |
| Descuento habitual del cliente | Incluida | Se propone en cada línea de sus pedidos de venta. |
| Plazo de entrega del proveedor | Incluida | Se guarda y se enseña en la ficha. La reposición todavía no lo usa para calcular. |
| Validaciones | Incluida | Nombre obligatorio, correo con forma de correo y descuento entre 0 y 100. |
| Archivar y borrar | Incluida | Lo que tiene documentos o productos asociados se archiva; lo que no se ha usado se puede borrar. |
| Exportar la lista | Incluida | A Excel o a CSV. |
| Varias direcciones de entrega | Prevista | Una tabla de direcciones por cliente y elegir una en el pedido y en el albarán. |
| Varios contactos por empresa | Prevista | Una tabla de contactos con su cargo, teléfono y correo. |
| Forma de pago y día de pago | Incluida | Cada cliente y cada proveedor puede tener los suyos; quien no, usa la forma de pago general. Se cambian desde su ficha. Ver [Tesorería](#tesorería). |
| Saldo pendiente en la ficha | Incluida | Lo que debe el cliente, o lo que se debe al proveedor, y cuánto de eso ha vencido. «Ver cobros» lleva a la lista con solo los suyos, y un botón lo dice y vuelve a enseñar los de todos. |
| Riesgo de cliente | Prevista | Un límite de crédito por cliente y un aviso al confirmar un pedido que lo supere. El saldo pendiente ya se calcula. |
| Tarifas por cliente | Prevista | Ver [Hacia un ERP](#hacia-un-erp). |

## Almacén y stock

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Libro de movimientos | Incluida | Cada cambio de stock es una línea con fecha, tipo, cantidad, coste, saldo resultante, documento de origen, motivo y usuario. El stock no se edita a mano. |
| Varios almacenes | Incluida | Se dan de alta en Ajustes, con uno predeterminado. No se desactiva un almacén que aún tiene stock. |
| Existencias | Incluida | Stock de cada producto en un almacén o en todos, con su nivel y su valor, filtrable por almacén y por nivel. |
| Entradas y salidas manuales | Incluida | Para devoluciones, regalos, consumo interno, roturas o muestras, con su motivo. |
| Ajuste a lo contado | Incluida | Se indica la cantidad real y se apunta la diferencia. |
| Traspasos entre almacenes | Incluida | Una sola operación saca de uno y mete en otro, con una referencia común. |
| Precio medio ponderado | Incluida | Cada entrada con coste lo recalcula; las salidas salen a ese precio. |
| Stock negativo opcional | Incluida | Por defecto no se puede sacar lo que no hay; una opción en Ajustes lo permite. |
| Reservado y disponible | Incluida | Lo comprometido en pedidos de venta abiertos y lo que falta por llegar de pedidos de compra se calculan al momento; el pedido de venta avisa si se pide más de lo disponible. |
| Filtros del libro | Incluida | Por tipo de movimiento, almacén, fechas y producto. Se exporta a Excel o a CSV. |
| Ubicación | Incluida | Un texto por producto, del tipo pasillo-estante-hueco, que sale en la ficha, en la etiqueta y ordena la hoja de recuento. |
| Ubicación distinta en cada almacén | Prevista | Hoy la ubicación es del producto. Haría falta guardarla por producto y almacén. |
| Comprobación de cuadre desde la pantalla | Prevista | El servicio que comprueba que cada existencia coincide con la suma del libro ya existe y lo usan las pruebas; falta un botón que lo lance y enseñe el resultado. |
| Stock a una fecha pasada | Prevista | Bastaría una consulta que sume el libro de movimientos hasta esa fecha; los movimientos ya llevan la fecha en que ocurrieron, también los de documentos fechados en el pasado. |
| Valoración FIFO | Prevista | Hoy se valora a precio medio ponderado. FIFO necesitaría guardar capas de coste por entrada. |
| Lotes, caducidades y números de serie | Prevista | Ver [Hacia un ERP](#hacia-un-erp). |

## Recuentos de inventario

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Abrir un recuento | Incluida | De un almacén, con todos sus productos activos o solo los de una categoría. Guarda el stock teórico de ese momento y recibe un número RI. |
| Contar con lector de códigos | Incluida | Cada lectura suma la cantidad indicada a lo contado de ese producto y se guarda al momento. También se puede escribir la cantidad o borrar lo contado. |
| Hoja ordenada por ubicación | Incluida | Las líneas salen ordenadas por ubicación y nombre, para recorrer el almacén. |
| Aplicar el recuento | Incluida | Cada diferencia se convierte en un ajuste con el número del recuento. Lo no contado se queda como estaba. |
| Aviso de movimientos durante el recuento | Incluida | Antes de aplicar, avisa de los productos con diferencia que se han movido desde que se abrió. |
| Cancelar un recuento | Incluida | Se cierra sin tocar el stock. |
| Recuento cíclico programado | Prevista | Proponer cada semana qué contar según la clase ABC. El informe ABC ya existe; faltaría el calendario. |
| Contar desde un móvil o terminal | Prevista | Necesita trabajo en red: ver [Hacia un ERP](#hacia-un-erp). |

## Compras y reposición

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Reposición | Incluida | Lista los productos cuyo stock previsto (lo que hay, menos lo vendido sin servir, más lo comprado sin recibir) cae bajo su mínimo, y propone pedir hasta el máximo o, sin máximo, hasta el doble del mínimo. |
| Corregir la propuesta | Incluida | Se cambia la cantidad de cada producto o se pone a cero para dejarlo fuera. |
| Pedidos desde la reposición | Incluida | Un clic crea un pedido en borrador por cada proveedor habitual, valorado al último coste. |
| Pedido de compra | Incluida | Cabecera con proveedor, almacén, fecha, referencia, fecha de llegada prevista y notas; líneas con cantidad, coste, descuento e IVA; totales al momento. |
| Recepción de un pedido | Incluida | Se proponen las cantidades pendientes y se bajan si solo llega una parte. Al confirmarla entra el stock, se recalcula el precio medio y el pedido pasa a servido en parte o completado. |
| Recepción sin pedido | Incluida | Una recepción se puede crear directamente. |
| Anular una recepción | Incluida | Devuelve el stock y deja otra vez pendiente lo recibido en el pedido. |
| Cancelar un pedido | Incluida | Lo pendiente deja de contar como por recibir. |
| Pedido en PDF | Incluida | Con los datos de la empresa y del proveedor, las líneas y el IVA desglosado. |
| Enviar el pedido por correo | Prevista | Hoy se guarda el PDF y se envía con el programa de correo de cada uno. Haría falta configurar una cuenta de correo. |
| Plazo de entrega en la reposición | Prevista | El plazo está en la ficha del proveedor; faltaría usarlo con el consumo diario para adelantar el pedido. |
| Previsión de demanda | Prevista | El informe de rotación ya calcula el consumo diario; faltaría proponer mínimos a partir de él. |
| Gastos de compra (portes, aranceles) | Prevista | Repartirlos en el coste de las líneas de la recepción. |
| Devoluciones a proveedor | Prevista | Hoy se registran como salida manual. Ver [Hacia un ERP](#hacia-un-erp). |

## Ventas

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Pedido de venta | Incluida | Como el de compra, con el precio de venta y el descuento habitual del cliente. Al confirmarlo reserva la mercancía. |
| Aviso de disponible | Incluida | Al añadir una línea dice cuánto hay libre (stock menos reservado) y avisa si se pide más. |
| Albarán desde el pedido | Incluida | Propone lo pendiente; se puede servir una parte. Al confirmarlo sale el stock al precio medio del momento y el pedido pasa a servido en parte o completado. |
| Albarán sin pedido | Incluida | Para la venta de mostrador o la entrega directa. |
| Todo o nada | Incluida | Si una línea no tiene stock, no sale ninguna y nada queda a medias. |
| Anular un albarán | Incluida | Devuelve el stock y reabre lo pendiente en el pedido. |
| Cancelar un pedido | Incluida | Libera lo reservado. |
| Albarán en PDF | Incluida | Con datos de empresa y cliente, líneas, IVA desglosado, notas y recuadro «Recibí conforme» para la firma. |
| Lectura de códigos en las líneas | Incluida | El campo para añadir productos acepta referencia, código de barras o nombre; el lector no confirma el documento por accidente. |
| Presupuestos | Prevista | Un tipo de documento más, antes del pedido. El motor de documentos está pensado para añadir tipos sin cambiar las tablas. |
| Facturas | Prevista | Fuera del alcance a propósito: ver [Hacia un ERP](#hacia-un-erp). |
| Hoja de preparación (picking) | Prevista | Un PDF del pedido ordenado por ubicación, para preparar antes de servir. |
| Devoluciones de cliente | Prevista | Hoy se anula el albarán entero o se registra una entrada manual. Ver [Hacia un ERP](#hacia-un-erp). |
| Firma en pantalla de la entrega | Prevista | Necesita un dispositivo táctil y guardar la imagen con el albarán. |

## Documentos en general

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Numeración por tipo y año | Incluida | PC-2026-00001 para pedidos de compra, RC para recepciones, PV para pedidos de venta y AL para albaranes. El número se da al guardar el primer borrador. |
| Estados | Incluida | Pedidos: borrador, confirmado, servido en parte, completado o cancelado. Recepciones y albaranes: borrador, confirmado o anulado. |
| Borradores editables | Incluida | Un borrador se modifica o se borra; lo confirmado ya no, solo se cancela o se anula. |
| Documentos con fecha pasada | Incluida | Un documento fechado en el pasado mueve el stock en su fecha, no en el momento de teclearlo. |
| Trazabilidad entre documentos | Incluida | Cada recepción o albarán guarda su pedido de origen, y el pedido enseña lo que ha salido de él. |
| Filtro por estado | Incluida | Cada lista de documentos se filtra por estado y se exporta. |
| Impresión directa | Prevista | Hoy se guarda el PDF y se imprime desde el lector de PDF. |
| Duplicar un documento | Prevista | Crear un borrador nuevo con las mismas líneas. |
| Adjuntos (fotos, PDF del proveedor) | Prevista | Una carpeta por documento junto a los datos y una tabla que la enlace. |

## Tesorería

Es tesorería de gestión: qué se espera cobrar y pagar, qué se ha cobrado y pagado y cuánto dinero hay. No factura, no imprime recibos y no lleva contabilidad.

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Cobro previsto de cada albarán | Incluida | Al confirmar un albarán nace su cobro, con el total con IVA, en los plazos de la forma de pago del cliente. Anular el albarán anula sus cobros; si ya hay dinero apuntado, no se deja anular hasta quitarlo. |
| Pago previsto de cada recepción | Incluida | Lo mismo con las recepciones y la forma de pago del proveedor. |
| Formas de pago | Incluida | Un medio (transferencia, recibo, efectivo, tarjeta, pagaré) y uno o varios plazos en días. Con varios, el importe se reparte a partes iguales y el último se lleva los céntimos. Hay una general para clientes y otra para proveedores; se gestionan en Ajustes. |
| Día de pago fijo | Incluida | Si un cliente paga siempre el 25, cada vencimiento se lleva al siguiente día 25; en un mes que no llega a ese día, al último. |
| Cobros y pagos a mano | Incluida | Lo que no pasa por el almacén (alquiler, nóminas, impuestos, seguros) y los saldos que ya había al empezar. Con su tipo de gasto, y repetibles cada mes, trimestre o año. |
| Registrar un cobro o un pago | Incluida | Entero o en parte, con su fecha, la cuenta en la que entra o de la que sale, el medio y una referencia libre. Nunca más de lo pendiente. |
| Un pago para varios vencimientos | Incluida | La transferencia que paga varios albaranes se reparte entre los vencimientos abiertos del cliente, del más antiguo al más nuevo. |
| Pantallas de Cobros y de Pagos | Incluida | Por fecha de vencimiento, con cuánto ha vencido, cuánto vence en 7 y en 30 días y el total; cada cifra filtra la lista. Ficha con lo pendiente, lo apuntado y las notas. Filtro por situación y exportación. |
| Cambiar un vencimiento | Incluida | De lo que sale de un documento se cambian la fecha, el medio, la referencia y las notas; el importe es el del documento. Lo apuntado a mano se cambia entero. |
| Anular lo pendiente | Incluida | Con su motivo, que se guarda aparte de las notas. Deja de contar en saldos y previsiones, conserva lo ya cobrado y se puede recuperar. |
| Quitar un cobro o un pago | Incluida | El vencimiento vuelve a quedar pendiente por ese importe y la cuenta lo descuenta. |
| Referencia de la factura | Incluida | Un texto libre en cada vencimiento, para el número de la factura hecha en el programa de facturación o recibida del proveedor. |
| Caja y bancos | Incluida | Cuentas de banco y cajas de efectivo con su saldo inicial. El saldo es la suma de los movimientos; cada movimiento enseña el saldo que dejó. |
| Traspasos y ajustes | Incluida | Dinero de una cuenta a otra (el efectivo que se ingresa en el banco) y ajuste al saldo real, apuntando la diferencia con su motivo (una comisión, un descuadre). |
| Del albarán a su cobro | Incluida | «Ver cobro» en un albarán confirmado y «Ver pago» en una recepción llevan a su vencimiento; desde este se abre el documento. |
| Aviso del límite de efectivo | Incluida | Al registrar en efectivo una operación de 1.000 € o más con un cliente o proveedor, recuerda el límite legal de pagos en efectivo. Avisa; no lo impide. |
| Documentos anteriores | Incluida | Desde Ajustes se crean los vencimientos de los albaranes y recepciones que se confirmaron antes de llevar aquí la tesorería, a partir de una fecha. |
| Recibo o justificante de cobro | Prevista | A propósito, cobrar no imprime nada: un justificante con valor fiscal pertenece a la facturación. |
| Remesas para el banco | Prevista | Ficheros SEPA de recibos y de transferencias a partir de los vencimientos. Hace falta guardar el IBAN y el mandato de cada cliente. |
| Leer el extracto y conciliar | Prevista | Importar el extracto del banco y casar cada línea con un cobro o un pago. Las cuentas y sus movimientos ya existen. |
| Reclamación de lo vencido | Prevista | Cartas o correos a quien debe. La lista de vencidos por cliente ya sale del informe de antigüedad. |
| Efectos y descuento comercial | Prevista | Pagarés en cartera, descontados y devueltos. Hoy el pagaré es solo un medio de pago. |
| Otras monedas | Prevista | Todo va en euros. |

## Etiquetas y códigos de barras

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Etiquetas de producto en PDF | Incluida | Desde la ficha de producto. Llevan referencia, nombre, código de barras, y si se quiere el precio con IVA y la ubicación. |
| Tres formatos | Incluida | Hoja A4 de 24 etiquetas (70 × 37 mm), hoja A4 de 65 (38,1 × 21,2 mm) y etiqueta suelta de 62 × 29 mm para impresora de etiquetas. |
| Aprovechar hojas empezadas | Incluida | Se elige en qué hueco va la primera etiqueta, y la vista previa marca los que se saltan. |
| EAN-13 o Code 128 | Incluida | Si el producto tiene un EAN-13 válido se usa ese; si no, la referencia en Code 128. Si no se puede codificar, avisa. |
| Etiqueta en la ficha | Incluida | La ficha de producto enseña su etiqueta con un código de barras legible por un lector. |
| Etiquetas de varios productos a la vez | Prevista | Elegir varios en la lista, o los de una recepción, y sacar un solo PDF. |
| Etiquetas de ubicación | Prevista | Etiquetas para las estanterías con el código de cada hueco. |

## Inicio e informes

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Pantalla de inicio | Incluida | Valor del stock, productos activos, bajo mínimo, sin stock, pedidos abiertos, lo que hay por cobrar y los pagos de los próximos 30 días; estado de cada producto; entradas y salidas del último mes; lo que hay que reponer; lo último que se ha movido y los pedidos abiertos. Cada cosa lleva a su pantalla. |
| Valoración de inventario | Incluida | Cuánto vale lo que hay, por almacén, a precio medio. |
| Bajo mínimo | Incluida | Qué productos no llegan a su mínimo, primero los más lejanos. |
| Análisis ABC | Incluida | Qué productos mueven más valor en salidas, repartidos en A (80 %), B (hasta el 95 %) y C. |
| Rotación y cobertura | Incluida | Para cuántos días queda stock al ritmo de consumo del periodo. |
| Sin movimiento | Incluida | Qué lleva tiempo parado y cuánto vale. |
| Compras por proveedor | Incluida | Importe recibido sin IVA en un periodo. |
| Ventas por cliente | Incluida | Importe servido sin IVA en un periodo. |
| Ventas por producto con margen | Incluida | Unidades, importe, coste al precio medio de cada salida y margen. |
| Antigüedad de saldos | Incluida | Lo que debe cada cliente, o lo que se debe a cada proveedor, repartido por lo que lleva vencido: sin vencer, de 1 a 30 días, de 31 a 60, de 61 a 90 y más de 90. |
| Previsión de caja | Incluida | El dinero que habrá al acabar cada una de las próximas 8, 13 o 26 semanas si cada vencimiento se cobra y se paga en su fecha. La primera semana carga con lo ya vencido. |
| Ciclo de caja | Incluida | Días que la mercancía pasa en el almacén, más los que se tarda en cobrar, menos los que se tarda en pagar, y cuánto dinero libera cada día que se acorta. Solo cuenta la mercancía, no los gastos fijos. |
| Radiografía de stock y caja | Incluida | En una página: cuánto dinero hay en el almacén y qué parte se mueve, sobra o está parada; qué se va a agotar; quién debe y desde cuándo; cuántos días tarda el dinero en volver; y las tres acciones que más caja liberan, de cuatro posibles (cobrar lo vencido, dejar de reponer lo que sobra, liquidar lo parado y pagar en el plazo en que se cobra), cada una con su importe, la cuenta de la que sale y lo que da por supuesto. Usa las cifras de los informes y cada parte lleva al suyo. Mide el ritmo de venta en los últimos 90 días. |
| Radiografía en PDF y con otros umbrales | Prevista | Guardarla como PDF de una hoja para llevarla a una reunión, y cambiar desde la pantalla los umbrales, que hoy son fijos: 15 días para agotarse, 90 para parado, 60 de venta como stock objetivo y recuperar el 50 % al liquidar. |
| Gráficos y exportación | Incluida | Los informes que lo necesitan llevan gráfico, y todos se exportan a Excel o a CSV. |
| Informes a medida | Prevista | Un editor de consultas o una exportación completa para una herramienta de análisis externa. |
| Envío programado de informes | Prevista | Necesita correo y que el programa se ejecute sin nadie delante. |

## Búsqueda y manejo

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Buscador global (Ctrl+K) | Incluida | Encuentra pantallas, productos, clientes, proveedores, documentos y cobros y pagos pendientes, sin distinguir tildes ni mayúsculas. Un código de barras leído con el lector lleva directo a su producto. |
| Navegación entre pantallas | Incluida | Desde un aviso de inicio, una ficha o un documento se salta a la pantalla relacionada con ese dato. |
| Recargar (F5) | Incluida | Vuelve a leer los datos de la pantalla. Las pantallas también se recargan solas cuando otra cambia algo que enseñan. |
| Tema claro y oscuro | Incluida | Se cambia en el menú lateral y se recuerda. |
| Mensajes claros | Incluida | Cada error dice qué ha pasado y cómo arreglarlo, y cada acción confirma lo hecho. |
| Varios idiomas | Prevista | Los textos están en español en el código; habría que sacarlos a ficheros de traducción. |
| Atajos de teclado configurables | Prevista | Hoy hay dos fijos: Ctrl+K y F5. |

## Administración

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Datos de la empresa | Incluida | Nombre, NIF, dirección, teléfono, correo y web; encabezan los documentos. |
| Usuarios | Incluida | Alta, cambio de nombre y perfil, desactivación y cambio de contraseña. Las contraseñas se guardan derivadas con PBKDF2 y sal propia, nunca en claro. Siempre queda un administrador activo. |
| Perfiles | Incluida | Administrador (todo), gestor (todo el trabajo diario, cobros y pagos incluidos, sin usuarios ni configuración), almacén (movimientos, recuentos, recepciones y albaranes, sin ver costes ni tesorería) y consulta (ve todo, no cambia nada). El servicio comprueba el permiso aunque la pantalla esconda el botón, y las pantallas de productos, existencias, movimientos, reposición, inicio, radiografía e informes esconden los costes a quien no tiene permiso para verlos. |
| Permisos a medida | Prevista | Hoy cada perfil lleva un conjunto fijo de permisos. Haría falta guardar los permisos por usuario o crear perfiles propios. |
| Copias de seguridad | Incluida | Copia completa en un clic, con fecha en el nombre; se conservan las que se indiquen (diez por defecto). Se puede guardar una en otra carpeta y restaurar; antes de restaurar se guardan aparte los datos del momento. |
| Copia automática al cerrar | Incluida | Si está activada en Ajustes, al cerrar la ventana principal se hace una copia de seguridad. Cerrar sesión no la dispara. |
| Copias programadas o en la nube | Prevista | Un horario y un destino externo. |
| Importar desde Excel o CSV | Incluida | Productos o clientes y proveedores. Antes de guardar enseña fila a fila qué se crea, qué se actualiza y qué tiene error. Reconoce nombres de columna habituales, CSV con punto y coma o comas, y ficheros guardados por Excel en Windows. Incluye plantillas de ejemplo. |
| Traer datos de la versión 1.0 | Incluida | Lee la base `inventario.db` antigua sin cambiarla y trae categorías, proveedores y productos con su stock. Se puede repetir sin duplicar. |
| Auditoría | Incluida | Registro de quién hizo qué y cuándo, con búsqueda y exportación. Apunta altas, cambios, archivados y bajas de productos, clientes, proveedores, almacenes y usuarios; la vida de cada documento y recuento; copias y restauraciones. Los movimientos de stock llevan su propio usuario en el libro. |
| Auditoría de todo cambio | Prevista | Categorías, unidades, tipos de IVA y opciones generales no se apuntan todavía. |
| Información del sistema | Incluida | Qué módulos están cargados, cuántos scripts de base de datos tiene cada uno, cuándo se aplicó el último y dónde están los datos. |

## Instalación y datos

| Función | Estado | Cómo funciona o qué haría falta |
|---|---|---|
| Programa portable para Windows | Incluida | Un zip con Java incluido: se descomprime y se abre `InvManager.exe`. No hay instalador. |
| Datos junto al programa | Incluida | En una carpeta `data` al lado del ejecutable: la base de datos y las copias de seguridad. |
| Un solo ordenador | Incluida | La base de datos es un fichero SQLite local. Varias personas se turnan con su usuario. |
| Varios puestos a la vez | Prevista | Ver [Hacia un ERP](#hacia-un-erp). |
| Instalador y actualización automática | Prevista | Un instalador por usuario y un aviso de versión nueva. |
| Firma del ejecutable | Prevista | Sin certificado de firma de código, Windows puede enseñar el aviso de SmartScreen. |
| macOS y Linux | Prevista | El código es Java y la interfaz es multiplataforma; faltaría empaquetarlo para esos sistemas. |

## Hacia un ERP

InvManager cubre el inventario, las compras y las ventas hasta el albarán, y la tesorería de gestión que sale de ellos. Un ERP de pyme suma a eso facturación, contabilidad, relación con clientes y, según el negocio, fabricación o venta en tienda. Esos módulos no existen hoy. Lo que sí existe son los puntos por los que se engancharían, y un módulo, el de tesorería, que ya se añadió por ellos sin tocar el motor de stock:

- **`Modulo`**: cada módulo trae sus tablas (scripts SQL que se aplican una vez), registra sus servicios y aporta pantallas al menú. Añadir uno es escribir su paquete y sumar una línea a la lista de módulos.
- **`EntradaMenu`**: las pantallas del módulo, con su grupo, su orden y el permiso que piden.
- **`AccionEntidad`**: botones sobre registros de otro módulo, como «Etiquetas» en la ficha de producto o «Ver cobro» en un albarán.
- **`SeccionFicha`**: un bloque en la ficha de un registro de otro módulo, como lo que debe un cliente en su ficha.
- **`OyenteDocumento`**: lo que otro módulo hace, en la misma transacción, cuando un documento se confirma o se anula. Así nacen los cobros de un albarán.
- **`FuenteBusqueda`**: un grupo de resultados en el buscador Ctrl+K.
- **El motor de documentos**: una sola pareja de tablas (cabecera y líneas) para todos los tipos, con numeración por tipo y año, estados y enlace con el documento de origen. Un tipo nuevo se añade a la lista de tipos sin cambiar las tablas.
- **El motor de stock**: el único sitio que mueve existencias. Cada movimiento guarda qué lo provocó (`origen` y `origen_id`) sin depender del módulo que lo pide.
- **El bus de avisos**: un módulo publica que algo ha cambiado y las pantallas abiertas de otros se recargan.

| Módulo | Qué necesitaría | Dónde se engancharía |
|---|---|---|
| Facturación con VERI\*FACTU | Facturas y facturas rectificativas a partir de albaranes, y cumplir los requisitos del reglamento de sistemas de facturación, que pide entre otras cosas que el registro de cada factura no se pueda alterar. Es la razón por la que hoy los documentos llegan hasta el albarán: un programa que factura entra en el reglamento de sistemas de facturación. | Tipos de documento nuevos con su serie, enlazados al albarán por el documento de origen; un `Modulo` con sus tablas propias para el registro; una `AccionEntidad` «Facturar» en el albarán. |
| Contabilidad | Plan de cuentas, asientos automáticos de compras, ventas, cobros y pagos y regularización de existencias, libros oficiales y modelos de IVA. | Un `Modulo` que escuche la confirmación de documentos (`OyenteDocumento`) y genere asientos; las bases por tipo de IVA ya se calculan en cada documento, y los cobros y pagos ya tienen su cuenta y su fecha. |
| Tesorería bancaria | Remesas SEPA, lectura del extracto, conciliación, efectos y riesgo de cliente. Los vencimientos, las formas de pago, los cobros y pagos, las cuentas y la previsión ya existen. | En el módulo de tesorería. Con facturación, el vencimiento nacería de la factura en lugar del albarán; lo demás no cambia. |
| CRM | Oportunidades, actividades, llamadas y seguimiento de presupuestos. | Un `Modulo` sobre la tabla de terceros, con su `FuenteBusqueda` y acciones en la ficha del cliente; los presupuestos serían un tipo de documento más. |
| TPV | Venta rápida en mostrador con lector, cobro y ticket. | Un albarán directo a un cliente genérico ya descuenta stock. El ticket, como documento de venta, tendría que estudiarse junto con la facturación. |
| Fabricación con listas de materiales | Escandallos, órdenes de fabricación que consumen componentes y dan de alta el producto terminado con su coste. | Tipos de movimiento nuevos registrados con el motor de stock, con el origen apuntando a la orden; el precio medio ya se recalcula en cada entrada con coste. |
| Tienda online e integraciones | Sincronizar catálogo y stock, descargar pedidos, avisos a transportistas. | Un servicio que lea el catálogo y las existencias y cree pedidos de venta con el servicio de documentos. Necesita un proceso que corra sin la ventana abierta y, en la práctica, trabajo en red. |
| Trabajo en red con varios puestos | Base de datos en un servidor, varias sesiones a la vez, bloqueos y numeración concurrente. | Queda fuera de esta versión. |
| Lotes | Número de lote en recepciones y albaranes, stock por lote y trazabilidad hacia atrás y hacia delante. | Una columna de lote en las líneas de documento y en el libro de movimientos, y existencias por producto, almacén y lote. Toca el motor de stock, que es el único que escribe existencias. |
| Caducidades | Fecha de caducidad por lote, avisos y salida del más próximo a caducar. | Sobre los lotes; un informe y un aviso en la pantalla de inicio. |
| Números de serie | Una línea por unidad con su número, garantía e historial. | Como los lotes, con cantidad uno por número. |
| Variantes | Producto padre con atributos (talla, color) y una referencia por combinación. | Una tabla de variantes en el catálogo; cada variante sería un producto para el stock y los documentos. |
| Tarifas por cliente | Listas de precios, precios por cantidad y promociones con fechas. | El precio que se propone en la línea del pedido de venta; hoy sale del precio de venta y el descuento habitual del cliente. |
| Devoluciones | Devolución de cliente con su documento, abono y vuelta al stock; devolución a proveedor. | Tipos de documento nuevos que mueven stock al revés, enlazados al albarán o a la recepción de origen. El abono pertenece a facturación. |
