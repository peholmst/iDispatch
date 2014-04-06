package net.pkhsolutions.idispatch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.RandomStringUtils;

@Entity
public class DispatchNotificationReceiver extends AbstractEntityWithOptimisticLocking {

    @Column(nullable = false, unique = true)
    @NotNull(message = "Please enter an ID for the receiver")
    private String receiverId;
    @Column(nullable = false)
    @NotNull(message = "Please enter or generate a security code for the receiver")
    private String securityCode;
    @Column(nullable = false)
    private boolean active = true;

    protected DispatchNotificationReceiver() {
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static final class Builder extends AbstractEntityWithOptimisticLockingBuilder<DispatchNotificationReceiver, Builder> {

        public Builder() {
            super(DispatchNotificationReceiver.class);
        }

        public Builder(DispatchNotificationReceiver original) {
            super(DispatchNotificationReceiver.class, original);
            entity.setReceiverId(original.getReceiverId());
            entity.setSecurityCode(original.getSecurityCode());
            entity.setActive(original.isActive());
        }

        public Builder withReceiverId(String receiverId) {
            entity.setReceiverId(receiverId);
            return this;
        }

        public Builder withSecurityCode(String securityCode) {
            entity.setSecurityCode(securityCode);
            return this;
        }

        public Builder withGeneratedSecurityCode() {
            entity.setSecurityCode(RandomStringUtils.randomAlphanumeric(15));
            return this;
        }

        public Builder active() {
            entity.setActive(true);
            return this;
        }

        public Builder inactive() {
            entity.setActive(false);
            return this;
        }
    }
}
