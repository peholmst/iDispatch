package net.pkhapps.idispatch.domain.support;

import java.io.Serializable;

public interface ConcurrencySafeDomainObject extends Serializable {

    long version();

}
