FROM node:18.7
RUN adduser --disabled-password myuser
RUN apt-get update && apt-get install -y openjdk-17-jre && apt-get clean cache
COPY target/syson-application-2024.9.1.jar ./app.jar
EXPOSE 8080
USER myuser
ENTRYPOINT ["java","-jar","/app.jar"]
