package net.pkhapps.idispatch.alerter.server.domain.recipient;

import lombok.EqualsAndHashCode;
import net.pkhapps.idispatch.base.domain.ValueObject;

import static net.pkhapps.idispatch.alerter.server.domain.ValidationUtils.requireNonBlankAndMaxLength;

/**
 * Value object representing a resource code (like a call sign).
 */
@EqualsAndHashCode
public class ResourceCode implements ValueObject {

    private final String code;

    public ResourceCode(String code) {
        this.code = requireNonBlankAndMaxLength(code, 50);
    }

    @Override
    public String toString() {
        return code;
    }
}
