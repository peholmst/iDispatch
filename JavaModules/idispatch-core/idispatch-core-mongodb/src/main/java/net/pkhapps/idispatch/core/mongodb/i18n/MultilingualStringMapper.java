package net.pkhapps.idispatch.core.mongodb.i18n;

import net.pkhapps.idispatch.core.domain.i18n.MultilingualString;
import org.bson.Document;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Locale;

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

    @Contract("null -> null")
    public MultilingualString toMultilingualString(Document document) {
        if (document == null || document.isEmpty()) {
            return null;
        }
        var map = new HashMap<Locale, String>(document.size());
        document.forEach((key, value) -> map.put(Locale.forLanguageTag(key), (String) value));
        return new MultilingualString(map);
    }
}
