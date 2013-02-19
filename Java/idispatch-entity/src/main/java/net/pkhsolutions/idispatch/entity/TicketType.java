package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class TicketType extends AbstractEntityWithOptimisticLocking {

    @Column(unique = true, nullable = false)
    @NotNull(message = "Please enter a code for the ticket type")
    private String code;
    @NotNull(message = "Please enter a description for the ticket type")
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

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<TicketType, Builder> {

        public Builder() {
            super(TicketType.class);
        }

        public Builder(TicketType original) {
            super(TicketType.class, original);
            entity.setCode(original.getCode());
            entity.setDescription(original.getDescription());
        }

        public Builder withCode(String code) {
            entity.setCode(code);
            return this;
        }

        public Builder withDescription(String description) {
            entity.setDescription(description);
            return this;
        }
    }
}
