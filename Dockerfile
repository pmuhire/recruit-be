# -------------------------
# 1. Build stage
# -------------------------
FROM maven:3.9.7-eclipse-temurin-22 AS build

WORKDIR /app

# Copy pom.xml first (for caching Maven dependencies)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the project
COPY src ./src

# Build the Spring Boot jar (skip tests for faster build)
RUN mvn clean package -DskipTests

# -------------------------
# 2. Run stage
# -------------------------
FROM eclipse-temurin:22-jdk-slim

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]