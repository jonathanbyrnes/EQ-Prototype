package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.UserLoginDto;
import byrnes.jonathan.eqprototype.dto.UserRegistrationDto;
import byrnes.jonathan.eqprototype.dto.UserUpdateDto;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return ResponseEntity.ok(this.userService.register(userRegistrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(this.userService.login(userLoginDto));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> update(@PathVariable String userId, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(this.userService.update(userId, userUpdateDto));
    }



}
