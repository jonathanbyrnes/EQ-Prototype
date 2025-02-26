package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Question> create(@RequestParam String quizId, @RequestParam String typeId,
                                           @Valid @RequestBody CreateQuestionDto createQuestionDto) {
        return ResponseEntity.ok(this.questionService.create(quizId, typeId, createQuestionDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<Question> edit(@RequestParam String questionId,
                                         @Valid @RequestBody EditQuestionDto editQuestionDto) {
        return ResponseEntity.ok(this.questionService.edit(questionId, editQuestionDto));
    }

}
