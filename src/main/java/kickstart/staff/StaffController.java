package kickstart.staff;

import kickstart.orders.Orders;
import kickstart.time.TimeSlot;
import kickstart.time.TimeSlotManagement;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Internetzugriffskontrolleinheit für alle Angelegenheiten des Personals
 */
@Controller
public class StaffController {
	private final StaffManagement staffManagement;
	private final TimeSlotManagement timeSlotManagement;
	private Long personalId ;
	private String kindOfMistake;

	/**
	 * Erstellung der Internetzugriffskontrolleinheit einer Filiale
	 * @param staffManagement Personalverwaltungseinheit einer Filiale
	 * @param timeSlotManagement Zeitplanverwaltungseinheit einer Filiale
	 */
	StaffController(StaffManagement staffManagement, TimeSlotManagement timeSlotManagement){
		Assert.notNull(staffManagement, "CustomerManagement must not be null!");
		Assert.notNull(timeSlotManagement, "TimeSlotManagement must not be null!");

		this.staffManagement = staffManagement;
		this.timeSlotManagement = timeSlotManagement;
		this.kindOfMistake = "";
	}

	/**
	 * Zeigt die Hauptseite an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return index.html
	 */
	@GetMapping("/")
	public String showIndex(Model model , Authentication authentication) {
		model.addAttribute("time", timeSlotManagement.isRun());
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
		return "index";
	}

	/**
	 * Registriert neuen Mitarbeiter oder zeigt Fehler im Registrationsformular an
	 * @param form Registrierungsformular
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return Weiterleitung zur Hauptseite oder register.html
	 */
	@PostMapping("/register")
	String getRegisterForm(RegistrationForm form, Model model, Authentication authentication){

		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		if(formVerification(form)) {
			if(passVerification(form)) {
				staffManagement.addStaff(form);
			}else {
				model.addAttribute("passVerification",true);
				return "register";
			}
			
		}else {
			model.addAttribute("formVerification",true);
			model.addAttribute("kindOfMistake",kindOfMistake);
			return "register";
		}
		
		return "redirect:/";
	}

	/**
	 * Zeigt die Registrationsseite an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return register.html
	 */
	@GetMapping("/register") 
	String showregisterPage(Model model , Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
			model.addAttribute("users",staffManagement.findAll());
		}
		return "register";
	}

	/**
	 * Zeigt die Loginseite an
	 * @return login.html
	 */
	@GetMapping("/login")
	String showLoginPage(){
		return "login";
	}

	/**
	 * Zeigt die Übersicht über alle Mitarbeiter an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return personallist.html
	 */
	@GetMapping("/personallist")
	String showPersonalPage(Model model , Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
			model.addAttribute("users",staffManagement.findAll());
		}
		return "personallist";
	}

	/**
	 * Löscht einen Mitarbeiter mit entsprechender Identifikationsnummer
	 * und verteilt alle diesem Mitarbeiter zugewiesenen Aufträge neu
	 * @param id Identifikationsnummer
	 * @return Weiterleitung zur Übersicht über alle Mitarbeiter
	 */
	@GetMapping("/delete/{id}")
	String deleteStaff(@PathVariable Long id){
		ArrayList<Orders> orders = new ArrayList<>();
		for(TimeSlot t : timeSlotManagement.findAll()) {
			Staff staff = staffManagement.getStaffById(id).get();
			if(t.getTimeStaff().equals(staff)) {
				timeSlotManagement.deleteTimeSlot(t);
				orders.add(t.getTimeOrders());
			}
		}
		staffManagement.deleteStaff(id);
		for(Orders order : orders) {
			timeSlotManagement.getFreeTimeSlot(order);
		}
		return "redirect:/personallist";
	}

	/**
	 * Zeigt Aktualisierungsformular für einen Mitarbeiter mit entsprechender Identifikationsnummer an
	 * @param id Identifikationsnummer
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return updatePersonal.html
	 */
	@GetMapping("/update/{id}")
	String updateStaff(@PathVariable Long id , Model model , Authentication authentication){
		
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}		
		this.personalId = id ;
		model.addAttribute("personal" , staffManagement.getStaffById(id).get());
		return "updatePersonal";
	}

	/**
	 * Aktualisiert einen Mitarbeiter mit zuvor angegebener Identifikationsnummer
	 * @param form Aktualisierungsformular
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return Weiterleitung zur Übersicht aller Mitarbeiter
	 */
	@PostMapping("/updatePersonal")
	String updateStaffForm(RegistrationForm form, Model model, Authentication authentication){

		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		ArrayList<Orders> orders = new ArrayList<>();
		for(TimeSlot t : timeSlotManagement.findAll()) {
			Staff staff = staffManagement.getStaffById(personalId).get();
			if(t.getTimeStaff().equals(staff)) {
				timeSlotManagement.deleteTimeSlot(t);
				orders.add(t.getTimeOrders());
			}
		}
		staffManagement.update(form, personalId);
		for(Orders order : orders) {
			timeSlotManagement.getFreeTimeSlot(order);
		}

		return "redirect:/personallist";
	}

	/**
	 * Verifiziert ein Registrierungsformular
	 * @param form Registrierungsformular
	 * @return Gibt false zurück, wenn Nachname oder Email-Adresse schon vergeben sind, sonst true
	 */
	private boolean formVerification(RegistrationForm form){
		
		for(Staff staff : staffManagement.findAll()) {
			if(form.getName().equals(staff.getName())) {
				kindOfMistake = "Diese Name wurde schon vergeben !";
				return false ;
			}else if(form.getEmail().equals(staff.getEmail())) {
				kindOfMistake = "Diese Email wurde schon vergeben !";
				return false ;
			}
		}
		
		if(form.getLohn() <= 0 || form.getLohn() > 2000000000) {
			kindOfMistake = "Der Gehaltsangabe stimmt nicht überein !";
			return false ;
		}
		
		if(form.getName().isBlank() || form.getVorname().isBlank()) {
			kindOfMistake = "Der Nameangabe stimmt nicht überein!";
			return false ;
		}
		
		return true;
	}

	/**
	 * Verifiziert die Passworteingabe
	 * @param form Registrierungsformular
	 * @return Gibt false zurück, wenn Passwort und Passwortüberprüfungseingabe nicht übereinstimmen, sonst true
	 */
	private boolean passVerification(RegistrationForm form){
		return form.getPasswort().equals(form.getPasswortValid());
	}
}
