# ---------- Phase 1: Bauen ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Erst nur Maven-Dateien kopieren und Abhängigkeiten laden
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Dann den Code kopieren und die .jar bauen
COPY src src
RUN ./mvnw -B package -DskipTests

# ---------- Phase 2: Ausführen ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]