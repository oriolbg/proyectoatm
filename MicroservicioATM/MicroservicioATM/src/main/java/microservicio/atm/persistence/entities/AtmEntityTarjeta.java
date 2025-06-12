package microservicio.atm.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AtmEntityTarjeta {

	@Id
    private String numero;
	
	@Column(length = 1)
    private String activa;// "S" o "N"

	public AtmEntityTarjeta() {
		
	}
	
    public AtmEntityTarjeta(String numero, String activa) {
        this.numero = numero;
        this.activa = activa;
    }

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getActiva() {
		return activa;
	}

	public void setActiva(String activa) {
		this.activa = activa;
	}
}
