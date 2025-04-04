package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateCategoryDto;
import byrnes.jonathan.eqprototype.dto.EditCategoryDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.model.Category;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CreateCategoryDto createCategoryDto) {
        Optional<Category> categoryOptional = this.categoryRepository.findByNameIgnoreCase(createCategoryDto.getName());
        if (categoryOptional.isPresent()) {
            throw new IllegalArgumentException("This category already exists.");
        }

        Category category = new Category(createCategoryDto.getName());

        return this.categoryRepository.save(category);
    }

    public Category get(String categoryId) {
        return getCategoryById(categoryId);
    }

    public Category edit(String categoryId, EditCategoryDto editCategoryDto) {
        Category category = getCategoryById(categoryId);
        category.setName(editCategoryDto.getName());

        return this.categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    private Category getCategoryById(String categoryId) {
        return this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("This category does not exist"));
    }
}
