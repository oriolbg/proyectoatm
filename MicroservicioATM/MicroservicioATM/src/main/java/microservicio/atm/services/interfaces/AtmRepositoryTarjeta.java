package microservicio.atm.services.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservicio.atm.persistence.entities.AtmEntityTarjeta;

@Repository
public interface AtmRepositoryTarjeta extends JpaRepository<AtmEntityTarjeta, String> {

}

