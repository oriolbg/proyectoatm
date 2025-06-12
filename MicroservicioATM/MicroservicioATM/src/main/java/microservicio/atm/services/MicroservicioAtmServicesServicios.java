package microservicio.atm.services;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.ws.rs.core.MediaType;

import org.apache.axis2.kernel.http.HTTPConstants;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import microservicio.atm.persistence.MicroservicioAtmRequest;
import microservicio.atm.persistence.MicroservicioAtmResponse;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;
import microservicio.atm.utils.MicroservicioAtmUtilsWSRest;

/**
 * Servicios para la ejecución de las operaciones seleccionadas por el cliente
 */
@Service
public class MicroservicioAtmServicesServicios {
	
	/**
	 * Creacion del objeto para las peticiones REST al microservicio
	 */
	public MicroservicioAtmRequest generaObjetoRequest(String tarjeta) {
		MicroservicioAtmRequest requestObjeto = new MicroservicioAtmRequest();
		requestObjeto.setTarjeta(tarjeta);
		return requestObjeto;
	}
	
	public MicroservicioAtmRequest generaObjetoRequest(String tarjeta, String cuenta, BigDecimal importe, String cbu) {
		MicroservicioAtmRequest requestObjeto = new MicroservicioAtmRequest();
		requestObjeto.setTarjeta(tarjeta);
		requestObjeto.setCuenta(cuenta);
		requestObjeto.setCbu(cbu);
		requestObjeto.setImporte(importe);
		return requestObjeto;
	}
	
	public MicroservicioAtmRequest generaObjetoRequest(String tarjeta, String cuenta) {
		MicroservicioAtmRequest requestObjeto = new MicroservicioAtmRequest();
		requestObjeto.setTarjeta(tarjeta);
		requestObjeto.setCuenta(cuenta);
		return requestObjeto;
	}
	
	/**
	 * Asignamos las diferentes parametrizaciones para consumir el WS
	 */
	public HashMap<String, Object> generaPeticionWSOperacion(String operacion, MicroservicioAtmRequest requestObjeto) {
		HashMap<String, Object> datos = new HashMap<String, Object>();
		switch (operacion) {
	        case MicroservicioAtmUtilsConstantes.OP_LOGIN:
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_URL,MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.URL_BASE_WS_PROPERTY)+"login");
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD, MicroservicioAtmUtilsWSRest.REQUEST_METHOD_POST);
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_BODY_DATA, requestObjeto);
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_TIMEOUT, MicroservicioAtmUtilsWSRest.DEFAULT_TIMEOUT);
	            break;
	        case MicroservicioAtmUtilsConstantes.OP_EXTRAER:
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_URL,MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.URL_BASE_WS_PROPERTY)+"extraer");
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD, MicroservicioAtmUtilsWSRest.REQUEST_METHOD_PUT);
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_BODY_DATA, requestObjeto);
	            break;
	        case MicroservicioAtmUtilsConstantes.OP_DEPOSITAR:
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_URL,MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.URL_BASE_WS_PROPERTY)+"depositar");
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD, MicroservicioAtmUtilsWSRest.REQUEST_METHOD_PUT);
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_BODY_DATA, requestObjeto);
	            break;
	        case MicroservicioAtmUtilsConstantes.OP_SALDO:
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_URL,MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.URL_BASE_WS_PROPERTY)+"saldo?tarjeta="+requestObjeto.getTarjeta()+"&cuenta="+requestObjeto.getCuenta());
	        	datos.put(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD, MicroservicioAtmUtilsWSRest.REQUEST_METHOD_GET);
	        	break;
	        default:
	            System.out.println(MicroservicioAtmUtilsConstantes.UNDEFINED_OPERATION_ERROR);
	    }
		return datos;
	}
	
	/**
	 * Consumo del WS publicado con metodos que requieren Body
	 */
	public MicroservicioAtmResponse gestionaPeticionWSConBody(HashMap<String, Object> datosPeticion) throws MicroservicioAtmServicesServiciosException {
		//Tratamiento de datos previos
		MicroservicioAtmRequest request = (MicroservicioAtmRequest) datosPeticion.get(MicroservicioAtmUtilsConstantes.WS_BODY_DATA);
		//Datos a enviar en el header
		HashMap<String, String> hmCabecera =  new HashMap<String, String>();
		hmCabecera.put(HTTPConstants.HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON);
		//Ejecutamos el consumo del WS
		HashMap<String, String> response = MicroservicioAtmUtilsWSRest.methodWSConsumoExterno(datosPeticion.get(MicroservicioAtmUtilsConstantes.WS_URL).toString(), 
				datosPeticion.get(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD).toString(), hmCabecera, 
				MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(request), MicroservicioAtmUtilsWSRest.DEFAULT_TIMEOUT);
		validaRespuestaWS(response);
		return generaRespuestaPeticionWS(response);
	}
	
	/**
	 * Consumo del WS publicado con metodos que no requieren Body
	 */
	public MicroservicioAtmResponse gestionaPeticionWS(HashMap<String, Object> datosPeticion) throws MicroservicioAtmServicesServiciosException {
		//Ejecutamos el consumo del WS
		HashMap<String, String> response = MicroservicioAtmUtilsWSRest.methodWSConsumoExterno(datosPeticion.get(MicroservicioAtmUtilsConstantes.WS_URL).toString(), 
				datosPeticion.get(MicroservicioAtmUtilsConstantes.WS_REQUEST_METHOD).toString(), null, null, MicroservicioAtmUtilsWSRest.DEFAULT_TIMEOUT);
		validaRespuestaWS(response);
		return generaRespuestaPeticionWS(response);
	}
	
	/**
	 * Comprobacion de la respuesta recibida por el WS
	 */
	private void validaRespuestaWS(HashMap<String, String> response) throws MicroservicioAtmServicesServiciosException {
		if(!CollectionUtils.isEmpty(response)){
			if(!response.containsKey(MicroservicioAtmUtilsWSRest.RESPONSE_MESSAGE)) {
				throw new MicroservicioAtmServicesServiciosException(MicroservicioAtmUtilsConstantes.NO_MESSAGE_RESPONSE_API_SERVER);
			}
		}else{
			throw new MicroservicioAtmServicesServiciosException(MicroservicioAtmUtilsConstantes.NO_RESPONSE_API_SERVER);
		}
	}
	
	/**
	 * Generacion del objeto respuesta en base a la peticion solicitada. Contiene el mensaje final al usuario
	 */
	private MicroservicioAtmResponse generaRespuestaPeticionWS(HashMap<String, String> response) throws MicroservicioAtmServicesServiciosException{
		try {
			return MicroservicioAtmUtilsValores.getInstance().devuelveClaseFromJson(response.get(MicroservicioAtmUtilsWSRest.RESPONSE_MESSAGE), MicroservicioAtmResponse.class);
		}catch(Exception e) {
			throw new MicroservicioAtmServicesServiciosException(e.getMessage());
		}
	}

}
