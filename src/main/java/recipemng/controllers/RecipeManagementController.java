package recipemng.controllers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipemng.dto.CreateRecipeRequestDto;
import recipemng.dto.CreateUserRequestDto;
import recipemng.models.Recipe;
import recipemng.models.User;
import recipemng.services.AuthenticationService;
import recipemng.services.RecipeService;
import recipemng.services.UserService;
import recipemng.util.HashUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("recipe-app")
public class RecipeManagementController {

    @Autowired
    RecipeService recipeService;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/user/recipes")
    public ResponseEntity<?> getAllRecipes(@RequestHeader(value = "secret-token") String secret){
        if (authenticationService.checkIfTokenIsValid(secret)) {
            User user = userService.findByUserName(authenticationService.getUsername(secret)).get();
            return new ResponseEntity<>(recipeService.getAllRecipesOfAUser(user), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Wrong token", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/public/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable Long id){
            return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.OK);
    }

    @GetMapping(path = "/public/recipes/search/{searchWord}")
    public ResponseEntity<?> searchRecipe(@PathVariable("searchWord") String searchToken){
        List<Recipe> recipes = recipeService.searchRecipe(searchToken);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @PostMapping(path = "/user/recipe")
    public ResponseEntity<?> createRecipe(@RequestHeader(value = "secret-token") String secret,
                                          @RequestBody CreateRecipeRequestDto createRecipeRequestDto){
        if (authenticationService.checkIfTokenIsValid(secret)) {
            User user = userService.findByUserName(authenticationService.getUsername(secret)).get();
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


}
