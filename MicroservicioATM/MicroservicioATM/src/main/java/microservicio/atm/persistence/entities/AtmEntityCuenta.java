package microservicio.atm.persistence.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AtmEntityCuenta {

	@Id
    private String numero;
    private String cbu;
    private BigDecimal saldo;

    public AtmEntityCuenta() {}

    public AtmEntityCuenta(String numero, String cbu, BigDecimal saldo) {
        this.numero = numero;
        this.cbu = cbu;
        this.saldo = saldo;
    }

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCbu() {
		return cbu;
	}

	public void setCbu(String cbu) {
		this.cbu = cbu;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
