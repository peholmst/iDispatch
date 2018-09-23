package net.pkhapps.idispatch.adapter.rest.v3.infrastructure;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * JAX-RS {@link ParamConverterProvider} for {@link DomainObjectIdParamConverter}s.
 */
@Provider
public class DomainObjectIdParamConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (DomainObjectId.class.isAssignableFrom(rawType)) {
            return (ParamConverter<T>) new DomainObjectIdParamConverter<>((Class<? extends DomainObjectId>) rawType);
        }
        return null;
    }
}
