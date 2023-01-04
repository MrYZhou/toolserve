FROM openjdk:17-slim

WORKDIR /java
ADD demo*.jar /java/app.jar
ENTRYPOINT ["java","-jar","/java/app.jar"]

EXPOSE 8080