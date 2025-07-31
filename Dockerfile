# === Stage 1: Dựng code giống IntelliJ (Maven với resource + compile) ===
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build

# Copy toàn bộ mã nguồn
COPY . .

# Bước tương đương với:
# - Copying resources...
# - Compiling sources...
# - Saving build caches...
# - Executing post-compile tasks...
RUN mvn clean package -DskipTests

# === Stage 2: Chạy ứng dụng với JDK nhẹ ===
FROM eclipse-temurin:21-jdk-alpine AS run
WORKDIR /app

# Copy file jar đã build từ stage trước
COPY --from=build /build/target/E-CommercePlatform-0.0.1-SNAPSHOT.jar app.jar

# Expose port ứng dụng (Spring Boot default)
EXPOSE 8080

# Command tương tự IntelliJ "Run" cấu hình: java -jar target/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
