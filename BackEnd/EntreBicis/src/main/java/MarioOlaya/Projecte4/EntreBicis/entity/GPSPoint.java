package MarioOlaya.Projecte4.EntreBicis.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un punt GPS dins del sistema.
 * Aquesta classe conté informació sobre la latitud, longitud, altitud i
 * data/hora associades a un punt GPS dins d'una ruta.
 * 
 * <p>
 * És una entitat que es mapeja a la taula "gps_point" de la base de dades.
 * </p>
 * 
 * @author Mario Olaya
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gps_point")
public class GPSPoint {

    /** Identificador únic del punt GPS */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Latitud geogràfica del punt */
    private double latitude;

    /** Longitud geogràfica del punt */
    private double longitude;

    /** Altitud del punt en metres */
    private double altitude;

    /** Marca temporal del moment en què es va registrar el punt */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private String timestamp;

    /** Ruta a la qual pertany aquest punt GPS */
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonBackReference("route-gps")
    private Route route;
}
