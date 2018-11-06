package net.pkhapps.idispatch.cad.bootstrap;

import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.application.support.infrastructure.jpa.JpaUnitOfWorkFactory;
import net.pkhapps.idispatch.application.support.infrastructure.tx.UnitOfWorkManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class ApplicationContext implements ServletContextListener {

    private PersistenceManager persistenceManager;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Initializing application context");
        persistenceManager = new PersistenceManager();
        var unitOfWorkFactory = new JpaUnitOfWorkFactory(persistenceManager.entityManagerFactory());
        var unitOfWorkManager = new UnitOfWorkManager(unitOfWorkFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Destroying application context");
        persistenceManager.close();
    }
}
