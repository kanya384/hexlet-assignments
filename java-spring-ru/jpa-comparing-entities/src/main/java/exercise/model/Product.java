package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "price"}))
@Data
@EqualsAndHashCode(of = {"title", "price"})
public class Product {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private Integer price;
}
// END
