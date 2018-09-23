package net.pkhapps.idispatch.client.v3.infrastructure;

import com.google.gson.GsonBuilder;
import net.pkhapps.idispatch.client.v3.AssignmentId;
import net.pkhapps.idispatch.client.v3.type.*;
import net.pkhapps.idispatch.client.v3.util.Color;
import net.pkhapps.idispatch.client.v3.util.MultilingualString;
import net.pkhapps.idispatch.client.v3.util.PhoneNumber;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Objects;

/**
 * Utility class for configuring a {@link com.google.gson.Gson}-instance through its {@link GsonBuilder}. This
 * configurer will register all the type adapters needed by the iDispatch Client module, among other things.
 */
public class GsonConfigurer {

    @Nonnull
    public GsonBuilder configure(@Nonnull GsonBuilder gsonBuilder) {
        Objects.requireNonNull(gsonBuilder, "gsonBuilder must not be null");
        return gsonBuilder
                .registerTypeAdapter(AssignmentId.class, new DomainObjectIdJsonTypeAdapter<>(AssignmentId::new))
                .registerTypeAdapter(AssignmentStateId.class, new DomainObjectIdJsonTypeAdapter<>(AssignmentStateId::new))
                .registerTypeAdapter(AssignmentTypeId.class, new DomainObjectIdJsonTypeAdapter<>(AssignmentTypeId::new))
                .registerTypeAdapter(Color.class, new ColorJsonTypeAdapter())
                .registerTypeAdapter(Instant.class, new InstantJsonTypeAdapter())
                .registerTypeAdapter(MultilingualString.class, new MultilingualStringTypeAdapter())
                .registerTypeAdapter(MunicipalityId.class, new DomainObjectIdJsonTypeAdapter<>(MunicipalityId::new))
                .registerTypeAdapter(PhoneNumber.class, new PhoneNumberJsonTypeAdapter())
                .registerTypeAdapter(ResourceId.class, new DomainObjectIdJsonTypeAdapter<>(ResourceId::new))
                .registerTypeAdapter(ResourceStateId.class, new DomainObjectIdJsonTypeAdapter<>(ResourceStateId::new))
                .registerTypeAdapter(ResourceTypeId.class, new DomainObjectIdJsonTypeAdapter<>(ResourceTypeId::new))
                .registerTypeAdapter(StationId.class, new DomainObjectIdJsonTypeAdapter<>(StationId::new));

    }
}
