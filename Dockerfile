FROM openjdk:21-jdk

WORKDIR /app

COPY target/zscore-core.jar /app/zscore-core.jar

EXPOSE 8080

CMD ["java", "-jar", "zscore-core.jar"]