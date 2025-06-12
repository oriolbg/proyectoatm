package microservicio.atm.services.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservicio.atm.persistence.entities.AtmEntityCuenta;

@Repository
public interface AtmRepositoryCuenta extends JpaRepository<AtmEntityCuenta, String> {
	

	Optional<AtmEntityCuenta> findByCbu(String cbu);

}

