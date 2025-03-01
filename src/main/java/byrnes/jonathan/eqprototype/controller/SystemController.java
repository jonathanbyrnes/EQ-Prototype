package byrnes.jonathan.eqprototype.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/status")
    public ResponseEntity<String> isAPIOnline() {
        return ResponseEntity.ok("online");
    }
}
