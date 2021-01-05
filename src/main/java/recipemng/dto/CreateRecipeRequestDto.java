package recipemng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.List;

@Data
public class CreateRecipeRequestDto {
    private String recipeTitle;
    private String recipeDescription;
    private List<String> ingredients;
}
