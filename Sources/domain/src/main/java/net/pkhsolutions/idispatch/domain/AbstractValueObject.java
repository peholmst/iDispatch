/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import javax.persistence.Embeddable;

/**
 * Base class for value objects (VOs). The VOs are immutable to clients and
 * provide builders for creating new VOs.<p>The VOs are designed to be persisted
 * as {@link Embeddable}s.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public abstract class AbstractValueObject<T extends AbstractValueObject<T>> implements java.io.Serializable {

    // TODO How to make form binding and builder pattern play nicely together?
    boolean isNull;

    /**
     * Base class for VO builders.
     *
     * @param <T> the type of VO created by the builder.
     */
    public static abstract class AbstractBuilder<T extends AbstractValueObject<T>> {

        private T valueObject;

        /**
         * Creates a new builder, initialized with an empty VO.
         */
        protected AbstractBuilder() {
            valueObject = createNew();
        }

        /**
         * Creates a new builder, initialized with the specified VO.
         *
         * @param valueObject the VO to work on, must not be {@code null}.
         */
        protected AbstractBuilder(T valueObject) {
            this.valueObject = valueObject;
            this.valueObject.isNull = false;
        }

        private T createNew() {
            final Class<T> voClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return newValueObject(voClass);
        }

        /**
         * Returns the VO that is currently being populated. This should not be
         * exposed to clients except by the {@link #build() }
         * method.
         *
         * @return the VO, never {@code null}.
         */
        protected T getValueObject() {
            return valueObject;
        }

        /**
         * Creates a new VO with the value specified by the builder. Subsequent
         * calls to this method will return separate object instances. The
         * returned VOs will never be marked as null.
         *
         * @return a new VO, never {@code null}.
         */
        public T build() {
            final T output = this.valueObject;
            this.valueObject = output.copy();
            return output;
        }
    }

    /**
     * Checks whether this VO has been marked as null. This is a variant of the
     * Null Pattern and is used to work around the fact that in JPA, an
     * {@link Embeddable} property can never be {@code null}. As a side effect,
     * the code becomes easier to read:
     * <pre>
     * if (myEntity.getSomeValue().isNull()) {...}
     * </pre> as opposed to:
     * <pre>
     * if (myEntity.getSomeValue() == null) {...}
     * </pre> It also gets rid of the {@code NullPointerException}s.
     *
     * @see #setToNull()
     *
     * @return true if the VO has been marked as null, false otherwise (the
     * default).
     */
    public boolean isNull() {
        return isNull;
    }

    /**
     * Marks this VO instance as being null.
     *
     * @see #isNull
     */
    public void setToNull() {
        isNull = true;
        internalSetValueToNull();
    }

    /**
     * This method is called by {@link #setToNull() } in order to reset the VO's
     * internal state, if necessary.
     */
    protected abstract void internalSetValueToNull();

    /**
     * Calculates the hash code of the VO's value. This method is used by {@link #hashCode()
     * }.
     */
    protected abstract int internalCalculateHashCode();

    /**
     * Copies the value of the VO to the destination.
     *
     * @param destination the destination VO, must not be {code null}.
     */
    protected abstract void internalCopy(T destination);

    /**
     * Creates a deep copy of this VO.
     *
     * @return a new VO instance with the same value as this VO, never
     * {@code null}.
     */
    public T copy() {
        T copy = newValueObject(getValueObjectType());
        internalCopy(copy);
        copy.isNull = this.isNull;
        return copy;
    }

    /**
     * Checks if this VO has the same value as the specified VO.
     *
     * @param value the VO to compare to, must not be {@code null}.
     * @return true if the values are equal, false otherwise.
     */
    public abstract boolean hasSameValue(T value);

    @Override
    public int hashCode() {
        return Boolean.valueOf(isNull).hashCode() + internalCalculateHashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        if (getValueObjectType().isInstance(o)) {
            if (isNull) {
                return ((AbstractValueObject) o).isNull;
            } else {
                return hasSameValue(getValueObjectType().cast(o));
            }
        } else {
            return super.equals(o);
        }
    }

    private Class<T> getValueObjectType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private static <T> T newValueObject(Class<T> voClass) {
        try {
            final Constructor<T> constructor = voClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Could not create instance of " + voClass, ex);
        }

    }

    /**
     * Derives a new VO from this VO.
     *
     * @return a builder initialized with a copy of this VO.
     */
    public abstract AbstractBuilder<T> derive();
}
