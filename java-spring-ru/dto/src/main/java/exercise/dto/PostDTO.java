package exercise.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String body;

    private List<CommentDTO> comments;
}
// END
