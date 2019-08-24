package net.pkhapps.idispatch.core.domain.resource.model;

import net.pkhapps.idispatch.core.domain.common.ValueObject;

public class CallSign implements ValueObject {

    public static CallSign createFromString(String callSign) {
        return new CallSign();
    }
}
