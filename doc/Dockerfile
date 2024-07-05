FROM alpine:latest AS build

RUN apk add npm

WORKDIR /usr/app

COPY doc-ui /usr/app

RUN npm install
RUN ./node_modules/gulp/bin/gulp.js bundle

FROM alpine:latest AS ui

COPY --from=build /usr/app/build/ui-bundle.zip /usr/app/ui-bundle.zip


FROM alpine:latest

RUN apk add git
RUN apk add npm
RUN apk add ruby ruby-dev gcc g++ make

RUN mkdir /home/antora

WORKDIR  /home/antora

COPY docs-site/package.json .

RUN npm install
RUN gem install asciidoctor
RUN gem install asciidoctor-pdf
RUN gem install rouge
RUN gem install kramdown-asciidoc

RUN apk del ruby-dev gcc g++ make

ENV PATH="$PATH:/home/antora/node_modules/@antora/cli/bin/"

COPY --from=ui /usr/app/ui-bundle.zip .

WORKDIR /usr/app

CMD ["antora", "--clean", "--stacktrace", "--log-failure-level=warn", "--log-level=all", "/usr/app/doc/docs-site/antora-playbook.yml"]
