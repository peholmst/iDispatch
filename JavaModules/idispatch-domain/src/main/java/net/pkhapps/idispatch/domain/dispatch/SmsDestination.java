package net.pkhapps.idispatch.domain.dispatch;

import net.pkhapps.idispatch.domain.common.PhoneNumber;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Document me!
 */
@Entity
@Table(name = "sms_destinations")
public class SmsDestination extends Destination {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sms_destination_phone_numbers")
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    @NonNull
    public Set<PhoneNumber> getPhoneNumbers() {
        return Collections.unmodifiableSet(phoneNumbers);
    }
}
