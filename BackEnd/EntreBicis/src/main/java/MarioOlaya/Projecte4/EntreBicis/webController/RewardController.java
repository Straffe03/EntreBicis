package MarioOlaya.Projecte4.EntreBicis.webController;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import MarioOlaya.Projecte4.EntreBicis.entity.Reward;
import MarioOlaya.Projecte4.EntreBicis.entity.StateType;
import MarioOlaya.Projecte4.EntreBicis.logic.RewardLogic;

/**
 * Controlador web per gestionar les operacions relacionades amb les
 * recompenses.
 * Aquest controlador proporciona funcionalitats per:
 * <ul>
 * <li>Llistar totes les recompenses disponibles o per usuari.</li>
 * <li>Llistar recompenses reservades.</li>
 * <li>Afegir noves recompenses mitjançant formularis.</li>
 * <li>Visualitzar els detalls d'una recompensa específica.</li>
 * <li>Eliminar recompenses.</li>
 * <li>Assignar recompenses en estat reservat a estat assignat.</li>
 * </ul>
 * Utilitza Thymeleaf per renderitzar les vistes i transmetre informació al
 * frontend.
 * També incorpora logging per rastrejar l'activitat dins del controlador.
 * 
 * @author Mario Olaya
 */
@Controller
@RequestMapping("/reward")
@Scope("request")
public class RewardController {

    @Autowired
    private RewardLogic rewardLogic;

    Logger log = LoggerFactory.getLogger(RewardController.class);

    /**
     * Mostra la llista de totes les recompenses.
     *
     * @param model Model de Spring per passar dades a la vista.
     * @return La plantilla Thymeleaf per mostrar les recompenses disponibles.
     */
    @GetMapping("/list")
    public String llistarRewards(Model model) {
        log.info("S'estan llistant totes les recompenses");

        List<Reward> rewards = rewardLogic.listAll();
        log.info("Llistant totes les recompenses (", rewards.size(), ").");

        List<Map<String, Object>> recompensesAmbImatge = rewards.stream().map(reward -> {
            Map<String, Object> rewardData = new HashMap<>();
            rewardData.put("id", reward.getId());
            rewardData.put("name", reward.getName());
            rewardData.put("storeName", reward.getStoreName());
            rewardData.put("observations", reward.getObservations());
            rewardData.put("adress", reward.getAdress());
            rewardData.put("state", reward.getState());
            rewardData.put("points", reward.getPoints());
            rewardData.put("description", reward.getDescription());
            rewardData.put("user", reward.getUser());
            rewardData.put("date", reward.getDate());
            rewardData.put("reservationDate", reward.getReservationDate());
            rewardData.put("assignationDate", reward.getAssignationDate());
            rewardData.put("pickupDate", reward.getPickupDate());

            if (reward.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(reward.getImage());
                rewardData.put("imatgeBase64", imatgeBase64);
                log.info("S'ha carregat la imatge de la recompensa " + reward.getName());
            } else {
                rewardData.put("imatgeBase64", null);
                log.info("La recompensa " + reward.getName() + " no té cap imatge associada.");
            }

            return rewardData;
        }).collect(Collectors.toList());

        model.addAttribute("rewards", recompensesAmbImatge);

        return "rewards-list";
    }

    /**
     * Mostra la llista de recompenses de un usuari.
     *
     * @param model Model de Spring per passar dades a la vista.
     * @return La plantilla Thymeleaf per mostrar les recompenses.
     */
    @GetMapping("/list/{email}")
    public String llistarRewards(@PathVariable String email, Model model) {
        log.info("S'estan llistant totes les recompenses per l'usuari amb email: {}", email);
        List<Reward> rewards = rewardLogic.getRewardsByUser(email);

        log.info("Llistant totes les recompenses del usuari (", rewards.size(), ").");

        List<Map<String, Object>> recompensesAmbImatge = rewards.stream().map(reward -> {
            Map<String, Object> rewardData = new HashMap<>();
            rewardData.put("id", reward.getId());
            rewardData.put("name", reward.getName());
            rewardData.put("storeName", reward.getStoreName());
            rewardData.put("observations", reward.getObservations());
            rewardData.put("adress", reward.getAdress());
            rewardData.put("state", reward.getState());
            rewardData.put("points", reward.getPoints());
            rewardData.put("description", reward.getDescription());
            rewardData.put("user", reward.getUser());
            rewardData.put("date", reward.getDate());
            rewardData.put("reservationDate", reward.getReservationDate());
            rewardData.put("assignationDate", reward.getAssignationDate());
            rewardData.put("pickupDate", reward.getPickupDate());

            if (reward.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(reward.getImage());
                rewardData.put("imatgeBase64", imatgeBase64);
                log.info("S'ha carregat la imatge de la recompensa " + reward.getName());
            } else {
                rewardData.put("imatgeBase64", null);
                log.info("La recompensa " + reward.getName() + " no té cap imatge associada.");
            }

            return rewardData;
        }).collect(Collectors.toList());

        model.addAttribute("rewards", recompensesAmbImatge);

        return "rewards-list";
    }

    /**
     * Mostra la llista de recompenses reservades.
     *
     * @param model Model de Spring per passar dades a la vista.
     * @return La plantilla Thymeleaf per mostrar les recompenses reservades.
     */
    @GetMapping("/reservades")
    public String llistarReservades(Model model) {

        log.info("S'estan llistant totes les recompenses reservades ");

        List<Reward> rewards = rewardLogic.listAllByState(StateType.RESERVADA);
        log.info("Llistant totes les recompenses (", rewards.size(), ").");

        List<Map<String, Object>> recompensesAmbImatge = rewards.stream().map(reward -> {
            Map<String, Object> rewardData = new HashMap<>();
            rewardData.put("id", reward.getId());
            rewardData.put("name", reward.getName());
            rewardData.put("storeName", reward.getStoreName());
            rewardData.put("observations", reward.getObservations());
            rewardData.put("adress", reward.getAdress());
            rewardData.put("state", reward.getState());
            rewardData.put("points", reward.getPoints());
            rewardData.put("description", reward.getDescription());
            rewardData.put("user", reward.getUser());

            if (reward.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(reward.getImage());
                rewardData.put("imatgeBase64", imatgeBase64);
                log.info("S'ha carregat la imatge de la recompensa " + reward.getName());
            } else {
                rewardData.put("imatgeBase64", null);
                log.info("La recompensa " + reward.getName() + " no té cap imatge associada.");
            }

            return rewardData;
        }).collect(Collectors.toList());

        model.addAttribute("rewards", recompensesAmbImatge);

        return "rewards-list-reservades";
    }

    /**
     * Mostra el formulari per afegir una nova recompensa.
     *
     * @param model Model de Spring per passar dades a la vista.
     * @return La plantilla Thymeleaf per afegir una recompensa.
     */
    @GetMapping("/create")
    public String mostrarFormulariAfegir(Model model) {
        log.info("Accedint al formulari d'alta de recompenses.");
        model.addAttribute("reward", new Reward());
        return "reward-create";
    }

    /**
     * Processa el formulari per afegir una nova recompensa.
     *
     * @param recompensa L'objecte recompensa amb les dades del formulari.
     * @param result     Resultat de la validació del formulari.
     * @return Redirecció a la llista de recompenses si té èxit, o torna al
     *         formulari si hi ha errors.
     */
    @PostMapping("/create")
    public String processarFormulariAfegir(@ModelAttribute("reward") Reward reward,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws Exception {
        log.info("Inici del procés d'alta per ala recompensa amb id: {}", reward.getId());

        try {
            if (!imageFile.isEmpty()) {
                reward.setImage(imageFile.getBytes());
            }
            rewardLogic.createReward(reward);
            log.info("Recompensa creada correctament amb id: {}", reward.getId());

        } catch (Exception e) {
            log.error("Error durant el registre de la recompensa amb id: {}", reward.getId(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reward", reward);

            // Si hay una imagen seleccionada, mantenerla en el modelo
            if (reward.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(reward.getImage());
                model.addAttribute("imatgeBase64", imatgeBase64);
            }

            return "reward-create";
        }
        log.info("Procés d'alta finalitzat amb èxit per a la recompensa amb id: {}", reward.getId());
        return "redirect:/reward/list";
    }

    /**
     * Mostra els detalls d'una recompensa específica.
     *
     * @param id                 L'ID de la recompensa a mostrar.
     * @param model              Model de Spring per passar dades a la vista.
     * @param redirectAttributes Atributs per redirecció.
     * @return La plantilla Thymeleaf per mostrar els detalls de la recompensa.
     */
    @GetMapping("/view/{id}")
    public String consultaClient(@PathVariable("id") Long id, Model model,
            RedirectAttributes redirectAttributes) {
        log.info("S'estan consultant els detalls del usuari amb email: {}", id);
        Reward reward = rewardLogic.getById(id);

        if (reward == null) {
            log.error("No s'ha trobat cap recompensa amb l'ID: {}", id);
            redirectAttributes.addFlashAttribute("error", "No s'ha trobat cap recompensa amb l'ID: " + id);
            return "redirect:/reward/list";
        }

        if (reward.getImage() != null) {
            String imatgeBase64 = Base64.getEncoder().encodeToString(reward.getImage());
            model.addAttribute("imatgeBase64", imatgeBase64);
            log.info("S'ha carregat la imatge de la recompensa amb id: {}", id);
        } else {
            model.addAttribute("imatgeBase64", null);
            log.info("No s'ha trobat cap imatge per a la recompensa amb id: {}", id);
        }

        model.addAttribute("reward", reward);
        log.info("S'han carregat els detalls de la recompensa amb id: {}", id);
        return "reward-view"; // Nom del fitxer HTML
    }

    /**
     * Elimina una recompensa específica.
     * 
     * @param id                 L'ID de la recompensa a eliminar.
     * @param redirectAttributes Atributs per redirecció.
     * @return Redirecció a la llista de recompenses.
     */
    @GetMapping("/delete/{id}")
    public String eliminarRecompensa(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("S'està eliminant la recompensa amb id: {}", id);
        try {
            Reward reward = rewardLogic.deleteReward(id);
            redirectAttributes.addFlashAttribute("success", "Recompensa eliminada correctament: " + reward.getName());
            log.info("Recompensa eliminada correctament amb id: {}", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            log.error("Error durant l'eliminació de la recompensa amb id: {}", id, e);
        }
        return "redirect:/reward/list";
    }

    /**
     * Assigna una recompensa passant-la de RESERVADA a ASSIGNADA.
     * 
     * @param id ID de la recompensa
     * @return Redirecció a la vista de la recompensa
     */
    @GetMapping("/assign/{id}")
    public String assignReward(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            log.info("S'està assignant la recompensa amb id: {}", id);
            Reward reward = rewardLogic.getById(id);

            if (reward.getState() == StateType.RESERVADA) {
                reward.setState(StateType.ASSIGNADA);
                rewardLogic.updateReward(reward);
                redirectAttributes.addFlashAttribute("success", "Recompensa assignada correctament.");
            } else {
                redirectAttributes.addFlashAttribute("error", "La recompensa no està en estat reservada.");
            }

            return "redirect:/reward/view/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reward/list";
        }

    }

}
