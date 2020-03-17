package kickstart.room;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Datenbank der Räume einer Filiale
 */
interface RoomRepository extends CrudRepository<Room, Long> {
	@Override
	Streamable<Room> findAll();
}