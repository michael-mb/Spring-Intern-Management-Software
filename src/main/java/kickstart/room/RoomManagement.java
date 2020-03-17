package kickstart.room;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Repräsentiert die Verwaltung der Räume einer Filiale
 */
@Service
@Transactional
public class RoomManagement {
	private final RoomRepository rooms;

	/**
	 * Erstellung der Raumverwaltungseinheit
	 * @param rooms Datenbank, die alle Räume beinhaltet
	 */
	RoomManagement(RoomRepository rooms){
		Assert.notNull(rooms, "RoomRepository must not be null!");

		this.rooms = rooms;
	}

	/**
	 * Neuer Raum wird in der Datenbank gespeichert
	 * @param room Neuer Raum
	 * @return Gibt den neu eingespeicherten Raum zurück
	 */
	public Room addRoom(Room room){
		return rooms.save(room);
	}

	/**
	 * Iterierbares Objekt, um über alle Räume in der Datenbank zu iterieren
	 * @return Gibt alle eingespeicherten Räume zurück
	 */
	public Streamable<Room> findAll() {
		return rooms.findAll();
	}
}