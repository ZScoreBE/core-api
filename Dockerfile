FROM openjdk:22-jdk

WORKDIR /app

COPY app.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]