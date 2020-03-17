package kickstart.material;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MaterialUnitTests {
	private Material material = new Material("Material", 10, 1, 20);

	@Test
	void setName(){
		material.setName("Material2");
		assertEquals("Material2", material.getName());
	}

	@Test
	void setAllowedLimit(){
		material.setAllowedLimit(100);
		assertEquals(100, material.getAllowedLimit());
	}

	@Test
	void newOder(){
		assertEquals(90, material.newOder());
		material.setAllowedLimit(10);
		assertEquals(0, material.newOder());
	}

	@Test
	void string(){
		assertEquals("ID: " + material.getId() + " Name: Material Amount: 10",
				material.toString());
	}
}
