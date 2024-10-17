package exercise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

// BEGIN
@Data
public class GuestCreateDTO {
    @NotNull
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^[+][0-9]{11,13}")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{4}")
    private String clubCard;

    @Future
    private LocalDate cardValidUntil;
}
// END
