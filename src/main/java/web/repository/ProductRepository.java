package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Long> {

    List<Product> findByName(String title);
}
