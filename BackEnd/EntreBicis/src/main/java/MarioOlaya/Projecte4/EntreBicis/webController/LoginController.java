package MarioOlaya.Projecte4.EntreBicis.webController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.logic.UserLogic;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador web per gestionar el procés d'autenticació d'usuaris.
 * Proporciona funcionalitats per mostrar la pàgina de login i verificar
 * les credencials dels usuaris.
 */

@Controller
@Scope("session")
public class LoginController {

    @Autowired
    private UserLogic userLogic;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    /**
     * Gestiona les peticions GET per a la pàgina de login.
     *
     * @param error  Paràmetre opcional que indica si hi ha hagut un error
     *               d'autenticació. Si no és null, es mostra un missatge
     *               d'error a l'usuari.
     * @param logout Paràmetre opcional que indica si l'usuari ha tancat
     *               la sessió correctament. Si no és null, es mostra un
     *               missatge de confirmació.
     * @param model  Model utilitzat per passar dades a la vista.
     * @return El nom de la vista corresponent a la pàgina de login.
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            log.warn("Error d'autenticació: Usuari o contrasenya incorrectes.");
            model.addAttribute("error",
                    "L'Usuari o la contrasenya són incorrectes o no es troben a la llista d'administradors.");
        }
        if (logout != null) {
            log.info("Sessió tancada correctament.");
            model.addAttribute("logout", "Sessió tancada correctament");
        }
        return "login";
    }

    /**
     * Gestiona la verificació de credencials d'un usuari mitjançant una petició
     * POST.
     *
     * @param email       El correu electrònic de l'usuari proporcionat al
     *                    formulari.
     * @param contrasenya La contrasenya de l'usuari proporcionada al formulari.
     * @param model       L'objecte Model utilitzat per passar dades a la vista.
     * @return Una cadena que indica la vista a mostrar. Retorna "/login" si les
     *         credencials
     *         són incorrectes, o redirigeix a la pàgina principal si l'autenticació
     *         és correcta.
     */
    @PostMapping("/verificar")
    public String postMethodName(@RequestParam("usuari") String email,
            @RequestParam("password") String contrasenya,
            Model model) {

        log.info("Iniciant verificació de credencials per a l'usuari: {}", email);
        Optional<User> user = userLogic.getUser(email);
        if (user.isEmpty() || !user.get().getPassword().equals(contrasenya)) {
            log.warn("Error d'autenticació per a l'usuari: {}", email);
            String error = "Usuari o contrasenya incorrectes, torna a intentar";
            model.addAttribute("error", error);
            return "/login";

        }
        log.info("Usuari autenticat correctament: {}", email);
        return "redirect:/";
    }

    /**
     * Gestiona el procés de logout de l'usuari.
     * 
     * Aquest mètode:
     * - Accedeix a l'autenticació actual.
     * - Finalitza la sessió i neteja el context de seguretat si l'usuari està
     * autenticat.
     * - Redirigeix a la pàgina de login amb un missatge indicant que la sessió s'ha
     * tancat.
     * 
     * @param request  Petició HTTP de l'usuari.
     * @param response Resposta HTTP per al navegador de l'usuari.
     * @return Redirecció a la pàgina de login amb un missatge de logout.
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Obtiene la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            // Maneja la sesión y limpia el contexto de seguridad
            log.info("Finalitzant la sessió per a l'usuari: {}", auth.getName());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            log.info("Sessió finalitzada correctament per a l'usuari: {}", auth.getName());

        }

        // Redirige al usuario a la página de login con mensaje
        log.info("Redirigint a la pàgina de login amb el missatge de logout.");
        return "redirect:/login?logout=true";
    }
}
