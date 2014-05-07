package net.pkhsolutions.idispatch.entity;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Document me!
 */
@Entity
@Table(name = "sms_destinations")
public class SmsDestination extends Destination {

    @ElementCollection
    @CollectionTable(name = "sms_destination_phone_numbers")
    private Set<String> phoneNumbers = new HashSet<>();

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
