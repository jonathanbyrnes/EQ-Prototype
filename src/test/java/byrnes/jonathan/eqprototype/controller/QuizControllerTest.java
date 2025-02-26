package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.dto.ShareQuizDto;
import byrnes.jonathan.eqprototype.exceptions.GlobalExceptionHandler;
import byrnes.jonathan.eqprototype.model.*;
import byrnes.jonathan.eqprototype.service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class QuizControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizController quizController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(quizController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testCreate_Success() throws Exception {
        CreateQuizDto createQuizDto = new CreateQuizDto(
                "Test Quiz", "Test quiz description", false);

        Quiz quiz = createFakeQuiz(createQuizDto);

        when(quizService.create(eq("user123"), eq("category123"), any(CreateQuizDto.class)))
                .thenReturn(quiz);

        mockMvc.perform(post("/api/quiz/create")
                        .param("userId", "user123")
                        .param("categoryId", "category123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuizDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testShare_Success() throws Exception {
        CreateQuizDto createQuizDto = new CreateQuizDto(
                "Test Quiz", "Test quiz description", false);
        Quiz quiz = createFakeQuiz(createQuizDto);
        quiz.setId("12345");

        ShareQuizDto shareQuizDto = new ShareQuizDto(
                "http://example.com/join/" + quiz.getId(),
                "base64QR");
        when(quizService.share(quiz.getId())).thenReturn(shareQuizDto);

        mockMvc.perform(post("/api/quiz/share")
                        .param("quizId", quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testEdit_Success() throws Exception {
        EditQuizDto editQuizDto = new EditQuizDto("New Quiz Title", "Updated quiz description", true);

        CreateQuizDto createQuizDto = new CreateQuizDto(
                "Test Quiz", "Test quiz description", false);
        Quiz quiz = createFakeQuiz(createQuizDto);

        quiz.setTitle(editQuizDto.getTitle());
        quiz.setDescription(editQuizDto.getDescription());
        quiz.setActive(editQuizDto.isActive());

        when(quizService.edit(eq(quiz.getId()), any(EditQuizDto.class))).thenReturn(quiz);

        mockMvc.perform(put("/api/quiz/edit")
                        .param("quizId", quiz.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editQuizDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete_Success() throws Exception {
        String quizId = "12345";

        when(quizService.delete(quizId)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/quiz/delete")
                        .param("quizId", quizId))
                .andExpect(status().isOk());
    }

    @Test
    void testReuse_Success() throws Exception {
        String quizId = "12345";
        CreateQuizDto createQuizDto = new CreateQuizDto(
                "Test Quiz", "Test quiz description", false);
        Quiz newQuiz = createFakeQuiz(createQuizDto);

        when(quizService.reuse(eq(quizId))).thenReturn(newQuiz);

        mockMvc.perform(post("/api/quiz/reuse")
                        .param("quizId", quizId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Quiz createFakeQuiz(CreateQuizDto createQuizDto) {
        Category category = new Category("None");
        LinkedRole linkedRole = new LinkedRole(new Role("USER"));
        User user = new User(
                linkedRole, "email", "password", new Date(), false
        );
        user.setId("user123");
        category.setId("category123");

        return new Quiz(
                user, category, createQuizDto.getTitle(), createQuizDto.getDescription(),
                createQuizDto.isActive(), new Date());
    }


}