package recipemng.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateRecipeResponseDto {
    private Long userId;
    private Long recipeId;
    private String recipeTitle;
    private String recipeDescription;
    private List<String> ingredients;
}
