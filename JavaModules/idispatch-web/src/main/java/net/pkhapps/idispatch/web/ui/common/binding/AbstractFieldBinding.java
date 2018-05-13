package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.server.SerializableConsumer;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import net.pkhapps.idispatch.web.ui.common.model.Property;
import net.pkhapps.idispatch.web.ui.common.model.PropertyChangeEvent;
import net.pkhapps.idispatch.web.ui.common.model.WritableProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Base class for a binding that binds a Vaadin {@link HasValue field} and a {@link Property}/{@link WritableProperty}
 * together. In case the field and property have different types, a {@link Converter} is used to convert between
 * the types.
 *
 * @param <FIELDTYPE>    the type of the field.
 * @param <PROPERTYTYPE> the type of the property.
 */
public abstract class AbstractFieldBinding<FIELDTYPE, PROPERTYTYPE> implements Binding {

    private final HasValue<FIELDTYPE> field;
    private final Converter<FIELDTYPE, PROPERTYTYPE> converter;
    private Property<PROPERTYTYPE> property;
    private SerializableConsumer<String> conversionErrorHandler;
    private Registration registration;

    /**
     * Protected constructor. Subclasses should override, but not expose to clients. They should create a fluent API
     * with builder methods and static factory methods for passing in the converter (see the other binder classes in
     * this package for examples).
     *
     * @param field     the field to bind to.
     * @param converter the converter to use when converting back and forth between the field and property types. If the
     *                  types are one and a same, pass in an instance of {@link SelfConverter}.
     */
    protected AbstractFieldBinding(@NonNull HasValue<FIELDTYPE> field,
                                   @NonNull Converter<FIELDTYPE, PROPERTYTYPE> converter) {
        this.field = Objects.requireNonNull(field, "field must not be null");
        this.converter = Objects.requireNonNull(converter, "converter must not be null");
    }

    /**
     * Returns the field that will be bound to the {@link #getProperty() property}.
     */
    @NonNull
    protected HasValue<FIELDTYPE> getField() {
        return field;
    }

    /**
     * Returns the property that the field has been bound to, or null if the binding has not been made yet or has been
     * {@link #unbind() unbound}.
     */
    @Nullable
    protected Property<PROPERTYTYPE> getProperty() {
        return property;
    }

    /**
     * Returns the converter used to convert between the property type and the field type.
     *
     * @see SelfConverter
     */
    @NonNull
    protected Converter<FIELDTYPE, PROPERTYTYPE> getConverter() {
        return converter;
    }

    /**
     * Binds the field to the given property. The binding will be unidirectional with the field marked as read only.
     *
     * @param property the property to bind to.
     * @throws IllegalStateException if this binding has already been bound to a property.
     * @see #bind(WritableProperty)
     * @see #bind(WritableProperty, SerializableConsumer)
     * @see #unbind()
     */
    public void bind(@NonNull Property<PROPERTYTYPE> property) {
        setPropertyIfNotAlreadyBound(property);
        field.setReadOnly(true);
        registration = property.addPropertyListenerAndFireEvent(this::onPropertyChangeEvent);
    }

    /**
     * Binds the field to the given property. The binding will be bidirectional with the field's read only flag matching
     * the property's {@link WritableProperty#isWritable() writable} flag.
     *
     * @param property the property to bind to.
     * @throws IllegalStateException if this binding has already been bound to a property.
     * @see #bind(Property)
     * @see #bind(WritableProperty, SerializableConsumer)
     * @see #unbind()
     */
    public void bind(@NonNull WritableProperty<PROPERTYTYPE> property) {
        bind(property, null);
    }

    /**
     * Binds the field to the given property. The binding will be bidirectional with the field's read only flag matching
     * the property's {@link WritableProperty#isWritable() writable} flag. Any conversion errors while converting from
     * the field type to the property type will be handled by the given conversion error handler.
     *
     * @param property               the property to bind to.
     * @param conversionErrorHandler the conversion error handler, or null to ignore conversion errors.
     * @throws IllegalStateException if this binding has already been bound to a property.
     * @see #bind(WritableProperty)
     * @see #bind(Property)
     * @see #unbind()
     */
    public void bind(@NonNull WritableProperty<PROPERTYTYPE> property,
                     @Nullable SerializableConsumer<String> conversionErrorHandler) {
        setPropertyIfNotAlreadyBound(property);
        this.conversionErrorHandler = conversionErrorHandler;

        var propertyRegistration = property.addPropertyListenerAndFireEvent(this::onPropertyChangeEvent);
        var canWriteRegistration = property.isWritable()
                .addPropertyListenerAndFireEvent(this::onPropertyCanWriteChangeEvent);
        var fieldRegistration = field.addValueChangeListener(this::onFieldValueChangeEvent);
        registration = (Registration) () -> {
            propertyRegistration.remove();
            canWriteRegistration.remove();
            fieldRegistration.remove();
        };
    }

    private void setPropertyIfNotAlreadyBound(@NonNull Property<PROPERTYTYPE> property) {
        if (this.property != null) {
            throw new IllegalStateException("This field has already been bound to a property");
        }
        this.property = Objects.requireNonNull(property, "property must not be null");
    }

    /**
     * Unbinds this binding from the property. The binding can be rebound by invoking any of the
     * {@code bind(...)} methods.
     *
     * @see #bind(Property)
     * @see #bind(WritableProperty)
     * @see #bind(WritableProperty, SerializableConsumer)
     */
    @Override
    public void unbind() {
        if (registration != null) {
            registration.remove();
            registration = null;
            property = null;
        }
    }

    private void onPropertyChangeEvent(@NonNull PropertyChangeEvent<PROPERTYTYPE> event) {
        var presentation = converter.convertToPresentation(event.getValue(), createValueContext());
        field.setValue(presentation);
    }

    private void onPropertyCanWriteChangeEvent(@NonNull PropertyChangeEvent<Boolean> event) {
        field.setReadOnly(!event.getValue());
    }

    private void onFieldValueChangeEvent(@NonNull HasValue.ValueChangeEvent<FIELDTYPE> event) {
        var model = converter.convertToModel(event.getValue(), createValueContext());
        model.handle(this::onConverterSuccess, this::setConversionErrorMessage);
    }

    private void onConverterSuccess(@Nullable PROPERTYTYPE value) {
        if (property instanceof WritableProperty) {
            ((WritableProperty<PROPERTYTYPE>) property).setValue(value);
            setConversionErrorMessage(null);
        } else {
            throw new IllegalStateException("property is not an instance of WritableProperty");
        }
    }

    private void setConversionErrorMessage(@Nullable String errorMessage) {
        if (conversionErrorHandler != null) {
            conversionErrorHandler.accept(errorMessage);
        }
    }

    /**
     * Creates a new {@link ValueContext} to use when invoking the {@link #getConverter() converter}. If the field is
     * a {@link Component}, it will be passed to the {@link ValueContext#ValueContext(Component)} constructor.
     * Otherwise, an empty context is created.
     */
    @NonNull
    protected ValueContext createValueContext() {
        if (field instanceof Component) {
            return new ValueContext((Component) field);
        } else {
            return new ValueContext();
        }
    }

    /**
     * An implementation of {@link Converter} that just uses the same type for model and presentation. This is used
     * by {@link AbstractFieldBinding} to allow for a consistent use of converters even in cases where no conversion is
     * needed (i.e. the property and field types are one and a same).
     */
    protected static class SelfConverter<T> implements Converter<T, T> {

        @Override
        public Result<T> convertToModel(T value, ValueContext context) {
            return Result.ok(value);
        }

        @Override
        public T convertToPresentation(T value, ValueContext context) {
            return value;
        }
    }
}
