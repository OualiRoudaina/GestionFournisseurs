# Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2 -Djdk.tls.client.protocols=TLSv1.2"
COPY pom.xml .
COPY src ./src
RUN mvn -B -U -DskipTests -Dmaven.wagon.http.retryHandler.count=5 package

# Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/backend-1.0.0.jar app.jar
EXPOSE 8080
# Profil : docker-compose définit SPRING_PROFILES_ACTIVE=docker.
# Cloud Run : passer SPRING_PROFILES_ACTIVE=cloudrun + SPRING_DATASOURCE_* (voir application-cloudrun.properties).
ENTRYPOINT ["java", "-jar", "app.jar"]
