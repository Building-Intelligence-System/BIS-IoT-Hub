FROM openjdk:17
MAINTAINER Ilya Gehrman <gehrman.ilya@gmail.com>

EXPOSE 54321

ADD build/libs/service.jar /

CMD echo "Server is starting..." && \
    \
    java -XX:+ExitOnOutOfMemoryError \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    -Djava.security.egd=file:/dev/./urandom \
    -Djavax.xml.accessExternalDTD=all \
    -Dspring.profiles.active=docker \
    $JAVA_OPTIONS \
    -jar /service.jar
