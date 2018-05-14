package net.pkhapps.idispatch.web.ui.common.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 * Base class for {@link Converter}s that only convert the model value to a string, but not the other way around.
 */
public abstract class OneWayToStringConverter<MODEL> implements Converter<String, MODEL> {

    @Override
    public final Result<MODEL> convertToModel(String value, ValueContext context) {
        return Result.error("This is a one-way converter");
    }
}
