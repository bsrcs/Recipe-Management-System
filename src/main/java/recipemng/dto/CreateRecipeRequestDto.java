package recipemng.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.List;

@Data
public class CreateRecipeRequestDto {
    private String recipeTitle;
    private String recipeDescription;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> ingredients;
}
