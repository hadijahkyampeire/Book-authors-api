FROM hseeberger/scala-sbt:8u242_1.3.7_2.12.10

WORKDIR /opt/book-api

ADD . /opt/book-api

EXPOSE 9000

ENTRYPOINT ["sbt"]
