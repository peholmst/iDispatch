package net.pkhapps.idispatch.cad.infrastructure.jooq.converter;

import net.pkhapps.idispatch.application.support.infrastructure.jooq.converter.DomainObjectIdConverter;
import net.pkhapps.idispatch.cad.domain.model.municipality.MunicipalityId;

import javax.annotation.concurrent.Immutable;

/**
 * JOOQ converter for {@link MunicipalityId}, assuming the ID is persisted as an integer.
 */
@Immutable
public class MunicipalityIdConverter extends DomainObjectIdConverter<MunicipalityId, Integer> {

    public MunicipalityIdConverter() {
        super(MunicipalityId.class, Integer.class, MunicipalityId::new);
    }
}
