FROM    openjdk:8-jre

RUN     apt-get update && apt-get -y install php5

CMD     ["java", "-Xmx1g", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app/piper.jar"]

COPY    target/piper-0.0.1-SNAPSHOT.jar /app/piper.jar
