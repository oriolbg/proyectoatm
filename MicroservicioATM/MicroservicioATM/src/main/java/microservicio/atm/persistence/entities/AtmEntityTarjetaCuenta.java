package microservicio.atm.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AtmEntityTarjetaCuenta {

	@Id
    private AtmEntityTarjetaCuentaId tarjetaCuentaId;

    public AtmEntityTarjetaCuenta() {}

    public AtmEntityTarjetaCuenta(AtmEntityTarjetaCuentaId tarjetaCuentaId) {
        this.tarjetaCuentaId = tarjetaCuentaId;
    }

	public AtmEntityTarjetaCuentaId getTarjetaCuentaId() {
		return tarjetaCuentaId;
	}

	public void setTarjetaCuentaId(AtmEntityTarjetaCuentaId tarjetaCuentaId) {
		this.tarjetaCuentaId = tarjetaCuentaId;
	}

}
