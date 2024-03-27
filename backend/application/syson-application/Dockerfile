FROM node:18.7
RUN adduser --disabled-password myuser
RUN apt-get update && apt-get install -y openjdk-17-jre && apt-get clean cache
COPY syside-cli.js ./syside-cli.js
COPY target/syson-application-2024.1.1.jar ./app.jar
EXPOSE 8080
USER myuser
ENTRYPOINT ["java","-jar","/app.jar"]
