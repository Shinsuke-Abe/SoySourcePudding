FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
VOLUME /opt/learning/
ADD ./learning.backend-1.0-SNAPSHOT.tar /

RUN apk add --no-cache g++
RUN apk add --update bash && rm -rf /var/cache/apk/*

ENTRYPOINT ["/learning.backend-1.0-SNAPSHOT/bin/learning.backend"]
