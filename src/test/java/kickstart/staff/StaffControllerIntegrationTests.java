package kickstart.staff;

import com.sun.security.auth.UserPrincipal;
import kickstart.orders.OrderCreationFrom;
import kickstart.orders.Orders;
import kickstart.orders.OrdersManagement;
import kickstart.orders.SubCategory;
import kickstart.time.TimeSlotManagement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static kickstart.staff.Skill.*;
import static kickstart.staff.Skill.ELEKTRO;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StaffControllerIntegrationTests {
	@Autowired WebApplicationContext webApplicationContext;
	MockMvc mockMvc;
	@Autowired StaffController staffController;
	@Autowired StaffManagement staffManagement;
	@Autowired OrdersManagement ordersManagement;
	@Autowired TimeSlotManagement timeSlotManagement;

	RegistrationForm registrationForm = new RegistrationForm("100", "naehen",
			null, null, null, null, null,
			"Scholz", "Oliver", "abc", "abc",
			"oliver.scholz1@mailbox.tu-dresden.de");

	RegistrationForm registrationForm2 = new RegistrationForm("100", "naehen",
			null, null, null, null, null,
			"Scholz", "Oliver", "abc", "abc",
			"oliver.scholz@mailbox.tu-dresden.de");

	@BeforeAll()
	void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
			timeSlotManagement.getFreeTimeSlot(orders);
		}
	}

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

	@Test
	void registerStaff() throws Exception{
		int before = staffManagement.findAll().toList().size();

		mockMvc.perform(post("/register")
				.param("name", registrationForm.getName())
				.param("firstName", registrationForm.getVorname())
				.param("naehen", "naehen")
				.param("salary", "100")
				.param("email", registrationForm.getEmail())
				.param("passwort", registrationForm.getPasswort())
				.param("passwortValid", registrationForm.getPasswortValid())
				.principal(authentication)
		).andExpect(status().isMovedTemporarily());
		assertEquals(before + 1, staffManagement.findAll().toList().size());

		mockMvc.perform(post("/register")
				.param("name", registrationForm.getName())
				.param("firstName", registrationForm.getVorname())
				.param("naehen", "naehen")
				.param("salary", "100")
				.param("email", registrationForm.getEmail())
				.param("passwort", registrationForm.getPasswort())
				.param("passwortValid", registrationForm.getPasswortValid())
				.principal(authentication)
		).andExpect(model().attribute("formVerification", true))
				.andExpect(status().isOk());

		RegistrationForm form2 = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Mitarbeiter", "Neuer", "abc", "ab",
				"neuer.mitarbeiter@firma.de");

		mockMvc.perform(post("/register")
				.param("name", form2.getName())
				.param("firstName", form2.getVorname())
				.param("naehen", "naehen")
				.param("salary", "100")
				.param("email", form2.getEmail())
				.param("passwort", form2.getPasswort())
				.param("passwortValid", form2.getPasswortValid())
				.principal(authentication)
		).andExpect(model().attribute("passVerification", true))
				.andExpect(status().isOk());
	}

	@Test
	void showIndexPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		staffController.showIndex(model, authentication);
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("time"));
	}

	@Test
	void showRegisterPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		staffController.showregisterPage(model, authentication);
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("users"));
	}

	@Test
	void showLoginPage() throws Exception{
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Sign in")));
	}

	@Test
	void showStaffPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		staffController.showPersonalPage(model, authentication);
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("users"));
	}

	@Test
	void deleteStaff() throws Exception{
		staffManagement.addStaff(registrationForm);
		String url = "/delete/" + staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId();

		mockMvc.perform(get(url))
				.andExpect(status().isMovedTemporarily())
				.andExpect(redirectedUrl("/personallist"));
	}

	@Test
	void updateStaff(){
		ExtendedModelMap model = new ExtendedModelMap();

		staffController.updateStaff(staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId(),
				model, authentication);
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("personal"));
	}

	@Test
	void updateStaffForm() throws Exception{
		staffManagement.addStaff(registrationForm);
		String url = "/update/" + staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId();
		mockMvc.perform(get(url));

		mockMvc.perform(post("/updatePersonal")
				.param("name", registrationForm2.getName())
				.param("firstName", registrationForm2.getVorname())
				.param("naehen", "naehen")
				.param("salary", "100")
				.param("email", registrationForm2.getEmail())
				.param("passwort", registrationForm2.getPasswort())
				.param("passwortValid", registrationForm2.getPasswortValid())
				.principal(authentication)
		);
		assertEquals(registrationForm2.getEmail(), staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getEmail());
	}

	@Test
	void formVerification(){
		ExtendedModelMap model = new ExtendedModelMap();
		RegistrationForm form = new RegistrationForm("1000", "naehen", null,
				null, null, null, null, "Scholz", "Oliver",
				"abc", "abc", "o@o");
		staffController.getRegisterForm(form, model, authentication);

		staffController.getRegisterForm(form, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form2 = new RegistrationForm("1000", "naehen", null,
				null, null, null, null, "Schulz", "Oliver",
				"abc", "abc", "o@o");

		staffController.getRegisterForm(form2, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form3 = new RegistrationForm("0", "naehen", null,
				null, null, null, null, "Schulz", "Oliver",
				"abc", "abc", "o@u");

		staffController.getRegisterForm(form3, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form4 = new RegistrationForm("-1", "naehen", null,
				null, null, null, null, "Schulz", "Oliver",
				"abc", "abc", "o@u");

		staffController.getRegisterForm(form4, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form5 = new RegistrationForm("2000000001", "naehen", null,
				null, null, null, null, "Schulz", "Oliver",
				"abc", "abc", "o@u");

		staffController.getRegisterForm(form5, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form6 = new RegistrationForm("1000", "naehen", null,
				null, null, null, null, "", "Oliver",
				"abc", "abc", "o@u");

		staffController.getRegisterForm(form6, model, authentication);
		assertTrue((boolean) model.get("formVerification"));

		RegistrationForm form7 = new RegistrationForm("1000", "naehen", null,
				null, null, null, null, "Schulz", "",
				"abc", "abc", "o@u");

		staffController.getRegisterForm(form7, model, authentication);
		assertTrue((boolean) model.get("formVerification"));
	}

}
