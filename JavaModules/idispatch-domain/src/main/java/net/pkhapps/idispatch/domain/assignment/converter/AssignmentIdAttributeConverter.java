package net.pkhapps.idispatch.domain.assignment.converter;

import net.pkhapps.idispatch.domain.assignment.AssignmentId;
import net.pkhapps.idispatch.domain.base.converter.AbstractAggregateRootIdAttributeConverter;

import javax.persistence.Converter;

/**
 * Attribute converter for {@link AssignmentId}.
 */
@Converter(autoApply = true)
public class AssignmentIdAttributeConverter extends AbstractAggregateRootIdAttributeConverter<AssignmentId> {

    public AssignmentIdAttributeConverter() {
        super(AssignmentId.class);
    }
}
