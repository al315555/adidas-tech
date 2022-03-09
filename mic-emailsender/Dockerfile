FROM maven:3.5-jdk-11
COPY src /src
COPY pom.xml /
RUN mvn clean compile -q
RUN mvn clean package -q
ADD target/mic-emailsender*.jar mic-emailsender
ENTRYPOINT ["java", "-jar", "mic-emailsender"]
EXPOSE 8080