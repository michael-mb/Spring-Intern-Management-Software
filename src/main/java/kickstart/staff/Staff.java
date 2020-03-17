package kickstart.staff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;

/**
 * Repräsentiert einen Mitarbeiter einer Filiale
 */
@Entity
public class Staff {
	private MonetaryAmount salary;
	private HashSet<Skill> skills;
	private @Id @GeneratedValue long staffId;
	private String name;
	private String firstName;
	private String email;

	private String account;
	private ArrayList<Role> roles;
	private UserAccountIdentifier accountId;

	@SuppressWarnings("unused")
	private Staff() {}

	/**
	 *
	 * @param account Benutzername des Mitarbeiters, um einen Login zu gewährleisten
	 * @param salary Gehalt des Mitarbeiters
	 * @param skills Fähigkeiten des Mitarbeiters
	 * @param name Nachname des Mitarbeiters
	 * @param firstName Vorname des Mitarbeiters
	 * @param email Email-Adresse des Mitarbeiters
	 */
	public Staff(UserAccount account, int salary, HashSet<Skill> skills, String name, String firstName, String email){
		this.account = account.getUsername();
		this.accountId = account.getId();
		this.roles = new ArrayList<>(account.getRoles().toList());
		this.skills = skills;
		this.salary = Money.of(salary, Monetary.getCurrency("EUR"));
		this.name = name;
		this.firstName = firstName;
		this.email = email;
	
	}

	/**
	 * Setzt das Gehalt eines Mitarbeiters neu
	 * @param salary neues Gehalt des Mitarbeiters
	 */
	public void setSalary(int salary) {
		this.salary = Money.of(salary, Monetary.getCurrency("EUR"));
	}

	/**
	 *
	 * @return Gibt das Gehalt des Mitarbeiters als Ganzzahl zurück
	 */
	public NumberValue getIntSalary() {
		return salary.getNumber();
	}

	/**
	 * Setzt die Fähigkeiten eines Mitarbeiters neu
	 * @param skills neue Fähigkeiten des Mitarbeiters
	 */
	public void setSkills(HashSet<Skill> skills) {
		this.skills = skills;
	}

	/**
	 *
	 * @return Gibt das Gehalt des Mitarbeiters zurück
	 */
	public MonetaryAmount getSalary() {
		return salary;
	}

	/**
	 *
	 * @return Gibt alle Fähigkeiten des Mitarbeiters zurück
	 */
	public HashSet<Skill> getSkills() {
		return skills;
	}

	/**
	 * Setzt den Nachnamen eines Mitarbeiters neu
	 * @param name neuer Nachname des Mitarbeiters
	 */
	public void setName(String name) {
		this.name = name ;
	}

	/**
	 *
	 * @return Gibt den Nachnamen des Mitarbeiters zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Vornamen eines Mitarbeiters neu
	 * @param firstName neuer Vorname des Mitarbeiters
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 *
	 * @return Gibt den Vornamen des Mitarbeiters zurück
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setzt die Email-Adresse eines Mitarbeiters neu
	 * @param email neue Email-Adresse des Mitarbeiters
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 *
	 * @return Gibt die Email-Adresse des Mitarbeiters zurück
	 */
	public String getEmail() {
		return email;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer des Mitarbeiters zurück
	 */
	public long getStaffId() {
		return staffId;
	}

	/**
	 * Setzt die Identifikationsnummer des Mitarbeiters neu
	 * @param id Neue Identifikationsnummer des Mitarbeiters
	 */
	public void setStaffId(Long id) {
		this.staffId = id ;
	}

	/**
	 *
	 * @return Gibt den Benutzernamen des Mitarbeiters zurück
	 */
	public String getAccount() {
		return account;
	}

	/**
	 *
	 * @return Gibt alle Rollen des Mitarbeiters zurück
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 *
	 * @return Gibt an, ob der Mitarbeiter ein Admin ist (true) oder nicht (false)
	 */
	public boolean isAdmin() {
		return getRoles().size() > 1;
	}

	/**
	 *
	 * @return Gibt die Identifikationsnummer des Mitarbeiters zurück
	 */
	public UserAccountIdentifier getAccountId() {
		return this.accountId;
	}

	/**
	 *
	 * @return Gibt alle Werte des Mitarbeiters aufgelistet zurück
	 */
	public String toString() {
		String str;
		String roles = "";
		
		for(Role role : getRoles()) {
			roles += role.toString() + " , ";
		}

		ArrayList<Skill> representSkills = new ArrayList<>(skills);
		representSkills.sort(Enum::compareTo);
		
		str = "id: " + staffId + " name: " + name + " vorname: " + firstName +
				" skills: " + representSkills + " lohn: " + salary + " email: " +
				email + " roles: " + roles;
		return str;
	}
}
