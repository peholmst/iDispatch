package net.pkhapps.idispatch.shared.domain.base;

import org.jetbrains.annotations.NotNull;

import javax.persistence.AttributeConverter;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * Base class for {@link AttributeConverter}s that convert between a wrapped ID and a subclass of {@link DomainObjectId}.
 * Please note that attribute converters cannot be used for {@link Id primary key} fields.
 *
 * @param <ID>       the wrapped ID type (to be stored in the database).
 * @param <DomainId> the {@link DomainObjectId} subclass.
 */
public abstract class DomainObjectIdConverter<ID extends Serializable, DomainId extends DomainObjectId<ID>>
        implements AttributeConverter<DomainId, ID> {

    private final Class<DomainId> domainIdClass;

    /**
     * Constructor that takes the {@link DomainObjectId} class as an explicit argument. Use this if the
     * {@link #DomainObjectIdConverter() default constructor} does not work.
     *
     * @param domainIdClass the domain ID class.
     */
    public DomainObjectIdConverter(@NotNull Class<DomainId> domainIdClass) {
        this.domainIdClass = Objects.requireNonNull(domainIdClass, "domainIdClass must not be null");
    }

    /**
     * Default constructor that tries to determine the {@link DomainObjectId} class by introspection.
     * If this is not possible, use {@link #DomainObjectIdConverter(Class)} instead.
     */
    @SuppressWarnings("unchecked")
    public DomainObjectIdConverter() {
        this.domainIdClass = (Class<DomainId>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    @Override
    public ID convertToDatabaseColumn(DomainId attribute) {
        return DomainObjectId.unwrap(attribute);
    }

    @Override
    public DomainId convertToEntityAttribute(ID dbData) {
        return DomainObjectId.wrap(domainIdClass, dbData);
    }
}
