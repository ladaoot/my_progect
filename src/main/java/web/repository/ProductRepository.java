package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.Product;

public interface ProductRepository extends CrudRepository<Product,Long> {
}
