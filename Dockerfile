FROM openjdk:11-jre-slim
EXPOSE 8080
ADD target/forecast-client.jar forecast-client.jar
ENTRYPOINT [ "java", "-jar", "/forecast-client.jar" ]
