package microservicio.atm.persistence.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AtmEntityRegistroEvento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private Date fecha;
    private String proceso;
    private String usuario;
    private String operacion;
    
    @Column(nullable = true)
    private String datos;
    
    @Column(nullable = true)
    private String otrosDatos;

    public AtmEntityRegistroEvento() {}

    public AtmEntityRegistroEvento(Date fecha, String proceso, String usuario, String operacion, String datos, String otrosDatos) {
        this.fecha = fecha;
        this.proceso = proceso;
        this.usuario = usuario;
        this.operacion = operacion;
        this.datos = datos;
        this.otrosDatos = otrosDatos;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public String getOtrosDatos() {
		return otrosDatos;
	}

	public void setOtrosDatos(String otrosDatos) {
		this.otrosDatos = otrosDatos;
	}
}
