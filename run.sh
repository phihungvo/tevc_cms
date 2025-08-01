#!/bin/bash

# chmod +x run.sh
# ./run.sh

# Start Spring Boot backend
echo "Starting Spring Boot backend..."
cd movienest_api
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Wait a few seconds for backend to fully start
echo "Waiting for backend to be ready..."
sleep 10

# Start React frontend
echo "Starting React frontend..."
cd movienest_app
npm start

# Optional: stop backend when frontend ends
kill $BACKEND_PID
