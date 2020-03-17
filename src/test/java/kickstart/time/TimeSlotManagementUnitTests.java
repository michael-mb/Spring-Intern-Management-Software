package kickstart.time;

import kickstart.Accountancy.AccountancyManagement;
import kickstart.orders.*;
import kickstart.room.Room;
import kickstart.room.RoomManagement;
import kickstart.staff.RegistrationForm;
import kickstart.staff.Skill;
import kickstart.staff.Staff;
import kickstart.staff.StaffManagement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.money.MonetaryAmount;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kickstart.staff.Skill.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class TimeSlotManagementUnitTests {
	@Autowired TimeSlotManagement timeSlotManagement;
	@Autowired OrdersManagement ordersManagement;
	@Autowired StaffManagement staffManagement;
	@Autowired RoomManagement roomManagement;
	@Autowired AccountancyManagement accountancyManagement;
	@Autowired BusinessTime businessTime;

	@BeforeAll
	void setup(){
		Orders order = ordersManagement.addOrder(new OrderCreationFrom("abc", Skill.CLEAN,
				"abc", "abc", SubCategory.WAESCHE));
		List.of(
				new RegistrationForm("500", "true", null, null, "true",
						"true", "true", "Mustermann", "Max",
						"abc", "abc", "max.mustermann@tu-dresden.de"),
				new RegistrationForm("600", null ,null, "true", null,
						"true", null, "Musterfrau", "Anna",
						"abc", "abc", "anna.musterfrau@tu-dresden.de"),
				new RegistrationForm("200", null ,null, null, null,
						null, "true", "Musterkind", "kind",
						"abc", "abc", "kind.musterfrau@tu-dresden.de")
		).forEach(staffManagement::addStaff);
		timeSlotManagement.getFreeTimeSlot(order);
	}

	@Test
	void getFreeTimeSlot(){
		OrderCreationFrom orderCreationFrom = new OrderCreationFrom("", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden,de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderCreationFrom);
		Orders order = ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1);
		int before = timeSlotManagement.findAll().toList().size();

		timeSlotManagement.getFreeTimeSlot(order);
		assertEquals(before + 1, timeSlotManagement.findAll().toList().size());

		TimeSlot timeSlot = timeSlotManagement.findAll().toList()
				.get(timeSlotManagement.findAll().toList().size() - 1);
		assertEquals(timeSlot.getTimeOrders().getDescription(),
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getDescription());
		assertEquals(timeSlot.getTimeRoom().getName(),
				roomManagement.findAll().toList().get(0).getName());
		assertEquals(timeSlot.getTimeStaff().getEmail(),
				staffManagement.findAll().toList().get(0).getEmail());

		timeSlotManagement.getFreeTimeSlot(order);
		assertEquals(before + 2, timeSlotManagement.findAll().toList().size());

		timeSlot = timeSlotManagement.findAll().toList()
				.get(timeSlotManagement.findAll().toList().size() - 1);
		assertEquals(timeSlot.getTimeOrders().getDescription(),
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getDescription());
		assertEquals(timeSlot.getTimeRoom().getName(),
				roomManagement.findAll().toList().get(1).getName());
		assertEquals(timeSlot.getTimeStaff().getEmail(),
				staffManagement.findAll().toList().get(0).getEmail());
	}

	@Test
	void createTimeSlot(){
		Orders order = ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1);
		Staff staff = staffManagement.findAll().toList().get(0);
		Room room = roomManagement.findAll().toList().get(0);
		int before = timeSlotManagement.findAll().toList().size();

		timeSlotManagement.createTimeSlot(LocalDateTime.now(), staff, order, room);
		assertEquals(before + 1, timeSlotManagement.findAll().toList().size());

		TimeSlot timeSlot = timeSlotManagement.findAll().toList()
				.get(timeSlotManagement.findAll().toList().size() - 1);
		assertEquals(timeSlot.getTimeOrders().getDescription(), order.getDescription());
		assertEquals(timeSlot.getTimeRoom().getName(), room.getName());
		assertEquals(timeSlot.getTimeStaff().getEmail(), staff.getEmail());
	}

	@Test
	void addTimeSlot(){
		Orders order = ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1);
		Staff staff = staffManagement.findAll().toList().get(0);
		Room room = roomManagement.findAll().toList().get(0);
		LocalDateTime time = LocalDateTime.now();
		TimeSlot timeSlot = new TimeSlot(time, staff, order, room);
		int before = timeSlotManagement.findAll().toList().size();

		timeSlotManagement.addTimeSlot(timeSlot);
		assertEquals(before + 1, timeSlotManagement.findAll().toList().size());

		assertEquals(timeSlot.getTimeOrders().getDescription(), order.getDescription());
		assertEquals(timeSlot.getTimeRoom().getName(), room.getName());
		assertEquals(timeSlot.getTimeStaff().getEmail(), staff.getEmail());
		assertEquals(timeSlot.getTime(), time);
	}

	@Test
	void deleteTimeSlot(){
		timeSlotManagement.addTimeSlot(new TimeSlot(LocalDateTime.now(),
				staffManagement.findAll().toList().get(0),
				ordersManagement.findAll().toList().get(0),
				roomManagement.findAll().toList().get(0)));
		TimeSlot timeSlot = timeSlotManagement.findAll().toList()
				.get(timeSlotManagement.findAll().toList().size() - 1);
		int before = timeSlotManagement.findAll().toList().size();

		timeSlotManagement.deleteTimeSlot(timeSlot);
		assertEquals(before - 1, timeSlotManagement.findAll().toList().size());
		assertFalse(timeSlotManagement.findAll().toList().contains(timeSlot));
	}

	@Test
	void getAllByStaff(){
		ArrayList<TimeSlot> timeSlots = new ArrayList<>();
		for(TimeSlot timeSlot : timeSlotManagement.findAll()){
			if(timeSlot.getTimeStaff().isAdmin()){
				timeSlots.add(timeSlot);
			}
		}
		Staff staff = staffManagement.findAll().toList().get(0);
		staff.setStaffId(staffManagement.findAll().toList().get(0).getStaffId());
		long id = staff.getStaffId();

		assertEquals(timeSlots.size(), timeSlotManagement
				.getAllByStaff(id).size());
	}

	@Test
	void setRun(){
		timeSlotManagement.setRun(false);
		assertFalse(timeSlotManagement.isRun());

		timeSlotManagement.setRun(true);
		assertTrue(timeSlotManagement.isRun());
	}

	@Test
	void skipDay(){
		LocalDateTime time = timeSlotManagement.getTime();

		timeSlotManagement.skipDay();
		assertEquals(time.toLocalDate().plusDays(1), timeSlotManagement.getTime().toLocalDate());
	}

	@Test
	void changeTime(){
		timeSlotManagement.setRun(false);
		ordersManagement.changeStatus(ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id(),
				Status.ACCEPTED);
		for(int i = 0; i < 6; i++) {
			timeSlotManagement.skipDay();
		}

		timeSlotManagement.changeTime();
		assertEquals(Status.DONE, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());

		for(int i = 0; i < 7; i++) {
			timeSlotManagement.skipDay();
		}

		timeSlotManagement.changeTime();
		assertEquals(Status.STORED, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());

		for(int i = 0; i < 90; i++) {
			timeSlotManagement.skipDay();
		}

		timeSlotManagement.changeTime();
		assertEquals(Status.DONATED, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());

		MonetaryAmount balance = accountancyManagement.getBalance();
		int day = businessTime.getTime().getDayOfMonth();
		if(day == 28){
			businessTime.forward(Duration.ofDays(4));
			day = businessTime.getTime().getDayOfMonth();
		}
		if(day <= 28){
			businessTime.forward(Duration.ofDays(28 - day));
		}else{
			businessTime.forward(Duration.ofDays(- (day - 28)));
		}

		timeSlotManagement.changeTime();
		assertNotEquals(balance, accountancyManagement.getBalance());

		balance = accountancyManagement.getBalance();
		businessTime.forward(Duration.ofHours(-1).minusSeconds(5));
		timeSlotManagement.setRun(true);

		timeSlotManagement.changeTime();
		assertEquals(balance, accountancyManagement.getBalance());
	}
}
