# ==========================================
# Multi-stage Dockerfile for PharmaMES
# ==========================================

# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Runtime Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-privileged security user
RUN addgroup -S mesgroup && adduser -S mesuser -G mesgroup
USER mesuser

# Copy built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose standard MES port
EXPOSE 8080

# Configure container-aware JVM memory options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
