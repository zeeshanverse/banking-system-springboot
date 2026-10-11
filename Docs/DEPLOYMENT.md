# Deployment guide

This service is container-ready and can be deployed to any platform that supports Docker containers and environment variables.

## Required environment variables

```text
DB_URL=jdbc:postgresql://<host>:5432/<database>
DB_USERNAME=<database-user>
DB_PASSWORD=<database-password>
JWT_SECRET=<long-random-secret>
REDIS_HOST=<redis-host>
REDIS_PORT=6379
```

## Build

```bash
./mvnw clean package -DskipTests
```

## Container

```bash
docker build -t banking-system .
docker run -p 8080:8080 \
  -e DB_URL="jdbc:postgresql://..." \
  -e DB_USERNAME="..." \
  -e DB_PASSWORD="..." \
  -e JWT_SECRET="..." \
  -e REDIS_HOST="..." \
  -e REDIS_PORT="6379" \
  banking-system
```

## Portfolio deployment recommendation

For a portfolio, deploy one Spring Boot instance plus managed PostgreSQL and Redis. Keep `JWT_SECRET`, database credentials, and Redis credentials in the platform's environment-variable/secret settings.

Because report tasks and rate-limit buckets are currently process-local, use a single API instance until those stores are made distributed.
