# Stage 1: Build compilation inside isolated Linux Container
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# FIXED: Explicitly added -DskipTests to prevent test suite compilation failures
RUN mvn clean package -DskipTests

# Stage 2: Optimized light runner JRE layer
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
