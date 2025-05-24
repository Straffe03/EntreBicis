package MarioOlaya.Projecte4.EntreBicis.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa un usuari dins del sistema.
 * Conté informació personal i dades relacionades amb l'usuari.
 * 
 * Atributs:
 * - email: Correu electrònic de l'usuari. És l'identificador únic.
 * - password: Contrasenya de l'usuari.
 * - name: Nom de l'usuari.
 * - surname: Cognom de l'usuari.
 * - city: Ciutat de residència de l'usuari.
 * - points: Punts acumulats per l'usuari.
 * - signupDate: Data de registre de l'usuari al sistema.
 * - phone: Telèfon de l'usuari. Ha de ser únic.
 * - image: Imatge de l'usuari en format byte array.
 * - userType: Tipus d'usuari dins del sistema.
 * 
 * @author Mario Olaya
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    private String email;

    private String password;

    private String name;

    private String surname;

    private String city;

    private double points;

    @Column(name = "signup_date")
    private LocalDate signupDate;

    @Column(unique = true)
    private String phone;

    private String observations;

    @Lob
    private byte[] image;

    @Column(name = "user_type")
    private TypeUser userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-reward")
    private List<Reward> rewards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Route> rutes;
}
