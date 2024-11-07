FROM node:18.7-alpine
RUN adduser --disabled-password myuser
RUN wget -O /etc/apk/keys/adoptium.rsa.pub https://packages.adoptium.net/artifactory/api/security/keypair/public/repositories/apk
RUN echo 'https://packages.adoptium.net/artifactory/apk/alpine/main' >> /etc/apk/repositories
RUN apk update && apk add temurin-17-jre
COPY target/syson-application-*.jar ./
RUN find ./ -name '*.jar' ! -name '*-sources.jar' -exec mv {} syson-application.jar \;
EXPOSE 8080
USER myuser
ENTRYPOINT ["java","-jar","/syson-application.jar"]
