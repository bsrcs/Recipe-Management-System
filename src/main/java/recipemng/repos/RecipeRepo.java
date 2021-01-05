package recipemng.repos;

import org.springframework.data.repository.CrudRepository;
import recipemng.models.Recipe;

public interface RecipeRepo extends CrudRepository<Recipe, Long> {

}
