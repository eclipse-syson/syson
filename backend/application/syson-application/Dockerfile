FROM eclipse-temurin:17-jre-alpine-3.20
RUN apk add --update-cache --no-cache nodejs=20.15.1-r0 npm=10.9.1-r0 && rm -rf /var/cache/apk/*
RUN adduser --disabled-password syson
COPY target/syson-application*[^sources].jar /syson-application.jar
EXPOSE 8080
USER syson
ENTRYPOINT ["java","-jar","/syson-application.jar"]