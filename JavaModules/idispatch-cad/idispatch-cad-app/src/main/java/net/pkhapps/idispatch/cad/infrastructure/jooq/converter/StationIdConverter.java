package net.pkhapps.idispatch.cad.infrastructure.jooq.converter;

import net.pkhapps.idispatch.application.support.infrastructure.jooq.converter.DomainObjectIdConverter;
import net.pkhapps.idispatch.cad.domain.model.station.StationId;

import javax.annotation.concurrent.Immutable;

/**
 * JOOQ converter for {@link StationId}, assuming the ID is persisted as a string.
 */
@Immutable
public class StationIdConverter extends DomainObjectIdConverter<StationId, String> {

    public StationIdConverter() {
        super(StationId.class, String.class, StationId::new);
    }
}
