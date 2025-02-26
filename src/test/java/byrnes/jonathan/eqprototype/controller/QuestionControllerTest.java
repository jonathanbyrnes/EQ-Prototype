package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.exceptions.GlobalExceptionHandler;
import byrnes.jonathan.eqprototype.model.*;
import byrnes.jonathan.eqprototype.service.QuestionService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testCreate_Success() throws Exception {
        List<String> answers = Arrays.asList("Correct Answer");
        List<String> options = Arrays.asList("Option1", "Option2", "Option3");
        CreateQuestionDto createQuestionDto = new CreateQuestionDto(
                "What is 2+2?", 30, 10, answers, options);

        Question question = createFakeQuestion(createQuestionDto);

        when(questionService.create(eq("quiz123"), eq("type123"), any(CreateQuestionDto.class)))
                .thenReturn(question);

        mockMvc.perform(post("/api/question/create")
                        .param("quizId", "quiz123")
                        .param("typeId", "type123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createQuestionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testEdit_Success() throws Exception {
        EditQuestionDto editQuestionDto = new EditQuestionDto(
                "What is the capital of France?",
                30,
                10,
                List.of("Paris", "London"),
                List.of("Option1", "Option2")
        );
        CreateQuestionDto createQuestionDto = new CreateQuestionDto(
                "What is 2+2?", 10, 30,
                List.of("England", "France"),
                List.of("Option2", "Option1"));

        Question question = createFakeQuestion(createQuestionDto);

        question.setQuestionStr(editQuestionDto.getQuestionStr());
        question.setTimeLimit(editQuestionDto.getTimeLimit());
        question.setWorth(editQuestionDto.getWorth());
        question.setAnswers(editQuestionDto.getAnswers());
        question.setOptions(editQuestionDto.getOptions());

        when(questionService.edit(eq(question.getId()), any(EditQuestionDto.class))).thenReturn(question);

        mockMvc.perform(put("/api/question/edit")
                        .param("questionId", question.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editQuestionDto)))
                .andExpect(status().isOk());
    }

    private Question createFakeQuestion(CreateQuestionDto createQuestionDto) {
        Category category = new Category("None");
        LinkedRole linkedRole = new LinkedRole(new Role("USER"));
        User user = new User(
                linkedRole, "email", "password", new Date(), false
        );
        Quiz quiz = new Quiz(
                user, category, "Title", "Description", false, false, new Date()
        );
        quiz.setId("quiz123");
        Type type = new Type("MCQ");
        type.setId("type123");

        return new Question(
                quiz, type, createQuestionDto.getQuestionStr(), createQuestionDto.getTimeLimit(),
                createQuestionDto.getWorth(), createQuestionDto.getAnswers(), createQuestionDto.getOptions()
        );
    }

}