FROM maven:latest

WORKDIR /app

COPY pom.xml /app

COPY . /app

RUN mvn clean package -DskipTests

CMD ["java", "-Djavafx.headless=true", "-jar", "target/CardApp.jar"]
