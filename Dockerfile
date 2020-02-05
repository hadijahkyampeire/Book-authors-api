FROM hseeberger/scala-sbt:8u242_1.3.7_2.12.10

WORKDIR /opt/book-api
EXPOSE 9000

# add build and project so that dependencies can be downloaded -- this will only rerun if the dependencies change,
# but not if the code changes
ADD ./project   ./project
ADD ./build.sbt ./build.sbt
RUN sbt update

# add everything else
ADD . .

ENTRYPOINT ["sbt"]
CMD ["run"]
