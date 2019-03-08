module idispatch.identity.server {
    requires static lombok;

    requires java.persistence;
    requires java.validation;

    requires org.hibernate.orm.core;
    requires org.hibernate.validator;

    requires org.flywaydb.core;

    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.core;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.security.core;
    requires spring.security.jwt;
    requires spring.security.oauth2;
}