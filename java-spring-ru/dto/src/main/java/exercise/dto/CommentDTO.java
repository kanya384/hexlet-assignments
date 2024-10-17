package exercise.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Data
public class CommentDTO {
    private Long id;
    private String body;
}
// END
