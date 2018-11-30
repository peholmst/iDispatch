package net.pkhapps.idispatch.application.support.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import net.pkhapps.idispatch.application.support.infrastructure.tx.UnitOfWork;
import net.pkhapps.idispatch.application.support.infrastructure.tx.UnitOfWorkFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

/**
 * TODO Document me!
 */
@ThreadSafe
@Slf4j
public class JpaUnitOfWorkFactory implements UnitOfWorkFactory {

    private final EntityManagerFactory entityManagerFactory;

    public JpaUnitOfWorkFactory(@Nonnull EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = Objects.requireNonNull(entityManagerFactory,
                "entityManagerFactory must not be null");
    }

    @Nonnull
    @Override
    public UnitOfWork createUnitOfWork() {
        return new JpaUnitOfWork(entityManagerFactory);
    }
}
