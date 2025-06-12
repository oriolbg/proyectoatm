package microservicio.atm.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;

public class MicroservicioAtmUtilsConstantes {

	protected static Logger log = Logger.getLogger(MicroservicioAtmUtilsConstantes.class);
	
	public ConfigurableApplicationContext context;
	
	public ConfigurableApplicationContext getContext() {
		return context;
	}

	public void setContext(ConfigurableApplicationContext context) {
		this.context = context;
	}

	protected static MicroservicioAtmUtilsConstantes instance;
	
	public static MicroservicioAtmUtilsConstantes getInstance() {
		if(instance == null){
			instance = new MicroservicioAtmUtilsConstantes();
		}
		return instance;
	}
	
	//Operaciones del menu
	public final static String OP_LOGIN = "LOGIN";
	public final static String OP_EXTRAER = "EXTRAER";
	public final static String OP_DEPOSITAR = "DEPOSITAR";
	public final static String OP_SALDO = "SALDO";
	public final static String OP_REGISTRO = "REGISTRO";
	
	public final static List<String> operacionesDisponibles = Arrays.asList(OP_LOGIN, OP_EXTRAER, OP_DEPOSITAR, OP_SALDO);
	
	//Mensajes de error de la aplicacion 
	public final static String UNDEFINED_OPERATION_ERROR 		= "La operacion solicitada no está disponible en el menú. Por favor, revise la operacion seleccionada";
	public final static String WRONG_FORMAT_OPERATION_ERROR 	= "La operacion seleccionada requiere de un formato de datos diferente al introduce. Por favor, revise los datos introducidos";
	public final static String WRONG_DATA_INTRODUCED_ERROR 		= "Los datos introducidos no permiten la interpretación de la operacion que desea realizar. Por favor, revise los datos introducidos";
	
	//Mensajes de error de los WS
	public final static String NO_RESPONSE_API_SERVER 			= "No se ha podido contactar con el servidor y no se ha gestionado su transaccion";
	public final static String NO_MESSAGE_RESPONSE_API_SERVER 	= "No se ha recibido respuesta de la operacion solicitada al servidor.";
	
	//Configuraciones del WS
	public final static String WS_URL = "URL";
	public final static String WS_REQUEST_METHOD = "REQUEST_METHOD";
	public final static String WS_HEADER_VALUES = "HEADER_VALUES";
	public final static String WS_BODY_DATA = "BODY_DATA";
	public final static String WS_TIMEOUT = "TIMEOUT";
	
	
	//Propiedades de configuracion de la aplicacion
	public final static String URL_BASE_WS_PROPERTY  = "url.base.ws";
	public final static String NOMBRE_USUARIO_CLIENTE  = "nombre.usuario.cliente.cajero";
	public final static String NOMBRE_USUARIO_ADMINISTRADOR  = "nombre.usuario.administrador";
	
	
	
	//Datos registro operaciones
	public final static String REG_OP_PROCESO_LOGIN  		= "LOGIN";
	public final static String REG_OP_PROCESO_EXTRACCION 	= "EXTRACCION";
	public final static String REG_OP_PROCESO_DEPOSITO  	= "DEPOSITO";
	public final static String REG_OP_PROCESO_CONSULTA  	= "CONSULTA SALDO";
	
	public final static String REG_OP_OPERACION_INICIO  		= "INICIO";
	public final static String REG_OP_OPERACION_DATA_TRANSACT  	= "TRANSACCION DATOS";
	public final static String REG_OP_OPERACION_FIN  			= "FIN";
	
	public String devuelveExplicacionMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("A continuacion se le mostraran las diferentes opciones disponibles. Siga el formato especificado segun la opcion deseada: " + System.lineSeparator());
		sb.append("1. Login <numero tarjeta debito>" + System.lineSeparator());
		sb.append("2. Extraer <tarjeta> <cuenta> <importe>" + System.lineSeparator());
		sb.append("3. Depositar <tarjeta> <cbu> <importe>" + System.lineSeparator());
		sb.append("4. Saldo <tarjeta> <cuenta>" + System.lineSeparator() + System.lineSeparator());
		return sb.toString();
	}
	
	public String devuelveMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append(System.lineSeparator() + "Seleccione la opción que desea ejecutar " + System.lineSeparator());
		sb.append("1. Identificarse" +  System.lineSeparator() + "2. Extraer efectivo de su cuenta" +  System.lineSeparator() + "3. Depositar efectivo" +  System.lineSeparator() + "4. Consultar saldo actual" + System.lineSeparator());
		sb.append(System.lineSeparator() + "Seleccione una opción siguiendo el formato anteriormente especificado: " + System.lineSeparator() + System.lineSeparator());
		return sb.toString();
	}
}
