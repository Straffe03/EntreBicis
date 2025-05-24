package MarioOlaya.Projecte4.EntreBicis.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MarioOlaya.Projecte4.EntreBicis.entity.LoginRequest;
import MarioOlaya.Projecte4.EntreBicis.entity.LoginResponse;
import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador API per a la verificació d'inici de sessió.
 */
@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginApiController {

    @Autowired
    private UserRepo userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);

    /**
     * Verifica les credencials de l'usuari.
     *
     * @param loginRequest Les credencials d'inici de sessió.
     * @return ResponseEntity amb l'estat de la verificació.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Intentant verificar usuari amb email: {}", loginRequest.getEmail());

        Optional<User> optionalUser = userRepository.findById(loginRequest.getEmail());

        if (!optionalUser.isPresent()) {
            logger.warn("Usuari no trobat per l'email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuari no trobat");
        }

        User user = optionalUser.get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Contrasenya incorrecta per l'usuari: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contrasenya incorrecta");
        }

        logger.info("Verificació d'usuari correcta per l'email: {}", loginRequest.getEmail());
        LoginResponse response = new LoginResponse(user.getEmail());

        return ResponseEntity.ok(response);
    }
}
