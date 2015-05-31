FROM debian:latest
MAINTAINER Roberto Cortez <radcortez@yahoo.com>

RUN apt-get update && apt-get -y install wget git

RUN wget --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u40-b25/jdk-8u40-linux-x64.tar.gz && \
    mkdir /opt/jdk && \
    tar -zxf jdk-8u40-linux-x64.tar.gz -C /opt/jdk && \
    update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_40/bin/java 100 && \
    update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk1.8.0_40/bin/javac 100 && \
    rm -rf jdk-8u40-linux-x64.tar.gz

ENV JAVA_HOME /opt/jdk/jdk1.8.0_40/

RUN wget http://mirrors.fe.up.pt/pub/apache/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz && \
    tar -zxf apache-maven-3.2.5-bin.tar.gz -C /opt/ && \
    rm -rf apache-maven-3.2.5-bin.tar.gz

ENV PATH /opt/apache-maven-3.2.5/bin:$PATH

RUN cd opt && \
    git clone https://github.com/radcortez/wow-auctions.git wow-auctions

WORKDIR /opt/wow-auctions/

RUN mvn clean install && \
    cd batch && \
    mvn wildfly:start

EXPOSE 8080

CMD git pull && cd batch && mvn wildfly:run
