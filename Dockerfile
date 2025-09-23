FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim

ENV DEBIAN_FRONTEND=noninteractive

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

COPY .env .env

RUN wget -O javafx.zip "https://download2.gluonhq.com/openjfx/20.0.2/openjfx-20.0.2_linux-x64_bin-sdk.zip" && \
    unzip javafx.zip && \
    mv javafx-sdk-20.0.2 /opt/javafx && \
    rm javafx.zip && \
    apt-get remove -y wget unzip && \
    apt-get autoremove -y

RUN echo '#!/bin/bash\n\
echo "Starting Xvfb..."\n\
Xvfb :99 -screen 0 1024x768x24 -ac +extension GLX +render -noreset &\n\
export DISPLAY=:99\n\
echo "Starting fluxbox..."\n\
fluxbox &\n\
echo "Starting x11vnc..."\n\
x11vnc -display :99 -nopw -forever -shared &\n\
sleep 2\n\
echo "Waiting for database connection..."\n\
until nc -z ${DB_HOST:-mariadb} ${DB_PORT:-3306}; do\n\
    echo "Waiting for MariaDB..."\n\
    sleep 2\n\
done\n\
echo "Database is ready!"\n\
sleep 2\n\
echo "Starting JavaFX application..."\n\
java -Dprism.order=sw \\\n\
     -Djava.awt.headless=false \\\n\
     -Dprism.verbose=true \\\n\
     --module-path /opt/javafx/lib \\\n\
     --add-modules javafx.controls,javafx.fxml \\\n\
     -jar app.jar' > /app/start.sh && chmod +x /app/start.sh

ENV DISPLAY=:99
ENV _JAVA_AWT_WM_NONREPARENTING=1

EXPOSE 5900

ENTRYPOINT ["/bin/bash", "/app/start.sh"]