package MarioOlaya.Projecte4.EntreBicis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MarioOlaya.Projecte4.EntreBicis.entity.SystemParameter;

/**
 * Repositori per a l'entitat SystemParameter.
 * Aquesta interfície extén JpaRepository per proporcionar operacions CRUD
 * i altres funcionalitats per a l'entitat SystemParameter.
 * 
 * @author Mario Olaya
 * @version 1.0
 */
@Repository
public interface SystemParameterRepo extends JpaRepository<SystemParameter, Long> {
    
}
