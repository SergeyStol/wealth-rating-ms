# Build image
FROM bellsoft/liberica-openjdk-alpine:17.0.1-12 AS builder
USER root
WORKDIR /app
COPY . .
RUN apk add maven
RUN mvn -B install -Dmaven.test.skip
RUN cp $(find /app/target/ -name 'wealth-rating-ms-*.jar') /app/target/wealth-rating-ms.jar

# Main app image
FROM bellsoft/liberica-openjre-alpine:17.0.1-12
EXPOSE 8080/tcp
WORKDIR /app
COPY --from=builder /app/target/wealth-rating-ms.jar /app/wealth-rating-ms.jar
ENTRYPOINT ["java", "-jar", "/app/wealth-rating-ms.jar"]