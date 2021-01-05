package recipemng.services;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipemng.models.User;
import recipemng.util.HashUtil;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    UserService userService;

    public boolean checkIfTokenIsValid(String token){

        boolean isTokenValid = false;
        byte[] decodedTokenBytes = Base64.decodeBase64(token);
        String decodedToken = new String(decodedTokenBytes);

        String[] splittedToken = decodedToken.split(":");
        String username = splittedToken[0];
        String hashedPassword = splittedToken[1];

        Optional<User> optionalUser = userService.findByUserName(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String realHash = HashUtil.getHash(user.getPassword());

            if(realHash.equals(hashedPassword)){
                isTokenValid = true;
            }
        }
        return isTokenValid;
    }

    public String getUsername(String token){
        byte[] decodedTokenBytes = Base64.decodeBase64(token);
        String decodedToken = new String(decodedTokenBytes);

        String[] splittedToken = decodedToken.split(":");
        String username = splittedToken[0];

        return username;
    }
}
