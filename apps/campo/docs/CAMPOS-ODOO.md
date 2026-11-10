# Campos y objetos agregados en Odoo

Todos los agregados son campos personalizados (`x_app_*`) y dos modelos nuevos. No se instaló ningún módulo y no se cambió ningún campo existente. Se crearon el 10-nov-2026 en la instancia bering.odoo.com (Odoo 18.0+e).

## Modelos nuevos

| Modelo | Uso | Acceso |
|---|---|---|
| `x_app_evento` (Bitácora app de campo) | Una fila por acción enviada desde el teléfono: persona, tarea, tipo, estado anterior y nuevo, hora del teléfono y del servidor, ubicación, datos, resultado y respuesta. El campo `x_uuid` es el folio único que evita duplicados al reintentar. | Usuarios internos: lectura. Administradores: total. |
| `x_app_material` (Material de servicio) | Materiales solicitados y utilizados por tarea. `x_estado` (solicitado/autorizado/entregado) lo cambia la oficina para autorizar. | Usuarios internos: leer, crear, editar. |

Campos de `x_app_evento`: x_name, x_uuid, x_operador_id, x_rol, x_modelo, x_res_id, x_tipo, x_estado_anterior, x_estado_nuevo, x_fecha_dispositivo, x_fecha_servidor, x_lat, x_lon, x_datos, x_resultado (en_proceso/aplicado/rechazado), x_motivo_rechazo, x_respuesta, x_adjuntos, x_app_version.

Campos de `x_app_material`: x_name, x_tarea_id, x_producto_id, x_cantidad, x_observaciones, x_tipo (solicitado/utilizado), x_estado, x_uuid, x_operador_id.

## Empleados (`hr.employee`): acceso a la app

| Campo | Tipo | Nota |
|---|---|---|
| x_app_activo | booleano | Sin esto la persona no entra, aunque tenga contraseña. |
| x_app_rol | selección | transportista / tecnico |
| x_app_usuario | texto | Usuario de la app (sin distinguir mayúsculas). |
| x_app_password_hash | texto | Solo administradores. PBKDF2-SHA256; nunca la contraseña. |
| x_app_password_temporal | texto | RH y administradores. Se borra sola al primer ingreso. |
| x_app_intentos | entero | Intentos fallidos seguidos. |
| x_app_bloqueado_hasta | fecha y hora | 15 min tras 5 intentos. Borrarlo desbloquea. |

## Entregas (`stock.picking`)

| Campo | Tipo | Quién lo llena |
|---|---|---|
| x_app_transportista_id | empleado | Oficina (asigna) |
| x_app_modalidad | propio / paqueteria | Oficina |
| x_app_estado | asignado / en_traslado / entregado_cliente / entregado_paqueteria / no_entregado | App |
| x_app_ventana_fin | fecha y hora | Oficina (opcional; inicio = fecha programada) |
| x_app_bultos, x_app_medidas | entero, texto | Oficina (el peso usa el campo nativo `shipping_weight`) |
| x_app_destino_paqueteria | texto | Oficina |
| x_app_inicio_traslado | fecha y hora | App |
| x_app_recibe_nombre | texto | App |
| x_app_fecha_confirmacion | fecha y hora | App (entrega al cliente o a paquetería) |
| x_app_paqueteria, x_app_guia, x_app_costo_envio, x_app_moneda_id, x_app_factura_paqueteria | | App |
| x_app_motivo_no_entrega | texto | App |
| signature (nativo) | imagen | App: firma de quien recibe |

La foto y la firma también quedan como adjuntos y en una nota del historial (chatter). La app nunca valida la entrega de inventario; eso lo sigue haciendo almacén.

## Servicios (`project.task`, proyecto «Servicios técnicos»)

| Campo | Tipo | Quién lo llena |
|---|---|---|
| x_app_tecnico_id | empleado | Oficina |
| x_app_tipo_servicio | instalacion / reparacion | Oficina |
| x_app_equipo | texto | Oficina |
| x_app_aceptado_el | fecha y hora | App |
| x_app_trabajo_realizado | texto | App |
| x_app_firma, x_app_firma_nombre | imagen, texto | App |
| x_app_motivo_pendiente | texto | App |
| x_app_finalizado_el | fecha y hora | App |
| x_app_material_ids | lista de x_app_material | App y oficina |

Etapas del proyecto: Asignado, En camino, En servicio, Pendiente, Finalizado, Cancelado. El estado del servicio es la etapa.

## Vistas

Pestaña «App de campo» agregada a los formularios de entrega (solo salidas), tarea y empleado (solo RH).

## Plazos (SLA)

No se agregó ningún campo de plazo. La app usa los que ya existen: `date_deadline` de la entrega o, si falta, `commitment_date` de la venta; en servicios, `date_deadline` de la tarea. Si no hay fecha, la app muestra «Sin fecha comprometida» y no inventa un plazo. El color ámbar aparece 2 h antes del límite (valor visual, ajustable cuando se defina la regla).
