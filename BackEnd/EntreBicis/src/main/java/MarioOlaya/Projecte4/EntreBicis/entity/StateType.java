/**
 * Representa els diferents estats possibles per a un objecte en el sistema.
 * 
 * <ul>
 *   <li><b>DISPONIBLE</b>: L'objecte està disponible per ser utilitzat.</li>
 *   <li><b>RESERVADA</b>: L'objecte ha estat reservat per un usuari.</li>
 *   <li><b>ASSIGNADA</b>: L'objecte ha estat assignat a un usuari específic.</li>
 *   <li><b>RECOLLIDA</b>: L'objecte ha estat recollit per l'usuari.</li>
 * </ul>
 */
package MarioOlaya.Projecte4.EntreBicis.entity;

public enum StateType {
    DISPONIBLE, RESERVADA, ASSIGNADA, RECOLLIDA
}
