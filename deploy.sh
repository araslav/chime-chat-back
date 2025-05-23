#!/bin/bash
cd /home/ubuntu/backend-repo || exit
git pull origin main
./mvnw clean package -DskipTests
docker compose down
docker compose up --build -d

