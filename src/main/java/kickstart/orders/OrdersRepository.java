package kickstart.orders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface OrdersRepository extends CrudRepository<Orders, Long> {
	@Override
	Streamable<Orders> findAll();
}
