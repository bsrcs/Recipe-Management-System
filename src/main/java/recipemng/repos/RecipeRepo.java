package recipemng.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipemng.models.Recipe;

import java.util.List;

@Repository
public interface RecipeRepo extends CrudRepository<Recipe, Long> {
    @Query("select r from Recipe r where r.recipeTitle like %?1% or r.recipeDescription like %?1%")
    List<Recipe> findBySearchWord(String searchWord);
}
