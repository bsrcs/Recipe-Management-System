package recipemng.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipemng.models.User;

import java.util.Optional;
@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
}
