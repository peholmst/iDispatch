package net.pkhapps.idispatch.cad.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link Configuration}.
 */
@ThreadSafe
@Slf4j
public class DefaultConfiguration implements Configuration {

    private final ConcurrentMap<String, Object> attributeValues = new ConcurrentHashMap<>();
    private final ConcurrentMap<Consumer<AttributeChangeEvent>, Void> listeners = new ConcurrentHashMap<>();

    private static <T> T castOrConvert(@Nonnull Class<T> type, @Nonnull Object value) {
        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            try {
                var factory = type.getMethod("valueOf", value.getClass());
                return type.cast(factory.invoke(null, value));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Could not convert attribute value " + value + " to " + type);
            }
        }
    }

    @Nonnull
    @Override
    public Set<String> attributeNames() {
        return Set.copyOf(attributeValues.keySet());
    }

    @Nonnull
    @Override
    public <T> T attribute(@Nonnull String attributeName, @Nonnull Class<T> attributeType, @Nonnull T defaultValue) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        Objects.requireNonNull(attributeType, "attributeType must not be null");
        Objects.requireNonNull(defaultValue, "defaultValue must not be null");
        return castOrConvert(attributeType, attributeValues.getOrDefault(attributeName, defaultValue));
    }

    @Nonnull
    @Override
    public <T> T attribute(@Nonnull String attributeName, @Nonnull Class<T> attributeType) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        Objects.requireNonNull(attributeType, "attributeType must not be null");
        var value = attributeValues.get(attributeName);
        if (value == null) {
            throw new IllegalArgumentException("No value found for attribute " + attributeName);
        } else {
            return castOrConvert(attributeType, value);
        }
    }

    @Override
    public void setMultiple(@Nonnull Map<?, Object> valueMap) {
        Objects.requireNonNull(valueMap, "valueMap must not be null");
        valueMap.forEach((key, value) -> {
            Objects.requireNonNull(key, "valueMap must not contain null keys");
            Objects.requireNonNull(value, "valueMap must not contain any null values");
            attributeValues.put(key.toString(), value);
        });
        fireEvent(new AttributeChangeEvent(this, valueMap.keySet().stream().map(Object::toString)
                .collect(Collectors.toSet())));
    }

    @Override
    public void setOne(@Nonnull String attributeName, @Nonnull Object value) {
        Objects.requireNonNull(attributeName, "attributeName must not be null");
        Objects.requireNonNull(value, "value must not be null");
        attributeValues.put(attributeName, value);
        fireEvent(new AttributeChangeEvent(this, Set.of(attributeName)));
    }

    private void fireEvent(@Nonnull AttributeChangeEvent event) {
        Set.copyOf(listeners.keySet()).forEach(listener -> {
            try {
                listener.accept(event);
            } catch (Exception ex) {
                log.error("Listener " + listener + " threw exception, ignoring", ex);
            }
        });
    }

    @Nonnull
    @Override
    public ListenerRegistration registerListener(@Nonnull Consumer<AttributeChangeEvent> listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.put(listener, null);
        return () -> listeners.remove(listener);
    }
}
