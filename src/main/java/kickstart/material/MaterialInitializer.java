package kickstart.material;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kickstart.staff.StaffDataInitializer;

/**
 * Repräsentiert einen vordefinierten Anfangszustand des Materials einer Filiale
 */
@Component
@Order(10)
public class MaterialInitializer  implements DataInitializer{
	
	private static final Logger LOG = LoggerFactory.getLogger(StaffDataInitializer.class);

	private final MaterialManagement materialManagement;

	/**
	 * Erstellung des Anfangszustandes
	 * @param materialManagement Materialverwaltungseinheit einer Filiale
	 */
	MaterialInitializer(MaterialManagement materialManagement){
		
		Assert.notNull(materialManagement, " MaterialManagement must not be null!");
		this.materialManagement = materialManagement ;
	}

	/**
	 * Materialien des Anfangszustandes werden hinzugefügt
	 */
	@Override
	public void initialize() {
		if(materialManagement.findAll().toList().size() != 0){
			materialManagement.initializeMaterialMap();
			return;
		}
		
		LOG.info("Creating default materials.");
		
		List.of(
				// Elektrowerkstatt --> ELEKTRO
				new Material("Solder" ,100, 10 , 100),
				new Material("Cable" , 20, 7 , 20),
				// Schleiferei --> GRIND
				new Material("Rubstone" , 32 ,9 , 32),
				// Schlüsseldienst --> KEY 
				new Material("Brute" , 49 , 9 , 49),
				// Flickschusterei --> PATH
				new Material("Heel" , 100 , 1 , 100),
				new Material("Sole" , 48 , 15 , 48),
				new Material("Wire" , 1000 , 12 , 1000),
				// Nähservice --> SEW
				new Material("Knob" , 20 , 5 , 20),
				// Schnellreinigung --> CLEAN
				new Material("Detergent" , 40 , 12 , 40),
				new Material("Shoe cream" , 120 , 23 , 120)
				
		).forEach(materialManagement::addMaterial);
		
		materialManagement.initializeMaterialMap();
	}
}
