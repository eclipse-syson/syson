FROM eclipse-temurin:17-jre
COPY target/syson-application-2023.10.9.jar ./app.jar
EXPOSE 8080
RUN adduser --disabled-password myuser
USER myuser
ENTRYPOINT ["java","-jar","/app.jar"]
