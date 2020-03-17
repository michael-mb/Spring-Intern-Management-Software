package kickstart.time;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.salespointframework.time.BusinessTime;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import kickstart.Accountancy.AccountancyManagement;
import kickstart.material.MaterialManagement;
import kickstart.orders.Orders;
import kickstart.orders.OrdersManagement;
import kickstart.orders.Status;
import kickstart.room.Room;
import kickstart.room.RoomManagement;
import kickstart.staff.Staff;
import kickstart.staff.StaffManagement;

/**
 * Repräsentiert die Verwaltung der Zeiten einer Filiale
 */
@Service
@Transactional
public class TimeSlotManagement {
	private final TimeSlotRepository timeSlots;
	private BusinessTime businessTime;
	private boolean run;
	private int lastMonth;

	private final StaffManagement staffManagement;
	private final OrdersManagement ordersManagement;
	private final AccountancyManagement accountancyManagement;
	private final MaterialManagement materialManagement;
	private final RoomManagement roomManagement;

	/**
	 * Erstellung der Personalverwaltungseinheit
	 * @param timeSlots Datenbank, die alle Zeiten beinhaltet
	 * @param businessTime Zeit des Systems
	 * @param staffManagement Personalverwaltungseinheit
	 * @param ordersManagement Auftragsverwaltungseinheit
	 * @param accountancyManagement Finanzverwaltungseinheit
	 * @param roomManagement Raumverwaltungseinheit
	 * @param materialManagement Materialverwaltungseinheit
	 */
	TimeSlotManagement(TimeSlotRepository timeSlots, BusinessTime businessTime,
					   StaffManagement staffManagement, OrdersManagement ordersManagement,
					   AccountancyManagement accountancyManagement, RoomManagement roomManagement ,
					   MaterialManagement materialManagement){
		Assert.notNull(timeSlots, "TimeSlotRepository must not be null!");
		Assert.notNull(businessTime, "BusinessTime must not be null!");
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(ordersManagement, "OrdersManagement must not be null!");
		Assert.notNull(accountancyManagement, "AccountacyManagement must not be null!");
		Assert.notNull(materialManagement, "MaterialManagement must not be null!");
		Assert.notNull(roomManagement, "RoomManagement must not be null!");
		
		this.timeSlots = timeSlots;
		this.businessTime = businessTime;
		run = false;
		lastMonth = businessTime.getTime().minusMonths(1).getMonthValue();
		this.staffManagement = staffManagement;
		this.ordersManagement = ordersManagement;
		this.accountancyManagement = accountancyManagement;
		this.roomManagement = roomManagement;
		this.materialManagement = materialManagement;
	}

	/**
	 * Ordnet einen zu bearbeitenden Auftrag seinen bearbeitenden Mitarbeiter, seinen Raum
	 * und die naheliegendste Zeit zu
	 * @param order Auftrag
	 */
	public void getFreeTimeSlot(Orders order) {
		if(order.getStatus() == Status.CREATED || order.getStatus() == Status.DECLINED) {
			Staff timeStaff = null;
			Room timeRoom = null;
			LocalDateTime now = businessTime.getTime().withMinute(0).withSecond(0).withNano(0);
			if (now.getHour() % 2 == 1) {
				now = now.plusHours(1);
			}
			LocalDateTime bestDate = now;

			Staff boss = staffManagement.findAll().toList().get(0);
			{
				boolean found = false;
				LocalDateTime date = now;
				while (!found) {
					boolean skip = false;
					if (date.getHour() < 8 || date.getHour() > 14
							|| date.getDayOfWeek() == DayOfWeek.SATURDAY
							|| date.getDayOfWeek() == DayOfWeek.SUNDAY) {
						skip = true;
					}
					if (!skip) {
						for (TimeSlot timeSlot : getAllByTime(date)) {
							if (timeSlot.getTimeStaff() == boss) {
								skip = true;
							}
						}
					}
					if (!skip) {
						ArrayList<Room> rooms = new ArrayList<>(roomManagement.findAll().toList());
						for (TimeSlot timeSlot : getAllByTime(date)) {
							for (Room room : roomManagement.findAll()) {
								if (timeSlot.getTimeRoom() == room) {
									rooms.remove(room);
								}
							}
						}
						if (!rooms.isEmpty()) {
							timeStaff = boss;
							timeRoom = rooms.get(0);
							found = true;
							bestDate = date;
						}
					}
					date = date.plusHours(2);
				}
			}

			for (Staff staff : staffManagement.findAll()) {
				if (staff.getSkills().contains(order.getSkill())) {
					boolean found = false;
					LocalDateTime date = now;
					while (!found && bestDate.isAfter(date)) {
						boolean skip = false;
						if (date.getHour() < 8 || date.getHour() > 14
								|| date.getDayOfWeek() == DayOfWeek.SATURDAY
								|| date.getDayOfWeek() == DayOfWeek.SUNDAY) {
							skip = true;
						}
						if (!skip) {
							for (TimeSlot timeSlot : getAllByTime(date)) {
								if (timeSlot.getTimeStaff() == staff) {
									skip = true;
								}
							}
						}
						if (!skip) {
							ArrayList<Room> rooms = new ArrayList<>(roomManagement.findAll().toList());
							for (TimeSlot timeSlot : getAllByTime(date)) {
								for (Room room : roomManagement.findAll()) {
									if (timeSlot.getTimeRoom() == room) {
										rooms.remove(room);
									}
								}
							}
							if (!rooms.isEmpty()) {
								timeStaff = staff;
								timeRoom = rooms.get(0);
								found = true;
								bestDate = date;
							}
						}
						date = date.plusHours(2);
					}
				}
			}
			TimeSlot timeSlot = createTimeSlot(bestDate, timeStaff, order, timeRoom);
			ordersManagement.setFinishDate(order, bestDate.toLocalDate());


			int date = timeSlot.getDate().getDayOfWeek().getValue();
			int time = (timeSlot.getTime().getHour() - 6) / 2;
			int slot;
			if (timeSlot.getTimeRoom().getName().equals("Room1")) {
				slot = 1;
			} else if (timeSlot.getTimeRoom().getName().equals("Room2")) {
				slot = 2;
			} else {
				throw new IllegalStateException();
			}
			order.updateTimeSlot(time, date, slot);
			ordersManagement.updateOrder(order);
		}
	}

	/**
	 * Erstellung eines neuen Eintrags
	 * @param date Zeit des Eintrags
	 * @param staff Mitarbeiter des Eintrags
	 * @param orders Auftrag des Eintrags
	 * @param room Raum des Eintrags
	 * @return
	 */
	public TimeSlot createTimeSlot(LocalDateTime date, Staff staff, Orders orders, Room room){
		TimeSlot timeSlot = new TimeSlot(date, staff, orders, room);
		addTimeSlot(timeSlot);
		return timeSlot;
	}

	/**
	 * Speichert einen neuen Eintrag in der Datenbank
	 * @param timeslot neuer Eintrag
	 * @return Gibt den neuen Eintrag zurück
	 */
	public TimeSlot addTimeSlot(TimeSlot timeslot){
		return timeSlots.save(timeslot);
	}

	/**
	 * Löscht einen Eintrag aus der Datenbank
	 * @param timeslot zu löschender Eintrag
	 */
	public void deleteTimeSlot(TimeSlot timeslot){
		timeSlots.delete(timeslot);
	}

	/**
	 * Iterierbares Objekt, um über alle Einträge in der Datenbank zu iterieren
	 * @return Gibt alle eingespeicherten Einträge zurück
	 */
	public Streamable<TimeSlot> findAll() {
		return timeSlots.findAll();
	}

	/**
	 *
	 * @param time Zeitpunkt
	 * @return Gibt alle Einträge der spezifischen Zeit zurück
	 */
	public List<TimeSlot> getAllByTime(LocalDateTime time) {
		ArrayList<TimeSlot> result = new ArrayList<>();

		for(TimeSlot timeSlot : findAll()) {
			if(timeSlot.getTime().isEqual(time)) {
				result.add(timeSlot);
			}
		}

		return result;
	}

	/**
	 *
	 * @param id Identifikationsnummer des Mitarbeiters
	 * @return Gibt alle Einträge, die der Mitarbeiter mit entsprechender Identifikationsnummer
	 * bearbeitet zurück
	 */
	public List<TimeSlot> getAllByStaff(Long id) {
		ArrayList<TimeSlot> result = new ArrayList<>();

		for(TimeSlot timeSlot : findAll()) {
			if(timeSlot.getTimeStaff().getStaffId() == id) {
				result.add(timeSlot);
			}
		}

		return result;
	}

	/**
	 *
	 * @return Gibt die aktuelle Zeit zurück
	 */
	public LocalDateTime getTime(){
		return businessTime.getTime();
	}

	/**
	 * Setzt das automatische Vorspulen neu
	 * @param run automatisches Vorspulen ja/nein
	 */
	public void setRun(boolean run) {
		this.run = run;
	}

	/**
	 *
	 * @return Gibt an, ob automatisches Vorspulen aktiviert ist (true) oder nicht (false)
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * Spult einen Tag vor
	 */
	public void skipDay() {
		businessTime.forward(Duration.ofDays(1));
	}

	/**
	 * Aktualisiert das komplette System (Zeitplan, Aufträge, Finanzen, Materialien) regelmäßig
	 */
	@Scheduled(fixedRate = 1000)
	public void changeTime() {
		if(run) {
			businessTime.forward(Duration.ofHours(1).minusSeconds(5));
		}

		if(businessTime.getTime().getDayOfMonth() == 28 &&
			businessTime.getTime().getMonthValue() != lastMonth) {
			for (Staff staff : staffManagement.findAll()) {
				accountancyManagement.addBill(staff.getSalary().negate(),
						"Gehalt für " + staff.getFirstName() + " " + staff.getName());
			}
			lastMonth = businessTime.getTime().getMonthValue();
			accountancyManagement.addMonth(businessTime.getTime());

			accountancyManagement.addBill(Money.of(materialManagement.renewStock(), Monetary.getCurrency("EUR")),
					"Monatliche Nachbestellung");
		}

		LocalDateTime time = businessTime.getTime();
		for(TimeSlot timeSlot : timeSlots.findAll()) {
			if(!timeSlot.getTime().isAfter(time)) {
				if(timeSlot.getTimeOrders().getStatus() == Status.CREATED
					|| timeSlot.getTimeOrders().getStatus() == Status.DECLINED) {
					deleteTimeSlot(timeSlot);
					ordersManagement.changeStatus(timeSlot.getTimeOrders().getOrder_id(),
							Status.CREATED);
					getFreeTimeSlot(timeSlot.getTimeOrders());
				}

				if(timeSlot.getTimeOrders().getStatus() == Status.ACCEPTED) {
					ordersManagement.changeStatus(timeSlot.getTimeOrders().getOrder_id(),
							Status.DONE);
					timeSlot.getTimeOrders().setStatus(Status.DONE);
				}
			}

			if(!timeSlot.getTime().isAfter(time.minusDays(7)) &&
				timeSlot.getTimeOrders().getStatus() == Status.DONE) {
					ordersManagement.changeStatus(timeSlot.getTimeOrders().getOrder_id(),
							Status.STORED);
					timeSlot.getTimeOrders().setStatus(Status.STORED);
			}

			if(!timeSlot.getTime().isAfter(time.minusDays(97)) &&
				timeSlot.getTimeOrders().getStatus() == Status.STORED) {
					ordersManagement.changeStatus(timeSlot.getTimeOrders().getOrder_id(),
							Status.DONATED);
					deleteTimeSlot(timeSlot);
			}
		}
	}
}