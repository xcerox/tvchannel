FROM eclipse-temurin:17.0.3_7-jre-focal@sha256:f25a044d2b9c6c506ea53ffd0971c33dbde92615b2cad0d9a7284a1b7dc82f4d AS layers
RUN adduser --system --no-create-home --disabled-login --group appuser
RUN mkdir -v /opt/app && mkdir -v /opt/app/storage
RUN chown -R appuser:appuser /opt/app/storage
USER appuser:appuser
ARG JAR_FILE=./build/libs/tvchannel-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
