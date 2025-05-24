package MarioOlaya.Projecte4.EntreBicis.apiController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MarioOlaya.Projecte4.EntreBicis.entity.GPSPoint;
import MarioOlaya.Projecte4.EntreBicis.entity.Route;
import MarioOlaya.Projecte4.EntreBicis.logic.RouteLogic;

/**
 * Controlador REST per gestionar les rutes.
 */
@RestController
@RequestMapping("/api/route")
@CrossOrigin(origins = "*")
public class RouteApiController {

    private static final Logger logger = LoggerFactory.getLogger(RouteApiController.class);

    @Autowired
    private RouteLogic routeLogic;

    /**
     * Endpoint per crear una nova ruta.
     *
     * @param ruta Objecte de tipus Ruta rebut pel body de la petició.
     * @return La ruta guardada.
     */
    @PostMapping("/create")
    public ResponseEntity<?> crearRuta(@RequestBody Route ruta) {
        try {
            logger.info("Creant una nova ruta per a l'usuari: {}", ruta.getUser().getEmail());
            Route novaRuta = routeLogic.crearRuta(ruta);
            return ResponseEntity.ok(novaRuta);
        } catch (Exception e) {
            logger.error("Error al crear la ruta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la ruta: " + e.getMessage());
        }
    }

    /**
     * Actualitza una ruta existent.
     *
     * @param ruta Ruta amb les dades noves.
     * @return Ruta actualitzada.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRuta(@RequestBody Route ruta) {
        try {
            logger.info("Actualitzant la ruta amb ID: {}", ruta.getId());
            Route actualitzada = routeLogic.updateRuta(ruta);
            return ResponseEntity.ok(actualitzada);
        } catch (Exception e) {
            logger.error("Error al actualitzar la ruta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualitzar la ruta: " + e.getMessage());
        }
    }

    /**
     * Afegeix una llista de punts GPS a una ruta específica.
     *
     * @param points Llista de punts GPS.
     * @return ResponseEntity amb el resultat de l'operació.
     */
    @PostMapping("/gpspoints")
    public ResponseEntity<?> addGpsPoints(@RequestBody List<GPSPoint> points) {
        try {
            logger.info("Afegint {} punts GPS a una ruta", points.size());
            Route ruta = routeLogic.saveGpsPoints(points);
            return ResponseEntity.ok(ruta);
        } catch (Exception e) {
            logger.error("Error al guardar punts GPS: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar puntos: " + e.getMessage());
        }
    }

    /**
     * Retorna totes les rutes associades a un usuari específic.
     *
     * @param userId ID de l'usuari.
     * @return Llista de rutes de l'usuari.
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getRoutesByUser(@PathVariable String userId) {
        try {
            logger.info("S'estan recuperant les rutes per a l'usuari amb ID: {}", userId);
            return ResponseEntity.ok(routeLogic.findByUserEmail(userId));
        } catch (Exception e) {
            logger.error("Error al recuperar les rutes per a l'usuari amb ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar les rutes: " + e.getMessage());
        }
    }

    /**
     * Retorna una ruta pel seu ID amb punts GPS inclosos.
     *
     * @param id ID de la ruta.
     * @return Ruta amb informació detallada.
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        logger.info("S'està recuperant la ruta amb ID: {}", id);
        return ResponseEntity.ok(routeLogic.getRouteById(id));
    }
}
