package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource type.
 */
@Entity
@Table(name = "resource_types")
public class ResourceType extends AbstractLockableEntity implements Deactivatable {

    public static final String PROP_CODE = "code";
    public static final String PROP_DESCRIPTION = "description";

    @Column(name = "code", unique = true, nullable = false)
    @NotBlank(message = "Please enter a code")
    private String code = "";
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Please enter a description")
    private String description = "";
    @Column(name = "active", nullable = false)
    private boolean active = true;

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(PROP_ID, getId())
                .add(PROP_VERSION, getVersion())
                .add(PROP_CODE, code)
                .toString();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
