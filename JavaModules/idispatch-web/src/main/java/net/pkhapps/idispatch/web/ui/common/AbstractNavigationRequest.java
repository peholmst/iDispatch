package net.pkhapps.idispatch.web.ui.common;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * TODO Document me
 */
public abstract class AbstractNavigationRequest {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, ParameterDescriptor> parameterDescriptors = new HashMap<>();
    private final Map<String, Object> parameterValues = new HashMap<>();

    protected AbstractNavigationRequest() {
    }

    @SuppressWarnings("unchecked")
    protected final void readParametersFromEvent(@NonNull ViewChangeListener.ViewChangeEvent event) {
        parameterDescriptors.forEach((parameterName, descriptor) -> {
            String parameterValue = event.getParameterMap().get(parameterName);
            if (parameterValue != null) {
                try {
                    setParameter(parameterName, descriptor.fromStringConverter.apply(parameterValue));
                } catch (Exception ex) {
                    logger.error("Error converting parameter value from string", ex);
                    setParameter(parameterName, null);
                }
            } else {
                setParameter(parameterName, null);
            }
        });
    }

    protected final <T> void registerParameter(@NonNull String parameterName, @NonNull Class<T> parameterType,
                                               @NonNull Function<T, String> toStringConverter,
                                               @NonNull Function<String, T> fromStringConverter) {
        Objects.requireNonNull(parameterName);
        ParameterDescriptor<T> descriptor = new ParameterDescriptor<>(toStringConverter, fromStringConverter);
        parameterDescriptors.put(parameterName, descriptor);
    }

    protected final void setParameter(@NonNull String parameterName, @Nullable Object parameterValue) {
        Objects.requireNonNull(parameterName);
        if (parameterValue == null) {
            parameterValues.remove(parameterName);
        } else {
            parameterValues.put(parameterName, parameterValue);
        }
    }

    @Nullable
    protected final <T> T getParameter(@NonNull String parameterName, @NonNull Class<T> parameterType) {
        Objects.requireNonNull(parameterName);
        try {
            return parameterType.cast(parameterValues.get(parameterName));
        } catch (ClassCastException ex) {
            logger.error("Parameter value had the wrong type", ex);
            return null;
        }
    }

    @NonNull
    protected abstract String getViewName();

    @SuppressWarnings("unchecked")
    public void navigate(@NonNull Navigator navigator) {
        Set<String> parameterStrings = new HashSet<>();
        parameterDescriptors.forEach((parameterName, descriptor) -> {
            Object parameterValue = parameterValues.get(parameterName);
            if (parameterValue != null) {
                try {
                    parameterStrings.add(String.format("%s=%s", parameterName,
                            descriptor.toStringConverter.apply(parameterValue)));
                } catch (Exception ex) {
                    logger.error("Error converting parameter value to string", ex);
                    // Act as if this parameter does not exist
                }
            }
        });
        if (parameterStrings.isEmpty()) {
            navigator.navigateTo(getViewName());
        } else {
            navigator.navigateTo(getViewName() + "/" + String.join(",", parameterStrings));
        }
    }

    private class ParameterDescriptor<T> {
        private final Function<T, String> toStringConverter;
        private final Function<String, T> fromStringConverter;

        ParameterDescriptor(@NonNull Function<T, String> toStringConverter,
                            @NonNull Function<String, T> fromStringConverter) {
            this.toStringConverter = Objects.requireNonNull(toStringConverter);
            this.fromStringConverter = Objects.requireNonNull(fromStringConverter);
        }
    }
}
