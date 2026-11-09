# Estudio de prefactibilidad: red neutral de kioscos de depósito en efectivo (cash-in multibanco) para casinos en México

**Versión:** 2.0 · **Fecha de investigación y consulta de fuentes:** 9 de noviembre de 2026
**Cambio respecto a la versión 1.0:** el modelo ya no es "kiosco que emite una tarjeta de un emisor". Ahora es el que definió Lucho: **un kiosco neutral donde el usuario elige el banco, la IFPE o el procesador al que quiere abonar su efectivo, o abre una cuenta nueva**, igual que los comercios que hoy reciben depósitos "en nombre de" Mercado Pago u otras entidades. La empresa es intermediaria entre las entidades financieras y el cliente, y no queda amarrada a un solo banco o procesador. La versión 1.0 queda en el historial del repositorio.
**Alcance:** México, salas de juego con permiso de SEGOB como ubicación inicial; el mismo kiosco puede operar fuera de casinos.
**Naturaleza:** prefactibilidad para decidir. No sustituye un dictamen jurídico, fiscal ni de cumplimiento.

> **Convenciones**
> - **[H] Hecho verificado:** fuente consultada el 9-nov-2026 y enlazada en el anexo A.
> - **[E] Estimación:** cálculo del consultor con supuestos explícitos. No es cotización.
> - **[S] Hipótesis:** razonable, no probada.
> - **[P] Pendiente de validación:** requiere dictamen, autoridad o confirmación comercial expresa.
>
> **Limitaciones.** Varios textos oficiales (DOF, portal de la CNBV) bloquean la lectura automática; su contenido se verificó con fuentes secundarias y se marca [P] donde falta confirmar contra el texto oficial. No se contactó a ninguna entidad, agregador ni proveedor: **no hay precios cotizados**.

---

## Resumen ejecutivo

**Recomendación: avanzar con condiciones.** El modelo de red neutral de depósito es legalmente viable en México y ya existe en la práctica: las tiendas reciben efectivo **a nombre de** bancos e IFPE como **comisionistas**, y hay **agregadores** (por ejemplo, tapi o PayCash) que conectan a muchas entidades con muchos puntos de depósito mediante una sola integración [H]. Un kiosco puede ocupar el lugar de esa tienda. Pero hay tres condiciones que deciden si el negocio funciona:

1. **"No amarrarse a un banco" se logra con tecnología, no con una figura legal propia.** Cada depósito tiene que hacerse por cuenta de una entidad regulada (banco o IFPE). La empresa puede ser **comisionista no exclusiva de varias entidades** y **administradora de comisionistas** (figura prevista por la CNBV) [H], y conectarse a varios **agregadores**. El kiosco funciona como un orquestador que muestra al usuario todas las entidades disponibles por cualquiera de esas vías. Lo que la empresa **no puede** hacer, salvo que obtenga un registro o autorización propia, es recibir el efectivo a nombre propio y luego "mandarlo" a la cuenta que el usuario quiera [H/P].
2. **Para abonar a "cualquier" cuenta, aun de entidades que no tienen convenio, existen dos rieles posibles,** cada uno con un costo regulatorio:
   - **Transferencia SPEI ordenada por un banco patrocinador** del que la empresa es comisionista: el usuario deposita efectivo y el banco envía un SPEI a la CLABE o tarjeta de débito que el usuario indique. La guía de la CNBV incluye las transferencias de fondos entre las operaciones que puede hacer un comisionista [H]; los límites para personas que no son clientes del banco están pendientes [P].
   - **Registro propio como transmisor de dinero** ante la CNBV (LGOAAC art. 81-A Bis, con dictamen técnico de PLD y renovación cada tres años) [H]. Da independencia, pero convierte a la empresa en sujeto supervisado en materia de PLD.
3. **La economía depende de que el efectivo no tenga que trasladarse.** Las redes como la de Mercado Pago funcionan porque el comercio **reutiliza el efectivo** y no hay traslado de valores; Mercado Pago le paga al comercio $4 por depósito o retiro [H]. Un kiosco con traslado de valores propio apenas cubre su costo variable en el escenario base [E]. **El casino es una ubicación ideal precisamente porque necesita efectivo** para su operación diaria: si el casino toma el efectivo del kiosco y lo liquida de forma electrónica el mismo día, el modelo mejora mucho. Ese esquema requiere dictamen, porque el casino estaría recibiendo efectivo destinado a cuentas financieras [P].

**Otros hallazgos:**

- **La "nueva ley de pagos digitales"** es la *Ley de Economía Digital para Pagos Digitales y Electrónicos* (DOF 4-nov-2026, vigente desde el 5-nov-2026) [H]. Para los privados la aceptación de pagos digitales es voluntaria (art. 11) salvo en los sectores que la SHCP declare obligatorios (art. 13), cuya lista se espera para finales de noviembre de 2026; no se han mencionado los casinos [H]. Es un viento a favor, no una obligación que cree demanda.
- **El vínculo con casinos sigue siendo el principal riesgo comercial.** Cada banco, IFPE o agregador decide si acepta puntos de depósito dentro de salas de juego. Los juegos con apuesta son actividad vulnerable con reglas nuevas desde el 30-nov-2026, y ha habido bloqueos de cuentas y sanciones a casinos [H]. **No debe suponerse que ninguna entidad acepta este giro sin confirmación escrita.**
- **El comisionista no puede cobrar comisiones propias al usuario** ni operar a nombre propio [H: guía de la CNBV]. Los ingresos vienen de las entidades (pago por depósito y por cuenta abierta) y, si acaso, de comisiones que la entidad registre y comparta.
- **Límites:** la guía de la CNBV menciona un tope de depósitos en efectivo vía comisionista de 4,000 UDIS diarios (≈ $35,000) [H, P: confirmar si es por cuenta]; las cuentas nivel 2 admiten 3,000 UDIS al mes y las 2 Bis, 15,000 UDIS con 3,000 en efectivo [H].
- **Números (todos estimados):** con traslado de valores propio, el escenario base deja una contribución de ≈ $7,300 MXN/kiosco/mes y requiere ≈ 109 kioscos para el equilibrio. Con el casino reciclando el efectivo, la contribución sube a ≈ $14,800 MXN, el equilibrio baja a ≈ 54 kioscos, el kiosco se paga en ≈ 14 meses y el financiamiento máximo en tres años es ≈ $51 M MXN [E]. El escenario conservador no alcanza equilibrio en ninguno de los dos esquemas.

**Condiciones para avanzar:**

| # | Condición | Evidencia | Antes de |
|---|---|---|---|
| 1 | Dictamen sobre la figura (comisionista y administrador de comisionistas, o transmisor de dinero), el riel SPEI para no clientes y el esquema de reciclaje con el casino | Opinión escrita de despacho financiero | Comprometer más de $1.5 M MXN |
| 2 | Un banco patrocinador (comisionista + SPEI) **o** un agregador que confirme por escrito que acepta kioscos dentro de casinos | Carta de intención o term sheet | Desarrollar integraciones |
| 3 | Al menos 3 entidades disponibles en el kiosco desde el día uno (directas o vía agregador), para que la neutralidad sea real | Contratos o cartas de intención | Lanzar el piloto |
| 4 | Dos casinos dispuestos a hospedar y, de preferencia, a reciclar el efectivo | Cartas de intención | Comprar hardware |

### Preguntas esenciales

1. **¿Qué entidades o agregadores tienen ya contactados o en mente?** Cambia la ruta de integración.
2. **¿Abonar a cuentas de entidades sin convenio (vía SPEI) es imprescindible desde el inicio, o basta con un catálogo de entidades conectadas?** Define si se necesita banco patrocinador con SPEI o registro de transmisor.
3. **¿Los casinos aceptarían tomar el efectivo del kiosco y liquidarlo electrónicamente?** Es la variable económica más importante.
4. **¿Se incluyen retiros de efectivo (cash-out)?** Mercado Pago los tiene y son el 57% de sus transacciones [H], pero en un kiosco exigen reciclador y más exposición de PLD.
5. **¿Cuánto capital hay disponible y en qué horizonte?**

Mientras no haya respuesta, el estudio supone: solo depósitos (cash-in) y apertura de cuentas; catálogo de entidades conectadas desde el inicio y riel SPEI como segunda etapa; casinos como ubicación con reparto de ingresos.

---

## 1. Definición y evaluación del modelo

### 1.1 Cómo funciona hoy el modelo de referencia

| Caso | Qué hace | Figura (verificada o probable) | Datos públicos |
|---|---|---|---|
| **Red de corresponsales de Mercado Pago** | Tiendas que reciben depósitos y entregan retiros en nombre de Mercado Pago | Comercios que actúan por cuenta de la entidad [P: figura exacta] | Más de 20,000 puntos (ene-2025); 60% cadenas (7-Eleven, Soriana, Neto, Farmacias del Ahorro, Circle K), 40% negocios locales; gratis para el usuario; **Mercado Pago paga $4 al comercio por operación**; depósito promedio $2,450; 57% retiros y 43% depósitos [H] |
| **tapi + OXXO + Banco Plata** | tapi actúa como capa de integración entre bancos y fintechs y los comercios (cash-in, cash-out, recargas) | Agregador | Banco Plata recibe depósitos en ≈ 24,000 OXXO vía tapi (may-2026); más de 40,000 puntos en total con su red previa [H] |
| **PayCash + albo** | Red de depósitos en efectivo por referencia en miles de establecimientos | Agregador | Comisión al usuario de $10 a $12 por depósito; topes de $5,000 a $20,000 por depósito según el lugar (jul-2024) [H] |
| **Paynet (Openpay)** | Pagos y depósitos referenciados en tiendas | Agregador [P] | No verificado en este estudio |

**Conclusión:** el mercado ya separó tres papeles: **la entidad regulada** (dueña de la cuenta), **el agregador** (integra entidades y puntos) y **el punto físico** (recibe el efectivo). La empresa puede ser punto físico, agregador o ambas cosas.

### 1.2 Las tres vías para que el kiosco sea multientidad

```
                         ┌──────────────── KIOSCO (orquestador neutral) ────────────────┐
 Usuario elige entidad → │  Vía 1: integración directa (comisionista de cada entidad)    │ → Banco A, IFPE B…
                         │  Vía 2: agregadores (tapi, PayCash, Paynet…)                  │ → entidades del agregador
                         │  Vía 3: riel SPEI (banco patrocinador o transmisor propio)    │ → cualquier CLABE / tarjeta
                         └──────────────────────────────────────────────────────────────┘
```

| Criterio | **Vía 1. Comisionista directo de varias entidades** (y administrador de comisionistas) | **Vía 2. Punto de depósito de agregadores** | **Vía 3a. Riel SPEI con banco patrocinador** | **Vía 3b. Transmisor de dinero propio** |
|---|---|---|---|---|
| Qué entidades alcanza | Las que firmen | Todas las conectadas al agregador | Cualquier CLABE o tarjeta de débito del SPEI | Cualquier destino (nacional) |
| Figura de la empresa | Comisionista no exclusivo; administrador de comisionistas si opera la red de kioscos o puntos de terceros [H: guía de la CNBV] | Subcomisionista o punto de recaudación del agregador [P: figura exacta en cada contrato] | Comisionista del banco patrocinador | Transmisor de dinero registrado ante la CNBV (LGOAAC art. 81-A Bis y 81-B) [H] |
| Autorizaciones | La entidad presenta un plan estratégico de negocio a la CNBV al menos 20 días hábiles antes y espera autorización [H] | Las tramita el agregador o la entidad [P] | Igual que la vía 1 | Dictamen técnico de PLD + registro; renovación cada 3 años [H] |
| Apertura de cuentas nuevas | **Sí**: la guía incluye la apertura de cuentas, incluso remota con validación de identidad [H] | Normalmente no (solo depósito) [S] | Solo cuentas del banco patrocinador | No: el transmisor no puede emitir fondos de pago electrónico [H] |
| ¿El usuario debe tener cuenta previa? | No, si la abre en el kiosco | Sí | Sí (en el banco destino) | Sí (en el destino) |
| Tiempo estimado | 6 a 12 meses por entidad [E] | 3 a 6 meses [E] | 6 a 12 meses [E] | 9 a 18 meses [E] |
| Ingreso para la empresa | Pago por operación y por cuenta abierta, negociado | Parte de la comisión del agregador | Pago del banco; comisión al usuario registrada por el banco [P] | Comisión propia al usuario |
| Riesgo principal | Negociar uno por uno; cada entidad decide sobre casinos | Margen menor; dependencia del agregador | Límites para no clientes; apetito del banco por casinos | Carga de PLD y supervisión; acceso a SPEI vía un banco |
| Veredicto | **Sí, para 2 a 4 entidades ancla** (abren cuentas y pagan mejor) | **Sí, para el catálogo amplio desde el inicio** | **Evaluar en la etapa 2** | **Opción de mediano plazo** si el volumen lo justifica |

> **Sobre la figura de "intermediario".** No existe una figura general de intermediario que permita recibir dinero de terceros y abonarlo a donde el usuario elija. Si la empresa recibe efectivo **a nombre propio** y luego lo transfiere, estaría haciendo transmisión de dinero (que exige registro) o, según el diseño, captación o emisión de fondos de pago electrónico sin autorización [P: el dictamen debe fijar la frontera y los artículos de sanción]. La forma de ser "intermediario" sin licencia propia es **actuar siempre por cuenta de una entidad identificada en cada operación**.

### 1.3 Actividades y responsables (modelo recomendado: vías 1 + 2, luego 3a)

| Actividad | Quién la realiza y con qué figura | Relación con el usuario | Custodia y responsabilidad | Contratos o permisos | Restricción por casinos |
|---|---|---|---|---|---|
| **Recepción y custodia del efectivo** | La empresa (o el casino, si recicla el efectivo), siempre **por cuenta de la entidad** elegida | De la entidad | La entidad responde ante el usuario y no puede delegar su responsabilidad [H: LIC art. 46 Bis 2]. La empresa responde ante la entidad o el agregador por el efectivo hasta entregarlo | Contrato de comisión (vía 1) o con el agregador (vía 2); traslado de valores o acuerdo con el casino | La entidad o el agregador puede rechazar puntos en casinos [P] |
| **Identificación del usuario** | La entidad define el nivel. El kiosco captura INE y prueba de vida cuando lo exija la operación o el monto | De la entidad | La entidad (expediente). La empresa como encargada de tratamiento de datos | Contrato de encargo; proveedor de identidad aceptado por cada entidad | Debida diligencia reforzada probable [S]. El casino tiene su propia obligación de identificar jugadores (LFPIORPI fr. I) [H] |
| **Apertura de cuenta** | La entidad, con la empresa como comisionista (vía 1) | De la entidad | La entidad | Contrato de comisión con la operación "apertura" | Nivel 2 / 2 Bis con límites [H] |
| **Abono** | La entidad acredita; la empresa transmite la instrucción y la referencia | De la entidad | La entidad ante el usuario; la empresa garantiza el efectivo en tránsito (prefondeo o línea) | Acuerdo de liquidación y garantía | — |
| **Transferencia SPEI a terceros (vía 3a)** | El banco patrocinador ordena el SPEI | Del banco patrocinador | El banco | Contrato de comisión + operación de transferencia | Límites para no clientes [P] |
| **Conciliación** | La empresa, por entidad y por agregador | — | Cada parte, con reglas de diferencias | SLA de conciliación con cada contraparte | El casino concilia su propio efectivo si recicla |
| **Atención y aclaraciones** | Primer nivel: la empresa. Responsable: cada entidad ante su UNE y la CONDUSEF [P] | De la entidad | La entidad | Protocolo por entidad | — |

### 1.4 Tarjeta de uso general frente a ecosistema cerrado

En este modelo **la empresa no emite instrumentos**: la tarjeta (si la hay) la entrega cada entidad a su cliente por sus propios canales. Por eso desaparece la discusión de la versión 1.0 sobre el dispensador de tarjetas y el BIN. Un saldo usable solo en el casino (ecosistema cerrado) queda fuera del alcance, porque Lucho lo descartó.

### 1.5 Modelo recomendado

**"Red neutral de cash-in" en tres capas, por etapas:**

1. **Etapa 1 (meses 0 a 12):** kioscos en casinos conectados a **uno o dos agregadores** (catálogo amplio de entidades para depósito) y a **dos o tres entidades directas** como comisionista (depósito + apertura de cuenta). La empresa actúa también como **administradora de comisionistas** para estructurar la red.
2. **Etapa 2 (meses 9 a 24):** **riel SPEI** con un banco patrocinador para abonar a cualquier CLABE o tarjeta de débito, si el dictamen y el banco lo permiten.
3. **Etapa 3 (mes 18 en adelante, opcional):** evaluar el **registro como transmisor de dinero** para no depender del banco patrocinador, y llevar la red a ubicaciones fuera de casinos.

En todas las etapas, el software del kiosco es un **orquestador**: cada entidad es un "conector" y el usuario ve un catálogo único. Esto cumple el objetivo de no quedar amarrado a un banco o procesador.

---

## 2. Análisis legal y regulatorio en México

### 2.1 Qué es la "nueva ley de pagos digitales"

| Instrumento | Estado al 9-nov-2026 | Qué dispone | Efecto en el proyecto |
|---|---|---|---|
| **Ley de Economía Digital para Pagos Digitales y Electrónicos** (DOF 4-nov-2026) [H] | **Vigente** desde el 5-nov-2026; 22 artículos y 5 transitorios [H] | Art. 2 fr. VII: medios de pago (QR, NFC, tarjetas, transferencias, domiciliación). Art. 10: las autoridades deben aceptarlos. **Art. 11: voluntario para los privados.** Art. 12: quien los implemente debe tener infraestructura e informar. **Art. 13: la SHCP define sectores estratégicos** donde el pago digital puede ser obligatorio. Art. 14: transiciones graduales. Arts. 15-16: efectivo en contingencias, no de forma recurrente. Arts. 17-20: inclusión financiera; Banxico y CNBV regulan, entre otros, la aceptación de QR. Transitorios: 15 días hábiles para la SHCP y otros 15 para las autoridades sectoriales [H] | **No obliga a dejar el efectivo** ni menciona casinos; la presidencia mencionó gasolineras y casetas [H]. Si la SHCP incluyera a los casinos, una red de cash-in **dentro** de la sala se volvería la forma natural de que los jugadores "digitalicen" su efectivo antes de jugar. Monitorear el DOF [P] |
| Reforma a la LFPIORPI (DOF 16-jul-2025) [H] | Vigente | Enfoque basado en riesgo, beneficiario controlador, conservación por 10 años, avisos en 24 horas, extensión a juegos con apuesta al amparo de autorizaciones [H] | Carga de cumplimiento del casino |
| Reglas de carácter general de la LFPIORPI (DOF 7-ago-2026) [H] | Vigencia general 30-nov-2026, escalonada hasta 2029 [H] | Metodología de riesgo y manual (1-mar-2027), herramientas automatizadas (1-jun-2027), auditoría anual (desde 2028) [H] | El casino querrá trazabilidad de su efectivo |
| Comisionistas de base tecnológica (DOF 11-jul-2024) [H] | Vigente | Bancos pueden operar con comisionistas a través de sitios o apps: apertura de cuentas nivel 2, créditos de hasta 3,000 UDIS, pagos y consultas; autenticación del cliente por el banco; plan estratégico aprobado por la CNBV [H] | Útil para la apertura de cuentas dentro del kiosco o mediante la app de la entidad [P: si aplica a kioscos] |
| Nivel 2 Bis (Circular 11/2026 de Banxico; DOF 10-jul-2026) [H] | Vigente; 18 meses de transición tecnológica | 15,000 UDIS al mes, de los cuales 3,000 en efectivo; apertura remota con prueba de vida [H] | Define cuánto efectivo puede recibir una cuenta de bajo nivel |
| Reforma al Reglamento de la LFJS (DOF 16-nov-2023) [H] | Vigente, impugnada | Prohíbe nuevas tragamonedas; permisos de 1 a 15 años [H] | Riesgo sobre el número de salas a futuro |
| Reforma a la LFJS | **Iniciativa**, no publicada [H] | Contenido final desconocido | Podría exigir trazabilidad o límites [P] |
| IEPS a juegos con apuesta | Vigente 2026 | Tasa de 30% a 50% según la reforma aprobada [H: prensa; P: texto y base] | Menos margen del casino |

### 2.2 Matriz regulatoria

| Actividad | Normativa | Autoridad | Obligación | Responsable | Evidencia | Pendiente |
|---|---|---|---|---|---|---|
| Recibir efectivo por cuenta de bancos | LIC arts. 46, 46 Bis 1 y 46 Bis 2; CUB arts. 318-334 y anexos 57-61 [H] | CNBV | Contrato de comisión mercantil; la institución presenta plan estratégico y obtiene autorización; recibos automáticos; **el comisionista no puede cobrar comisiones propias al cliente, subcontratar ni operar a nombre propio** [H] | Banco (responsable) y empresa (comisionista) | Autorización de la CNBV; contrato; manuales | Si un kiosco sin personal cumple el requisito de "establecimiento permanente y giro propio" del anexo 57; si una sala de juego puede ser establecimiento [P] |
| Ser comisionista de varios bancos | Guía de la CNBV sobre comisionistas [H] | CNBV | Se permite la no exclusividad. Si un banco contrata exclusividad, otro banco no puede contratar al mismo comisionista en los 12 meses siguientes [H] | Empresa | Contratos sin cláusula de exclusividad | — |
| Operar una red de puntos | Figura de administrador de comisionistas [H] | CNBV | Contrata a nombre de la institución con otros comisionistas | Empresa | Mandato o comisión de la institución | Si el casino, al reciclar el efectivo, debe ser comisionista administrado por la empresa [P] |
| Límites de depósito vía comisionista | CUB art. 323 [H: citado en la guía] | CNBV | 4,000 UDIS diarios en depósitos para bancos (≈ $35,000) [H] | Banco | Parametrización en el kiosco | Si el límite es por cuenta, por usuario o por comisionista [P] |
| Recibir efectivo por cuenta de IFPE | Ley Fintech art. 45 (solo recursos desde cuentas bancarias, salvo excepción de la CNBV) y art. 54 (terceros) [H]; Disposiciones aplicables a las IFPE (arts. 48, 54 y 56 de la Ley Fintech) [H] | CNBV, Banxico | La IFPE necesita la excepción para efectivo y autorización o aviso para sus comisionistas [H] | IFPE | Excepción y contrato | Qué IFPE ya tienen la excepción [P] |
| Agregadores | Contratos privados + régimen de la entidad final | — | El agregador estructura la relación con las entidades | Agregador y entidad | Contrato; anexo con la figura de cada punto | **Figura legal exacta de cada agregador y si permite subcomisionistas en casinos** [P] |
| Transferencia SPEI ordenada en el kiosco (vía 3a) | Operaciones de comisionista (transferencias de fondos y órdenes de pago) [H]; disposiciones del art. 115 LIC para usuarios no clientes [P] | CNBV, Banxico | Identificar al ordenante según el monto | Banco patrocinador | Expediente del usuario | Límites para no clientes en efectivo [P] |
| Transmisor de dinero (vía 3b) | LGOAAC arts. 81-A Bis, 81-B, 81-D, 86 Bis, 95 Bis; Disposiciones para transmisores (DOF 10-abr-2012 y reformas, la última de 5-abr-2023) [H] | CNBV, SHCP | Registro con dictamen técnico de PLD; identificación por montos (datos desde 500 USD, identificación desde 3,000 USD, expediente completo desde 5,000 USD); reportes; oficial de cumplimiento; auditoría anual; conservación por 10 años [H] | Empresa | Registro vigente | Si puede abonar a cuentas de terceros vía SPEI y con qué banco [P] |
| Naturaleza de la cuenta | LIC (depósito) o Ley Fintech arts. 22-29 (fondos de pago electrónico) [H] | CNBV, Banxico | Definida por cada entidad | Entidad | Contrato de adhesión | — |
| PLD de la entidad | LIC art. 115; Ley Fintech art. 58 | CNBV, UIF | Programa propio, perfil transaccional, reportes | Entidad | Manual | Exigencias reforzadas para puntos en casinos [P] |
| PLD del casino | LFPIORPI art. 17 fr. I; reglas (DOF 7-ago-2026) [H] | UIF, SAT | Identificar desde 325 UMA ($38,125.75); aviso desde 645 UMA ($75,664.95); acumulación en 24 horas y 6 meses [H] | Casino | Expediente y avisos | Si los registros del kiosco le sirven al casino [P] |
| Juego presencial | LFJS y Reglamento [H] | SEGOB | Moneda nacional (art. 10); sin crédito a apostadores (art. 11); apuestas en efectivo salvo medios electrónicos (art. 86) [H] | Casino | Permiso | Si un kiosco de terceros dentro de la sala requiere aviso o aprobación de SEGOB [P] |
| Datos personales y biometría | LFPDPPP (DOF 20-mar-2025) [H] | Secretaría Anticorrupción y Buen Gobierno [H] | Aviso de privacidad, consentimiento expreso para datos sensibles, derechos ARCO | Entidad (responsable), empresa (encargada) | Avisos y contratos | Tratamiento de la biometría facial en kiosco [P] |
| Protección al usuario | LPDUSF; LTOSF | CONDUSEF, Banxico | Comisiones registradas, UNE, aclaraciones | Entidad | Registros | — |
| Fiscal | CFF, LIVA, LISR | SAT | CFDI por comisiones cobradas a entidades y agregadores; **el efectivo de usuarios no es ingreso** | Empresa | Contabilidad de recursos de terceros | Tratamiento del efectivo en tránsito y del reciclaje con el casino [P] |
| Traslado de valores | Ley Federal de Seguridad Privada | SSPC | Solo empresas autorizadas | Proveedor | Permiso | — |

### 2.3 Distribución de obligaciones

| Parte | Obligaciones |
|---|---|
| **Empresa** | Operar kioscos seguros; actuar siempre por cuenta de la entidad elegida; custodiar el efectivo hasta entregarlo; no cobrar comisiones propias al usuario como comisionista; no subcontratar sin permiso; conciliación; primer nivel de atención; seguridad de la información; cumplimiento de los estándares de cada entidad y agregador. Si se registra como transmisor: todo el régimen de PLD propio. |
| **Casino** | Hospedar el kiosco; su propio régimen de LFPIORPI y de juego; si recicla el efectivo, cumplir el contrato de liquidación y lo que el dictamen determine sobre su figura [P]. |
| **Entidades (bancos e IFPE)** | Autorización del esquema de comisionistas; contrato con el usuario; KYC y PLD; acreditación; aclaraciones; supervisión de la empresa como comisionista. |
| **Agregadores** | Ruteo de referencias y liquidación con las entidades; reglas de su red. |

### 2.4 Asuntos que requieren dictamen antes de invertir

1. Figura de la empresa en cada vía: comisionista, administradora de comisionistas, subcomisionista de agregador o transmisor de dinero; frontera con la captación y la emisión de fondos de pago electrónico sin autorización.
2. Si un **kiosco desatendido** cumple los requisitos de comisionista (establecimiento permanente, giro propio) y si una **sala de juego** puede ser establecimiento de comisionista.
3. **Reciclaje del efectivo por el casino:** figura del casino (¿comisionista administrado?), quién custodia el efectivo y cómo se liquida.
4. **Riel SPEI para no clientes:** límites, identificación y si el banco puede ordenar transferencias a cualquier CLABE con efectivo recibido por comisionista.
5. Si el transmisor de dinero puede abonar a cuentas de terceros en México vía SPEI.
6. Biometría en kiosco y transferencia de datos a varias entidades.
7. Tratamiento fiscal del efectivo en tránsito y de las comisiones.

---

## 3. Mercado y benchmark

### 3.1 Contexto

| Dato | Valor | Tipo |
|---|---|---|
| Adultos con al menos un producto financiero (ENIF 2024) | 76.5% | [H] |
| Adultos con cuenta formal de ahorro (ENIF 2024) | 63.0% | [H] |
| Tendencia de pagos | Baja el efectivo como medio más frecuente; la mayor alza es de transferencias o apps en compras de más de $500 | [H] |
| Red de Mercado Pago | Más de 20,000 puntos; $4 por operación al comercio; depósito promedio $2,450; ≈ 2 operaciones por usuario al mes | [H] |
| Red tapi + OXXO para Banco Plata | ≈ 24,000 OXXO; más de 40,000 puntos con la red previa | [H] |
| Comisión al usuario en red PayCash (albo) | $10 a $12 por depósito | [H] (2024) |
| Salas de juego (AIEJA, 2024) | 425 salas, ≈ 100,000 máquinas; gasto diario por jugador ≈ $480 | [H] (gremial) |
| Escrutinio a casinos | 13 casinos con cuentas bloqueadas; 10 sancionados por OFAC/FinCEN | [H] |
| Traslado de valores | Reportes de retrasos en la devolución de efectivo resguardado por Servicio Panamericano/Brinks (ago-2026) | [H] (alegaciones) |

### 3.2 Segmentos y pagadores

| Segmento | Necesidad | ¿Paga? |
|---|---|---|
| **Usuario con cuenta (banco, IFPE, app)** | Depositar el efectivo que trae (o que ganó) en su cuenta sin buscar una tienda | En principio no. El comisionista no puede cobrarle comisiones propias; la entidad sí puede cobrar comisiones registradas [H/P] |
| **Usuario sin cuenta** | Abrir una cuenta y depositar en el mismo lugar | No; la entidad paga por la adquisición [S] |
| **Entidades (bancos, IFPE, neobancos)** | Más puntos de depósito, sobre todo para clientes que manejan efectivo; adquisición de clientes | **Sí**: pago por depósito y por cuenta abierta y fondeada [S, con referencia de $4 de Mercado Pago] |
| **Agregadores** | Más puntos en su red | Sí: comparten su comisión [S] |
| **Casino** | Menos efectivo circulando sin control, efectivo disponible para su operación, servicio para sus clientes | Recibe reparto de ingresos o, si valora el efectivo, puede ceder la ubicación sin costo [S] |

### 3.3 Competidores y sustitutos

| Solución | Propuesta de valor | Cobertura | Experiencia | Modelo de ingresos | Costos públicos | Relación con casinos | Limitaciones | Presencia |
|---|---|---|---|---|---|---|---|---|
| **Tiendas de conveniencia (OXXO, 7-Eleven y otras)** | Miles de puntos, horarios amplios | Nacional | Fila; referencia o número de tarjeta | Comisión al usuario o a la entidad | Variables por entidad [P] | Ninguna; están fuera de la sala | Topes por operación; no están dentro del casino | México, comprobada |
| **Redes de corresponsales de una entidad (Mercado Pago)** | Gratis para el usuario | Más de 20,000 puntos | Código en la app | La entidad paga $4 al comercio | Gratis al usuario [H] | Ninguna | Solo sirven para esa entidad | México, comprobada |
| **Agregadores (tapi, PayCash, Paynet)** | Una integración, muchas entidades | Decenas de miles de puntos | Referencia | Comisión por operación | $10 a $12 al usuario en el caso de albo/PayCash [H] | Ninguna conocida | Dependen de comercios de terceros | México, comprobada |
| **Sucursales y cajeros con depósito** | Seguridad | Amplia en ciudades | Cajero o ventanilla | Comisiones bancarias | Variables [P] | Ninguna | Solo para el banco dueño; horarios | México, comprobada |
| **Caja del casino** | Inmediata | Todas las salas | Fila | Interno | No aplica | Propia | No deposita en cuentas financieras | México, comprobada |
| **Kioscos de pago de servicios y recargas** | Autoservicio | Plazas y tiendas | Pantalla | Comisión al usuario | Variables [P] | Ocasional | No depositan en cuentas de cualquier entidad [S] | México, comprobada en general [S] |

**Diferencial de la propuesta [S]:** es el único punto de depósito **dentro de la sala** y **neutral** (varias entidades en el mismo kiosco), con apertura de cuenta en sitio. El riesgo es que, fuera de la sala, compite con decenas de miles de puntos gratuitos o baratos; por eso la ubicación en casinos es una ventaja y no solo un nicho.

### 3.4 Mercado potencial (supuestos explícitos)

| Nivel | Supuesto | Resultado [E] |
|---|---|---|
| Universo en casinos | 425 salas [H]; 1 a 2 kioscos por sala | 425 a 850 kioscos |
| Mercado accesible | 50% de las salas (grupos con controles de PLD sólidos y permiso con horizonte de más de 5 años) [S] | ≈ 210 salas; ≈ 300 kioscos |
| Alcanzable en 3 años (base) | ≈ 50% del accesible | 150 kioscos |
| Depósitos | 60 por kiosco al día × $1,200 promedio [S] | ≈ $2.16 M MXN por kiosco al mes; ≈ $3,900 M MXN al año con 150 kioscos — **dinero de usuarios, no ingreso** |
| Ingreso de la empresa | ≈ $29,700 por kiosco al mes (sección 7) | ≈ $53 M MXN al año con 150 kioscos |
| Expansión | El mismo kiosco puede ir a plazas, mercados y centrales de abasto | No incluida en la base |

---

## 4. Diseño operativo y experiencia del usuario

### 4.1 Recorrido del usuario

**A. Usuario con cuenta (depósito):**
1. Toca "Depositar" y elige la entidad en el catálogo (logos, búsqueda).
2. Se identifica según lo que pida esa entidad: número de tarjeta, CLABE, celular o un código o QR generado en la app de la entidad (lo más seguro, porque la entidad confirma la cuenta y el nombre antes de recibir el efectivo).
3. El kiosco muestra el nombre parcial del titular, los límites disponibles y la comisión que cobra la entidad, si la hay.
4. Introduce billetes; ve el acumulado; confirma.
5. El kiosco envía la instrucción por la vía correspondiente (directa, agregador o SPEI) e imprime el comprobante con folio y la entidad responsable.
6. El usuario recibe la notificación de su entidad.

**B. Usuario sin cuenta (apertura):**
1. Toca "Abrir cuenta" y elige entre las entidades que permiten apertura por comisionista.
2. Escanea su INE, se toma una selfie con prueba de vida, captura su celular y acepta el contrato y el aviso de privacidad **de la entidad** con firma electrónica.
3. La entidad decide (segundos o minutos). Si aprueba, el kiosco ofrece el primer depósito de inmediato; la tarjeta física o virtual la entrega la entidad por sus canales.
4. Alternativa más sencilla para la etapa 1: el kiosco muestra un QR que lleva a la apertura en la app de la entidad, y el usuario vuelve al kiosco para depositar.

**C. Depósito a cualquier CLABE o tarjeta (vía 3a, etapa 2):** el usuario captura la CLABE o los 16 dígitos de la tarjeta, se identifica según el monto, deposita, y el banco patrocinador envía el SPEI.

### 4.2 Movimiento del dinero y de la información

```
DINERO

Esquema A: traslado de valores propio
 Usuario --efectivo--> [KIOSCO/bóveda] --T+1 a T+3--> [Traslado de valores] --depósito--> [Cuenta de liquidación]
                        control: empresa,                                                     |
                        por cuenta de la entidad                                              v
 Instrucción ──> [Plataforma] ──> entidad directa / agregador / banco patrocinador (SPEI) ──> cuenta del usuario
                       ^                                                                      (disponible T+0)
                       └── prefondeo o línea garantizada de la empresa (cubre el efectivo aún físico)

Esquema B: el casino recicla el efectivo [P]
 Usuario --efectivo--> [KIOSCO/bóveda] --apertura con doble control--> [Bóveda del casino]
                                                                           |  SPEI mismo día por el importe
                                                                           v
                                                                    [Cuenta de liquidación] ──> entidades
```

| Etapa | Quién controla los fondos | ¿Disponible para el usuario? |
|---|---|---|
| Billete aceptado | Empresa, por cuenta de la entidad | No, hasta confirmar |
| Abono confirmado | La entidad (con la garantía de la empresa por el efectivo físico) | **Sí, de inmediato** (según la entidad o el agregador) |
| Efectivo en traslado o en la bóveda del casino | Proveedor de traslado o casino (riesgo contractual de la empresa) | Sí |
| Liquidación recibida en la cuenta de liquidación | Entidad o agregador | Sí; se libera la garantía |

> **Capital de trabajo [E]:** como el saldo se acredita antes de que llegue el efectivo, la empresa debe prefondear o dar garantía a cada contraparte: ≈ 3 días de volumen en el esquema A (≈ $216,000 por kiosco en el escenario base) o ≈ 1 día en el esquema B (≈ $72,000). Es colateral recuperable, no gasto. Con varias entidades y agregadores, conviene una **cuenta de liquidación única** y garantías por contraparte, lo que debe negociarse.

```
INFORMACIÓN

 Kiosco --(eventos firmados: sesión, billetes, identidad, entidad elegida)--> Plataforma (orquestador)
 Plataforma --(validación de cuenta, alta, abono)--> conector de cada entidad / agregador / banco SPEI
 Entidad --(confirmación, nombre del titular, límites, rechazos de PLD)--> Plataforma --> pantalla y comprobante
 Plataforma --(reportes de liquidación por contraparte)--> entidades, agregadores, casino, traslado de valores
```

### 4.3 Excepciones

| Excepción | Tratamiento |
|---|---|
| Billete rechazado o sospechoso | Devolución; si se retiene por sospecha, evento con video y protocolo de Banxico [P] |
| La entidad elegida no responde o está fuera de servicio | El catálogo la marca como no disponible **antes** de recibir efectivo; nunca se recibe efectivo sin confirmar la cuenta destino |
| Falla de conexión a mitad de la sesión | Comprobante de "abono pendiente" con folio y entidad; reintento idempotente; plazo máximo garantizado |
| Cuenta destino inválida o rechazada después de recibir el efectivo | Bloqueo previo con validación; si aun así ocurre, reembolso por el canal que defina el contrato con la entidad (otro kiosco, transferencia o vale) [P] |
| Límite de la cuenta excedido | El kiosco consulta el límite disponible antes del depósito y detiene la aceptación al llegar al tope |
| Identidad no validada (apertura o montos altos) | No se recibe efectivo |
| Fraude (cuenta de tercero, fraccionamiento, mulas) | Confirmación de titular, límites por usuario, kiosco y día; reglas de fraccionamiento compartidas con las entidades; bloqueo inmediato |
| Reclamaciones | Primer nivel de la empresa con folio y video; escalamiento a la UNE de la entidad responsable |

### 4.4 Depósitos, pagos, premios y retiros

| Función | ¿En el MVP? | Motivo |
|---|---|---|
| **Depósito a cuenta existente** | **Sí** | Núcleo del modelo |
| **Apertura de cuenta** | **Sí, en versión QR**; apertura completa en kiosco en la etapa 2 | Depende de las entidades directas |
| **Pago de servicios o referencias** | Opcional (vía agregador) | Ingreso adicional sin riesgo especial [S] |
| **Depósito a cualquier CLABE o tarjeta (SPEI)** | **Etapa 2** | Requiere banco patrocinador y dictamen |
| **Premios** | **No** | Los paga el casino con sus retenciones; podría pagar a cuenta por SPEI desde su propio banco sin pasar por el kiosco |
| **Retiros en efectivo** | **No; evaluar después** | Necesita reciclador, efectivo en sitio y más riesgo de PLD; en un casino es justamente el flujo más sensible |

---

## 5. Requerimientos tecnológicos y del kiosco

### 5.1 Arquitectura

```
[KIOSCO]                              [NUBE]
 App en modo kiosco          mTLS     API Gateway
 Validador + casete   ───────────────► Orquestador de sesiones (estado, idempotencia)
 Caja fuerte                           Catálogo de entidades (disponibilidad, límites, comisiones, logos)
 Escáner de INE + cámara               Conectores: entidad A | entidad B | agregador 1 | agregador 2 | banco SPEI
 Impresora, pantalla táctil            Servicio de identidad (INE, prueba de vida; enrutado a cada entidad)
 Red cableada + 4G/LTE, UPS            Motor de riesgo y límites (por usuario, kiosco, entidad)
                                       Libro de efectivo y liquidación por contraparte
                                       Consola de operación, soporte, cumplimiento y casino
                                       Monitoreo, SIEM, bitácora inmutable
```

| Componente | Requisitos clave |
|---|---|
| **Orquestador neutral** | Cada entidad o agregador es un conector con el mismo contrato interno (validar cuenta, consultar límite, abonar, consultar estado, revertir). Agregar una entidad no cambia el kiosco. **Esto es lo que evita quedar amarrado a un banco o procesador.** |
| **Catálogo** | Orden neutral (alfabético o por uso, no pagado, salvo que se transparente); disponibilidad en tiempo real |
| **Identidad** | Captura única de INE y prueba de vida, enviada a la entidad que la requiera con consentimiento separado por entidad |
| **Liquidación** | Libro por contraparte: efectivo recibido, abonos confirmados, liquidaciones enviadas, garantía disponible; alertas cuando la garantía de una contraparte se acerca al tope |
| **Conciliación** | Contador del validador + apertura de bóveda + conteo (traslado o casino) + liquidaciones + confirmaciones de cada entidad |

### 5.2 Hardware

Al no emitir tarjetas, el kiosco se simplifica:

| Elemento | Especificación |
|---|---|
| Validador de billetes | Multisensor, patrones de Banxico actualizables, casete con cerradura y bitácora |
| Caja fuerte | Rating comercial, cerradura electrónica con código de un solo uso, sensores, anclaje |
| Identificación | Escáner de INE y cámara para selfie con iluminación controlada |
| Pantalla, impresora, lector de QR | Para referencias y códigos generados en apps |
| Conectividad y energía | Cableado segmentado + 4G/LTE; UPS |
| **Opcional (etapa posterior)** | Reciclador de billetes si se agregan retiros |

### 5.3 Disponibilidad, seguridad y operación

- Disponibilidad objetivo: 98% en horario del casino por kiosco y 99.9% en la plataforma [E]. Sin conexión no se recibe efectivo.
- Seguridad física: zona vigilada fuera del área de máquinas, CCTV, doble control para la bóveda, órdenes de servicio con código de un solo uso.
- Seguridad digital: mTLS, llaves en TPM y HSM o KMS, cifrado de datos personales, segregación de la red del casino, MFA en la consola, pruebas de penetración antes del piloto.
- Recolección: esquema A (traslado de valores con 2 visitas por semana [S]) o esquema B (bóveda del casino con doble control y liquidación electrónica el mismo día) [P].
- Mantenimiento preventivo mensual; correctivo con SLA de 4 a 8 horas.

### 5.4 Comprar, adaptar o desarrollar

| Opción | Costo estimado [E] | Plazo [E] | Ventajas | Riesgos |
|---|---|---|---|---|
| **Comprar** kiosco y software de pago de servicios | $200,000 a $320,000 por kiosco + licencias | 3 a 5 meses | Rápido | El software de terceros no suele ser neutral ni multientidad |
| **Adaptar** (recomendado): gabinete de fabricante mexicano + plataforma propia | $160,000 a $230,000 por kiosco; desarrollo de $6 a $9 M | 6 a 9 meses | El orquestador neutral, que es el activo principal, queda en casa | Equipo técnico propio; certificaciones a cargo de la empresa |
| **Desarrollar** el hardware | Mayor inversión inicial | 12 a 18 meses | Menor costo a escala | Plazo |

### 5.5 Estándares aplicables

| Estándar | Alcance | Quién | ¿Aplica? |
|---|---|---|---|
| **PCI DSS v4.x** | Datos de tarjeta | Empresa si el kiosco recibe los 16 dígitos de una tarjeta para depositar (vía 3a o referencias por tarjeta) | Sí en ese caso; minimizar con tokenización o códigos generados por la app |
| **Requisitos de seguridad de cada entidad y de la CNBV para comisionistas** | Seguridad de la información, gestión de procesos, servidores, terceros | Empresa, por contrato | Sí [H: exigidos en el régimen de comisionistas] |
| **Prueba de vida y verificación aceptadas por la CNBV** | Apertura no presencial | Proveedor de identidad, a través de la entidad | Sí, para la apertura [H] |
| **ISO/IEC 27001, SOC 2 tipo II** | Gestión de seguridad | Empresa (voluntario) | Recomendable; las entidades lo piden |
| **GLI-20 (kioscos)** | Kioscos en casinos conectados al sistema de juego | Fabricante o integrador | **Probablemente no**, porque el kiosco no se conecta al sistema de juego [S/P] |
| **NOM eléctricas y de seguridad** | Equipo comercializado en México | Fabricante o importador | Sí [P] |
| **Ley Federal de Seguridad Privada** | Traslado de valores | Proveedor | Sí |

---

## 6. Aliados y proveedores

> Ningún proveedor fue contactado. **Que una entidad o agregador opere en México no significa que acepte puntos dentro de casinos.** Lo marcado **CE** requiere confirmación comercial expresa.

### 6.1 Candidatos

| Categoría | Candidatos públicos | En México | CE requerida |
|---|---|---|---|
| **Agregadores de cash-in** | tapi (integra bancos y fintechs con OXXO) [H]; PayCash (red de depósito usada por albo) [H]; Paynet/Openpay [P] | Sí | Aceptar kioscos propios como puntos; puntos dentro de casinos; garantía por punto; reparto de comisión |
| **Entidades directas (comisionista)** | Bancos con programas de corresponsales; neobancos e IFPE con estrategia de efectivo (por ejemplo, Mercado Pago, Banco Plata, albo, Nu, Klar, Spin y otros, según su figura vigente) | Sí | Contrato de comisión no exclusivo; apertura de cuentas; pago por operación y por cuenta; puntos en casinos |
| **Banco patrocinador SPEI** | Bancos con oferta de corresponsalía y banca transaccional | Sí | Transferencias a terceros con efectivo recibido por comisionista; límites para no clientes |
| **Identidad y prueba de vida** | Truora, Incode, MetaMap, Jumio y otros | Verificar | Funcionamiento en kiosco; aceptación por cada entidad |
| **Fabricación de kioscos** | Fabricantes e integradores mexicanos de kioscos de pago | Sí | Integración de validador, escáner y cámara; soporte nacional |
| **Validadores de billetes** | Fabricantes internacionales con distribuidor en México | Sí | Patrones de Banxico; servicio local |
| **Traslado de valores** | Cometra, Servicio Panamericano y regionales | Sí | Servicio a kioscos en casinos; debida diligencia por los reportes de 2026 [H] |
| **Mantenimiento** | Integradores con red técnica | Sí | SLA en ciudades piloto |

### 6.2 Prioridad

| # | Aliado | Meta a 90 días |
|---|---|---|
| 1 | **Agregadores (2)** | Confirmar si aceptan kioscos en casinos; propuesta económica |
| 2 | **Entidades directas (4 a 6 sondeadas)** | 2 cartas de intención (depósito + apertura) |
| 3 | **Casinos (10 a 15 entrevistados)** | 2 cartas de intención; posición sobre el reciclaje de efectivo |
| 4 | **Banco patrocinador SPEI** | Saber si la vía 3a es posible y en qué plazo |
| 5 | **Identidad** | 2 pruebas de concepto en kiosco |
| 6 | **Fabricante** | 3 cotizaciones |
| 7 | **Traslado de valores** | 2 cotizaciones por ciudad (solo si no hay reciclaje) |

### 6.3 Cuestionario para propuestas comparables

**A. Para todos:** razón social y autorizaciones; referencias en México; **¿aceptan que el servicio opere dentro de salas de juego con permiso de SEGOB? (respuesta por escrito)**; precios completos (únicos, mensuales, por transacción, mínimos); SLA; certificaciones; plazo.

**B. Agregadores:** entidades conectadas y cuáles permiten apertura de cuenta; figura legal de los puntos (comisionista, subcomisionista u otra); si aceptan un operador de kioscos como punto o como subagregador; garantía requerida por punto y forma de liquidación; reparto de la comisión; quién cobra al usuario y cuánto; topes por operación y por día; API (validación de cuenta antes de recibir efectivo, confirmación en tiempo real, reversos); manejo de aclaraciones.

**C. Entidades directas:** disposición a contratar a la empresa como comisionista **no exclusiva** y como administradora de comisionistas; operaciones (depósito, apertura, consulta); niveles de cuenta y límites; pago por depósito y por cuenta abierta y fondeada; garantía o prefondeo; requisitos de KYC en kiosco; debida diligencia a los casinos; plazo para obtener la autorización de la CNBV.

**D. Banco patrocinador SPEI:** transferencias a terceros con efectivo recibido por comisionista; límites e identificación para no clientes; costo por SPEI; horario y tiempo de acreditación.

**E. Fabricante, identidad y traslado:** como en la versión 1.0 (componentes, SDK, precio por volumen; tasas de aprobación en kiosco; costo por visita, seguro y plazo de depósito).

---

## 7. Modelo de negocio y evaluación financiera

### 7.1 Ingresos y quién paga

| Ingreso | Quién paga | Por qué | Referencia |
|---|---|---|---|
| **Pago por depósito** | Entidad o agregador | Le da un punto de depósito en un lugar donde sus clientes traen efectivo | Mercado Pago paga $4 por operación a sus comercios [H] |
| **Comisión registrada al usuario, compartida** | Usuario, cobrada por la entidad | Conveniencia | En la red PayCash/albo el usuario paga $10 a $12 [H]. El comisionista no puede cobrar comisiones propias [H] |
| **Pago por cuenta abierta y fondeada** | Entidad | Adquisición de clientes | No hay dato público; estimación [E] |
| **Pago de servicios y referencias** | Agregador | Uso adicional del kiosco | No modelado |
| **Reparto a la ubicación** | **Costo** para la empresa (pago al casino) | El casino cede espacio y, en el esquema B, recicla el efectivo | Estimación [E] |

> **El efectivo de usuarios no es ingreso.** Se registra como recursos de terceros en tránsito, y la garantía es un activo recuperable.

### 7.2 Supuestos (estimaciones [E], nada cotizado)

| Variable | Conservador | Base | Optimista |
|---|---|---|---|
| Kiosco + instalación | 230,000 + 25,000 | 190,000 + 20,000 | 160,000 + 18,000 |
| Desarrollo e integración (único) | 9,000,000 | 7,000,000 | 6,000,000 |
| Legal y cumplimiento (único) | 3,500,000 | 2,500,000 | 2,000,000 |
| Costo fijo mensual | 950,000 | 800,000 | 750,000 |
| Kioscos al cierre de los años 1, 2 y 3 | 10 / 30 / 50 | 20 / 70 / 150 | 40 / 120 / 250 |
| Depósitos por kiosco al día | 30 | 60 | 110 |
| Depósito promedio | $1,000 | $1,200 | $1,400 |
| Pago de la entidad o agregador por depósito | $7 | $9 | $11 |
| Parte de la comisión al usuario que recibe la empresa | $0 | $5 | $8 |
| Cuentas abiertas y fondeadas por kiosco al mes / pago por cuenta | 20 / $60 | 50 / $90 | 90 / $120 |
| Reparto al casino (% del ingreso) | 20% (A) · 15% (B) | 15% (A) · 10% (B) | 12% (A) · 7% (B) |
| Traslado de valores por kiosco al mes | 12,000 (A) · 1,000 (B) | 7,000 (A) · 1,000 (B) | 5,000 (A) · 1,000 (B) |
| Mantenimiento / conectividad / seguro al mes | 3,500 / 1,000 / 1,800 | 3,000 / 900 / 1,600 | 2,500 / 800 / 1,400 |
| Costo por transacción (agregador + identidad promedio) | $4.50 | $3.00 | $2.30 |
| Días de garantía | 4 (A) · 1 (B) | 3 (A) · 1 (B) | 2 (A) · 1 (B) |

### 7.3 Resultados por kiosco (mensual, régimen estable)

| Concepto (MXN/mes) | Conservador A | Conservador B | **Base A** | **Base B** | Optimista A | Optimista B |
|---|---|---|---|---|---|---|
| Depósitos | 900 | 900 | 1,800 | 1,800 | 3,300 | 3,300 |
| Efectivo recibido (**no ingreso**) | 900,000 | 900,000 | 2,160,000 | 2,160,000 | 4,620,000 | 4,620,000 |
| Ingreso por depósitos | 6,300 | 6,300 | 25,200 | 25,200 | 62,700 | 62,700 |
| Ingreso por cuentas | 1,200 | 1,200 | 4,500 | 4,500 | 10,800 | 10,800 |
| **Ingreso total** | **7,500** | **7,500** | **29,700** | **29,700** | **73,500** | **73,500** |
| Reparto al casino | 1,500 | 1,125 | 4,455 | 2,970 | 8,820 | 5,145 |
| Otros costos variables | 22,350 | 11,350 | 17,900 | 11,900 | 17,290 | 13,290 |
| **Contribución por kiosco** | **−16,350** | **−4,975** | **7,345** | **14,830** | **47,390** | **55,065** |
| Margen por depósito | −$18.17 | −$5.53 | $4.08 | $8.24 | $14.36 | $16.69 |
| Recuperación del kiosco (meses) | No se recupera | No se recupera | 29 | 14 | 4 | 3 |
| Garantía por kiosco (recuperable) | 120,000 | 30,000 | 216,000 | 72,000 | 308,000 | 154,000 |

### 7.4 Resultados de la empresa (3 años)

| Indicador | Conservador A | Conservador B | **Base A** | **Base B** | Optimista A | Optimista B |
|---|---|---|---|---|---|---|
| Inversión inicial (desarrollo + legal) | 12.5 M | 12.5 M | 9.5 M | 9.5 M | 8.0 M | 8.0 M |
| Kioscos para el equilibrio operativo | No alcanza | No alcanza | ≈ 109 | ≈ 54 | ≈ 16 | ≈ 14 |
| Flujo acumulado años 1 / 2 / 3 (incluye kioscos y garantía), M MXN | −28.6 / −51.5 / −78.2 | −27.0 / −45.3 / −64.8 | −26.7 / −53.7 / −87.7 | −23.0 / −38.7 / −51.2 | −25.1 / −27.5 / +5.6 | −17.1 / +0.2 / +70.3 |
| Necesidad máxima de financiamiento | > 78 M | > 65 M | ≈ 88 M (≈ 32 M es garantía) | ≈ 51 M (≈ 11 M es garantía) | ≈ 28 M | ≈ 17 M |
| EBITDA del año 3 | −21.2 M | −14.4 M | +3.6 M | +17.1 M | +133.2 M | +156.2 M |

### 7.5 Sensibilidad (Base B; contribución por kiosco y kioscos para el equilibrio)

| Variación | Contribución | Kioscos para equilibrio |
|---|---|---|
| Base B | $14,830 | 54 |
| Depósitos por día −25% | $10,510 | 76 |
| Pago por depósito −25% | $11,185 | 72 |
| Sin parte de la comisión al usuario | $6,730 | 119 |
| Pago por cuenta −50% | $12,805 | 62 |
| Reparto al casino al doble (20%) | $11,860 | 67 |
| Traslado propio en lugar de reciclaje (Base A) | $7,345 | 109 |

**Lectura:**
- Para que la red funcione con traslado propio hacen falta **≈ $17 de ingreso neto por depósito** o **≈ 85 depósitos diarios por kiosco** [E]. Las redes de tiendas funcionan con $4 porque no tienen traslado ni inversión en hardware.
- **El reciclaje del efectivo por el casino es la palanca que convierte el proyecto en viable** en el escenario base, porque elimina la mayor parte del traslado y reduce la garantía. Por eso el casino no es solo una ubicación: es la contraparte que "absorbe" el efectivo, como lo hace un OXXO.
- La posibilidad de compartir una comisión cobrada al usuario pesa mucho. Si las entidades no la permiten, el modelo depende del volumen.

---

## 8. Plan de desarrollo y recomendación

### 8.1 Etapas

| Etapa | Entregables | Responsables | Presupuesto [E] | Duración [E] | Dependencias | Aprobación |
|---|---|---|---|---|---|---|
| **0. Validación jurídica** | Dictamen (sección 2.4) | Fundadores + despacho | $0.6 a $1.2 M | 6 a 10 semanas | — | Ruta legal para las vías 1 y 2 y para el reciclaje del casino |
| **1. Validación comercial** | Entrevistas con agregadores, entidades y casinos; cartas de intención | Fundadores | $0.3 a $0.6 M | 8 a 12 semanas (en paralelo) | — | 1 agregador + 2 entidades + 2 casinos por escrito |
| **2. Selección de aliados** | RFP (6.3), evaluación, contratos marco | Fundadores + asesor | $0.3 M | 6 a 8 semanas | 0 y 1 | Ingreso neto por depósito compatible con el modelo |
| **3. Diseño** | UX del catálogo neutral, arquitectura del orquestador, modelo de riesgos, manuales | Producto y tecnología | $0.8 a $1.2 M | 8 semanas | 2 | Aprobación de entidades y casino |
| **4. Prototipo** | 2 kioscos en laboratorio con 1 agregador y 1 entidad en ambiente de pruebas | Tecnología + fabricante | $1.2 a $2.0 M | 10 a 12 semanas | 3 | Pruebas de extremo a extremo; pentest sin hallazgos críticos |
| **5. Integración y autorizaciones** | Conectores productivos; autorización de la CNBV del esquema de comisionistas (la tramita cada entidad) | Tecnología + entidades | $2 a $3.5 M | 3 a 6 meses | 2 y 4 | Autorizaciones obtenidas |
| **6. Piloto** | 4 a 6 kioscos en 2 salas, 90 días, con al menos 3 entidades disponibles | Operación + casinos | $1.5 a $2.5 M (incluye hardware) | 3 meses | 5 | Métricas 8.2 |
| **7. Escalamiento** | 20 kioscos al año 1 y 70 al año 2 (base); riel SPEI | Toda la empresa | Sección 7 | 12 a 24 meses | Piloto + financiamiento | Contribución ≥ $12,000 por kiosco al mes |

### 8.2 MVP y métricas del piloto

**MVP:** kiosco con catálogo neutral de al menos 3 entidades (directas o vía agregador), depósito con validación previa de la cuenta destino, comprobante con entidad responsable, apertura de cuenta por QR, consola de liquidación por contraparte. Sin retiros ni premios.

| Métrica | Meta |
|---|---|
| Depósitos por kiosco al día (promedio de los últimos 30 días) | ≥ 50 |
| Depósito promedio | ≥ $1,000 |
| Entidades con uso real (≥ 10% de los depósitos cada una) | ≥ 2 |
| Tiempo de depósito (usuario con código de la app) | ≤ 45 segundos |
| Abonos pendientes > 15 minutos | < 0.1% |
| Diferencias de efectivo no explicadas | 0 por conciliación semanal |
| Disponibilidad | ≥ 98% |
| Reclamaciones por cada 1,000 operaciones | < 2 |
| Recurrencia (usuarios con 2 o más depósitos en 30 días) | ≥ 40% |
| Cuentas abiertas y fondeadas por kiosco al mes | ≥ 30 |
| Casino dispuesto a reciclar el efectivo después del piloto | Sí, por escrito |
| Incidentes graves de PLD o fraude | 0 |

### 8.3 Recomendación

**Avanzar con condiciones**, con el modelo de red neutral de cash-in:

1. Estructurar a la empresa como **comisionista no exclusiva y administradora de comisionistas**, conectada además a **agregadores**, con un **orquestador propio** que haga al kiosco independiente de cualquier banco o procesador.
2. Negociar con los casinos el **reciclaje del efectivo** como condición económica central.
3. Añadir el **riel SPEI** con banco patrocinador en la etapa 2, y evaluar el **registro como transmisor de dinero** solo si el volumen lo justifica.
4. **Reformular o detener** si en 90 días ningún agregador ni entidad acepta kioscos en casinos, o si el ingreso neto por depósito confirmado queda por debajo de ≈ $10 sin reciclaje de efectivo.

### 8.4 Riesgos y medidas

| Riesgo | Probabilidad / impacto | Medida |
|---|---|---|
| Entidades y agregadores rechazan puntos en casinos | Alta / crítico | Sondeo amplio; paquete de controles de PLD; plan B de ubicaciones fuera de casinos |
| Ingreso por depósito insuficiente (benchmark de $4) | Alta / alto | Reciclaje con el casino; pago por cuenta abierta; comisión compartida; volumen mínimo en contrato |
| La CNBV no acepta kioscos desatendidos como comisionistas | Media / alto | Dictamen; consulta a través de la entidad; el casino como comisionista administrado [P] |
| Uso de la red para lavado (mulas, fraccionamiento) | Media / crítico | Validación de titular, límites, monitoreo compartido, sin retiros en el MVP |
| Concentración en un solo agregador | Media / medio | Al menos 2 agregadores + entidades directas |
| Robo o faltantes | Media / medio | Seguro, bóveda certificada, doble control |
| Fallas del traslado de valores | Media / medio | Reciclaje del casino; dos proveedores |
| Reforma a la LFJS o vencimiento de permisos | Media / alto | Diversificar salas y ubicaciones |
| Sanciones a casinos anfitriones | Media / alto | Debida diligencia de los casinos antes de firmar |
| Datos y biometría | Media / alto | Privacidad desde el diseño; consentimiento por entidad |

### 8.5 Decisiones antes de invertir

1. Vías de inicio: agregadores + entidades directas (recomendado) o también SPEI desde el día uno.
2. Figura legal (según el dictamen) y si se buscará el registro de transmisor de dinero.
3. Esquema de efectivo: traslado propio o reciclaje con el casino.
4. Política de comisiones al usuario (gratis frente a comisión registrada y compartida).
5. Si los retiros (cash-out) entran en la hoja de ruta.
6. Comprar frente a adaptar el kiosco.
7. Financiamiento por etapas y quién aporta la garantía de liquidación.

### 8.6 Plan de 90 días

| Semanas | Actividades | Entregable |
|---|---|---|
| 1 a 2 | Contratar despacho; preparar presentación para agregadores, entidades y casinos; monitorear la lista de sectores de la SHCP | Carta de encargo; presentación |
| 3 a 6 | Reuniones con 2 a 3 agregadores, 4 a 6 entidades y 10 a 15 casinos (volumen de efectivo, disposición a reciclarlo) | Mapa de interés |
| 5 a 8 | Dictamen preliminar; RFP enviada | Dictamen preliminar; RFP |
| 7 a 10 | Cartas de intención; modelo financiero v3 con números reales de las propuestas | 1 + 2 + 2 cartas de intención; modelo v3 |
| 10 a 12 | Dictamen final; diseño del piloto; **comité de decisión: avanzar, reformular o detener** | Decisión documentada |

Presupuesto de los 90 días: **$1.2 a $2.0 M MXN** [E].

---

## Anexo A. Fuentes consultadas (9 de noviembre de 2026)

**Modelo cash-in y comisionistas**
- CNBV, *Guía para la autorización de contratación de comisionistas* (v1.0, ago-2017): https://www.cnbv.gob.mx/SECTORES-SUPERVISADOS/BANCA-MULTIPLE/Documents/Guia_Comisionistas_agosto_2017.pdf
- Kavanagh, comisionistas de base tecnológica (DOF 11-jul-2024): https://www.k-g.com.mx/en/client-alert-la-cnbv-publica-modificaciones-para-instituciones-de-credito-para-permitir-la-contratacion-de-operaciones-por-medio-de-comisionistas-digitales/
- LIC art. 46 Bis 2: https://mley.mx/LIC/articulo/46-bis%202/index.html.md
- DPL News, red de corresponsales de Mercado Pago (7-feb-2025): https://dplnews.com/?p=264698
- Latam Fintech, Banco Plata + tapi + OXXO (may-2026): https://www.latamfintech.co/articles/banco-plata-habilita-en-mexico-depositos-y-pagos-en-efectivo-en-oxxo-a-traves-de-tapi
- Xataka, albo + PayCash (jul-2024): https://www.xataka.com.mx/empresas-y-economia/albo-va-nu-oxxo-sabe-como-pelear-red-masiva-depositos-efectivo-mexico
- KYC Systems, transmisores de dinero (fuente comercial; cita LGOAAC y disposiciones): https://kyc-systems.com/blog/transmisor-de-dinero
- Descubre.vc, licencias bancarias de Nu, Mercado Pago y Klar (dic-2025): https://www.descubre.vc/noticia/nu-mercado-pago-y-klar-ingresan-al-sector-bancario-mexicano-en-2026-2025-12-29

**Ley de Economía Digital para Pagos Digitales y Electrónicos**
- DOF 4-nov-2026 (no legible por lectura automática): https://dof.gob.mx/nota_detalle.php?codigo=5803017&fecha=04%2F11%2F2026
- PYME Noticias: https://www.pymenoticias.com/post/mexico-ley-pagos-digitales-dof-hacienda-sectores
- Infobae: https://www.infobae.com/mexico/2026/11/05/publican-en-el-dof-la-ley-de-economia-digital-hacienda-tiene-15-dias-para-definir-quien-ya-no-aceptara-efectivo/
- El Heraldo de Saltillo: https://elheraldodesaltillo.mx/2026/11/04/publican-ley-de-pagos-digitales-obligatorios/

**PLD, juego y regulación financiera**
- EY, reforma LFPIORPI (16-jul-2025): https://www.ey.com/es_mx/technical/tax/boletines-fiscales/reforma-ley-antilavado-2025-nuevas-obligaciones
- Holland & Knight, Reglas de carácter general (7-ago-2026): https://www.hklaw.com/en/insights/publications/2026/08/nuevas-reglas-de-caracter-general-de-la-ley-antilavado-en-mexico
- SAT, umbrales: https://sppld.sat.gob.mx/pld/interiores/umbrales.html
- Reglamento de la LFJS: https://www.diputados.gob.mx/LeyesBiblio/regley/Reg_LFJS.pdf · texto por artículos: https://mley.mx/Reg_LFJS/index.html.md
- Greenberg Traurig, reforma al RLFJS (2023): https://www.gtlaw.com/es/insights/2023/11/reformas-y-adiciones-de-diversas-disposiciones-al-reglamento-de-la-ley-federal-de-juegos-y-sorteos
- Revista Casino Perú, reforma a la LFJS (sep-2026): https://www.revistacasinoperu.com/mexico-reforma-a-la-ley-federal-juegos
- Focus Gaming News, IEPS al 50%: https://focusgn.com/latinoamerica/diputados-mexicanos-aprueban-la-suba-del-ieps-que-eleva-la-carga-impositiva-a-los-juegos-de-azar-a-un-50
- Ley Fintech (arts. 11, 22-29, 45, 54): https://www.ordenjuridico.gob.mx/Documentos/Federal/html/wo120920.html
- Disposiciones aplicables a las IFPE (CNBV): https://www.cnbv.gob.mx/Normatividad/Disposiciones%20aplicables%20a%20las%20instituciones%20de%20fondos%20de%20pago%20electr%C3%B3nico%20a%20que%20se%20refieren%20los%20art%C3%ADculos%2048,%20segundo%20p%C3%A1rrafo;%2054,%20primer%20p%C3%A1rrafo%20y%2056,%20primer.pdf
- Nivel 2 Bis (fuente secundaria): https://kyc-systems.com/blog/cuenta-nivel-2-bis
- Garrigues, LFPDPPP (20-mar-2025): https://www.garrigues.com/es_ES/noticia/mexico-nueva-ley-federal-proteccion-datos-personales-posesion-particulares-introduce

**Mercado**
- INEGI, ENIF 2024: https://www.inegi.org.mx/contenidos/saladeprensa/boletines/2025/enif/ENIF2024_CP.pdf
- UnoTV, cifras de la AIEJA: https://www.unotv.com/negocios/cuanto-generan-los-casinos-y-que-revisan-hoy-las-autoridades/
- La Crónica, traslado de valores (ago-2026): https://www.cronica.com.mx/negocios/2026/08/04/clientes-reportan-presuntos-retrasos-en-la-entrega-de-efectivo-resguardado-por-servicio-panamericano/

## Anexo B. Pendientes de validación

1. Lista de sectores de la SHCP (art. 13 de la Ley de Economía Digital) y texto oficial del DOF.
2. Si un kiosco desatendido puede ser comisionista y si una sala de juego puede ser establecimiento.
3. Alcance del límite de 4,000 UDIS diarios (por cuenta, usuario o comisionista) y texto vigente de los arts. 319 y 323 de la CUB.
4. Figura legal de los puntos de cada agregador y si admiten operadores de kioscos en casinos.
5. Límites para que un banco ordene SPEI con efectivo de no clientes recibido por comisionista.
6. Si un transmisor de dinero puede abonar a cuentas de terceros en México vía SPEI.
7. Figura del casino si recicla el efectivo del kiosco.
8. Qué IFPE tienen excepción de la CNBV para recibir efectivo.
9. Figura regulatoria vigente de Mercado Pago y de otros neobancos (las licencias bancarias estaban en trámite a dic-2025).
10. Todos los precios: pagos por depósito y por cuenta, costos de agregadores, kioscos, traslado e identidad.
