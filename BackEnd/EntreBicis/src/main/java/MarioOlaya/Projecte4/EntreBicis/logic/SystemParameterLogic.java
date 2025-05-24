package MarioOlaya.Projecte4.EntreBicis.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;
import MarioOlaya.Projecte4.EntreBicis.repository.SystemParameterRepo;

/**
 * Classe de lògica per gestionar els paràmetres del sistema.
 * Proporciona funcionalitats per actualitzar i obtenir els paràmetres del
 * sistema.
 * 
 * Aquesta classe utilitza un repositori per accedir i modificar els paràmetres
 * emmagatzemats a la base de dades.
 * 
 * @author Mario Olaya
 */
@Service
public class SystemParameterLogic {
    private static final Logger log = LoggerFactory.getLogger(SystemParameterLogic.class);

    @Autowired
    private SystemParameterRepo systemParameterRepo;

    /**
     * Actualitza els paràmetres del sistema amb els valors proporcionats.
     * 
     * @param systemParameter L'objecte SystemParameter que conté els nous valors
     *                         per actualitzar els paràmetres del sistema.
     *                         S'assegura que l'ID sigui 1, ja que només hi ha un registre.
     * @return L'objecte SystemParameter actualitzat després de ser desat al repositori.
     */
    public SystemParameter actualizarParametros(SystemParameter systemParameter) {
        log.info("Actualitzant els paràmetres del sistema: {}", systemParameter);
        systemParameter.setId(1L); // Assegurem-nos que l'ID sigui 1, ja que només hi ha un registre
        return systemParameterRepo.save(systemParameter);
        
    }

    /**
     * Obté el paràmetre del sistema amb un ID fix.
     * 
     * Aquest mètode cerca un paràmetre del sistema a la base de dades utilitzant
     * un ID fix (1L). Si no es troba cap paràmetre amb aquest ID, retorna null.
     * 
     * @return El paràmetre del sistema amb ID 1L, o null si no existeix.
     */
    public SystemParameter obtenerParametroPorId() {
        log.info("Obtenint el paràmetre del sistema amb ID: {}", 1L);
        return systemParameterRepo.findById(1L).orElse(null); // Retorna null si no es troba
    }
    
}
