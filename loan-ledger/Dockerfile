# BUILDER
FROM eclipse-temurin:17.0.2_8-jdk AS builder

# Run stage as non root
RUN useradd --user-group --system --create-home --no-log-init nonrootuser1
USER nonrootuser1

WORKDIR /app

COPY gradle gradle
COPY gradlew gradlew.bat settings.gradle build.gradle ./

RUN ./gradlew dependencies --no-daemon
COPY src src

# Unset SUID and GUID permissions to get a hardened image
RUN for i in `find / -perm +6000 -type f`; do chmod a-s $i; done
RUN ./gradlew bootJar --no-daemon

# APP
FROM eclipse-temurin:17.0.2_8-jre
# Run stage as non root
RUN useradd --user-group --system --create-home --no-log-init nonrootuser2
USER nonrootuser2

WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]
COPY --from=builder /app/build/libs/*.jar ./app.jar
COPY --from=builder /app/src/test/resources/tests ./test-files

# Unset SUID and GUID permissions to get a hardened image
RUN for i in `find / -perm +6000 -type f`; do chmod a-s $i; done
