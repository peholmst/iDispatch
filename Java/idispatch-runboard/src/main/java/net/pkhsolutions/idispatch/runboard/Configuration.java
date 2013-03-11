package net.pkhsolutions.idispatch.runboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Configuration extends net.pkhsolutions.idispatch.rest.client.Configuration {

    public Set<String> getConcernedResources() {
        String resources = System.getProperty("idispatch.resources");
        if (resources == null || resources.isEmpty()) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(Arrays.asList(resources.split(",")));
        }
    }

    public Language getLanguage() {
        String language = System.getProperty("idispatch.language", "sv");
        switch (language) {
            case "fi":
                return Language.FINNISH;
            case "sv":
            default:
                return Language.SWEDISH;
        }
    }

    public boolean isUndecorated() {
        String value = System.getProperty("idispatch.undecorated", "true");
        return Boolean.parseBoolean(value);
    }

    public Boolean isLowResolution() {
        String value = System.getProperty("idispatch.lowres", "false");
        return Boolean.parseBoolean(value);
    }
}
