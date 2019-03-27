package net.pkhapps.idispatch.cad.server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Document me!
 */
@RestController
public class PingController {

    @GetMapping(path = "/ping")
    public ResponseEntity<Void> ping() {
        return ResponseEntity.noContent().build();
    }
}
