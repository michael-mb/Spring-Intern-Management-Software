package kickstart.orders;

import kickstart.staff.Skill;
import kickstart.time.TimeSlot;
import kickstart.time.TimeSlotManagement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class OrdersManagementUnitTests {
	@Autowired OrdersManagement ordersManagement;
	@Autowired TimeSlotManagement timeSlotManagement;

	@Test
	void updateOrder(){
		OrderCreationFrom orderCreationForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"abcd", "ab@cd", SubCategory.WAESCHE);
		ordersManagement.addOrder(new OrderCreationFrom("abc", Skill.CLEAN, "abc",
				"ab@cd", SubCategory.WAESCHE));
		timeSlotManagement.getFreeTimeSlot(ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1));
		for(TimeSlot timeSlot : timeSlotManagement.findAll()){
			if(timeSlot.getTimeOrders().equals(ordersManagement.findAll().toList()
					.get(ordersManagement.findAll().toList().size() - 1))){
				timeSlotManagement.deleteTimeSlot(timeSlot);
			}
		}
		int before = ordersManagement.findAll().toList().size();

		ordersManagement.updateOrder(orderCreationForm,
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id());
		assertEquals(before, ordersManagement.findAll().toList().size());
		assertEquals(orderCreationForm.getCustumerName(),
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getCustomerName());
	}
}
