package MarioOlaya.Projecte4.EntreBicis.apiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;
import MarioOlaya.Projecte4.EntreBicis.logic.SystemParameterLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador REST per gestionar els paràmetres de sistema.
 */
@RestController
@RequestMapping("/api/parameters")
@CrossOrigin(origins = "*")
public class SystemParameterApiController {

    @Autowired
    private SystemParameterLogic systemParameterLogic;

    private static final Logger logger = LoggerFactory.getLogger(SystemParameterApiController.class);

    /**
     * Obté els paràmetres del sistema.
     *
     * @return un objecte {@link SystemParameter} que conté els paràmetres del
     *         sistema.
     */
    @GetMapping("/get")
    public SystemParameter getParameters() {
        logger.info("S'estan recuperant els paràmetres de sistema");
        return systemParameterLogic.obtenerParametroPorId();
    }
}
