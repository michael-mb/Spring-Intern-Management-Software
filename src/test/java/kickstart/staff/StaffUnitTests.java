package kickstart.staff;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class StaffUnitTests {
	@Autowired UserAccountManager userAccountManager;
	private Staff staff;

	@BeforeAll
	void setup(){
		UserAccount account = userAccountManager.create("abc", Password.UnencryptedPassword.of("abc"),
				Role.of("USER"));
		HashSet<Skill> skills = new HashSet<>();
		skills.add(Skill.CLEAN);
		skills.add(Skill.GRIND);
		staff = new Staff(account, 1000, skills, "abc", "def", "abc@def");
	}

	@Test
	void setSalary(){
		staff.setSalary(2000);
		assertEquals(2000, staff.getSalary().getNumber().intValueExact());
	}

	@Test
	void setSkills(){
		HashSet<Skill> newSkills = new HashSet<>();
		newSkills.add(Skill.KEY);

		staff.setSkills(newSkills);
		assertTrue(staff.getSkills().contains(Skill.KEY));
		assertEquals(1, staff.getSkills().size());
	}

	@Test
	void setName(){
		staff.setName("abcd");
		assertEquals("abcd", staff.getName());
	}

	@Test
	void setFirstName(){
		staff.setFirstName("defg");
		assertEquals("defg", staff.getFirstName());
	}

	@Test
	void setEmail(){
		staff.setEmail("abcd@defg");
		assertEquals("abcd@defg", staff.getEmail());
	}

	@Test
	void string(){
		assertEquals("id: 0 name: abc vorname: def skills: [CLEAN, GRIND] lohn: EUR 1000 email: abc@def roles: USER , ",
				staff.toString());
	}
}
