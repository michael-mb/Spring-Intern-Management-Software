package kickstart.orders;

import com.sun.security.auth.UserPrincipal;
import kickstart.staff.Skill;
import kickstart.time.TimeSlot;
import kickstart.time.TimeSlotManagement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import static kickstart.staff.Skill.*;
import static kickstart.staff.Skill.ELEKTRO;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrdersControllerIntegrationTests {
	@Autowired MockMvc mockMvc;
	@Autowired OrdersController ordersController;
	@Autowired OrdersManagement ordersManagement;
	@Autowired BusinessTime businessTime;
	@Autowired TimeSlotManagement timeSlotManagement;

	Authentication authentication = new Authentication() {
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			ArrayList<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add((GrantedAuthority) () -> "ADMIN");
			authorities.add((GrantedAuthority) () -> "USER");
			return authorities;
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getDetails() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return new UserPrincipal("Boss");
		}

		@Override
		public boolean isAuthenticated() {
			return true;
		}

		@Override
		public void setAuthenticated(boolean b) throws IllegalArgumentException {

		}

		@Override
		public String getName() {
			return "Boss";
		}
	};

	@BeforeAll
	void setup(){
		int before = ordersManagement.findAll().toList().size();

		OrderCreationFrom init1 = new OrderCreationFrom("Musterbeschreibung 1", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" ,  SubCategory.NAEHTE);
		ordersManagement.addOrder(init1).updateTimeSlot(1 , 2 , 1);

		OrderCreationFrom init2 = new OrderCreationFrom("Musterbeschreibung 2", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ABSAETZE);
		ordersManagement.addOrder(init2).updateTimeSlot(1 , 2 , 1);

		OrderCreationFrom init3 = new OrderCreationFrom("Musterbeschreibung 3", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SOHLEN);
		ordersManagement.addOrder(init3).updateTimeSlot(2 , 2 , 1);

		OrderCreationFrom init4 = new OrderCreationFrom("Musterbeschreibung 4", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KNOEPFE);
		ordersManagement.addOrder(init4).updateTimeSlot(2 , 2 , 2);

		OrderCreationFrom init5 = new OrderCreationFrom("Musterbeschreibung 5", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.FLICKEN);
		ordersManagement.addOrder(init5).updateTimeSlot(3 , 2 , 1);

		OrderCreationFrom init6 = new OrderCreationFrom("Musterbeschreibung 6", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.NAEHTE);
		ordersManagement.addOrder(init6).updateTimeSlot(3 , 2 , 2);

		OrderCreationFrom init7 = new OrderCreationFrom("Musterbeschreibung 7", KEY,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHILDER_GRAVIEREN);
		ordersManagement.addOrder(init7).updateTimeSlot(4 , 2 , 1);

		OrderCreationFrom init8 = new OrderCreationFrom("Musterbeschreibung 8", KEY ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHLUESSEL_KOPIEREN);
		ordersManagement.addOrder(init8).updateTimeSlot(4, 2 , 2);

		OrderCreationFrom init9 = new OrderCreationFrom("Musterbeschreibung 9", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LEDER);
		ordersManagement.addOrder(init9).updateTimeSlot(1 , 3 , 1);

		OrderCreationFrom init10 = new OrderCreationFrom("Musterbeschreibung 10", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.WAESCHE);
		ordersManagement.addOrder(init10).updateTimeSlot(1 , 3 , 2);

		OrderCreationFrom init11 = new OrderCreationFrom("Musterbeschreibung 11", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ANZUEGE);
		ordersManagement.addOrder(init11).updateTimeSlot(2 , 3 , 1);

		OrderCreationFrom init12 = new OrderCreationFrom("Musterbeschreibung 12", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.MESSER_SCHAERFEN);
		ordersManagement.addOrder(init12).updateTimeSlot(2 , 3 , 2);

		OrderCreationFrom init13 = new OrderCreationFrom("Musterbeschreibung 13", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHEREN);
		ordersManagement.addOrder(init13).updateTimeSlot(3 , 3 , 1);

		OrderCreationFrom init14 = new OrderCreationFrom("Musterbeschreibung 14", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KABEL_ERSETZEN);
		ordersManagement.addOrder(init14).updateTimeSlot(3 , 3 , 2);

		OrderCreationFrom init15 = new OrderCreationFrom("Musterbeschreibung 15", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LOETEN);
		ordersManagement.addOrder(init15).updateTimeSlot(4 , 3 , 1);

		OrderCreationFrom init16 = new OrderCreationFrom("Musterbeschreibung 16", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" ,  SubCategory.NAEHTE);
		ordersManagement.addOrder(init16).updateTimeSlot(4 , 3 , 2);

		OrderCreationFrom init17 = new OrderCreationFrom("Musterbeschreibung 17", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ABSAETZE);
		ordersManagement.addOrder(init17).updateTimeSlot(1 , 4 , 1);

		OrderCreationFrom init18 = new OrderCreationFrom("Musterbeschreibung 18", PATCH ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SOHLEN);
		ordersManagement.addOrder(init18).updateTimeSlot(1 , 4 , 2);

		OrderCreationFrom init19 = new OrderCreationFrom("Musterbeschreibung 19", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KNOEPFE);
		ordersManagement.addOrder(init19).updateTimeSlot(2 , 4 , 1);

		OrderCreationFrom init20 = new OrderCreationFrom("Musterbeschreibung 20", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.FLICKEN);
		ordersManagement.addOrder(init20).updateTimeSlot(2 , 4 , 2);

		OrderCreationFrom init21 = new OrderCreationFrom("Musterbeschreibung 21", SEW ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.NAEHTE);
		ordersManagement.addOrder(init21).updateTimeSlot(3 , 4 , 1);

		OrderCreationFrom init22 = new OrderCreationFrom("Musterbeschreibung 22", KEY,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHILDER_GRAVIEREN);
		ordersManagement.addOrder(init22).updateTimeSlot(3 , 4 , 2);

		OrderCreationFrom init23 = new OrderCreationFrom("Musterbeschreibung 23", KEY ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHLUESSEL_KOPIEREN);
		ordersManagement.addOrder(init23).updateTimeSlot(4, 4 , 1);

		OrderCreationFrom init24 = new OrderCreationFrom("Musterbeschreibung 24", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LEDER);
		ordersManagement.addOrder(init24).updateTimeSlot(4 , 4 , 2);

		OrderCreationFrom init25 = new OrderCreationFrom("Musterbeschreibung 25", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.WAESCHE);
		ordersManagement.addOrder(init25).updateTimeSlot(1 , 5 , 1);

		OrderCreationFrom init26 = new OrderCreationFrom("Musterbeschreibung 26", CLEAN ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.ANZUEGE);
		ordersManagement.addOrder(init26).updateTimeSlot(1 , 5 , 2);

		OrderCreationFrom init27 = new OrderCreationFrom("Musterbeschreibung 27", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.MESSER_SCHAERFEN);
		ordersManagement.addOrder(init27).updateTimeSlot(2 , 5 , 1);

		OrderCreationFrom init28 = new OrderCreationFrom("Musterbeschreibung 28", GRIND ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.SCHEREN);
		ordersManagement.addOrder(init28).updateTimeSlot(2 , 5 , 2);

		OrderCreationFrom init29 = new OrderCreationFrom("Musterbeschreibung 29", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.KABEL_ERSETZEN);
		ordersManagement.addOrder(init29).updateTimeSlot(3 , 5 , 1);

		OrderCreationFrom init30 = new OrderCreationFrom("Musterbeschreibung 30", ELEKTRO ,
				"Mustermann" , "mustermann@mailbox.de" , SubCategory.LOETEN);
		ordersManagement.addOrder(init30).updateTimeSlot(3 , 5 , 2);

		for(Orders orders : ordersManagement.findAll()) {
			if(before > 0) {
				before--;
			}else {
				timeSlotManagement.getFreeTimeSlot(orders);
			}
		}
	}

	@Test
	void showServicePage(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("services", ordersController.showServicesPage(model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
	}

	@Test
	void showOrderPage(){
		ExtendedModelMap model = new ExtendedModelMap();
		ordersManagement.addOrder(new OrderCreationFrom("abc", Skill.CLEAN, "abc",
				"abc", SubCategory.WAESCHE));

		assertEquals("orderpage", ordersController.showOrderPage(model,
				ordersManagement.findAll().toList().get(0).getOrder_id(), authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("order"));
		assertNotNull(model.get("created"));
	}

	@Test
	void showOrderTable(){
		ExtendedModelMap model = new ExtendedModelMap();
		for(Orders orders : ordersManagement.findAll()){
			timeSlotManagement.getFreeTimeSlot(orders);
		}
		ordersController.getNext();
		ordersController.getNext();

		assertEquals("ordertable", ordersController.showOrderTable(model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("time1Slot1"));
		assertNotNull(model.get("time2Slot1"));
		assertNotNull(model.get("time3Slot1"));
		assertNotNull(model.get("time4Slot1"));
		assertNotNull(model.get("time1Slot2"));
		assertNotNull(model.get("time2Slot2"));
		assertNotNull(model.get("time3Slot2"));
		assertNotNull(model.get("time4Slot2"));
		assertNotNull(model.get("time1Slot1"));
		assertNotNull(model.get("week"));
	}

	@Test
	void getPrevious(){
		assertEquals("redirect:/ordertable", ordersController.getPrevious());
	}

	@Test
	void getNext(){
		assertEquals("redirect:/ordertable", ordersController.getNext());
	}

	@Test
	void resetOffset(){
		assertEquals("redirect:/ordertable", ordersController.resetOffset());
	}

	@Test
	void deleteOrder(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);
		int before = ordersManagement.findAll().toList().size();

		assertEquals("redirect:/ordertable", ordersController.deleteOrder(
				ordersManagement.findAll().toList().get(
						ordersManagement.findAll().toList().size() - 1).getOrder_id()));
		assertEquals(before - 1, ordersManagement.findAll().toList().size());
	}

	@Test
	void updateOrder(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("updateOrder", ordersController.updateOrder(
				ordersManagement.findAll().toList().get(
						ordersManagement.findAll().toList().size() - 1).getOrder_id(),
				model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("order"));
	}

	@Test
	void updateOrderForm(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);
		ExtendedModelMap model = new ExtendedModelMap();
		ordersController.updateOrder(ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id(),
				model, authentication);
		OrderCreationFrom newOrderForm = new OrderCreationFrom("abcd", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		int before = ordersManagement.findAll().toList().size();

		assertEquals("redirect:/ordertable", ordersController.updateOrderForm(newOrderForm, model,
				authentication));
		assertEquals(before, ordersManagement.findAll().toList().size());
		assertEquals(newOrderForm.getDescription(), ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getDescription());
	}

	@Test
	void acceptStatusForm(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);

		assertEquals("redirect:/ordertable", ordersController.acceptStatusForm(
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id(), authentication));
		assertEquals(Status.ACCEPTED, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());
	}

	@Test
	void declineStatusForm(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);

		assertEquals("redirect:/ordertable", ordersController.declineStatusForm(
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id()));
		assertEquals(Status.DECLINED, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());
	}

	@Test
	void pickedUpStatusForm(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		ordersManagement.addOrder(orderForm);
		timeSlotManagement.getFreeTimeSlot(ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1));
		businessTime.forward(Duration.ofDays(12));

		assertEquals("redirect:/ordertable", ordersController.pickedupStatusForm(
				ordersManagement.findAll().toList()
						.get(ordersManagement.findAll().toList().size() - 1).getOrder_id()));
		assertEquals(Status.PICKED_UP, ordersManagement.findAll().toList()
				.get(ordersManagement.findAll().toList().size() - 1).getStatus());
	}

	@Test
	void showOrderCreationPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("ordercreation",
				ordersController.showOrderCreationPage(model, 1, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("subcategories"));
	}

	@Test
	void getCreationForm(){
		OrderCreationFrom orderForm = new OrderCreationFrom("abc", Skill.CLEAN,
				"Oliver Scholz", "oliver.scholz1@mailbox.tu-dresden.de",
				SubCategory.WAESCHE);
		assertEquals("redirect:/ordertable", ordersController.getCreationForm(orderForm));
	}

	@Test
	void toShowCategory(){
		assertTrue(ordersController.toShowCategory(0).contains(SubCategory.ABSAETZE));
		assertTrue(ordersController.toShowCategory(1).contains(SubCategory.FLICKEN));
		assertTrue(ordersController.toShowCategory(2).contains(SubCategory.SCHLUESSEL_KOPIEREN));
		assertTrue(ordersController.toShowCategory(3).contains(SubCategory.WAESCHE));
		assertTrue(ordersController.toShowCategory(4).contains(SubCategory.KABEL_ERSETZEN));
		assertTrue(ordersController.toShowCategory(5).contains(SubCategory.SCHEREN));
	}
}
