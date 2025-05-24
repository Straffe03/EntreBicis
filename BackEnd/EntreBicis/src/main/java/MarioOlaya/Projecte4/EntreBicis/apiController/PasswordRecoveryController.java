package MarioOlaya.Projecte4.EntreBicis.apiController;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.logic.EmailLogic;
import MarioOlaya.Projecte4.EntreBicis.logic.UserLogic;

/**
 * Controlador que gestiona la recuperació de contrasenyes mitjançant correu
 * electrònic.
 */
@RestController
@RequestMapping("/api/auth")
public class PasswordRecoveryController {

    @Autowired
    private UserLogic userService;

    @Autowired
    private EmailLogic emailService;

    private static final Logger logger = LoggerFactory.getLogger(PasswordRecoveryController.class);

    /**
     * Endpoint per recuperar la contrasenya d'un usuari registrat.
     *
     * @param email Correu electrònic de l'usuari que sol·licita la recuperació.
     * @return ResponseEntity amb l'estat de l'operació.
     */
    @PostMapping("/recover")
    public ResponseEntity<?> recoverPassword(@RequestParam String email) {
        Optional<User> userOpt = userService.getUser(email);

        if (userOpt.isEmpty()) {
            logger.error("Correu no registrat: {}", email);
            return ResponseEntity.badRequest()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body("Correu no registrat");
        }

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        userService.updatePassword(email, tempPassword);

        emailService.sendEmail(
                email,
                "Recuperació de contrasenya - EntreBicis",
                "Hola,\n\n" +
                        "Hem rebut una sol·licitud per restablir la contrasenya del teu compte d'EntreBicis.\n" +
                        "Aquí tens la teva nova contrasenya temporal:\n\n" +
                        "🔐 Contrasenya temporal: " + tempPassword + "\n\n" +
                        "Per motius de seguretat, et recomanem que canviïs aquesta contrasenya tan aviat com accedeixis a la teva compte.\n\n"
                        +
                        "Si no has sol·licitat aquest canvi, posa't en contacte amb nosaltres immediatament.\n\n" +
                        "Gràcies per confiar en EntreBicis.\n\n" +
                        "Atentament,\n" +
                        "L’equip d’EntreBicis\n" +
                        "📩 suport@entrebicis.cat\n\n" +
                        "———\n" +
                        "Aquest missatge s’ha generat automàticament. Si tens qualsevol dubte, no dubtis a escriure’ns.");

        logger.info("S'ha enviat un correu de recuperació de contrasenya a: {}", email);
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body("S'ha enviat un correu amb la nova contrasenya");
    }
}
