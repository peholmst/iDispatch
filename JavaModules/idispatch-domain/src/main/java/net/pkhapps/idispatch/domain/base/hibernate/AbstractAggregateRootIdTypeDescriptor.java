package net.pkhapps.idispatch.domain.base.hibernate;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.springframework.lang.NonNull;

import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * TODO Document me!
 *
 * @param <ID>
 */
public abstract class AbstractAggregateRootIdTypeDescriptor<ID extends AbstractAggregateRootId> extends AbstractTypeDescriptor<ID> {

    private final Constructor<ID> constructor;

    protected AbstractAggregateRootIdTypeDescriptor(Class<ID> type) {
        super(type);
        try {
            constructor = type.getConstructor(Long.class);
        } catch (Exception ex) {
            throw new RuntimeException("ID class does not have an accessible Long constructor", ex);
        }
    }

    @Override
    public String toString(ID value) {
        return Long.toString(value.toLong());
    }

    @Override
    public ID fromString(String string) {
        return newInstance(Long.valueOf(string));
    }

    @Override
    public <X> X unwrap(ID value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (getJavaTypeClass().isAssignableFrom(type)) {
            return type.cast(value);
        }
        if (String.class.isAssignableFrom(type)) {
            return type.cast(Long.toString(value.toLong()));
        }
        if (Long.class.isAssignableFrom(type)) {
            return type.cast(value.toLong());
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> ID wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (getJavaTypeClass().isInstance(value)) {
            return getJavaTypeClass().cast(value);
        }
        if (String.class.isInstance(value)) {
            return newInstance(Long.valueOf(String.class.cast(value)));
        }
        if (Long.class.isInstance(value)) {
            return newInstance(Long.class.cast(value));
        }
        throw unknownWrap(value.getClass());
    }

    @NonNull
    private ID newInstance(@NonNull Long id) {
        Objects.requireNonNull(id);
        try {
            return constructor.newInstance(id);
        } catch (Exception ex) {
            throw new RuntimeException("Could not invoke Long constructor", ex);
        }
    }
}
