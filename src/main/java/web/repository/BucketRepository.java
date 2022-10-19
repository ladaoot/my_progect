package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.Bucket;

public interface BucketRepository extends CrudRepository<Bucket,Long> {
}
