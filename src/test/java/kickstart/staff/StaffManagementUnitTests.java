package kickstart.staff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class StaffManagementUnitTests {
	@Autowired StaffManagement staffManagement;

	@Test
	void admin(){
		assertTrue(staffManagement.findAll().toList().get(0).isAdmin());
		for(int i = 1; i < staffManagement.findAll().toList().size(); i++){
			assertFalse(staffManagement.findAll().toList().get(i).isAdmin());
		}
	}

	@Test
	void addStaff(){
		RegistrationForm registrationForm = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Scholz", "Oliver", "abc", "abc",
				"oliver.scholz1@mailbox.tu-dresden.de");
		int before = staffManagement.findAll().toList().size();

		staffManagement.addStaff(registrationForm);
		assertEquals(before + 1, staffManagement.findAll().toList().size());
	}

	@Test
	void updateStaff(){
		RegistrationForm registrationForm = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Scholz1", "Oliver", "abc", "abc",
				"oliver.scholz2@mailbox.tu-dresden.de");
		staffManagement.addStaff(registrationForm);
		int before = staffManagement.findAll().toList().size();
		RegistrationForm newRegistrationForm = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Scholz1", "Oliver", "abc", "abc",
				"oliver.scholz@mailbox.tu-dresden.de");

		staffManagement.update(newRegistrationForm, staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId());
		assertEquals(before, staffManagement.findAll().toList().size());
		assertEquals(newRegistrationForm.getEmail(), staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getEmail());
	}

	@Test
	void deleteStaff(){
		RegistrationForm registrationForm = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Schulz", "Oliver", "abc", "abc",
				"oliver.scholz@mailbox.tu-dresden.de");
		staffManagement.addStaff(registrationForm);
		int before = staffManagement.findAll().toList().size();
		Staff staff = staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1);
		staff.setStaffId(staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId());
		long id = staff.getStaffId();

		staffManagement.deleteStaff(id);
		assertEquals(before - 1, staffManagement.findAll().toList().size());
		assertFalse(staffManagement.findAll().toList().contains(staff));
	}

	@Test
	void getStaffById(){
		Staff staff = staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1);
		long id = staff.getStaffId();

		assertTrue(staffManagement.getStaffById(id).isPresent());
		assertEquals(staffManagement.getStaffById(id).get().getStaffId(), staff.getStaffId());

		assertFalse(staffManagement.getStaffById(id + 1).isPresent());
	}

	@Test
	void getStaffByName(){
		RegistrationForm registrationForm = new RegistrationForm("100", "naehen",
				null, null, null, null, null,
				"Schulz", "Oliver", "abc", "abc",
				"oliver.scholz1@mailbox.tu-dresden.de");
		staffManagement.addStaff(registrationForm);
		Staff staff = staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1);
		staff.setStaffId(staffManagement.findAll().toList()
				.get(staffManagement.findAll().toList().size() - 1).getStaffId());
		long id = staff.getStaffId();
		String name = staff.getName();

		assertEquals(StaffManagement.getStaffByName(name).getName(), staff.getName());

		staffManagement.deleteStaff(id);
		assertNull(StaffManagement.getStaffByName(name));
	}
}
