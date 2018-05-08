package net.pkhapps.idispatch.domain.common;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object representing a phone number.
 */
public class PhoneNumber implements Serializable {

    private final String phoneNumber;

    public PhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = Objects.requireNonNull(phoneNumber);
    }

    // TODO Validation


    @Nullable
    public static PhoneNumber fromString(@Nullable String phoneNumber) {
        return phoneNumber == null ? null : new PhoneNumber(phoneNumber);
    }

    @Override
    public String toString() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (PhoneNumber) o;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }
}
