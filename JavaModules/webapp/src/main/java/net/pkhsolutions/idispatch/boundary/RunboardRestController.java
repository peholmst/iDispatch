package net.pkhsolutions.idispatch.boundary;

import net.pkhsolutions.idispatch.control.RunboardDispatcher;
import net.pkhsolutions.idispatch.entity.DispatchNotification;
import net.pkhsolutions.idispatch.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class RunboardRestController {

    @Autowired
    RunboardDispatcher runboardDispatcher;

    @RequestMapping("/runboardNotifications")
    public List<Map<String, Object>> findNewNotifications(@RequestParam(value = "runboardKey", required = true) String runboardKey) {
        return runboardDispatcher.getDispatchNotifications(runboardKey).stream().map(this::notificationToMap).collect(Collectors.toList());
    }

    @RequestMapping("/runboardAcknowledge")
    public void acknowledge(@RequestParam(value = "runboardKey", required = true) String runboardKey, @RequestParam(value = "notification", required = true) Long notificationId) {
        runboardDispatcher.acknowledgeDispatchNotification(runboardKey, notificationId);
    }

    private Map<String, Object> notificationToMap(DispatchNotification notification) {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", notification.getId());
        map.put("timestamp", notification.getTimestamp());
        map.put("assignment_id", notification.getAssignment().getId());
        map.put("assignment_type_code", notification.getAssignmentType().getCode());
        map.put("assignment_type_descr", notification.getAssignmentType().getDescription());
        map.put("urgency", notification.getUrgency());
        map.put("municipality", notification.getMunicipality().getName());
        map.put("address", notification.getAddress());
        map.put("description", notification.getDescription());
        map.put("resources", notification.getAssignedResources().stream().map(Resource::getCallSign).collect(Collectors.toSet()));
        return map;
    }

}
