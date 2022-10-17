package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.Image;

public interface ImageRepository extends CrudRepository<Image,Long> {
}
