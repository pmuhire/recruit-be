# ==============================
# Stage 1 — Build the application
# ==============================
FROM maven:3.9.8-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom first (layer caching optimization)
COPY pom.xml .

# Download dependencies (cache)
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# ==============================
# Stage 2 — Runtime image
# ==============================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8000

# Run Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]