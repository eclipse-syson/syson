FROM eclipse-temurin:17-jdk-jammy

ENV NODE_VERSION="18.7"
ENV SYSON_VERSION="2024.1.0"

RUN apt-get update --yes && \
    apt-get install --yes --no-install-recommends \
    git \
    ca-certificates \
    curl \
    gnupg \
    maven

ENV USER_NAME="myuser"
RUN adduser --disabled-password ${USER_NAME}
USER ${USER_NAME}
WORKDIR /home/${USER_NAME}

# ****************************************************************************************************
# Step 1: get source code
RUN git clone https://github.com/eclipse-syson/syson
WORKDIR /home/${USER_NAME}/syson

# ****************************************************************************************************
# Step 2: Front end -- Configure build environment and build
# use nvm to select the correct version of node.js, which is 18.7
RUN curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash

USER root
RUN . /home/${USER_NAME}/.nvm/nvm.sh && \
    nvm install $NODE_VERSION && \
    npm install -g npm-cli-login

USER ${USER_NAME}
RUN --mount=type=secret,id=tokens,mode=0444,target=/run/secrets/mytokens \
    . /run/secrets/mytokens && \
    . /home/${USER_NAME}/.nvm/nvm.sh && \
    nvm use $NODE_VERSION && \
    npm-cli-login -u $USER -p $PASSWORD -e $EMAIL  -s @NAMESPACE -r https://npm.pkg.github.com  && \
    npm ci && \
    npx turbo run build

# post processing
RUN mkdir -p backend/application/syson-frontend/src/main/resources/static
RUN cp -R frontend/syson/dist/* backend/application/syson-frontend/src/main/resources/static

# ****************************************************************************************************
# Step 3: Back end
RUN --mount=type=secret,id=tokens,mode=0444,target=/run/secrets/mytokens \
    . /run/secrets/mytokens && \
    mvn -U -B -e clean verify  --settings settings.xml

RUN ls backend/application/syson-application/
RUN cp backend/application/syson-application/target/syson-application-${SYSON_VERSION}.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/myuser/syson/app.jar"]
