# Build SysON from the working tree and package the runtime image, tests skipped.
# Same steps as .github/workflows/build.yml. USERNAME and PASSWORD: a GitHub login and a
# token with read:packages, for the dependencies hosted on GitHub Packages.

FROM node:24 AS frontend
ARG PASSWORD
WORKDIR /src
COPY . .
# .npmrc has no trailing newline, hence the printf
RUN printf '\n//npm.pkg.github.com/:_authToken=%s\n' "${PASSWORD}" >> .npmrc
RUN npm ci
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
ARG USERNAME
ARG PASSWORD
WORKDIR /src
COPY . .
COPY --from=frontend /src/frontend/syson/dist backend/application/syson-frontend/src/main/resources/static
RUN mvn -B clean package -DskipTests --settings settings.xml

# Runtime, same as backend/application/syson-application/Dockerfile
FROM eclipse-temurin:21-jre-alpine-3.20
RUN apk add --update-cache --no-cache nodejs=20.15.1-r0 npm=10.9.1-r0 && rm -rf /var/cache/apk/*
RUN adduser --disabled-password syson
COPY --from=backend /src/backend/application/syson-application/target/syson-application*[^sources].jar /syson-application.jar
EXPOSE 8080
USER syson
ENTRYPOINT ["java","-jar","/syson-application.jar"]
