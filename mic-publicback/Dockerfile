FROM maven:3.5-jdk-11
COPY src /src
COPY pom.xml /
RUN mvn clean package -q
ADD target/mic-publicback*.jar mic-publicback
ENTRYPOINT ["java", "-jar", "mic-publicback"]
EXPOSE 8080