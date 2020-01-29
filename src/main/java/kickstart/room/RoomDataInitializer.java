package kickstart.room;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Repräsentiert einen vordefinierten Anfangszustand der Räume einer Filiale
 */
@Component
@Order(10)
public class RoomDataInitializer implements DataInitializer {
	private static final Logger LOG = LoggerFactory.getLogger(RoomDataInitializer.class);

	private final RoomManagement roomManagement;

	/**
	 * Erstellung des Anfangszustandes
	 * @param roomManagement Raumverwaltungseinheit der Filiale
	 */
	RoomDataInitializer(RoomManagement roomManagement) {
		Assert.notNull(roomManagement, "RoomRepository must not be null!");

		this.roomManagement = roomManagement;
	}

	/**
	 * Räume des Anfangszustandes werden hinzugefügt
	 */
	@Override
	public void initialize() {
		if(roomManagement.findAll().toList().size() != 0){
			return;
		}

		LOG.info("Creating default rooms.");

		roomManagement.addRoom(new Room("Room1"));
		roomManagement.addRoom(new Room("Room2"));
	}
}