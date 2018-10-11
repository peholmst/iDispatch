package net.pkhapps.idispatch.client.v3.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value object that represents a phone number.
 */
@Immutable
@SuppressWarnings("WeakerAccess")
public class PhoneNumber implements Serializable {

    /**
     * This software is designed to be used in Finland so it makes sense to hardcode the Finnish country code here.
     */
    private static final String DEFAULT_COUNTRY_CODE = "+358";
    private String number;

    public PhoneNumber(@Nonnull String phoneNumber) {
        setPhoneNumber(phoneNumber);
    }

    private void setPhoneNumber(@Nonnull String phoneNumber) {
        Objects.requireNonNull(phoneNumber, "phoneNumber must not be null");

        if (!phoneNumber.matches("\\+?[\\d().,\\-\\s]+")) {
            throw new IllegalArgumentException(phoneNumber + " is not a valid phone number");
        }

        var sb = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); ++i) {
            var chr = phoneNumber.charAt(i);
            if (chr == '+' || Character.isDigit(chr)) {
                sb.append(chr);
            }
        }
        this.number = sb.toString();
    }

    @Nonnull
    public String toE164() {
        if (number.charAt(0) == '0') {
            return DEFAULT_COUNTRY_CODE + number.substring(1);
        }
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), number);
    }
}
