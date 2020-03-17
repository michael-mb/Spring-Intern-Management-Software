package kickstart.Accountancy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class AccountancyControllerIntegrationTests {
	@Autowired AccountancyController accountancyController;

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
			return null;
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
	void showAllBills(){
		ExtendedModelMap model = new ExtendedModelMap();
		assertEquals("financialoverview",
				accountancyController.showAllBills(model, authentication));

		assertNotNull(model.getAttribute("name"));
		assertNotNull(model.getAttribute("authorities"));
		assertNotNull(model.getAttribute("salary"));
		assertNotNull(model.getAttribute("month"));
		assertNotNull(model.getAttribute("entries"));
		assertNotNull(model.getAttribute("balance"));
		assertNotNull(model.getAttribute("earnings"));
		assertNotNull(model.getAttribute("balances"));
		assertNotNull(model.getAttribute("indexes"));
	}

	@Test
	void getPrevious(){
		assertEquals("redirect:/financialoverview",
				accountancyController.getPrevious());
	}

	@Test
	void getNext(){
		assertEquals("redirect:/financialoverview",
				accountancyController.getNext());
	}

	@Test
	void resetOffset(){
		assertEquals("redirect:/financialoverview",
				accountancyController.resetOffset());
	}
}
