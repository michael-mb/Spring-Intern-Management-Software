package kickstart.staff;

import java.util.Optional;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Repräsentiert die Verwaltung der Mitarbeiter einer Filiale
 */
@Service
@Transactional
public class StaffManagement {
	private static StaffRepository staffRepository;
	private final UserAccountManager accounts;

	/**
	 * Erstellung der Personalverwaltungseinheit
	 * @param staffRepository Datenbank, die alle Mitarbeiter beinhaltet
	 * @param accounts Datenbank, die alle Benutzerkonten beinhaltet
	 */
	StaffManagement(StaffRepository staffRepository, UserAccountManager accounts){
		Assert.notNull(staffRepository, "PersonalRepository must not be null!");
		Assert.notNull(accounts, "UserAccountManager must not be null!");

		StaffManagement.staffRepository = staffRepository;
		this.accounts = accounts;
	}

	/**
	 * Neuer Mitarbeiter wird in der Datenbank hinzugefügt
	 * @param registrationForm Registrierungsformular des neuen Mitarbeiters
	 * @return Gibt den neu eingespeicherten Mitarbeiter zurück
	 */
	public Staff addStaff(RegistrationForm registrationForm){
		
		var password = UnencryptedPassword.of(registrationForm.getPasswort());
		var account = accounts.create(registrationForm.getName(), password, Role.of("USER") );
		if(registrationForm.getRolles().size() > 1 ) {
			 account.add(Role.of("ADMIN"));
		}
		return staffRepository.save(new Staff(account, registrationForm.getLohn(), registrationForm.getSkills(),
				registrationForm.getName(), registrationForm.getVorname(), registrationForm.getEmail()));
	}

	/**
	 * Mitarbeiter in der Datenbank wird mit den neuen Werten aktualisiert
	 * @param registrationForm Neues Registrierungsformular
	 * @param id Identifikationsnummer des zu aktualisierenden Mitarbeiters
	 * @return Gibt den aktualisierten Mitarbeiter zurück
	 */
	public Staff update(RegistrationForm registrationForm , Long id) {
		deleteStaff(id);
		var password = UnencryptedPassword.of(registrationForm.getPasswort());
		var account = accounts.create(registrationForm.getName(), password, Role.of("USER") );
		if(registrationForm.getRolles().size() > 1 ) {
			 account.add(Role.of("ADMIN"));
		}
		
		Staff toSave = new Staff(account, registrationForm.getLohn(), registrationForm.getSkills(),
				registrationForm.getName(), registrationForm.getVorname(), registrationForm.getEmail());
			toSave.setStaffId(id);
		return staffRepository.save(toSave);
	}

	/**
	 * Löscht einen Mitarbeiter und sein Benutzerkonto aus der Datenbank
	 * @param id Identifikationsnummer des zu löschenden Mitarbeiters
	 */
	public void deleteStaff(Long id){
		for(Staff person : staffRepository.findAll()){
			if(person.getStaffId() == id){
				staffRepository.delete(person);
				accounts.delete(accounts.findByUsername(person.getAccount()).get());
				
			}
		}
	}

	/**
	 * Findet Mitarbeiter in Datenbank mit entsprechender Identifikationsnummer
	 * @param id Identifikationsnummer
	 * @return Gibt den Mitarbeiter mit entsprechender Nummer wenn vorhanden oder null zurück
	 */
	public Optional<Staff> getStaffById(Long id) {
		return staffRepository.findById(id);
	}

	/**
	 * Findet Mitarbeiter in Datenbank mit entsprechendem Nachnamen
	 * @param name Nachname des Mitarbeiters
	 * @return Gibt den Mitarbeiter mit entsprechendem Nachnamen wenn vorhanden oder null zurück
	 */
	public static Staff getStaffByName(String name) {
		for(Staff s : staffRepository.findAll()) {
			if(s.getName().equals(name)) {
				
				return s ;
			}
		}
		return null ;
	}

	/**
	 * Iterierbares Objekt, um über alle Mitarbeiter in der Datenbank zu iterieren
	 * @return Gibt alle eingespeicherten Mitarbeiter zurück
	 */
	public Streamable<Staff> findAll() {
		return staffRepository.findAll();
	}
}
