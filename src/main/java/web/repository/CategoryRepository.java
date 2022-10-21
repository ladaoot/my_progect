package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.Category;

public interface CategoryRepository extends CrudRepository<Category,Long> {
    Category findByTitle(String title);
}
