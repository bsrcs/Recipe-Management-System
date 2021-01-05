package recipemng.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipemng.dto.CreateRecipeRequestDto;
import recipemng.dto.CreateRecipeResponseDto;
import recipemng.models.Recipe;
import recipemng.models.User;
import recipemng.repos.RecipeRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService{

    @Autowired
    RecipeRepo recipeRepo;
    @Override
    public List<Recipe> getAllRecipesOfAUser(User user) {
        try {
            List<Recipe> recipes = new ArrayList<>();
            for (Recipe recipe : recipeRepo.findAll()) {
                recipes.add(recipe);
            }
            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Recipe getRecipe(Long id) {

        try {
            if (recipeRepo.findById(id).isPresent()) {
                return recipeRepo.findById(id).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Recipe createOrUpdateRecipe(Recipe recipe) {
        return recipeRepo.save(recipe);
    }

    @Override
    public CreateRecipeResponseDto createRecipe(CreateRecipeRequestDto createRecipeRequestDto, User user) {
        Recipe newRecipe = new Recipe();
        newRecipe.setUser(user);
        newRecipe.setRecipeTitle(createRecipeRequestDto.getRecipeTitle());
        newRecipe.setRecipeDescription(createRecipeRequestDto.getRecipeDescription());
        newRecipe.setIngredients(createRecipeRequestDto.getIngredients());
        Recipe savedNewRecipe = this.createOrUpdateRecipe(newRecipe);

        CreateRecipeResponseDto recipeResponseDto = new CreateRecipeResponseDto();
        recipeResponseDto.setUserId(user.getUserId());
        recipeResponseDto.setRecipeId(savedNewRecipe.getRecipeId());
        recipeResponseDto.setRecipeTitle(savedNewRecipe.getRecipeTitle());
        recipeResponseDto.setRecipeDescription(savedNewRecipe.getRecipeDescription());
        recipeResponseDto.setIngredients(savedNewRecipe.getIngredients());
        return recipeResponseDto;
    }

    @Override
    public void deleteRecipe(Recipe recipe) {
        recipeRepo.delete(recipe);
    }

    @Override
    public Recipe printRecipe(Recipe recipe) {
        return null;
    }
}
