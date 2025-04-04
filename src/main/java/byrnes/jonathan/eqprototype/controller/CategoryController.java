package byrnes.jonathan.eqprototype.controller;

import byrnes.jonathan.eqprototype.dto.CreateCategoryDto;
import byrnes.jonathan.eqprototype.dto.EditCategoryDto;
import byrnes.jonathan.eqprototype.model.Category;
import byrnes.jonathan.eqprototype.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<Category> create(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return ResponseEntity.ok(this.categoryService.create(createCategoryDto));
    }

    @GetMapping("/get")
    public ResponseEntity<Category> get(@RequestParam String categoryId) {
        return ResponseEntity.ok(this.categoryService.get(categoryId));
    }

    @PutMapping("/edit")
    public ResponseEntity<Category> edit(@RequestParam String categoryId,
                                     @Valid @RequestBody EditCategoryDto editCategoryDto) {
        return ResponseEntity.ok(this.categoryService.edit(categoryId, editCategoryDto));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(this.categoryService.getAll());
    }
}
