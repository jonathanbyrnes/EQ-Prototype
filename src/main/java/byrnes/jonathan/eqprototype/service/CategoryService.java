package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateCategoryDto;
import byrnes.jonathan.eqprototype.model.Category;
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

    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }
}
