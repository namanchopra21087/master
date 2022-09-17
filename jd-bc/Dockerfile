FROM adoptopenjdk/openjdk15:alpine-jre

# Refer to Maven build -> finalName
ARG JAR_FILE=target/*.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY src/main/resources/config/*.json src/main/resources/config/
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["sh","-c","java -jar -DENV_NAME=${PROFILE} app.jar"]
