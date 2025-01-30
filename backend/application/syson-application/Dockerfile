FROM eclipse-temurin:17-jre-alpine
RUN apk add --update-cache --no-cache nodejs npm && rm -rf /var/cache/apk/*
RUN adduser --disabled-password syson
COPY target/syson-application*[^sources].jar /syson-application.jar
EXPOSE 8080
USER syson
ENTRYPOINT ["java","-jar","/syson-application.jar"]