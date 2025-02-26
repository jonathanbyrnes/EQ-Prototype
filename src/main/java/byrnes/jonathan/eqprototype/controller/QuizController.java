package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<Quiz> create(@RequestParam String userId, @RequestParam String categoryId, @Valid @RequestBody CreateQuizDto createQuizDto) {
        return ResponseEntity.ok(this.quizService.create(userId, categoryId, createQuizDto));
    }

}
