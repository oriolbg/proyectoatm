package microservicio.atm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import microservicio.atm.facade.MicroservicioAtmFacadeOperacionesCliente;
import microservicio.atm.persistence.MicroservicioAtmRequest;
import microservicio.atm.persistence.MicroservicioAtmResponse;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;

/**
 * Publicacion de los servicios REST que consumirán las diferentes acciones
 */
@RestController
@RequestMapping("/atm")
public class MicroservicioAtmServiciosRest {

	private static final Logger log = Logger.getLogger(MicroservicioAtmServiciosRest.class.getName());
	
	@Autowired
	private MicroservicioAtmFacadeOperacionesCliente facadeCliente;
	
    private void inicializaRecursosServiciosRest() {
    	if(facadeCliente==null) {
    		facadeCliente = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(MicroservicioAtmFacadeOperacionesCliente.class);
    	}
    }
	
	@GetMapping
    public String obtenerSaludo() {
        return "¡Hola desde el microservicio!";
    }

    @PostMapping
    public String recibirSaludo(@RequestBody String nombre) {
        return "Hola, " + nombre + "! Tu saludo fue recibido.";
    }
    
    @PostMapping("/login")
    @ResponseBody
    public MicroservicioAtmResponse login(@RequestBody MicroservicioAtmRequest request) {
    	log.debug("login() - Se ha recibido los siguientes datos: " + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(request));
    	MicroservicioAtmResponse response = new MicroservicioAtmResponse();
    	try {
    		inicializaRecursosServiciosRest();
    		String mensajeUsuario = facadeCliente.identificarseSistema(request.getTarjeta());
    		response.setCodigoResultado(HttpStatus.OK.value());
    		response.setMensaje(mensajeUsuario);
		} catch (MicroservicioAtmServiciosRestException e) {
			response.setCodigoResultado(HttpStatus.FORBIDDEN.value());
    		response.setMensaje(e.getMessage());
		} catch (Exception e) {
			response.setCodigoResultado(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		response.setMensaje(e.getMessage());
		}
		
        return response;
    }
    
    @PutMapping("/extraer")
    @ResponseBody
    //@Transactional(rollbackFor = Exception.class)
    public MicroservicioAtmResponse extraer(@RequestBody MicroservicioAtmRequest request) {
    	log.debug("extraer() - Se ha recibido los siguientes datos: " + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(request));
    	MicroservicioAtmResponse response = new MicroservicioAtmResponse();
    	try {
    		inicializaRecursosServiciosRest();
    		String mensajeUsuario = facadeCliente.extraerEfectivo(request.getTarjeta(), request.getCuenta(), request.getImporte());
    		response.setCodigoResultado(HttpStatus.OK.value());
    		response.setMensaje(mensajeUsuario);
		} catch (MicroservicioAtmServiciosRestException e) {
			response.setCodigoResultado(HttpStatus.FORBIDDEN.value());
    		response.setMensaje(e.getMessage());
		} catch (Exception e) {
			response.setCodigoResultado(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		response.setMensaje(e.getMessage());
		}
		
        return response;
    }
    
    @PutMapping("/depositar")
    @ResponseBody
    public MicroservicioAtmResponse depositar(@RequestBody MicroservicioAtmRequest request) {
    	log.debug("depositar() - Se ha recibido los siguientes datos: " + MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(request));
    	MicroservicioAtmResponse response = new MicroservicioAtmResponse();
    	try {
    		inicializaRecursosServiciosRest();
    		String mensajeUsuario = facadeCliente.depositarEfectivo(request.getTarjeta(), request.getCbu(), request.getImporte());
    		response.setCodigoResultado(HttpStatus.OK.value());
    		response.setMensaje(mensajeUsuario);
		} catch (MicroservicioAtmServiciosRestException e) {
			response.setCodigoResultado(HttpStatus.FORBIDDEN.value());
    		response.setMensaje(e.getMessage());
		} catch (Exception e) {
			response.setCodigoResultado(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		response.setMensaje(e.getMessage());
		}
		
        return response;
    }
    
    @GetMapping("/saldo")
    @ResponseBody
    public MicroservicioAtmResponse saldo(@RequestParam("tarjeta") String tarjeta, @RequestParam("cuenta") String cuenta) {
    	log.debug("saldo() - Se ha recibido los siguientes datos - Tarjeta: " + tarjeta + " - Cuenta: " + cuenta);
    	MicroservicioAtmResponse response = new MicroservicioAtmResponse();
    	try {
    		inicializaRecursosServiciosRest();
    		String mensajeUsuario = facadeCliente.consultarSaldo(tarjeta, cuenta);
    		response.setCodigoResultado(HttpStatus.OK.value());
    		response.setMensaje(mensajeUsuario);
		} catch (MicroservicioAtmServiciosRestException e) {
			response.setCodigoResultado(HttpStatus.FORBIDDEN.value());
    		response.setMensaje(e.getMessage());
		} catch (Exception e) {
			response.setCodigoResultado(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		response.setMensaje(e.getMessage());
		}
		
        return response;
    }
    
    @GetMapping("/registros")
    @ResponseBody
    public MicroservicioAtmResponse obtenerRegistros() {
		log.debug("obtenerRegistros() - Se procede a devolver todos los registros generados en las transacciones");
    	MicroservicioAtmResponse response = new MicroservicioAtmResponse();
    	try {
    		inicializaRecursosServiciosRest();
    		String mensajeUsuario = facadeCliente.consultarTodosRegistros();
    		response.setCodigoResultado(HttpStatus.OK.value());
    		response.setMensaje(mensajeUsuario);
		} catch (MicroservicioAtmServiciosRestException e) {
			response.setCodigoResultado(HttpStatus.FORBIDDEN.value());
    		response.setMensaje(e.getMessage());
		} catch (Exception e) {
			response.setCodigoResultado(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		response.setMensaje(e.getMessage());
		}
		
        return response;
    }
}
