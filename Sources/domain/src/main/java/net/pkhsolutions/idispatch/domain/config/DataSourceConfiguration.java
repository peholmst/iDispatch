/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;

/**
 * This class configures the data sources used by iDispatch and registers them
 * with JNDI. Currently, a JavaDB server running on localhost is used.
 *
 * @author Petter Holmström
 * @since 1.0
 */
@DataSourceDefinition(className = "org.apache.derby.jdbc.ClientDataSource",
                      serverName = "localhost",
                      name = "java:global/jdbc/idispatch",
                      databaseName = "idispatch;create=true",
                      portNumber = 1527,
                      user = "idispatch",
                      password = "idispatch")
@Singleton
public class DataSourceConfiguration {
}
