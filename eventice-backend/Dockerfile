FROM openjdk:26-ea-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

ENV DB_URL=jdbc:postgresql://localhost:5432/backend_database
ENV DB_USERNAME=username
ENV DB_PASSWORD=password

WORKDIR /app

EXPOSE 8080/tcp

ENTRYPOINT [ "java", "-jar", "app.jar" ]