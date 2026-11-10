# Deployment

## Local Windows — easiest for the presentation

Use START-NEXRIDE.bat. This automatically runs the included JAR on a free port and opens the correct page. No Maven, npm, VS Code extension or Docker is needed. Admin login is ADMIN-LOGIN.bat. Data stays in this extracted folder.

## Docker on a local computer

Install Docker and run `docker compose up -d --build` in the extracted project directory. Visit http://localhost:8095. The named volume persists records and photos across container restarts. `docker compose down` stops the container. Do not add `-v` unless you intend to delete stored data.

## Public Java/container host

This Spring Boot application needs one Java 17+ process or container plus persistent storage. A static HTML host cannot run its APIs.

1. Upload/build the supplied Dockerfile and NexRide.jar, or start the JAR on a Java host.
2. Mount a persistent writable disk at `/app/data` and set `NEXRIDE_DATA_DIR=/app/data`. For the included container, the mount must be writable by UID 10001.
3. Bind to `SERVER_ADDRESS=0.0.0.0`. The Docker image defaults to `SERVER_PORT=8080`. If the host provides a dynamic `PORT`, set `SERVER_PORT` to its expected port or omit the Docker image's SERVER_PORT override and pass `--server.port` explicitly. The Java properties support `PORT` when SERVER_PORT is not set.
4. Put the application behind the host's HTTPS reverse proxy. Set `NEXRIDE_PUBLIC_URL=https://your-domain` and `SESSION_COOKIE_SECURE=true` once HTTPS is in place.
5. For the first boot, configure `NEXRIDE_ADMIN_EMAIL` and a strong `NEXRIDE_ADMIN_PASSWORD` through the host's secret settings. These seed the first account only; they do not reset an existing account.
6. Configure PayHere merchant secrets only when enabling real payments. See ONLINE_PAYMENTS.md. Test with provider sandbox first.
7. Keep a single replica. The JSON store deliberately locks out a second writer. Back up the complete data directory, including uploads.

The supplied Compose file binds its host port to loopback for local use. On a VPS, connect an HTTPS reverse proxy to that loopback port. Merely running Compose does not configure a domain or TLS.

## Rebuild after code changes

```
mvn clean package
```

Copy `target/nexride-reborn-3.0.0.jar` over `NexRide.jar` after stopping the running server. Docker users rebuild the image. Frontend files are embedded in the JAR; changing source HTML/CSS/JS without rebuilding will not change the running prebuilt JAR.

## Verification limits

No hosted account was connected or deployed in this task. Docker and Windows runtime execution were unavailable in the development environment. The JAR and full HTTP workflows were tested on Linux; the supplied launcher and container files were reviewed but need their first native-environment run.

References:
- https://docs.docker.com/reference/cli/docker/compose/up/
- https://docs.spring.io/spring-boot/3.5/reference/features/external-config.html
