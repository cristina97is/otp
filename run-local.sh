#!/usr/bin/env bash
set -e
echo "Starting PostgreSQL 17 in Docker..."
docker compose up -d
echo "Starting OTP Service..."
mvn spring-boot:run
