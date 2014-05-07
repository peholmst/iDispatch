package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a resource type.
 */
@Entity
@Table(name = "resource_types")
public class ResourceType extends AbstractLockableEntity {

    public static final String PROP_CODE = "code";
    public static final String PROP_DESCRIPTION = "description";

    @Column(name = "code", unique = true, nullable = false)
    private String code = "";
    @Column(name = "description", nullable = false)
    private String description;

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
}
