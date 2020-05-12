FROM openjdk:11
MAINTAINER danielkaiser.de

COPY . /app

# update packages and install maven
RUN  \
  export DEBIAN_FRONTEND=noninteractive && \
  apt-get update && \
    apt-get -y upgrade && \
  apt-get install -y vim wget curl maven

EXPOSE 8080

CMD cd /app && ls && mvn spring-boot:run
