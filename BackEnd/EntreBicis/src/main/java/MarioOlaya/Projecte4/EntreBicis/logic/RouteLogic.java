package MarioOlaya.Projecte4.EntreBicis.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import MarioOlaya.Projecte4.EntreBicis.entity.GPSPoint;
import MarioOlaya.Projecte4.EntreBicis.entity.Route;
import MarioOlaya.Projecte4.EntreBicis.repository.GPSPointRepo;
import MarioOlaya.Projecte4.EntreBicis.repository.RouteRepo;

/**
 * Classe de lògica per gestionar les operacions relacionades amb les rutes.
 */
@Service
public class RouteLogic {

    private static final Logger logger = LoggerFactory.getLogger(RouteLogic.class);

    @Autowired
    RouteRepo routeRepo;

    @Autowired
    GPSPointRepo GPSPointRepo;

    /**
     * Crea una nova ruta establint la data actual i l'emmagatzema a la base de
     * dades.
     *
     * @param ruta Ruta a crear
     * @return Ruta creada
     */
    public Route crearRuta(Route ruta) {
        ruta.setDate(LocalDateTime.now().toLocalDate());
        logger.info("Creant ruta: " + ruta);
        return routeRepo.save(ruta);
    }

    /**
     * Actualitza una ruta existent.
     *
     * @param ruta Ruta amb informació nova
     * @return Ruta actualitzada
     */
    public Route updateRuta(Route ruta) {
        logger.info("Actualitzant ruta: " + ruta);
        return routeRepo.save(ruta);
    }

    /**
     * Retorna totes les rutes existents.
     * 
     * @return llista de rutes
     */
    public List<Route> getAllRoutes() {
        return routeRepo.findAll();
    }

    /**
     * Desa una llista de punts GPS en una ruta.
     * 
     * @param points Llista de punts GPS.
     * @return Ruta actualitzada amb els punts GPS, o null si hi ha error.
     */
    public Route saveGpsPoints(List<GPSPoint> points) {
        logger.info("Desant punts GPS: " + points);
        try {
            for (GPSPoint gpsPoint : points) {
                GPSPointRepo.save(gpsPoint);
            }
        } catch (Exception e) {
            logger.error("Error al desar els punts GPS: " + e.getMessage());
            return null;
        }
        logger.info("Punts GPS desats correctament.");
        return routeRepo.findById(points.get(0).getRoute().getId()).orElse(null);
    }

    /**
     * Retorna totes les rutes associades a un usuari.
     * 
     * @param userId ID de l'usuari
     * @return Llista de rutes
     */
    public List<Route> findByUserEmail(String userId) {
        logger.info("Recuperant rutes per a l'usuari amb ID: {}", userId);
        return routeRepo.findByUserEmail(userId);
    }

    /**
     * Recupera una ruta pel seu ID.
     *
     * @param id ID de la ruta
     * @return Ruta trobada
     * @throws RuntimeException si no es troba la ruta
     */
    public Route getRouteById(Long id) {
        logger.info("Recuperant ruta amb ID: " + id);
        try {
            return routeRepo.findById(id).orElseThrow(() -> new RuntimeException("Ruta no trobada amb ID: " + id));
        } catch (RuntimeException e) {
            logger.error("Error al recuperar la ruta: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Marca una ruta com a validada i actualitza els punts de l'usuari.
     * 
     * @param id ID de la ruta a validar
     */
    public void validarRuta(Long id) {
        try {
            Route route = getRouteById(id);
            if (!route.isValidation()) {
                route.setValidation(true);
                route.getUser().setPoints(route.getUser().getPoints() + route.getPoints());
                logger.info("Ruta amb ID {} ha estat validada.", id);
                logger.info("Punts de l'usuari {} actualitzats a {}.", route.getUser().getEmail(),
                        route.getUser().getPoints());
                routeRepo.save(route);
            } else {
                logger.info("Ruta amb ID {} ja estava validada.", id);
            }
        } catch (Exception e) {
            logger.error("Error al validar la ruta: " + e.getMessage());
            throw new RuntimeException("Error al validar la ruta: " + e.getMessage());
        }
    }

    /**
     * Marca una ruta com a invalidada i resta punts a l'usuari si correspon.
     * 
     * @param id ID de la ruta a invalidar
     */
    public void invalidarRuta(Long id) {
        try {
            Route route = getRouteById(id);
            if (route.isValidation()) {
                if (route.getUser().getPoints() < route.getPoints()) {
                    throw new RuntimeException("No es pot invalidar la ruta, l'usuari no té prou punts.");
                } else {
                    route.setValidation(false);
                    route.getUser().setPoints(route.getUser().getPoints() - route.getPoints());
                    logger.info("Ruta amb ID {} ha estat invalidada.", id);
                    logger.info("Punts de l'usuari {} actualitzats a {}.", route.getUser().getEmail(),
                            route.getUser().getPoints());
                    routeRepo.save(route);
                }
            } else {
                logger.info("Ruta amb ID {} ja estava invalidada.", id);
            }
        } catch (Exception e) {
            logger.error("Error al invalidar la ruta: " + e.getMessage());
            throw new RuntimeException("Error al invalidar la ruta: " + e.getMessage());
        }
    }

}
