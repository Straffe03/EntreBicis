package MarioOlaya.Projecte4.EntreBicis.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una recompensa dins del sistema.
 * Aquesta classe conté informació sobre la recompensa, incloent-hi la seva
 * imatge,
 * nom, botiga, punts requerits i estat actual.
 * 
 * <p>
 * És una entitat que es mapeja a la taula "reward" de la base de dades.
 * </p>
 * 
 * @author Mario Olaya
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reward")
public class Reward {

    /** Identificador únic de la recompensa */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Imatge de la recompensa, emmagatzemada com a array de bytes */
    @Lob
    private byte[] image;

    /** Nom de la recompensa */
    private String name;

    /** Nom de la botiga que ofereix la recompensa */
    @Column(name = "store_name")
    private String storeName;

    /** Observacions addicionals sobre la recompensa */
    private String observations;

    /** Descripció detallada de la recompensa */
    private String description;

    /** Punts necessaris per obtenir la recompensa */
    private Double points;

    /** Adreça física on recollir o fer ús de la recompensa */
    private String adress;

    /** Estat actual de la recompensa */
    private StateType state;

    /** Data de creació de la recompensa */
    private LocalDate date;

    /** Data en què s'ha reservat la recompensa */
    private LocalDate reservationDate;

    /** Data d'assignació de la recompensa a un usuari */
    private LocalDate assignationDate;

    /** Data de recollida de la recompensa per part de l'usuari */
    private LocalDate pickupDate;

    /** Usuari associat a la recompensa, si n'hi ha */
    @ManyToOne
    @JoinColumn(name = "user", nullable = true)
    @JsonBackReference("user-reward")
    private User user;

}
