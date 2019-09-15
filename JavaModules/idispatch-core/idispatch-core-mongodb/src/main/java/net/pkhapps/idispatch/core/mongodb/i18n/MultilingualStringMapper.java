package net.pkhapps.idispatch.core.mongodb.i18n;

import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import org.bson.Document;
import org.jetbrains.annotations.Contract;

/**
 * TODO Document me!
 */
public class MultilingualStringMapper {

    @Contract("null -> null")
    public Document toDocument(MultilingualString multilingualString) {
        if (multilingualString == null) {
            return null;
        }
        var document = new Document();
        multilingualString.toMap().forEach((locale, value) -> document.put(locale.toLanguageTag(), value));
        return document;
    }
}
