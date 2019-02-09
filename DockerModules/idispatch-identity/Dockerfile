FROM jboss/keycloak:4.8.3.Final

USER jboss

ENV KEYCLOAK_USER=admin
ENV KEYCLOAK_PASSWORD=Admin123
ENV PROXY_ADDRESS_FORWARDING=true
ENV DB_VENDOR=postgres
ENV DB_USER=keycloak
ENV DB_PASSWORD=mysecretpassword
ENV DB_ADDR=idispatch-db
ENV DB_PORT=5432
ENV DB_DATABASE=keycloak

RUN sed -i -e 's/<web-context>auth<\/web-context>/<web-context>keycloak\/auth<\/web-context>/' $JBOSS_HOME/standalone/configuration/standalone.xml \
    && sed -i -e 's/<web-context>auth<\/web-context>/<web-context>keycloak\/auth<\/web-context>/' $JBOSS_HOME/standalone/configuration/standalone-ha.xml
