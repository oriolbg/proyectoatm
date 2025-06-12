package microservicio.atm.facade;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import microservicio.atm.MicroservicioAtmServiciosRestException;
import microservicio.atm.persistence.entities.AtmEntityCuenta;
import microservicio.atm.persistence.entities.AtmEntityException;
import microservicio.atm.persistence.entities.AtmEntityRegistroEvento;
import microservicio.atm.persistence.entities.AtmEntityTarjeta;
import microservicio.atm.persistence.entities.AtmEntityTarjetaCuenta;
import microservicio.atm.services.MicroservicioAtmServicesServiciosCliente;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;

/**
 * Servicio para la gestion de las operacion ejecutadas segun la operacion seleccionada.
 */
@Service
public class MicroservicioAtmFacadeOperacionesCliente {

	protected static Logger log = Logger.getLogger(MicroservicioAtmFacadeOperacionesCliente.class);

	@Autowired
	private MicroservicioAtmServicesServiciosCliente serviciosCliente;
	
    public void inicializaFacadeOperacionesCliente() {
    	if(serviciosCliente==null) {
    		serviciosCliente = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(MicroservicioAtmServicesServiciosCliente.class);
    	}
    }
	
    /**
     * Gestion de todos los servicios para realizar la consulta de la tarjeta en el sistema
     */
	public String identificarseSistema(String tarjeta) throws MicroservicioAtmServiciosRestException{
		try {
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.REG_OP_PROCESO_LOGIN, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Tarjeta", tarjeta);
			AtmEntityTarjeta tarjetaEntity = serviciosCliente.compruebaTarjeta(tarjeta);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.REG_OP_PROCESO_LOGIN, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Ingreso exitoso", "");
			if(StringUtils.equals(tarjetaEntity.getActiva(),"N")) {
				return "Ingreso no exitoso";
			}
			return "Ingreso exitoso";
		}catch (AtmEntityException e) {
			log.debug("identificarseSistema() - No se encontró la tarjeta");
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.REG_OP_PROCESO_LOGIN, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Ingreso no exitoso", e.getMessage());
			return "Ingreso no exitoso";
		}
	}
	
    /**
     * Gestion de todos los servicios para realizar la extraccion de dinero en el sistema
     * ** Se marca como transaccional para que asegurar la correcta funcionalidad de la operacion
     */
	@Transactional(rollbackFor = Exception.class)
	public String extraerEfectivo(String tarjeta, String cuenta, BigDecimal importe) throws MicroservicioAtmServiciosRestException{
		try {
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Tarjeta: " + tarjeta, "Cuenta: " + tarjeta + " - Importe: " + importe);
			AtmEntityTarjetaCuenta tarjetaCuentaEntity = serviciosCliente.compruebaTarjetaCuenta(tarjeta, cuenta);
			AtmEntityCuenta cuentaEntity = serviciosCliente.compruebaCuenta(tarjetaCuentaEntity.getTarjetaCuentaId().getCuenta());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_DATA_TRANSACT, "Saldo Actual: " + cuentaEntity.getSaldo(), "Importe extraccion: " + importe);
			BigDecimal saldoRestante = serviciosCliente.validaValorExtraccion(cuentaEntity.getSaldo(), importe);
			BigDecimal saldoActual = cuentaEntity.getSaldo();
			serviciosCliente.actualizaSaldoCuenta(cuentaEntity, saldoRestante);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_DATA_TRANSACT, "Nuevo Saldo Actual: " + saldoRestante, "Saldo Anterior: " + saldoActual);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "", "");
			return "Retire su dinero”";
		}catch (AtmEntityException e) {
			log.debug("extraerEfectivo() - Error en la extraccion de efectivo - Error: " + e.getMessage());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Error en la extraccion de efectivo", e.getMessage());
			return e.getMessage();
		}
	}
	
	/**
     * Gestion de todos los servicios para realizar el deposito de dinero en el sistema
     * ** Se marca como transaccional para que asegurar la correcta funcionalidad de la operacion
     */
	@Transactional(rollbackFor = Exception.class)
	public String depositarEfectivo(String tarjeta, String cbu, BigDecimal importe) throws MicroservicioAtmServiciosRestException{
		try {
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_DEPOSITAR, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Tarjeta: " + tarjeta, "CBU: " + cbu + " - Importe: " + importe);
			AtmEntityCuenta cuentaEntity = serviciosCliente.compruebaCuentaPorCbu(cbu);
			serviciosCliente.compruebaTarjetaCuenta(tarjeta, cuentaEntity.getNumero());
			BigDecimal nuevoSaldo = serviciosCliente.validaValorDeposito(cuentaEntity.getSaldo(), importe);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_EXTRAER, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_DATA_TRANSACT, "Saldo Actual: " + cuentaEntity.getSaldo(), "Importe deposito: " + importe);
			BigDecimal saldoActual = cuentaEntity.getSaldo();
			serviciosCliente.actualizaSaldoCuenta(cuentaEntity, nuevoSaldo);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_DEPOSITAR, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_DATA_TRANSACT, "Nuevo Saldo Actual: " + nuevoSaldo, "Saldo Anterior: " + saldoActual);
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_DEPOSITAR, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "", "");
			return "Depósito exitoso";
		}catch (AtmEntityException e) {
			log.debug("depositarEfectivo() - Error en el deposito de efectivo - Error: " + e.getMessage());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_DEPOSITAR, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Error en el deposito de efectivo", e.getMessage());
			return e.getMessage();
		}
		
	}
	
	/**
     * Gestion de todos los servicios para realizar la consulta de saldo de un usuario
     */
	public String consultarSaldo(String tarjeta, String cuenta) throws MicroservicioAtmServiciosRestException{
		try {
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_SALDO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Tarjeta: " + tarjeta, "Cuenta: " + tarjeta);
			AtmEntityTarjetaCuenta tarjetaCuentaEntity = serviciosCliente.compruebaTarjetaCuenta(tarjeta, cuenta);
			serviciosCliente.compruebaCuenta(tarjetaCuentaEntity.getTarjetaCuentaId().getCuenta());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_SALDO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "", "");
			return "Su saldo es $ 99999,99";
		}catch (AtmEntityException e) {
			log.debug("consultarSaldo() - Error en la consulta de saldo - Error: " + e.getMessage());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_SALDO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_CLIENTE), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Error en la consulta", e.getMessage());
			return e.getMessage();
		}
	}
	
	/**
     * Gestion de todos los servicios para realizar la consulta de todos los registros en el sistema
     */
	public String consultarTodosRegistros() throws MicroservicioAtmServiciosRestException{
		try {
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_REGISTRO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_ADMINISTRADOR), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_INICIO, "Consultar todos los registros", "Auditoria Interna");
			List<AtmEntityRegistroEvento> registrosOperaciones = serviciosCliente.devuelteTodosRegistros();
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_REGISTRO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_ADMINISTRADOR), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "", "");
			return MicroservicioAtmUtilsValores.getInstance().devuelveJsonFromClase(registrosOperaciones);
		}catch (AtmEntityException e) {
			log.debug("consultarTodosRegistros() - Error en la consulta de saldo - Error: " + e.getMessage());
			serviciosCliente.creaRegistroOperacion(MicroservicioAtmUtilsConstantes.OP_REGISTRO, MicroservicioAtmUtilsValores.getInstance().getPropertySystem(MicroservicioAtmUtilsConstantes.NOMBRE_USUARIO_ADMINISTRADOR), MicroservicioAtmUtilsConstantes.REG_OP_OPERACION_FIN, "Error en la consulta", e.getMessage());
			return e.getMessage();
		}
	}
}
