FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=target/actions-0.0.1*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]