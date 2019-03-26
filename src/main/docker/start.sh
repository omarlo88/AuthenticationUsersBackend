#!/usr/bin/env bash
./wait-for-it.sh myDbUsers:3306 -t 15
java -Djava.security.egd=file:/dev/./data -jar GestionAuthenticationUsers.jar