package MarioOlaya.Projecte4.EntreBicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MarioOlaya.Projecte4.EntreBicis.entity.Route;

/**
 * Repositori per gestionar l'entitat {@link Route}.
 * 
 * Proporciona funcionalitats per recuperar i manipular rutes,
 * incloent la recuperació de rutes per usuari.
 * 
 * @author Mario Olaya
 */
@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {

    /**
     * Retorna totes les rutes d'un usuari específic segons el seu correu
     * electrònic.
     * 
     * @param userId Correu electrònic de l'usuari
     * @return Llista de rutes associades a l'usuari
     */
    List<Route> findByUserEmail(String userId);
}
