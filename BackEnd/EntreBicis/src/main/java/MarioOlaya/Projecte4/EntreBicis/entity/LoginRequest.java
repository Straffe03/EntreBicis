package MarioOlaya.Projecte4.EntreBicis.entity;

/**
 * DTO per gestionar la petició d'inici de sessió.
 * <p>
 * Aquesta classe encapsula les credencials d'accés necessàries per a iniciar
 * sessió:
 * l'email i la contrasenya de l'usuari.
 * </p>
 * 
 * @author Mario
 */
public class LoginRequest {

    /** Correu electrònic de l'usuari */
    private String email;

    /** Contrasenya de l'usuari */
    private String password;

    /**
     * Constructor buit requerit per a la deserialització.
     */
    public LoginRequest() {
    }

    /**
     * Constructor amb paràmetres.
     *
     * @param email    Correu electrònic de l'usuari
     * @param password Contrasenya de l'usuari
     */
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Retorna el correu electrònic.
     * 
     * @return Correu electrònic de l'usuari
     */
    public String getEmail() {
        return email;
    }

    /**
     * Estableix el correu electrònic.
     * 
     * @param email Nou correu electrònic
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna la contrasenya.
     * 
     * @return Contrasenya de l'usuari
     */
    public String getPassword() {
        return password;
    }

    /**
     * Estableix la contrasenya.
     * 
     * @param password Nova contrasenya
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
