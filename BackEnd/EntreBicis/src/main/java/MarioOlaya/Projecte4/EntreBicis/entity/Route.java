package MarioOlaya.Projecte4.EntreBicis.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una ruta dins del sistema.
 * Aquesta classe conté informació sobre la ruta, incloent-hi el seu estat,
 * estat de validació, distància, velocitat, punts i punts GPS associats.
 * 
 * <p>
 * És una entitat que es mapatge a la taula "route" de la base de dades.
 * </p>
 * 
 * @author Mario Olaya
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route")
public class Route {

    /** Identificador únic de la ruta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Estat actual de la ruta (CREADA, VALIDADA, REBUTJADA, etc.) */
    private RouteState state;

    /** Indica si la ruta ha estat validada per l’administrador */
    private boolean validation;

    /** Data en què s’ha realitzat la ruta */
    private LocalDate date;

    /** Distància total recorreguda en la ruta, expressada en metres */
    private double distance;

    /** Velocitat mitjana durant la ruta */
    private double velocity;

    /** Velocitat màxima assolida durant la ruta */
    private double maxVelocity;

    /** Temps total de la ruta, expressat en minuts */
    private double time;

    /** Punts obtinguts per completar la ruta */
    private double points;

    /** Llista de punts GPS associats a la ruta */
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("route-gps")
    private List<GPSPoint> gpsPoints;

    /** Usuari al qual pertany la ruta */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
