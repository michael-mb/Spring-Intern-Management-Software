package kickstart.time;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Datenbank der Zeiten zur Bearbeituung eines Auftrags einer Filiale
 */
interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {
	@Override
	Streamable<TimeSlot> findAll();
}