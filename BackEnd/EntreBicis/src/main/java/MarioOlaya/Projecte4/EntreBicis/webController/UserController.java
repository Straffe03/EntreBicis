package MarioOlaya.Projecte4.EntreBicis.webController;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.logic.UserLogic;

/**
 * Controlador per gestionar les operacions relacionades amb els usuaris.
 * <p>
 * Aquest controlador proporciona funcionalitats per:
 * </p>
 * <ul>
 * <li>Llistar tots els usuaris.</li>
 * <li>Consultar detalls d’un usuari concret.</li>
 * <li>Registrar un nou usuari.</li>
 * <li>Modificar la informació d’un usuari existent.</li>
 * <li>Mostrar la imatge de perfil d’un usuari.</li>
 * </ul>
 * 
 * <p>
 * Utilitza el servei {@link UserLogic} per accedir a la lògica de negoci.
 * Inclou registres detallats de totes les accions mitjançant SLF4J
 * {@link Logger}.
 * </p>
 * 
 * @author Mario
 */
@Controller
@RequestMapping("/user")
@Scope("session")
public class UserController {

    @Autowired
    private UserLogic userLogic;

    Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * Mètode per obtenir una llista de tots els usuaris.
     * 
     * @param model Objecte Model utilitzat per passar dades a la vista.
     * @return El nom de la vista "user-list" que mostra la llista d'usuaris.
     */
    @GetMapping("/list")
    public String llistarClients(Model model) {
        log.info("S'estàn llistant tots els usuaris");

        List<User> users = userLogic.listAll();
        log.info("Llistant tots els clients (", users.size(), ").");

        List<Map<String, Object>> usuarisAmbImatge = users.stream().map(user -> {
            Map<String, Object> userData = new HashMap<>();
            userData.put("email", user.getEmail());
            userData.put("name", user.getName());
            userData.put("surname", user.getSurname());
            userData.put("phone", user.getPhone());
            userData.put("userType", user.getUserType());
            userData.put("points", user.getPoints());
            userData.put("observations", user.getObservations());

            if (user.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(user.getImage());
                userData.put("imatgeBase64", imatgeBase64);
                log.info("S'ha carregat la imatge de l'usuari " + user.getName());
            } else {
                userData.put("imatgeBase64", null);
                log.info("L'usuari " + user.getName() + " no té cap imatge associada.");
            }

            return userData;
        }).collect(Collectors.toList());

        model.addAttribute("user", usuarisAmbImatge);
        return "user-list";
    }

    /**
     * Consulta els detalls d'un usuari.
     *
     * @param email              El email del usuari a consultar.
     * @param model              Model per passar dades a la vista.
     * @param redirectAttributes Atributs per passar missatges de redirecció.
     * @return El nom de la vista per consultar els detalls del client.
     */
    @GetMapping("/view/{email}")
    public String consultaClient(@PathVariable("email") String email, Model model,
            RedirectAttributes redirectAttributes) {
        log.info("S'estan consultant els detalls del usuari amb email: {}", email);
        Optional<User> user = userLogic.getUser(email);

        if (user.isEmpty()) {
            log.error("No s'ha trobat cap usuari amb el email: {}", email);
            redirectAttributes.addAttribute("error", "No s'ha trobat el client");
            return "redirect:/user/list";
        }

        User existent = user.get();
        if (existent.getImage() != null) {
            String imatgeBase64 = Base64.getEncoder().encodeToString(existent.getImage());
            model.addAttribute("imatgeBase64", imatgeBase64);
            log.info("S'ha carregat la imatge de l'usuari amb email: {}", email);
        } else {
            model.addAttribute("imatgeBase64", null);
            log.info("No s'ha trobat cap imatge per a l'usuari amb email: {}", email);
        }

        model.addAttribute("user", existent);
        log.info("S'han carregat els detalls del usuari amb email: {}", email);
        return "user-view"; // Nom del fitxer HTML
    }

    /**
     * Gestiona la petició GET per accedir al formulari de creació d'usuaris.
     * 
     * @param model L'objecte Model utilitzat per passar dades a la vista. Si no
     *              conté
     *              un atribut "user", s'afegeix un nou objecte User al model.
     * @return El nom de la vista "user-create" que mostra el formulari de creació
     *         d'usuaris.
     */
    @GetMapping("/create")
    public String mostrarFormulari(Model model) {
        log.info("Accedint al formulari d'alta de usuaris.");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "user-create";
    }

    /**
     * Gestiona la creació d'un nou usuari a través d'una petició POST.
     *
     * @param user      L'objecte User que conté les dades del nou usuari.
     * @param imageFile El fitxer d'imatge carregat per l'usuari.
     * @param model     L'objecte Model utilitzat per passar dades a la vista.
     * @return Una cadena que indica la vista a mostrar o una redirecció.
     * @throws Exception Si es produeix un error durant el procés de registre.
     *
     *                   Aquest mètode processa la informació enviada per l'usuari
     *                   per registrar-se.
     *                   Si es proporciona una imatge, aquesta es converteix a bytes
     *                   i s'assigna a l'usuari.
     *                   En cas d'error, es registra el problema, es manté la
     *                   informació de l'usuari i
     *                   la imatge (si n'hi ha) al model, i es retorna a la vista de
     *                   creació d'usuari.
     *                   Si el procés és correcte, es redirigeix a la llista
     *                   d'usuaris.
     */
    @PostMapping("/create")
    public String registrarClient(@ModelAttribute("user") User user,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) throws Exception {

        // TODO ROL NO PERSISTE TRAS RECARGA POR ERROR
        log.info("Inici del procés d'alta per al usuari amb email: {}", user.getEmail());

        try {
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            }
            userLogic.createUser(user);
            log.info("Usuari registrat correctament amb email: {}", user.getEmail());

        } catch (Exception e) {
            log.error("Error durant el registre del client amb email: {}", user.getEmail(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);

            // Si hay una imagen seleccionada, mantenerla en el modelo
            if (user.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(user.getImage());
                model.addAttribute("imatgeBase64", imatgeBase64);
            }

            return "user-create";
        }
        log.info("Procés d'alta finalitzat amb èxit per al usuari amb email: {}", user.getEmail());
        return "redirect:/user/list";

    }

    @GetMapping("/profile-image/{email}")
    @ResponseBody
    public ResponseEntity<byte[]> obtenirImatgePerfilBase64(@PathVariable("email") String email) {
        log.info("S'està sol·licitant la imatge de perfil per a l'usuari amb email: {}", email);

        Optional<User> user = userLogic.getUser(email);
        if (user.isEmpty() || user.get().getImage() == null) {
            log.warn("No s'ha trobat cap imatge per a l'usuari amb email: {}", email);
            return ResponseEntity.notFound().build();
        }

        byte[] image = user.get().getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Cambia a IMAGE_PNG si las imágenes son PNG
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    /**
     * Gestiona la petició GET per accedir al formulari de modificació d'un usuari.
     *
     * @param email              El correu electrònic de l'usuari que es vol
     *                           modificar.
     * @param model              L'objecte Model utilitzat per passar dades a la
     *                           vista.
     * @param redirectAttributes Objecte per afegir atributs a la redirecció en cas
     *                           d'error.
     * @return El nom del fitxer HTML corresponent al formulari de modificació
     *         o una redirecció a la llista d'usuaris si no es troba l'usuari.
     */
    @GetMapping("/modify/{email}")
    public String modificarClient(@PathVariable("email") String email, Model model,
            RedirectAttributes redirectAttributes) {
        log.info("Accedint al formulari de modificació d'usuari amb email: {}", email);
        Optional<User> user = userLogic.getUser(email);

        if (user.isEmpty()) {
            log.error("No s'ha trobat cap usuari amb el email: {}", email);
            redirectAttributes.addFlashAttribute("error", "No s'ha trobat el client");
            return "redirect:/user/list";
        }

        User existent = user.get();
        if (existent.getImage() != null) {
            String imatgeBase64 = Base64.getEncoder().encodeToString(existent.getImage());
            model.addAttribute("imatgeBase64", imatgeBase64);
            log.info("S'ha carregat la imatge de l'usuari amb email: {}", email);
        } else {
            model.addAttribute("imatgeBase64", null);
            log.info("No s'ha trobat cap imatge per a l'usuari amb email: {}", email);
        }

        model.addAttribute("user", existent);
        return "user-modify"; // Nom del fitxer HTML
    }

    /**
     * Modifica les dades d'un usuari existent identificat pel seu correu
     * electrònic.
     * 
     * @param user               L'objecte User amb les dades actualitzades de
     *                           l'usuari.
     * @param imageFile          El fitxer d'imatge carregat per l'usuari
     *                           (opcional).
     * @param model              L'objecte Model per passar dades a la vista.
     * @param redirectAttributes Els atributs per redirigir missatges a la vista.
     * @return Una cadena que indica la vista a mostrar o una redirecció.
     * @throws Exception Si es produeix un error durant el procés de modificació.
     * 
     *                   Aquest mètode permet modificar les dades d'un usuari
     *                   existent. Si es proporciona
     *                   una nova imatge, aquesta es guarda en l'objecte User. En
     *                   cas d'error, es retorna
     *                   a la vista de modificació amb els detalls de l'error i les
     *                   dades actuals de l'usuari.
     *                   Si la modificació és exitosa, es redirigeix a la llista
     *                   d'usuaris amb un missatge
     *                   d'èxit.
     */
    @PostMapping("/modify")
    public String modificarClient(@ModelAttribute("user") User user,
            @RequestParam("imageFile") MultipartFile imageFile, Model model, RedirectAttributes redirectAttributes)
            throws Exception {
        String email = user.getEmail();
        log.info("Inici del procés de modificació per al usuari amb email: {}", email);

        try {
            if (!imageFile.isEmpty()) {
                user.setImage(imageFile.getBytes());
            }
            userLogic.modifyUser(user);
            log.info("Usuari modificat correctament amb email: {}", user.getEmail());

        } catch (Exception e) {
            log.error("Error durant la modificació del client amb email: {}", user.getEmail(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);

            // Si hay una imagen seleccionada, mantenerla en el modelo
            if (user.getImage() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(user.getImage());
                model.addAttribute("imatgeBase64", imatgeBase64);
            }

            return "user-modify";
        }
        log.info("Procés de modificació finalitzat amb èxit per al usuari amb email: {}", user.getEmail());
        redirectAttributes.addFlashAttribute("success", "Usuari modificat correctament");
        return "redirect:/user/list";
    }
}
