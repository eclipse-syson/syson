FROM alpine:latest AS BUILD

RUN apk add npm

WORKDIR /usr/app

COPY doc-ui /usr/app

RUN npm install
RUN ./node_modules/gulp/bin/gulp.js bundle

FROM alpine:latest AS UI

COPY --from=BUILD /usr/app/build/ui-bundle.zip /usr/app/ui-bundle.zip


FROM alpine:latest

RUN apk add git
RUN apk add npm
RUN apk add ruby ruby-dev gcc g++ make

RUN mkdir /home/antora

WORKDIR  /home/antora

COPY docs-site/package.json .
COPY docs-site/package-lock.json .

RUN npm install
RUN gem install asciidoctor
RUN gem install asciidoctor-pdf
RUN gem install rouge
RUN gem install kramdown-asciidoc

RUN apk del ruby-dev gcc g++ make

ENV PATH="$PATH:/home/antora/node_modules/@antora/cli/bin/"

COPY --from=UI /usr/app/ui-bundle.zip .

WORKDIR /usr/app

CMD ["antora", "--clean", "--stacktrace", "--log-level=all", "/usr/app/doc/docs-site/antora-playbook.yml"]
