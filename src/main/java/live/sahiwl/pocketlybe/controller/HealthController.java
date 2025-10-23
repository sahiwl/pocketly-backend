package live.sahiwl.pocketlybe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "UP",
            "service", "pocketly-be",
            "version", "1.0.0",
            "timestamp", LocalDateTime.now().toString()
        );
    }
}
