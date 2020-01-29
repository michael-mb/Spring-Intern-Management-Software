package kickstart.staff;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.salespointframework.useraccount.Role;

/**
 * Repräsentiert das Eingabeformular bei der Registration eines neuen Mitarbeiters
 */
public class RegistrationForm {
	private final String salary;
	private HashSet<Skill> skills;
	private final String name;
	private final String firstName;
	private final String passwort;
	private final String passwortValid;
	private final String email;
	private List<Role> role;


	/**
	 *
	 * @param salary Eingabefeld für das Gehalt des Mitarbeiters
	 * @param naehen Checkboxfeld für Fähigkeit: Nähservice
	 * @param schluessel Checkboxfeld für Fähigkeit: Schlüsseldienst
	 * @param elektro Checkboxfeld für Fähigkeit: Elektrowerkstatt
	 * @param flicken Checkboxfeld für Fähigkeit: Flickservice
	 * @param schleifen Checkboxfeld für Fähigkeit: Scherenschleiferei
	 * @param reinigen Checkboxfeld für Fähigkeit: Reinigungsservice
	 * @param name Eingabefeld für den Nachnamen des Mitarbeiters
	 * @param firstName Eingabefeld für den Vornamen des Mitarbeiters
	 * @param passwort Eingabefeld für das Passwort
	 * @param passwortValid Überprüfungsfeld für das Passwort
	 * @param email Eingabefeld für die Email-Adresse des Mitarbeiters
	 */
	public RegistrationForm(String salary, String naehen, String schluessel, String elektro,
							String flicken, String schleifen, String reinigen, String name,
							String firstName, String passwort, String passwortValid, String email){
		this.salary = salary;
		skills = new HashSet<>();
		role = new ArrayList<>();
		if(naehen != null){
			skills.add(Skill.SEW);
		}
		if(schluessel != null){
			skills.add(Skill.KEY);
		}
		if(elektro != null){
			skills.add(Skill.ELEKTRO);
		}
		if(flicken != null){
			skills.add(Skill.PATCH);
		}
		if(schleifen != null){
			skills.add(Skill.GRIND);
		}
		if(reinigen != null){
			skills.add(Skill.CLEAN);
		}
		
		this.name = name;
		this.firstName = firstName;
		this.passwort = passwort;
		this.passwortValid = passwortValid;
		this.email = email;
		if(name.equals("Boss")){
			role.add( Role.of("USER"));
			role.add( Role.of("ADMIN"));
		}else{
			role.add( Role.of("USER"));
		}
	}

	/**
	 *
	 * @return Gibt den Lohn des Mitarbeiters zurück
	 */
	public int getLohn() {
		return Integer.parseInt(salary);
	}

	/**
	 *
	 * @return Gibt alle Fähigkeiten des Mitarbeiters zurück
	 */
	public HashSet<Skill> getSkills() {
		return skills;
	}

	/**
	 *
	 * @return Gibt den Nachnamen des Mitarbeiters zurück
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @return Gibt den Vornamen des Mitarbeiters zurück
	 */
	public String getVorname() {
		return firstName;
	}

	//public String getUsername() {
	//	return username;
	//}

	//public void setUsername(String username){
	//	this.username = username;
	//}

	/**
	 *
	 * @return Gibt das eingegebene Passwort zurück
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 *
	 * @return Gibt das eingegebene Überpfüfungsfeld des Passwortes zurück
	 */
	public String getPasswortValid() {
		return passwortValid;
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
	 * @return Gibt alle Rollen des Mitarbeiters zurück
	 */
	public List<Role> getRolles() {
		return role;
	}
}
