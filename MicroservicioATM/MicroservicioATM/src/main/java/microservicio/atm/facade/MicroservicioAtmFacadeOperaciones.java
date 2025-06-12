package microservicio.atm.facade;

import java.text.ParseException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import microservicio.atm.persistence.MicroservicioAtmRequest;
import microservicio.atm.persistence.MicroservicioAtmResponse;
import microservicio.atm.services.MicroservicioAtmServicesServicios;
import microservicio.atm.services.MicroservicioAtmServicesServiciosException;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;

/**
 * Servicio para el tratamiento y validacion de las operaciones seleccionadas por el usuario.
 */
@Service
public class MicroservicioAtmFacadeOperaciones {

	protected static Logger log = Logger.getLogger(MicroservicioAtmFacadeOperaciones.class);
	
	@Autowired
	private MicroservicioAtmServicesServicios serviciosAtm;
	
	
    public void inicializaFacadeOperaciones() {
    	if(serviciosAtm==null) {
    		serviciosAtm = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(MicroservicioAtmServicesServicios.class);
    	}
    }
	
	/**
	 * Proceso de validacion de la operacion seleccionada. Comprueba que la operacion exista y que el formato de entrada (campos), sean los necesarios
	 */
	public String[] validaOperacionSeleccionada(String operacion) throws MicroservicioAtmFacadeOperacionesException {	
		String[] detallesOperacion = operacion.split(" ");

		if(detallesOperacion.length>1) {
			String operacionPosible = detallesOperacion[0].toUpperCase();
			if(MicroservicioAtmUtilsConstantes.operacionesDisponibles.contains(operacionPosible)) {
				detallesOperacion[0] = operacionPosible;
				switch (operacionPosible) {
			        case MicroservicioAtmUtilsConstantes.OP_LOGIN:
			        	if(detallesOperacion.length==2) {
			        		return detallesOperacion;
			        	}
			        	break;
			        case MicroservicioAtmUtilsConstantes.OP_EXTRAER:
			        	if(detallesOperacion.length==4) {
			        		return detallesOperacion;
			        	}
			        	break;
			        case MicroservicioAtmUtilsConstantes.OP_DEPOSITAR:
			        	if(detallesOperacion.length==4) {
			        		return detallesOperacion;
			        	}
			        	break;
			        case MicroservicioAtmUtilsConstantes.OP_SALDO:
			        	if(detallesOperacion.length==3) {
			        		return detallesOperacion;
			        	}
			        	break;
			        	
			    }
				//Si la operacion existe pero no cumple con el formato, mostramos la excepcion correspondiente
				throw new MicroservicioAtmFacadeOperacionesException(MicroservicioAtmUtilsConstantes.WRONG_FORMAT_OPERATION_ERROR);
			}else {
				//Si el usuario introduce una operacion desconocida, mostramos la excepcion correspondiente
				throw new MicroservicioAtmFacadeOperacionesException(MicroservicioAtmUtilsConstantes.UNDEFINED_OPERATION_ERROR);
			}
		}else {
			//Si el usuario introduce datos no procesables, mostramos la excepcion correspondiente
			throw new MicroservicioAtmFacadeOperacionesException(MicroservicioAtmUtilsConstantes.WRONG_DATA_INTRODUCED_ERROR);
		}
	}
	
	/**
	 * Gestion de procesos necesarios para consumir el WS de Login
	 */
    public void identificarse(String tarjeta) throws MicroservicioAtmServicesServiciosException  {
    	log.debug("Se procede a relizar la operacion identificarse con la tarjeta " + tarjeta);
    	MicroservicioAtmRequest requestObjeto = serviciosAtm.generaObjetoRequest(tarjeta);
    	HashMap<String, Object> datosPeticion = serviciosAtm.generaPeticionWSOperacion(MicroservicioAtmUtilsConstantes.OP_LOGIN, requestObjeto);
    	log.debug("Datos a procesar:" + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(datosPeticion));
    	MicroservicioAtmResponse responseWS = serviciosAtm.gestionaPeticionWSConBody(datosPeticion);
    	log.debug(responseWS.getMensaje());
    	System.out.println(responseWS.getMensaje());
    }
    
    /**
	 * Gestion de procesos necesarios para consumir el WS de Extraccion
	 */
    public void extraerEfectivo(String tarjeta, String cuenta, String importe) throws ParseException, MicroservicioAtmServicesServiciosException  {
    	log.debug("Se procede a relizar la operacion extraer con la tarjeta " + tarjeta + " y cuenta " +  cuenta);
    	MicroservicioAtmRequest requestObjeto = serviciosAtm.generaObjetoRequest(tarjeta, cuenta, MicroservicioAtmUtilsValores.getInstance().convertStringToBigDecimal(importe), null);
    	HashMap<String, Object> datosPeticion = serviciosAtm.generaPeticionWSOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, requestObjeto);
    	log.debug("Datos a procesar:" + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(datosPeticion));
    	MicroservicioAtmResponse responseWS = serviciosAtm.gestionaPeticionWSConBody(datosPeticion);
    	log.debug(responseWS.getMensaje());
    	System.out.println(responseWS.getMensaje());
    }
    
    /**
	 * Gestion de procesos necesarios para consumir el WS de Deposito
	 */
    public void depositarEfectivo(String tarjeta, String cbu, String importe) throws ParseException, MicroservicioAtmServicesServiciosException  {
    	log.debug("Se procede a relizar la operacion extraer con la tarjeta " + tarjeta + " y cbu " +  cbu);
    	MicroservicioAtmRequest requestObjeto = serviciosAtm.generaObjetoRequest(tarjeta, null, MicroservicioAtmUtilsValores.getInstance().convertStringToBigDecimal(importe), cbu);
    	HashMap<String, Object> datosPeticion = serviciosAtm.generaPeticionWSOperacion(MicroservicioAtmUtilsConstantes.OP_DEPOSITAR, requestObjeto);
    	log.debug("Datos a procesar:" + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(datosPeticion));
    	MicroservicioAtmResponse responseWS = serviciosAtm.gestionaPeticionWSConBody(datosPeticion);
    	log.debug(responseWS.getMensaje());
    	System.out.println(responseWS.getMensaje());
    }
    
    /**
	 * Gestion de procesos necesarios para consumir el WS de Consulta Saldo
	 */
    public void consultarSaldoActual(String tarjeta, String cuenta) throws MicroservicioAtmServicesServiciosException  {
    	log.debug("Se procede a relizar la operacion consutlar saldo con la tarjeta " + tarjeta + " y cuenta " +  cuenta);
    	MicroservicioAtmRequest requestObjeto = serviciosAtm.generaObjetoRequest(tarjeta, cuenta);
    	HashMap<String, Object> datosPeticion = serviciosAtm.generaPeticionWSOperacion(MicroservicioAtmUtilsConstantes.OP_SALDO, requestObjeto);
    	log.debug("Datos a procesar:" + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(datosPeticion));
    	MicroservicioAtmResponse responseWS = serviciosAtm.gestionaPeticionWS(datosPeticion);
    	log.debug(responseWS.getMensaje());
    	System.out.println(responseWS.getMensaje());
    }
}
