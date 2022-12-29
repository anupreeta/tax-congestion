FROM openjdk:17-oracle
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY target/calculator-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]