package exercise.daytime;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Night implements Daytime {
    private String name = "night";

    public String getName() {
        return name;
    }

    // BEGIN
    @PostConstruct
    public void postConstruct() {
        log.info(String.format("бин %s был создан\n", getName()));
    }
    // END
}
