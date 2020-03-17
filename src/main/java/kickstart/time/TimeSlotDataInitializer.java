package kickstart.time;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kickstart.orders.Orders;
import kickstart.orders.OrdersManagement;
import kickstart.room.RoomManagement;
import kickstart.staff.StaffManagement;

/**
 * Repräsentiert einen vordefinierten Anfangszustand der Zeiten zur Bearbeitung eines Auftrags einer Filiale
 */
@Component
@Order(80)
public class TimeSlotDataInitializer implements DataInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(TimeSlotDataInitializer.class);

	private final TimeSlotManagement timeslotManagement;
	private final OrdersManagement ordersManagement;

	/**
	 * Erstellung des Anfangszustandes
	 * @param timeslotManagement Zeitenverwaltungseinheit einer Filiale
	 * @param ordersManagement Auftragsverwaltungseinheit einer Filiale
	 */
	TimeSlotDataInitializer(TimeSlotManagement timeslotManagement, OrdersManagement ordersManagement) {
		Assert.notNull(timeslotManagement, "TimeSlotRepository must not be null!");
		Assert.notNull(ordersManagement, "OrdersRepository must not be null");

		this.timeslotManagement = timeslotManagement;
		this.ordersManagement = ordersManagement;
	}

	/**
	 * Jeder offene Auftrag bekommt eine Zeit zugewiesen
	 */
	@Override
	public void initialize() {
		LOG.info("Creating default timeslots.");

		for(Orders order : ordersManagement.findAll()) {
			if(order.getFinishDateIntern().isBefore(timeslotManagement.getTime().toLocalDate())) {
				timeslotManagement.getFreeTimeSlot(order);
			}
		}
	}
}