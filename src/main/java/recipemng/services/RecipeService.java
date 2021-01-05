package recipemng.services;

import recipemng.dto.CreateRecipeRequestDto;
import recipemng.dto.CreateRecipeResponseDto;
import recipemng.models.Recipe;
import recipemng.models.User;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipesOfAUser(User user);
    Recipe getRecipe(Long id);
    Recipe createOrUpdateRecipe(Recipe recipe);
    CreateRecipeResponseDto createRecipe(CreateRecipeRequestDto createRecipeRequestDto, User user);
    void deleteRecipe(Recipe recipe);
    Recipe printRecipe(Recipe recipe);
}
