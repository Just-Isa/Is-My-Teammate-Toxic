FROM openjdk:22
ADD build/libs/*.jar toxic-teammate-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","toxic-teammate-1.0.jar"]