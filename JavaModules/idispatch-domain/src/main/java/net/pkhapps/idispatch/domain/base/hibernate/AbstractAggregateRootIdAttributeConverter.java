package net.pkhapps.idispatch.domain.base.hibernate;

import net.pkhapps.idispatch.domain.base.AbstractAggregateRootId;
import org.springframework.lang.NonNull;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * Base class for attribute converters that convert between subclasses of {@link AbstractAggregateRootId} and long.
 */
@Deprecated
public class AbstractAggregateRootIdAttributeConverter<T extends AbstractAggregateRootId>
        implements AttributeConverter<T, Long> {

    private final Constructor<T> constructor;

    /**
     * Protected constructor. The specified class must have a public constructor that takes a single long parameter.
     */
    protected AbstractAggregateRootIdAttributeConverter(@NonNull Class<T> idClass) {
        Objects.requireNonNull(idClass);
        try {
            constructor = idClass.getConstructor(Long.class);
        } catch (Exception ex) {
            throw new RuntimeException("ID class does not have an accessible Long constructor", ex);
        }
    }

    @Override
    public Long convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.toLong();
    }

    @Override
    public T convertToEntityAttribute(Long dbData) {
        try {
            return dbData == null ? null : constructor.newInstance(dbData);
        } catch (Exception ex) {
            throw new RuntimeException("Could not invoke Long constructor", ex);
        }
    }
}
