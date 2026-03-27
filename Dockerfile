# ── Stage 1: Build ─────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Cache Maven dependencies
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build
COPY src src
RUN ./mvnw clean package -DskipTests -B

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Upload qovluğu — runtime-da volume olaraq mount ediləcək
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app
VOLUME /app/uploads

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
