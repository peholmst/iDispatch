package net.pkhapps.idispatch.alerter.server.domain.alert;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import net.pkhapps.idispatch.alerter.server.domain.DbConstants;
import net.pkhapps.idispatch.alerter.server.domain.recipient.RecipientId;
import net.pkhapps.idispatch.alerter.server.domain.recipient.ResourceCode;
import net.pkhapps.idispatch.base.domain.AggregateRoot;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Enumerated(EnumType.ORDINAL)
    private AlertPriority priority;

    @Column(name = "alert_date")
    private Instant alertDate;

    @ElementCollection
    @CollectionTable(schema = DbConstants.SCHEMA_NAME, name = "alert_recipient", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "recipient_id")
    private Set<RecipientId> recipients;

    @ElementCollection
    @CollectionTable(schema = DbConstants.SCHEMA_NAME, name = "alert_resource", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "resource_code")
    private Set<ResourceCode> resources;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "content")
    @Type(type = "JsonType")
    private JsonNode content;

    protected Alert() {
        super(AlertIdConverter.INSTANCE);
    }

    public Alert(AlertPriority priority, Instant alertDate, String contentType, JsonNode content,
                 Map<ResourceCode, Collection<RecipientId>> recipients) {
        this();
        setPriority(priority);
        setAlertDate(alertDate);
        setContentType(contentType);
        setContent(content);
        setResources(recipients.keySet().stream());
        setRecipients(recipients.values().stream().flatMap(Collection::stream));
        registerEvent(new AlertSentEvent(this));
    }

    private void setPriority(AlertPriority priority) {
        this.priority = requireNonNull(priority);
    }

    private void setAlertDate(Instant alertDate) {
        this.alertDate = requireNonNull(alertDate);
    }

    private void setResources(Stream<ResourceCode> resources) {
        var set = requireNonNull(resources).collect(Collectors.toSet());
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Must have a least one resource");
        }
        this.resources = set;
    }

    private void setRecipients(Stream<RecipientId> recipients) {
        var set = requireNonNull(recipients).collect(Collectors.toSet());
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one recipient");
        }
        this.recipients = set;
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
