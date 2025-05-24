package MarioOlaya.Projecte4.EntreBicis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;
import MarioOlaya.Projecte4.EntreBicis.entity.TypeUser;
import MarioOlaya.Projecte4.EntreBicis.entity.User;
import MarioOlaya.Projecte4.EntreBicis.logic.SystemParameterLogic;
import MarioOlaya.Projecte4.EntreBicis.logic.UserLogic;

/**
 * Classe principal per a l'aplicació EntreBicis.
 * 
 * Aquesta classe inicia l'aplicació Spring Boot, afegeix usuaris d'exemple i
 * configura els paràmetres del sistema per defecte si no existeixen.
 * 
 * Les operacions d'inicialització inclouen:
 * <ul>
 * <li>Inici de l'aplicació Spring Boot.</li>
 * <li>Creació d'un usuari administrador per defecte.</li>
 * <li>Creació d'un usuari ciclista per defecte.</li>
 * <li>Configuració dels paràmetres del sistema per defecte si no
 * existeixen.</li>
 * </ul>
 * 
 * Aquesta inicialització és útil en entorns de desenvolupament o per a la
 * primera execució del sistema.
 * 
 * @author Mario
 */
@SpringBootApplication
public class EntreBicisApplication {

	@Autowired
	private static UserLogic userLogic;

	@Autowired
	private static SystemParameterLogic systemParameterLogic;

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(EntreBicisApplication.class);

		log.info("Iniciant aplicació EntreBicis...");

		var context = SpringApplication.run(EntreBicisApplication.class, args);
		userLogic = context.getBean(UserLogic.class);
		systemParameterLogic = context.getBean(SystemParameterLogic.class);

		log.info("Afegint usuaris d'exemple...");

		try {
			User user = new User(
					"admin@gmail.com", "admin", "adminName", "adminSurname", "Barcelona", 2.1,
					null, "123456789", "Es un admin", null, TypeUser.ADMIN, null, null);

			if (userLogic.getUser(user.getEmail()).isEmpty()) {
				userLogic.createUser(user);
				log.info("Usuari afegit: {}", user);
			}

		} catch (Exception e) {
			log.error("Error afegint client: {}", e.getMessage());
		}

		try {
			User user = new User(
					"user@gmail.com", "user", "userName", "userSurname", "Barcelona", 0,
					null, "987654321", "No es un admin", null, TypeUser.CICLISTA, null, null);

			if (userLogic.getUser(user.getEmail()).isEmpty()) {
				userLogic.createUser(user);
				log.info("Usuari afegit: {}", user);
			}

		} catch (Exception e) {
			log.error("Error afegint client: {}", e.getMessage());
		}

		try {
			if (systemParameterLogic.obtenerParametroPorId() != null) {
				log.info("Paràmetres del sistema ja existents, no s'afegeixen per defecte.");
			} else {
				SystemParameter systemParameter = new SystemParameter(1L, 60, 5, 1, 72);
				systemParameterLogic.actualizarParametros(systemParameter);
				log.info("Paràmetres del sistema per defecte afegits: {}", systemParameter);
			}
		} catch (Exception e) {
			log.error("Error afegint paràmetres del sistema: {}", e.getMessage());
		}
	}
}
