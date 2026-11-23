# Producto ATM — Simulador de Cajero Automático y Microservicio Financiero

![Java](https://img.shields.io/badge/Java-8-orange?style=flat-square&logo=java)
![Architecture](https://img.shields.io/badge/Architecture-Microservices%20%2F%20REST-blue?style=flat-square)
![Database](https://img.shields.io/badge/Database-H2%20In--Memory-lightgrey?style=flat-square)
![Domain](https://img.shields.io/badge/Domain-Banking%20%26%20Payments-green?style=flat-square)

Solución integral desarrollada en **Java 8** que simula la operativa transaccional de un cajero automático (ATM) mediante una arquitectura desacoplada compuesta por un **microservicio backend** (exposición de Web Services) y una **aplicación cliente de escritorio en modo consola** (CLI).

---

## 📋 Tabla de Contenidos

1. [Descripción y Arquitectura](#-descripción-y-arquitectura)
2. [Funcionalidades Implementadas](#-funcionalidades-implementadas)
3. [Modelo de Datos](#-modelo-de-datos)
4. [Requisitos Previos e Instalación](#-requisitos-previos-e-instalación)
5. [Guía de Ejecución y Pruebas](#-guía-de-ejecución-y-pruebas)
6. [Decisiones de Diseño y Requisitos No Funcionales](#-decisiones-de-diseño-y-requisitos-no-funcionales)
7. [Concesiones por Limitación de Tiempo (Scope 72h)](#-concesiones-por-limitación-de-tiempo-scope-72h)
8. [Autor](#-autor)

---

## 🏗 Descripción y Arquitectura

El sistema está diseñado bajo un enfoque orientado a servicios para separar la interfaz operativa del terminal ATM respecto a la lógica de negocio y persistencia financiera:

* **Microservicio ATM (Backend):** Expone los servicios web encargados de validar tarjetas, gestionar saldos, ejecutar transacciones monetarias y mantener la trazabilidad de auditoría sobre una base de datos en memoria (**H2**).
* **Cliente ATM (Aplicación de Escritorio / CLI):** Aplicación ligera ejecutada por línea de comandos que recibe las instrucciones del usuario, valida parámetros de entrada, consume los endpoints del microservicio mediante HTTP/REST y devuelve la respuesta operativa en consola.

```text
+-----------------------+       HTTP / REST (JSON)       +--------------------------+       JDBC / JPA       +------------------+
|  Cliente ATM (CLI)    | -----------------------------> |   Microservicio ATM      | ---------------------> |  Base de Datos   |
|  (Java 8 Console App) | <----------------------------- |   (Web Services Java 8)  | <--------------------- |  H2 (In-Memory)  |
+-----------------------+                                +--------------------------+                        +------------------+
```

---

## ⚙️ Funcionalidades Implementadas

El sistema soporta las cuatro operaciones core de un cajero automático:

1. **Autenticación (`login`):** Verifica la existencia del número de tarjeta de débito en el sistema y comprueba que su estado sea activo (`Activa = S`).
2. **Extracción de efectivo (`extraer`):** Valida la relación tarjeta-cuenta, verifica fondos suficientes (garantizando que el saldo nunca sea negativo) y debita el importe solicitado.
3. **Depósito en efectivo (`depositar`):** Acredita de forma online el importe indicado sobre la cuenta asociada al **CBU** destino (permitiendo depósitos tanto en cuentas propias como de terceros).
4. **Consulta de saldo (`saldo`):** Recupera y formatea el saldo disponible actualizado de la cuenta vinculada a la tarjeta.

---

## 🗄 Modelo de Datos

La persistencia en memoria se articula sobre tres entidades principales y una entidad de trazabilidad:

* **`Tarjeta`**: `Numero` (PK), `Activa` (`S` / `N`).
* **`Cuenta`**: `Numero` (PK), `CBU` (Identificador único interbancario), `Saldo`.
* **`TarjetaCuenta`**: Entidad asociativa (`Tarjeta`, `Cuenta`) que permite vincular una tarjeta con múltiples cuentas operativas.
* **`AuditoriaTransaccion`** *(Trazabilidad)*: Registro histórico de cada operación solicitada (fecha/hora, operación, tarjeta, cuenta/CBU, importe, estado resultante y saldo anterior/posterior).

---

## 🚀 Requisitos Previos e Instalación

* **JDK 8** (`java -version` -> `1.8.x`)
* **Apache Maven** (o el wrapper incluido en el proyecto)
* **Git**

### Clonado y compilación

```bash
# Clonar el repositorio
git clone https://github.com/oriolbg/proyectoatm.git
cd proyectoatm

# Compilar y empaquetar el proyecto
mvn clean package
```

---

## 💻 Guía de Ejecución y Pruebas

### 1. Levantar el Microservicio Backend
Ejecuta el artefacto `.jar` correspondiente al microservicio. Al iniciar, cargará automáticamente el esquema y los datos iniciales de prueba en la base de datos H2:

```bash
java -jar atm-microservice/target/atm-microservice-1.0.0.jar
```

### 2. Ejecutar operaciones desde el Cliente ATM (Consola)
En una nueva terminal, ejecuta el cliente de escritorio pasando como argumentos la operación y sus parámetros:

* **Identificarse (Login):**
  ```bash
  java -jar atm-client/target/atm-client-1.0.0.jar login <tarjeta>
  # Salida esperada: "Ingreso exitoso" o "Ingreso no exitoso"
  ```

* **Consultar saldo:**
  ```bash
  java -jar atm-client/target/atm-client-1.0.0.jar saldo <tarjeta> <cuenta>
  # Salida esperada: "Su saldo es $ 99999,99" o mensaje de error
  ```

* **Extraer efectivo:**
  ```bash
  java -jar atm-client/target/atm-client-1.0.0.jar extraer <tarjeta> <cuenta> <importe>
  # Salida esperada: "Retire su dinero" o mensaje indicando error (ej. saldo insuficiente)
  ```

* **Depositar efectivo:**
  ```bash
  java -jar atm-client/target/atm-client-1.0.0.jar depositar <tarjeta> <cbu> <importe>
  # Salida esperada: "Depósito exitoso" o mensaje indicando error (ej. CBU inexistente)
  ```

---

## 🛡 Decisiones de Diseño y Requisitos No Funcionales

Para abordar el proyecto con criterios de entorno productivo financiero:

* **Atomicidad e Integridad Transaccional (`ACID`):** Todas las operaciones de débito y crédito se ejecutan dentro de contextos transaccionales controlados para evitar inconsistencias de saldo o condiciones de carrera.
* **Validación Defensiva en Doble Capa:** Tanto el cliente CLI (formato de parámetros, importes positivos) como el microservicio (estado de tarjeta, titularidad de cuenta, existencia de CBU y saldo mínimo `>= 0`) validan cada petición antes de mutar el estado.
* **Trazabilidad y Auditoría Completa:** Dado que el sistema maneja transacciones monetarias críticas, cada petición genera un registro de log estructurado y persistencia de auditoría con sello temporal, identificadores de origen/destino y resultado de la operación, permitiendo reconstruir el historial completo de saldos.
* **Desacoplamiento y Disponibilidad:** La separación entre cliente y servidor mediante contratos REST estándar permite escalar el backend de forma independiente o conectar múltiples canales (ATM físico, banca móvil, web) sobre los mismos servicios.

---

## ⏱ Concesiones por Limitación de Tiempo (Scope 72h)

En cumplimiento con el alcance de desarrollo establecido (entrega acotada a 72 horas), se priorizó la robustez del núcleo transaccional frente a componentes periféricos:

1. **Autenticación simplificada:** Se implementó la validación por número de tarjeta y estado activo según especificación funcional, omitiendo el flujo completo de validación de PIN cifrado y gestión de tokens JWT/OAuth2.
2. **Base de datos en memoria (H2):** Se utilizó H2 con precarga de datos para facilitar la portabilidad y ejecución inmediata sin dependencias de infraestructura externa, dejando el acceso a datos desacoplado para migrar fácilmente a motores productivos (Oracle, PostgreSQL, SQL Server).
3. **Resiliencia distribuida:** En un entorno productivo real se incorporarían patrones como *Circuit Breaker*, colas de mensajería asíncrona para conciliación y mecanismos de idempotencia mediante claves de transacción únicas (`idempotency-key`) ante reintentos de red.

---

## 👤 Autor

**Josep Oriol Borras Garcia** — *Senior Java Backend Developer | Retail & Payment Systems Consultant*
* GitHub: [@oriolbg](https://github.com/oriolbg)
* LinkedIn: [linkedin.com/in/oriolborras](https://www.linkedin.com/in/oriolborras/)