package net.pkhapps.idispatch.cad.config;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Interface defining a set of configuration attributes that can be changed during runtime. Clients can register
 * {@link #registerListener(Consumer) listeners} that will be notified if any attribute value is changed. This is
 * useful if you need to tweak e.g. timeouts without taking down the application.
 */
public interface Configuration {

    /**
     * Returns a set of the names of all attributes that currently have values.
     *
     * @return an unmodifiable set of attribute names.
     */
    @Nonnull
    Set<String> attributeNames();

    /**
     * Returns the value of the given attribute.
     *
     * @param attributeName the name of the attribute to get.
     * @param attributeType the type of the attribute value.
     * @param defaultValue  the value to return if the attribute value does not exist.
     * @param <T>           the type of the attribute value.
     * @return the attribute value.
     * @throws IllegalArgumentException if the attribute value cannot be converted to the requested type.
     */
    @Nonnull
    <T> T attribute(@Nonnull String attributeName, @Nonnull Class<T> attributeType, @Nonnull T defaultValue);

    /**
     * Returns the value of the given attribute.
     *
     * @param attributeName the name of the attribute to get.
     * @param attributeType the type of the attribute value.
     * @param defaultValue  the value to return if the attribute value does not exist.
     * @param <T>           the type of the attribute value.
     * @return the attribute value.
     * @throws IllegalArgumentException if the attribute value cannot be converted to the requested type.
     */
    @Nonnull
    default <T> T attribute(@Nonnull AttributeName attributeName, @Nonnull Class<T> attributeType,
                            @Nonnull T defaultValue) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        return attribute(attributeName.toString(), attributeType, defaultValue);
    }

    /**
     * Returns the value of the given attribute.
     *
     * @param attributeName the name of the attribute to get.
     * @param attributeType the type of the attribute value.
     * @param <T>           the type of the attribute value.
     * @return the attribute value.
     * @throws IllegalArgumentException if the attribute value does not exist or cannot be converted to the requested type.
     */
    @Nonnull
    <T> T attribute(@Nonnull String attributeName, @Nonnull Class<T> attributeType);

    /**
     * Returns the value of the given attribute.
     *
     * @param attributeName the name of the attribute to get.
     * @param attributeType the type of the attribute value.
     * @param <T>           the type of the attribute value.
     * @return the attribute value.
     * @throws IllegalArgumentException if the attribute value does not exist or cannot be converted to the requested type.
     */
    @Nonnull
    default <T> T attribute(@Nonnull AttributeName attributeName, @Nonnull Class<T> attributeType) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        return attribute(attributeName.toString(), attributeType);
    }

    /**
     * Loads attributes from the given map and fires a {@link AttributeChangeEvent} for all attributes whose values have
     * changed. Any attributes that are not in the map are not affected. The attribute name is computed by calling
     * the {@link Object#toString()} method of the map key. This means it will work with both string keys and
     * {@link AttributeName} keys.
     *
     * @param valueMap a name - value map of the attributes to set.
     */
    void setMultiple(@Nonnull Map<?, Object> valueMap);

    /**
     * Sets the value of the given attribute and fires a {@link AttributeChangeEvent} for the attribute.
     *
     * @param attributeName the name of the attribute to set.
     * @param value         the new value.
     */
    void setOne(@Nonnull String attributeName, @Nonnull Object value);

    /**
     * Sets the value of the given attribute and fires a {@link AttributeChangeEvent} for the attribute.
     *
     * @param attributeName the name of the attribute to set.
     * @param value         the new value.
     */
    default void setOne(@Nonnull AttributeName attributeName, @Nonnull Object value) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        setOne(attributeName.toString(), value);
    }

    /**
     * Registers a listener to be notified whenever an attribute value is changed. If the listener itself throws
     * an exception when invoked, it will be silently ignored.
     *
     * @param listener the listener to register.
     * @return a registration handle used to unregister the listener when no longer needed.
     */
    @Nonnull
    ListenerRegistration registerListener(@Nonnull Consumer<AttributeChangeEvent> listener);

    /**
     * Marker interface to be used when using enum constants as attribute names. The {@link #toString()} method
     * should be overridden to return the attribute name in string format.
     */
    interface AttributeName {
    }

    /**
     * Registration handle for {@link #registerListener(Consumer) registered} listeners.
     */
    interface ListenerRegistration {

        /**
         * Unregisters the listener from the configuration object. After calling this, the listener will no longer
         * receive {@link AttributeChangeEvent}s.
         */
        void unregister();
    }

    /**
     * Event fired by a {@link Configuration} when one or more attribute values change.
     */
    @Immutable
    class AttributeChangeEvent {

        private final Configuration configuration;
        private final Set<String> attributeNames;

        /**
         * Creates a new {@code AttributeChangeEvent}.
         *
         * @param configuration  the configuration that fires the event.
         * @param attributeNames the names of the attributes whose values have changed.
         */
        public AttributeChangeEvent(@Nonnull Configuration configuration, @Nonnull Collection<String> attributeNames) {
            this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
            this.attributeNames = Set.copyOf(Objects.requireNonNull(attributeNames, "attributeNames must not be null"));
        }

        /**
         * Returns the {@link Configuration} that fired the event.
         */
        @Nonnull
        public Configuration configuration() {
            return configuration;
        }

        /**
         * Returns the names of the attributes whose values have changed.
         *
         * @return an unmodifiable set of attribute names.
         */
        @Nonnull
        public Set<String> changedAttributeNames() {
            return attributeNames;
        }
    }
}
