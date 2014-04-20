package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static net.pkhsolutions.idispatch.utils.StringUtils.nullToEmpty;

/**
 * Entity representing a ticket type.
 */
@Entity
@Table(name = "ticket_types")
public class TicketType extends AbstractLockableEntity {

    @Column(name = "code", unique = true, nullable = false)
    private String code = "";
    @Column(name = "description", nullable = false)
    private String description = "";

    protected TicketType() {
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedDescription() {
        return String.format("%s - %s", code, description);
    }

    /**
     * Builder for creating instances of {@link TicketType}.
     */
    public static final class Builder extends AbstractLockableEntityBuilder<TicketType, Builder> {

        public Builder() {
            super(TicketType.class);
        }

        public Builder(TicketType original) {
            super(TicketType.class, original);
            entity.code = original.code;
            entity.description = original.description;
        }

        public Builder withCode(String code) {
            entity.code = nullToEmpty(code);
            return this;
        }

        public Builder withDescription(String description) {
            entity.description = nullToEmpty(description);
            return this;
        }
    }
}
