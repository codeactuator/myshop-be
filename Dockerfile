# Stage 1: Build the application jar using JDK and Maven
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build package (skipping tests for faster builds)
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create a minimal, secure runtime image using JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as non-root user for security best practices
RUN groupadd -g 1001 appgroup && \
    useradd -u 1001 -g appgroup -m -s /bin/bash appuser

# Copy the compiled JAR from the builder stage
COPY --from=builder /app/target/app.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

# Expose standard container port
EXPOSE 8080

# Optimize JVM for serverless cold-starts on Cloud Run: Enable container support, 75% max RAM, and tiered compilation
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]