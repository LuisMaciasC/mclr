# Estudio de prefactibilidad: kioscos de conversión de efectivo a pago digital para casinos en México

**Versión:** 1.0 · **Fecha de investigación y consulta de fuentes:** 9 de noviembre de 2026
**Alcance:** México, juego presencial (salas con permiso de SEGOB) y, en segundo plano, juego en línea.
**Naturaleza del documento:** análisis de prefactibilidad para tomar decisiones. No sustituye un dictamen jurídico, fiscal ni de cumplimiento.

> **Convenciones usadas en todo el documento**
>
> - **[H] Hecho verificado:** viene de una fuente consultada el 9-nov-2026 y enlazada.
> - **[E] Estimación:** cifra calculada por el consultor con supuestos explícitos. No es una cotización.
> - **[S] Hipótesis:** planteamiento razonable que todavía no está probado.
> - **[P] Pendiente de validación:** requiere dictamen, confirmación de una autoridad o confirmación comercial expresa.
>
> **Limitación de la investigación.** Se consultaron fuentes en línea el 9-nov-2026. Varios textos oficiales (en especial el DOF) no pudieron leerse directamente porque el sitio bloquea la lectura automática. En esos casos el contenido se verificó con al menos dos fuentes secundarias (despachos, medios especializados) y se marca como **[P]** la confirmación contra el texto publicado. Ningún proveedor fue contactado: **no hay precios cotizados en este estudio**. Todas las cifras de costo son estimaciones de orden de magnitud.

---

## Resumen ejecutivo

**Recomendación: avanzar con condiciones.** El problema que se quiere resolver existe y el entorno regulatorio de 2026 empuja en la dirección correcta, pero el proyecto, tal como está planteado (kiosco en casino que recibe efectivo, abre una cuenta y entrega una tarjeta Visa o Mastercard de uso general), **no puede ejecutarlo la empresa por sí sola** y depende de que un banco o una IFPE acepte expresamente el giro de casinos. Esa aceptación es hoy la principal incógnita del proyecto.

**Lo que se encontró:**

1. **La "nueva ley de pagos digitales" sí existe, pero no obliga a sustituir el efectivo en casinos.** Es la *Ley de Economía Digital para Pagos Digitales y Electrónicos*, publicada en el DOF el 4-nov-2026 y vigente desde el 5-nov-2026 [H]. Para los particulares, aceptar pagos digitales es voluntario (art. 11) salvo en los sectores que la SHCP declare "estratégicos" (art. 13), lo cual debe ocurrir dentro de 15 días hábiles (aprox. finales de noviembre de 2026). A la fecha de consulta no hay lista de sectores publicada, y las declaraciones oficiales mencionan gasolineras y casetas, no casinos [H]. **La ley es un viento a favor comercial, no una obligación que cree demanda forzosa.** Si la SHCP incluyera a los casinos, la oportunidad crecería mucho; hay que monitorearlo.
2. **Recibir efectivo de terceros y abonarlo a una "cuenta" es una actividad reservada.** Solo pueden hacerlo bancos (captación) o IFPE autorizadas por la CNBV (emisión de fondos de pago electrónico, Ley Fintech art. 22) [H]. La empresa puede participar como **comisionista** de esa entidad (LIC art. 46 Bis 1 y 46 Bis 2; Ley Fintech art. 54) [H] o como proveedor tecnológico, pero **no puede custodiar ni registrar saldos de usuarios a nombre propio** sin autorización. Una IFPE, además, solo puede recibir efectivo si la CNBV lo autoriza como excepción (Ley Fintech art. 45) [H], lo que hace que **un banco sea, en principio, el aliado más directo para el componente de efectivo** [S].
3. **El casino es el eslabón de mayor riesgo regulatorio para los aliados.** Los juegos con apuesta son actividad vulnerable (LFPIORPI art. 17 fr. I) con reglas nuevas de enfoque basado en riesgo que entran en vigor el 30-nov-2026 [H]; 13 casinos tuvieron cuentas bloqueadas y 10 fueron sancionados por OFAC/FinCEN [H]; Visa clasifica el juego no presencial como categoría de alto riesgo "Tier 1" que exige registro y aprobación específica del adquirente [H]. **No debe suponerse que ningún emisor acepta este giro sin confirmación escrita.**
4. **Una cuenta de bajo nivel transaccional limita el producto.** Las cuentas nivel 2 permiten abonos de hasta 3,000 UDIS al mes (≈ $26,400 MXN) y el nuevo nivel 2 Bis, hasta 15,000 UDIS con un tope de 3,000 UDIS en efectivo (DOF 10-jul-2026) [H]. Para clientes frecuentes de casino que depositan más, el producto necesitará cuentas de expediente completo (nivel 3 o 4), lo que encarece el registro en kiosco [S].
5. **Existe un camino de entrada de menor riesgo:** empezar con un **kiosco "cashless" de ecosistema cerrado** dentro del casino (efectivo → saldo del jugador en el sistema del casino → tarjeta de jugador), en el que el casino conserva la relación y los recursos, y la empresa es proveedor tecnológico y operador. Ese modelo no requiere licencia financiera propia [S/P], genera ingresos por renta y operación, y construye la base instalada y los datos para el segundo paso: la **tarjeta prepagada de red abierta** emitida por un banco o IFPE aliado.
6. **La economía unitaria es estrecha.** En el escenario base estimado, cada kiosco genera ≈ $41,700 MXN/mes de ingresos y ≈ $15,500 MXN/mes de contribución; el punto de equilibrio de la empresa requiere ≈ 58 kioscos operando, y el financiamiento máximo necesario en tres años es ≈ $67 millones MXN, incluidos ≈ $17 millones de colateral de prefondeo recuperable [E]. El escenario conservador no alcanza equilibrio. Las variables que más mueven el resultado son la renta mensual que pague el casino, el número de cargas por kiosco, el costo de recolección de efectivo y el costo por cuenta activa del emisor.

**Condiciones para avanzar (puertas de decisión):**

| # | Condición | Cómo se demuestra | Antes de |
|---|---|---|---|
| 1 | Dictamen jurídico sobre la figura (comisionista o proveedor tecnológico), sobre la naturaleza del saldo y sobre el art. 86 del Reglamento de la LFJS | Opinión escrita de despacho especializado en regulación financiera y de juego | Comprometer más de $1.5 M MXN |
| 2 | Al menos un banco o IFPE que **confirme por escrito** su disposición a operar el programa con casinos, con términos indicativos | Carta de intención o term sheet | Desarrollar integración financiera |
| 3 | Dos o más permisionarios dispuestos a un piloto pagado o con costo compartido | Carta de intención con sala, número de kioscos y cuota | Comprar hardware |
| 4 | Monitoreo de la determinación de sectores de la SHCP y de la reforma a la LFJS | Publicación en DOF | Actualizar el caso de negocio |

**Decisión sugerida para los próximos 90 días:** invertir ≈ $1.2 a $2.0 millones MXN [E] en validación jurídica, sondeo de emisores y casinos, y diseño de un piloto. No comprar kioscos ni desarrollar software productivo hasta cumplir las condiciones 1 a 3.

### Preguntas esenciales (respuestas que cambian conclusiones)

1. **¿Hay casinos o permisionarios concretos interesados?** ¿Cuántas salas y en qué estados? Sin un ancla comercial no hay piloto.
2. **¿Qué es imprescindible al inicio: que la tarjeta funcione fuera del casino (red abierta) o basta con eliminar efectivo dentro del casino (ecosistema cerrado)?** Define si el primer año depende o no de un emisor.
3. **¿El producto debe pagar premios y retiros desde el inicio?** Eso agrega dispensación de efectivo, retenciones fiscales y más exposición de lavado de dinero.
4. **¿Cuánto capital hay disponible y en qué horizonte?** El escenario base pide ≈ $67 M MXN en tres años.
5. **¿El canal en línea se quiere con operadores con permiso específicos?** Cambia los requisitos del emisor (juego no presencial = Tier 1 en Visa).

Mientras no haya respuesta, el estudio supone: (a) arranque en juego presencial, (b) depósitos y pagos en el MVP, sin premios ni retiros, (c) tarjeta de red abierta como objetivo de la fase 2, y (d) financiamiento por etapas.

---

## 1. Definición y evaluación del modelo

### 1.1 Descomposición de la propuesta en actividades

| Actividad | Quién la realiza y con qué figura | Relación contractual con el usuario | Custodia de recursos y responsabilidad | Permisos, contratos y alianzas | Restricciones por el vínculo con casinos |
|---|---|---|---|---|---|
| **Recepción y custodia del efectivo** (kiosco y bóveda) | La empresa opera el kiosco. Si el efectivo se abona a una cuenta financiera, debe recibirlo **a nombre y por cuenta** del banco o IFPE como comisionista [H: LIC art. 46 Bis 1; Ley Fintech art. 54]. | Con el banco o IFPE (la empresa actúa por cuenta de este). | Desde que entra al kiosco, el riesgo físico (robo, faltante) lo asume la empresa frente a la entidad, según el contrato de comisión. La entidad responde ante el usuario y no puede delegar su responsabilidad regulatoria [H: LIC art. 46 Bis 2]. | Contrato de comisión mercantil aprobado o notificado a la CNBV [P]; seguro de valores; contrato de traslado de valores. | La entidad exigirá controles reforzados por estar en un casino. Una IFPE necesita excepción de la CNBV para recibir efectivo [H: Ley Fintech art. 45]. |
| **Identificación del usuario** | La empresa captura los datos y documentos en el kiosco; la entidad (y su proveedor de verificación) valida y decide. | Con la entidad. El casino tiene además su propia obligación de identificar al jugador [H: LFPIORPI art. 17 fr. I; umbral de 325 UMA]. | La entidad responde por su expediente de cliente (KYC). La empresa responde por la integridad de la captura y por el tratamiento de los datos como encargada. | Contrato de encargo de tratamiento de datos; proveedor de verificación de identidad y prueba de vida autorizado por la CNBV para los niveles que lo requieran [H: nivel 2 Bis remoto exige prueba de vida]. | Mayor perfil de riesgo de PLD: debida diligencia reforzada probable [S]. Prohibición de acceso de menores a las áreas de juego [H: RLFJS art. 5 y 29 fr. V Bis]. |
| **Apertura de cuenta** | Solo un banco (cuenta de depósito) o una IFPE (cuenta de fondos de pago electrónico) [H: Ley Fintech art. 22]. | Con la entidad (contrato de adhesión registrado). | La entidad. | Contrato de programa (*program manager*) entre la empresa y la entidad. | La entidad puede negarse por política de riesgo. Requiere confirmación expresa [P]. |
| **Abono de recursos** | La entidad acredita el saldo; la empresa envía la instrucción desde el kiosco. | Con la entidad. | La entidad frente al usuario. La empresa responde ante la entidad por el efectivo pendiente de entregar (**prefondeo** o colateral). | Acuerdo de liquidación; cuenta concentradora; garantía. | Límites por nivel de cuenta: nivel 2 = 3,000 UDIS al mes; nivel 2 Bis = 15,000 UDIS, de los cuales 3,000 en efectivo [H]. |
| **Emisión y activación de la tarjeta** | La entidad es el emisor ante la red (Visa o Mastercard). La empresa puede ser *program manager*, almacenar tarjetas innominadas y entregarlas en el kiosco [S]. | Con la entidad. | La entidad (y el procesador emisor contratado por ella). | Licencia o patrocinio de BIN de la red a través del emisor; procesador emisor; personalización de plásticos certificada. | La red y el emisor pueden restringir el uso en MCC 7995 (juego) y en operaciones *quasi-cash* [P]. |
| **Procesamiento de pagos** | Procesador emisor contratado por la entidad; adquirentes de los comercios. | Ninguna directa con la empresa. | Emisor y adquirente según las reglas de la red. | Contratos de la entidad con el procesador y la red. | Juego no presencial es "Tier 1" de alto riesgo para Visa: el adquirente necesita registro y aprobación por categoría [H: Visa Integrity Risk Program]. |
| **Conciliación** | La empresa (plataforma) concilia kiosco, bóveda, traslado de valores, depósitos bancarios y acreditaciones; la entidad concilia su contabilidad. | No aplica. | Cada parte responde por su propio registro; las diferencias de efectivo son de la empresa según el contrato [S]. | Acuerdos de niveles de servicio (SLA) y de manejo de diferencias. | El casino concilia aparte sus propias fichas, créditos y premios. |
| **Atención al cliente y aclaraciones** | Primer nivel: la empresa (en kiosco, por teléfono o chat). Responsable legal: la entidad, ante la UNE y la CONDUSEF [P]. | Con la entidad. | La entidad. | Protocolo de aclaraciones con plazos regulatorios [P]. | Quejas mezcladas casino/kiosco/tarjeta: hay que definir quién atiende qué. |

### 1.2 Alternativas de estructura

| Criterio | **A. Empresa tecnológica y operadora de kioscos; servicios financieros a cargo de aliados** | **B. Empresa comisionista de un banco o IFPE** (figura específica dentro de A) | **C. Empresa con autorización propia (IFPE o banco)** |
|---|---|---|---|
| Qué hace la empresa | Kiosco, software, operación, soporte, *program management* | Lo anterior **más** la recepción de efectivo y la captura del expediente a nombre de la entidad | Todo, incluida la emisión de cuentas y tarjetas |
| Quién tiene al usuario | Casino (cerrado) o entidad (abierto) | Entidad | La empresa |
| Quién custodia los fondos | Casino o entidad | Entidad (la empresa solo transita el efectivo) | La empresa (con segregación y capital) |
| Licencias propias necesarias | Ninguna financiera [P]; registro como prestador de servicios de la entidad | Ninguna propia; la entidad gestiona el alta del comisionista ante la CNBV [P] | Autorización de la CNBV con acuerdo del Comité Interinstitucional [H: Ley Fintech art. 11]; capital mínimo, PLD propio, auditoría |
| Tiempo estimado | 4 a 8 meses [E] | 6 a 12 meses, según la entidad [E] | 18 a 36 meses o más [E]; solo 89 autorizaciones fintech en 7 años, según la prensa especializada |
| Riesgo principal | Depender de que la entidad acepte casinos | Igual, más la responsabilidad operativa por el efectivo | Costo, plazo y que la CNBV acepte un modelo de efectivo en casinos |
| Ingresos | Renta, operación, comisión por transacción, reparto de ingresos | Además, comisión de la entidad por cada operación | Margen completo (intercambio, comisiones), sin intereses a favor del cliente [H: Ley Fintech art. 29] |
| Veredicto | **Base recomendada** | **Figura recomendada para el componente de efectivo en la fase 2** | **No recomendable para iniciar.** Revaluar con escala (más de 50,000 cuentas activas) [S] |

> **Sobre la figura de "intermediario":** no existe una figura general de "intermediario" que permita recibir y administrar dinero de terceros. Si la empresa recibe efectivo y registra saldos a nombre propio sin autorización, podría configurar captación no autorizada (LIC) o emisión de fondos de pago electrónico sin autorización (Ley Fintech art. 22), conductas con consecuencias penales [P: confirmar artículos de sanción vigentes con el despacho]. **Este punto debe quedar resuelto en el dictamen antes de cualquier piloto.**

### 1.3 Tarjeta de uso general frente a instrumento de ecosistema cerrado

| Aspecto | **Ecosistema cerrado** (saldo de jugador o tarjeta del casino o de una cadena) | **Tarjeta de uso general** (prepagada Visa o Mastercard) |
|---|---|---|
| Dónde se usa | Solo en el casino emisor o en su grupo | En cualquier comercio de la red, y en línea si el emisor lo permite |
| Naturaleza jurídica | Pago anticipado aceptado solo por el emisor o su grupo: **excluido** de fondos de pago electrónico [H: Ley Fintech art. 24]. Si varias empresas no relacionadas lo aceptan, la exclusión podría no aplicar [P]. | Medio de disposición emitido por banco o IFPE [H: Ley Fintech art. 25] |
| Quién lo emite | El casino (la empresa como proveedor tecnológico) | Banco o IFPE |
| Regulación de PLD | Régimen del casino (LFPIORPI fr. I). Monederos y vales emitidos por no financieros pueden ser actividad vulnerable (fr. II; umbral de 645 UMA) [H/P] | Régimen financiero del emisor (art. 115 LIC o art. 58 Ley Fintech) |
| Reglas de red | No aplican | Aplican, incluidas las de juego |
| Tiempo para lanzar | Corto | Largo (depende del emisor) |
| Valor para el usuario | Menor (no sirve fuera del casino) | Alto (inclusión financiera real) |
| Valor para el casino | Alto: menos manejo de efectivo y cajeros, trazabilidad | Alto, pero con riesgo de que el saldo "salga" del casino |

### 1.4 Modelo recomendado para iniciar

**Modelo A en dos fases, con la figura B para la fase 2.**

- **Fase 1 (meses 0 a 12): "Kiosco cashless" de ecosistema cerrado.** La empresa instala y opera kioscos que funcionan como caja de autoservicio del casino: el usuario se identifica (con la credencial del club del casino o una INE validada), deposita efectivo y el importe se acredita como saldo de juego en el sistema del casino; recibe o recarga una tarjeta de jugador. **El casino es dueño de la relación y de los recursos. La empresa cobra renta, operación y transacciones.**
  *Condiciones:* dictamen que confirme que no hay emisión de fondos de pago electrónico ni captación por parte de la empresa [P]; integración certificada con el sistema de gestión del casino [P]; cumplimiento del casino con la LFPIORPI y con el art. 86 del RLFJS [P].
- **Fase 2 (meses 9 a 24): tarjeta prepagada de red abierta**, emitida por un banco (preferente para el efectivo) o una IFPE con excepción de efectivo; la empresa actúa como *program manager* y comisionista para la recepción de efectivo. Se lanza **solo si** un emisor la acepta por escrito, con reglas claras sobre MCC 7995, límites y monitoreo.

Este orden reduce el riesgo: la fase 1 genera ingresos y aprendizaje sin depender del emisor, y la fase 2 se negocia con datos reales de volumen y fraude.

---

## 2. Análisis legal y regulatorio en México

### 2.1 Qué es la "nueva ley de pagos digitales"

| Instrumento | Estado al 9-nov-2026 | Qué dispone | Efecto en el proyecto |
|---|---|---|---|
| **Ley de Economía Digital para Pagos Digitales y Electrónicos** (decreto, DOF 4-nov-2026, edición vespertina) [H] | **Vigente** desde el 5-nov-2026; 22 artículos y 5 transitorios [H] | Art. 1: promover medios de pago digitales. Art. 2 fr. VII: incluye QR, NFC, tarjetas, transferencias y domiciliación. Art. 10: las autoridades deben aceptar pagos digitales. **Art. 11: para los proveedores privados es voluntario.** Art. 12: quien los implemente debe tener infraestructura, informar y atender contingencias. **Art. 13: la SHCP determina los "sectores estratégicos" donde el pago digital puede ser obligatorio o único.** Art. 14: las autoridades sectoriales pueden fijar transiciones graduales desde el efectivo. Art. 15: el efectivo se permite en contingencias. Art. 16: las contingencias no justifican incumplimientos recurrentes. Art. 19: Banxico y CNBV regulan, entre otros temas, la aceptación de QR. Transitorio 3.º: la SHCP tiene 15 días hábiles para determinar los sectores; 4.º: las autoridades, otros 15 días hábiles para emitir disposiciones [H] | **No crea una obligación general de dejar el efectivo**, ni menciona casinos [H]. La presidencia mencionó gasolineras y casetas como primeros sectores [H]. **Si la SHCP incluyera juegos con apuesta, sería un catalizador.** Monitorear el DOF hasta finales de noviembre de 2026 [P]. |
| Reforma a la LFPIORPI (DOF 16-jul-2025) [H] | Vigente | Enfoque basado en riesgo, beneficiario controlador, conservación por 10 años, avisos en 24 horas, extensión a juegos con apuesta realizados al amparo de autorizaciones, regulación expresa de instrumentos de almacenamiento de valor (645 UMA) [H] | Eleva la carga de cumplimiento del casino y de cualquier emisor no financiero de monederos |
| Reglas de carácter general de la LFPIORPI (DOF 7-ago-2026) [H] | Vigencia general: 30-nov-2026, escalonada hasta 2029 [H] | Metodología basada en riesgo y manual (1-mar-2027), herramientas automatizadas (1-jun-2027), auditoría anual (desde 2028) [H] | **Oportunidad comercial:** los casinos necesitarán trazabilidad automatizada, y el kiosco la genera |
| Reforma al Reglamento de la LFJS (DOF 16-nov-2023) [H] | Vigente, impugnada; varios jueces la han declarado inconstitucional en amparos individuales; la SCJN no fijó un criterio general (mar-2025) [H] | Prohíbe nuevas máquinas tragamonedas; permisos de 1 a 15 años; desaparece la figura del operador [H] | Incertidumbre sobre el crecimiento de salas; los permisos existentes vencen sin prórroga |
| Reforma a la Ley Federal de Juegos y Sorteos | **Iniciativa**, no publicada. SEGOB dijo en sep-2026 que su revisión interna estaba "prácticamente concluida"; hay además una iniciativa legislativa de oct-2025 (instituto nacional, edad de 21 años, autoexclusión, límites de apuesta) [H] | Contenido final desconocido | Riesgo y oportunidad: podría exigir trazabilidad, límites o medios de pago específicos [P] |
| IEPS a juegos con apuesta | Vigente para 2026 | Tasa elevada de 30% a 50% según la reforma aprobada en el paquete económico 2026 [H: prensa especializada; P: verificar texto de la LIEPS 2026 y su base] | Presión sobre los márgenes del casino: menos espacio para pagar servicios, pero más interés en reducir costos de caja |

### 2.2 Matriz regulatoria

| Actividad | Normativa aplicable | Autoridad | Obligación | Responsable | Evidencia requerida | Pendiente de validación |
|---|---|---|---|---|---|---|
| Recepción de efectivo de usuarios para abonarlo a cuentas | LIC art. 46 Bis 1 y 46 Bis 2; Disposiciones de carácter general aplicables a las instituciones de crédito (capítulo de comisionistas) | CNBV | Solo a nombre de la entidad, bajo contrato de comisión; la entidad conserva la responsabilidad | Banco (responsable); empresa (comisionista) | Contrato de comisión; alta o aviso ante la CNBV; manuales operativos | Límites por operación y por comisionista para depósitos en efectivo; si una sala de juego puede ser establecimiento de comisionista [P] |
| Ídem con una IFPE | Ley Fintech art. 45 (solo recursos desde cuentas bancarias, salvo excepción de la CNBV) y art. 54 (contratación de terceros) | CNBV, Banxico | Requiere excepción expresa para efectivo | IFPE | Autorización de la excepción | Si la IFPE candidata ya tiene la excepción [P] |
| Naturaleza de la "cuenta digital" | LIC (depósito bancario), Ley Fintech arts. 22-29 (fondos de pago electrónico), Ley Fintech art. 24 (exclusión de prepago cerrado) | CNBV, Banxico | Elegir la figura. Fondos de pago electrónico: sin intereses (art. 29), saldos inactivos a cuenta global a los 3 años (art. 28) [H] | Entidad | Contrato de adhesión; registro de comisiones | Si un saldo multi-casino sigue siendo "cerrado" [P] |
| Niveles de cuenta y límites | Disposiciones del art. 115 LIC (art. 14 Bis, nivel 2 Bis; DOF 10-jul-2026) y Circular 11/2026 de Banxico [H] | CNBV, Banxico, SHCP | Nivel 2: 3,000 UDIS/mes; 2 Bis: 15,000 UDIS (3,000 en efectivo); nivel 4: 30,000 UDIS [H: fuente secundaria] | Entidad | Expediente según el nivel; prueba de vida si es remoto | Límites de nivel 1 y 3, y equivalentes para IFPE [P] |
| Emisión, distribución y activación de tarjetas | Ley Fintech art. 25; reglas de Banxico sobre medios de disposición; reglas de Visa y Mastercard | Banxico, CNBV, redes | Emisor autorizado; custodia segura de plásticos; activación con autenticación | Emisor; la empresa como distribuidor | Contrato de programa; procedimientos de custodia de inventario | Si la entrega de plástico en kiosco es aceptable para el emisor y la red [P] |
| Juego presencial | LFJS (1947) y su Reglamento (última reforma DOF 16-nov-2023) [H] | SEGOB | Solo permisionarios operan (reforma 2023). Operaciones en moneda nacional (RLFJS art. 10). **Prohibido dar crédito a apostadores (art. 11)**. Las apuestas se aceptan en efectivo salvo las hechas por Internet, teléfono o medios electrónicos (art. 86) [H] | Casino | Permiso vigente; reglas aprobadas | **Si cargar saldo de juego desde una tarjeta prepagada o desde un kiosco es compatible con el art. 86** y si requiere aprobación de SEGOB [P, crítico] |
| Juego en línea | RLFJS arts. 85 y 87 (registro de cuenta e identidad del apostador; comprobantes electrónicos) [H] | SEGOB | Solo con operadores con permiso que incluya modalidad en línea | Operador | Permiso con modalidad remota | Si los operadores aceptan depósitos desde tarjetas de terceros [P] |
| Prevención de lavado de dinero en el casino | LFPIORPI art. 17 fr. I; Reglas de carácter general (DOF 7-ago-2026) [H] | SHCP (UIF), SAT | Identificar desde 325 UMA ($38,125.75); aviso desde 645 UMA ($75,664.95); acumulación en 24 horas y en 6 meses; conservación por 10 años [H] | Casino | Expediente, avisos, manual, auditoría | Si los datos del kiosco pueden servir como expediente del casino [P] |
| Restricción de efectivo | LFPIORPI art. 32 | SHCP | Prohibición de pagar boletos o premios en efectivo a partir de 3,210 UMA ($376,565) según fuente secundaria [P: confirmar fracción y monto] | Casino | Política de pagos | Relevante si se incluyen premios en efectivo |
| PLD del emisor | LIC art. 115 y sus disposiciones; Ley Fintech art. 58 | CNBV, UIF | Programa de PLD propio; perfil transaccional; reportes | Entidad | Manual; sistema de monitoreo | Requisitos de debida diligencia reforzada para el canal casino [P] |
| PLD de la empresa | LFPIORPI art. 17 fr. II (tarjetas prepagadas y monederos **no** emitidos por entidades financieras) [H] | SAT | Si la empresa **comercializa** instrumentos propios, podría ser actividad vulnerable | Empresa | Alta en el portal del SAT | Si como comisionista de una entidad financiera queda fuera de la fr. II [P] |
| Datos personales y biométricos | LFPDPPP nueva (DOF 20-mar-2025; vigente desde el 21-mar-2025) [H] | Secretaría Anticorrupción y Buen Gobierno [H] | Aviso de privacidad; consentimiento expreso para datos sensibles; derechos ARCO; medidas de seguridad | Entidad (responsable) y empresa (encargada); casino (responsable de sus datos) | Avisos, contratos de encargo, análisis de riesgo | Si la biometría facial se trata como dato sensible y la forma de consentimiento en kiosco [P] |
| Protección al usuario de servicios financieros | Ley de Protección y Defensa al Usuario de Servicios Financieros; Ley para la Transparencia y Ordenamiento de los Servicios Financieros | CONDUSEF, Banxico | Contratos registrados, comisiones registradas, UNE, plazos de aclaración | Entidad | Registro de contratos y comisiones | Plazos de aclaración aplicables a fondos de pago electrónico [P] |
| Protección al consumidor (servicios no financieros) | Ley Federal de Protección al Consumidor | PROFECO | Información clara de cargos del casino o de la empresa | Casino o empresa | Términos y condiciones | — |
| Fiscal: premios | Ley del ISR (retención sobre premios) y leyes estatales | SAT, estados | Retención del ISR y del impuesto estatal por premios | Casino | CFDI de retenciones | Si se incluyen premios en la tarjeta [P] |
| Fiscal: IEPS y sistemas | Ley del IEPS (tasa 2026; obligación de sistemas de registro en línea para casinos) | SAT | Tasa y registro en línea de las operaciones de juego | Casino | Sistema central conectado al SAT | Si el kiosco debe integrarse con el sistema reportado al SAT [P] |
| Fiscal: ingresos de la empresa | CFF, LIVA, LISR | SAT | CFDI por renta, servicios y comisiones; **el efectivo de usuarios no es ingreso de la empresa** | Empresa | Contabilidad separada de cuentas de orden | Tratamiento del efectivo en tránsito [P] |
| Reglas de redes para juego | Visa Integrity Risk Program; Mastercard (programa equivalente) | Visa, Mastercard | El juego no presencial (MCC 7995) es categoría "Tier 1": registro previo del comercio, debida diligencia reforzada, aprobación específica y certificación anual del adquirente [H: Visa] | Adquirentes; emisor (decisión de bloquear o permitir) | Registro del comercio | Reglas de Mastercard; política del emisor sobre recargas en ubicaciones de juego y sobre MCC 7995 [P] |
| Seguridad privada y traslado de valores | Ley Federal de Seguridad Privada | SSPC | El traslado y custodia de valores solo con empresas autorizadas | Proveedor de traslado de valores | Permiso vigente del proveedor | — |

### 2.3 Distribución de obligaciones

| Parte | Obligaciones principales |
|---|---|
| **Empresa** | Operación segura del kiosco; custodia del efectivo hasta entregarlo; integridad de la captura de datos (encargada de tratamiento); conciliación; primer nivel de atención; cumplimiento contractual de los estándares de la entidad y de la red (PCI, seguridad); posible régimen de la LFPIORPI fr. II si comercializa instrumentos propios [P]. |
| **Casino (permisionario)** | Permiso vigente; identificación de jugadores y avisos (LFPIORPI fr. I); prohibición de crédito; pago y retención de premios; IEPS; control de acceso (menores, art. 5 RLFJS); aprobar la mecánica ante SEGOB si se requiere [P]. |
| **Banco o IFPE** | Autorización; contrato con el usuario; KYC y PLD; custodia de los fondos; emisión de la tarjeta; aclaraciones ante la CONDUSEF; supervisión del comisionista. |
| **Procesador y red** | Autorización y liquidación; reglas de la red; seguridad del procesamiento. |
| **Proveedor de traslado de valores** | Recolección, custodia, conteo y depósito; seguro. |

### 2.4 Asuntos que requieren dictamen jurídico antes de invertir o lanzar

1. **Figura de la empresa** (proveedor tecnológico, comisionista u otra) y frontera con la captación y la emisión de fondos de pago electrónico sin autorización.
2. **Compatibilidad del modelo con el art. 86 del RLFJS** (apuestas en efectivo) y si cargar créditos de juego desde una tarjeta requiere aprobación de SEGOB.
3. **Naturaleza del saldo en la fase 1** (pago anticipado cerrado, art. 24 de la Ley Fintech) y los límites de la exclusión si participan varios casinos o una cadena.
4. **Aplicación de la LFPIORPI fr. II** a la empresa en cada fase.
5. **Viabilidad de que una sala de juego sea establecimiento de comisionista** y los límites de depósito en efectivo aplicables.
6. **Biometría y prueba de vida en kiosco**: consentimiento, conservación y transferencia a la entidad.
7. **Tratamiento fiscal** del efectivo en tránsito y de las comisiones; retenciones si se incluyen premios.
8. **Efecto de la determinación de sectores de la SHCP** (art. 13 de la Ley de Economía Digital) si incluye juegos con apuesta.

---

## 3. Mercado y benchmark

### 3.1 Contexto del mercado mexicano

| Dato | Valor | Tipo |
|---|---|---|
| Adultos con al menos un producto financiero (ENIF 2024) | 76.5% (mujeres 72.8%, hombres 80.9%) | [H] |
| Adultos con cuenta formal de ahorro (ENIF 2024) | 63.0% | [H] |
| Tendencia de pagos (ENIF 2024 frente a 2021) | Baja el efectivo como medio más frecuente; la mayor alza es de transferencias o apps en compras mayores a $500 (+4.8 pp) | [H] |
| Salas de juego operando (AIEJA, 2024) | 425 salas; ≈ 756 permisos emitidos para futuras aperturas | [H] (fuente gremial) |
| Máquinas legales en operación | ≈ 100,000 | [H] (fuente gremial) |
| Gasto diario promedio por jugador | De $320 a ≈ $480 en siete años | [H] (fuente gremial) |
| Empleo | > 50,000 directos, 120,000 indirectos | [H] (fuente gremial) |
| Mercado en línea (Statista citado por prensa) | US$2.7 mil millones en 2024, crecimiento ≈ 7.5% anual | [H] (secundaria) |
| Escrutinio regulatorio | 13 casinos con cuentas bloqueadas por la SHCP; 10 sancionados por OFAC/FinCEN | [H] |
| Traslado de valores | Reportes de retrasos en la devolución de efectivo resguardado por Servicio Panamericano/Brinks México (ago-2026) | [H] (alegaciones no verificadas) |

> Las cifras de la AIEJA son gremiales y no auditadas. No se encontró una estadística oficial de SEGOB con el número de salas en operación. [P]

### 3.2 Segmentos

| Segmento | Quién es | Qué necesita | ¿Paga? |
|---|---|---|---|
| **Usuario bancarizado** | Jugador con cuenta o tarjeta | Rapidez, no cargar efectivo, controlar su gasto | Poco dispuesto a pagar comisiones; ya tiene alternativas |
| **Usuario no bancarizado o de efectivo** | Jugador que usa efectivo por preferencia, privacidad o falta de cuenta | Convertir efectivo en un medio seguro y reutilizable | Puede pagar el costo del plástico; sensible al precio |
| **Casino (cliente comercial)** | Permisionario o grupo | Menos costo de caja y de manejo de efectivo; menos faltantes y robos; trazabilidad para PLD; datos del jugador | **Pagador principal** (renta y operación) |
| **Emisor o banco aliado** | Banco o IFPE | Captación de clientes y de saldo, intercambio | Comparte ingresos o paga por adquisición de cuenta [S] |
| **Operador en línea** | Plataforma con permiso | Depósitos rápidos y trazables desde efectivo | Comisión por depósito [S] |

### 3.3 Competidores y sustitutos

| Solución | Propuesta de valor | Cobertura | Experiencia | Modelo de ingresos | Costos públicos | Relación con casinos | Limitaciones | Presencia |
|---|---|---|---|---|---|---|---|---|
| **Caja tradicional del casino** | Conocida; sin inversión adicional | Todas las salas | Filas; atención humana | Costo interno del casino | No aplica | Propia | Costo de personal; riesgo de faltantes; trazabilidad manual | México, comprobada |
| **Sistemas de tickets (TITO) y tarjeta de jugador o club** | Ecosistema cerrado en la sala, con lealtad | Salas con sistemas de gestión | Buena dentro de la sala | Licencias del proveedor del sistema | No públicos [P] | Integrada | No sirve fuera del casino; no es inclusión financiera | México, comprobada en salas grandes [S: no se verificó por operador] |
| **Kioscos de redención y cambio de los proveedores de máquinas** | Canje de tickets y billetes | Salas grandes | Autoservicio | Venta o renta de hardware | No públicos [P] | Integrada | No abre cuentas ni emite tarjetas de red | Referencia internacional; presencia en México no verificada [P] |
| **Depósito en efectivo en tiendas (OXXO Pay, Paynet y similares) hacia operadores en línea** | Depósito en efectivo en miles de tiendas | Nacional | Referencia o código; acreditación casi inmediata | Comisión al comercio o al usuario | No verificados en este estudio [P] | Operadores en línea | No sirve en juego presencial; límites por operación | México, comprobada (listados de operadores) |
| **Cuentas digitales con depósito en tiendas** (bancos digitales, IFPE, monederos) | Cuenta y tarjeta de red abierta | Nacional | App | Intercambio y comisiones | Variables [P] | Ninguna específica | Exigen celular y app; el depósito se hace fuera del casino | México, comprobada |
| **Prepago en línea (por ejemplo, ToditoCash, Paysafecard)** | Saldo prepago para pagos en línea | Nacional (en línea) | Código o PIN | Comisión | No verificados [P] | Operadores en línea | Solo en línea | México, comprobada en listados de operadores |
| **Transferencias SPEI y CoDi/DiMo** | Inmediatas, bajo costo | Nacional | App bancaria | Gratis o bajo costo | Banxico: SPEI 24/7 | Operadores en línea; algunas salas | Requieren cuenta bancaria | México, comprobada |
| **Cuentas "cashless" de casino con tarjeta prepagada (por ejemplo, Play+ de Sightline)** | Cuenta prepagada para jugar en casino y en línea, con tarjeta de red | EE. UU. | Integrada al casino | Comisiones y reparto con el casino | No aplica | Muy integrada | Diseñada para el marco de EE. UU. | **Referencia internacional**; no hay presencia comprobada en México |

**Conclusión del benchmark [S]:** no se encontró en México una oferta que combine, **dentro de la sala**, efectivo → cuenta regulada → tarjeta de red abierta. Ese hueco puede ser una oportunidad o una señal de que los emisores no han querido entrar en el giro. La validación con emisores (sección 6) es la prueba para distinguir entre ambas.

### 3.4 Estimación del mercado potencial

Se evita igualar el mercado total de pagos digitales con el mercado accesible. Supuestos explícitos [E]:

| Nivel | Supuesto | Resultado |
|---|---|---|
| **Universo (TAM operativo)** | 425 salas [H]; ingreso de servicio potencial de 1 a 3 kioscos por sala | 425 a 1,275 kioscos |
| **Mercado accesible (SAM)** | Salas de grupos medianos y grandes con sistema de gestión integrable y permiso con horizonte de 5 años o más: 50% [S] | ≈ 210 salas; ≈ 320 kioscos (1.5 por sala) |
| **Mercado alcanzable en 3 años (SOM)** | 25% a 40% del SAM en el escenario base [S] | 80 a 130 kioscos; el escenario base usa 120 |
| **Ingreso de servicio anual al SOM** | ≈ $41,700 MXN/kiosco/mes (base, sección 7) | ≈ $60 millones MXN/año con 120 kioscos [E] |
| **Volumen de efectivo convertido** | 80 cargas/día × $600 por kiosco [S] | ≈ $1.44 millones MXN/kiosco/mes; ≈ $2,070 millones MXN/año con 120 kioscos [E] — **dinero de usuarios, no ingreso** |

Adicional (no incluido en la base): canal en línea y comercios fuera de casino si la SHCP declarara sectores obligatorios afines.

---

## 4. Diseño operativo y experiencia del usuario

### 4.1 Recorrido del usuario (fase 2, tarjeta de red abierta)

1. **Llegada al kiosco** (dentro de la sala, en zona vigilada y fuera del área de máquinas).
2. **Registro:** escaneo de la INE (frente y reverso), validación contra la base del emisor o su proveedor, selfie con prueba de vida, captura del celular con código de verificación, aceptación del aviso de privacidad y del contrato de la entidad con firma electrónica.
3. **Decisión de KYC del emisor** (segundos o minutos). Si se rechaza o queda en revisión, se informa en pantalla y no se recibe efectivo.
4. **Depósito:** el usuario introduce billetes; el validador los autentica; la pantalla muestra el acumulado; el usuario confirma.
5. **Abono:** el kiosco envía la instrucción; la entidad acredita el saldo y el kiosco imprime el comprobante.
6. **Tarjeta:** el kiosco dispensa una tarjeta innominada del inventario, la asocia a la cuenta y la activa (PIN elegido en el kiosco o en la app).
7. **Uso:** en el casino (según se permita) y en cualquier comercio de la red; consulta de saldo en la app o por SMS.
8. **Recargas posteriores:** en cualquier kiosco con la tarjeta, sin repetir el registro, dentro de los límites del nivel.

### 4.2 Movimiento del dinero y de la información

```
DINERO (fase 2, red abierta)

 Usuario --efectivo--> [KIOSCO / bóveda]  --recolección (T+1 a T+3)-->  [Traslado de valores]
                         | control: empresa           control: proveedor de traslado
                         |                                   |
                         |                                   v  depósito
                         |                          [Cuenta concentradora del EMISOR]
                         |                                   ^
   instrucción de abono  v                                   | concilia
 [Plataforma de la empresa] --API--> [EMISOR: cuenta del usuario]  <-- prefondeo/colateral de la empresa
                                         | saldo DISPONIBLE al instante (T+0)
                                         v
                               [Procesador / red] --> comercios (casino, tiendas, en línea)
```

| Etapa | Quién controla los fondos | ¿Disponible para el usuario? |
|---|---|---|
| Billete aceptado en el kiosco | Empresa (como comisionista, por cuenta del emisor) | No hasta la confirmación |
| Abono confirmado | Emisor (con la garantía de la empresa por el efectivo aún físico) | **Sí, de inmediato** |
| Efectivo en traslado | Proveedor de traslado (riesgo contractual de la empresa) | Sí (ya acreditado) |
| Depósito en la cuenta concentradora | Emisor | Sí; se libera el colateral |

> **Implicación clave [E]:** como el saldo está disponible antes de que el efectivo llegue al banco, alguien debe **prefondear**. En el escenario base equivale a 3 días de volumen, unos $144,000 MXN por kiosco. Es capital de trabajo o colateral, **no gasto ni ingreso**.

```
INFORMACIÓN

 Kiosco --(eventos firmados: billetes, sesión, KYC, dispensado)--> Plataforma de la empresa
 Plataforma --(alta, KYC, abono, activación)--> Emisor / procesador
 Emisor --(confirmaciones, saldo, alertas PLD)--> Plataforma --> app y SMS del usuario
 Plataforma --(reportes de efectivo y conciliación)--> Traslado de valores, emisor y casino
 Casino <--(datos de identidad del jugador, solo con base legal y consentimiento)-- Plataforma [P]
```

**Fase 1 (ecosistema cerrado):** el efectivo queda en la bóveda del kiosco, que es propiedad o custodia del casino; la recolección la hace el personal de bóveda del casino con doble control, y el saldo se acredita en el sistema de gestión del casino. No hay prefondeo de la empresa ni traslado propio de valores.

### 4.3 Excepciones

| Excepción | Tratamiento propuesto |
|---|---|
| Billete rechazado o sospechoso | Se devuelve. Si el validador lo retiene por sospecha de falsificación, se registra el evento con video y se sigue el protocolo de Banxico para piezas presuntamente falsas [P]. |
| Falla de conexión antes del abono | El kiosco no acepta efectivo sin conexión (modo seguro). Si la conexión se cae a mitad de la sesión: comprobante de "abono pendiente" con folio; reintento automático; acreditación garantizada en un plazo máximo definido. |
| Abono pendiente | Cola de reintentos idempotente; alerta a soporte a los 5 minutos; conciliación diaria contra el contador de la bóveda. |
| Tarjeta no entregada (atasco o inventario vacío) | La cuenta y el saldo existen; se ofrece tarjeta virtual en la app o recoger la física en otro kiosco o en el mostrador del casino; alerta de reabastecimiento. |
| Identidad no validada | No se recibe efectivo. Opción de completar en mostrador asistido o en la app. Nunca se acepta efectivo "a cuenta" sin identidad. |
| Devolución (el usuario se arrepiente) | Antes de confirmar el depósito: devolución del efectivo en el kiosco si tiene reciclador; si no, la confirmación es irreversible y se informa antes. Después: transferencia o retiro según las reglas del emisor. |
| Fraude (identidad suplantada, estructuración de depósitos, tarjeta robada) | Prueba de vida; límites por sesión, día y mes; reglas de fraccionamiento coordinadas con el emisor y el casino; bloqueo inmediato desde la app o la línea. |
| Reclamaciones | Primer nivel de la empresa (folio, video, bitácora del kiosco); escalamiento a la UNE del emisor; plazos regulatorios [P]. |

### 4.4 Depósitos, pagos, premios y retiros

| Función | ¿En el MVP? | Motivo |
|---|---|---|
| **Depósitos en efectivo** | **Sí** | Es el núcleo de la propuesta. |
| **Pagos** (en el casino y en comercios) | **Sí, con condiciones** | En la fase 1, solo dentro del casino. En la fase 2, en comercios; el uso en el propio casino con tarjeta depende del art. 86 del RLFJS, del adquirente del casino y de la política del emisor [P]. |
| **Premios** (pago de premios a la tarjeta) | **No; evaluación adicional** | Implica retenciones fiscales del casino, umbrales de la LFPIORPI, restricción de efectivo del art. 32 y posibles reglas de red para pagos de juego [P]. Es atractivo para el casino, pero se deja para después del piloto. |
| **Retiros en efectivo en el kiosco** | **No** | Exige reciclador, más efectivo en sitio y más riesgo de lavado (ciclo efectivo-tarjeta-efectivo). Los retiros se harían en cajeros de la red, sujetos al emisor. |

---

## 5. Requerimientos tecnológicos y del kiosco

### 5.1 Arquitectura inicial

```
                   ┌───────────────────────── NUBE (región MX o US con acuerdo) ─────────────────────────┐
 [KIOSCO]          │  API Gateway (mTLS)                                                                 │
  - App de kiosco  │   ├─ Servicio de dispositivos (alta, telemetría, comandos remotos, actualizaciones) │
    (modo kiosco)  │   ├─ Orquestador de sesiones (estado de la transacción, idempotencia)              │
  - Validador de   │   ├─ Servicio de identidad (integración con el proveedor de KYC y prueba de vida)  │
    billetes       │   ├─ Conector del emisor (alta, abono, tarjeta, saldo)    --->  EMISOR / PROCESADOR │
  - Bóveda         │   ├─ Conector del casino (sistema de gestión, fase 1)     --->  SISTEMA DEL CASINO  │
  - Dispensador    │   ├─ Motor de reglas de riesgo y límites                                           │
    de tarjetas    │   ├─ Libro de efectivo y conciliación (kiosco, traslado, banco, emisor)            │
  - Lector de INE  │   ├─ Consola de administración y soporte (roles, auditoría)                        │
  - Cámara         │   └─ Monitoreo, alertas, SIEM, bitácora inmutable                                  │
  - Impresora      └─────────────────────────────────────────────────────────────────────────────────────┘
  - 4G/LTE + cableado
```

| Componente | Requisitos clave |
|---|---|
| **Software del kiosco** | Sistema operativo endurecido en modo kiosco; arranque seguro; cifrado de disco; actualización remota firmada; operación transaccional con estado persistente para reanudar tras una falla. |
| **Registro e identificación** | Lectura de INE (OCR y código de barras), prueba de vida pasiva o activa, validación contra fuentes autorizadas a través del proveedor. Sin almacenamiento local de imágenes después del envío. |
| **Integraciones** | API del emisor y procesador (alta, KYC, abono, emisión, activación, saldo); sistema de gestión del casino (fase 1); traslado de valores (reportes); notificaciones (SMS o WhatsApp). |
| **Administración** | Consola web con roles (operación, soporte, cumplimiento, casino); bitácora de cada acción. |
| **Conciliación** | Libro de efectivo por kiosco: contador del validador + apertura de bóveda + conteo del traslado + depósito bancario + abonos acreditados. Diferencias con alerta automática. |
| **Soporte** | Mesa de ayuda 24/7 (horario de casino), herramientas de diagnóstico remoto. |

### 5.2 Hardware y manejo de efectivo

| Elemento | Especificación propuesta | Comentario |
|---|---|---|
| Aceptación y validación de billetes | Validador con detección multisensor y actualización de patrones para billetes de Banxico; casete con cerradura y bitácora | Se recomienda solo validador y casete en el MVP; reciclador solo si después se incluyen devoluciones o retiros |
| Resguardo | Caja fuerte con rating comercial, cerradura electrónica con código de un solo uso, sensores de apertura e inclinación, anclaje al piso | La capacidad del casete define la frecuencia de recolección |
| Dispensador de tarjetas | Dispensador con tolva de 200 a 500 tarjetas, lectura de chip para asociar y activar, bandeja de rechazo | Inventario de tarjetas innominadas bajo control dual |
| Identificación | Escáner de documentos, cámara frontal para selfie con iluminación controlada | Debe soportar la prueba de vida exigida por el emisor |
| Cámaras de seguridad | Cámara de evidencia del usuario y de la zona de bóveda | Conservación según el aviso de privacidad |
| Conectividad | Cableada (red del casino segmentada) + 4G/LTE de respaldo | Sin conexión no se acepta efectivo |
| Energía | UPS para cerrar la transacción en curso | — |

### 5.3 Disponibilidad, recuperación y monitoreo

- Objetivo de disponibilidad por kiosco: 98% en el horario del casino; plataforma central: 99.9% [E].
- Transacciones idempotentes con folio único; reconciliación automática al reconectar.
- Telemetría cada minuto: nivel del casete, inventario de tarjetas, errores del validador, puerta, temperatura, red.
- Alertas: casete al 80%, menos de 50 tarjetas, puerta abierta sin orden de servicio, discrepancias.

### 5.4 Seguridad física y digital

- **Física:** ubicación dentro del perímetro del casino con CCTV; anclaje; doble control para abrir la bóveda; órdenes de servicio con código de un solo uso; protocolos de robo.
- **Digital:** mTLS entre kiosco y nube; llaves en módulo de hardware (TPM en el kiosco y HSM o KMS en la nube); cifrado de datos personales en reposo; segregación de la red del casino; mínimo privilegio; MFA en la consola; bitácora inmutable; pruebas de penetración antes del piloto.
- **Prevención de fraude:** límites por sesión, día y mes por usuario y por kiosco; detección de fraccionamiento; listas negras compartidas con el emisor; revisión de video en las alertas.

### 5.5 Operación de campo

| Proceso | Fase 1 | Fase 2 |
|---|---|---|
| Recolección de efectivo | Personal de bóveda del casino, doble control | Proveedor de traslado de valores autorizado, o liquidación con el casino [P] |
| Mantenimiento | Preventivo mensual; correctivo con SLA de 4 a 8 horas en ciudad | Igual |
| Reposición de tarjetas | No aplica (o tarjeta de jugador del casino) | Inventario controlado, entrega con el traslado o con técnico |
| Incidentes | Mesa de ayuda y técnico | Igual, más protocolo con el emisor |

> **Opción a validar: "liquidación con el casino" [S/P].** Como el casino necesita efectivo para su operación, podría vaciar la bóveda del kiosco y transferir el mismo día por SPEI el importe a la cuenta concentradora del emisor. Esto elimina el traslado propio de valores y gran parte del prefondeo. **Exige dictamen**, porque el casino estaría recibiendo efectivo destinado a una cuenta financiera (posible figura de comisionista del casino).

### 5.6 Comprar, adaptar o desarrollar

| Opción | Hardware | Software | Costo estimado [E] | Plazo [E] | Ventajas | Riesgos |
|---|---|---|---|---|---|---|
| **Comprar** (kiosco y software llave en mano) | Kiosco de fabricante con validador y dispensador | Software del fabricante o de un integrador | $250,000 a $400,000 MXN por kiosco + licencias | 3 a 5 meses | Rapidez; certificaciones ya hechas | Dependencia; integración con el emisor posiblemente inexistente |
| **Adaptar** (recomendado) | Gabinete de fabricante mexicano con componentes estándar (validador, dispensador, escáner) | Plataforma propia en la nube + aplicación de kiosco propia sobre SDK de los periféricos | $180,000 a $300,000 MXN por kiosco; desarrollo de $6 a $10 millones MXN | 6 a 9 meses | Control de la experiencia, de los datos y de las integraciones | Requiere equipo técnico; certificaciones a cargo de la empresa |
| **Desarrollar** el hardware | Diseño propio del gabinete | Propio | Mayor costo inicial; solo conviene a gran escala | 12 a 18 meses | Costo unitario menor a escala | Plazo; certificación eléctrica y de seguridad |

> Ninguna cifra está cotizada. Se deben pedir al menos tres cotizaciones (sección 6).

### 5.7 Certificaciones y estándares aplicables

| Estándar | Alcance | ¿Quién debe cumplirlo? | ¿Aplica? |
|---|---|---|---|
| **PCI DSS v4.x** | Protección de datos de tarjeta (PAN) en sistemas que los almacenan, procesan o transmiten | Empresa si su plataforma toca el PAN (por ejemplo, al asociar la tarjeta en el kiosco); emisor y procesador siempre | Sí. Diseñar para minimizar el alcance (tokenización con el procesador) |
| **PCI PIN / PCI PTS** | Captura de PIN en dispositivos | Fabricante del PIN pad y empresa si el kiosco captura PIN | Solo si se elige PIN en el kiosco; alternativa: PIN en la app |
| **EMV (EMVCo) y certificación de la red** | Tarjetas con chip, personalización y emisión | Emisor, procesador y personalizador | Sí (de la entidad y sus proveedores) |
| **Seguridad física de tarjetas (Visa/Mastercard)** | Custodia de plásticos e inventario | Personalizador y, por contrato, la empresa como depositaria | Sí, según el contrato con el emisor [P] |
| **GLI-16 (sistemas cashless) y GLI-20 (kioscos)** | Estándares de laboratorio para sistemas de juego sin efectivo y kioscos en casinos | Fabricante o integrador del kiosco que interactúa con el sistema de juego | **Probable** si el casino o SEGOB exigen certificación de equipos conectados al sistema de juego [P] |
| **ISO/IEC 27001** | Sistema de gestión de seguridad de la información | Empresa (voluntario) | Recomendable; suele pedirlo el emisor |
| **SOC 2 tipo II** | Controles de proveedor de servicios | Empresa (voluntario) | Útil frente a emisores internacionales |
| **Requisitos del emisor de prueba de vida y verificación** | KYC no presencial | Proveedor de identidad, a través de la entidad | Sí [H: exigido para cuentas remotas de nivel 2 Bis, 3 y 4] |
| **NOM eléctricas y de seguridad de producto** | Equipo eléctrico comercializado en México | Fabricante o importador | Sí [P: identificar NOM específicas] |
| **Ley Federal de Seguridad Privada** | Traslado y custodia de valores | Proveedor de traslado | Sí, para el proveedor |

---

## 6. Aliados y proveedores

### 6.1 Candidatos por categoría

> **Advertencia:** ningún proveedor fue contactado. La lista se basa en presencia pública en México. **Que un proveedor ofrezca servicios financieros generales no significa que acepte el giro de casinos.** Todo lo marcado **CE** requiere **confirmación comercial expresa** por escrito.

| Categoría | Candidatos (ejemplos públicos) | Operación en México | Confirmación expresa (CE) requerida |
|---|---|---|---|
| **Emisión de cuentas y tarjetas (banco)** | Bancos con programas de prepago, corresponsales o banca como servicio (BaaS); bancos medianos con apetito por nichos | Sí | CE: aceptación del giro de casinos, comisionista en sala, límites, MCC 7995, prefondeo |
| **Emisión (IFPE)** | IFPE autorizadas con emisión de tarjetas (consultar el padrón vigente de la CNBV) | Sí (autorizadas) | CE: excepción de efectivo (Ley Fintech art. 45), apetito por casinos |
| **Procesador emisor / plataforma de tarjetas** | Procesadores con operación local (por ejemplo, Pomelo, con country manager en México y programas de tarjetas con fintechs mexicanas [H]); procesadores tradicionales del mercado | Sí | CE: procesar un programa con origen en casinos; certificación con el emisor elegido |
| **Identificación y prueba de vida** | Proveedores regionales con validación de INE y prueba de vida (por ejemplo, Truora, Incode, MetaMap, Jumio) | Sí (verificar por proveedor) | CE: integración en kiosco (no solo móvil); certificación aceptada por el emisor |
| **Prevención de fraude y PLD** | Proveedores de monitoreo transaccional y listas (incluidos proveedores locales de software de PLD) | Sí | CE: reglas para juego y efectivo |
| **Fabricación de kioscos** | Fabricantes e integradores mexicanos de kioscos de pago (hay catálogos públicos de kioscos de pago y dispensadores de tarjetas) | Sí | CE: integrar validador, dispensador, lector de INE y cámara; soporte nacional |
| **Aceptación de efectivo (componentes)** | Fabricantes internacionales de validadores y recicladores con distribuidores en México | Sí (distribuidores) | CE: actualización de patrones de billetes de Banxico; servicio local |
| **Traslado de valores** | Empresas autorizadas con cobertura nacional (por ejemplo, Cometra, Servicio Panamericano, otros regionales) | Sí | CE: servicio a kioscos dentro de casinos; costo por visita; seguro. Debida diligencia por los reportes de 2026 sobre retrasos [H] |
| **Mantenimiento de campo** | Integradores con red técnica nacional | Sí | CE: SLA en ciudades de los casinos piloto |
| **Sistemas de gestión de casino** | Proveedores de sistemas de las salas piloto | Sí | CE: API o protocolo para acreditar saldo desde el kiosco (fase 1) |

### 6.2 Lista priorizada

| Prioridad | Categoría | Por qué es crítica | Meta en 90 días |
|---|---|---|---|
| 1 | **Emisor (banco y una IFPE como alternativa)** | Sin él no existe la fase 2 | Sondear 6 a 8 entidades; conseguir 2 cartas de intención |
| 2 | **Casinos ancla** | Definen el piloto y la fase 1 | 2 cartas de intención |
| 3 | **Proveedor del sistema de gestión del casino** | Condición técnica de la fase 1 | Confirmar API y costo de integración |
| 4 | **Procesador emisor** | Condiciona la integración y el costo por cuenta | 2 propuestas comparables |
| 5 | **Identidad y prueba de vida** | Experiencia en kiosco y aceptación del emisor | 2 pruebas de concepto |
| 6 | **Fabricante del kiosco** | Costo unitario y plazo | 3 cotizaciones |
| 7 | **Traslado de valores** | Costo variable principal en la fase 2 | 2 cotizaciones por ciudad |
| 8 | **Mantenimiento** | SLA | 2 propuestas |

### 6.3 Cuestionario para solicitar propuestas comparables

**A. Para todos**
1. Razón social, autorizaciones vigentes (número y autoridad) y antigüedad operando en México.
2. Clientes de referencia en México en el mismo tipo de servicio.
3. **¿Aceptan prestar el servicio a un programa que opera dentro de salas de juego con permiso de SEGOB? ¿Con qué condiciones? Respuesta por escrito.**
4. Esquema de precios completo: cargos únicos, mensuales, por transacción, mínimos, penalizaciones y ajustes por inflación.
5. SLA, penalizaciones, horario de soporte y cobertura geográfica.
6. Certificaciones vigentes (PCI DSS, ISO 27001, SOC 2, las que apliquen) con fecha y alcance.
7. Plazo de implementación con plan de trabajo.

**B. Emisor (banco o IFPE)**
1. Figura de la cuenta (depósito o fondos de pago electrónico) y niveles de cuenta disponibles; límites mensuales y en efectivo.
2. ¿Pueden contratar a la empresa como comisionista para recibir efectivo en kioscos? ¿Tienen la excepción de efectivo (si es IFPE)? ¿Límites por operación y por comisionista?
3. Política sobre MCC 7995, operaciones *quasi-cash* y uso en el propio casino; ¿bloquearán el juego en línea?
4. Requisitos de prefondeo o colateral (días, forma, rendimiento).
5. Requisitos de KYC en kiosco y proveedores aceptados.
6. Reparto de intercambio y de comisiones; costo por cuenta activa y por tarjeta.
7. Debida diligencia que aplicarán a los casinos (listas, beneficiario controlador, sanciones).
8. Atención a aclaraciones: quién hace qué y en qué plazo.

**C. Procesador**
1. Emisores con los que ya está certificado en México.
2. API de emisión instantánea, asociación de tarjeta innominada y activación en dispositivo.
3. Tokenización para reducir el alcance PCI del kiosco.
4. Costo por cuenta activa, por autorización y por plástico.

**D. Fabricante del kiosco**
1. Lista de componentes (validador, casete, caja fuerte, dispensador, escáner, cámara, impresora, PC, módem).
2. SDK y protocolos de cada periférico; soporte para Linux o Windows IoT.
3. Precio por volumen (10, 50 y 200 unidades); plazo de entrega; garantía; refacciones.
4. Certificaciones (NOM, GLI-20 si aplica).

**E. Traslado de valores**
1. Costo por visita, por monto trasladado y por conteo; seguro incluido y su límite.
2. Plazo de depósito en la cuenta del emisor (T+n).
3. Experiencia en casinos y protocolos con su seguridad.
4. Procedimiento ante diferencias y siniestros.

**F. Identidad**
1. Funcionamiento en kiosco (cámara fija), tasas de aprobación y de falsos rechazos, tiempos.
2. Fuentes de validación de INE y CURP.
3. Aceptación por la CNBV y por los emisores candidatos.
4. Precio por verificación y mínimos.

---

## 7. Modelo de negocio y evaluación financiera

### 7.1 Fuentes de ingreso y quién paga

| Ingreso | Quién lo paga | Por qué lo pagaría | Fase |
|---|---|---|---|
| **Instalación** | Casino | Puesta en marcha e integración con su sistema | 1 y 2 (opcional; en el modelo se absorbe y se recupera vía renta) |
| **Renta o suscripción mensual por kiosco** (incluye operación y mantenimiento) | Casino | Sustituye parte de la caja (personal, faltantes, robos) y aporta trazabilidad para la LFPIORPI | 1 y 2 |
| **Comisión por transacción** (% del efectivo convertido) | Casino | Alinea el pago con el uso | 1 y 2 |
| **Cargo por tarjeta** | Usuario (o subsidiado por el casino) | Plástico y activación | 2 |
| **Reparto de intercambio** | Emisor | Uso de la tarjeta fuera del casino | 2 |
| **Comisión de comisionista** | Emisor | Captación de efectivo y de clientes | 2 [S] |
| **Cargo por carga al usuario** | Usuario | **No recomendado al inicio**: frena la adopción | — |

> **El efectivo depositado por los usuarios no es ingreso de la empresa.** Se registra como recursos de terceros en tránsito (cuentas de orden), y el prefondeo es un activo (colateral), no un gasto.

### 7.2 Supuestos por escenario (todas son estimaciones [E], ninguna cotizada)

| Variable | Conservador | Base | Optimista |
|---|---|---|---|
| Costo del kiosco + instalación (MXN) | 300,000 + 30,000 | 240,000 + 25,000 | 200,000 + 20,000 |
| Desarrollo e integración (único) | 12,000,000 | 9,000,000 | 8,000,000 |
| Legal, cumplimiento y certificaciones (único) | 4,000,000 | 3,000,000 | 2,500,000 |
| Costo fijo mensual (equipo, nube, oficinas, seguros corporativos) | 1,100,000 | 900,000 | 800,000 |
| Kioscos al cierre de los años 1, 2 y 3 | 10 / 25 / 40 | 20 / 60 / 120 | 30 / 100 / 200 |
| Cargas por kiosco por día | 40 | 80 | 140 |
| Monto promedio por carga | $500 | $600 | $650 |
| Renta mensual cobrada al casino | 18,000 | 25,000 | 30,000 |
| Comisión por volumen al casino | 0.6% | 0.8% | 1.0% |
| Tarjetas nuevas por kiosco al mes / margen neto por tarjeta | 120 / $10 | 250 / $15 | 400 / $20 |
| Cuentas activas por kiosco / costo del emisor por cuenta activa al mes | 500 / $12 | 1,000 / $8 | 1,800 / $6 |
| KYC por alta | $25 | $20 | $18 |
| Traslado de valores por kiosco al mes | 12,000 | 7,000 | 5,000 |
| Mantenimiento / conectividad / seguro por kiosco al mes | 4,000 / 1,000 / 2,000 | 3,500 / 900 / 1,800 | 3,000 / 800 / 1,600 |
| Gasto fuera del casino / reparto de intercambio | 10% / 0.4% | 20% / 0.5% | 30% / 0.6% |
| Días de prefondeo | 4 | 3 | 2 |

### 7.3 Resultados por kiosco (régimen estable, mensual)

| Concepto (MXN/mes) | Conservador | Base | Optimista |
|---|---|---|---|
| Efectivo convertido (de usuarios, **no ingreso**) | 600,000 | 1,440,000 | 2,730,000 |
| Cargas | 1,200 | 2,400 | 4,200 |
| Renta | 18,000 | 25,000 | 30,000 |
| Comisión por volumen | 3,600 | 11,520 | 27,300 |
| Margen de tarjetas | 1,200 | 3,750 | 8,000 |
| Reparto de intercambio | 240 | 1,440 | 4,914 |
| **Ingreso total por kiosco** | **23,040** | **41,710** | **70,214** |
| Costos variables (emisor, KYC, traslado, mantenimiento, conectividad, seguro) | 28,000 | 26,200 | 28,400 |
| **Contribución por kiosco** | **−4,960** | **15,510** | **41,814** |
| Margen variable por operación (excluye la renta) | −$3.30 | $1.55 | $5.29 |
| Recuperación del kiosco (meses) | No se recupera | 17 | 5 |
| Prefondeo por kiosco (colateral recuperable) | 80,000 | 144,000 | 182,000 |

### 7.4 Resultados de la empresa (3 años)

| Indicador | Conservador | Base | Optimista |
|---|---|---|---|
| Inversión inicial (desarrollo + legal) | $16.0 M | $12.0 M | $10.5 M |
| Kioscos necesarios para el punto de equilibrio operativo | No alcanza | ≈ 58 | ≈ 19 |
| Flujo acumulado al cierre de los años 1, 2 y 3 (incluye capex y colateral) | −33.6 / −54.0 / −75.3 M | −29.1 / −48.8 / −67.4 M | −24.6 / −29.8 / −4.3 M |
| Necesidad máxima de financiamiento en 3 años | > $75 M (sin cierre) | ≈ $67 M (de los cuales ≈ $17 M es colateral recuperable) | ≈ $30 M |
| EBITDA del año 3 | −$15.6 M | +$11.5 M | +$90.8 M |
| Recuperación de la inversión | No se recupera | Después del año 5 [E] | ≈ año 3 a 4 [E] |

Desglose del escenario base (millones de MXN): año 1 = −12.0 inversión inicial, −5.3 kioscos, −2.9 colateral, −8.9 operación; año 2 = −10.6 kioscos, −5.8 colateral, −3.4 operación; año 3 = −15.9 kioscos, −8.6 colateral, +6.0 operación.

### 7.5 Sensibilidad (escenario base; contribución por kiosco y kioscos para equilibrio)

| Variación | Contribución por kiosco | Kioscos para equilibrio |
|---|---|---|
| Base | $15,510 | 58 |
| Renta −20% | $10,510 | 86 |
| Cargas por día −25% | $12,270 | 73 |
| Comisión por volumen −25% | $12,630 | 71 |
| Costo del emisor por cuenta +50% | $11,510 | 78 |
| Traslado de valores +50% | $12,010 | 75 |
| **Liquidación con el casino (sin traslado propio ni prefondeo) [P]** | **$21,010** | **43** (recuperación del kiosco en ≈ 13 meses) |

**Variables que más afectan la rentabilidad:** (1) la renta que acepte pagar el casino, que depende de cuánto personal de caja sustituya el kiosco; (2) el número de cargas diarias; (3) el costo de recolección del efectivo; (4) el costo del emisor por cuenta activa y los mínimos del procesador; (5) el capital inmovilizado en prefondeo.

**Lectura [S]:** el negocio se sostiene con **renta del casino más volumen**. Los ingresos de la tarjeta fuera del casino (intercambio) son marginales al inicio. La fase 1 (sin emisor, sin prefondeo, sin traslado propio) tiene mejor economía unitaria y menor necesidad de capital, lo que refuerza la recomendación de empezar por ahí.

---

## 8. Plan de desarrollo y recomendación

### 8.1 Etapas

| Etapa | Entregables | Responsables | Presupuesto [E] | Duración [E] | Dependencias | Criterio de aprobación |
|---|---|---|---|---|---|---|
| **0. Validación jurídica y regulatoria** | Dictamen sobre la figura, la naturaleza del saldo, el art. 86 del RLFJS, la LFPIORPI fr. II y datos personales; mapa de obligaciones | Fundadores + despacho financiero y de juego | $0.6 a $1.2 M | 6 a 10 semanas | Ninguna | Ruta legal viable para la fase 1 y al menos una para la fase 2 |
| **1. Validación comercial** | Entrevistas con 10 a 15 casinos; 2 cartas de intención; sondeo de 6 a 8 emisores; 2 cartas de intención de emisor | Fundadores | $0.3 a $0.6 M | 8 a 12 semanas (en paralelo con la 0) | Ninguna | 2 casinos y 1 emisor comprometidos por escrito |
| **2. Selección de aliados** | RFP con el cuestionario 6.3; matriz de evaluación; contratos marco | Fundadores + asesor | $0.3 M | 6 a 8 semanas | Etapas 0 y 1 | Propuestas comparables y precios que mantengan la contribución base |
| **3. Diseño** | Experiencia de usuario, arquitectura, modelo de riesgos, manuales operativos | Producto + tecnología | $1.0 a $1.5 M | 8 semanas | Etapa 2 | Diseño aprobado por el casino y (fase 2) por el emisor |
| **4. Prototipo** | 2 kioscos funcionales en laboratorio; integración con el sistema del casino en ambiente de pruebas | Tecnología + fabricante | $1.5 a $2.5 M | 10 a 12 semanas | Etapa 3 | Pruebas de extremo a extremo; prueba de penetración sin hallazgos críticos |
| **5. Integración** | Integración certificada con el sistema del casino (fase 1) y con el emisor y procesador (fase 2) | Tecnología + aliados | Fase 1: $2 a $3 M; fase 2: $3 a $5 M | 3 a 6 meses | Etapas 2 y 4 | Certificaciones del emisor y la red (fase 2); aprobación del casino |
| **6. Piloto** | 4 a 6 kioscos en 2 salas, 90 días | Operación + casinos | $2 a $3 M (incluye hardware) | 3 meses | Etapa 5 | Métricas del piloto (8.2) |
| **7. Escalamiento** | 20 kioscos al año 1 y 60 al año 2 (base) | Toda la empresa | Según la sección 7 | 12 a 24 meses | Piloto aprobado y financiamiento | Contribución por kiosco ≥ $15,000 MXN/mes y cero incidentes regulatorios |

### 8.2 Producto mínimo viable y métricas del piloto

**MVP (fase 1):** kiosco que identifica al jugador (credencial del casino + INE), recibe efectivo, acredita saldo de juego en el sistema del casino, emite o recarga la tarjeta de jugador, imprime el comprobante y envía todo a la consola de conciliación. Sin premios ni retiros.

**MVP (fase 2):** lo anterior más alta de cuenta en el emisor (nivel 2 o 2 Bis), abono y tarjeta prepagada de red abierta, con app básica de saldo y bloqueo.

| Métrica | Meta para aprobar el piloto |
|---|---|
| Cargas por kiosco por día (promedio, últimos 30 días) | ≥ 60 |
| % del efectivo de la sala que pasa por kioscos | ≥ 15% |
| Tiempo medio de carga (usuario registrado) | ≤ 60 segundos |
| Tiempo de registro inicial | ≤ 4 minutos |
| Tasa de aprobación de KYC | ≥ 85% |
| Disponibilidad del kiosco | ≥ 98% |
| Diferencias de efectivo no explicadas | 0 al cierre de cada conciliación semanal |
| Abonos pendientes > 15 minutos | < 0.1% |
| Reclamaciones por cada 1,000 operaciones | < 2 |
| Recurrencia (usuarios con 2 o más cargas en 30 días) | ≥ 40% |
| Disposición del casino a pagar | Renta firmada ≥ $20,000 MXN/kiosco/mes |
| Incidentes de fraude o PLD escalados por el emisor o el casino | 0 graves |

### 8.3 Recomendación

**Avanzar con condiciones.** La oportunidad es real (salas que siguen operando con mucho efectivo, presión de la LFPIORPI y de la nueva ley de pagos digitales, ausencia de una oferta integrada en México), pero el modelo completo depende de terceros regulados que hoy no han confirmado interés en el giro. Se recomienda:

1. Empezar por la **fase 1 (cashless cerrado)** como proveedor tecnológico y operador.
2. Preparar la **fase 2 (red abierta)** con un banco como aliado preferente y la empresa como *program manager* y comisionista.
3. **No** buscar autorización propia en esta etapa.
4. **Detener o reformular** si en 90 días no hay ruta legal clara para la fase 1 **o** si ningún casino firma carta de intención con renta compatible con el modelo.

### 8.4 Principales riesgos y medidas

| Riesgo | Probabilidad / impacto | Medida |
|---|---|---|
| Ningún emisor acepta el giro de casinos | Alta / alto | Fase 1 independiente del emisor; sondeo amplio (bancos e IFPE); paquete de controles de PLD como argumento de venta |
| Interpretación del art. 86 del RLFJS o criterio de SEGOB contrario | Media / alto | Dictamen previo; consulta con SEGOB a través del casino |
| Calificación de la empresa como captadora o emisora sin autorización | Baja con un buen diseño / crítico | Dictamen; nunca custodiar saldos a nombre propio; contratos de comisión |
| Uso del kiosco para lavado de dinero (fraccionamiento) | Media / crítico | Límites, monitoreo compartido con el casino y el emisor, sin retiros en efectivo en el MVP |
| Reforma a la LFJS o vencimiento de permisos | Media / alto | Diversificar casinos y estados; contratos con salida; vigilar el proceso legislativo |
| Sanciones o bloqueos a casinos clientes | Media / alto | Debida diligencia de los casinos (beneficiario controlador, listas, sanciones) antes de firmar |
| Robo o faltantes de efectivo | Media / medio | Seguro, bóveda certificada, doble control, ubicación vigilada |
| Fallas del proveedor de traslado de valores | Media / medio | Dos proveedores; límites de exposición; liquidación con el casino si es viable |
| Economía unitaria insuficiente | Media / alto | Renta mínima en contrato; decisión de escalar basada en las métricas del piloto |
| Datos biométricos y seguridad | Media / alto | Privacidad desde el diseño; no almacenar imágenes en el kiosco; pruebas de penetración |
| La SHCP no incluye casinos en los sectores obligatorios | Alta / bajo | El caso de negocio no depende de esa obligación |

### 8.5 Decisiones que deben tomarse antes de invertir

1. Alcance del primer año: ¿fase 1 cerrada como producto, o solo como vehículo hacia la tarjeta abierta?
2. Figura legal de la empresa y estructura societaria (con base en el dictamen).
3. Tipo de emisor preferente (banco frente a IFPE) y tolerancia a la dependencia de un solo emisor.
4. Modelo de recolección de efectivo (traslado propio frente a liquidación con el casino).
5. Precio y modelo de cobro a los casinos (renta mínima, comisión por volumen).
6. Comprar frente a adaptar el kiosco.
7. Presupuesto y fuente de financiamiento por etapas (capital, deuda para hardware, colateral provisto por un aliado).
8. Si el canal en línea entra o no en los primeros 24 meses.

### 8.6 Plan de trabajo para los primeros 90 días

| Semanas | Actividades | Entregable |
|---|---|---|
| 1 a 2 | Contratar despacho (financiero y de juego); definir preguntas del dictamen (sección 2.4); preparar la presentación para casinos y emisores; monitorear la determinación de sectores de la SHCP en el DOF | Carta de encargo; presentación comercial |
| 3 a 6 | Entrevistas con 10 a 15 casinos (costo de caja, volumen de efectivo, sistema de gestión, disposición a pagar); sondeo de 6 a 8 bancos e IFPE; reuniones con 2 proveedores de sistemas de casino | Notas de entrevistas; mapa de interés |
| 5 a 8 | Dictamen preliminar; envío de la RFP (cuestionario 6.3) a emisores, procesadores, identidad, fabricantes y traslado | Dictamen preliminar; RFP enviada |
| 7 a 10 | Cartas de intención con casinos y emisores; evaluación de propuestas; recalcular el modelo financiero con cotizaciones reales | 2 + 1 cartas de intención; modelo financiero v2 |
| 10 a 12 | Dictamen final; diseño de alto nivel del piloto; presupuesto detallado de las etapas 3 a 6; **comité de decisión: avanzar, reformular o detener** | Dictamen final; plan del piloto; decisión documentada |

Presupuesto estimado de los 90 días: **$1.2 a $2.0 millones MXN** [E] (despacho, asesoría de cumplimiento, viajes y entrevistas, prueba de concepto de identidad, tiempo del equipo fundador no incluido).

---

## Anexo A. Fuentes consultadas (9 de noviembre de 2026)

**Ley de Economía Digital para Pagos Digitales y Electrónicos**
- DOF, 4-nov-2026 (no se pudo leer el texto completo por bloqueo del sitio a la lectura automática): https://dof.gob.mx/nota_detalle.php?codigo=5803017&fecha=04%2F11%2F2026
- PYME Noticias (arts. 1, 2, 10-16, 19, 20 y transitorios): https://www.pymenoticias.com/post/mexico-ley-pagos-digitales-dof-hacienda-sectores
- Infobae, 5-nov-2026: https://www.infobae.com/mexico/2026/11/05/publican-en-el-dof-la-ley-de-economia-digital-hacienda-tiene-15-dias-para-definir-quien-ya-no-aceptara-efectivo/
- El Heraldo de Saltillo (Agencia Reforma), 4-nov-2026: https://elheraldodesaltillo.mx/2026/11/04/publican-ley-de-pagos-digitales-obligatorios/
- Basham: https://basham.com.mx/en/se-publica-la-ley-de-economia-digital-para-pagos-digitales-y-electronicos/

**Prevención de lavado de dinero**
- EY, reforma a la LFPIORPI (DOF 16-jul-2025): https://www.ey.com/es_mx/technical/tax/boletines-fiscales/reforma-ley-antilavado-2025-nuevas-obligaciones
- Holland & Knight, Reglas de carácter general (DOF 7-ago-2026): https://www.hklaw.com/en/insights/publications/2026/08/nuevas-reglas-de-caracter-general-de-la-ley-antilavado-en-mexico
- SAT, Portal de PLD, umbrales: https://sppld.sat.gob.mx/pld/interiores/umbrales.html
- KYC Systems, juegos con apuesta y LFPIORPI 2026 (fuente comercial, usada para el art. 32 y el calendario): https://kyc-systems.com/blog/juegos-con-apuesta-sorteos-lfpiorpi

**Juegos y sorteos**
- Reglamento de la LFJS (Cámara de Diputados): https://www.diputados.gob.mx/LeyesBiblio/regley/Reg_LFJS.pdf
- Texto consolidado con artículos (arts. 5, 10, 11, 29, 85, 86, 87): https://mley.mx/Reg_LFJS/index.html.md
- Greenberg Traurig, reforma al RLFJS (DOF 16-nov-2023): https://www.gtlaw.com/es/insights/2023/11/reformas-y-adiciones-de-diversas-disposiciones-al-reglamento-de-la-ley-federal-de-juegos-y-sorteos
- Focus Gaming News, SCJN (abr-2025): https://focusgn.com/latinoamerica/la-suprema-corta-de-mexico-rechaza-expedirse-sobre-el-estatus-legal-de-los-casinos
- Revista Casino Perú, reforma a la LFJS (sep-2026): https://www.revistacasinoperu.com/mexico-reforma-a-la-ley-federal-juegos
- Vanguardia, regulación 2026: https://www.vanguardia.com/mundo/2026/08/20/asi-cambio-la-regulacion-de-apuestas-y-casinos-en-mexico-en-2026/
- Focus Gaming News, IEPS al 50%: https://focusgn.com/latinoamerica/diputados-mexicanos-aprueban-la-suba-del-ieps-que-eleva-la-carga-impositiva-a-los-juegos-de-azar-a-un-50
- LIEPS 2026 (versión publicada por el IMCP): https://test2.imcp.org.mx/wp-content/uploads/2025/12/LIEPS-2026-DOF.pdf

**Regulación financiera**
- Ley para Regular las Instituciones de Tecnología Financiera (arts. 11, 22-29, 45, 54): https://www.ordenjuridico.gob.mx/Documentos/Federal/html/wo120920.html
- LIC art. 46 Bis 2: https://mley.mx/LIC/articulo/46-bis%202/index.html.md · art. 46 Bis 1: https://mley.mx/LIC/articulo/46-bis%201/
- Nivel 2 Bis (DOF 10-jul-2026, Circular 11/2026 de Banxico; fuente secundaria): https://kyc-systems.com/blog/cuenta-nivel-2-bis
- White & Case, disposiciones secundarias para IFPE: https://www.whitecase.com/sites/default/files/2021-02/secondary-fintech-regulations-related-to-e-money-institutions-ifpes-spanish.pdf

**Datos personales**
- Garrigues, nueva LFPDPPP (DOF 20-mar-2025): https://www.garrigues.com/es_ES/noticia/mexico-nueva-ley-federal-proteccion-datos-personales-posesion-particulares-introduce

**Redes de pago**
- Visa, Visa Integrity Risk Program (documento regional, 6-abr-2023): https://visa.cl/content/dam/VCOM/regional/lac/SPA/chile/Documents/visa-integrity-risk-program-chile.pdf
- Reglas de Mastercard: **no consultadas** [P].

**Mercado**
- INEGI, ENIF 2024 (13-mar-2025): https://www.inegi.org.mx/contenidos/saladeprensa/boletines/2025/enif/ENIF2024_CP.pdf
- UnoTV, cifras de la AIEJA (13-nov-2025): https://www.unotv.com/negocios/cuanto-generan-los-casinos-y-que-revisan-hoy-las-autoridades/
- La Crónica, traslado de valores (4-ago-2026): https://www.cronica.com.mx/negocios/2026/08/04/clientes-reportan-presuntos-retrasos-en-la-entrega-de-efectivo-resguardado-por-servicio-panamericano/
- Latam Fintech, Pomelo en México: https://www.latamfintech.co/articles/pomelo-nombra-a-jacob-levin-como-country-manager-de-mexico-y-se-consolida-como-socio-estrategico-de-los-grandes-emisores-de-tarjetas
- Gambling.com, métodos de pago de operadores en línea en México (OXXO, ToditoCash, Paysafecard): https://www.gambling.com/mx/casino-online/oxxo
- Cronista, autorizaciones fintech: https://www.cronista.com/mexico/finanzas-economia/en-7-anos-vive-mexico-auge-de-solicitudes-fintech-pero-apenas-89-han-sido-autorizadas

## Anexo B. Pendientes de validación (resumen)

1. Texto oficial del DOF de la Ley de Economía Digital y la lista de sectores de la SHCP (esperada hacia finales de noviembre de 2026).
2. Interpretación del art. 86 del RLFJS para cargas de saldo de juego desde kiosco o tarjeta.
3. Artículos de sanción penal por captación o emisión de fondos de pago electrónico sin autorización (LIC y Ley Fintech).
4. Límites de depósito en efectivo por comisionista y si una sala de juego puede ser establecimiento de comisionista.
5. Límites de las cuentas de nivel 1 y 3 y sus equivalentes para IFPE.
6. Monto y fracción exactos del art. 32 de la LFPIORPI aplicables a juegos con apuesta.
7. Base y alcance de la tasa del IEPS de 2026 para juego presencial.
8. Reglas de Mastercard y políticas de los emisores sobre MCC 7995, *quasi-cash* y recargas en ubicaciones de juego.
9. Exigencia de GLI-16 o GLI-20 por parte de SEGOB o de los casinos.
10. Todos los precios de proveedores (no hay cotizaciones).
11. Número oficial de salas en operación (solo hay cifras gremiales).
