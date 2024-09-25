package recipes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import recipes.model.Recipe;
import recipes.model.RecipeDTO;
import recipes.model.User;
import recipes.service.RecipeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class RecipeController {
    private final RecipeService rs;

    public RecipeController(RecipeService rs) {
        this.rs = rs;
    }

    @Transactional
    @PostMapping("/api/recipe/new")
    public ResponseEntity<RecipeDTO> postRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails u) {
//        if (recipe == null || recipe.getName() == null) {
//            return ResponseEntity.badRequest().build();
//        }

        Recipe r = rs.addRecipe(new Recipe(recipe.getName(), recipe.getCategory(), LocalDateTime.now(), recipe.getDescription(), recipe.getIngredients(), recipe.getDirections(), (User) u));
        RecipeDTO rto = new RecipeDTO(r.getId());
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("id", id);  // Assuming getId() gives the generated ID

        return ResponseEntity.ok(rto);
    }

    @Transactional(readOnly = true)
    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        Optional<Recipe> r = rs.getRecipeById(id);
        if (!r.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(r.get());
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails u) {
        Optional<Recipe> r = rs.getRecipeById(id);
        if (!r.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            if (r.get().getAuthor().getUsername().equals(u.getUsername())) {
                rs.deleteRecipeById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails u) {
        Optional<Recipe> r = rs.getRecipeById(id);
        if (!r.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (r.get().getAuthor().getUsername().equals(u.getUsername())) {
            r.get().setName(recipe.getName());
            r.get().setDate(LocalDateTime.now());
            r.get().setCategory(recipe.getCategory());
            r.get().setDescription(recipe.getDescription());
            r.get().setIngredients(recipe.getIngredients());
            r.get().setDirections(recipe.getDirections());

            rs.addRecipe(r.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @GetMapping("/api/recipe/search/")
    public ResponseEntity<List<Recipe>> searchRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {
        List<Recipe> recipes = new ArrayList<>();
        if ((category != null && name != null) || (category == null && name == null)) {
            return ResponseEntity.badRequest().build();
        } else if (!(category == null)) {
            recipes = rs.search(category, true);
        } else if (!(name == null)) {
            recipes = rs.search(name, false);
        }
        System.out.println("Number of recipes found: " + recipes.size());
        for (Recipe recipe : recipes) {
            System.out.println(recipe);
        }
        recipes.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return ResponseEntity.ok(recipes);
    }

}
