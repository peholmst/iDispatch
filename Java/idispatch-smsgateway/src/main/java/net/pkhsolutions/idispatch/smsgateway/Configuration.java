package net.pkhsolutions.idispatch.smsgateway;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Configuration extends net.pkhsolutions.idispatch.rest.client.Configuration {

    public Set<String> getNumbersForResource(String resource) {
        String numbers = System.getProperty("idispatch.numbers." + resource);
        if (numbers == null || numbers.isEmpty()) {
            return Collections.emptySet();
        } else {
            return new HashSet<>(Arrays.asList(numbers.split(",")));
        }
    }

    public String getPort() {
        String port = System.getProperty("idispatch.modem.port");
        return port;
    }

    public int getBaudrate() {
        String baudrate = System.getProperty("idispatch.modem.baudrate", "115200");
        return Integer.parseInt(baudrate);
    }

    public String getModemManufacturer() {
        String manufacturer = System.getProperty("idispatch.modem.manufacturer", "Huawei");
        return manufacturer;
    }
}
