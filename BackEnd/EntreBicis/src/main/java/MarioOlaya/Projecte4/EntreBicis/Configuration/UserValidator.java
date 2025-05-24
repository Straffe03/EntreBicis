package MarioOlaya.Projecte4.EntreBicis.Configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import MarioOlaya.Projecte4.EntreBicis.entity.TypeUser;
import MarioOlaya.Projecte4.EntreBicis.repository.UserRepo;

/**
 * Classe per validar usuaris que intenten autenticar-se mitjançant Spring
 * Security.
 * Aquesta implementació només permet l'accés als usuaris amb rol ADMIN.
 */
@Service
public class UserValidator implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    @Autowired
    private UserRepo userRepo;

    /**
     * Carrega un usuari pel seu correu electrònic si és de tipus ADMIN.
     *
     * @param email correu electrònic de l'usuari.
     * @return Detalls de l'usuari per a l'autenticació.
     * @throws UsernameNotFoundException si l'usuari no existeix o no és
     *                                   administrador.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<MarioOlaya.Projecte4.EntreBicis.entity.User> user = userRepo.findById(email);

        if (user.isPresent() && user.get().getUserType() == TypeUser.ADMIN) {
            MarioOlaya.Projecte4.EntreBicis.entity.User userExistent = user.get();
            logger.info("Usuari administrador trobat: {}", email);
            return User.builder()
                    .username(userExistent.getEmail())
                    .password(userExistent.getPassword())
                    .roles(userExistent.getUserType().toString())
                    .build();
        }

        logger.warn("Intent fallit d'inici de sessió per: {} - No és un administrador", email);
        throw new UsernameNotFoundException("Usuari no trobat a la llista d'administradors: " + email);
    }
}
