package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.ProductCount;

public interface ProductCountRepository extends CrudRepository<ProductCount,Long> {
}
