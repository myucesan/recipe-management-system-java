package recipes.service;

import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {
    RecipeRepository recipeRepository;


    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

//    private Map<Integer, Recipe> recipes = new HashMap<>();
//    private AtomicInteger counter = new AtomicInteger(1);
    public Recipe addRecipe(Recipe r) {
//        int id = counter.getAndIncrement();
//        recipes.put(id, r);
        return recipeRepository.save(r);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
        recipeRepository.flush();
    }

    public List<Recipe> search(String txt, boolean isCategory) {
        if (isCategory) {
            return recipeRepository.findByCategoryIgnoreCase(txt);
        } else {
            return recipeRepository.findByNameContainingIgnoreCase(txt);
        }

    }


}
