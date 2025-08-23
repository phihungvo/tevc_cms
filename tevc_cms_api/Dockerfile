# Stage 1: Build ứng dụng
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests || (apt-get update && apt-get install -y maven && mvn clean package -DskipTests)

# Stage 2: Run ứng dụng
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
