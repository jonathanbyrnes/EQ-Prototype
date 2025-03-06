package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.dto.FullQuizDisplayDto;
import byrnes.jonathan.eqprototype.dto.ShareQuizDto;
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
    public ResponseEntity<Quiz> create(@RequestParam String userId,
                                       @RequestParam String categoryId,
                                       @Valid @RequestBody CreateQuizDto createQuizDto) {
        return ResponseEntity.ok(this.quizService.create(userId, categoryId, createQuizDto));
    }

    @PostMapping("/share")
    public ResponseEntity<ShareQuizDto> share(@RequestParam String quizId) {
        return ResponseEntity.ok(this.quizService.share(quizId));
    }

    @PutMapping("/edit")
    public ResponseEntity<Quiz> edit(@RequestParam String quizId,
                                     @Valid @RequestBody EditQuizDto editQuizDto) {
        return ResponseEntity.ok(this.quizService.edit(quizId, editQuizDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam String quizId) {
        return this.quizService.delete(quizId);
    }

    @PostMapping("/reuse")
    public ResponseEntity<Quiz> reuse(@RequestParam String quizId) {
        return ResponseEntity.ok(this.quizService.reuse(quizId));
    }

    @GetMapping("/get")
    public ResponseEntity<FullQuizDisplayDto> get(@RequestParam String quizId) {
        return ResponseEntity.ok(this.quizService.get(quizId));
    }

}
