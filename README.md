# Pacífico Repuestos

Sistema web de gestión y comercialización de repuestos automotrices.
**Ubicación:** Ayacucho, Perú | **Metodología:** Scrum | **Flujo:** ADIPD

## Tecnologías
- Frontend: React 18 + Tailwind CSS → Vercel
- Backend: Spring Boot 3 (Java 17) → Render
- Base de datos: PostgreSQL → Railway

## Estructura del repositorio
```
pacifico-repuestos/
├── frontend/     # React + Tailwind
├── backend/      # Spring Boot
├── database/     # Scripts SQL
└── docs/         # Documentación completa
```

## Inicio rápido

### Backend
```bash
cd backend
# Configura application.properties con tu BD local
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
# Abre http://localhost:5173
```

### Base de datos
```bash
# En PostgreSQL ejecuta:
psql -U postgres -d pacifico_db -f database/01_ddl_create.sql
psql -U postgres -d pacifico_db -f database/02_seed_data.sql
```

## Pruebas
```bash
cd backend
mvn test                    # Ejecuta pruebas
mvn jacoco:report           # Genera reporte de cobertura
# Reporte en: backend/target/site/jacoco/index.html
```

## Documentación
Ver carpeta `/docs`:
- `ESPECIFICACION.md` — Fuente de verdad del proyecto
- `requerimientos.md` — RF y RNF
- `historias-de-usuario.md` — 22 historias Scrum
- `product-backlog-sprints.md` — 4 sprints planificados
- `casos-de-uso.md` — 23 casos de uso
- `arquitectura.md` — C4, API REST, BD

## Credenciales demo
- Admin: `admin@pacificorepuestos.com` / `admin123`
- Cliente: `cliente@demo.com` / `admin123`
