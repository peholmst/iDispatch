package net.pkhapps.idispatch.shared.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.idispatch.shared.domain.base.ValueObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO Document me
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultilingualString implements ValueObject {

    public static final String ATTRIBUTE_FIN = "fin";
    public static final String ATTRIBUTE_SWE = "swe";
    public static final String ATTRIBUTE_ENG = "eng";

    // TODO Replace with map
    @JsonProperty("fin")
    private String fin;
    @JsonProperty("swe")
    private String swe;
    @JsonProperty("eng")
    private String eng;

    /**
     * Creates an empty {@code MultilingualString}.
     */
    public MultilingualString() {
    }

    public MultilingualString(@NotNull Map<Language, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        fin = values.get(Language.FINNISH);
        swe = values.get(Language.SWEDISH);
        eng = values.get(Language.ENGLISH);
    }

    // TODO Create builder

    /**
     * Returns the string contents in the given {@code language}.
     */
    @NotNull
    public Optional<String> get(@NotNull Language language) {
        switch (language) {
            case FINNISH:
                return Optional.ofNullable(fin);
            case SWEDISH:
                return Optional.ofNullable(swe);
            case ENGLISH:
                return Optional.ofNullable(eng);
        }
        return Optional.empty();
    }

    @NotNull
    public Map<Language, String> toMap() {
        return Map.of(Language.FINNISH, fin, Language.SWEDISH, swe, Language.ENGLISH, eng);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultilingualString that = (MultilingualString) o;
        return Objects.equals(fin, that.fin) &&
                Objects.equals(swe, that.swe) &&
                Objects.equals(eng, that.eng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fin, swe, eng);
    }

    public static class Builder {

        public Builder() {

        }

        public Builder(int maxLength) {

        }

        public Builder with(Language language, String string) {
            return this;
        }

        public MultilingualString build() {
            return new MultilingualString();
        }

        public Optional<MultilingualString> buildOptional() {
            return Optional.empty();
        }
    }
}
