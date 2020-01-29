package kickstart.Accountancy;

import java.time.LocalDateTime;
import java.time.Month;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysema.commons.lang.Assert;

/**
 * Internetzugriffskontrolleinheit für alle Angelegenheiten der Finanzen
 */
@Controller
public class AccountancyController{
	
	private final AccountancyManagement accountancyManagement;
	private int monthOffset = 0;

	/**
	 * Erstellung der Internetzugriffskontrolleinheit einer Filiale
	 * @param accountancyManagement Finanzverwaltungseinheit einer Filiale
	 */
	AccountancyController(AccountancyManagement accountancyManagement) {

		Assert.notNull(accountancyManagement, "AccountancyManagement must not be null!");
		
		this.accountancyManagement = accountancyManagement;
	}

	/**
	 * Zeigt die Finanzübersicht an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return finanzialoverview.html
	 */
	@GetMapping("/financialoverview")
	String showAllBills(Model model, Authentication authentication) {
		LocalDateTime month = accountancyManagement.getOffset(monthOffset);
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		model.addAttribute("salary", accountancyManagement.getsalary(authentication.getName()));
		model.addAttribute("month", month.getMonth().name() + " " + month.getYear());
		model.addAttribute("entries", accountancyManagement.findAllBills(month));
		model.addAttribute("balance", accountancyManagement.getBalance());
		model.addAttribute("earnings", accountancyManagement.getMonthlyEarnings(month));
		model.addAttribute("balances", accountancyManagement.getBalances());
		model.addAttribute("indexes", accountancyManagement.getIndexes());

		
		return "financialoverview";
	}

	/**
	 * Springt einen Monat zurück
	 * @return Weiterleitung zur Finanzübersicht
	 */
	@GetMapping("/financialoverview/previous")
	String getPrevious(){
		monthOffset -= 1;
		return "redirect:/financialoverview";
	}

	/**
	 * Springt einen Monat vor
	 * @return Weiterleitung zur Finanzübersicht
	 */
	@GetMapping("/financialoverview/next")
	String getNext(){
		monthOffset += 1;
		return "redirect:/financialoverview";
	}

	/**
	 * Springt zum aktuellen Monat
	 * @return Weiterleitung zur Finanzübersicht
	 */
	@GetMapping("/financialoverview/today")
	String resetOffset(){
		monthOffset = 0;
		return "redirect:/financialoverview";
	}
}
