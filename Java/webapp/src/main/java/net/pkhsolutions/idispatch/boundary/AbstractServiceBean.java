package net.pkhsolutions.idispatch.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;

abstract class AbstractServiceBean {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    PlatformTransactionManager txManager;

    private TransactionTemplate txTemplate;

    @PostConstruct
    void init() {
        txTemplate = new TransactionTemplate(txManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    TransactionTemplate getTxTemplate() {
        return txTemplate;
    }

    ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
