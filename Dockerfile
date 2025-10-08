# Użyj oficjalnego obrazu JDK jako podstawy
FROM eclipse-temurin:17-jdk-alpine

# Ustaw katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj plik pom.xml i katalog źródłowy do obrazu
COPY pom.xml .
COPY src ./src

# Pobierz zależności Maven (warstwa cache)
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Zbuduj aplikację
RUN mvn package -DskipTests

# Skopiuj plik JAR do obrazu
RUN cp target/*.jar app.jar

# Ustaw komendę startową
ENTRYPOINT ["java", "-jar", "app.jar"]