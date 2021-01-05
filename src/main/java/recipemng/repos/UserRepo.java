package recipemng.repos;

import org.springframework.data.repository.CrudRepository;
import recipemng.models.User;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);
}
