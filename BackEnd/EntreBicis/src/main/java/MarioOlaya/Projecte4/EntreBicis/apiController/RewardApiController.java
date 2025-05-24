package MarioOlaya.Projecte4.EntreBicis.apiController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MarioOlaya.Projecte4.EntreBicis.entity.Reward;
import MarioOlaya.Projecte4.EntreBicis.entity.StateType;
import MarioOlaya.Projecte4.EntreBicis.logic.RewardLogic;

/**
 * Controlador REST per gestionar les recompenses.
 */
@RestController
@RequestMapping("/api/reward")
@CrossOrigin(origins = "*")
public class RewardApiController {

    @Autowired
    private RewardLogic rewardLogic;

    private static final Logger logger = LoggerFactory.getLogger(RewardApiController.class);

    /**
     * Retorna totes les recompenses disponibles.
     *
     * @return Llista de recompenses disponibles.
     */
    @GetMapping("/list")
    public List<Reward> getAllRewards() {
        logger.info("S'estan obtenint totes les recompenses disponibles.");
        return rewardLogic.listAllByState(StateType.DISPONIBLE);
    }

    /**
     * Obté una recompensa pel seu ID.
     *
     * @param rewardId L'ID de la recompensa.
     * @return La recompensa amb l'ID especificat.
     */
    @GetMapping("/view/{rewardId}")
    public ResponseEntity<Reward> getRewardById(@PathVariable Long rewardId) {
        logger.info("S'està consultant la recompensa amb ID: {}", rewardId);
        Reward reward = rewardLogic.getById(rewardId);
        return ResponseEntity.ok(reward);
    }

    /**
     * Actualitza una recompensa a partir de l'objecte rebut.
     *
     * @param updatedReward L'objecte recompensa complet a actualitzar.
     * @return Recompensa actualitzada o missatge d'error.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateReward(@RequestBody Reward updatedReward) {
        try {
            logger.info("S'està actualitzant la recompensa amb ID: {}", updatedReward.getId());
            Reward saved = rewardLogic.updateReward(updatedReward);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Error en actualitzar la recompensa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    /**
     * Retorna totes les recompenses d'un usuari pel seu correu electrònic.
     *
     * @param email Correu de l'usuari.
     * @return Llista de recompenses de l'usuari.
     */
    @GetMapping("/list/{email}")
    public ResponseEntity<List<Reward>> getRewardsByUser(@PathVariable String email) {
        logger.info("S'estan obtenint les recompenses per a l'usuari amb email: {}", email);
        return ResponseEntity.ok(rewardLogic.getRewardsByUser(email));
    }
}
