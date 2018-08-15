#!/usr/bin/env bash

set -e
mvn clean verify -B

echo "Starting server"
echo "==============="

java -jar target/simple-transaction-service-exec.jar