# EI1027 - Disseny i Implementació de Sistemes d'informació
## Memoria del proyecto desarrollado: OVI System

**Autores:**
- Rubén Martínez Cabedo
- Ignacio Odriozola Chop
- Alex Forcada Capella

---

# Índice

1. [Introducción](#1-introducción)
2. [Diseño de la base de datos](#2-diseño-de-la-base-de-datos)
3. [Diseño de interfaces de usuario](#3-diseño-de-interfaces-de-usuario)
4. [Implementación](#4-implementación)
5. [Mejoras implementadas](#5-mejoras-implementadas)
6. [Conclusiones](#6-conclusiones)
7. [Bibliografía y Referencias Técnicas](#7-bibliografía-y-referencias-técnicas)

---

# 1. Introducción

## 1.1. Objetivos y funcionalidades

El objetivo principal de este proyecto es el desarrollo de una aplicación web para la gestión integral de la **Oficina de Vida Independent (OVI)**, que actúa como intermediaria entre personas usuarias que necesitan Asistencia Personal y los profesionales que la ofrecen (Assistents Personals, de tipo PAP o PATY).

La plataforma, desarrollada con Spring Boot y el patrón MVC, cubre el ciclo completo del servicio: desde el registro y validación de usuarios hasta la formalización de contratos, pasando por la negociación mediante chat entre las partes.

**Funcionalidades principales implementadas:**

- **Gestión de usuarios y asistentes:** Registro, validación por parte del técnico OVI (aceptar/rechazar con motivo), y edición de perfil. Los asistentes incluyen información adicional como formación académica, experiencia previa y proximidad geográfica.

- **Flujo de solicitudes de asistencia (APRequest):** El usuario crea una solicitud indicando el tipo de servicio (PAP o PATY) y sus preferencias. El técnico la revisa mediante un flujo de confirmación en dos pasos (pantalla de revisión + modal de confirmación) antes de aprobar o rechazar.

- **Preselección de candidatos AP (CandidatPreassignat):** El técnico puede preseleccionar qué asistentes son visibles para cada solicitud aprobada. Si existe preselección, el usuario sólo ve los candidatos preseleccionados; si no existe, ve todos los candidatos compatibles ordenados por relevancia.

- **Sistema de negociación por chat (Conversa/Missatge):** Una vez aprobada la solicitud, se habilita un canal de comunicación entre el usuario y los asistentes candidatos para negociar los términos del servicio. El sistema verifica la propiedad de cada conversación antes de permitir enviar mensajes.

- **Registro de contratos (RegistreContracte):** Cuando hay acuerdo, se formaliza un contrato vinculado a la solicitud, con fechas de inicio y fin y documento PDF adjunto. Al crear el contrato, la solicitud pasa automáticamente a estado `Tancada_Contracte`.

- **Projecte de Vida:** Documento personal libre que cada usuario OVI puede redactar y actualizar con sus objetivos y aspiraciones vitales. Es accesible en modo lectura por los asistentes con quienes el usuario tiene una conversación activa, y por el técnico OVI desde el directorio de usuarios.

- **Módulo de formación continua:** Los asistentes pueden consultar e inscribirse en actividades formativas y de divulgación impartidas por formadores especializados.

- **Panel del técnico OVI:** Vista centralizada para auditar todo el sistema: validar altas, aprobar solicitudes, supervisar negociaciones, consultar contratos y gestionar bajas de usuarios y asistentes.

- **Baixa de cuenta con anonimización RGPD:** Tanto el usuario (autocancelación) como el técnico (baja administrativa) pueden dar de baja una cuenta. El proceso verifica que no haya contrato activo, cierra las solicitudes abiertas y anonimiza los datos personales (nombre, apellidos, email, teléfono, contraseña) preservando el historial operativo para no romper la integridad referencial.

## 1.2. Uso de IA Generativa

En el desarrollo de este proyecto, se ha integrado el uso de herramientas de Inteligencia Artificial Generativa como apoyo técnico, bajo un modelo de supervisión humana constante. El uso de esta tecnología se ha centrado en tres pilares fundamentales:

- **Soporte en el aprendizaje de nuevas tecnologías:** Dado que el entorno de Spring Boot y el manejo de JDBC eran conceptos nuevos para varios miembros del equipo, la IA se ha utilizado como una tutoría de refuerzo para comprender el flujo de datos entre controladores, servicios y DAOs.
- **Agilización de tareas repetitivas:** Se ha empleado para la redacción inicial de scripts SQL de creación de tablas, permitiendo al grupo centrarse en la lógica de negocio y en la integridad referencial del sistema.
- **Depuración y corrección de errores:** Ha servido como herramienta de apoyo en la resolución de errores de configuración del entorno (como dependencias de Maven o problemas de conexión al dataSource) y en la optimización de consultas SQL complejas.

**Declaración de autoría:** Todas las sugerencias proporcionadas por la IA han sido revisadas, testeadas y validadas por los miembros del grupo para asegurar que cumplen con los requisitos específicos de la asignatura y el diseño lógico planteado.

## 1.3. Organización del Trabajo en Grupo

### 1.3.1. Composición del equipo y roles

| Miembro | Identificador UJI | Rol formal | Responsabilidades principales |
|---|---|---|---|
| Rubén Martínez Cabedo | al438617 | Jefe de Proyecto y Desarrollador Full-Stack | Coordinación general, implementación de lógica de negocio con Spring Boot (controladores, servicios y capa DAO), diseño y desarrollo de vistas Thymeleaf. |
| Álex Forcada Capella | al395369 | Responsable de Base de Datos y Arquitectura de Información | Diseño conceptual y lógico de la base de datos (modelo UML, diseño relacional), redacción y mantenimiento de scripts SQL (DDL/DML), diseño del sitemap y estructura de URLs. |
| Ignacio Odriozola Chop | al435182 (n4ch0p0) | Responsable de Calidad, Accesibilidad y Documentación | Integración y revisión del código en el repositorio compartido, pruebas de accesibilidad con WAVE, evaluación heurística, tests de aceptación y redacción de la presente memoria. |

### 1.3.2. Metodología de trabajo

El desarrollo del proyecto se ha organizado siguiendo una metodología iterativa e incremental, fundamentada en ciclos de trabajo semanales. Se ha utilizado GitHub como repositorio centralizado para el control de versiones, con gestión de ramas y commits descriptivos. La comunicación diaria se ha apoyado en un grupo de WhatsApp para la coordinación rápida.

### 1.3.3. Reparto de tareas por fase del proyecto

| Fases del proyecto | Rubén (Full-Stack) | Álex (BD / Navegación) | Ignacio (QA / Documentación) |
|---|---|---|---|
| Fase 1: Diseño | Definición de la arquitectura MVC y estructura de paquetes | Modelo UML conceptual y diseño lógico relacional | Análisis de requisitos y redacción inicial de la memoria |
| Fase 2: Backend | Controladores, servicios y DAOs (JDBC) | Scripts CREATE TABLE, INSERTs de prueba y tipos ENUM | Pruebas de integración y verificación de consultas |
| Fase 3: Frontend | Vistas Thymeleaf, formularios y navegación | Sitemap y estructura de URLs | Evaluación WAVE, test heurístico y test de aceptación |
| Fase 4: Mejoras | Chat técnico (ConversaTecnic), Projecte de Vida, Baixa RGPD, preselección de candidatos | Ajustes en integridad referencial y nuevas tablas | Documentación final, conclusiones y bibliografía |

---

# 2. Diseño de la base de datos

## 2.1. Diseño Conceptual

*(Ver diagrama UML adjunto en la versión PDF del documento — página 7)*

El modelo conceptual parte del esquema DCV01 proporcionado por la asignatura e incorpora las entidades y relaciones adicionales necesarias para el sistema final implementado.

## 2.2. Modificaciones realizadas

El modelo UML original proporcionado por la asignatura (DCV01) definía una estructura básica con las siguientes entidades: UsuariOVI, APRequest, Seleccion, AssistentPersonal, ComunicacióUsuariOVIPAP, RegistreContracte, Formador, ActivitatFormació y AssistènciaFormació.

Respecto a ese modelo inicial, el diseño final incorpora las siguientes modificaciones:

**Entidades renombradas y reestructuradas:**

- La entidad `Seleccion` y la entidad `ComunicacióUsuariOVIPAP` han sido fusionadas y rediseñadas en dos entidades separadas con responsabilidades más claras: **Conversa** y **Missatge**. Esta separación permite registrar el historial completo de comunicaciones y no solo la existencia del canal.

**Atributos añadidos:**

- **UsuariOVI** se ha ampliado con los campos `telefono`, `contrasenya`, `consentimentInformat`, `estat` (enum EstatValidacio), `motiu_rebuig` y **`projecte_vida`** (TEXT, nullable). Este último campo almacena el documento personal de objetivos vitales del usuario, editable por él mismo y accesible en lectura por asistentes y el técnico.
- **AssistentPersonal** incorpora atributos nuevos como `formacioAcademica`, `experienciaPrevia`, `proximitatGeografica`, `actiu`, `estat` y `motiu_rebuig`.
- **APRequest** añade `preferencies` y el enum EstatAPR con los estados del flujo completo (`Revisio`, `Aprovada`, `Rebutjada`, `Tancada_Contracte`, `Finalitzada`).
- **RegistreContracte** incorpora `fecha_Inici`, `fecha_Fin` y `pdf_Path`.
- **AssistènciaFormació** se amplía con `assistencia` (booleà) i `url_certificat`.

**Tipos enumerados formalizados:**

El modelo final introduce explícitamente los enumerados `EstatValidacio`, `TipusAP`, `EstatAPR`, `TipusAF`, `TipusEmissor` y `TipusEmissorTecnic`.

**Entidades añadidas como mejoras:**

Como parte de las mejoras opcionales implementadas, se han incorporado cuatro nuevas entidades no presentes en el modelo original:

- **ConversaTecnic:** Canal de comunicación persistente entre un UsuariOVI y el técnico de la plataforma (relación 1:1 con UsuariOVI).
- **MissatgeTecnic:** Almacena cada mensaje individual del canal técnico, con su emisor (`Usuari` o `Tecnic`), contenido y marca temporal.
- **CandidatPreassignat:** Tabla de preselección que permite al técnico OVI indicar qué asistentes son candidatos recomendados para una solicitud concreta antes de que el usuario acceda a la vista de candidatos. Compuesta exclusivamente por las claves foráneas `id_request` y `dni_ap`. Si no hay preselección para una solicitud, el usuario ve todos los candidatos compatibles; si la hay, solo ve los preseleccionados.
- El campo **`projecte_vida`** en UsuarioOVI actúa como entidad lógica propia aunque se implementa como columna: recoge el Plan de Vida del usuario y está sujeto a acceso diferenciado según el rol (edición propia, lectura por AP con conversación activa, lectura por técnico).

## 2.3. Diseño Lógico

### 2.3.1. Entidades y Atributos

#### UsuarioOVI

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_usuario | Serial | PK, NOT NULL | Auto-incremental | Identificador único del usuario |
| dni | VARCHAR(20) | UNIQUE, NOT NULL | — | Documento de identidad |
| nom | VARCHAR(100) | NOT NULL | — | Nombre |
| cognoms | VARCHAR(100) | NOT NULL | — | Apellidos |
| email | VARCHAR(150) | NOT NULL | — | Correo electrónico |
| telefono | INT | NULL permitido | NULL | Teléfono de contacto |
| contrasenya | VARCHAR(255) | NOT NULL | — | Contraseña cifrada (BCrypt) |
| consentimentInformat | BOOLEAN | NOT NULL | FALSE | Consentimiento legal LOPD |
| estat | estat_validacio | NOT NULL | 'Pendent' | Estado de la cuenta |
| motiu_rebuig | TEXT | NULL permitido | NULL | Razón del rechazo, si aplica |
| **projecte_vida** | **TEXT** | **NULL permitido** | **NULL** | **Documento personal de objetivos vitales (Projecte de Vida)** |

#### AssistentPersonal

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_ap | Serial | PK, NOT NULL | Auto-incremental | Identificador único |
| dni | VARCHAR(20) | UNIQUE, NOT NULL | — | Documento de identidad |
| nom | VARCHAR(100) | NOT NULL | — | Nombre |
| cognoms | VARCHAR(100) | NOT NULL | — | Apellidos |
| email | VARCHAR(150) | NOT NULL | — | Correo electrónico |
| telefono | INT | NULL permitido | NULL | Teléfono |
| contrasenya | VARCHAR(255) | NOT NULL | — | Contraseña cifrada |
| tipus | tipus_ap | NOT NULL | — | Tipo de asistente (PAP o PATY) |
| formacioAcademica | TEXT | NULL permitido | NULL | Formación académica |
| experienciaPrevia | TEXT | NULL permitido | NULL | Experiencia previa |
| proximitatGeografica | TEXT | NULL permitido | NULL | Zona geográfica de trabajo |
| actiu | BOOLEAN | NOT NULL | TRUE | Indica si está disponible |
| estat | estat_validacio | NOT NULL | 'Pendent' | Estado de validación |
| motiu_rebuig | TEXT | NULL permitido | NULL | Razón del rechazo |

#### APRequest

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_request | Serial | PK, NOT NULL | Auto-incremental | Identificador de la solicitud |
| id_usuario | INT | FK → UsuarioOVI(id_usuario), NOT NULL | — | Usuario solicitante (clave surrogate) |
| tipusServei | tipus_ap | NOT NULL | — | Tipo de servicio (PAP/PATY) |
| preferencies | TEXT | NULL permitido | NULL | Preferencias del usuario |
| estat | estat_apr | NOT NULL | 'Revisio' | Estado del flujo |

> **Nota sobre la FK id_usuario:** APRequest almacena el `id_usuario` numérico (clave surrogate) de UsuarioOVI, **no el DNI**. Todos los DAOs realizan un JOIN explícito con UsuarioOVI para obtener el DNI cuando es necesario. La política de borrado es RESTRICT: no se permite borrar un usuario con solicitudes asociadas; se debe anonimizar en su lugar.

**Estados del ENUM `estat_apr`:** `Revisio` → `Aprovada` / `Rebutjada` → (si Aprovada) `Tancada_Contracte` → `Finalitzada`

#### Conversa

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_conversa | Serial | PK, NOT NULL | Auto-incremental | Identificador de la conversación |
| id_request | INT | FK → APRequest(id_request), NOT NULL | — | Solicitud asociada |
| id_ap | INT | FK → AssistentPersonal(id_ap), NOT NULL | — | Asistente participante |
| data_inici | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Fecha de inicio |

**Restricción UNIQUE:** `(id_request, id_ap)` — Solo una conversación por par solicitud-asistente.

Políticas FK: ON UPDATE CASCADE / ON DELETE RESTRICT en ambas claves ajenas.

#### Missatge

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_missatge | Serial | PK, NOT NULL | Auto-incremental | Identificador del mensaje |
| id_conversa | INT | FK → Conversa(id_conversa), NOT NULL | — | Conversación a la que pertenece |
| emissor | tipus_emissor | NOT NULL | — | Quién envía (`Usuari` o `AP`) |
| text_missatge | TEXT | NOT NULL | — | Contenido del mensaje |
| data_enviament | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Fecha de envío |

#### RegistreContracte

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_contracte | Serial | PK, NOT NULL | Auto-incremental | Identificador del contrato |
| id_request | INT | FK → APRequest(id_request), UNIQUE, NOT NULL | — | Solicitud asociada (1:1) |
| id_ap | INT | FK → AssistentPersonal(id_ap), NOT NULL | — | Asistente asignado |
| fecha_Inici | DATE | NOT NULL | — | Fecha de inicio del contrato |
| fecha_Fin | DATE | NULL permitido | NULL | Fecha de fin (NULL = indefinido) |
| pdf_Path | VARCHAR(255) | NULL permitido | NULL | Ruta al documento PDF del contrato |

Políticas FK: ON UPDATE CASCADE / ON DELETE RESTRICT en ambas claves ajenas.

> Al formalizar un contrato, el DAO ejecuta en transacción: INSERT en RegistreContracte + UPDATE APRequest SET estat = 'Tancada_Contracte'.

#### CandidatPreassignat *(nueva — no estaba en el modelo original)*

Tabla de preselección que permite al técnico OVI indicar qué asistentes son candidatos recomendados para una solicitud concreta. Si no hay filas para un `id_request`, el usuario ve todos los candidatos compatibles. Si hay filas, solo ve los preseleccionados.

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_request | INT | FK → APRequest(id_request), NOT NULL | — | Solicitud para la que se preselecciona |
| dni_ap | VARCHAR(20) | FK → AssistentPersonal(dni), NOT NULL | — | DNI del asistente preseleccionado |

**Clave primaria compuesta:** `(id_request, dni_ap)`

**Políticas FK:**
- `id_request` → ON UPDATE CASCADE / ON DELETE CASCADE (si se borra la solicitud, desaparecen sus preselecciones)
- `dni_ap` → ON UPDATE CASCADE / ON DELETE CASCADE (si se da de baja un asistente, se eliminan sus preselecciones antes de anonimizar)

> **Nota de implementación:** El DAO implementa el patrón delete-and-reinsert: al guardar la selección del técnico, primero borra todas las filas de ese `id_request` y después inserta las nuevas. El método `removeAssistentFromAllSeleccions(dniAp)` se invoca durante la baja de un asistente, antes de anonimizarlo.

#### Formador

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_formador | Serial | PK, NOT NULL | Auto-incremental | Identificador del formador |
| nombre | VARCHAR(100) | NOT NULL | — | Nombre del formador |
| especialidad | VARCHAR(100) | NULL permitido | NULL | Área de especialización |

#### ActivitatFormacio

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_actividad | Serial | PK, NOT NULL | Auto-incremental | Identificador de la actividad |
| id_formador | INT | FK → Formador(id_formador), NOT NULL | — | Formador responsable |
| titol | VARCHAR(250) | NOT NULL | — | Título de la actividad |
| fecha | TIMESTAMP | NOT NULL | — | Fecha y hora programada |
| tipus | tipus_af | NOT NULL | — | Tipo (formación o divulgación) |

#### AssistenciaFormacio

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_assistencia | Serial | PK, NOT NULL | Auto-incremental | Identificador del registro |
| id_actividad | INT | FK → ActivitatFormacio(id_actividad), NOT NULL | — | Actividad formativa |
| id_usuario | INT | FK → UsuarioOVI(id_usuario), NULL | NULL | Usuario inscrito (si aplica) |
| id_ap | INT | FK → AssistentPersonal(id_ap), NULL | NULL | Asistente inscrito (si aplica) |
| assistencia | BOOLEAN | NOT NULL | FALSE | Si asistió o no |
| url_certificat | VARCHAR(255) | NULL permitido | NULL | URL del certificado generado |

**Restricción CHECK:** Exactamente uno de `id_usuario` o `id_ap` debe ser NOT NULL (exclusividad).

Políticas FK: `id_actividad` → RESTRICT; `id_usuario` e `id_ap` → SET NULL (desvincular sin borrar el registro de asistencia).

#### ConversaTecnic *(mejora)*

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_conversa_tecnic | Serial | PK, NOT NULL | Auto-incremental | Identificador |
| dni_usuario | VARCHAR(20) | FK → UsuarioOVI(dni), UNIQUE, NOT NULL | — | Usuario (relación 1:1) |
| data_inici | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Fecha de apertura |

Política FK `dni_usuario`: ON UPDATE CASCADE / ON DELETE RESTRICT.

#### MissatgeTecnic *(mejora)*

| Atributo | Tipo | Restricciones | Valor por defecto | Descripción |
|---|---|---|---|---|
| id_missatge | Serial | PK, NOT NULL | Auto-incremental | Identificador del mensaje |
| id_conversa_tecnic | INT | FK → ConversaTecnic(id_conversa_tecnic), NOT NULL | — | Conversación de soporte |
| emissor | VARCHAR(10) | CHECK ('Usuari','Tecnic'), NOT NULL | — | Quién envía |
| text_missatge | TEXT | NOT NULL | — | Contenido |
| data_enviament | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | Fecha de envío |

Política FK: ON UPDATE CASCADE / ON DELETE RESTRICT.

---

### 2.3.2. Relaciones y Cardinalidad

| Origen | Destino | Tipo | Descripción |
|---|---|---|---|
| UsuarioOVI | APRequest | 1:N | Un usuario puede realizar múltiples solicitudes de asistencia. |
| APRequest | Conversa | 1:N | Una solicitud puede generar varios chats con distintos APs candidatos. |
| AssistentPersonal | Conversa | 1:N | Un AP puede participar en varios chats de diferentes solicitudes. |
| Conversa | Missatge | 1:N | Una conversación contiene múltiples mensajes. |
| APRequest | RegistreContracte | 1:1 | Una solicitud se cierra con un único contrato formal. |
| AssistentPersonal | RegistreContracte | 1:N | Un AP puede estar vinculado a varios contratos (de distintas solicitudes). |
| **APRequest** | **CandidatPreassignat** | **1:N** | **Una solicitud puede tener varios APs preseleccionados por el técnico.** |
| **AssistentPersonal** | **CandidatPreassignat** | **1:N** | **Un AP puede estar preseleccionado en varias solicitudes distintas.** |
| Formador | ActivitatFormacio | 1:N | Un formador puede impartir muchas actividades. |
| ActivitatFormacio | AssistenciaFormacio | 1:N | Una actividad tiene una lista de asistentes. |
| Usuario/AP | AssistenciaFormacio | 1:N | Tanto usuarios como APs pueden inscribirse en múltiples formaciones. |
| ConversaTecnic | MissatgeTecnic | 1:N | Una conversación técnica está compuesta por el histórico de mensajes entre usuario y técnico. |
| UsuarioOVI | ConversaTecnic | 1:1 | Un usuario tiene un único canal de soporte con el técnico. |

---

### 2.3.3. Reglas de Integridad y Políticas de Borrado

**Política general: RESTRICT en lugar de CASCADE**

Tras la revisión de la segunda tutoría, se ha adoptado una política de borrado RESTRICT como norma general para todas las claves ajenas del sistema. Si se utilizara ON DELETE CASCADE y por error se eliminara un registro de UsuarioOVI, el sistema borraría automáticamente todas sus APRequests, conversaciones, mensajes y contratos, destruyendo información que afecta a terceras personas.

**Estrategia de anonimización (LOPD/RGPD):**

En caso de baja, no se procede al borrado del registro sino a la **anonimización selectiva** de los datos personales:
1. Se sustituyen `nom`, `cognoms`, `email` por valores genéricos (`'Usuari Eliminat'`, `'anonim@ovi.es'`).
2. Se pone `telefono` a NULL.
3. Se genera una contraseña BCrypt de UUID aleatorio para impedir el re-login.
4. Se establece `estat` a `Rebutjat` para impedir el acceso.
5. Se conservan íntegros los registros de solicitudes, conversaciones y contratos.

Para la baja de un **asistente**, se invoca adicionalmente `candidatPreassignatDao.removeAssistentFromAllSeleccions(dni)` antes de anonimizar, para limpiar las preselecciones activas sin violar integridad referencial.

**Resumen de políticas por clave ajena:**

| Entidad | Clave Ajena | ON UPDATE | ON DELETE | Justificación |
|---|---|---|---|---|
| APRequest | id_usuario → UsuarioOVI | CASCADE | RESTRICT | Preservar historial de solicitudes |
| Conversa | id_request → APRequest | CASCADE | RESTRICT | No perder conversaciones activas |
| Conversa | id_ap → AssistentPersonal | CASCADE | RESTRICT | No perder chats del asistente |
| Missatge | id_conversa → Conversa | CASCADE | RESTRICT | Preservar mensajes |
| RegistreContracte | id_request → APRequest | CASCADE | RESTRICT | El contrato es documento legal |
| RegistreContracte | id_ap → AssistentPersonal | CASCADE | RESTRICT | Preservar vínculo contractual |
| **CandidatPreassignat** | **id_request → APRequest** | **CASCADE** | **CASCADE** | **Las preselecciones se borran con la solicitud** |
| **CandidatPreassignat** | **dni_ap → AssistentPersonal(dni)** | **CASCADE** | **CASCADE** | **Se limpian al dar de baja un AP** |
| ActivitatFormacio | id_formador → Formador | CASCADE | RESTRICT | No perder actividades programadas |
| AssistenciaFormacio | id_actividad → ActivitatFormacio | CASCADE | RESTRICT | Preservar registros de asistencia |
| AssistenciaFormacio | id_usuario → UsuarioOVI | CASCADE | SET NULL | Desvincular sin borrar el registro |
| AssistenciaFormacio | id_ap → AssistentPersonal | CASCADE | SET NULL | Desvincular sin borrar el registro |
| ConversaTecnic | dni_usuario → UsuarioOVI | CASCADE | RESTRICT | No perder canal de soporte |
| MissatgeTecnic | id_conversa_tecnic → ConversaTecnic | CASCADE | RESTRICT | Preservar mensajes de soporte |

**Validaciones de dominio:**
- **Tipos ENUM:** `estat_validacio`, `tipus_ap`, `estat_apr`, `tipus_af`, `tipus_emissor` restringen los valores posibles a nivel de base de datos.
- **Restricción UNIQUE compuesta (Conversa):** `(id_request, id_ap)` impide conversaciones duplicadas.
- **Restricción CHECK (AssistenciaFormacio):** Garantiza que exactamente uno de `id_usuario` o `id_ap` sea NOT NULL.

---

## 2.4. Diseño Físico (SQL)

```sql
-- ==========================================================
-- 1. CREACIÓ DE TIPUS ENUMERATS (Dominis de dades)
-- ==========================================================
CREATE TYPE estat_validacio AS ENUM ('Pendent', 'Acceptat', 'Rebutjat');
CREATE TYPE tipus_ap        AS ENUM ('PAP', 'PATY');
CREATE TYPE estat_apr       AS ENUM ('Revisio', 'Aprovada', 'Rebutjada',
                                     'Tancada_Contracte', 'Finalitzada');
CREATE TYPE tipus_af        AS ENUM ('formacio', 'divulgacio');
CREATE TYPE tipus_emissor   AS ENUM ('Usuari', 'AP');

-- ==========================================================
-- 2. TAULES MESTRES
-- ==========================================================

-- Taula d'Usuaris (Persones usuàries OVI)
CREATE TABLE UsuarioOVI (
    id_usuario          SERIAL PRIMARY KEY,
    dni                 VARCHAR(20)  UNIQUE NOT NULL,
    nom                 VARCHAR(100) NOT NULL,
    cognoms             VARCHAR(100) NOT NULL,
    email               VARCHAR(150) NOT NULL,
    telefono            INT,
    contrasenya         VARCHAR(255) NOT NULL,
    consentimentInformat BOOLEAN DEFAULT FALSE,
    estat               estat_validacio DEFAULT 'Pendent',
    motiu_rebuig        TEXT,
    projecte_vida       TEXT          -- Camp afegit: Projecte de Vida de l'usuari
);

-- Taula d'Assistents Personals (PAP / PATY)
CREATE TABLE AssistentPersonal (
    id_ap                 SERIAL PRIMARY KEY,
    dni                   VARCHAR(20)  UNIQUE NOT NULL,
    nom                   VARCHAR(100) NOT NULL,
    cognoms               VARCHAR(100) NOT NULL,
    email                 VARCHAR(150) NOT NULL,
    telefono              INT,
    contrasenya           VARCHAR(255) NOT NULL,
    tipus                 tipus_ap NOT NULL,
    formacioAcademica     TEXT,
    experienciaPrevia     TEXT,
    proximitatGeografica  TEXT,
    actiu                 BOOLEAN DEFAULT TRUE,
    estat                 estat_validacio DEFAULT 'Pendent',
    motiu_rebuig          TEXT
);

-- Taula de Formadors
CREATE TABLE Formador (
    id_formador  SERIAL PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100)
);

-- ==========================================================
-- 3. TAULES AMB DEPENDÈNCIES
-- ==========================================================

-- Sol·licituds d'Assistència (Creades per l'Usuari)
CREATE TABLE APRequest (
    id_request   SERIAL PRIMARY KEY,
    id_usuario   INT NOT NULL,
    tipusServei  tipus_ap NOT NULL,
    preferencies TEXT,
    estat        estat_apr DEFAULT 'Revisio',
    CONSTRAINT fk_usuario_request FOREIGN KEY (id_usuario)
        REFERENCES UsuarioOVI(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Converses (Iniciades per l'Usuari amb un AP compatible)
CREATE TABLE Conversa (
    id_conversa SERIAL PRIMARY KEY,
    id_request  INT NOT NULL,
    id_ap       INT NOT NULL,
    data_inici  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conversa_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_conversa_ap FOREIGN KEY (id_ap)
        REFERENCES AssistentPersonal(id_ap) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT unq_request_ap UNIQUE (id_request, id_ap)
);

-- Missatges dins d'una Conversa
CREATE TABLE Missatge (
    id_missatge    SERIAL PRIMARY KEY,
    id_conversa    INT NOT NULL,
    emissor        tipus_emissor NOT NULL,
    text_missatge  TEXT NOT NULL,
    data_enviament TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_missatge_conversa FOREIGN KEY (id_conversa)
        REFERENCES Conversa(id_conversa) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Registre de Contractes
CREATE TABLE RegistreContracte (
    id_contracte SERIAL PRIMARY KEY,
    id_request   INT NOT NULL,
    id_ap        INT NOT NULL,
    fecha_Inici  DATE NOT NULL,
    fecha_Fin    DATE,
    pdf_Path     VARCHAR(255),
    CONSTRAINT fk_contracte_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_contracte_ap FOREIGN KEY (id_ap)
        REFERENCES AssistentPersonal(id_ap) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT unq_contracte_request UNIQUE (id_request)
);

-- Activitats de Formació i Divulgació
CREATE TABLE ActivitatFormacio (
    id_actividad SERIAL PRIMARY KEY,
    id_formador  INT,
    titol        VARCHAR(250) NOT NULL,
    fecha        TIMESTAMP,
    tipus        tipus_af,
    CONSTRAINT fk_formador_actividad FOREIGN KEY (id_formador)
        REFERENCES Formador(id_formador) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Assistència a Formació
CREATE TABLE AssistenciaFormacio (
    id_assistencia SERIAL PRIMARY KEY,
    id_actividad   INT NOT NULL,
    id_usuario     INT,
    id_ap          INT,
    assistencia    BOOLEAN DEFAULT FALSE,
    url_certificat VARCHAR(255),
    CONSTRAINT fk_actividad_asist FOREIGN KEY (id_actividad)
        REFERENCES ActivitatFormacio(id_actividad) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_usuario_asist FOREIGN KEY (id_usuario)
        REFERENCES UsuarioOVI(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_ap_asist FOREIGN KEY (id_ap)
        REFERENCES AssistentPersonal(id_ap) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT chk_solo_un_participante CHECK (
        (id_usuario IS NOT NULL AND id_ap IS NULL) OR
        (id_usuario IS NULL  AND id_ap IS NOT NULL)
    )
);

-- ==========================================================
-- 4. TAULES DE MILLORES
-- ==========================================================

-- Conversa Tècnic (canal de suport Usuari ↔ Tècnic)
CREATE TABLE ConversaTecnic (
    id_conversa_tecnic SERIAL PRIMARY KEY,
    dni_usuario        VARCHAR(20) NOT NULL,
    data_inici         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conv_tecnic_usuario FOREIGN KEY (dni_usuario)
        REFERENCES UsuarioOVI(dni) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT unq_conv_tecnic_usuario UNIQUE (dni_usuario)
);

-- Missatges dins de la Conversa Tècnic
CREATE TABLE MissatgeTecnic (
    id_missatge        SERIAL PRIMARY KEY,
    id_conversa_tecnic INT NOT NULL,
    emissor            VARCHAR(10) NOT NULL CHECK (emissor IN ('Usuari', 'Tecnic')),
    text_missatge      TEXT NOT NULL,
    data_enviament     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_msg_conv_tecnic FOREIGN KEY (id_conversa_tecnic)
        REFERENCES ConversaTecnic(id_conversa_tecnic) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Preselecció de Candidats AP pel Tècnic
-- Si no hi ha files per a un id_request, l'usuari veu tots els candidats compatibles.
-- Si n'hi ha, l'usuari només veu els preseleccionats.
CREATE TABLE candidat_preassignat (
    id_request INT NOT NULL,
    dni_ap     VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_request, dni_ap),
    CONSTRAINT fk_candpre_request FOREIGN KEY (id_request)
        REFERENCES APRequest(id_request) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_candpre_ap FOREIGN KEY (dni_ap)
        REFERENCES AssistentPersonal(dni) ON UPDATE CASCADE ON DELETE CASCADE
);
```

---

# 3. Diseño de interfaces de usuario

## 3.1. Proceso de Diseño Centrado en el Usuario

*(El contenido completo de las secciones 3.1.1, 3.1.2, 3.3, 3.4, 3.5 y 3.6 se mantiene íntegramente de la versión anterior del documento. Se reproducen aquí los elementos actualizados o añadidos.)*

El diseño de las interfaces de la plataforma OVI Castelló se ha fundamentado en la metodología de Diseño Centrado en el Usuario (DCU), siguiendo las fases de definición de arquetipos, establecimiento de guía de estilo, prototipado y evaluación iterativa.

### 3.1.1. Arquetipos de Usuario (Personas)

*(Sin cambios respecto a la versión anterior — ver PDF páginas 27-28)*

### 3.1.2. Diseño de Estilo y Contenido

*(Sin cambios respecto a la versión anterior — ver PDF páginas 28-30)*

---

## 3.2. Sitemap y Navegación

El mapa web completo del sistema, organizado por zonas de acceso según el perfil del usuario. Se incluyen todas las rutas implementadas en el código.

**LEYENDA:**
- 🔵 **Azul:** Zona Pública (accesible sin autenticación)
- 🟢 **Verde:** Persona Usuaria OVI (usuario autenticado y aceptado)
- 🟡 **Amarillo:** Asistente Personal — PAP/PATY (asistente autenticado y aceptado)
- 🔴 **Rojo:** Técnico OVI / Administración
- 🟣 **Lila:** Módulo de comunicaciones (transversal)

---

### 1. Zona Pública i Control d'Accés

- 🔵 `/` — Página de inicio pública
- 🔵 `GET /login` — Formulario de inicio de sesión
- 🔵 `POST /login` — Autenticación → redirige según rol y estado
- 🔵 `GET /logout` — Cierre de sesión → redirige a /login
- 🔵 `GET /registro/seleccion` — Selección del tipo de registro
- 🔵 `GET /registro/usuario` — Formulario de registro de Usuario OVI
- 🔵 `POST /registro/usuario/add` — Guardar nuevo usuario
- 🔵 `GET /registro/asistente` — Formulario de registro de Asistente PAP/PATY
- 🔵 `POST /registro/asistente/add` — Guardar nuevo asistente

---

### 2. Àrea de la Persona Usuària OVI

- 🟢 `GET /usuario/dashboard` — Panel principal del usuario
- 🟢 `GET /usuario/espera` — Pantalla de cuenta pendiente de validación
- 🟢 `GET /usuario/rebutjat` — Pantalla con motivo de rechazo
- 🟢 `GET /usuario/perfil` — Ver y editar datos personales
- 🟢 `POST /usuario/perfil/guardar` — Guardar cambios del perfil
- 🟢 `GET /usuario/solicitudes` — Listado de solicitudes de asistencia
  - 🟢 `GET /usuario/solicitudes/nueva` — Formulario de nueva solicitud
  - 🟢 `POST /usuario/solicitudes/add` — Guardar nueva solicitud
  - 🟢 `GET /usuario/solicitudes/{id}/candidatos` — Vista de candidatos (filtrada por preselección del técnico si existe)
- 🟢 `GET /usuario/contractes` — Listado de contratos formalizados
  - 🟢 `GET /usuario/contractes/editar/{id}` — Editar fechas del contrato
  - 🟢 `POST /usuario/contractes/update` — Guardar cambios de fechas
  - 🟢 `POST /usuario/contractes/add` — Formalizar nuevo contrato
  - 🟢 `GET /usuario/contractes/{id}/veure` — Ver detalle de un contrato
- 🟢 `GET /usuario/projecte-vida` — Ver y editar el Projecte de Vida
- 🟢 `POST /usuario/projecte-vida/guardar` — Guardar el Projecte de Vida
- 🟢 `POST /usuario/baja` — Solicitar baja de cuenta (anonimización RGPD)

---

### 3. Àrea del Personal Assistent — PAP/PATY

- 🟡 `GET /asistente/dashboard` — Panel principal del asistente
- 🟡 `GET /asistente/espera` — Pantalla de cuenta pendiente de validación
- 🟡 `GET /asistente/rebutjat` — Pantalla con motivo de rechazo
- 🟡 `GET /asistente/perfil` — Ver y editar perfil profesional
  - 🟡 `POST /asistente/perfil/actualizar` — Guardar datos del perfil
- 🟡 `GET /asistente/contractes` — Listado de contratos asignados
- 🟡 `GET /asistente/formacio` — Panel de actividades formativas
  - 🟡 `POST /asistente/formacio/inscriure` — Inscribirse a una actividad
- 🟡 `GET /asistente/missatges` — Redirige a `/conversa/list`
- 🟡 `GET /asistente/client/projecte-vida?dniUsuari={dni}` — Ver el Projecte de Vida de un cliente *(solo accesible si existe conversación activa entre el AP y el usuario)*

---

### 4. Àrea del Tècnic OVI / Administració

- 🔴 `GET /tecnic/dashboard` — Panel de administración central
- 🔴 `GET /tecnic/usuarios` — Directorio completo de usuarios OVI
  - 🔴 `GET /tecnic/usuarios/{dni}/projecte-vida` — Ver el Projecte de Vida de un usuario
  - 🔴 `GET /tecnic/validar-usuarios` — Listado de usuarios pendientes de validación
  - 🔴 `POST /tecnic/validar-usuario` — Aceptar/Rechazar usuario con motivo
  - 🔴 `GET /tecnic/baixa-usuari/{dni}` — Pantalla de confirmación de baja de usuario
  - 🔴 `POST /tecnic/baixa-usuari/{dni}` — Ejecutar baja y anonimización del usuario
- 🔴 `GET /tecnic/asistentes` — Directorio completo de asistentes PAP/PATY
  - 🔴 `GET /tecnic/validar-asistentes` — Listado de asistentes pendientes de validación
  - 🔴 `POST /tecnic/decidir-asistente` — Aceptar/Rechazar asistente
  - 🔴 `GET /tecnic/baixa-assistent/{dni}` — Pantalla de confirmación de baja de asistente
  - 🔴 `POST /tecnic/baixa-assistent/{dni}` — Ejecutar baja y anonimización del asistente
- 🔴 `GET /tecnic/solicitudes` — Solicitudes pendientes de aprobación (estado `Revisio`)
  - 🔴 `GET /tecnic/confirmar-solicitud/{id}?accio={aprobar|rebutjar}` — Pantalla de confirmación antes de aprobar/rechazar
  - 🔴 `POST /tecnic/aprobar-solicitud` — Ejecutar aprobación/rechazo definitivo
  - 🔴 `GET /tecnic/solicitudes/{id}/candidatos` — Ver y preseleccionar candidatos AP para una solicitud
  - 🔴 `POST /tecnic/solicitudes/{id}/candidatos/guardar` — Guardar la preselección de candidatos del técnico
- 🔴 `GET /tecnic/negociacions` — Listado de solicitudes aprobadas con conversación activa
  - 🔴 `GET /tecnic/negociacions/{idRequest}` — Supervisar conversaciones de una solicitud en curso
- 🔴 `GET /tecnic/todos-contractes` — Visión global de todos los contratos
  - 🔴 `GET /tecnic/todos-contractes/{id}/veure` — Ver detalle de un contrato

---

### 5. Mòdul Transversal de Comunicacions

- 🟣 `GET /conversa/list` — Historial de chats Usuari↔AP (renderizado según rol del visitante)
- 🟣 `POST /conversa/add` — Crear nueva sala de negociación (iniciada por el usuario)
- 🟣 `POST /conversa/enviar` — Enviar mensaje en un chat de negociación
- 🟣 `GET /conversa/tecnic/list` — Chat de soporte con el técnico OVI (usuario ve sus chats; técnico ve todos)
- 🟣 `POST /conversa/tecnic/iniciar` — Iniciar conversación técnica (puede hacerlo tanto el usuario como el técnico)
- 🟣 `POST /conversa/tecnic/enviar` — Enviar mensaje en el canal técnico

---

# 4. Implementación

## 4.1. Descripción de las decisiones

Se ha optado por una arquitectura **MVC (Model-View-Controller)** respaldada por el framework Spring Boot. La estructura lógica sigue un patrón **Service-DAO**, lo que garantiza la separación de responsabilidades: los controladores manejan el flujo HTTP, los servicios orquestan la lógica de negocio y los DAOs encapsulan la interacción con la base de datos.

Para la capa de persistencia se utiliza **Spring JDBC (JdbcTemplate)** descartando el uso de ORMs como JPA/Hibernate. Esta decisión responde a la necesidad de mantener un control granular sobre las consultas SQL y facilitar la integración directa con funciones y tipos nativos de PostgreSQL (enumerados personalizados, CASTing explícito). Si bien supone escribir más código repetitivo, resulta en una arquitectura más predecible para la complejidad del proyecto.

La autenticación y autorización se gestionan con **HttpSession**. Los roles persisten como atributos de sesión (`usuarioLogueado`, `asistenteLogueado`, `tecnicLogueado`). El rol administrativo (Técnico OVI) se ha programado con credenciales hardcoded (`admin`/`admin`) en `MainController`, separando la autenticación del técnico de la de usuarios y asistentes que sí se validan contra la base de datos con BCrypt.

**El proceso de propuesta de candidatos compatibles** (`/tecnic/solicitudes/{id}/candidatos` y `/usuario/solicitudes/{id}/candidatos`) se fundamenta en un filtrado multicriterio en `AsistenteDaoImpl.getCandidatosAdecuados()`: filtra por `actiu = true` y `estat = 'Acceptat'`, restringe por `tipus_ap` (PAP/PATY según la solicitud) y ordena dinámicamente por relevancia geográfica y formativa usando las preferencias del usuario. Sobre esta lista, el técnico puede guardar una **preselección** en la tabla `candidat_preassignat`; cuando el usuario accede a la vista de candidatos, el sistema consulta si existe preselección y, si es así, filtra la lista a solo los preseleccionados.

**El Projecte de Vida** es un campo de texto libre (`projecte_vida` en `UsuarioOVI`) editable únicamente por el propio usuario. El acceso en lectura está restringido por rol: el técnico accede desde el directorio de usuarios (`/tecnic/usuarios/{dni}/projecte-vida`), y un asistente solo puede acceder al Projecte de Vida de un cliente concreto si existe una conversación activa entre ambos (verificado con `conversaDao.existeixConversaEntreApIUsuari()`), garantizando la privacidad del documento.

**La funcionalidad de Baixa** aplica una política RGPD coherente: antes de anonimizar, el sistema verifica que no haya contrato activo en vigor (bloquea la baja si lo hay) y cierra todas las solicitudes activas (`Revisio` o `Aprovada`) marcándolas como `Rebutjada`. Para la baja de un asistente, se eliminan previamente sus preselecciones de `candidat_preassignat`. La contraseña se reemplaza por el hash BCrypt de un UUID aleatorio, imposibilitando cualquier re-login con la cuenta anonimizada.

**La gestión de sesión expirada** se implementa mediante `SessionExpirationInterceptor`, un `HandlerInterceptor` que se evalúa en el preHandle de cada petición. Si el path es protegido (`/usuario`, `/tecnic`, `/asistente`, `/conversa`) y el ID de sesión es conocido pero ya no válido en el servidor, redirige automáticamente a `/login?expirat`.

## 4.2. Control de errores desarrollados

El aseguramiento del flujo y la prevención de fallos se aborda en seis niveles.

### 4.2.1. Validación frontend

Las plantillas Thymeleaf incorporan validación nativa HTML5:
- `required`: impide el envío con campos obligatorios vacíos.
- `pattern`: expresiones regulares para DNI (`[0-9]{8}[A-Za-z]{1}`) y teléfono (`[0-9]{9}`).
- `minlength`/`maxlength`: restricciones de longitud.
- Tipos semánticos: `type="email"`, `type="tel"`, `type="password"`.

### 4.2.2. Validación en el servidor

Se implementan validadores Spring (`Validator` + `BindingResult`) para garantizar que ningún dato inválido alcance la base de datos independientemente de las comprobaciones del cliente:

| Campo | Validación servidor | Mensaje de error |
|---|---|---|
| DNI | Formato `[0-9]{8}[A-Z]` + unicidad en BD | "El format del DNI no és vàlid" / "Aquest DNI ja està registrat" |
| Email | Formato RFC 5322 + unicidad | "El correu electrònic no és vàlid" / "Aquest email ja està en ús" |
| Contrasenya | Longitud mínima 6 caracteres | "La contrasenya ha de tindre almenys 6 caràcters" |
| Nom / Cognoms | No vacío | "El nom no pot estar buit" |
| Teléfono | 9 dígitos numéricos | "El telèfon ha de tindre 9 dígits" |
| ConsentimentInformat | Debe ser `true` en registro | "Has d'acceptar el consentiment informat" |
| Preferències (APRequest) | No vacío | "Has d'indicar les teues preferències" |

### 4.2.3. Confirmaciones de acciones importantes

Las acciones irreversibles requieren confirmación explícita:

| Acción | Tipo de confirmación | Información mostrada |
|---|---|---|
| Aceptar/Rechazar usuario | Modal + motivo obligatorio si rechaza | DNI, nombre, motivo |
| Aprobar/Rechazar solicitud | Página intermedia (`/tecnic/confirmar-solicitud/{id}`) + modal | Nombre usuario, tipo servicio, preferencias, consecuencia de la acción |
| Dar de baja usuario (técnico) | Página de confirmación (`/tecnic/baixa-usuari/{dni}`) | Nombre, contratos activos, solicitudes activas |
| Dar de baja asistente (técnico) | Página de confirmación (`/tecnic/baixa-assistent/{dni}`) | Nombre, contratos activos |
| Solicitar baja propia (usuario) | Bloqueo si hay contrato activo; mensaje de error con flash attribute | — |
| Iniciar conversación con candidato | Modal de confirmación | Nombre del asistente, zona geográfica |

### 4.2.4. Control de acceso y sesión

- **Restricciones de acceso:** Todas las rutas protegidas verifican la existencia del atributo de sesión correspondiente. Si no está autenticado, se redirige a `/login`.
- **Aislamiento por estados:** Si la cuenta está en estado `Pendent` o `Rebutjat`, se redirige a pantallas informativas específicas, impidiendo el acceso al dashboard.
- **Verificación de propiedad (ownership):** Antes de actualizar o acceder a un recurso (contrato, conversación, solicitud), los controladores verifican que el recurso pertenece al usuario en sesión. Por ejemplo, `UsuarioController` verifica `registreContracteUsuarioDao.getContractesByUsuario(dni).stream().anyMatch(c -> c.getId() == id)` antes de editar un contrato, previniendo accesos a recursos de otros usuarios.
- **Acceso al Projecte de Vida del AP:** `AsistenteController` verifica `conversaDao.existeixConversaEntreApIUsuari(dniAp, dniUsuari)` antes de mostrar el Projecte de Vida de un cliente al asistente.

### 4.2.5. Feedback contextual por tipo de usuario

En lugar de mostrar excepciones técnicas, los controladores emplean `RedirectAttributes` con flash attributes que disparan alertas contextuales:

- **Alertas de éxito (`mensajeExito`):** Registro exitoso, solicitud enviada, perfil actualizado, Projecte de Vida guardado.
- **Alertas de error (`errorBaixa`):** Intento de baja con contrato activo.
- **Mensajes de envío (`mensajeEnvio`):** Solicitud enviada correctamente.

El feedback se adapta al tono de cada rol: cercano para usuarios OVI, profesional para asistentes, informativo con datos concretos para el técnico.

### 4.2.6. Navegación y control del usuario

- **Botones de retroceso:** Todas las vistas incluyen `backUrl` parametrizado para regresar a la pantalla correcta según el rol que accede (ej.: el técnico y el asistente ven el Projecte de Vida en modo lectura con `backUrl` distinto según su origen).
- **Botón "Cancel·lar" en formularios:** Todos los formularios de creación incluyen cancelación que redirige al dashboard.
- **Cierre de sesión visible:** El botón "Tancar Sessió" está siempre visible en la barra de navegación.

### 4.2.7. Interceptor de caducidad de sesión

`SessionExpirationInterceptor` implementa `HandlerInterceptor` y se registra en `WebConfig` (clase `@Configuration` que implementa `WebMvcConfigurer`). En cada petición a rutas protegidas (`/usuario/**`, `/tecnic/**`, `/asistente/**`, `/conversa/**`), verifica si el ID de sesión del cliente es conocido pero ya expiró en el servidor. En ese caso, redirige a `/login?expirat` antes de ejecutar el controlador, evitando NullPointerExceptions por acceso a atributos de sesión inexistentes.

```java
// Lògica de SessionExpirationInterceptor.preHandle():
if (isProtected && sessionId != null && !request.isRequestedSessionIdValid()) {
    response.sendRedirect(request.getContextPath() + "/login?expirat");
    return false;
}
```

### 4.2.8. Gestión global de excepciones

`OviControllerAdvice` (`@ControllerAdvice`) intercepta las excepciones no capturadas en los controladores:
- Si es `OviException` (excepción de dominio personalizada con `errorName` y `message`), muestra la vista `error` con los atributos específicos.
- Si es cualquier otra `Exception`, muestra la vista `error` con un mensaje genérico "Error inesperat".

Esto garantiza que nunca se muestren stack traces al usuario final.

---

## 4.3. Lista de paquetes y clases

### `es.uji.ei1027.ovi` *(paquete raíz)*

- **`OviApplication.java`** — Clase principal para el arranque de Spring Boot (`@SpringBootApplication`).
- **`OviConfiguration.java`** — Clase de configuración Spring para beans de infraestructura (DataSource, etc.).
- **`WebConfig.java`** — Configuración Spring MVC (`@Configuration`, implementa `WebMvcConfigurer`). Registra el `SessionExpirationInterceptor` mediante `addInterceptors()`.

---

### `es.uji.ei1027.ovi.controller` *(capa de presentación y manejo de endpoints)*

- **`MainController.java`** — Gestión del portal público (`/`), login universal (`GET/POST /login`) y cierre de sesión (`GET /logout`). Incluye lógica de autenticación con BCrypt y redirección según estado del usuario.
- **`RegistroController.java`** — Despliegue y recogida de datos de los formularios de inscripción de usuarios (`/registro/usuario`) y asistentes (`/registro/asistente`). Invoca `UsuarioValidator` y `AsistenteValidator`.
- **`UsuarioController.java`** — Panel completo de la persona usuaria OVI: perfil, solicitudes, candidatos, contratos, Projecte de Vida y baja de cuenta.
- **`AsistenteController.java`** — Panel del asistente: perfil, contratos, formación, mensajes y acceso al Projecte de Vida de clientes con conversación activa.
- **`TecnicController.java`** — Operaciones exclusivas del técnico: validación de altas, gestión de solicitudes (con flujo de confirmación en dos pasos), preselección de candidatos (`CandidatPreassignatDao`), supervisión de negociaciones, consulta global de contratos, y gestión de bajas de usuarios y asistentes con anonimización RGPD.
- **`ConversaController.java`** — Motor central de rutas para el sistema de mensajería: maneja tanto el canal Usuari↔AP (`/conversa/*`) como el canal Usuari↔Tècnic (`/conversa/tecnic/*`). Verifica propiedad de conversaciones antes de permitir envíos. Invoca `NotificationService` tras cada envío de mensaje.
- **`SessionExpirationInterceptor.java`** — `HandlerInterceptor` que intercepta todas las peticiones a rutas protegidas para detectar sesiones expiradas y redirigir a `/login?expirat`.
- **`OviControllerAdvice.java`** — `@ControllerAdvice` para gestión global de excepciones: captura `OviException` y `Exception` genérica, redirige a la vista `error` con mensaje amigable.
- **`OviException.java`** — Excepción de dominio personalizada con campos `errorName` y `message`, lanzada por los controladores ante errores de negocio.
- **`UsuarioValidator.java`** — Implementa `Validator` de Spring para validar formularios de registro y edición de `UsuarioOVI` (DNI, email, contraseña, nombre, teléfono, consentimiento).
- **`AsistenteValidator.java`** — Implementa `Validator` de Spring para validar formularios de registro de `AssistentPersonal`.
- **`APRequestValidator.java`** — Implementa `Validator` de Spring para validar formularios de nueva solicitud (`APRequest`), garantizando que el campo `preferencies` no esté vacío.

---

### `es.uji.ei1027.ovi.service` *(lógica orquestada entre múltiples repositorios)*

- **`OviService.java`** — Interfaz del servicio principal: define operaciones de autenticación (`loginUsuario`, `loginAsistente`), registro, actualización y consulta de solicitudes y contratos.
- **`OviServiceImpl.java`** — Implementación del servicio principal. Gestiona la autenticación BCrypt con fallback para contraseñas en texto plano de prueba.
- **`NotificationService.java`** — Servicio de notificación que simula la emisión de alertas en tiempo real (`notifyUser()`). Invocado por `ConversaController` después de cada envío de mensaje en ambos canales de chat.

---

### `es.uji.ei1027.ovi.dao` *(repositorios de persistencia JDBC)*

- **`UsuarioDao.java` / `UsuarioDaoImpl.java`** — Acceso a credenciales e información de los clientes OVI. Incluye `updateProjecteVida(dni, contingut)` para actualizar el Projecte de Vida y `anonimizarUsuario(dni)` para la baja RGPD.
- **`AsistenteDao.java` / `AsistenteDaoImpl.java`** — Extracción de perfiles PAP/PATY, filtrado multicriterio de candidatos compatibles (`getCandidatosAdecuados()`), actualización de estados y `anonimizarAsistente(dni)` para la baja RGPD.
- **`APRequestDao.java` / `APRequestDaoImpl.java`** — Modificación de estados y lectura de solicitudes. Incluye `getRequestsEnNegociacio()` (solicitudes aprobadas con conversación activa), `teRequestsActives()` (verificación previa a la baja) y `tancarRequestsActivesPerUsuari()` (cierre masivo durante la baja).
- **`CandidatPreassignatDao.java` / `CandidatPreassignatDaoImpl.java`** — **DAO para la preselección de candidatos AP.** Operaciones:
  - `saveSeleccio(idRequest, dniAps)`: elimina las preselecciones actuales e inserta las nuevas (patrón delete-and-reinsert dentro de la misma operación).
  - `getDniApsSeleccionats(idRequest)`: devuelve la lista de DNIs preseleccionados para una solicitud.
  - `removeAssistentFromAllSeleccions(dniAp)`: elimina al asistente de todas las preselecciones activas (invocado durante la baja del asistente).
- **`ConversaDao.java` / `ConversaDaoImpl.java`** — Rastreo e inicio de salas de negociación. Incluye `existeixConversaEntreApIUsuari(dniAp, dniUsuari)` para el control de acceso al Projecte de Vida.
- **`ConversaTecnicDao.java` / `ConversaTecnicDaoImpl.java`** — Operaciones del canal de soporte Usuari↔Tècnic.
- **`MissatgeDao.java` / `MissatgeDaoImpl.java`** — Volcado y recuperación de mensajes de negociación.
- **`MissatgeTecnicDao.java` / `MissatgeTecnicDaoImpl.java`** — Persistencia de mensajes en el canal técnico.
- **`RegistreContracteUsuarioDao.java` / `RegistreContracteUsuarioDaoImpl.java`** — Persistencia y lectura de contratos desde la perspectiva del usuario. El método `addContracte()` usa `@Transactional` para insertar el contrato y actualizar el estado de la APRequest en una sola transacción.
- **`RegistreContracteAsistenteDao.java` / `RegistreContracteAsistenteDaoImpl.java`** — Consultas de visualización de contratos desde la perspectiva del asistente. Incluye `teContracteActiu(dniAp)` para verificación previa a la baja.
- **`TecnicDao.java` / `TecnicDaoImpl.java`** — Herramientas específicas de consulta administrativa: listado de usuarios pendientes y actualización de estado con motivo.
- **`ActivitatFormacioDao.java` / `ActivitatFormacioDaoImpl.java`** — Acceso a la oferta de cursos e inserción de inscripciones.
- **`UsuarioOVIRowMapper.java`** — Implementa `RowMapper<UsuarioOVI>` para mapear los resultados JDBC de la tabla `UsuarioOVI`, incluyendo el campo `projecte_vida`.
- **`AssistentPersonalRowMapper.java`** — Implementa `RowMapper<AssistentPersonal>` para mapear los resultados JDBC de la tabla `AssistentPersonal`.

---

### `es.uji.ei1027.ovi.model` *(entidades de datos — POJOs sin lógica operativa)*

- **`UsuarioOVI.java`** — Perfil y credenciales del usuario con diversidad funcional. Incluye el campo `projecteVida` con getters/setters.
- **`AssistentPersonal.java`** — Entidad del profesional PAP/PATY.
- **`APRequest.java`** — Contenedor de la demanda de asistencia personal.
- **`Conversa.java`** — Contenedor de un hilo de negociación entre usuario y asistente; incluye campos transitorios `nomAp`, `dniUsuari`, `apActiu`, `usuariActiu` y la lista de mensajes (cargada por el DAO).
- **`Missatge.java`** — Instancia de un mensaje de negociación.
- **`ConversaTecnic.java`** — Entidad del canal de soporte técnico.
- **`MissatgeTecnic.java`** — Mensaje dentro del canal técnico.
- **`RegistreContracteUsuarioOvi.java`** — Vista consolidada del contrato para el usuario (join con AP y solicitud).
- **`RegistreContracteAsistente.java`** — Vista del contrato para el asistente.
- **`ActivitatFormacio.java`** — Estructura base de cursos y actividades.
- **`AssistenciaFormacio.java`** — Relación de presencia o inscripción a una formación.
- **`Formador.java`** — Representación básica del perfil docente.

---

# 5. Mejoras implementadas

Las mejoras opcionales implementadas se han centrado en cuatro funcionalidades que extienden de forma significativa el sistema base:

### 5.1. Chat Técnico (ConversaTecnic / MissatgeTecnic)

Canal de comunicación persistente e independiente entre el UsuarioOVI y el técnico de la plataforma. Cubre la necesidad de soporte directo sin interferir en el flujo de negociación de Asistencia Personal.

**Componentes:** tablas `ConversaTecnic` y `MissatgeTecnic`, DAOs correspondientes, rutas `/conversa/tecnic/*`, plantillas `tecnic/list_comunicacions.html` y `usuario/list_comunicacions_tecnic.html`.

**Características:** tanto el técnico como el usuario pueden iniciar la conversación; el técnico ve todas las conversaciones técnicas activas en una única pantalla; se garantiza una sola conversación por usuario (constraint UNIQUE en `dni_usuario`).

### 5.2. Projecte de Vida

Documento personal libre que cada usuario OVI puede redactar, editar y guardar en la plataforma. Refleja sus objetivos vitales, aspiraciones y necesidades de vida independiente.

**Componentes:** campo `projecte_vida TEXT` en la tabla `UsuarioOVI`, método `updateProjecteVida()` en `UsuarioDaoImpl`, rutas `GET/POST /usuario/projecte-vida`, `GET /asistente/client/projecte-vida` y `GET /tecnic/usuarios/{dni}/projecte-vida`, plantillas `usuario/projecte_vida.html` y `usuario/projecte_vida_readonly.html`.

**Control de acceso diferenciado:**
- El usuario puede ver y editar su propio Projecte de Vida.
- Un asistente solo puede verlo (en modo lectura) si existe una conversación activa con ese usuario, garantizando privacidad.
- El técnico puede verlo (en modo lectura) desde el directorio de usuarios.

### 5.3. Baixa de Compte amb Anonimització RGPD

Sistema de gestión de bajas conforme al Reglamento General de Protección de Datos, implementado tanto para la autocancelación del usuario como para la baja administrativa por el técnico.

**Componentes:** `POST /usuario/baja`, `GET/POST /tecnic/baixa-usuari/{dni}`, `GET/POST /tecnic/baixa-assistent/{dni}`, métodos `anonimizarUsuario()` en `UsuarioDaoImpl` y `anonimizarAsistente()` en `AsistenteDaoImpl`, plantillas `tecnic/confirmar_baixa_usuari.html` y `tecnic/confirmar_baixa_assistent.html`.

**Flujo de baja:**
1. Verificación de que no hay contrato activo (bloquea la baja si lo hay).
2. Para bajas de asistente: limpieza de preselecciones en `candidat_preassignat`.
3. Cierre de todas las solicitudes activas del usuario (`tancarRequestsActivesPerUsuari()`).
4. Anonimización: `nom='Usuari Eliminat'`, `cognoms=''`, `email='anonim@ovi.es'`, `telefono=NULL`, contraseña reemplazada por BCrypt de UUID aleatorio, `estat='Rebutjat'`.
5. Invalidación de la sesión si es autocancelación.

### 5.4. Preselecció de Candidats AP (CandidatPreassignat)

Sistema que permite al técnico OVI indicar qué asistentes son candidatos recomendados para una solicitud aprobada concreta, antes de que el usuario acceda a la vista de candidatos.

**Componentes:** tabla `candidat_preassignat`, `CandidatPreassignatDao` / `CandidatPreassignatDaoImpl`, rutas `GET /tecnic/solicitudes/{id}/candidatos` y `POST /tecnic/solicitudes/{id}/candidatos/guardar`.

**Lógica de filtrado:**
- Si el técnico no ha guardado ninguna preselección para la solicitud → el usuario ve todos los candidatos compatibles ordenados por relevancia.
- Si el técnico ha guardado una preselección → el usuario solo ve los candidatos preseleccionados.
- En la vista del técnico, los APs preseleccionados aparecen ordenados al principio de la lista, diferenciados visualmente.

---

# 6. Conclusiones

La conclusión principal de este proyecto reside en la consolidación de habilidades profesionales de desarrollo web mediante la implementación rigurosa del patrón MVC complementado con el enfoque Service-DAO. Esta arquitectura garantiza la separación de responsabilidades, la escalabilidad y la mantenibilidad del sistema.

La realización del proyecto supuso un significativo salto cualitativo en el dominio de tecnologías de backend como Spring Boot y Thymeleaf, integrado con la aplicación práctica de diseño de bases de datos relacionales con integridad referencial y control de versiones con GitHub.

El proceso de Diseño Centrado en el Usuario ha sido especialmente enriquecedor, permitiendo comprender la importancia de diseñar interfaces accesibles y usables para personas con diversidad funcional.

Los comentarios recibidos durante las tutorías resultaron fundamentales para mejorar aspectos críticos: la política de integridad referencial (sustitución de ON DELETE CASCADE por RESTRICT), la necesidad de validaciones obligatorias en el servidor, y la implementación de un sistema de confirmación en dos pasos para acciones administrativas irreversibles.

**Trabajo futuro:**

- Sustituir la autenticación manual HttpSession por Spring Security para una gestión de roles más robusta.
- Implementar notificaciones en tiempo real mediante WebSockets, eliminando la necesidad de consultar manualmente la aplicación.
- Completar el módulo de formación con el área de autenticación propia del Formador/Instructor para el registro directo de asistencia.
- Adaptación completa de las interfaces para pantallas pequeñas (dispositivo móvil).

---

# 7. Bibliografía y Referencias Técnicas

**Documentación de Tecnologías y Frameworks:**

- Spring Boot: VMware Tanzu. (2024). *Spring Boot Reference Documentation*. https://docs.spring.io/spring-boot/docs/current/reference/html/
- Thymeleaf: Thymeleaf Team. (2024). *Tutorial: Using Thymeleaf*. https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html
- Spring JDBC: Spring Framework. (2024). *Data Access with JDBC*. https://docs.spring.io/spring-framework/reference/data-access/jdbc.html
- PostgreSQL: The PostgreSQL Global Development Group. (2024). *PostgreSQL 16.0 Documentation*. https://www.postgresql.org/docs/16/index.html
- Bootstrap: Twitter Inc. (2024). *Bootstrap v5.3 Documentation*. https://getbootstrap.com/docs/5.3/getting-started/introduction/

**Herramientas de Desarrollo y Calidad:**

- GitHub: GitHub, Inc. (2024). *GitHub Docs: Collaborative Coding*. https://docs.github.com/es
- WAVE: WebAIM. (2024). *WAVE Web Accessibility Evaluation Tool*. Utah State University. https://wave.webaim.org/

**Contexto del Dominio (Vida Independiente):**

- Foro de Vida Independiente y Divertad. (2024). *Diccionario de conceptos de Vida Independiente*. http://forovidaindependiente.org/
- Ministerio de Derechos Sociales y Agenda 2030. (2022). *Guía de la asistencia personal para la autonomía en la comunidad*.

**Guías Académicas:**

- UJI: Departamento de Ingeniería y Ciencia de los Computadores. (2026). *Guía de la segunda tutoría obligatoria — EI1027*. Universitat Jaume I.

---

*Documento generado como complemento y actualización de la memoria original EI1027 — OVI System.*
*Las secciones de diseño gráfico (wireframes, capturas de pantalla, diagramas UML, informe WAVE y evaluaciones heurísticas) se mantienen tal como aparecen en la versión PDF del documento.*
