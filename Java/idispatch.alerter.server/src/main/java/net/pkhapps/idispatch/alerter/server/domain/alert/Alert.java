package net.pkhapps.idispatch.alerter.server.domain.alert;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.pkhapps.idispatch.alerter.server.domain.DbConstants;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.base.domain.AggregateRoot;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.alerter.server.domain.ValidationUtils.requireNonBlankAndMaxLength;

/**
 * Alert that is sent to different recipients as a JSON message. The recipients are expected to know what to do with it.
 */
@Entity
@Table(schema = DbConstants.SCHEMA_NAME, name = "alert")
@Getter
public class Alert extends AggregateRoot<AlertId> {

    @Column(name = "priority")
    private AlertPriority priority; // No need for @Enumerated, we have a converter

    @Column(name = "alert_date")
    private Instant alertDate;

    @ElementCollection
    @CollectionTable(schema = DbConstants.SCHEMA_NAME, name = "alert_recipient", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "recipient_id")
    private Set<RecipientId> recipients;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content")
    @Type(type = "JsonType")
    private JsonNode content;

    protected Alert() {
        super(AlertIdConverter.INSTANCE);
    }

    public Alert(AlertPriority priority, Instant alertDate, String contentType, JsonNode content,
                 Collection<RecipientId> recipients) {
        this();
        setPriority(priority);
        setAlertDate(alertDate);
        setContentType(contentType);
        setContent(content);
        setRecipients(recipients);
    }

    private void setPriority(AlertPriority priority) {
        this.priority = requireNonNull(priority);
    }

    private void setAlertDate(Instant alertDate) {
        this.alertDate = requireNonNull(alertDate);
    }

    private void setRecipients(Collection<RecipientId> recipients) {
        if (requireNonNull(recipients).isEmpty()) {
            throw new IllegalArgumentException("Must have at least one recipient");
        }
        this.recipients = new HashSet<>(recipients);
    }

    private void setContentType(String contentType) {
        this.contentType = requireNonBlankAndMaxLength(contentType, 200);
    }

    public JsonNode getContent() {
        return content.deepCopy();
    }

    private void setContent(JsonNode content) {
        this.content = requireNonNull(content).deepCopy();
    }
}
