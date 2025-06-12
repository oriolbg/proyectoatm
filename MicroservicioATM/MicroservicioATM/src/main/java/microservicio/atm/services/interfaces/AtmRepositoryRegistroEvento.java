package microservicio.atm.services.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservicio.atm.persistence.entities.AtmEntityRegistroEvento;

@Repository
public interface AtmRepositoryRegistroEvento extends JpaRepository<AtmEntityRegistroEvento, Long> {
}

