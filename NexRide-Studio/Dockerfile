FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN groupadd --gid 10001 nexride && useradd --uid 10001 --gid nexride --no-create-home nexride && mkdir -p /app/data && chown nexride:nexride /app/data
COPY --chown=nexride:nexride NexRide.jar /app/NexRide.jar
USER nexride
ENV SERVER_ADDRESS=0.0.0.0 SERVER_PORT=8080 NEXRIDE_DATA_DIR=/app/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/NexRide.jar"]
