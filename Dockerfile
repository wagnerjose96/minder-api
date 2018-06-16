FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/escoladeti2018-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]