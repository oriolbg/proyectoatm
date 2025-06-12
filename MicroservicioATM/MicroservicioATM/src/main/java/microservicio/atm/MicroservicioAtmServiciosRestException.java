package microservicio.atm;

public class MicroservicioAtmServiciosRestException extends Exception {

	private static final long serialVersionUID = -4588354159030884723L;

	public MicroservicioAtmServiciosRestException(String msg) {
        super(msg);
    }

    public MicroservicioAtmServiciosRestException(String msg, Throwable e) {
        super(msg, e);
    }

}
