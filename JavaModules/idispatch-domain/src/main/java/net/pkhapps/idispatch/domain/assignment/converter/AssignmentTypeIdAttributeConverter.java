package net.pkhapps.idispatch.domain.assignment.converter;

import net.pkhapps.idispatch.domain.assignment.AssignmentTypeId;
import net.pkhapps.idispatch.domain.base.converter.AbstractAggregateRootIdAttributeConverter;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link AssignmentTypeId}.
 */
@Converter(autoApply = true)
public class AssignmentTypeIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<AssignmentTypeId> {

    public AssignmentTypeIdAttributeConverter() {
        super(AssignmentTypeId.class);
    }
}
