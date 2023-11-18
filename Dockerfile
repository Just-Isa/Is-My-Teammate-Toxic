# Stage 1: Build Vue.js Frontend
FROM node:latest AS vue-build

WORKDIR /app/frontend
COPY frontend /app/frontend
RUN npm install && npm run build

# Stage 2: Build Spring Boot Application
FROM openjdk:17-jdk AS spring-build
RUN microdnf install findutils

WORKDIR /app
COPY . .
COPY --from=vue-build /app/frontend/dist /app/src/main/resources/static
RUN ./gradlew --no-daemon build -x test

# Stage 3: Package Stage
FROM openjdk:17-jdk AS final
RUN microdnf install findutils

WORKDIR /app
COPY --from=spring-build /app/build/libs/*.jar app.jar

# Expose the port your app runs on (if applicable)
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]
