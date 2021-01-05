package recipemng.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipemng.models.User;
import recipemng.repos.UserRepo;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepo userRepo;

    @Override
    public User createOrUpdateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return userRepo.findByUsername(username);
    }
}
