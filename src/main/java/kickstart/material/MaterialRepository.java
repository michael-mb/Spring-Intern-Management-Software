package kickstart.material;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Datenbank der Materialien einer Filiale
 */
interface MaterialRepository extends CrudRepository<Material, Long>{
	@Override
	Streamable<Material> findAll();
	
}
