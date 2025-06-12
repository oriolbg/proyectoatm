package microservicio.atm.services.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import microservicio.atm.persistence.entities.AtmEntityTarjetaCuenta;
import microservicio.atm.persistence.entities.AtmEntityTarjetaCuentaId;

@Repository
public interface AtmRepositoryTarjetaCuenta extends JpaRepository<AtmEntityTarjetaCuenta, AtmEntityTarjetaCuentaId> {
}
