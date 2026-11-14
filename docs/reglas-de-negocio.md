# Reglas de negocio

Casi todas las pruebas automáticas montan la aplicación entera, sin ventanas, sobre una base de datos temporal recién creada; solo las tres del final, que vigilan la independencia del núcleo, arrancan menos. En las pruebas de stock, documentos, recuentos, tesorería, informes, importación, migración y etiquetas, al terminar cada una se comprueba además que las existencias de cada producto en cada almacén coinciden con la suma de su libro de movimientos, que los plazos de cada documento suman su total y que ningún vencimiento tiene apuntado más dinero que su importe.

Aquí están solo las reglas que tienen una prueba que las vigila. Entre comillas de código va el nombre de esa prueba.

## Stock y libro de movimientos

- Cada entrada o salida cambia la existencia y deja una línea en el libro con la cantidad, el saldo que queda y el coste. `entradaYSalidaActualizanLaExistenciaYElLibro`
- No se puede sacar más de lo que hay. Si se intenta, el programa dice cuánto falta y no apunta nada. `noDejaSacarMasDeLoQueHay`
- Si la empresa lo activa en las opciones, una salida puede dejar el stock en negativo hasta que entre mercancía. `conElAjusteActivadoSiPermiteStockNegativo`
- El coste del producto es el precio medio ponderado: cada entrada con coste lo recalcula con lo que ya había, y las salidas no lo cambian. También se guarda el último coste de compra. `elPrecioMedioSePonderaEnCadaEntradaYNoCambiaAlSalir`
- El precio medio se redondea a cuatro decimales. `elPrecioMedioSeRedondeaACuatroDecimales`
- Si no quedaba nada en el almacén, el precio medio pasa a ser el coste de la nueva entrada. `sinStockPrevioElMedioEsElCosteDeLaEntrada`
- Un traspaso entre almacenes saca de uno y mete en otro sin cambiar el total ni el precio medio, y las dos mitades llevan la misma referencia. `elTraspasoMueveEntreAlmacenesSinCambiarTotalNiPrecioMedio`
- No se puede traspasar más de lo que hay ni al mismo almacén, y un traspaso que falla no deja media operación hecha. `noSePuedeTraspasarMasDeLoQueHayNiAlMismoAlmacen`
- Un ajuste deja el stock en la cantidad real contada y apunta la diferencia; si ya coincide, no deja movimiento. `elAjusteDejaElStockEnLoContado`
- Las cantidades de una entrada, una salida o un ajuste no pueden ser cero ni negativas. `rechazaCantidadesCeroONegativas`

## Recuentos de inventario

- Al aplicar un recuento, cada producto contado con diferencia recibe un ajuste con el número del recuento como referencia; lo que no se contó se queda como estaba, y un recuento aplicado ya no admite más anotaciones. `aplicarUnRecuentoConvierteCadaDiferenciaEnUnAjuste`
- El programa avisa de los productos que se han movido mientras el recuento estaba abierto, y el ajuste es la diferencia encontrada al contar, de modo que lo que salió después sigue descontado. `avisaDeLosProductosMovidosMientrasElRecuentoEstabaAbierto`
- Un recuento en el que no se ha contado nada no se puede aplicar. `unRecuentoSinNadaContadoNoSeAplica`

## Compras y reposición

- La reposición mira el stock previsto (lo que hay, menos lo vendido sin servir, más lo comprado sin recibir) y propone pedir hasta el máximo o, si no hay máximo, hasta el doble del mínimo. Los productos de un mismo proveedor van en un solo pedido, que se crea como borrador. `laReposicionCuentaLoVendidoSinServirYLoCompradoSinRecibir`
- Un producto sin proveedor habitual no se puede pedir desde la reposición: el programa pide que se le asigne uno. `sinProveedorHabitualNoSeSabeAQuienPedir`
- Confirmar un pedido de compra no mueve stock; lo anuncia como pendiente de recibir. Una recepción parcial deja el pedido servido en parte, la siguiente lo completa, y el coste de entrada es el precio menos el descuento de la línea. `unaRecepcionParcialDejaElPedidoAMediasYLaSiguienteLoCompleta`
- En una recepción no se puede recibir más de lo que queda pendiente en el pedido. `noSePuedeRecibirMasDeLoQueQuedaPendiente`
- Anular una recepción devuelve el stock, vuelve a dejar pendiente lo recibido y reabre el pedido; lo anulado no se puede anular otra vez. `anularUnaRecepcionDevuelveElStockYReabreElPedido`
- La recepción de un pedido va siempre al mismo proveedor que el pedido. `laRecepcionVaAlMismoProveedorQueSuPedido`

## Ventas

- Un pedido de venta confirmado reserva la mercancía. El albarán la descuenta del stock al precio medio del momento, deja el pedido servido en parte si no sale todo y libera la reserva de lo servido. `elAlbaranDescuentaStockAlPrecioMedioYLiberaLaReserva`
- Si una línea del albarán no tiene stock, no sale ninguna: la confirmación entera se deshace y el pedido sigue como estaba. `siUnaLineaNoTieneStockNoSeSirveNingunaDelAlbaran`
- Cancelar un pedido de venta libera lo que tenía reservado, y ya no se puede servir. `cancelarUnPedidoLiberaLoPendiente`
- Un albarán sin pedido previo también descuenta stock al confirmarlo. `unAlbaranDirectoSinPedidoTambienMueveStock`
- El albarán en PDF lleva los datos de la empresa y del cliente, el IVA desglosado por tipo, las notas y un recuadro para la firma de quien recibe, y ningún texto lo presenta como factura. `elAlbaranSaleEnPdfConLosDatosDelClienteYElRecuadroDeFirma`

## Documentos en general

- Los totales separan la base de cada tipo de IVA y calculan el impuesto de cada tramo. `losTotalesSeparanCadaTipoDeIva`
- El importe de una línea es cantidad por precio menos el descuento, redondeado a céntimos. `elImporteDeLineaRedondeaACentimos`
- Un borrador se puede cambiar conservando su número; un documento confirmado ya no se puede modificar ni borrar. `unBorradorSeModificaYUnDocumentoConfirmadoNo`
- Cada tipo de documento tiene su propia numeración correlativa por año: PC para pedidos de compra, RC para recepciones, y así con los demás. `cadaTipoLlevaSuPropiaNumeracionCorrelativa`
- No se guarda un documento sin líneas, sin cliente o proveedor, o con un descuento fuera de 0 a 100. `validaLoMinimoAntesDeGuardar`

## Tesorería

- Un albarán confirmado crea su cobro y una recepción su pago, con el total con IVA y el vencimiento que da la forma de pago general: transferencia a 30 días. `unAlbaranConfirmadoCreaSuCobroYUnaRecepcionSuPago`
- Un pedido no crea vencimientos, y un documento sin importe tampoco. `unPedidoNoCreaVencimientosYUnDocumentoSinImporteTampoco`
- Con varios plazos el importe se reparte a partes iguales, el último se lleva los céntimos que sobran y la suma es el total del documento al céntimo. `losPlazosSumanElTotalAlCentimoYElUltimoSeLlevaElResto`
- Un importe de menos céntimos que plazos no se parte. `unImporteDeMenosCentimosQuePlazosNoSeParte`
- Con día de pago fijo, cada vencimiento se lleva al siguiente día así; en un mes que no llega a ese día, al último. `elDiaFijoLlevaElVencimientoAlSiguienteDiaDePago`
- Las condiciones propias de un cliente mandan sobre las generales, las de clientes no tocan a las de proveedores, y al quitarlas vuelve a las generales. Quien tiene fijada una forma de pago la conserva aunque coincida con la general y esta cambie después; quien solo tiene día de pago sigue a la general. `lasCondicionesPropiasMandanSobreLasGeneralesYSePuedenQuitar`
- Anular un documento anula sus vencimientos. Si ya tienen dinero apuntado, no se anula nada, ni el documento ni su stock, hasta quitar esos cobros. `anularElDocumentoAnulaSusVencimientosSalvoQueTenganDineroApuntado`
- Quien sirve un albarán no necesita permiso de tesorería para que nazca su cobro, pero no puede registrar cobros ni cambiar condiciones de pago. `quienSirveNoNecesitaPermisoDeTesoreriaPeroNoPuedeApuntarCobros`
- Una base de datos anterior al módulo recibe sus tablas al abrirse, con sus cuentas y formas de pago y sin ningún vencimiento. Los documentos que ya estaban confirmados reciben los suyos cuando se pide, desde una fecha, y repetirlo no duplica nada. `losDocumentosAnterioresALaTesoreriaSeIncorporanCuandoSePide`
- Un cobro parcial deja el resto pendiente, y no se puede cobrar más de lo pendiente. `unCobroParcialDejaElRestoPendienteYNoSePuedeCobrarDeMas`
- Un pago sale de su cuenta; al quitarlo, el vencimiento vuelve a quedar pendiente y la cuenta recupera el saldo. `unPagoSaleDeLaCuentaYAlEliminarloVuelveAQuedarPendiente`
- Un cobro que salda varios vencimientos se reparte en orden, no se mezclan cobros con pagos y un mismo vencimiento no se cobra dos veces. `unaTransferenciaQueSaldaVariosVencimientosSeReparteEnOrden`
- No se apunta dinero con fecha futura, ni en una cuenta desactivada, ni por un importe que no sea mayor que cero; menos de medio céntimo es cero. `noSeApuntaDineroConFechaFuturaNiEnUnaCuentaDesactivada`
- Un gasto que se repite crea un vencimiento por periodo; del día 31 de un mes pasa al último del siguiente. `unGastoQueSeRepiteCreaUnVencimientoPorPeriodo`
- Un vencimiento apuntado a mano se cambia, pero nunca por debajo de lo ya cobrado ni de cliente si ya tiene dinero apuntado. Al anularlo conserva lo cobrado y guarda el motivo aparte de las notas; al recuperarlo, el motivo desaparece. Solo se borra si no tiene dinero apuntado. `unVencimientoAManoSeCambiaSeAnulaYSeBorra`
- De un vencimiento que sale de un documento solo cambian la fecha, el medio, la referencia y las notas. `enUnVencimientoDeDocumentoSoloCambianLaFechaElMedioYLaReferencia`
- Un cliente con vencimientos no se borra, y el programa lo dice en claro; sin ellos, sus condiciones de pago se van con él. `unClienteConVencimientosNoSeBorraYLoDiceEnClaro`
- Una forma de pago necesita nombre y plazos de 0 a 365 días, de menor a mayor y sin repetir; la general no se puede borrar ni desactivar, ni la que tiene puesta algún cliente. `lasFormasDePagoSeValidanYLaGeneralNoSePuedeQuitar`
- El saldo de una cuenta es la suma de sus movimientos, cada movimiento enseña el saldo que dejó, un traspaso no cambia el total y quitarlo quita sus dos mitades. `elSaldoDeUnaCuentaEsLaSumaDeSusMovimientos`
- Una cuenta y su saldo inicial se guardan juntos: si el saldo no vale (sin fecha, o con fecha futura), la cuenta tampoco queda creada. `laCuentaYSuSaldoInicialSeGuardanJuntosOSeQuedanSinGuardar`
- El saldo inicial cuenta el primero de su día, aunque se apunte o se corrija después de otros movimientos. `elSaldoInicialCuentaElPrimeroDeSuDiaAunqueSeCorrijaDespues`
- Siempre hay una cuenta predeterminada y solo una; al desactivarla lo pasa a ser otra. `siempreHayUnaCuentaPredeterminadaYSoloUna`
- El resumen separa lo vencido de lo que vence en 7 y en 30 días; lo que vence hoy todavía no está vencido. `elResumenYElSaldoDeUnClienteSeparanLoVencidoDeLoQueVence`
- «En 7 días» incluye lo que vence justo dentro de siete, y «en 30», lo de dentro de treinta: lo mismo que dice la etiqueta de cada vencimiento. `loQueVenceDentroDeSieteODeTreintaDiasCuentaElDiaSieteYElTreinta`
- La antigüedad agrupa por cliente o proveedor, no por su nombre: dos que se llamen igual no se mezclan. `dosClientesConElMismoNombreNoSeJuntanEnLaAntiguedad`
- La antigüedad reparte lo pendiente de cada cliente por tramos (sin vencer, hasta 30, 60 y 90 días, y más) y pone primero a quien más vencido tiene. `laAntiguedadReparteLoPendientePorTramos`
- La previsión va semana a semana, de lunes a domingo, parte del dinero que hay hoy y carga a la primera semana lo que ya ha vencido. `laPrevisionVaSemanaASemanaYLaPrimeraCargaConLoVencido`

## Catálogo, clientes y proveedores

- No puede haber dos productos con la misma referencia, sin distinguir mayúsculas. Un producto con movimientos no se borra: se archiva, y deja de aparecer en las listas normales. `noSeRepitenReferenciasNiSeBorraLoQueTieneHistoria`
- Al dar de alta un cliente o proveedor se quitan los espacios sobrantes y el NIF se guarda en mayúsculas. `elAltaGuardaLosDatosLimpios`
- Un cliente o proveedor necesita nombre, tiene que ser cliente, proveedor o las dos cosas, su correo tiene que parecer un correo y su descuento tiene que estar entre 0 y 100. `rechazaLoQueNoSePuedeGuardar`
- Archivar un cliente lo quita de las listas normales sin perderlo, y se puede recuperar. `archivarLoQuitaDelListadoNormal`
- Un proveedor que es el habitual de algún producto no se puede borrar; el programa propone archivarlo. `noSeBorraUnProveedorQueTieneProductos`
- Un proveedor que no se ha usado se puede borrar del todo. `unProveedorSinUsoSeBorraDelTodo`

## Usuarios y permisos

- Un usuario entra con su nombre de acceso, sin distinguir mayúsculas, y su contraseña. Si algo falla, el mensaje no dice si es el usuario o la contraseña. No se repiten nombres de acceso y la contraseña tiene al menos seis caracteres. `altaDeUsuarioYAcceso`
- Siempre queda al menos un administrador activo. `siempreQuedaUnAdministradorActivo`
- Solo quien gestiona usuarios puede crearlos o cambiarles el perfil. Cada persona puede cambiar su propia contraseña, pero no la de otra. `soloQuienGestionaUsuariosLosCreaPeroCadaUnoCambiaSuClave`
- Cambiar la configuración de la empresa exige permiso; las preferencias de pantalla, como el tema, las cambia cualquiera. `laConfiguracionDeLaEmpresaExigePermisoYLasPreferenciasDePantallaNo`
- El perfil limita lo que se puede hacer, aunque la pantalla no esconda el botón: el perfil de almacén mueve stock pero no toca el catálogo, y el de consulta no mueve stock. `elRolLimitaLoQueSePuedeHacer`
- En el buscador, un usuario no ve las pantallas para las que no tiene permiso. `unUsuarioSinPermisoNoVeLasPantallasQueNoLeCorresponden`

## Datos guardados

- Si una operación falla a medias, no queda nada guardado de ella. `siUnaTransaccionFallaNoQuedaNadaGuardado`
- En una carga por lotes cada operación sigue siendo de todo o nada, y si la carga entera falla no queda nada de ella. `enUnLoteCadaOperacionEsDeTodoONadaYElLoteTambien`
- Al abrir el programa, la base de datos se pone al día una sola vez; repetirlo no cambia nada. `lasMigracionesSeAplicanUnaSolaVez`
- Los números se leen escritos a la española (1.234,56) y también con punto decimal, y las fechas imposibles se rechazan. `leeNumerosEscritosALaEspanola`
- Importes, cantidades, precios y porcentajes se escriben a la española. `escribeNumerosALaEspanola`

## Copias de seguridad

- Cada copia es una base de datos completa que se puede abrir tal cual, con la fecha y la hora en el nombre. `laCopiaEsUnaBaseCompletaConLaFechaEnElNombre`
- Las copias se listan de la más reciente a la más antigua, y lo que no es una copia no aparece. `listaLasCopiasDeLaMasRecienteALaMasAntigua`
- Solo se conservan las copias que dicen los ajustes; las más antiguas se borran. Los datos guardados justo antes de una restauración no cuentan ni se borran. `soloConservaLasQueDicenLosAjustes`
- Si no se ha dicho otra cosa, se conservan diez copias. `porDefectoConservaDiez`
- Restaurar una copia recupera lo que había en ella y guarda aparte los datos de justo antes. `restaurarRecuperaLoQueSeHabiaBorrado`
- No se restaura un fichero que no sea una copia de InvManager, y los datos siguen intactos. `noRestauraLoQueNoEsUnaBaseDeInvManager`
- La copia de cierre solo se hace si está activada en los ajustes. `laCopiaAlSalirSoloSeHaceSiEstaActivada`
- Guardar una copia en otra carpeta deja un fichero idéntico, que no se mezcla con las copias propias. `guardarEnOtraCarpetaDejaUnFicheroIgual`

## Importación desde hojas de cálculo

- Antes de importar, el programa lee el fichero y dice qué pasará con cada fila (se crea, se actualiza o tiene un error y por qué) sin guardar nada. También dice qué categorías y proveedores se crearán y qué columnas no se usan. `analizaSinGuardarYDiceQuePasaraConCadaFila`
- Al importar se guardan las filas válidas; lo que el fichero no trae se conserva, el stock de un producto que ya existía no se toca y una categoría o un proveedor nuevos se crean una sola vez. `importaLasFilasValidasYCuentaLoQueHaHecho`
- Una fila que falla al guardarse no impide las demás. `unaFilaQueFallaAlGuardarNoImpideLasDemas`
- Se leen CSV separados por comas con textos entre comillas. `leeCsvConComasYComillas`
- Se leen CSV guardados por Excel en Windows, con sus tildes y eñes. `leeCsvGuardadoPorExcelEnWindows1252`
- En un Excel, los códigos numéricos largos, como un código de barras, no se estropean. `leeExcelSinEstropearLosCodigosNumericos`
- Al importar clientes y proveedores, los que ya existen se reconocen por su NIF y se actualizan. `importaTercerosYReconoceLosQueYaEstanPorSuNif`
- Las plantillas de ejemplo, en CSV y en Excel, se importan sin errores. `lasPlantillasDeEjemploSeImportanSinErrores`
- Si falta la columna del nombre, el fichero está vacío o es un .xls antiguo, el programa lo dice. `avisaSiFaltaLaColumnaDelNombreOElFicheroEstaVacio`
- Los nombres de columna se reconocen sin importar mayúsculas, tildes, espacios ni signos. `normalizaLosNombresDeColumna`

## Datos de la versión 1.0

- Antes de traer nada, el programa cuenta lo que hay en la base antigua. `cuentaLoQueHayAntesDeTraerlo`
- Se traen categorías, proveedores y productos, y el stock de cada producto entra como saldo inicial. `traeCategoriasProveedoresYProductosConSuStock`
- Repetir la migración no duplica nada. `repetirLaMigracionNoDuplicaNada`
- La base antigua se abre sin poder cambiarla. `laBaseAntiguaSeAbreSinPoderCambiarla`
- Lo que no es una base de la versión 1.0 se rechaza. `rechazaLoQueNoEsUnaBaseDeLaVersionAnterior`

## Inicio e informes

- La pantalla de inicio cuenta el valor del stock, los productos activos, los que están bajo mínimo, los que no tienen stock y los pedidos abiertos; lo archivado y los borradores no cuentan. `elResumenCuentaValorNivelesYPedidosAbiertos`
- El gráfico de entradas y salidas incluye los días sin movimiento y deja fuera los traspasos, que solo cambian la mercancía de sitio. `entradasYSalidasPorDiaIncluyenLosDiasVaciosYDejanFueraLosTraspasos`
- El stock se valora a precio medio por categoría y por almacén. `valoraElStockPorCategoriaYPorAlmacen`
- El informe de bajo mínimo pone primero los productos que más lejos están de su mínimo. `bajoMinimoOrdenaPorLoLejosQueEstanDelMinimo`
- El análisis ABC reparte los productos por el valor acumulado de sus salidas (ventas y salidas manuales; los ajustes no cuentan): A hasta el 80 %, B hasta el 95 %, C el resto. `elAbcRepartePorValorAcumuladoDeLasSalidas`
- La cobertura son los días que dura el stock al ritmo de consumo del periodo. `laCoberturaEsElStockEntreElConsumoDiario`
- El informe de productos sin movimiento da el valor parado y los días que lleva así. `sinMovimientoDaElValorParadoYLosDiasQueLleva`
- Compras por proveedor y ventas por cliente o por producto suman solo documentos confirmados del periodo; lo anulado no cuenta. El margen por producto compara lo vendido con lo que costó al precio medio de cada salida. `comprasYVentasSumanSoloDocumentosConfirmadosDelPeriodo`
- El ciclo de caja suma los días de almacén y los de cobro y resta los de pago. Compara saldos con movimientos de la misma clase (a coste y sin IVA en el stock; con IVA en lo cobrado y lo pagado), solo cuenta la mercancía y no se calcula si en el periodo no se movió nada. `elCicloDeCajaSumaLosDiasDeAlmacenYDeCobroYRestaLosDePago`
- Un periodo con la fecha final anterior a la inicial no se acepta. `unPeriodoAlRevesNoSeAcepta`

## Radiografía

- La radiografía da las mismas cifras que los informes de los que sale: el dinero del almacén es el de la pantalla de inicio; lo parado, el del informe «Sin movimiento» a 90 días; los días, los del «Ciclo de caja»; lo que se debe, la «Antigüedad de saldos»; y los días de stock de cada producto, los de «Rotación y cobertura». `laRadiografiaDaLasMismasCifrasQueLosInformes`
- Lo que se debe a la empresa y no es mercancía (una subvención, por ejemplo) cuenta en «quién debe» y en la acción de cobrar lo vencido, sin contarse como un cliente más, pero no en los días de cobro, que comparan mercancía con mercancía. `loQueNoEsMercanciaCuentaEnLoQueDebenPeroNoEnLosDiasDeCobro`
- Una empresa sin movimientos tiene una radiografía vacía: sin cifras y sin acciones. `unaEmpresaSinMovimientosTieneUnaRadiografiaVacia`
- El dinero del almacén se reparte en lo que sobra (de cada producto que se vende, lo que pasa de 60 días de venta, a su coste medio), lo parado y, lo demás, lo que se mueve. Un producto se va a agotar si le queda stock para menos de 15 días al ritmo de los últimos 90. `elDineroDelAlmacenSeReparteEnLoQueSeMueveLoQueSobraYLoParado`
- Lo parado y lo que sobra no cuentan dos veces el mismo producto. `loParadoYLoQueSobraNoCuentanDosVecesElMismoProducto`
- Sin salidas en el periodo no hay ritmo con el que decir que sobre nada. `sinSalidasEnElPeriodoNoSeDiceQueSobreNada`
- De lo que se va a agotar va primero lo que más valor mueve y, dentro de eso, lo que antes se acaba. `loQueSeVaAAgotarPonePrimeroLoQueMasValorMueve`
- Cada acción sale de una fórmula escrita: cobrar lo vencido es todo lo que ha pasado de su fecha; dejar de reponer, lo que sobra; liquidar, la mitad de lo que vale lo parado; y pagar en el plazo en que se cobra, los días de diferencia por lo comprado al día. Para esta última el cobro se mide sin lo vencido, que ya lo cuenta la acción de cobrarlo. `cadaAccionSaleDeSuFormula`
- Pagar en el plazo en que se cobra nunca propone más de 60 días, el máximo que la Ley 3/2004 (artículo 4.3) deja pactar entre empresas. Si ya se paga más tarde de lo que se cobra no libera nada, y sin compras o sin ventas en el periodo no se calcula. `pagarComoSeCobraNuncaProponeMasDeSesentaDias`
- Salen las tres acciones de mayor importe. Si liberan algo menos de tres, salen esas: no se rellena hasta tres. `salenLasTresAccionesDeMayorImporteYNoSeRellenaHastaTres`
- Cada perfil ve solo las acciones cuyos importes puede ver: las del stock piden ver costes; cobrar, ver la tesorería; y pagar, las dos cosas. `cadaPerfilVeSoloLasAccionesQueLeTocan`
- Las partes de una barra suman el total que se enseña. Los tantos por ciento suman 100; en euros, cada cifra se dice igual que en las frases, y la parte que solo sale en la barra (lo que se mueve, lo que no ha vencido) se queda con el euro que falte o que sobre. `lasPartesDeUnaBarraSumanElTotalQueSeEnsena`
- Las frases salen de las cifras, y quien no ve costes o tesorería no lee sus importes. `lasFrasesDeLaRadiografiaSalenDeLasCifras`
- Sin datos no hay cifras ni acciones, y cada parte dice qué falta. `sinDatosNoHayCifrasNiAccionesYLasFrasesLoDicen`

## Buscador (Ctrl+K)

- Un código de barras leído con el lector pone primero el producto que lo tiene exactamente. `elCodigoDeBarrasExactoVaElPrimero`
- Una coincidencia exacta, como un NIF, adelanta su grupo de resultados. `laCoincidenciaExactaAdelantaSuGrupo`
- Se busca sin distinguir tildes ni mayúsculas. `buscaSinTildesNiMayusculas`
- Todas las palabras escritas tienen que aparecer. `todasLasPalabrasTienenQueAparecer`
- Salen como mucho seis resultados por grupo, y primero los que empiezan por lo buscado. `comoMuchoSeisPorGrupoYLosQueEmpiezanPorLoBuscadoAntes`
- Los grupos salen en un orden fijo: productos, clientes y proveedores, documentos. `losGruposSalenEnElOrdenDeSusFuentes`
- Con el campo vacío, el buscador enseña las pantallas. `conElCampoVacioEnsenaLasPantallas`
- Cada resultado lleva a su pantalla: un pedido de compra a pedidos de compra, un cliente a clientes, y así. `cadaResultadoLlevaASuPantalla`

## Etiquetas

- El PDF de etiquetas sale con las páginas que tocan en cada formato, contando la posición de la primera etiqueta en una hoja empezada. `generaElPdfDeLosTresFormatosConLasPaginasQueTocan`
- Si el producto no tiene un EAN-13 válido, el código de barras lleva su referencia. `sinEanUsaCode128DeLaReferencia`
- Si tiene un EAN-13 válido, se usa ese aunque la referencia no se pueda codificar. `conEanUsaElEanAunqueLaReferenciaNoSeaCodificable`
- Una referencia que no se puede codificar sale sin barras y con un aviso. `unaReferenciaQueNoSePuedeCodificarSaleSinBarrasYConAviso`
- El precio de la etiqueta lleva el IVA del producto, redondeado a céntimos. `precioConIva`
- Se proponen tantas etiquetas como unidades en stock, al menos una y como mucho 500. `cantidadPorDefectoYLimites`
- La rejilla de cada hoja A4 cuadra con sus medidas. `laRejillaCuadraConElA4`
- Ningún texto se sale de la etiqueta ni pisa a otro, y un nombre largo se corta con puntos suspensivos. `nadaSeSaleDeLaEtiquetaNiSePisa`

## Primer uso y datos de demostración

- Los datos de demostración solo se cargan en una base de datos vacía. `soloSeCarganEnUnaBaseVacia`
- La empresa de demostración queda con una historia que cuadra: las existencias coinciden con el libro, hay productos en todos los niveles de stock y documentos en todos sus estados. Su tesorería también: casi todo cobrado, algo vencido y un cobro a medias, gastos fijos pagados y previstos, y ninguna cuenta que haya estado en números rojos. `rellenaUnaBaseVaciaConUnaHistoriaQueCuadra`
- Los NIF inventados de la demostración tienen un dígito de control válido. `losNifInventadosTienenUnDigitoDeControlValido`
- Al crear la empresa propia, el asistente propone como nombre de acceso la primera palabra del nombre de la persona, sin tildes. `elNombreDeAccesoSaleDeLaPrimeraPalabraSinTildes`
- La ruta de la carpeta de datos, si no cabe en la ventana, se acorta por el medio. `unaRutaQueNoCabeSeAcortaPorElMedio`

## Construcción del programa

- Ninguna pieza del núcleo depende de los módulos de negocio. `ningunaClaseDelNucleoImportaOtrosModulos`
- El buscador no depende de la interfaz, para poder probarlo solo. `elBuscadorNoTiraDeSwing`
- Con solo el núcleo, sin ningún módulo de negocio, el buscador funciona y devuelve las pantallas. `conSoloElNucleoElBuscadorDevuelvePantallas`
