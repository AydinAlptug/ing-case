FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /app
COPY pom.xml /app/
COPY src /app/src/

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /app/target/credit-*.jar /app/credit-app.jar

EXPOSE 9090

CMD ["java", "-jar", "/app/credit-app.jar"]
