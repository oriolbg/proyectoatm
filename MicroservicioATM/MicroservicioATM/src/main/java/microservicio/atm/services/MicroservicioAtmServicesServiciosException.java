package microservicio.atm.services;

public class MicroservicioAtmServicesServiciosException extends Exception {

	private static final long serialVersionUID = 1402625357019595038L;

	public MicroservicioAtmServicesServiciosException(String msg) {
        super(msg);
    }

    public MicroservicioAtmServicesServiciosException(String msg, Throwable e) {
        super(msg, e);
    }

}

