package net.pkhapps.idispatch.application.support.infrastructure.jpa.hibernate;

import net.pkhapps.idispatch.domain.support.DomainObjectId;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
public class DomainObjectIdTypeDescriptor<ID extends DomainObjectId> extends AbstractTypeDescriptor<ID> {

    private final Constructor<ID> constructor;

    public DomainObjectIdTypeDescriptor(@Nonnull Class<ID> type) {
        super(type);
        try {
            constructor = type.getConstructor(Serializable.class);
        } catch (Exception ex) {
            throw new IllegalArgumentException("ID class does not have an accessible Serializable constructor", ex);
        }
    }

    @Override
    public ID fromString(String string) {
        return newInstance(string);
    }

    @Override
    public String toString(ID value) {
        return value.unwrap().toString();
    }

    @Override
    public <X> X unwrap(ID value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (type.isAssignableFrom(getJavaType())) {
            return type.cast(value);
        }
        var unwrapped = value.unwrap();
        if (type.isInstance(unwrapped)) {
            return type.cast(unwrapped);
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> ID wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (getJavaType().isInstance(value)) {
            return getJavaType().cast(value);
        }
        if (value instanceof Serializable) {
            return newInstance((Serializable) value);
        }
        throw unknownWrap(value.getClass());
    }

    private ID newInstance(@Nonnull Serializable id) {
        Objects.requireNonNull(id, "id must not be null");
        try {
            return constructor.newInstance(id);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not invoke Serializable constructor", ex);
        }
    }
}
