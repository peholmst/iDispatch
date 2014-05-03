package net.pkhsolutions.idispatch.domain.tickets;

import net.pkhsolutions.idispatch.domain.AbstractLockableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a ticket type.
 */
@Entity
@Table(name = "ticket_types")
public class TicketType extends AbstractLockableEntity {

    public static final String PROP_CODE = "code";
    public static final String PROP_DESCRIPTION = "description";

    @Column(name = "code", unique = true, nullable = false)
    private String code = "";
    @Column(name = "description", nullable = false)
    private String description = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = nullToEmpty(code);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = nullToEmpty(description);
    }

    public String getFormattedDescription() {
        return String.format("%s - %s", code, description);
    }
}
