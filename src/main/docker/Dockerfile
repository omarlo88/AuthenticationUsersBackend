FROM openjdk:8-jre-alpine
WORKDIR /tmp
VOLUME ["/tmp"]
COPY maven/GestionAuthenticationUsers.jar GestionAuthenticationUsers.jar
COPY maven/start.sh start.sh
COPY maven/wait-for-it.sh wait-for-it.sh
RUN sh -c 'touch GestionAuthenticationUsers.jar'
ENTRYPOINT ["./start.sh"]