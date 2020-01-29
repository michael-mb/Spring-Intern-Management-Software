package kickstart.staff;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Datenbank der Mitarbeiter einer Filiale
 */
interface StaffRepository extends CrudRepository<Staff, Long> {
	@Override
	Streamable<Staff> findAll();
}
