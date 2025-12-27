FROM eclipse-temurin:21-jdk

LABEL maintainer="mergen"

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
