#!/bin/sh

./mvnw -B clean compile jib:dockerBuild -Djib.to.image=scaching-app || exit 1

docker network inspect scaching_network >/dev/null 2>&1 || \
    docker network create --driver bridge scaching_network

# Run redis
docker run -d \
  --network scaching_network \
  -p 6379:6379 \
  -p 8001:8001 \
  -h redis \
  redis/redis-stack:latest > redis.id || exit 1

sleep 5

# Run replica 1
docker run -d \
  -p 8081:8080 \
  --network scaching_network \
  -h scaching-app-west \
  scaching-app > scaching-app-west.id

# Run replica 2
docker run -d \
  -p 8082:8080 \
  --network scaching_network \
  -h scaching-app-east \
  scaching-app > scaching-app-east.id
