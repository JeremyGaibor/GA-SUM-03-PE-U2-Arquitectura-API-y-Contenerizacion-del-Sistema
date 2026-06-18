# ETAPA 1: COMPILACION
# Construye el archivo .jar usando Maven y Java 17
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ETAPA 2: EJECUCION
# Ejecuta el microservicio con Java 17
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8001

ENTRYPOINT ["java", "-jar", "app.jar"]