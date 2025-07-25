# Stage 1: Build using Maven and JDK 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies first (improves rebuild time)
COPY pom.xml ./
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime with minimal base image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
