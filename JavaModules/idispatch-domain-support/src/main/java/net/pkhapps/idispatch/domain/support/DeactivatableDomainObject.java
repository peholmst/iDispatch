package net.pkhapps.idispatch.domain.support;

@SuppressWarnings("SpellCheckingInspection")
public interface DeactivatableDomainObject {

    boolean active();

    default boolean inactive() {
        return !active();
    }

    void deactivate();

    void activate();
}
