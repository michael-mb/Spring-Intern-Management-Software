package kickstart.room;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class RoomManagementUnitTests {
	@Autowired RoomManagement roomManagement;

	Room room = new Room("HSZ/002");

	@Test
	void addRoom(){
		int before = roomManagement.findAll().toList().size();
		roomManagement.addRoom(room);
		assertEquals(before + 1, roomManagement.findAll().toList().size());

		Room addedRoom = roomManagement.findAll().toList()
				.get(roomManagement.findAll().toList().size() - 1);
		assertEquals("HSZ/002", addedRoom.getName());
	}
}
