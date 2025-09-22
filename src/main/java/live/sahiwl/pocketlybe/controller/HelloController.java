package live.sahiwl.pocketlybe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String greet(){
        return  "hi, this app is in development stage :D";
    }
}
