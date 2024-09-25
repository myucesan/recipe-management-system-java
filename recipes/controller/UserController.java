package recipes.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.model.User;
import recipes.repository.UserRepository;

@RestController
public class UserController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> postRecipe(@Valid @RequestBody User u) {
        try {
            User user = new User();
            user.setEmail(u.getEmail());
            user.setPassword(passwordEncoder.encode(u.getPassword()));
            repository.save(user);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("User already exists.");
        }
        return ResponseEntity.ok("User registered succesfully");
    }
}



