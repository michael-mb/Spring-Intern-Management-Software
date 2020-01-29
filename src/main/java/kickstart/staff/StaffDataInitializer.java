package kickstart.staff;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Repräsentiert einen vordefinierten Anfangszustand des Personals einer Filiale
 */
@Component
@Order(10)
public class StaffDataInitializer implements DataInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(StaffDataInitializer.class);

	private final UserAccountManager accountManager;
	private final StaffManagement staffManagement;

	/**
	 * Erstellung des Anfangszustandes
	 * @param accountManager Benutzerkontenverwaltungseinheit einer Filiale
	 * @param staffManagement Personalverwaltungseinheit einer Filiale
	 */
	StaffDataInitializer(UserAccountManager accountManager, StaffManagement staffManagement) {
		Assert.notNull(accountManager, "accountManager must not be null!");
		Assert.notNull(staffManagement, "StaffRepository must not be null!");

		this.accountManager = accountManager;
		this.staffManagement = staffManagement;
	}

	/**
	 * Mitarbeiter des Anfangszustands werden hinzugefügt
	 */
	@Override
	public void initialize() {
		if (accountManager.findByUsername("Boss").isPresent()) {
			return;
		}

		LOG.info("Creating default staff.");

		List.of(
				new RegistrationForm("1000", "true", "true", "true", "true",
						"true", "true", "Boss", "Boss",
						"abc", "abc", "boss@betrieb.de")
		//		new RegistrationForm("500", "true", null, null, "true",
		//				"true", "true", "Mustermann", "Max",
		//				"abc", "abc", "max.mustermann@tu-dresden.de"),
		//		new RegistrationForm("600", null ,null, "true", null,
		//				"true", null, "Musterfrau", "Anna",
		//				"abc", "abc", "anna.musterfrau@tu-dresden.de"),
		//		new RegistrationForm("200", null ,null, null, null,
		//				null, "true", "Musterkind", "kind",
		//				"abc", "abc", "kind.musterfrau@tu-dresden.de")
		).forEach(staffManagement::addStaff);
	}
}
