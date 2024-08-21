FROM gradle:7.6.2-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle build --no-daemon --refresh-dependencies || return 0
COPY src ./src
RUN gradle build --no-daemon




FROM eclipse-temurin:17.0.3_7-jre-focal@sha256:f25a044d2b9c6c506ea53ffd0971c33dbde92615b2cad0d9a7284a1b7dc82f4d
RUN adduser --system --no-create-home --disabled-login --group appuser
RUN mkdir -v /opt/app && mkdir -v /opt/app/storage
RUN chown -R appuser:appuser /opt/app/storage
USER appuser:appuser
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
