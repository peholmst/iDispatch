package net.pkhapps.idispatch.client.v3.base.retrofit;

import net.pkhapps.idispatch.client.v3.base.DomainObjectId;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Retrofit converter factory that makes it possible to use {@link DomainObjectId}s as parameters for REST calls.
 */
public class DomainObjectIdConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class && DomainObjectId.class.isAssignableFrom((Class<?>) type)) {
            return (Converter<DomainObjectId, String>) value -> String.valueOf(value.toLong());
        }
        return super.stringConverter(type, annotations, retrofit);
    }
}
