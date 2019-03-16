package net.pkhapps.idispatch.base.domain;

import org.springframework.lang.NonNull;

import javax.persistence.AttributeConverter;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import static java.util.Objects.requireNonNull;

/**
 * Base class for attribute converters that convert between {@code long}s and {@link DomainObjectId} instances.
 */
public class DomainObjectIdConverter<T extends DomainObjectId> implements AttributeConverter<T, Long>, Serializable {

    private final Class<T> domainObjectIdClass;

    /**
     * Default constructor that tries to use introspection to determine the type of the domain object ID. Only works
     * if the concrete class itself is not generic.
     */
    public DomainObjectIdConverter() {
        this.domainObjectIdClass = introspectDomainObjectIdClass();
    }

    /**
     * Constructor that accepts the type of the domain object ID as a parameter. Use this if {@link DomainObjectIdConverter}
     * does not work.
     *
     * @param domainObjectIdClass the type of the domain object ID.
     */
    public DomainObjectIdConverter(@NonNull Class<T> domainObjectIdClass) {
        this.domainObjectIdClass = requireNonNull(domainObjectIdClass);
    }

    @SuppressWarnings("unchecked")
    private Class<T> introspectDomainObjectIdClass() {
        var genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            var parameterizedType = (ParameterizedType) genericSuperclass;
            if (parameterizedType.getActualTypeArguments().length > 0) {
                var firstTypeArgumentType = parameterizedType.getActualTypeArguments()[0];
                if (firstTypeArgumentType instanceof Class && DomainObjectId.class.isAssignableFrom((Class) firstTypeArgumentType)) {
                    return (Class<T>) firstTypeArgumentType;
                }
            }
        }
        throw new UnsupportedOperationException("Could not determine type of DomainObjectId through introspection");
    }

    @Override
    public Long convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.toLong();
    }

    @Override
    public T convertToEntityAttribute(Long dbData) {
        try {
            return dbData == null ? null : domainObjectIdClass.getDeclaredConstructor(Long.class).newInstance(dbData);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Could not convert " + dbData + " to instance of " + domainObjectIdClass, ex);
        }
    }
}
