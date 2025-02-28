FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /src/advshop

# Copy source code into the builder image
COPY . .

# Build the Spring Boot application (produces a .jar file)
RUN ./gradlew clean bootJar

FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

ARG USER_NAME=advshop
ARG USER_UID=1000
ARG USER_GID=1000

# Create a non-root user/group
RUN addgroup -g ${USER_GID} ${USER_NAME} \
  && adduser -h /opt/advshop -D -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

# Switch to the new user
USER ${USER_NAME}
WORKDIR /opt/advshop

# Copy the built JAR from the builder stage
COPY --chown=${USER_NAME}:${USER_NAME} --from=builder /src/advshop/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]