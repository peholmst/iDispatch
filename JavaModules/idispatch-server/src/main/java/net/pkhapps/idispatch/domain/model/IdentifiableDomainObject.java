package net.pkhapps.idispatch.domain.model;

public interface IdentifiableDomainObject<ID extends DomainObjectId> {

    ID id();
}
