package kickstart.material;

import kickstart.orders.SubCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MaterialControllerIntegrationTests {
	@Autowired MockMvc mockMvc;
	@Autowired MaterialController materialController;
	@Autowired MaterialManagement materialManagement;

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
	void showMaterialPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("materials", materialController.showMaterialsPage(model, null));

		assertEquals("materials", materialController.showMaterialsPage(model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("Listmaterials"));
		assertNotNull(model.get("error"));
	}

	@Test
	void createMaterialPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("createMaterial", materialController.createMaterialPage(model, null));

		assertEquals("createMaterial", materialController.createMaterialPage(model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNull(model.get("mistake"));

		materialManagement.addMaterial(new Material("Testmaterial2", 10,
				1, 20));
		materialController.createMaterial(new MaterialUpdateForm("Testmaterial2",
				10, 1, 20), model, authentication);
		materialController.createMaterialPage(model, authentication);
		assertNotNull(model.get("mistake"));
	}

	@Test
	void createMaterial(){
		ExtendedModelMap model = new ExtendedModelMap();
		MaterialUpdateForm form = new MaterialUpdateForm("Testmaterial", 10, 1, 20);
		int before = materialManagement.findAll().toList().size();

		assertEquals("redirect:/materials", materialController.createMaterial(form, model, null));

		assertEquals("redirect:/createMaterial", materialController.createMaterial(form, model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertEquals(before + 1, materialManagement.findAll().toList().size());
	}

	@Test
	void deleteMaterial() throws Exception{
		Material material = new Material("Testmaterial", 10, 1, 20);
		materialManagement.addMaterial(material);
		String url = "/deleteMaterial/" + materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1).getId();

		mockMvc.perform(get(url))
				.andExpect(status().isMovedTemporarily());
	}

	@Test
	void updateMaterial(){
		Material material = new Material("Testmaterial", 10, 1, 20);
		materialManagement.addMaterial(material);
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("updateMaterial",
				materialController.updateMaterial(materialManagement.findAll().toList()
						.get(materialManagement.findAll().toList().size() - 1).getId(),
						model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("material"));
	}

	@Test
	void updateMaterialForm(){
		Material material = new Material("Testmaterial", 10, 1, 20);
		materialManagement.addMaterial(material);
		ExtendedModelMap model = new ExtendedModelMap();
		materialController.updateMaterial(materialManagement.findAll().toList()
				.get(materialManagement.findAll().toList().size() - 1).getId(), model, authentication);
		MaterialUpdateForm materialUpdateForm = new MaterialUpdateForm("Testmaterial", 100, 2, 200);

		materialController.updateMaterialForm(materialUpdateForm, model, authentication);
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
	}

	@Test
	void assignMaterialPage(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("assignMaterial",
				materialController.assignMaterialPage(materialManagement.findAll().toList().get(0).getId(),
						model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("entries"));
	}

	@Test
	void assignCategory(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("amount",
				materialController.assignCategory(SubCategory.WAESCHE, model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
	}

	@Test
	void assignAmount(){
		ExtendedModelMap model = new ExtendedModelMap();
		MaterialUpdateForm form = new MaterialUpdateForm("Testmaterial", 10, 1, 20);
		materialController.assignMaterialPage(materialManagement.findAll().toList().get(0).getId(),
				model, authentication);
		materialController.assignCategory(SubCategory.WAESCHE, model, authentication);

		assertEquals("redirect:/materials", materialController.assignAmount(form, model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
	}

	@Test
	void showMaterialInfo(){
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("materialInfos", materialController.showMaterialInfos(model, authentication));
		assertNotNull(model.get("name"));
		assertNotNull(model.get("authorities"));
		assertNotNull(model.get("entries"));
		assertNotNull(model.get("materialMap"));
	}
}
