package microservicio.atm.persistence.entities;

public class AtmEntityException extends Exception {

	private static final long serialVersionUID = 1402625357019595038L;

	public AtmEntityException(String msg) {
        super(msg);
    }

    public AtmEntityException(String msg, Throwable e) {
        super(msg, e);
    }

}

