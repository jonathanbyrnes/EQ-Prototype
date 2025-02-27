package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.*;
import byrnes.jonathan.eqprototype.exceptions.GlobalExceptionHandler;
import byrnes.jonathan.eqprototype.model.*;
import byrnes.jonathan.eqprototype.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

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

    @Test
    public void testJoinQuiz_Success() throws Exception {
        String userId = "user123";
        String quizId = "quiz123";

        User dummyUser = Mockito.mock(User.class);
        Quiz dummyQuiz = Mockito.mock(Quiz.class);

        Date now = new Date();
        LinkedQuiz linkedQuiz = new LinkedQuiz(dummyUser, dummyQuiz, now, "IN PROGRESS", 0, now, now);
        linkedQuiz.setId("linkedQuiz123");

        when(userService.joinQuiz(userId, quizId)).thenReturn(linkedQuiz);

        mockMvc.perform(post("/api/user/join")
                        .param("userId", userId)
                        .param("quizId", quizId))
                .andExpect(status().isOk());
    }

    @Test
    public void testSubmitResponse_Success() throws Exception {
        String linkedQuizId = "linkedQuiz123";
        String questionId = "question123";

        LinkedQuiz dummyLinkedQuiz = Mockito.mock(LinkedQuiz.class);
        Question dummyQuestion = Mockito.mock(Question.class);
        when(dummyQuestion.getAnswers()).thenReturn(List.of("correctAnswer"));

        ResponseDto responseDto = new ResponseDto("correctAnswer");

        boolean isCorrect = dummyQuestion.getAnswers().contains(responseDto.getResponse());

        Date now = new Date();
        Response response = new Response(dummyLinkedQuiz, dummyQuestion, responseDto.getResponse(), isCorrect, now);
        response.setId("response123");

        when(userService.submitResponse(eq(linkedQuizId), eq(questionId), any(ResponseDto.class)))
                .thenReturn(response);

        String responseJson = objectMapper.writeValueAsString(responseDto);

        mockMvc.perform(post("/api/user/submit")
                        .param("linkedQuizId", linkedQuizId)
                        .param("questionId", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(responseJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testCompleteQuiz_Success() throws Exception {
        String linkedQuizId = "test-quiz-id";
        QuizSummaryDto summary = new QuizSummaryDto(linkedQuizId, 10, 7, 70);

        when(userService.completeQuiz(linkedQuizId)).thenReturn(summary);

        mockMvc.perform(post("/api/user/complete")
                        .param("linkedQuizId", linkedQuizId))
                .andExpect(status().isOk());
    }

}


