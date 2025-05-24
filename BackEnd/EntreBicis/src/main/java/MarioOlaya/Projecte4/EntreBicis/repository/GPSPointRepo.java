package MarioOlaya.Projecte4.EntreBicis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MarioOlaya.Projecte4.EntreBicis.entity.GPSPoint;

/**
 * Repositori per gestionar l'entitat {@link GPSPoint}.
 * 
 * Aquesta interfície proporciona operacions CRUD per als punts GPS utilitzant
 * Spring Data JPA.
 * 
 * @author Mario Olaya
 */
@Repository
public interface GPSPointRepo extends JpaRepository<GPSPoint, Long> {
}
