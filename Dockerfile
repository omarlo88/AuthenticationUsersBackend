FROM openjdk:8-jre-alpine
VOLUME /tmp
ADD /target/GestionAuthenticationUsers.jar GestionAuthenticationUsers.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "GestionAuthenticationUsers.jar"]