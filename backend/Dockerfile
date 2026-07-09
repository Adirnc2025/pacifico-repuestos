# ============================================================
# Pacifico Repuestos - Backend
# Build multi-etapa: compila con Maven, corre con un JRE liviano
# ============================================================

# ---- Etapa 1: compilar ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar solo el pom.xml primero para cachear las dependencias
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copiar el codigo fuente y compilar el jar ejecutable (sin correr tests aqui)
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Etapa 2: runtime ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Puerto informativo (Render asigna el puerto real por la variable de entorno PORT,
# que application.properties ya lee mediante server.port=${PORT:8080})
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
