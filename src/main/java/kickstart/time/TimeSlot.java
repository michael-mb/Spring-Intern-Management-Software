package kickstart.time;

import kickstart.orders.Orders;
import kickstart.room.Room;
import kickstart.staff.Staff;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Repräsentiert die Zuordnung eines Mitarbeiters, eines Auftrags und eines Raumes zu einer bestimmten Zeit
 */
@Entity
public class TimeSlot {
	private @Id @GeneratedValue long timeSlotId;
	private LocalDate date;
	private LocalDateTime time;
	private @ManyToOne Staff timeStaff;
	private @OneToOne Orders timeOrders;
	private @ManyToOne Room timeRoom;

	@SuppressWarnings("unused")
	private TimeSlot() {}

	/**
	 *
	 * @param time Zeitpunkt der Bearbeitung
	 * @param timeStaff bearbeitender Mitarbeiter
	 * @param timeOrders zu bearbeitender Auftrag
	 * @param timeRoom Raum der Bearbeitung
	 */
	public TimeSlot(LocalDateTime time, Staff timeStaff, Orders timeOrders, Room timeRoom){
		date = time.toLocalDate();
		this.time = time;
		this.timeStaff = timeStaff;
		this.timeOrders = timeOrders;
		this.timeRoom = timeRoom;
	}

	/**
	 *
	 * @return Gibt den Mitarbeiter zurück
	 */
	public Staff getTimeStaff() {
		return timeStaff;
	}

	/**
	 *
	 * @return Gibt den Auftrag zurück
	 */
	public Orders getTimeOrders() {
		return timeOrders;
	}

	/**
	 *
	 * @return Gibt den Raum zurück
	 */
	public Room getTimeRoom() {
		return timeRoom;
	}

	/**
	 *
	 * @return Gibt den Tag zurück
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 *
	 * @return Gibt die Zeit zurück
	 */
	public LocalDateTime getTime() {
		return time;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer zurück
	 */
	public long getTimeSlotId() {
		return timeSlotId;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer zurück
	 */
	public long getId() {
		return timeSlotId;
	}
}
