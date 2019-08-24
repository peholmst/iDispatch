package net.pkhapps.idispatch.core.domain.organization;

public interface OrganizationSpecificObject {

    /**
     * Returns the organization that owns this object (never {@code null}).
     */
    OrganizationId owningOrganization();
}
