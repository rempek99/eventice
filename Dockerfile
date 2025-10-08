# Użyj oficjalnego obrazu JDK jako podstawy
FROM eclipse-temurin:17-jdk-alpine

# Ustaw katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj gotowy plik JAR do obrazu
COPY app.jar app.jar

# Ustaw komendę startową
ENTRYPOINT ["java", "-jar", "app.jar"]