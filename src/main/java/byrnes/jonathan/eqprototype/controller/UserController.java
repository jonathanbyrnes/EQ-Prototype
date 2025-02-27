package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.*;
import byrnes.jonathan.eqprototype.model.LinkedQuiz;
import byrnes.jonathan.eqprototype.model.Response;
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

    @PostMapping("/generatePasswordResetToken")
    public ResponseEntity<String> generatePasswordResetToken(@Valid @RequestBody GeneratePasswordResetTokenDto generatePasswordResetTokenDto) {
        return ResponseEntity.ok(this.userService.generatePasswordResetToken(generatePasswordResetTokenDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<User> resetPassword(@RequestParam String token, @Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return ResponseEntity.ok(this.userService.resetPassword(token, resetPasswordDto));
    }

    @PostMapping("/join")
    public ResponseEntity<LinkedQuiz> joinQuiz(@RequestParam String userId,
                                               @RequestParam String quizId) {
        return ResponseEntity.ok(this.userService.joinQuiz(userId, quizId));
    }

    @PostMapping("submit")
    public ResponseEntity<Response> submitResponse(@RequestParam String linkedQuizId,
                                                   @RequestParam String questionId,
                                                   @Valid @RequestBody ResponseDto responseDto) {
        return ResponseEntity.ok(this.userService.submitResponse(linkedQuizId, questionId, responseDto));
    }




}
