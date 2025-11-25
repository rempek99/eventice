FROM maven:3.9-amazoncorretto-25 as build
COPY . .
RUN mvn package -DskipTests

FROM ubuntu/jre:21-24.04_stable
COPY --from=build target/*.jar /app/app.jar

ENV POSTGRES_USER=zimny
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=EVENTICE_DB
ENV DB_URL=jdbc:postgresql://localhost:5432/backend_database

WORKDIR /app

EXPOSE 8080/tcp

ENTRYPOINT [ "java", "-jar", "app.jar" ]