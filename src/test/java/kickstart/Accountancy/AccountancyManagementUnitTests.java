package kickstart.Accountancy;

import kickstart.orders.Orders;
import kickstart.orders.SubCategory;
import kickstart.staff.Skill;
import kickstart.staff.StaffManagement;
import kickstart.time.TimeSlotManagement;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class AccountancyManagementUnitTests {
	@Autowired AccountancyManagement accountancyManagement;
	@Autowired TimeSlotManagement timeSlotManagement;
	@Autowired StaffManagement staffManagement;

	@BeforeAll
	void setup(){
		accountancyManagement.addBill(Money.of(10000, Monetary.getCurrency("EUR")),"Startguthaben");
	}

	@Test
	void findAllBills(){
		assertNotNull(accountancyManagement.findAllBills(timeSlotManagement.getTime()));
		assertNull(accountancyManagement.findAllBills(timeSlotManagement.getTime().plusMonths(1)));
	}

	@Test
	void getMonthEarning(){
		timeSlotManagement.setRun(false);
		LocalDateTime time = timeSlotManagement.getTime();
		MonetaryAmount before = accountancyManagement.getMonthlyEarnings(time);

		accountancyManagement.addBill(Money.of(10, Monetary.getCurrency("EUR")), "abc");
		assertEquals(before.add(Money.of(10, Monetary.getCurrency("EUR"))),
				accountancyManagement.getMonthlyEarnings(time));
		assertEquals(Money.of(0, Monetary.getCurrency("EUR")),
				accountancyManagement.getMonthlyEarnings(time.plusMonths(1)));
	}

	@Test
	void getOffset(){
		assertEquals(timeSlotManagement.getTime().plusMonths(2).toLocalDate(),
				accountancyManagement.getOffset(2).toLocalDate());
	}

	@Test
	void addBill(){
		timeSlotManagement.setRun(false);
		LocalDateTime time = timeSlotManagement.getTime().minusMinutes(1);
		int before = accountancyManagement.findAllBills(time).toList().size();

		accountancyManagement.addBill(Money.of(10, Monetary.getCurrency("EUR")), "abc");
		assertEquals(before + 1,
				accountancyManagement.findAllBills(time).toList().size());

		Orders order = new Orders(time.toLocalDate(),
				"abc", Skill.CLEAN, SubCategory.WAESCHE, "Oliver Scholz",
				"oliver.scholz1@mailbox.tu-dresden.de");

		accountancyManagement.addBill(order);
		assertEquals(before + 2,
				accountancyManagement.findAllBills(time).toList().size());
	}

	@Test
	void getBalance(){
		assertTrue(accountancyManagement.getBalance().isPositive());
	}

	@Test
	void getSalary(){
		assertEquals(staffManagement.findAll().toList().get(0).getSalary(),
				accountancyManagement.getsalary(staffManagement.findAll().toList().get(0).getName()));
	}

	@Test
	void addMonth(){
		int before1 = accountancyManagement.getBalances().size();
		int before2 = accountancyManagement.getIndexes().size();

		accountancyManagement.addMonth(timeSlotManagement.getTime().plusMonths(2));
		assertEquals(before1 + 1,
				accountancyManagement.getBalances().size());
		assertEquals(before2 + 1,
				accountancyManagement.getIndexes().size());
	}
}
