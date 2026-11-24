# ETAPA 1: Compilação
FROM maven:3.10.2-eclipse-temurin-25 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# ETAPA 2: Execução
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/dscommerce-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]