package exercise.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Data
public class ProductUpdateDTO {
    private String title;
    private int price;
}
// END
