# Seguridad de la app de campo

Revisión del 10-nov-2026, antes de publicar el puente.

## Dónde vive cada secreto

| Secreto | Dónde está | Dónde NO está |
|---|---|---|
| Llave API de Odoo | Secreto cifrado del Worker en Cloudflare (`wrangler secret put`). Solo el código del puente la lee, en memoria, para hablar con Odoo por HTTPS. | Repositorio, APK, teléfono, respuestas del puente, registros (logs). |
| Usuario de integración (correo) | Secreto del Worker. | Repositorio, APK. |
| TOKEN_SECRET (firma de sesiones) | Secreto del Worker. | Repositorio, APK. |
| Contraseña de cada persona | Solo su hash PBKDF2-SHA256 con sal, en la ficha del empleado (visible solo para administradores). | En texto en ningún lado. El teléfono tampoco la guarda. |
| Sesión en el teléfono | Token de 12 h en almacenamiento cifrado de Android (EncryptedSharedPreferences). Sin respaldo en la nube (`allowBackup=false`). | |

El APK solo contiene la dirección del puente. Si alguien descompila la app, no encuentra ninguna llave.

## Qué puede hacer una persona con usuario de la app

Solo cinco operaciones fijas del puente (iniciar sesión, cambiar su contraseña, ver sus tareas, registrar acciones). No hay forma de mandar consultas libres a Odoo.

- Ve solo las tareas asignadas a su ficha.
- Solo registra acciones (incluidas fotos) en tareas que tiene o tuvo asignadas; "tuvo" se comprueba con el historial de cambios de Odoo. Una tarea ajena se rechaza sin guardar nada.
- Un transportista no toca servicios y un técnico no toca entregas.
- Solo se aceptan fotos JPEG o PNG reales.
- Si la oficina desactiva su ficha, la sesión deja de funcionar en la siguiente petición.
- 5 intentos fallidos bloquean el usuario 15 minutos, y el limitador gratuito de Cloudflare corta a más de 10 intentos de sesión por minuto desde una misma IP.
- El tipo de cada archivo lo decide su contenido, no lo que diga el teléfono.
- Cambiar la contraseña usa siempre la persona de la sesión.

## Riesgos que quedan y cómo se cubren

1. **El usuario de integración es administrador con acceso a todas las empresas.** Si alguien obtuviera la llave, tendría ese poder en Odoo. Mitigación: la llave solo existe en Cloudflare; activar verificación en dos pasos en la cuenta de Cloudflare; crear la llave con vencimiento (90 días) y renovarla; nadie más con acceso a esa cuenta de Cloudflare. Opción más fuerte, con costo: un usuario de integración propio sin permisos de administrador (requiere licencia).
2. **El repositorio es público.** No contiene secretos, pero la llave de firma de pruebas (`firma/pruebas.jks`) es pública. Las versiones de prueba sirven para PRUEBAS IT; antes de instalar en teléfonos del personal se firma con una llave privada guardada en los secretos de GitHub, y la de pruebas no se usa más.
3. **Costo por abuso.** El plan gratuito de Cloudflare no cobra: si se pasan las 100,000 peticiones diarias, el puente responde con error hasta el día siguiente. No hay forma de generar cargos.
4. **Las acciones aparecen en Odoo hechas por el usuario de integración.** La identidad real (persona, hora del teléfono, ubicación) queda en la bitácora `x_app_evento` y en la nota del historial de cada registro.

## Pruebas automáticas de seguridad

`npx vitest run` en `apps/campo/puente` incluye: token alterado, ficha desactivada, bloqueo por intentos, comodines en el usuario, tarea ajena sin guardar nada, archivo que no es foto, y perfil que no corresponde.
