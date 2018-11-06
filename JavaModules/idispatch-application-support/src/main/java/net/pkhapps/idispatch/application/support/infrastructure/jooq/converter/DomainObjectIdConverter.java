package net.pkhapps.idispatch.application.support.infrastructure.jooq.converter;

import net.pkhapps.idispatch.domain.support.DomainObjectId;
import org.jooq.Converter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Objects;
import java.util.function.Function;

/**
 * TODO Document me!
 *
 * @param <ID>
 * @param <DB>
 */
@Immutable
public abstract class DomainObjectIdConverter<ID extends DomainObjectId, DB> implements Converter<DB, ID> {

    private final Class<ID> idClass;
    private final Class<DB> dbClass;
    private final Function<DB, ID> factory;

    protected DomainObjectIdConverter(@Nonnull Class<ID> idClass,
                                      @Nonnull Class<DB> dbClass,
                                      @Nonnull Function<DB, ID> factory) {
        this.idClass = Objects.requireNonNull(idClass, "idClass must not be null");
        this.dbClass = Objects.requireNonNull(dbClass, "dbClass must not be null");
        this.factory = Objects.requireNonNull(factory, "factory must not be null");
    }

    @Override
    public ID from(DB s) {
        return factory.apply(s);
    }

    @Override
    public DB to(ID id) {
        return dbClass.cast(id.unwrap());
    }

    @Override
    public Class<DB> fromType() {
        return dbClass;
    }

    @Override
    public Class<ID> toType() {
        return idClass;
    }
}
