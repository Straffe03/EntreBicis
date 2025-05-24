package MarioOlaya.Projecte4.EntreBicis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MarioOlaya.Projecte4.EntreBicis.entity.User;

/**
 * Repositori per a l'entitat {@link User}.
 * Proporciona funcionalitats per recuperar usuaris per email o telèfon,
 * així com operacions CRUD heretades de {@link JpaRepository}.
 * 
 * @author Mario Olaya
 * @version 1.0
 */
@Repository
public interface UserRepo extends JpaRepository<User, String> {

    /**
     * Cerca un usuari pel seu número de telèfon.
     * 
     * @param phone Número de telèfon
     * @return {@link Optional} que conté l'usuari si es troba
     */
    Optional<User> findByPhone(String phone);
}
