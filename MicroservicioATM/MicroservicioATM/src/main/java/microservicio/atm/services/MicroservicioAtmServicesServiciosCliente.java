package microservicio.atm.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import microservicio.atm.persistence.entities.AtmEntityCuenta;
import microservicio.atm.persistence.entities.AtmEntityException;
import microservicio.atm.persistence.entities.AtmEntityRegistroEvento;
import microservicio.atm.persistence.entities.AtmEntityTarjeta;
import microservicio.atm.persistence.entities.AtmEntityTarjetaCuenta;
import microservicio.atm.persistence.entities.AtmEntityTarjetaCuentaId;
import microservicio.atm.services.interfaces.AtmRepositoryCuenta;
import microservicio.atm.services.interfaces.AtmRepositoryRegistroEvento;
import microservicio.atm.services.interfaces.AtmRepositoryTarjeta;
import microservicio.atm.services.interfaces.AtmRepositoryTarjetaCuenta;
import microservicio.atm.utils.MicroservicioAtmUtilsBigDecimal;
import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;

/**
 * Servicio internos necesarios en la ejecucion de la operacion seleccionada por el cliente
 */
@Service
public class MicroservicioAtmServicesServiciosCliente {
	
	protected static Logger log = Logger.getLogger(MicroservicioAtmServicesServiciosCliente.class);
	
	@Autowired
    private AtmRepositoryCuenta repositoryCuenta;
	
	@Autowired
    private AtmRepositoryTarjeta repositoryTarjeta;
	
	@Autowired
    private AtmRepositoryTarjetaCuenta repositoryTarjetaCuenta;
	
	@Autowired
    private AtmRepositoryRegistroEvento repositoryRegistroEvento;
	
    public void inicializaServicesServiciosCliente() {
    	if(repositoryCuenta==null) {
    		repositoryCuenta = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(AtmRepositoryCuenta.class);
    	}
    	if(repositoryTarjeta==null) {
    		repositoryTarjeta = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(AtmRepositoryTarjeta.class);
    	}
    	if(repositoryTarjetaCuenta==null) {
    		repositoryTarjetaCuenta = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(AtmRepositoryTarjetaCuenta.class);
    	}
    	if(repositoryRegistroEvento==null) {
    		repositoryRegistroEvento = MicroservicioAtmUtilsConstantes.getInstance().getContext().getBean(AtmRepositoryRegistroEvento.class);
    	}
    }
	
	/**
	 * Operaciones basicas de consulta en base de datos: CRUD
	 */
    public AtmEntityTarjeta compruebaTarjeta(String numero) throws AtmEntityException {
        return repositoryTarjeta.findById(numero)
                .orElseThrow(() -> new AtmEntityException("Tarjeta no encontrada"));
    }
    
    public AtmEntityTarjeta almacenaTarjeta(String tarjeta, String activa){
    	log.debug("Almacenando Tarjeta: " + tarjeta);
    	AtmEntityTarjeta tarjetaEntity = new AtmEntityTarjeta(tarjeta, activa);
        return repositoryTarjeta.save(tarjetaEntity);
    }
    
    public AtmEntityCuenta compruebaCuenta(String numero) throws AtmEntityException {
        return repositoryCuenta.findById(numero)
                .orElseThrow(() -> new AtmEntityException("Cuenta no encontrada"));
    }
    
    public AtmEntityCuenta compruebaCuentaPorCbu(String cbu) throws AtmEntityException {
        return repositoryCuenta.findByCbu(cbu)
                .orElseThrow(() -> new AtmEntityException("Cuenta por CBU no encontrada"));
    }
    
    public AtmEntityCuenta almacenaCuenta(String numero, String cbu, BigDecimal saldo){
    	AtmEntityCuenta cuentaEntity = new AtmEntityCuenta(numero, cbu, saldo);
        return repositoryCuenta.save(cuentaEntity);
    }
    
    public AtmEntityCuenta actualizaSaldoCuenta(AtmEntityCuenta cuentaEntity, BigDecimal saldoRestante) {
    	cuentaEntity.setSaldo(saldoRestante);
    	return repositoryCuenta.save(cuentaEntity);
    }
    
    public AtmEntityTarjetaCuenta compruebaTarjetaCuenta(String tarjeta, String cuenta) throws AtmEntityException {
    	AtmEntityTarjetaCuentaId id = new AtmEntityTarjetaCuentaId(tarjeta, cuenta);
        return repositoryTarjetaCuenta.findById(id)
                .orElseThrow(() -> new AtmEntityException("Vinculacion de Tarjeta con Cuenta no encontrada"));
    }
    
    public AtmEntityTarjetaCuenta almacenaTarjetaCuenta(String tarjeta, String cuenta){
    	AtmEntityTarjetaCuentaId id = new AtmEntityTarjetaCuentaId(tarjeta, cuenta);
    	AtmEntityTarjetaCuenta tarjetaCuentaEntity = new AtmEntityTarjetaCuenta(id);
        return repositoryTarjetaCuenta.save(tarjetaCuentaEntity);
    }
    
    public List<AtmEntityRegistroEvento> devuelteTodosRegistros() throws AtmEntityException {
    	List<AtmEntityRegistroEvento> registosOperaciones = repositoryRegistroEvento.findAll();
    	if(CollectionUtils.isEmpty(registosOperaciones)) {
    		throw new AtmEntityException("No se han encontrado registros de operaciones");
    	}
    	return registosOperaciones;
    }
	
    public AtmEntityRegistroEvento creaRegistroOperacion(String proceso, String usuario, String operacion, String datos, String otrosDatos) {
    	AtmEntityRegistroEvento registro = generaObjetoRegistroOperacion(proceso, usuario, operacion, datos, otrosDatos);
        return repositoryRegistroEvento.save(registro);
    }
	
	/**
	 * Creacion del objeto de registro de todas las operaciones
	 */
	public AtmEntityRegistroEvento generaObjetoRegistroOperacion(String proceso, String usuario, String operacion, String datos, String otrosDatos) {
		return new AtmEntityRegistroEvento(new Date(), proceso, usuario, operacion, datos, otrosDatos);
	}
	
	/**
	 * Validamos el importe para la extraccion de dinero
	 */
	public BigDecimal validaValorExtraccion(BigDecimal saldo, BigDecimal importeExtraccion) throws AtmEntityException {
		if(MicroservicioAtmUtilsBigDecimal.getInstance().isMayorACero(saldo)) {
			if(MicroservicioAtmUtilsBigDecimal.getInstance().isMayorACero(importeExtraccion)) {
				if(MicroservicioAtmUtilsBigDecimal.getInstance().isMenor(saldo, importeExtraccion)) {
					throw new AtmEntityException("El importe a extraer supera el saldo disponible");
				}else {
					return saldo.subtract(importeExtraccion);
				}
			}else {
				throw new AtmEntityException("El importe a extraer debe ser positivo");
			}
		}else {
			throw new AtmEntityException("El saldo disponible no permite realizar ninguna extraccion");
		}
	}
	
	/**
	 * Validamos el importe para el deposito de dinero
	 */
	public BigDecimal validaValorDeposito(BigDecimal saldo, BigDecimal importe) throws AtmEntityException {
		if(MicroservicioAtmUtilsBigDecimal.getInstance().isMenorOrIgualACero(importe)) {
			throw new AtmEntityException("El importe a ingresar debe ser mayor a 0");
		}
		return saldo.add(importe);
	}
}
