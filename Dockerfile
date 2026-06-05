# Stage 1: Compilation avec Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
# Téléchargement des dépendances en cache
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Image finale légère pour le Raspberry Pi
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/testelasticsearch-0.0.1-SNAPSHOT.jar app.jar

# Port exposé par Spring Boot
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]