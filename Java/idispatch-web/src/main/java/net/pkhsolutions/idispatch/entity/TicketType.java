package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class TicketType extends AbstractEntityWithOptimisticLocking {

    @Column(unique = true, nullable = false)
    @NotNull(message = "Please enter a code for the ticket type")
    private String code;
    @NotNull(message = "Please enter a Finnish description for the ticket type")
    private String descriptionFi;
    @NotNull(message = "Please enter a Swedish description for the ticket type")
    private String descriptionSv;

    protected TicketType() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescriptionFi() {
        return descriptionFi;
    }

    public void setDescriptionFi(String descriptionFi) {
        this.descriptionFi = descriptionFi;
    }

    public String getDescriptionSv() {
        return descriptionSv;
    }

    public void setDescriptionSv(String descriptionSv) {
        this.descriptionSv = descriptionSv;
    }

    public String getFormattedDescriptionFi() {
        return String.format("%s - %s", code, descriptionFi);
    }

    public String getFormattedDescriptionSv() {
        return String.format("%s - %s", code, descriptionSv);
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<TicketType, Builder> {

        public Builder() {
            super(TicketType.class);
        }

        public Builder(TicketType original) {
            super(TicketType.class, original);
            entity.setCode(original.getCode());
            entity.setDescriptionFi(original.getDescriptionFi());
            entity.setDescriptionSv(original.getDescriptionSv());
        }

        public Builder withCode(String code) {
            entity.setCode(code);
            return this;
        }

        public Builder withFinnishDescription(String description) {
            entity.setDescriptionFi(description);
            return this;
        }

        public Builder withSwedishDescription(String description) {
            entity.setDescriptionSv(description);
            return this;
        }
    }
}
