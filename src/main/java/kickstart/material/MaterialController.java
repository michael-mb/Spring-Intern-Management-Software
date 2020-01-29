package kickstart.material;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kickstart.orders.SubCategory;

/**
 * Internetzugriffskontrolleinheit für alle Angelegenheiten des Materials
 */
@Controller
public class MaterialController {
	private final MaterialManagement materialManagement;
	private Long materialId ;
	private SubCategory sub ;
	private int amount = 0 ;
	private boolean error = false ;
	private String mistake = null ;

	/**
	 * Erstellung der Internetzugriffskontrolleinheit einer Filiale
	 * @param materialManagement Materialverwaltungseinheit einer Filiale
	 */
	MaterialController(MaterialManagement materialManagement){
		Assert.notNull(materialManagement, "MaterialManagement must not be null!");

		this.materialManagement = materialManagement;
	}

	/**
	 * Zeigt die Materialübersicht an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return materials.html
	 */
	@GetMapping("/materials")
	String showMaterialsPage(Model model , Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
			model.addAttribute("Listmaterials", materialManagement.findAll());	
			model.addAttribute("error", error);
		}
		return "materials";
	}

	/**
	 * Zeigt die Erstellungsseite für ein neues Material an
	 * @param model Zu übermittelnde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return createMaterial.html
	 */
	@GetMapping("/createMaterial")
	String createMaterialPage(Model model , Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
			model.addAttribute("mistake", mistake);
			
		}
		
		return "createMaterial";
	}

	/**
	 * Erstellt ein neues Material oder zeigt Fehler bei der Materialerstellung an
	 * @param form Formular zur Materialerstellung
	 * @param model Zu übermittelde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return Weiterleitung zur Materialübersicht oder zur Erstellungsseite für ein Material
	 */
	@PostMapping("/createMaterial")
	String createMaterial(MaterialUpdateForm form, Model model, Authentication authentication){
		if(authentication != null) {
			model.addAttribute("name",authentication.getName());
			model.addAttribute("authorities",authentication.getAuthorities().toString());
		}
		
		for (Material m : materialManagement.findAll()) {
			if(m.getName().equals(form.getName())) {
				mistake = "Der Name dieses Materials ist schon besetzt !";		
				return "redirect:/createMaterial";
			}
		}
		
		materialManagement.addMaterial(new Material(form.getName(),form.getAmount(),form.getPrice(),form.getLimit()));
		mistake = null ;
		return "redirect:/materials";
	}

	/**
	 * Löscht ein Material mit entsprechender Identifikationsnummer
	 * @param id Identifikationsnummer
	 * @return Weiterleitung zur Materialübersicht
	 */
	@GetMapping("/deleteMaterial/{id}")
	String deleteMaterial(@PathVariable Long id){
		materialManagement.deleteMaterial(id);
		return "redirect:/materials";
	}

	/**
	 * Zeigt die Aktualisierungsseite für ein Material mit entsprechender Identifikationsnummer an
	 * @param id Identifikationsnummer
	 * @param model Zu übermittelde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return updateMaterial.html
	 */
	@GetMapping("/updateMaterial/{id}")
	String updateMaterial(@PathVariable Long id , Model model , Authentication authentication){
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		model.addAttribute("mistake", mistake);
		this.materialId = id ;
		model.addAttribute("material",materialManagement.findById(id).get());
		mistake = null ;
		return "updateMaterial";
	}

	/**
	 * Aktualisiert ein Material mit entsprechender Identifikationsnummer
	 * oder zeigt Fehler bei der Materialaktualisierung an
	 * @param form Formular zur Materialaktualisierung
	 * @param model Zu übermittelde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return Weiterleitung zur Materialübersicht
	 * oder zur Aktualisierungsseite eines Materials mit entsprechender Identifikationsnummer
	 */
	@PostMapping("/updateMaterial")
	String updateMaterialForm(MaterialUpdateForm form, Model model, Authentication authentication){
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		if(!(form.getName().equals(materialManagement.findById(materialId).get().getName()))) {
			for (Material m : materialManagement.findAll()) {
				if(m.getName().equals(form.getName())) {
					mistake = "Der Name dieses Materials ist schon besetzt !";		
					return "redirect:/updateMaterial/" + materialId ;
				}
			}
		}
		materialManagement.updateMaterial(form, materialId);
		materialManagement.updateInMaterialMap(materialId, form.getName());
		return "redirect:/materials";
	}
	
	/* ASSIGN MATERIAL */
	
	@GetMapping("/assignMaterial/{id}")
	String assignMaterialPage(@PathVariable Long id , Model model , Authentication authentication){
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		this.materialId = id ;
		model.addAttribute("entries", SubCategory.values());
		
		return "assignMaterial";
	}
	
	@GetMapping("/assignCategory/{category}")
	String assignCategory(@PathVariable SubCategory category , Model model , Authentication authentication){
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		this.sub = category ;
		
		return "amount";
	}
	
	@PostMapping("/assignAmount")
	String assignAmount(MaterialUpdateForm form, Model model , Authentication authentication){
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		this.amount = form.getAmount();
		
		materialManagement.updateMaterialMap(sub,materialId,amount);
		
		return "redirect:/materials";
	}

	/**
	 * Zeigt die detailliertere Übersicht über alle Materialien an
	 * @param model Zu übermittelde Daten
	 * @param authentication Aktueller Mitarbeiter
	 * @return materialInfos.html
	 */
	@GetMapping("/materialInfos")
	String showMaterialInfos( Model model , Authentication authentication){
		
		model.addAttribute("name", authentication.getName());
		model.addAttribute("authorities", authentication.getAuthorities().toString());
		
		model.addAttribute("entries", SubCategory.values());
		
		model.addAttribute("materialMap",materialManagement.getMaterialMap());
		
		return "materialInfos";
	}

	/**
	 * Setzt Fehler neu
	 * @param error neuer Fehler
	 */
	public void setError(boolean error) {
		this.error = error;
	}
}
