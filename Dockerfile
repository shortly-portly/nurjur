FROM openjdk:8-alpine

COPY target/uberjar/nurjur.jar /nurjur/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/nurjur/app.jar"]
