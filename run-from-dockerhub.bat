@echo off
echo Starting CardApp from DockerHub...

REM Check if .env.local exists
if not exist .env.local (
    echo ERROR: .env.local file not found!
    echo Please copy .env.local.example to .env.local and fill in your database credentials.
    pause
    exit /b 1
)

echo Loading environment variables from .env.local...
for /f "usebackq tokens=1,2 delims==" %%a in (".env.local") do (
    if not "%%a"=="" if not "%%b"=="" (
        set "%%a=%%b"
    )
)

echo Pulling latest image from DockerHub...
docker pull vladi1009/card_memo:v0.1.1

echo Starting application...
docker-compose -f docker-compose.prod.yaml up

pause