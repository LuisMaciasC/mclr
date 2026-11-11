# Validación de punta a punta (10-nov-2026)

Contra el puente publicado (`https://app-campo-puente.bering-campo.workers.dev`) y Odoo real (bering), solo con registros de PRUEBAS IT (empresa 186). Usuarios de prueba: `prueba.chofer` (empleado 98) y `prueba.tecnico` (empleado 99). Verificado después leyendo Odoo directamente.

## Entregas

| Flujo | Registro | Resultado en Odoo |
|---|---|---|
| Directa | prueb/OUT/00009 (10724) | Estado app «Entregado al cliente»; inicio de traslado 16:36:38, confirmación 16:36:40; recibió «Recibe Prueba»; firma nativa guardada (`is_signed`); foto y firma como adjuntos (image/jpeg, image/png) en la nota del historial. Bitácora: eventos 1 y 2. |
| Reintento | mismo folio del evento 2 | Respuesta `repetido: true`; sin evento, adjunto ni nota duplicados. |
| Paquetería | prueb/OUT/00007 (10683) | Estado «Entregado a paquetería» (distinto de entregado al cliente); Estafeta, guía PRUEBA-APP-001, 250.00 MXN, factura F-PRUEBA-1, fecha 16:36:45; comprobante adjunto. Evento 3. |
| No entregada | prueb/OUT/00008 (10719) | Estado «No entregado», motivo «Cliente ausente — Prueba de la app»; no se marcó entregada. Evento 4. Después volvió a «En traslado» (evento 12). |

## Servicios

| Flujo | Registro | Resultado en Odoo |
|---|---|---|
| Completo | Tarea 37 «Instalación de prueba (app)» | Etapas Asignado → En camino → En servicio → Finalizado (eventos 5, 6, 9); aceptado 16:36:54; trabajo realizado, firmó «Cliente Prueba», finalizado 16:37:03; foto y firma adjuntas; materiales: 2 solicitados (estado solicitado) y 1 utilizado (eventos 7, 8). |
| Incidencia y pendiente | Tarea 38 «Reparación de prueba (app)» | Incidencia «Equipo dañado» registrada sin cambiar etapa (evento 10); etapa Pendiente con motivo «Falta material» (evento 11). |

Cada evento guarda persona, rol, hora del teléfono y del servidor, estado anterior y nuevo, y número de adjuntos en `x_app_evento`.

## Hallazgos corregidos durante la prueba

1. `fetch` en Cloudflare Workers fallaba con «Illegal invocation» (no se ve en pruebas locales). Corregido.
2. Las notas del historial salían con el HTML como texto: Odoo 18 requiere `body_is_html`. Corregido y verificado en la nota del evento 12.
3. El «Estado de la entrega» de la venta es el campo nativo `delivery_status` (calculado, solo lectura): Odoo lo cambia cuando almacén valida la entrega. El puente ya no intenta escribirlo; el estado de campo vive en la entrega (`x_app_estado`).

## No cubierto aún

- Uso de la app en un teléfono real (la app compila y sus pruebas unitarias pasan; la prueba anterior usó el mismo puente que usa la app).
- Reasignación desde Odoo con acciones sin señal: cubierta por pruebas automáticas del puente, no repetida contra Odoo real.
