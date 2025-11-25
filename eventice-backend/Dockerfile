FROM maven:3.9-amazoncorretto-25 as build
COPY . .
RUN mvn package -DskipTests

FROM ubuntu/jre:21-24.04_stable
COPY --from=build target/*.jar /app/app.jar

WORKDIR /app

EXPOSE 8080/tcp

ENTRYPOINT [ "java", "-jar", "app.jar" ]