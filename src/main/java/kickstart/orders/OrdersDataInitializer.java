package kickstart.orders;

import static kickstart.staff.Skill.CLEAN;
import static kickstart.staff.Skill.ELEKTRO;
import static kickstart.staff.Skill.GRIND;
import static kickstart.staff.Skill.KEY;
import static kickstart.staff.Skill.PATCH;
import static kickstart.staff.Skill.SEW;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(10)
public class OrdersDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(OrdersDataInitializer.class);

	private final OrdersManagement ordersManagement;

	OrdersDataInitializer(OrdersManagement ordersManagement) {

		Assert.notNull(ordersManagement, "orderManagement must not be null!");

		this.ordersManagement = ordersManagement;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default Orders");
		
		
	}
}