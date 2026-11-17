FROM eclipse-temurin:21-jre-alpine-3.20
RUN adduser --disabled-password syson
COPY target/syson-application*[^sources].jar /syson-application.jar
EXPOSE 8080
USER syson
ENTRYPOINT ["java","-jar","/syson-application.jar"]
