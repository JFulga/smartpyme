# ===============================
# ETAPA 1: BUILD
# ===============================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src/ src/

RUN ./mvnw clean package -DskipTests


# ===============================
# ETAPA 2: RUNTIME
# ===============================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/smartP-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]