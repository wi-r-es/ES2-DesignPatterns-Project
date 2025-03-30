@echo off
echo Cleaning up previous Docker container and image...

REM Stop and remove the container if it exists
docker rm -f java8-app-container >nul 2>&1

REM Remove the image if it exists
docker rmi -f java8-app >nul 2>&1

@echo off
echo  Building Docker Image...
docker build -t java8-app . || exit /b

echo  Running the container...
docker run --name java8-app-container java8-app || exit /b

echo  Application has been started successfully!
