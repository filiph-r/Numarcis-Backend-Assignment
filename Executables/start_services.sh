#!/bin/bash

# Get the directory of the script
SCRIPT_DIR=$(dirname "$0")

# Define the paths to your JAR files relative to the script's directory
EUREKA_SERVER_JAR="$SCRIPT_DIR/eureka-server.jar"
API_GATEWAY_JAR="$SCRIPT_DIR/api-gateway.jar"
ORDER_SERVICE_JAR="$SCRIPT_DIR/order-service.jar"
PRODUCT_SERVICE_JAR="$SCRIPT_DIR/product-service.jar"
USER_SERVICE_JAR="$SCRIPT_DIR/user-service.jar"

# PIDs of started processes
PIDS=()

# Function to start a JAR file and keep it running
start_jar() {
    local jar_path="$1"
    nohup java -jar "$jar_path" > /dev/null 2>&1 &
    PIDS+=($!)
    echo "Started $jar_path with PID $!"
}

# Function to stop all started JAR files
stop_jars() {
    echo "Stopping all services..."
    for pid in "${PIDS[@]}"; 
    do
        kill "$pid"
        wait "$pid"
    done
    echo "All services stopped."
    exit 0
}

# Trap SIGTERM and SIGINT to stop services on script termination
trap stop_jars SIGTERM SIGINT

# Start the Eureka server
start_jar "$EUREKA_SERVER_JAR"

# Wait a few seconds to ensure Eureka server is up
echo "Waiting 10 seconds for Eureka server to start..."
sleep 10

# Start the API gateway
start_jar "$API_GATEWAY_JAR"

# Wait a few seconds to ensure API gateway is up
echo "Waiting 10 seconds for API gateway to start..."
sleep 10

# Start the remaining services
start_jar "$ORDER_SERVICE_JAR"
start_jar "$PRODUCT_SERVICE_JAR"
start_jar "$USER_SERVICE_JAR"

echo "All services started. Press Ctrl+C to stop."

# Keep script running
while true; do
    sleep 1
done
