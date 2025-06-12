package microservicio.atm;

import java.math.BigDecimal;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import microservicio.atm.facade.MicroservicioAtmFacadeOperaciones;
import microservicio.atm.facade.MicroservicioAtmFacadeOperacionesException;
import microservicio.atm.services.MicroservicioAtmServicesServiciosCliente;
import microservicio.atm.services.MicroservicioAtmServicesServiciosException;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;

/**
 * Clase para la gestion de las diferentes opciones que se le muestran al cliente
 */
@Component
public class MicroservicioAtmClientApplication {

	private static final Logger log = Logger.getLogger(MicroservicioAtmClientApplication.class.getName());
	
	@Autowired
	private MicroservicioAtmServicesServiciosCliente serviciosCliente;
	
	@Autowired
	private MicroservicioAtmFacadeOperaciones facadeOperaciones;
	
	
	private static MicroservicioAtmClientApplication instance = null;
	
    public static MicroservicioAtmClientApplication getInstance() {
        if (instance == null) {
            instance = new MicroservicioAtmClientApplication();
        }
        return instance;
    }
    
    public void inicializaAplicacionCliente() {
    	if(serviciosCliente==null) {
    		serviciosCliente = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(MicroservicioAtmServicesServiciosCliente.class);
    	}
    	if(facadeOperaciones==null) {
    		facadeOperaciones = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(MicroservicioAtmFacadeOperaciones.class);
    	}
    	cargaInicialDatosCliente();
    	ejecutarMenuOperaciones();
    }
	
	/**
	 * Muestra las opciones de menu al usuario
	 */
	public void ejecutarMenuOperaciones() {
		//Explicacion de usabilidad del menu
		facadeOperaciones.inicializaFacadeOperaciones();
		System.out.println(MicroservicioAtmUtilsConstantes.getInstance().devuelveExplicacionMenu());
		Scanner scanner = new Scanner(System.in);
		while (true) {
			
			try {
				//Muestras las opciones de menu
				System.out.println(MicroservicioAtmUtilsConstantes.getInstance().devuelveMenu());
			    String opcion = scanner.nextLine();
			
			    //Validamos las opciones
			    if(StringUtils.isBlank(opcion)) {
			    	System.out.println("No ha seleccionado ninguna opción del menú");
			    }else {
			    	log.debug("se ha recibido la informacion: " + opcion);
			    	//Se determina la opcion seleccionada

			    	String[] datosOperacion = facadeOperaciones.validaOperacionSeleccionada(opcion);
					
					//Se ejecuta la operacion solicitada por el cliente
			    	System.out.println("se ha recibido la operacion: " + datosOperacion[0]);
			    	
				    switch (datosOperacion[0]) {
				        case MicroservicioAtmUtilsConstantes.OP_LOGIN:
				        	facadeOperaciones.identificarse(datosOperacion[1]);
				            break;
				        case MicroservicioAtmUtilsConstantes.OP_EXTRAER:
				        	facadeOperaciones.extraerEfectivo(datosOperacion[1], datosOperacion[2], datosOperacion[3]);
				            break;
				        case MicroservicioAtmUtilsConstantes.OP_DEPOSITAR:
				        	facadeOperaciones.depositarEfectivo(datosOperacion[1], datosOperacion[2], datosOperacion[3]);
				            break;
				        case MicroservicioAtmUtilsConstantes.OP_SALDO:
				        	facadeOperaciones.consultarSaldoActual(datosOperacion[1], datosOperacion[2]);
				        	break;
				        default:
				            System.out.println(MicroservicioAtmUtilsConstantes.UNDEFINED_OPERATION_ERROR);
				    }
			    }
			} catch (MicroservicioAtmFacadeOperacionesException e) {
				log.error("ejecutarMenuOperaciones() - Ha ocurrido un error en la validacion de la operacion seleccionada - Error: " + e.getMessage());
			} catch (MicroservicioAtmServicesServiciosException e) {
				log.error("ejecutarMenuOperaciones() - Ha ocurrido un error en el procesado de los datos introducidos en la opearacion seleccionada - Error: " + e.getMessage());
			} catch(Exception e) {
				log.error("ejecutarMenuOperaciones() - Ha ocurrido un error no controlado en la aplicación - Tipo Error: " + e.getClass().getSimpleName() + " Error: " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * Metodo para cargar de algunos datos de clientes
	 * serviciosCliente.almacenaTarjeta(tarjeta, activa);
	 * serviciosCliente.almacenaCuenta(cuenta, cbu, saldo);
	 * serviciosCliente.almacenaTarjetaCuenta(tarjeta, cuenta);
	 */
	public void cargaInicialDatosCliente() {
		log.debug("cargaInicialDatosCliente() - Se procede a cargar los datos iniciales de la base de datos");
		
		serviciosCliente.inicializaServicesServiciosCliente();
		serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_REGISTRO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_ADMINISTRADOR), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Carga Inicial de Datos", "Auditoria Interna");

		//Cliente 1 con 2 tarjetas
		serviciosCliente.almacenaTarjeta("2200989781", "S");
		serviciosCliente.almacenaCuenta("9220098978", "3220098978", new BigDecimal(400.4));
		serviciosCliente.almacenaTarjetaCuenta("2200989781", "9220098978");
		
		serviciosCliente.almacenaTarjeta("2200989782", "S");
		serviciosCliente.almacenaCuenta("9220098978", "3220098978", new BigDecimal(500));
		serviciosCliente.almacenaTarjetaCuenta("2200989782", "9220098978");
		
		//Cliente 2 con 1 tarjeta
		serviciosCliente.almacenaTarjeta("220975851", "S");
		serviciosCliente.almacenaCuenta("922097585", "322097585", new BigDecimal(350));
		serviciosCliente.almacenaTarjetaCuenta("220975851", "922097585");
		
		//Cliente 3 con 2 tarjetas
		serviciosCliente.almacenaTarjeta("227786321", "N");
		serviciosCliente.almacenaCuenta("922778632", "322778632", new BigDecimal(350));
		serviciosCliente.almacenaTarjetaCuenta("227786321", "922778632");
		
		serviciosCliente.almacenaTarjeta("227786322", "S");
		serviciosCliente.almacenaCuenta("922778632", "322778632", new BigDecimal(350));
		serviciosCliente.almacenaTarjetaCuenta("227786322", "922778632");
		
		serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_REGISTRO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_ADMINISTRADOR), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Carga Inicial de Datos", "Auditoria Interna");

	}
}
