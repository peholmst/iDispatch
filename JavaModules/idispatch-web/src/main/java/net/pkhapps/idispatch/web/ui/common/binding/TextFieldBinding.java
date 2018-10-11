package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.AbstractTextField;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Binder that binds an {@link AbstractTextField} to a property.
 */
public class TextFieldBinding<MODEL> extends AbstractFieldBinding<String, MODEL> {

    private TextFieldBinding(@NonNull AbstractTextField field, @NonNull Converter<String, MODEL> converter) {
        super(field, converter);
    }

    /**
     * Creates a new binding for the given text field.
     */
    @NonNull
    public static TextFieldBinding<String> forField(@NonNull AbstractTextField field) {
        return new TextFieldBinding<>(field, new NullAwareStringConverter(field.getEmptyValue()));
    }

    /**
     * Creates a new text field binding based on this binding with the given converter. Use this method when you need
     * to bind a property whose type is not {@code String}.
     */
    @NonNull
    public <D> TextFieldBinding<D> withConverter(@NonNull Converter<MODEL, D> converter) {
        Objects.requireNonNull(converter, "converter must not be null");
        return new TextFieldBinding<>(getField(), getConverter().chain(converter));
    }

    @Override
    protected AbstractTextField getField() {
        return (AbstractTextField) super.getField();
    }

    private static class NullAwareStringConverter implements Converter<String, String> {

        private final String nullRepresentation;

        private NullAwareStringConverter(String nullRepresentation) {
            this.nullRepresentation = nullRepresentation;
        }

        @Override
        public Result<String> convertToModel(String value, ValueContext context) {
            return Result.ok(Objects.equals(nullRepresentation, value) ? null : value);
        }

        @Override
        public String convertToPresentation(String value, ValueContext context) {
            return value == null ? nullRepresentation : value;
        }
    }
}
