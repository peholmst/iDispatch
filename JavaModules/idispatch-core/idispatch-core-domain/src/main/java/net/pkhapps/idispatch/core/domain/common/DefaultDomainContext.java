package net.pkhapps.idispatch.core.domain.common;

import java.time.Clock;

public class DefaultDomainContext implements DomainContext {

    private final Clock clock = Clock.systemUTC();

    @Override
    public Clock clock() {
        return clock;
    }
}
