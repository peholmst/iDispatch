package net.pkhapps.idispatch.base.domain;

/**
 * Marker interface for standard types that are coming from another bounded context. Standard types are typically used
 * as lookup or reference data, are immutable outside their originating context and can be clearly identified. Inside
 * the context that defines the standard types, they are typically modeled as aggregate roots.
 */
public interface StandardType<ID> extends IdentifiableDomainObject<ID> {
}
