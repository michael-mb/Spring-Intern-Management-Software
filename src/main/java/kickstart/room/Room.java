package kickstart.room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Repräsentiert einen Raum einer Filiale
 */
@Entity
public class Room {
	private @GeneratedValue @Id long roomId;
	private String name;

	@SuppressWarnings("unused")
	private Room(){}

	/**
	 * Erstellung eines neuen Raumes mit einem Namen
	 * @param name Name des Raumes
	 */
	public Room(String name){
		this.name = name;
	}

	/**
	 * Getter für den Namen des Raumes
	 * @return Gibt den Namen des Raumes zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter für die Identifikationsnummer des Raumes
	 * @return Gibt die Identifikationsnummer des Raumes zurück
	 */
	public long getId() {
		return roomId;
	}
}
