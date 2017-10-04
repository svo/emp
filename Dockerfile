FROM openjdk:8-jre-alpine
MAINTAINER Sean Van Osselaer <svo@qual.is>

ADD target/emp-0.0.1-SNAPSHOT-standalone.jar /emp/app.jar

RUN mkdir -p /var/lib/emp

EXPOSE 1099 8080

CMD ["java", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-XX:+PrintGCTimeStamps", "-Xloggc:gc.log", "-Dcom.sun.management.jmxremote.port=1099", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-jar", "/emp/app.jar"]
