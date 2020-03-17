package kickstart.orders;

import kickstart.staff.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrdersUnitTests {
	private Orders order = new Orders(LocalDate.now(), "abcd", Skill.CLEAN,
			SubCategory.WAESCHE, "abc", "ab");

	@Test
	void getDate(){
		assertEquals(LocalDate.now(), order.getDate());
	}

	@Test
	void setOrderId(){
		order.setOrder_id((long) 10);
		assertEquals((long) 10, order.getOrder_id());
	}

	@Test
	void getCustomerName(){
		assertEquals("abc", order.getCustomerName());
	}

	@Test
	void getCustomerContact(){
		assertEquals("ab", order.getCustomerContact());
	}

	@Test
	void setSubCategory(){
		order.setSubcategory(SubCategory.SOHLEN);
		assertEquals(SubCategory.SOHLEN, order.getSubcategory());
	}

	@Test
	void getSkillColor(){
		assertEquals("#0000FF", order.getSkillColor());
	}

	@Test
	void setSkillColorNull(){
		Orders newOrder = order;
		newOrder.setSkillColor(null);
		assertEquals(order.getSkillColor(), newOrder.getSkillColor());
	}

	@Test
	void getStatusColor(){
		assertEquals("#FF0000", order.getStatusColor());
	}

	@Test
	void setStatusColorNull(){
		Orders newOrder = order;
		newOrder.setStatusColor(null);
		assertEquals(order.getStatusColor(), newOrder.getStatusColor());
	}

	@Test
	void getIsAcceptedBy(){
		assertNull(order.getIsAcceptedBy());
	}

	@Test
	void string(){
		assertEquals("customerName : abc", order.toString());
	}
}
