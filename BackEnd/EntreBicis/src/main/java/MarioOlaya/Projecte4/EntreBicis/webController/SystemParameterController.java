package MarioOlaya.Projecte4.EntreBicis.webController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;
import MarioOlaya.Projecte4.EntreBicis.logic.SystemParameterLogic;

/**
 * Controlador per gestionar els paràmetres del sistema.
 * 
 * <p>
 * Proporciona funcionalitats per:
 * </p>
 * <ul>
 * <li>Visualitzar el formulari de modificació dels paràmetres del sistema.</li>
 * <li>Actualitzar els valors configurables com distància mínima o punts per
 * km.</li>
 * </ul>
 * 
 * <p>
 * Totes les operacions es registren mitjançant el sistema de logging.
 * </p>
 * 
 * @author Mario Olaya
 */
@Controller
@RequestMapping("/parameters")
@Scope("request")
public class SystemParameterController {

    private static final Logger log = LoggerFactory.getLogger(SystemParameterController.class);

    @Autowired
    private SystemParameterLogic systemParameterLogic;

    /**
     * Mostra el formulari per actualitzar els paràmetres del sistema.
     *
     * @param model Model per passar dades a la vista.
     * @return Nom de la vista Thymeleaf per modificar els paràmetres.
     */
    @GetMapping("/update")
    public String showUpdateSystemParameters(Model model) {
        log.info("Mostrant el formulari de modificació dels paràmetres del sistema.");

        SystemParameter systemParameter = systemParameterLogic.obtenerParametroPorId();
        if (systemParameter == null) {
            log.warn("No s'ha trobat cap paràmetre del sistema. Es crea un per defecte.");
            systemParameter = new SystemParameter();
        }

        model.addAttribute("systemParameter", systemParameter);
        return "systemParameter-modify";
    }

    /**
     * Processa la modificació dels paràmetres del sistema.
     *
     * @param systemParameter Objecte amb els nous valors configurats.
     * @param model           Model per passar dades a la vista.
     * @return Vista actualitzada amb el resultat de la modificació.
     */
    @PostMapping("/update")
    public String updateSystemParameters(SystemParameter systemParameter, Model model) {
        log.info("S'estan actualitzant els paràmetres del sistema amb valors: {}", systemParameter);

        systemParameterLogic.actualizarParametros(systemParameter);

        SystemParameter updatedSystemParameter = systemParameterLogic.obtenerParametroPorId();
        model.addAttribute("systemParameter", updatedSystemParameter);
        model.addAttribute("successMessage", "Paràmetres del sistema actualitzats correctament.");

        log.info("Paràmetres del sistema actualitzats amb èxit.");
        return "systemParameter-modify";
    }
}
