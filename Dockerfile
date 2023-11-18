FROM gradle:7.2.0-jdk17 AS build
COPY . .
RUN gradle build

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /build/libs/toxic-teammate-1.0.jar test.jar

ADD build/libs/*.jar toxic-teammate-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","test.jar"]
