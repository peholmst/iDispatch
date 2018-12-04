package net.pkhapps.idispatch.gis.domain.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;

/**
 * TODO Document me!
 *
 * @param <T>
 */
public interface Code<T> {


    @NonNull
    static <T, E extends Enum<E> & Code<T>> E findByCode(@NonNull Class<E> enumType, @NonNull T code) {
        for (E enumConstant : enumType.getEnumConstants()) {
            if (enumConstant.code().equals(code)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    @NotNull T code();
}
