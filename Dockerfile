# ============================================================
# DevFlow Multi-Stage Dockerfile (Build + Apache Tomcat 9 Runtime)
# ============================================================

# Stage 1: Build with Maven & OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production Apache Tomcat 9 Container
FROM tomcat:9.0-jdk17-temurin
LABEL maintainer="DevFlow Team <support@devflow.io>"

# Remove default ROOT application
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy packaged WAR to ROOT context
COPY --from=builder /app/target/DevFlow.war /usr/local/tomcat/webapps/ROOT.war

# Create document uploads folder
RUN mkdir -p /usr/local/tomcat/uploads/docs

EXPOSE 8080
CMD ["catalina.sh", "run"]
