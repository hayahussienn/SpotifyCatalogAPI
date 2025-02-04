FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE ${SERVER_PORT:-8080}  # Use the environment variable for server port, defaulting to 8080

CMD ["java", "-Dserver.port=${SERVER_PORT:-8080}", "-Drate-limit.enabled=${RATE_LIMIT_ENABLED:-true}", "-Drate-limit.rpm=${RATE_LIMIT_RPM:-10}", "-Drate-limit.algo=${RATE_LIMIT_ALGO:-fix}", "-jar", "app.jar"]
