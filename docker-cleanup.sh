#!/bin/bash

# Prune dangling images
docker image prune -f

# Bring up your Docker Compose setup
docker-compose up -d