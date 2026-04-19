FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar /app/rest-api.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","/app/rest-api.jar" ]