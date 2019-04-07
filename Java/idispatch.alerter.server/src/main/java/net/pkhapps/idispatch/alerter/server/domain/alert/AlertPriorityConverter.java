package net.pkhapps.idispatch.alerter.server.domain.alert;

import org.springframework.lang.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static net.pkhapps.idispatch.alerter.server.domain.alert.AlertPriority.*;

/**
 * Attribute converter for {@link AlertPriority}.
 */
@Converter(autoApply = true)
public class AlertPriorityConverter implements AttributeConverter<AlertPriority, Integer> {

    @Override
    @Nullable
    public Integer convertToDatabaseColumn(@Nullable AlertPriority attribute) {
        if (attribute == null) {
            return null;
        }
        switch (attribute) {
            case LOW:
                return 0;
            case NORMAL:
                return 1;
            case HIGH:
                return 2;
            default:
                throw new IllegalArgumentException("Unknown priority: " + attribute);
        }
    }

    @Override
    @Nullable
    public AlertPriority convertToEntityAttribute(@Nullable Integer dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case 0:
                return LOW;
            case 1:
                return NORMAL;
            case 2:
                return HIGH;
            default:
                throw new IllegalArgumentException("Unknown priority: " + dbData);
        }
    }
}
