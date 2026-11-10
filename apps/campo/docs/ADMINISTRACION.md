# Administrar personas de la app de campo

Las personas de campo son **empleados** de Odoo, no usuarios: no consumen licencia.

## Dar de alta

1. Empleados → abre (o crea) a la persona en su empresa.
2. Pestaña **App de campo**:
   - Activa **App activo**.
   - **Rol**: Transportista (ve entregas) o Técnico (ve servicios).
   - **Usuario**: corto y único, por ejemplo `juan.perez`.
   - **Contraseña temporal**: mínimo 8 caracteres con letras y números.
3. Entrégale el usuario y la contraseña temporal. Al primer ingreso la app le pide crear la suya, y la temporal se borra.

## Asignar trabajo

- **Entrega**: en la orden de entrega (salida), pestaña App de campo → Transportista y Modalidad (Transporte propio o Paquetería). Para paquetería llena también bultos, medidas y la sucursal donde dejar. El peso es el campo nativo «Peso».
- **Servicio**: crea la tarea en el proyecto **Servicios técnicos**, etapa Asignado, pestaña App de campo → Técnico, tipo y equipo.
- **Plazo**: usa la «Fecha límite» de la entrega o de la tarea. Si la dejas vacía, la app muestra «Sin fecha comprometida».

## Reasignar o cancelar

Cambia el transportista o técnico, o cancela, en Odoo. En la siguiente actualización (máximo 5 minutos con la app abierta, o al deslizar hacia abajo) la tarea desaparece del teléfono de la persona anterior. Si esa persona ya había registrado algo sin señal, sus fotos y firma se suben igual y quedan en la bitácora y el historial, pero **no** cambian el estado: el envío queda como «Rechazado por oficina».

## Contraseña olvidada o bloqueo

- Olvidó la contraseña: escribe una nueva **Contraseña temporal**. Podrá entrar con ella y crear otra.
- Bloqueada por 5 intentos: borra **Bloqueado hasta** (o espera 15 minutos).

## Dar de baja

Desactiva **App activo**. La sesión abierta deja de funcionar en su siguiente petición. Lo ya registrado se conserva.

## Autorizar materiales

En la tarea, pestaña App de campo → lista de materiales: cambia **Estado** de «solicitado» a «autorizado» o «entregado». El técnico lo ve en la app al actualizar.

## Revisar lo que llegó del campo

- Historial (chatter) de la entrega o tarea: nota con persona, hora del teléfono, ubicación, datos y fotos.
- Bitácora completa: modelo **x_app_evento** (Ajustes → Técnico → Modelos, o una vista de Studio). Cada fila tiene su folio único.
