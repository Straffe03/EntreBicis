package MarioOlaya.Projecte4.EntreBicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MarioOlaya.Projecte4.EntreBicis.entity.Reward;
import MarioOlaya.Projecte4.EntreBicis.entity.StateType;

/**
 * Repositori per gestionar l'entitat {@link Reward}.
 * 
 * Proporciona operacions de persistència per a les recompenses, incloent
 * mètodes personalitzats per filtrar per estat i per usuari.
 * 
 * @author Mario Olaya
 */
@Repository
public interface RewardRepo extends JpaRepository<Reward, Long> {

    /**
     * Retorna totes les recompenses amb un estat determinat.
     * 
     * @param state Estat de la recompensa (ex. DISPONIBLE, RESERVADA)
     * @return Llista de recompenses amb l'estat especificat
     */
    List<Reward> findByState(StateType state);

    /**
     * Retorna totes les recompenses associades a un usuari.
     * 
     * @param email Correu electrònic de l'usuari
     * @return Llista de recompenses
     */
    List<Reward> findByUserEmail(String email);
}
