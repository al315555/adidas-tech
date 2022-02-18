FROM maven:3.5-jdk-11
COPY src /src
COPY pom.xml /
RUN mvn clean package -q
ADD target/mic-subscription*.jar mic-subscription
ENTRYPOINT ["java", "-jar", "mic-subscription"]
EXPOSE 8080