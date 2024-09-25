package recipes.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import recipes.model.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//    @EntityGraph(attributePaths = {"ingredients", "directions"})
    List<Recipe> findByCategoryIgnoreCase(String category);
//    @EntityGraph(attributePaths = {"ingredients", "directions"})
    List<Recipe> findByNameContainingIgnoreCase(String name);
}
