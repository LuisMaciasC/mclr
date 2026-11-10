# Instalación

Tres piezas: Odoo (ya configurado), el **servidor puente** (guarda la llave de Odoo) y la **app Android**.

## 1. Servidor puente (Cloudflare Workers, plan gratuito)

El plan gratuito permite 100,000 peticiones al día; 30 personas actualizando cada 5 minutos durante 12 horas usan unas 4,500. Costo: $0.

Requisitos: una cuenta gratuita de Cloudflare y Node 20.

```bash
cd apps/campo/puente
npm install
npx wrangler login
# Datos de Odoo (no van en el código ni en el APK):
npx wrangler secret put ODOO_API_KEY     # llave API del usuario de integración
npx wrangler secret put TOKEN_SECRET     # texto aleatorio largo: openssl rand -base64 48
```

Edita `wrangler.toml`: `ODOO_DB` (nombre de la base) y `ODOO_LOGIN` (correo del usuario de integración). Luego:

```bash
npx wrangler deploy
curl https://<tu-worker>.workers.dev/v1/salud
```

### Usuario de integración en Odoo

Usa un usuario interno existente (no hace falta licencia nueva). Crea su llave en Preferencias → Seguridad de la cuenta → Nueva llave API, con nombre «App de campo». Ese usuario debe tener acceso a las empresas donde habrá personas de campo, y permisos de inventario, proyecto y empleados.

## 2. APK

GitHub Actions compila el APK en cada cambio dentro de `apps/campo/` (workflow «App de campo»). Descárgalo en la pestaña Actions → la última ejecución → Artifacts → `campo-apk-N`.

Para que el APK apunte a tu puente sin teclear la dirección en cada teléfono, crea en GitHub la variable de repositorio `CAMPO_PUENTE_URL` (Settings → Secrets and variables → Actions → Variables) y vuelve a ejecutar el workflow. Sin la variable, la dirección se escribe una vez en la pantalla de inicio de sesión → «Servidor».

Firma: hoy usa una llave de pruebas incluida en el repositorio. Antes de distribuirla a todos, crea una llave propia y guárdala en los secretos `CAMPO_KEYSTORE_B64`, `CAMPO_KEYSTORE_PASSWORD`, `CAMPO_KEY_ALIAS` y `CAMPO_KEY_PASSWORD`. Cambiar de llave después obliga a desinstalar la app en cada teléfono.

## 3. Instalar en el teléfono

Android 8 o superior. Copia el APK al teléfono (WhatsApp, Drive o cable), ábrelo y acepta «Instalar apps de origen desconocido» la primera vez. Permisos: cámara (fotos) y ubicación (opcional, se agrega a cada registro si está disponible).

## Pruebas automáticas

```bash
cd apps/campo/puente && npx vitest run         # 15 pruebas del puente
cd apps/campo/android && ./gradlew testDebugUnitTest
```
