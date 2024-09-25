package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    private LocalDateTime date;

    @NotBlank
    private String description;

    @Size(min=1)
    @NotNull
    @ElementCollection
//    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
//    @Column(name = "ingredient")
    @OrderColumn(name = "ingredient_order")
//    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<String> ingredients;

    @Size(min = 1)
    @NotNull
    @ElementCollection
//    @CollectionTable(name = "recipe_directions", joinColumns = @JoinColumn(name = "recipe_id"))
//    @Column(name = "direction")
    @OrderColumn(name = "direction_order")
//    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<String> directions;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User author;

    @Builder
    public Recipe(String name, String category, LocalDateTime date, String description,
                  List<String> ingredients, List<String> directions, User u) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.author = u;
    }

    @Override
    public String toString() {
        return "Recipe{name='" + name + "', category='" + category + "', ingredients=" + ingredients + ", directions=" + directions + "}";
    }
}
