package net.pkhapps.idispatch.alerter.server.domain.recipient;

import net.pkhapps.idispatch.alerter.server.domain.DbConstants;
import net.pkhapps.idispatch.base.domain.OrganizationId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Recipient that forwards the alerts using an SMS gateway.
 */
@Entity
@DiscriminatorValue("SMS")
public class SmsRecipient extends Recipient<SmsRecipient> {

    @ElementCollection
    @CollectionTable(schema = DbConstants.SCHEMA_NAME, name = "recipient_sms",
            joinColumns = @JoinColumn(name = "recipient_id"))
    @Column(name = "phone_number")
    private Set<PhoneNumber> phoneNumbers;

    protected SmsRecipient() {
    }

    public SmsRecipient(String description, OrganizationId organization) {
        super(description, organization);
        phoneNumbers = new HashSet<>();
    }

    public SmsRecipient addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.add(requireNonNull(phoneNumber));
        return this;
    }

    public SmsRecipient removePhoneNumber(PhoneNumber phoneNumber) {
        phoneNumbers.remove(requireNonNull(phoneNumber));
        return this;
    }
}
