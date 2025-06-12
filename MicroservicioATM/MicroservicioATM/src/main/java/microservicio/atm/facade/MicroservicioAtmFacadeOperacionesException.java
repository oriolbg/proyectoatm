package microservicio.atm.facade;

/**
 * Se usará la excepcion para determinar errores de validacion de las operaciones solicitadas por usuario
 */
public class MicroservicioAtmFacadeOperacionesException extends Exception {

	private static final long serialVersionUID = -7003652211151519377L;

	public MicroservicioAtmFacadeOperacionesException(String msg) {
        super(msg);
    }

    public MicroservicioAtmFacadeOperacionesException(String msg, Throwable e) {
        super(msg, e);
    }

}