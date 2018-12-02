package net.pkhapps.idispatch.shared.domain.base;

/**
 * TODO Document me!
 */
@SuppressWarnings("SpellCheckingInspection")
public interface DeactivatableDomainObject extends DomainObject {

    boolean isActive();

    void deactivate();

    void reactivate();
}
