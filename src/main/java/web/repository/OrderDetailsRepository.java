package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.models.OrderDetails;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails,Long> {
}
