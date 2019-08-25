module idispatch.core.mongodb {
    requires idispatch.core.domain;

    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;

    requires org.slf4j;

    requires static org.jetbrains.annotations;
}