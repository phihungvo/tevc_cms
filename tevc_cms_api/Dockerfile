# Stage 1: Build JAR
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests  # Sử dụng Maven wrapper, thay bằng "mvn" nếu dùng global

# Stage 2: Run JAR
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
