package kickstart.time;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Internetzugriffskontrolleinheit für alle Angelegenheiten der Zeiten
 */
@Controller
public class TimeSlotController {
	private final TimeSlotManagement timeSlotManagement;

	/**
	 * Erstellung der Internetzugriffskontrolleinheit einer Filiale
	 * @param timeSlotManagement Zeitplanverwaltungseinheit einer Filiale
	 */
	TimeSlotController(TimeSlotManagement timeSlotManagement){
		Assert.notNull(timeSlotManagement, "TimeSlotManagement must not be null!");

		this.timeSlotManagement = timeSlotManagement;
	}

	/**
	 * Deaktiviert das automatische Vorspulen der Zeit des Systems
	 * @param model Zu übermittelnde Daten
	 * @return Weiterleitung zur Hauptseite
	 */
	@GetMapping("/stop")
	String stop(Model model){
		timeSlotManagement.setRun(false);
		model.addAttribute("time", false);
		return "redirect:/";
	}

	/**
	 * Aktiviert das automatische Vorspulen der Zeit des Systems
	 * @param model Zu übermittelnde Daten
	 * @return Weiterleitung zur Hauptseite
	 */
	@GetMapping("/start")
	String start(Model model){
		timeSlotManagement.setRun(true);
		model.addAttribute("time", true);
		return "redirect:/";
	}

	/**
	 * Spult das System einen Tag vor
	 * @return Weiterleitung zur Hauptseite
	 */
	@GetMapping("/skipDay")
	String skipDay() {
		timeSlotManagement.skipDay();
		return "redirect:/";
	}
}
