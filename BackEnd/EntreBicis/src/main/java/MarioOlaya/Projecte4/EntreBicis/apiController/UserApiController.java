package MarioOlaya.Projecte4.EntreBicis.apiController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.logic.UserLogic;

/**
 * Controlador REST per gestionar els usuaris.
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserApiController {

    Logger log = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserLogic userLogic;

    /**
     * Obté un usuari pel seu ID.
     *
     * @param userId L'ID de l'usuari.
     * @return L'usuari amb l'ID especificat, o null si no es troba.
     */
    @GetMapping("/view/{userId}")
    public User getUserById(@PathVariable String userId) {
        log.info("S'està intentant recuperar l'usuari amb ID: {}", userId);
        Optional<User> user = userLogic.getUser(userId);
        if (user.isEmpty()) {
            log.warn("Usuari amb ID {} no trobat", userId);
            return null;
        }
        log.info("Usuari amb ID {} recuperat correctament", userId);
        return user.get();
    }

    /**
     * Actualitza un usuari existent.
     *
     * @param user L'usuari amb les dades actualitzades.
     * @return L'usuari actualitzat o un error si no s'ha pogut actualitzar.
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            log.info("S'està intentant modificar l'usuari amb ID: {}", user.getEmail());
            User updateduser = userLogic.modifyUser(user);
            log.info("Usuari amb ID {} modificat correctament", user.getEmail());
            return ResponseEntity.ok(updateduser);
        } catch (Exception e) {
            log.error("Error al modificar l'usuari amb ID {}: {}", user.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
