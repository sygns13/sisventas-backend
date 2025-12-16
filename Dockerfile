# --- Etapa 1: Compilación (Builder) ---
# Usamos una imagen de Maven con Java 11
FROM maven:3.8-openjdk-11-slim AS builder

WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de Docker
# Si no cambias dependencias, Docker no volverá a descargar todo internet
COPY pom.xml .
# Descarga dependencias (sin copiar el código fuente aún)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos y empaquetamos (sin correr tests para agilizar el build en prod)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Imagen Final) ---
# Usamos Eclipse Temurin (Java oficial y optimizado) versión Alpine (muy ligera)
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Creamos un usuario sin privilegios por seguridad (para no correr como root)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el JAR generado en la etapa anterior
# Ajusta "target/*.jar" si tu jar tiene un nombre específico fijo
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno para la JVM (Optimizadas para Contenedores)
# -XX:MaxRAMPercentage=75.0: Le dice a Java "Usa el 75% de la RAM que te asigne Docker"
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Puerto por defecto (se puede sobrescribir en docker-compose)
EXPOSE 8005

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]