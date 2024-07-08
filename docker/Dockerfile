FROM gradle:7.6-jdk17-alpine as builder

ENV APP_HOME=/apps
ENV TZ=Asia/Seoul

WORKDIR $APP_HOME

COPY gradlew $APP_HOME
COPY build.gradle $APP_HOME
COPY settings.gradle $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src

ARG JAR_FILE_PATH=./build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]