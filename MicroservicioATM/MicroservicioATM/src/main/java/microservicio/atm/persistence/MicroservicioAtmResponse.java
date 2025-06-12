package microservicio.atm.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MicroservicioAtmResponse {
	
	private String mensaje;
	private Integer codigoResultado;
	
	public MicroservicioAtmResponse() {
		mensaje = "";
		codigoResultado = 0;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Integer getCodigoResultado() {
		return codigoResultado;
	}

	public void setCodigoResultado(Integer codigoResultado) {
		this.codigoResultado = codigoResultado;
	}


}
