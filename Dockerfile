# syntax=docker/dockerfile:1

FROM node:24 AS frontend
WORKDIR /src
ENV CI=true
COPY . .
RUN --mount=type=secret,id=github_token,env=NODE_AUTH_TOKEN \
    --mount=type=cache,target=/root/.npm \
    npm config set //npm.pkg.github.com/:_authToken "$NODE_AUTH_TOKEN" \
    && npm ci \
    && npm config delete //npm.pkg.github.com/:_authToken
RUN node scripts/check-frontend-dependencies.js
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS backend
ARG USERNAME
WORKDIR /src
COPY . .
COPY --from=frontend /src/frontend/syson/dist backend/application/syson-frontend/src/main/resources/static
RUN --mount=type=secret,id=github_token,env=PASSWORD \
    --mount=type=cache,target=/root/.m2 \
    mvn -U -B -e clean package -DskipTests --settings settings.xml

FROM maven:3.9-eclipse-temurin-21 AS verify
WORKDIR /src
COPY . .
COPY --from=frontend /src/frontend/syson/dist backend/application/syson-frontend/src/main/resources/static

FROM eclipse-temurin:21-jre-alpine-3.20 AS runtime
RUN apk add --update-cache --no-cache nodejs=20.15.1-r0 npm=10.9.1-r0 && rm -rf /var/cache/apk/*
RUN adduser --disabled-password syson
EXPOSE 8080
USER syson
ENTRYPOINT ["java","-jar","/syson-application.jar"]

FROM runtime AS source
COPY --from=backend /src/backend/application/syson-application/target/syson-application*[^sources].jar /syson-application.jar

FROM runtime AS prebuilt
COPY build-artifacts/backend/application/syson-application/target/syson-application*[^sources].jar /syson-application.jar

FROM frontend AS frontend-publish
RUN --mount=type=secret,id=github_token,env=NODE_AUTH_TOKEN \
    npm config set //npm.pkg.github.com/:_authToken "$NODE_AUTH_TOKEN" \
    && npm publish --workspaces --provenance --access public \
    && npm config delete //npm.pkg.github.com/:_authToken

FROM backend AS backend-publish
RUN --mount=type=secret,id=github_token,env=PASSWORD \
    GITHUB_TOKEN="$PASSWORD" mvn -B deploy -DskipTests --settings settings.xml
