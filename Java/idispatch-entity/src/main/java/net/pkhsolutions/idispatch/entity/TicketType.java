package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TicketType extends AbstractEntityWithOptimisticLocking {

    @Column(unique = true, nullable = false)
    private String code;
    private String description;

    protected TicketType() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {
        private TicketType ticketType;

        public Builder() {
            ticketType = new TicketType();
        }

        public Builder withCode(String code) {
            ticketType.setCode(code);
            return this;
        }

        public Builder withDescription(String description) {
            ticketType.setDescription(description);
            return this;
        }

        public TicketType build() {
            // TODO Validate the fields
            return ticketType;
        }
    }
}
