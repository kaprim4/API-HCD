FROM openjdk:11
ADD target/hcodata.jar hcodata.jar
ENTRYPOINT ["java", "-jar","hcodata.jar","--spring.profiles.active=prod"]
EXPOSE 8080