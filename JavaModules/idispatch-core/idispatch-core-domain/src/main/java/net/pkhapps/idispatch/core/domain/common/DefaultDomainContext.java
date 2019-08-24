package net.pkhapps.idispatch.core.domain.common;

import java.time.Clock;

class DefaultDomainContext implements DomainContext {

    private final Clock clock = Clock.systemUTC();

    @Override
    public Clock clock() {
        return clock;
    }
}
