FROM ubuntu:latest
LABEL authors="sungh"

ENTRYPOINT ["top", "-b"]