package kickstart.Accountancy;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import kickstart.orders.Status;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import kickstart.orders.Orders;
import kickstart.staff.Skill;
import kickstart.staff.Staff;
import kickstart.staff.StaffManagement;

import static kickstart.staff.Skill.*;

/**
 * Repräsentiert die Verwaltung der Finanzen einer Filiale
 */
@Service
public class AccountancyManagement{
		
	private final Accountancy accountancy;
	private final StaffManagement staffManagement;
	private final BusinessTime businesstime;
	
	final LocalDateTime firstDate;

	private List<Double> balances;
	private List<String> indexes;

	/**
	 * Erstellung der Finanzverwaltungseinheit
	 * @param accountancy Datenbank, die alle Rechnungen beinhaltet
	 * @param staffManagement Personalverwaltungseinheit
	 * @param businesstime aktuelle Geschäftszeit
	 */
	AccountancyManagement(Accountancy accountancy, StaffManagement staffManagement, BusinessTime businesstime){
		
		Assert.notNull(accountancy, "Accountancy must not be null");
		Assert.notNull(staffManagement, "Staffmanagement must not be null");
		
		this.accountancy = accountancy;
		this.staffManagement = staffManagement;
		this.businesstime = businesstime;
		this.firstDate = businesstime.getTime().withDayOfMonth(1);
		balances = new ArrayList<>();
		indexes = new ArrayList<>();
	}

	/**
	 * Findet alle Rechnungen eines spezifischen Monats
	 * @param month Monat
	 * @return Gibt alle Rechnungen des jeweiligen Monats zurück
	 */
	public Streamable<AccountancyEntry> findAllBills(LocalDateTime month) {
		Interval interval = Interval.from(firstDate).to(businesstime.getTime());

	
		Map<Interval, Streamable<AccountancyEntry>> entries = accountancy.find(interval, Period.ofMonths(1));
		for(Interval key : entries.keySet()) {
			if (key.contains(month)) {
				return entries.get(key);
			}
		}
		return null;
	}

	/**
	 * Berechnet die Einnahmen bzw. Ausgaben eines spezifischen Monats
	 * @param month Monat
	 * @return Gibt Bilanz eines jeweiligen Monats zurück
	 */
	public MonetaryAmount getMonthlyEarnings(LocalDateTime month) {
		Interval interval = Interval.from(firstDate).to(businesstime.getTime());
		
		Map<Interval, MonetaryAmount> entries = accountancy.salesVolume(interval, Period.ofMonths(1));
		
		for(Interval key : entries.keySet()) {
			if (key.contains(month)) {
				return entries.get(key);
			}
		}
		return Money.of(0, Monetary.getCurrency("EUR"));
	}

	/**
	 * aktuelle Zeit wir um bestimmte Anzahl Monate verschoben
	 * @param monthOffset Verschiebung der Monate
	 * @return Gibt die aktuelle Zeit verschoben um bestimmt viele Monate zurück
	 */
	public LocalDateTime getOffset(int monthOffset) {
		return businesstime.getTime().plusMonths(monthOffset);
	}

	/**
	 * Fügt eine neue Rechnung hinzu
	 * @param value Einnahmen/Ausgaben der Rechnung
	 * @param description Beschreibung der Rechnung
	 */
	public void addBill (MonetaryAmount value, String description) {

		accountancy.add(new AccountancyEntry(value, description));
	}

	/**
	 * Fügt eine neue Rechnung hinzu
	 * @param order Auftrag
	 */
	public void addBill(Orders order) {

		//if created
		if (order.getStatus() == Status.CREATED) {
			accountancy.add(new AccountancyEntry(getPriceForService(order),
					"Auftrag " + order.getOrder_id() + ": " + order.getSkill()));
		}

		Double price;

		//if PickedUp
		if (order.getStatus() == Status.PICKED_UP) {
			price = -calculateDiscountForDelay(order);

			price += calculateStorageCost(order);
			
			accountancy.add(new AccountancyEntry(Money.of(price, Monetary.getCurrency("EUR")),
					"Nachzahlung Auftrag " + order.getOrder_id() + ": " + order.getSkill()));
		}
	}

	/**
	 * Berechnet den Preis eines Auftrags
	 * @param order Auftrag
	 * @return Gibt den Grundpreis für einen jeweiligen Auftrags zurück
	 */
	public MonetaryAmount getPriceForService(Orders order) {
		MonetaryAmount price = null;

		//initialen Preis für Service abrechnen
		if (order.getSkill() == null) {
			price = Money.of(0, Monetary.getCurrency("EUR"));
		} else {
			switch (order.getSkill()) {
				case PATCH:
					price = Money.of(99.99, Monetary.getCurrency("EUR"));
					break;
				case SEW:
					price = Money.of(65.99, Monetary.getCurrency("EUR"));
					break;
				case KEY:
					price = Money.of(49.99, Monetary.getCurrency("EUR"));
					break;
				case CLEAN:
					price = Money.of(19.99, Monetary.getCurrency("EUR"));
					break;
				case ELEKTRO:
					price = Money.of(229.99, Monetary.getCurrency("EUR"));
					break;
				case GRIND:
					price = Money.of(59.99, Monetary.getCurrency("EUR"));
					break;
			}
		}
		return price;
	}

	/**
	 * Berechnet den Rabatt für die Rückgabe eines fertigen Auftrags
	 * @param order Auftrag
	 * @return Gibt den Wert des Rabattes zurück
	 */
	public Double calculateDiscountForDelay(Orders order) {

		//Preis pro Service / 10 = 10%
		//10% * Anzahl verspäteter Tage = Rabatt
		double delay = ((getPriceForService(order).getNumber().doubleValue()) / 10)
				* ((order.getFinishDateCustomer().datesUntil(order.getFinishDateIntern())).count());
		delay = delay * 100;
		delay = Math.round(delay);
		return delay / 100;
	}

	/**
	 * Berechnet die Lagerkosten für die Rückgabe eines fertigen Auftrags
	 * @param order Auftrag
	 * @return Gibt den Wert der Lagerkosten zurück
	 */
	public Double calculateStorageCost(Orders order) {

		//Anzahl gelagerter Tage durch 7 um die Anzahl der (vollen) Wochen zu erhalten
		//Dann * 0.5 für 50ct pro Woche
		double storage = (((order.getFinishDateIntern().datesUntil(order.getPickUpDate())).count()) / 7.0) * 0.5;
		storage = storage * 100;
		storage = Math.round(storage);
		return storage / 100;
	}

	/**
	 * Berechnet die Gesamtbilanz
	 * @return Gibt die Gesamtbilanz zurück
	 */
	public MonetaryAmount getBalance() {
		MonetaryAmount balance = Money.of(0, Monetary.getCurrency("EUR"));
		
		for(AccountancyEntry entry : accountancy.findAll()) {
			balance = balance.add(entry.getValue());
		}
		
		return balance;
	}

	/**
	 * Berechnet das Gehalt eines spezifischen Mitarbeiters
	 * @param user Nachname des Mitarbeiters
	 * @return Gibt das Gehalt des Mitarbeiters zurück
	 */
	public MonetaryAmount getsalary(String user) {
		
		int salaryInt = 0;									
		
		for(Staff staff : staffManagement.findAll()) {
			if(user.equals(staff.getName())) {
				return staff.getSalary();
			}
		}
		
		return Money.of(salaryInt , Monetary.getCurrency("EUR"));
	}

	/**
	 * Fügt einen neuen Monat für das Liniendiagramm hinzu
	 * @param time aktuelle Zeit
	 */
	public void addMonth(LocalDateTime time){
		indexes.add(time.getMonth() + " " + time.getYear());
		balances.add(getBalance().getNumber().doubleValueExact());
	}

	/**
	 *
	 * @return Gibt die Liste der Monatsbilanzen zurück
	 */
	public List<Double> getBalances() {
		return balances;
	}

	/**
	 *
	 * @return Gibt die Liste der Monate zurück
	 */
	public List<String> getIndexes() {
		return indexes;
	}
}
