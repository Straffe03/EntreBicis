package MarioOlaya.Projecte4.EntreBicis.entity;

/**
 * DTO per retornar la resposta d'inici de sessió.
 * <p>
 * Aquesta classe encapsula la resposta que s'envia al client després d'una
 * verificació d'inici de sessió exitosa, habitualment incloent l'email de
 * l'usuari.
 * </p>
 * 
 * @author Mario
 */
public class LoginResponse {

    /** Correu electrònic de l'usuari autenticat */
    private String email;

    /**
     * Constructor amb paràmetre per inicialitzar l'email.
     *
     * @param email Correu electrònic de l'usuari
     */
    public LoginResponse(String email) {
        this.email = email;
    }

    /**
     * Retorna el correu electrònic de l'usuari.
     * 
     * @return Correu electrònic
     */
    public String getEmail() {
        return email;
    }

    /**
     * Estableix el correu electrònic de l'usuari.
     * 
     * @param email Nou correu electrònic
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
