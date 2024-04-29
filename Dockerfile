FROM openjdk:22-jdk

WORKDIR /app

COPY target/zscore-core.jar /app/zscore-core.jar

EXPOSE 8080

CMD ["java", "-jar", "zscore-core.jar"]