package recipemng.services;

import recipemng.models.User;

import java.util.Optional;

public interface UserService {
    User createOrUpdateUser(User user);
    Optional<User> getUserByUsernameAndPassword(String username, String password);
    Optional<User> findByUserName(String username);
}
