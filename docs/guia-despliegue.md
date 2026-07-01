# Guía de Despliegue — Pacífico Repuestos

**Etapa:** D — Despliegue  
**Flujo:** ADIPD  
**Stack:** Vercel (frontend) + Render (backend) + Railway (PostgreSQL)

---

## Orden de despliegue

```
1. Railway  →  Crear base de datos PostgreSQL
2. Render   →  Desplegar backend Spring Boot
3. Vercel   →  Desplegar frontend React
```

---

## PASO 1 — Base de datos en Railway

1. Ir a [railway.app](https://railway.app) → New Project → PostgreSQL
2. Copiar las credenciales: **Host, Port, Database, Username, Password**
3. Conectarte con cualquier cliente SQL (DBeaver, TablePlus, pgAdmin) y ejecutar:
   ```
   database/01_ddl_create.sql
   database/02_seed_data.sql
   ```
4. Verificar que se crearon las 15 tablas correctamente

---

## PASO 2 — Backend en Render

### Preparar el repositorio

Asegúrate de tener en `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
frontend.url=${FRONTEND_URL}
```

### Desplegar en Render

1. Ir a [render.com](https://render.com) → New → Web Service
2. Conectar tu repositorio GitHub
3. Configurar:
   ```
   Name:            pacifico-repuestos-api
   Root Directory:  backend
   Runtime:         Java
   Build Command:   mvn clean package -DskipTests
   Start Command:   java -jar target/repuestos-1.0.0.jar
   ```
4. En **Environment Variables** agregar:
   ```
   DATABASE_URL       = jdbc:postgresql://<host-railway>:<port>/<database>
   DATABASE_USERNAME  = <usuario-railway>
   DATABASE_PASSWORD  = <password-railway>
   JWT_SECRET         = pacifico-repuestos-clave-secreta-256bits-2025
   JWT_EXPIRATION     = 86400000
   FRONTEND_URL       = https://pacifico-repuestos.vercel.app
   ```
5. Deploy → esperar que aparezca **Live**
6. Anotar la URL: `https://pacifico-repuestos-api.onrender.com`
7. Probar: `GET https://pacifico-repuestos-api.onrender.com/api/auth/health`

---

## PASO 3 — Frontend en Vercel

### Preparar el repositorio

Crear archivo `frontend/.env.production`:
```
VITE_API_URL=https://pacifico-repuestos-api.onrender.com/api
```

### Desplegar en Vercel

1. Ir a [vercel.com](https://vercel.com) → New Project
2. Importar repositorio GitHub
3. Configurar:
   ```
   Root Directory:   frontend
   Framework:        Vite
   Build Command:    npm run build
   Output Directory: dist
   ```
4. En **Environment Variables** agregar:
   ```
   VITE_API_URL = https://pacifico-repuestos-api.onrender.com/api
   ```
5. Deploy → esperar que aparezca **Ready**
6. URL final: `https://pacifico-repuestos.vercel.app`

---

## PASO 4 — Verificación final (prueba de humo)

Verificar cada funcionalidad en producción:

| # | Prueba | URL | Esperado |
|---|--------|-----|---------|
| 1 | API health | `/api/auth/health` | `"Pacífico Repuestos API - OK"` |
| 2 | Listar marcas | `/api/marcas` | JSON con marcas |
| 3 | Registro cliente | Frontend → /registro | Cuenta creada + token |
| 4 | Login admin | Frontend → /login | Redirige a /admin |
| 5 | Ver productos | Frontend → /productos | Grilla de productos |
| 6 | Buscar por vehículo | Buscador encadenado | Productos filtrados |
| 7 | Agregar al carrito | Botón en producto | Contador actualizado |
| 8 | Realizar pedido | /checkout → confirmar | Número de pedido generado |
| 9 | Panel admin | /admin | Dashboard con métricas |
| 10 | Cambiar estado pedido | Admin → Pedidos | Estado actualizado |

---

## PASO 5 — Cobertura JaCoCo (evidencia de pruebas)

Ejecutar localmente antes del despliegue final:

```bash
cd backend
mvn clean test jacoco:report
```

El reporte se genera en:
```
backend/target/site/jacoco/index.html
```

Abrir en el navegador y verificar que la cobertura global sea **≥ 90%**.

Capturar pantalla del reporte como evidencia para el informe del proyecto.

---

## Variables de entorno — Resumen

### Backend (Render)
| Variable | Valor |
|----------|-------|
| `DATABASE_URL` | `jdbc:postgresql://host:5432/railway` |
| `DATABASE_USERNAME` | Usuario Railway |
| `DATABASE_PASSWORD` | Password Railway |
| `JWT_SECRET` | Clave de 32+ caracteres |
| `JWT_EXPIRATION` | `86400000` (24 horas) |
| `FRONTEND_URL` | URL de Vercel |

### Frontend (Vercel)
| Variable | Valor |
|----------|-------|
| `VITE_API_URL` | URL del backend en Render + `/api` |

---

## Estructura de ramas Git

```
main          ← producción (lo que está desplegado)
develop       ← integración
feature/sprint-1  ← ya mergeado
feature/sprint-2  ← ya mergeado
feature/sprint-3  ← ya mergeado
feature/sprint-4  ← ya mergeado → merge a main → tag v1.0.0
```

### Comandos para cerrar el proyecto

```bash
# Merge develop a main
git checkout main
git merge develop

# Crear tag de versión
git tag -a v1.0.0 -m "Pacífico Repuestos v1.0.0 - Producción"
git push origin main --tags
```

---

## Credenciales demo (producción)

| Rol | Correo | Contraseña |
|-----|--------|-----------|
| Admin | admin@pacificorepuestos.com | admin123 |
| Cliente | cliente@demo.com | admin123 |

> ⚠️ Cambiar las contraseñas antes de entregar el proyecto al cliente.

---

*Documento generado en la Etapa D (Despliegue) del flujo ADIPD — Proyecto Pacífico Repuestos*  
*Metodología: Scrum | Herramienta de apoyo: IA (Claude)*
