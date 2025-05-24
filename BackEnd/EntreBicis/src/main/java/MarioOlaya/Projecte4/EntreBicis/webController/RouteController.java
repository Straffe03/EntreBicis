package MarioOlaya.Projecte4.EntreBicis.webController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import MarioOlaya.Projecte4.EntreBicis.entity.Route;
import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;
import MarioOlaya.Projecte4.EntreBicis.logic.RouteLogic;
import MarioOlaya.Projecte4.EntreBicis.logic.SystemParameterLogic;

/**
 * Controlador per gestionar la visualització de les rutes.
 */
@Controller
@RequestMapping("/route")
@Scope("request")
public class RouteController {

    @Autowired
    private RouteLogic routeLogic;
    @Autowired
    private SystemParameterLogic systemParameterLogic;

    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);

    /**
     * Mètode que gestiona la petició per mostrar el llistat de totes les rutes.
     *
     * @param model Objecte Model de Spring utilitzat per passar dades a la vista.
     * @return Nom de la plantilla Thymeleaf per mostrar el llistat de rutes.
     */
    @GetMapping("/list")
    public String listRoutes(Model model) {
        List<Route> routes = routeLogic.getAllRoutes();
        model.addAttribute("routes", routes);
        return "route-list"; // nombre de la plantilla HTML (sin .html)
    }

    /**
     * Mètode que gestiona la petició per mostrar el llistat de totes les rutes d'un
     * usuari.
     *
     * @param model Objecte Model de Spring utilitzat per passar dades a la vista.
     * @return Nom de la plantilla Thymeleaf per mostrar el llistat de rutes.
     */
    @GetMapping("/list/{email}")
    public String listRoutesbyUser(@PathVariable String email, Model model) {
        List<Route> routes = routeLogic.findByUserEmail(email);
        model.addAttribute("routes", routes);
        return "route-list"; // nombre de la plantilla HTML (sin .html)
    }

    /**
     * Mostra la vista detallada d'una ruta amb el seu mapa.
     * 
     * @param id    ID de la ruta
     * @param model Model de dades per a la vista
     * @return Vista amb el mapa i informació de la ruta
     */
    @GetMapping("/view/{id}")
    public String viewRoute(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Route route = routeLogic.getRouteById(id); // ha d'incloure gpsPoints
            model.addAttribute("route", route);
            SystemParameter systemParameter = systemParameterLogic.obtenerParametroPorId();
            model.addAttribute("parametres", systemParameter);
            return "route-view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ruta no trobada.");
            return "redirect:/route/list"; // redirigeix a la llista de rutes
        }
    }

    /**
     * Valida una ruta concretant el seu ID.
     * Marca la ruta com a validada (validation = true).
     * 
     * @param id ID de la ruta a validar
     * @return Redirecció a la vista detallada de la ruta
     */
    @GetMapping("/validate/{id}")
    public String validarRuta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            routeLogic.validarRuta(id);
            logger.info("Ruta validada amb èxit: " + id);
            redirectAttributes.addFlashAttribute("success", "Ruta validada amb èxit.");
            return "redirect:/route/view/" + id;
        } catch (Exception e) {
            logger.error("Error al validar la ruta: " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al validar la ruta.");
            return "redirect:/route/view/" + id; // redirigeix a la llista de rutes en cas d'error
        }
    }

    /**
     * Invalida una ruta concretant el seu ID.
     * Marca la ruta com a no validada (validation = false).
     * 
     * @param id ID de la ruta a invalidar
     * @return Redirecció a la vista detallada de la ruta
     */
    @GetMapping("/invalidate/{id}")
    public String invalidarRuta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            routeLogic.invalidarRuta(id);
            logger.info("Ruta invalidada amb èxit: " + id);
            redirectAttributes.addFlashAttribute("success", "Ruta invalidada amb èxit.");
            return "redirect:/route/view/" + id;
        } catch (Exception e) {
            logger.error("Error al invalidada la ruta: " + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al invalidada la ruta.");
            return "redirect:/route/view/" + id; // redirigeix a la llista de rutes en cas d'error
        }
    }
}
