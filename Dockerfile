FROM openjdk:11-jre-slim
EXPOSE 8761
ADD target/forecast-server.jar forecast-server.jar
ENTRYPOINT [ "java", "-jar", "/forecast-server.jar" ]
