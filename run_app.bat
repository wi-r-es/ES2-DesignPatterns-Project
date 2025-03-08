@echo off
echo  Building Docker Image...
docker build -t java8-app . || exit /b

echo  Running the container...
docker run --name java8-app-container java8-app || exit /b

echo  Application has been started successfully!
