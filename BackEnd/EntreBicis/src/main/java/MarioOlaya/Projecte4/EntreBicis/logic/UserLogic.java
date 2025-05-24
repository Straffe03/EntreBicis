package MarioOlaya.Projecte4.EntreBicis.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import MarioOlaya.Projecte4.EntreBicis.entity.TypeUser;
import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.repository.UserRepo;

/**
 * Classe de lògica per gestionar les operacions relacionades amb els usuaris.
 * Aquesta classe proporciona funcionalitats per crear usuaris i obtenir
 * informació
 * d'usuaris existents.
 * 
 * @author Mario Olaya
 */
@Service
public class UserLogic {

    private static final Logger logger = LoggerFactory.getLogger(UserLogic.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crea un nou usuari a la base de dades.
     * Abans de crear l'usuari, es realitza una validació per garantir que les
     * dades siguin correctes. Si alguna dada no és vàlida, es llença una excepció.
     * 
     * Validacions inclouen:
     * - L'email no pot ser nul o buit.
     * - La població no pot ser nul·la o buida.
     * - El nom no pot ser nul o buit.
     * - La contrasenya no pot ser nul·la o buida.
     * - El telèfon ha de ser numèric, no pot ser nul o buit, i ha de tenir 9
     * dígits.
     * - El cognom no pot ser nul o buit.
     * 
     * Si el tipus d'usuari no està especificat, es defineix com a USER per defecte.
     * També s'inicialitzen els punts de l'usuari a 0 i es registra la data d'alta.
     * La contrasenya es guarda encriptada.
     * 
     * @param user L'objecte User amb les dades de l'usuari a crear.
     * @return L'usuari creat i emmagatzemat a la base de dades.
     * @throws Exception Si alguna de les dades proporcionades no és vàlida.
     */
    public User createUser(User user) throws Exception {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.error("L'email no pot ser nul o buit.");
            throw new Exception("L'email no pot ser nul o buit.");
        }
        if (user.getCity() == null || user.getCity().isEmpty()) {
            logger.error("La població no pot ser nul·la o buida.");
            throw new Exception("La població no pot ser nul·la o buida.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            logger.error("El nom no pot ser nul o buit.");
            throw new Exception("El nom no pot ser nul o buit.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.error("La contrasenya no pot ser nul·la o buida.");
            throw new Exception("La contrasenya no pot ser nul·la o buida.");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty() || !user.getPhone().matches("\\d{9}")) {
            logger.error("El telèfon ha de ser numèric, no pot ser nul o buit, i ha de tenir 9 dígits.");
            throw new Exception("El telèfon ha de ser numèric, no pot ser nul o buit, i ha de tenir 9 dígits.");
        }
        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            logger.error("El cognom no pot ser nul o buit.");
            throw new Exception("El cognom no pot ser nul o buit.");
        }

        if (user.getUserType() == null) {
            user.setUserType(TypeUser.CICLISTA);
        }

        if (userRepo.findById(user.getEmail()).isPresent()) {
            logger.error("Ja existeix un usuari amb aquest email.");
            throw new Exception("Ja existeix un usuari amb aquest email.");
        }

        if (userRepo.findByPhone(user.getPhone()).isPresent()) {
            logger.error("Ja existeix un usuari amb aquest telèfon.");
            throw new Exception("Ja existeix un usuari amb aquest telèfon.");
        }

        user.setPoints(0);
        user.setSignupDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        logger.info("Creant usuari amb email: {}", user.getEmail());
        return userRepo.save(user);
    }

    /**
     * Obté un usuari de la base de dades a partir del seu email.
     * 
     * @param email L'email de l'usuari que es vol obtenir.
     * @return Un Optional que conté l'usuari si existeix, o buit si no es troba.
     */
    public Optional<User> getUser(String email) {
        logger.info("Buscant usuari amb email: {}", email);
        return userRepo.findById(email);
    }

    /**
     * Recupera la llista completa d'usuaris.
     * 
     * @return Una llista amb tots els usuaris disponibles.
     */
    public List<User> listAll() {
        logger.info("Recuperant la llista completa d'usuaris");
        return userRepo.findAll();
    }

    /**
     * Modifica un usuari existent amb les dades proporcionades. Si algun camp no es proporciona,
     * es mantindrà el valor existent de l'usuari.
     *
     * @param user L'usuari amb les dades actualitzades.
     * @return L'usuari modificat després de ser desat al repositori.
     * @throws Exception Si no existeix cap usuari amb l'email proporcionat, si ja existeix un usuari
     *                   amb el mateix telèfon però un email diferent, o si el tipus d'usuari és nul.
     */
    public User modifyUser(User user) throws Exception {

        Optional<User> existing = userRepo.findById(user.getEmail());
        if (!existing.isPresent()) {
            logger.error("No existeix cap usuari amb aquest email.");
            throw new Exception("No existeix cap usuari amb aquest email.");
        }

        Optional<User> existingUserWithPhone = userRepo.findByPhone(user.getPhone());
        if (existingUserWithPhone.isPresent() && !existingUserWithPhone.get().getEmail().equals(user.getEmail())) {
            logger.error("Ja existeix un usuari amb aquest telèfon.");
            throw new Exception("Ja existeix un usuari amb aquest telèfon.");
        }

        if (user.getUserType() == null) {
            logger.error("El tipus d'usuari no pot ser nul.");
            throw new Exception("El tipus d'usuari no pot ser nul.");
        }

        User oldUser = existing.get();

        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            logger.info("No s'ha proporcionat una nova contrasenya. Es mantindrà l'antiga.");
            user.setPassword(oldUser.getPassword());
        }

        if (user.getImage() == null) {
            user.setImage(oldUser.getImage());
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(oldUser.getName());
        }
        if (user.getSurname() == null || user.getSurname().isEmpty() || user.getSurname().isBlank()) {
            user.setSurname(oldUser.getSurname());
        }
        if (user.getCity() == null || user.getCity().isEmpty() || user.getCity().isBlank()) {
            user.setCity(oldUser.getCity());
        }
        if (user.getPhone() == null || user.getPhone().isEmpty() || user.getPhone().isBlank()) {
            user.setPhone(oldUser.getPhone());
        }
        String phoneRegex = "^[0-9]{9}$";
        if (user.getPhone() != null && !user.getPhone().matches(phoneRegex)) {
            logger.error("El telèfon ha de tenir 9 digits numerics.");
            throw new Exception("El telèfon ha de tenir 9 digits numerics.");
        }

        if (user.getSignupDate() == null) {
            user.setSignupDate(oldUser.getSignupDate());
        }
        user.setPoints(oldUser.getPoints());

        logger.info("Modificant usuari amb email: {}", user.getEmail());
        return userRepo.save(user);
    }


    /**
     * Actualitza la contrasenya d'un usuari pel seu correu electrònic.
     * 
     * @param email       Correu electrònic de l'usuari
     * @param newPassword Nova contrasenya (en pla o ja xifrada)
     */
    public void updatePassword(String email, String newPassword) {
        logger.info("Actualitzant contrasenya per a l'usuari amb email: {}", email);

        User user = userRepo.findById(email)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat amb el correu: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepo.save(user);
    }
}
