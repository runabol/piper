FROM    openjdk:8-jre

CMD     ["java", "-Xmx1g", "-jar", "-Djava.security.egd=file:/dev/./urandom", "/app/piper.jar"]

COPY    target/piper-0.0.1-SNAPSHOT.jar /app/piper.jar
