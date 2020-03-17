package kickstart.material;

import kickstart.orders.SubCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
class MaterialManagementUnitTests {
	@Autowired MaterialManagement materialManagement;

	Material material = new Material("Testmaterial", 10, 10, 20);

	@Test
	void addMaterial(){
		int before = materialManagement.findAll().toList().size();

		materialManagement.addMaterial(material);
		assertEquals(before + 1, materialManagement.findAll().toList().size());
		Material newMaterial = materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1);
		assertEquals(material.getName(), newMaterial.getName());
		assertEquals(material.getAmount(), newMaterial.getAmount());
		assertEquals(material.getAllowedLimit(), newMaterial.getAllowedLimit());
		assertEquals(material.getPrice(), newMaterial.getPrice());
	}

	@Test
	void reduceAmount(){
		materialManagement.addMaterial(material);
		int before = materialManagement.findAll().toList().size();

		assertTrue(materialManagement.reduceAmount(material.getName(), 3));
		assertEquals(before, materialManagement.findAll().toList().size());
		assertEquals(7, materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1).getAmount());

		assertFalse(materialManagement.reduceAmount(material.getName(), -1));
		assertFalse(materialManagement.reduceAmount(material.getName(), 8));
	}

	@Test
	void newOrder(){
		for(Material material1 : materialManagement.findAll()) {
			materialManagement.deleteMaterial(material1.getId());
		}
		materialManagement.addMaterial(material);
		int before = materialManagement.findAll().toList().size();

		assertEquals(100, materialManagement.renewStock());
		assertEquals(before, materialManagement.findAll().toList().size());
		assertEquals(20, materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1).getAmount());
	}

	@Test
	void deleteMaterial(){
		int before = materialManagement.findAll().toList().size();

		materialManagement.deleteMaterial(materialManagement.findAll().toList().get(0).getId());
		assertEquals(before - 1, materialManagement.findAll().toList().size());
	}

	@Test
	void updateMaterial(){
		MaterialUpdateForm form = new MaterialUpdateForm("Testmaterial2", 100, 2, 200);
		materialManagement.addMaterial(material);

		int before = materialManagement.findAll().toList().size();

		materialManagement.updateMaterial(form,
				materialManagement.findAll().toList()
						.get(materialManagement.findAll().toList().size() - 1).getId());
		assertEquals(before, materialManagement.findAll().toList().size());
		Material newMaterial = materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1);
		assertEquals(form.getName(), newMaterial.getName());
		assertEquals(form.getAmount(), newMaterial.getAmount());
		assertEquals(form.getLimit(), newMaterial.getAllowedLimit());
		assertEquals(form.getPrice(), newMaterial.getPrice());
	}

	@Test
	void setMaterialMap(){
		Map<SubCategory, Map<Material, Integer>> oldMap = materialManagement.getMaterialMap();

		HashMap<SubCategory, Map<Material, Integer>> newMap = new HashMap<>();
		newMap.put(SubCategory.WAESCHE, null);
		materialManagement.setMaterialMap(newMap);

		assertNull(materialManagement.getMaterialMap().get(SubCategory.WAESCHE));

		materialManagement.setMaterialMap(oldMap);
	}
}
