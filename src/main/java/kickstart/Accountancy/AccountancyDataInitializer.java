package kickstart.Accountancy;

import kickstart.staff.StaffDataInitializer;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.money.Monetary;
import java.time.LocalDateTime;

/**
 * Repräsentiert einen vordefinierten Anfangszustand der Finanzen einer Filiale
 */
@Component
@Order(10)
public class AccountancyDataInitializer implements DataInitializer{
	private static final Logger LOG = LoggerFactory.getLogger(StaffDataInitializer.class);
	
	private final AccountancyManagement accountancyManagement;
	private final Accountancy accountancy;

	/**
	 * Erstellung des Anfangszustandes
	 * @param accountancyManagement Finanzverwaltungseinheit einer Filiale
	 * @param accountancy Datenbank der Finanzeinträge
	 */
	AccountancyDataInitializer(AccountancyManagement accountancyManagement, Accountancy accountancy){
		Assert.notNull(accountancyManagement, "Accountancy must not be null");

		this.accountancyManagement = accountancyManagement;
		this.accountancy = accountancy;
	}

	/**
	 * Startguthaben wird hinzugefügt
	 */
	@Override
	public void initialize() {
		if(accountancy.findAll().toList().size() != 0){
			accountancyManagement.addMonth(LocalDateTime.now().minusMonths(1));
			return;
		}

		LOG.info("Creating default accountancy.");

		accountancyManagement.addBill(Money.of(10000, Monetary.getCurrency("EUR")),"Startguthaben");
		accountancyManagement.addMonth(LocalDateTime.now().minusMonths(1));
	}
	
}
