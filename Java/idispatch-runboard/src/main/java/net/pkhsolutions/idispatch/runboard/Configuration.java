package net.pkhsolutions.idispatch.runboard;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Configuration {

    private Configuration() {
    }

    public static Set<String> getConcernedResources() {
        String resources = System.getProperty("idispatch.resources");
        if (resources == null || resources.isEmpty()) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(Arrays.asList(resources.split(",")));
        }
    }

    public static String getUrl() {
        String url = System.getProperty("idispatch.rest.url", "");
        return url;
    }

    public static String getReceiverId() {
        String receiverId = System.getProperty("idispatch.receiverId", "");
        return receiverId;
    }

    public static String getSecurityCode() {
        String securityCode = System.getProperty("idispatch.securityCode", "");
        return securityCode;
    }

    public static Language getLanguage() {
        String language = System.getProperty("idispatch.language", "sv");
        switch (language) {
            case "fi":
                return Language.FINNISH;
            case "sv":
            default:
                return Language.SWEDISH;
        }
    }

    public static int getPollingIntervalMilliseconds() {
        String interval = System.getProperty("idispatch.pollingInterval", "3000");
        return Integer.parseInt(interval);
    }

    public static boolean isVerifyingSslCertificate() {
        String value = System.getProperty("idispatch.ssl.verify", "true");
        return Boolean.parseBoolean(value);
    }

    public static boolean isUndecorated() {
        String value = System.getProperty("idispatch.undecorated", "true");
        return Boolean.parseBoolean(value);
    }

    public static Boolean isLowResolution() {
        String value = System.getProperty("idispatch.lowres", "false");
        return Boolean.parseBoolean(value);
    }
}
