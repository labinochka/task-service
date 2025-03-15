FROM openjdk:17-alpine
WORKDIR /app
COPY ./task-service-impl/build/libs/task-service-impl.jar /app/application.jar

CMD ["java", "-Xmx256m", "-jar", "/app/application.jar"]
EXPOSE 8080
