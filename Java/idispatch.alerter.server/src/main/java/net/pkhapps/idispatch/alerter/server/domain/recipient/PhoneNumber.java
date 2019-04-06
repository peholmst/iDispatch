package net.pkhapps.idispatch.alerter.server.domain.recipient;

import lombok.EqualsAndHashCode;
import net.pkhapps.idispatch.base.domain.ValueObject;

import java.util.regex.Pattern;

import static net.pkhapps.idispatch.alerter.server.domain.ValidationUtils.requireNonBlankAndMaxLength;

/**
 * Phone number in a format suitable for use with SMS (+1234567890).
 */
@EqualsAndHashCode
public class PhoneNumber implements ValueObject {

    private static final Pattern PATTERN = Pattern.compile("^\\+\\d+$");
    private final String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        requireNonBlankAndMaxLength(phoneNumber, 20);
        if (!PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }
}
