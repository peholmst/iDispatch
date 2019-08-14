package net.pkhapps.idispatch.infra.api.devices;

import net.pkhapps.idispatch.infra.api.devices.data.BriefDeviceDescriptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@RestController
@RequestMapping("/api/devices/1.0")
public class DeviceManagementController {

    private final SecureRandom secureRandom;

    public DeviceManagementController() throws NoSuchAlgorithmException {
        secureRandom = SecureRandom.getInstanceStrong();
    }

    private String generateId() {
        byte[] bytes = secureRandom.generateSeed(6);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new DeviceManagementController().generateId());
    }

    @GetMapping
    Flux<BriefDeviceDescriptor> getDevices() { // TODO Pagination
        return Flux.empty();
    }

    private BriefDeviceDescriptor createDescriptor(String deviceId, String description, boolean enabled,
                                                   boolean inUse, Instant lastSeen) {
        var descriptor = new BriefDeviceDescriptor();
        descriptor.deviceId = deviceId;
        descriptor.description = description;
        descriptor.enabled = enabled;
        descriptor.inUse = inUse;
        descriptor.lastSeen = lastSeen;
        return descriptor;
    }
}
