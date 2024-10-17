package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
@RequestMapping("")
public class WelcomeController {
    @Autowired
    Daytime daytime;

    @GetMapping("/welcome")
    public String greet() {
        return String.format("It is %s now! Welcome to Spring!", daytime.getName());
    }
}
// END
