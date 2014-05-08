package net.pkhsolutions.idispatch.entity;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Entity representing a assignment type.
 */
@Entity
@Table(name = "assignment_types")
public class AssignmentType extends AbstractLockableEntity {

    public static final String PROP_CODE = "code";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_FORMATTED_DESCRIPTION = "formattedDescription";

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add(PROP_ID, getId())
                .add(PROP_VERSION, getVersion())
                .add(PROP_CODE, code)
                .toString();
    }
}
