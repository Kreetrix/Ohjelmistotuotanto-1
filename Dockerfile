FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libx11-6 \
    libxcb1 \
    libxtst6 \
    libxrender1 \
    libxrandr2 \
    libasound2 \
    libpangocairo-1.0-0 \
    libatk-bridge2.0-0 \
    libdrm2 \
    libxcomposite1 \
    libxdamage1 \
    libxss1 \
    xvfb \
    fluxbox \
    x11vnc \
    wget \
    unzip \
    netcat-traditional \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/OHJELMISTOTUOTANTO-1-1.0-SNAPSHOT.jar app.jar

COPY .env.docker .env.docker

RUN wget -O javafx.zip "https://download2.gluonhq.com/openjfx/20.0.2/openjfx-20.0.2_linux-x64_bin-sdk.zip" && \
    unzip javafx.zip && \
    mv javafx-sdk-20.0.2 /opt/javafx && \
    rm javafx.zip && \
    apt-get remove -y wget unzip && \
    apt-get autoremove -y



CMD ["sh", "-c", "Xvfb :99 -screen 0 1280x1024x24 & export DISPLAY=:99 && x11vnc -forever -shared -nopw -rfbport 5900 & sleep 10 && java -jar app.jar"]