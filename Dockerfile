FROM eclipse-temurin:17-jdk-alpine
LABEL authors="Fahlaoui"
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 3030
ENTRYPOINT ["java", "-jar", "app.jar"]