package MarioOlaya.Projecte4.EntreBicis.logic;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MarioOlaya.Projecte4.EntreBicis.entity.Reward;
import MarioOlaya.Projecte4.EntreBicis.entity.StateType;
import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.repository.RewardRepo;
import jakarta.persistence.EntityNotFoundException;

/**
 * Servei de lògica per gestionar les operacions relacionades amb les
 * recompenses.
 */
@Service
public class RewardLogic {
    private static final Logger logger = LoggerFactory.getLogger(RewardLogic.class);

    @Autowired
    private RewardRepo rewardRepo;

    /**
     * Recupera la llista completa de recompenses.
     * 
     * @return Una llista amb totes les recompenses.
     */
    public List<Reward> listAll() {
        logger.info("Recuperant la llista completa de recompenses");
        return rewardRepo.findAll();
    }

    /**
     * Recupera una recompensa per la seva ID.
     * 
     * @param id L'ID de la recompensa a recuperar.
     * @return La recompensa amb l'ID especificat, o null si no existeix.
     */
    public Reward getById(Long id) {
        logger.info("Recuperant la recompensa amb ID: {}", id);
        return rewardRepo.findById(id).orElse(null);
    }

    /**
     * Recupera una llista de recompenses filtrades per estat.
     * 
     * @param state L'estat de les recompenses a recuperar.
     * @return Una llista de recompenses amb l'estat especificat.
     */
    public List<Reward> listAllByState(StateType state) {
        logger.info("Recuperant la llista de recompenses amb estat: {}", state);
        return rewardRepo.findByState(state);
    }

    /**
     * Crea una nova recompensa i l'emmagatzema a la base de dades.
     * Estableix l'estat de la recompensa a DISPONIBLE per defecte.
     * 
     * @param reward La recompensa a crear.
     * @throws IllegalArgumentException Si la recompensa ja existeix.
     * @return La recompensa creada.
     */
    public Reward createReward(Reward reward) {
        logger.info("Creant una nova recompensa: {}", reward.getName());
        reward.setState(StateType.DISPONIBLE);
        reward.setDate(LocalDate.now());

        return rewardRepo.save(reward);
    }

    /**
     * Elimina una recompensa pel seu ID si està disponible.
     * 
     * @param id L'ID de la recompensa a eliminar.
     * @return La recompensa eliminada.
     * @throws Exception Si la recompensa no existeix o no està disponible.
     */
    public Reward deleteReward(Long id) throws Exception {
        logger.info("Eliminant la recompensa amb ID: {}", id);
        Reward reward = getById(id);
        if (reward != null) {
            if (reward.getState() != StateType.DISPONIBLE) {
                logger.warn("No es pot eliminar la recompensa {} perquè no està disponible.", reward.getName());
                throw new Exception(
                        "No es pot eliminar la recompensa " + reward.getName() + " perquè no està disponible.");
            }
            rewardRepo.delete(reward);
            return reward;
        } else {
            logger.warn("No s'ha trobat la recompensa amb ID: {}", id);
            throw new Exception("No s'ha trobat la recompensa amb ID: " + id);
        }
    }

    /**
     * Actualitza una recompensa existent a la base de dades.
     * 
     * @param updatedReward L'objecte recompensa amb els canvis
     * @return Recompensa actualitzada
     */
    public Reward updateReward(Reward updatedReward) {
        try {
            logger.info("Actualitzant la recompensa amb ID: {}", updatedReward.getId());
            Reward existing = rewardRepo.findById(updatedReward.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Recompensa no trobada amb ID: " + updatedReward.getId()));

            existing.setUser(updatedReward.getUser());

            if (updatedReward.getState() == StateType.RESERVADA) {
                List<Reward> rewards = rewardRepo.findByUserEmail(updatedReward.getUser().getEmail());
                for (Reward reward : rewards) {
                    if (reward.getState() == StateType.RESERVADA && !reward.getId().equals(updatedReward.getId())) {
                        logger.warn("Usuari {} ja té una recompensa reservada.", updatedReward.getUser().getEmail());
                        throw new Exception("No es pot reservar perquè l'usuari ja té una recompensa reservada.");
                    }
                }
            }

            existing.setState(updatedReward.getState());

            if (updatedReward.getState() == StateType.ASSIGNADA) {
                existing.setAssignationDate(LocalDate.now());
                User user = updatedReward.getUser();
                if (user.getPoints() < updatedReward.getPoints()) {
                    logger.warn("Usuari {} no té prou punts per a la recompensa {}", user.getEmail(),
                            updatedReward.getName());
                    throw new Exception("No es pot assignar la recompensa " + updatedReward.getName()
                            + " perquè l'usuari no té prou punts.");
                } else {
                    logger.info("Assignant la recompensa {} a l'usuari {}", updatedReward.getName(), user.getEmail());
                    user.setPoints(user.getPoints() - updatedReward.getPoints());
                }
                List<Reward> rewards = rewardRepo.findByUserEmail(updatedReward.getUser().getEmail());
                for (Reward reward : rewards) {
                    if (reward.getState() == StateType.ASSIGNADA && !reward.getId().equals(updatedReward.getId())) {
                        logger.warn("Usuari {} ja té una recompensa assignada.", updatedReward.getUser().getEmail());
                        throw new Exception("No es pot assignar la recompensa " + updatedReward.getName()
                                + " perquè l'usuari ja té una recompensa assignada.");
                    }
                }
            }

            if (updatedReward.getState() == StateType.RECOLLIDA) {
                existing.setPickupDate(LocalDate.now());
            }

            if (updatedReward.getState() == StateType.RESERVADA) {
                existing.setReservationDate(LocalDate.now());
            }

            logger.info("Recompensa amb ID {} actualitzada correctament", existing.getId());
            return rewardRepo.save(existing);
        } catch (Exception e) {
            logger.error("Error al actualitzar la recompensa amb ID: {} - {}", updatedReward.getId(), e.getMessage(),
                    e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Llista totes les recompenses d'un usuari donat el seu correu.
     * 
     * @param email Correu electrònic de l'usuari
     * @return Llista de recompenses
     */
    public List<Reward> getRewardsByUser(String email) {
        logger.info("Recuperant recompenses de l'usuari: {}", email);
        return rewardRepo.findByUserEmail(email);
    }
}
