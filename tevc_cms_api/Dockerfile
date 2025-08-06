# Giai đoạn 1: Build ứng dụng
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Giai đoạn 2: Tạo image chạy ứng dụng
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/tevc_cms_api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "/app.jar"]