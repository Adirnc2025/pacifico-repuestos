# Informe Final del Proyecto — Pacífico Repuestos

**Proyecto:** Sistema Web de Gestión y Comercialización de Repuestos Automotrices  
**Metodología:** Scrum  
**Flujo de trabajo:** ADIPD (Análisis → Diseño → Implementación → Pruebas → Despliegue)  
**Herramienta de apoyo:** IA (Claude — Anthropic)  
**Duración:** 8 semanas / 4 Sprints  

---

## 1. Resumen Ejecutivo

Se desarrolló exitosamente el sistema web **Pacífico Repuestos**, una plataforma de comercialización de repuestos automotrices para una tienda ubicada en Ayacucho, Perú.

El sistema permite a los clientes buscar repuestos por compatibilidad de vehículo, agregar productos al carrito y realizar pedidos con delivery local e interprovincial. Los administradores gestionan el catálogo, inventario, pedidos y reportes desde un panel dedicado.

---

## 2. Cumplimiento del Flujo ADIPD

| Etapa | Documentos generados | Estado |
|-------|---------------------|--------|
| **A — Análisis** | requerimientos.md, historias-de-usuario.md, product-backlog-sprints.md, casos-de-uso.md | ✅ Completo |
| **D — Diseño** | arquitectura.md, ESPECIFICACION.md, 01_ddl_create.sql, 02_seed_data.sql | ✅ Completo |
| **I — Implementación** | Código backend (Spring Boot) + frontend (React) en 4 sprints | ✅ Completo |
| **P — Pruebas** | AuthServiceTest, ProductoServiceTest, PedidoServiceTest, CatalogoServiceTest, ReporteServiceTest | ✅ Completo |
| **D — Despliegue** | guia-despliegue.md, Vercel + Render + Railway | ✅ Completo |

---

## 3. Metodología Scrum — Evidencia

### Sprints ejecutados

| Sprint | Objetivo | Puntos | Semanas | Estado |
|--------|----------|--------|---------|--------|
| Sprint 1 | Autenticación + estructura base | 17 pts | 1-2 | ✅ |
| Sprint 2 | Catálogo + búsqueda + admin productos | 30 pts | 3-4 | ✅ |
| Sprint 3 | Carrito + pedidos + gestión operativa | 19 pts | 5-6 | ✅ |
| Sprint 4 | Reportes + despliegue + cierre | 21 pts | 7-8 | ✅ |
| **Total** | | **87 pts** | **8 sem** | ✅ |

### Ceremonias Scrum realizadas
- Sprint Planning al inicio de cada sprint
- Daily Standup durante el desarrollo
- Sprint Review al finalizar cada sprint
- Sprint Retrospective para mejora continua

---

## 4. Requerimientos Cumplidos

### Funcionales
| Módulo | RF cubiertos | Total |
|--------|-------------|-------|
| Autenticación | RF01–RF06 | 6 |
| Catálogo automotriz | RF07–RF14 | 8 |
| Productos | RF15–RF24 | 10 |
| Carrito y pedidos | RF25–RF31 | 7 |
| Delivery | RF32–RF35 | 4 |
| Contacto | RF36–RF37 | 2 |
| Reportes | RF38–RF40 | 3 |
| **Total** | | **40 RF** |

### No Funcionales
- ✅ RNF03/04 — Seguridad con BCrypt + JWT
- ✅ RNF07 — Diseño responsive con Tailwind CSS
- ✅ RNF14 — Cobertura de pruebas ≥ 90% (JaCoCo)
- ✅ RNF15 — Arquitectura MVC por capas
- ✅ RNF16 — Código versionado en GitHub

---

## 5. Pruebas Unitarias — Cobertura

| Clase de prueba | Casos de prueba | Cobertura objetivo |
|-----------------|----------------|-------------------|
| AuthServiceTest | 6 casos | ≥ 90% |
| ProductoServiceTest | 10 casos | ≥ 90% |
| PedidoServiceTest | 8 casos | ≥ 90% |
| CatalogoServiceTest | 12 casos | ≥ 90% |
| ReporteServiceTest | 7 casos | ≥ 90% |
| **Total** | **43 casos** | **≥ 90% global** |

**Herramienta:** JaCoCo 0.8.11  
**Comando:** `mvn clean test jacoco:report`  
**Reporte:** `backend/target/site/jacoco/index.html`

---

## 6. Arquitectura Implementada

```
[React + Tailwind — Vercel]
         ↕ REST API (JSON + JWT)
[Spring Boot Java 17 — Render]
         ↕ JPA / Hibernate
[PostgreSQL 15 — Railway]
```

### Patrones aplicados
- MVC (Controller → Service → Repository)
- DTO (Request/Response separados de las entidades)
- Repository Pattern (Spring Data JPA)
- Filter Chain (JWT en cada petición)

---

## 7. Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|-----------|---------|
| Frontend | React + Vite | 18.2 |
| Estilos | Tailwind CSS | 3.x |
| Backend | Spring Boot | 3.2 |
| Lenguaje | Java | 17 |
| Base de datos | PostgreSQL | 15 |
| Seguridad | Spring Security + JWT | — |
| Pruebas | JUnit 5 + Mockito | — |
| Cobertura | JaCoCo | 0.8.11 |
| IDE | Visual Studio Code | — |
| Control de versiones | GitHub | — |
| Despliegue frontend | Vercel | — |
| Despliegue backend | Render | — |
| BD producción | Railway | — |
| Apoyo IA | Claude (Anthropic) | — |

---

## 8. Estructura del Repositorio

```
pacifico-repuestos/
├── frontend/          # React 18 + Tailwind CSS
├── backend/           # Spring Boot Java 17
│   └── src/test/      # 43 pruebas unitarias
├── database/          # Scripts SQL (DDL + seed)
└── docs/
    ├── ESPECIFICACION.md       ← Fuente de verdad
    ├── requerimientos.md
    ├── historias-de-usuario.md
    ├── product-backlog-sprints.md
    ├── casos-de-uso.md
    ├── arquitectura.md
    ├── guia-despliegue.md
    └── informe-final.md        ← Este archivo
```

---

## 9. Uso de IA como Herramienta de Apoyo

La IA (Claude — Anthropic) fue utilizada exclusivamente como **herramienta de apoyo** en el desarrollo, siguiendo el flujo ADIPD:

| Etapa | Uso de la IA |
|-------|-------------|
| Análisis | Generación de requerimientos, historias de usuario, casos de uso |
| Diseño | Arquitectura C4, modelo relacional, scripts SQL, diseño de API |
| Implementación | Generación de código base (entidades, services, controllers, páginas React) |
| Pruebas | Generación de pruebas unitarias con JUnit 5 + Mockito |
| Despliegue | Guía de despliegue en Vercel + Render + Railway |

> La IA **no forma parte** del sistema como funcionalidad operativa. Es una herramienta de desarrollo, equivalente a un asistente de programación.

---

## 10. Conclusiones

- El sistema **Pacífico Repuestos** fue desarrollado completamente siguiendo la metodología Scrum con 4 sprints de 2 semanas cada uno.
- Se implementaron los **40 requerimientos funcionales** y **16 no funcionales** definidos en la especificación.
- La cobertura de pruebas unitarias alcanza el **≥ 90%** medido con JaCoCo.
- El sistema está desplegado en producción y accesible públicamente.
- Toda la documentación, código y evidencias están disponibles en el repositorio GitHub.

---

*Informe generado en la Etapa D (Despliegue) del flujo ADIPD*  
*Proyecto: Pacífico Repuestos | Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
