package kickstart.material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import kickstart.Accountancy.AccountancyManagement;
import kickstart.orders.SubCategory;

/**
 * Repräsentiert die Verwaltung des Materials einer Filiale
 */
@Service
@Transactional
public class MaterialManagement {
	private final MaterialRepository materialRepository ;
	private final AccountancyManagement accountancyManagement;
	private Map<SubCategory,Map<Material,Integer>>materialMap ;

	/**
	 * Erstellung der Materialverwaltungseinheit
	 * @param materialRepository Datenbank, die alle Materialien beinhaltet
	 * @param accountancyManagement Finanzverwaltungseinheit
	 */
	public MaterialManagement(MaterialRepository materialRepository , AccountancyManagement accountancyManagement) {
		
		Assert.notNull(materialRepository, " MaterialRepository must not be null!");
		Assert.notNull(accountancyManagement, " AccountancyManagement must not be null!");
		this.materialRepository = materialRepository ;
		this.accountancyManagement = accountancyManagement ;
		
	}

	/**
	 * Neues Material wird in der Datenbank hinzugefügt
	 * @param material neues Material
	 */
	public void addMaterial(Material material) {
		materialRepository.save(material);
		
		int billamount = - (material.getPrice() * material.getAmount()); 
		String description  =  material.getAmount() + " " + material.getName() +  " wurde(n) nachbestellt " ;
		
		accountancyManagement.addBill(Money.of( billamount , Monetary.getCurrency("EUR")),
				description);
	}

	/**
	 * Reduziert die Stückzahl eines Materials
	 * @param name Name des Materials
	 * @param amount zu reduzierende Stückzahl
	 * @return Gibt true zurück, wenn genügend Material verfügbar ist, sonst false
	 */
	public boolean reduceAmount(String name , int amount) {
		
		if (amount <= 0) {
			return false;
		}
		
		for(Material m : materialRepository.findAll()) {
			if(m.getName().equals(name)) {
				if(m.getAmount() >= amount) {
					m.setAmount( m.getAmount() - amount);
					return true ;
				}else {
					break;
				}
			}
		}
		
		return false ;
	}

	/**
	 * Frischt den Bestand jedes Materials auf und erstellt Rechnung
	 * @return Betrag der Kosten
	 */
	public int renewStock() {
		int expense = 0 ;
		
		for(Material m : materialRepository.findAll()) {
			if(m.getAmount() < m.getAllowedLimit()) {
				expense += m.newOder();
				m.setAmount(m.getAllowedLimit());
				this.updateMaterial(m);
			}
		}
		
		int billamount = - expense; 
		String description  = "Dieser Monat wurde " + expense + " ausgegeben , für die Nachbestellung von Materialien " ;
		accountancyManagement.addBill(Money.of( billamount , Monetary.getCurrency("EUR")),
				description);
		
		return expense ;
	}

	/**
	 * Löscht das Material mit spezifischer Identifikationsnummer aus der Datenbank heraus
	 * @param id Identifikationsnummer
	 */
	public void deleteMaterial(Long id) {
		
		for(SubCategory s : materialMap.keySet()) {
			deleteInMaterialMap(id);
		}
		materialRepository.deleteById(id);
	}

	/**
	 * Löscht das Material aus der Materialnutzung für die Aufträge heraus
	 * @param id Identifikationsnummer
	 */
	public void deleteInMaterialMap(Long id) {
		
		Material toDelete = null ; 
		SubCategory deleteInto = null ; 
		for(SubCategory s : materialMap.keySet()) {
			for(Material m : materialMap.get(s).keySet()) {
				if(m.getId() == id) {
					deleteInto = s ;
					toDelete = m ; 				
				}
			}
		}
		
		if(toDelete != null && deleteInto!= null) {
			materialMap.get(deleteInto).remove(toDelete);
		}
	}

	/**
	 * Aktualisiert den Namen eines Materials in der Materialnutzung für die Aufträge
	 * @param id Identifikationsnummer
	 * @param newName neuer Materialname
	 */
	public void updateInMaterialMap(Long id , String newName) {
		
		Material toUpdate = null ; 
		SubCategory updateInto = null ; 
		for(SubCategory s : materialMap.keySet()) {
			for(Material m : materialMap.get(s).keySet()) {
				if(m.getId() == id) {
					updateInto = s ;
					toUpdate = m ; 				
				}
			}
		}
		
		if(toUpdate != null && updateInto!= null) {
			toUpdate.setName(newName);
		}
	}

	/**
	 * Iterierbares Objekt, um über alle Materialien in der Datenbank zu iterieren
	 * @return Gibt alle eingespeicherten Materialien zurück
	 */
	public Streamable<Material> findAll() {
		return materialRepository.findAll();
	}

	/**
	 *
	 * @param id Identifiationsnummer
	 * @return Gibt, wenn vorhanden, das Material mit entsprechender Identifikationsnummer zurück
	 */
	public Optional<Material> findById(Long id) {
		return materialRepository.findById(id) ;
	}

	/**
	 *
	 * @param name Name des Materials
	 * @return Gibt, wenn vorhanden, das Material mit entsprechender Identifikationsnummer zurück, sonst null
	 */
	public Material findByName(String name) {
		for(Material m : findAll()) {
			if(m.getName().equals(name)) {
				return m ;
			}
		}
		return null;
	}

	/**
	 * Aktualisiert das Material mit entsprechender Identifikationsnummer mit dem neuen Formular
	 * @param form Aktualisierungsformular
	 * @param id Identifikationsnummer
	 */
	public void updateMaterial(MaterialUpdateForm form , Long id) {
		
		if(form.getAmount() > form.getLimit()) {
			return;
		}
		
		Material tempMaterial = findById(id).get();
		
		int difference = form.getAmount() -  tempMaterial.getAmount() ;
		int billamount = -((form.getAmount() - tempMaterial.getAmount()) * tempMaterial.getPrice()); 
		String description  = difference + "  " + tempMaterial.getName() +  " Nachbestellt " ;
		
		accountancyManagement.addBill(Money.of( billamount , Monetary.getCurrency("EUR")),
				description);
		
		Material material = new Material(form.getName() , form.getAmount() , form.getPrice() ,form.getLimit());
		material.setId(id);	
		this.materialRepository.save(material);
		
	}

	/**
	 * Speichert das aktualisierte Material in der Datenbank ab
	 * @param material aktualisiertes Material
	 */
	public void updateMaterial(Material material) {
		materialRepository.save(material);
	}

	/**
	 *
	 * @return Gibt die Zuordnung der Stückzahl von Materialien an eine Auftragsunterkategorie zurück
	 */
	public Map<SubCategory, Map<Material, Integer>> getMaterialMap() {
		return materialMap;
	}

	/**
	 * Setzt die Zuordnung der Stückzahl von Materialien an eine Auftragsunterkategorie neu
	 * @param materialMap neue Zuordnung der Stückzahl von Materialien an eine Auftragsunterkategorie
	 */
	public void setMaterialMap(Map<SubCategory, Map<Material, Integer>> materialMap) {
		this.materialMap = materialMap;
	}

	/**
	 * Initialisiert die Zuordnung der Stückzahl von Materialien an eine Auftragsunterkategorie
	 */
	public void initializeMaterialMap(){
		
		Map<SubCategory,Map<Material,Integer>>materialMap = new HashMap<>();
		
		/* PATCH */
		materialMap.put(SubCategory.ABSAETZE , new HashMap<>());
		materialMap.get(SubCategory.ABSAETZE).put(findByName("Heel"), 2);
		materialMap.get(SubCategory.ABSAETZE).put(findByName("Sole"), 4);
		materialMap.get(SubCategory.ABSAETZE).put(findByName("Wire"), 3);
		

		materialMap.put(SubCategory.SOHLEN , new HashMap<>());
		materialMap.get(SubCategory.SOHLEN).put(findByName("Heel"), 5);
		materialMap.get(SubCategory.SOHLEN).put(findByName("Sole"), 2);
		materialMap.get(SubCategory.SOHLEN).put(findByName("Wire"), 4);
		
		materialMap.put(SubCategory.NAEHTE , new HashMap<>());
		materialMap.get(SubCategory.NAEHTE).put(findByName("Heel"), 1);
		materialMap.get(SubCategory.NAEHTE).put(findByName("Sole"), 1);
		materialMap.get(SubCategory.NAEHTE).put(findByName("Wire"), 1);
		
		
		/* SEW */
		
		materialMap.put(SubCategory.FLICKEN , new HashMap<>());
		materialMap.get(SubCategory.FLICKEN).put(findByName("Knob"), 4);
		
		materialMap.put(SubCategory.KNOEPFE , new HashMap<>());
		materialMap.get(SubCategory.KNOEPFE).put(findByName("Knob"), 5);

		
		/* KEY */
		
		materialMap.put(SubCategory.SCHLUESSEL_KOPIEREN, new HashMap<>());
		materialMap.get(SubCategory.SCHLUESSEL_KOPIEREN).put(findByName("Brute"), 2);
		
		materialMap.put(SubCategory.SCHILDER_GRAVIEREN , new HashMap<>());
		materialMap.get(SubCategory.SCHILDER_GRAVIEREN).put(findByName("Knob"), 1);
		
		/* CLEAN */
		
		materialMap.put(SubCategory.WAESCHE , new HashMap<>());
		materialMap.get(SubCategory.WAESCHE).put(findByName("Detergent"), 2);
		materialMap.get(SubCategory.WAESCHE).put(findByName("Shoe cream"), 4);

		
		materialMap.put(SubCategory.LEDER , new HashMap<>());
		materialMap.get(SubCategory.LEDER).put(findByName("Detergent"), 4);
		materialMap.get(SubCategory.LEDER).put(findByName("Shoe cream"), 6);
		
		materialMap.put(SubCategory.ANZUEGE , new HashMap<>());
		materialMap.get(SubCategory.ANZUEGE).put(findByName("Detergent"), 4);
		materialMap.get(SubCategory.ANZUEGE).put(findByName("Shoe cream"), 6);
		
				
		/* GRIND */
		
		materialMap.put(SubCategory.MESSER_SCHAERFEN , new HashMap<>());
		materialMap.get(SubCategory.MESSER_SCHAERFEN).put(findByName("Rubstone"), 3);
		
		materialMap.put(SubCategory.SCHEREN , new HashMap<>());
		materialMap.get(SubCategory.SCHEREN).put(findByName("Rubstone"), 4);

		
		/* ELEKTRO */
		

		materialMap.put(SubCategory.LOETEN , new HashMap<>());
		materialMap.get(SubCategory.LOETEN).put(findByName("Solder"), 3);
		materialMap.get(SubCategory.LOETEN).put(findByName("Cable"), 4);
		
		materialMap.put(SubCategory.KABEL_ERSETZEN , new HashMap<>());
		materialMap.get(SubCategory.KABEL_ERSETZEN).put(findByName("Solder"), 2);
		materialMap.get(SubCategory.KABEL_ERSETZEN).put(findByName("Cable"), 7);
		
		this.materialMap = materialMap ;
	}

	/**
	 * Aktualisiert die Stückzahl aller Materialien einer Unterkategorie
	 * @param sub Unterkategorie
	 * @return Gibt true zurück, wenn alle Materialien einer Unterkategorie bei einem Auftrag aktualisiert
	 * werden können, sonst false
	 */
	public boolean updateMaterialAmount(SubCategory sub) {

		for(Material m : materialMap.get(sub).keySet()) {
			if(!reduceAmount(m.getName(),materialMap.get(sub).get(m))) {
				return false ;
			}
		}
		return true;

	}

	/**
	 * Aktualisiert die Stückzahl eines Materials mit entsprechender Identifikationsnummer
	 * einer Unterkategorie
	 * @param sub Unterkategorie eines Auftrags
	 * @param id Identifikationsnummer
	 * @param amount neue Stückzahl
	 */
	public void updateMaterialMap(SubCategory sub , Long id , int amount) {

		Map<Material, Integer> map = materialMap.get(sub) ;

		for(Material m : map.keySet()) {
			if(m.getId() == id) {
				materialMap.get(sub).remove(m);
				materialMap.get(sub).put(findById(id).get(), amount);
				return ;
			}
		}
		materialMap.get(sub).put(findById(id).get(), amount);
		
	}
}
