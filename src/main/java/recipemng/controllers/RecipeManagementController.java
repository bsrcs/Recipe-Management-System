package recipemng.controllers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipemng.dto.CreateRecipeRequestDto;
import recipemng.dto.CreateRecipeResponseDto;
import recipemng.dto.CreateUserRequestDto;
import recipemng.models.Recipe;
import recipemng.models.User;
import recipemng.services.RecipeService;
import recipemng.services.UserService;
import recipemng.util.HashUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class RecipeManagementController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;

    @GetMapping("/recipes")
    public ResponseEntity<?> getAllRecipes(@RequestHeader(value = "secret-token") String secret){
        if (checkIfTokenIsValid(secret)) {
            User user = userService.findByUserName(getUsername(secret)).get();
            return new ResponseEntity<>(recipeService.getAllRecipesOfAUser(user), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Wrong token", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/recipe/{id}")
    public ResponseEntity<?> getRecipe(@RequestHeader(value = "secret-token") String secret,
    @PathVariable Long id){
        if (checkIfTokenIsValid(secret)) {
            return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Wrong token", HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping(path = "/recipe")
    public ResponseEntity<?> createRecipe(@RequestHeader(value = "secret-token") String secret,
                                          @RequestBody CreateRecipeRequestDto createRecipeRequestDto){
        if (checkIfTokenIsValid(secret)) {
            User user = userService.findByUserName(getUsername(secret)).get();
            return new ResponseEntity<>(recipeService.createRecipe(createRecipeRequestDto,user), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Bad token :(", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequestDto userRequestDto){
        User newUser = new User();
        newUser.setPassword(userRequestDto.getPassword());
        newUser.setUsername(userRequestDto.getUsername());

        return new ResponseEntity<>(userService.createOrUpdateUser(newUser), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    // add the HttpServletResponse object to our REST endpoint as an argument, then use the addHeader() method
    public ResponseEntity<?> login(HttpServletResponse servletResponse, @RequestBody CreateUserRequestDto userRequestDto){
        Optional<User> checkedUser = userService.getUserByUsernameAndPassword(userRequestDto.getUsername(), userRequestDto.getPassword());
        if (checkedUser.isPresent()) {
            String userCredentials = userRequestDto.getUsername()+":"+HashUtil.getHash(userRequestDto.getPassword());
            byte[] bytesEncoded = Base64.encodeBase64(userCredentials.getBytes());
            String base64EncodedUserCredentials = new String(bytesEncoded);
            servletResponse.addHeader("secret-token", base64EncodedUserCredentials);
            return new ResponseEntity<>("Login is successful", HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity<>("Wrong credentials", HttpStatus.FORBIDDEN);
        }
    }

    private boolean checkIfTokenIsValid(String token){

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

    private String getUsername(String token){
        byte[] decodedTokenBytes = Base64.decodeBase64(token);
        String decodedToken = new String(decodedTokenBytes);

        String[] splittedToken = decodedToken.split(":");
        String username = splittedToken[0];

        return username;
    }
}
