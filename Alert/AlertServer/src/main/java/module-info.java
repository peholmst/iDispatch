module idispatch.alert.server {
    requires freemarker;
    requires org.slf4j;

    exports net.pkhapps.idispatch.alert.server.data to freemarker;
}
