package microservicio.atm.persistence.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class AtmEntityTarjetaCuentaId implements Serializable {

	private static final long serialVersionUID = -6601696974862382560L;
	private String tarjeta;
	private String cuenta;

    public AtmEntityTarjetaCuentaId() {}

    public AtmEntityTarjetaCuentaId(String tarjeta, String cuenta) {
        this.tarjeta = tarjeta;
        this.cuenta = cuenta;
    }

    public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtmEntityTarjetaCuentaId)) return false;
        AtmEntityTarjetaCuentaId that = (AtmEntityTarjetaCuentaId) o;
        return Objects.equals(tarjeta, that.tarjeta) &&
               Objects.equals(cuenta, that.cuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tarjeta, cuenta);
    }
}
