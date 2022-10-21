package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import web.models.Category;
import web.repository.CategoryRepository;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category convert(String title) {
        return categoryRepository.findByTitle(title);
    }
}
