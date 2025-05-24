package MarioOlaya.Projecte4.EntreBicis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa els paràmetres del sistema utilitzats per configurar diferents aspectes
 * del funcionament de l'aplicació.
 * 
 * Aquesta entitat inclou informació sobre la velocitat màxima, el temps màxim
 * d'aturada, la conversió de quilòmetres a punts i el temps de caducitat de les recompenses.
 * 
 * @author Mario Olaya
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_parameter")
public class SystemParameter {
    
    @Id
    private Long id;
    
    //velocitat maxima en km/h
    private int maxVelocity;

    //temps maxim de aturada en minuts
    private int maxStopTime;

    //conversio entre km y punts en punts per km
    private int pointsPerKm;

    //tempx de caducitat de la recompensa en hores
    private int rewardExpirationTime;

}
