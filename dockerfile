# Multi-stage build for smaller image size

# Stage 1: Build the application
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven files for dependency caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for faster builds)
RUN ./mvnw clean package -DskipTests

# Stage 2: Create runtime image
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/pocketly-be-0.0.1-SNAPSHOT.jar app.jar

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# Expose port (Render will assign its own port via $PORT)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Run the application
# Render will override PORT, so we use ${PORT:-8080}
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]