package kickstart.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class TimeSlotControllerIntegrationTests {
	@Autowired TimeSlotController timeSlotController;
	@Autowired TimeSlotManagement timeSlotManagement;

	@Test
	void stop(){
		ExtendedModelMap model = new ExtendedModelMap();
		timeSlotController.stop(model);

		assertFalse(timeSlotManagement.isRun());
		assertEquals(false, model.getAttribute("time"));
	}

	@Test
	void start(){
		ExtendedModelMap model = new ExtendedModelMap();
		timeSlotController.start(model);

		assertTrue(timeSlotManagement.isRun());
		assertEquals(true, model.getAttribute("time"));
	}

	@Test
	void skipDay(){
		timeSlotManagement.setRun(false);
		LocalDate time = timeSlotManagement.getTime().toLocalDate();
		timeSlotController.skipDay();

		assertEquals(time.plusDays(1), timeSlotManagement.getTime().toLocalDate());
	}
}
