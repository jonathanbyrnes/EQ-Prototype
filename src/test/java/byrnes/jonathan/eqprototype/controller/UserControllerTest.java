package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.*;
import byrnes.jonathan.eqprototype.exceptions.GlobalExceptionHandler;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {


    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testRegistration_Success() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "example@gmail.com", "password");

        User user = new User(null, "example@gmail.com", "password", new Date(), false);

        when(userService.register(any(UserRegistrationDto.class))).thenReturn(user);
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistration_DuplicateEmail() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(
                "example@gmail.com", "password");

        when(userService.register(any(UserRegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("This email already exists."));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_ValidationError() throws Exception {
        // Invalid email
        UserRegistrationDto userDto = new UserRegistrationDto(
                "", "password123");

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_InvalidJson() throws Exception {
        // Malformed JSON
        String invalidJson = "{ \"email\": \"test@example.com\", \"password\": }";

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto(
                "example@gmail.com", "password");

        User user = new User(null, "example@gmail.com", "password", new Date(), false);

        when(userService.login(any(UserLoginDto.class))).thenReturn(user);
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate_Success() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto(
                "example@gmail.com", "password");

        User updatedUser = new User(null, "updated@example.com", "updatedpassword", new Date(), false);
        updatedUser.setId("123");

        when(userService.update(eq("123"), any(UserUpdateDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/update/{userId}", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testGeneratePasswordResetToken_Success() throws Exception {
        GeneratePasswordResetTokenDto generatePasswordResetTokenDto = new GeneratePasswordResetTokenDto(
                "test@example.com");

        when(userService.generatePasswordResetToken(any(GeneratePasswordResetTokenDto.class)))
                .thenReturn("token-123");

        mockMvc.perform(post("/api/user/generatePasswordResetToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(generatePasswordResetTokenDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testResetPassword_Success() throws Exception {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto(
                "updatedpassword");

        User updatedUser = new User(null, "updated@example.com", "updatedpassword", new Date(), false);

        when(userService.resetPassword(eq("token-123"), any(ResetPasswordDto.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(post("/api/user/reset-password")
                        .param("token", "token-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordDto)))
                .andExpect(status().isOk());
    }



}


