package exercise;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.beans.factory.annotation.Autowired;

import exercise.model.User;
import exercise.component.UserProperties;

@SpringBootApplication
@RestController
public class Application {
    @Autowired
    UserProperties userProperties;

    // Все пользователи
    private List<User> users = Data.getUsers();

    // BEGIN
    @GetMapping("/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAdmins() {
        List<String> adminEmails = userProperties.getAdmins();
        return users.stream().filter(u -> adminEmails.contains(u.getEmail())).sorted((User u1, User u2) -> {
            return u1.getName().compareTo(u2.getName());
        }).map(u -> u.getName()).toList();
    }
    // END

    @GetMapping("/users")
    public List<User> index() {
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> show(@PathVariable Long id) {
        var user = users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
