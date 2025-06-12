package microservicio.atm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import microservicio.atm.utils.MicroservicioAtmUtilsConstantes;
import microservicio.atm.utils.MicroservicioAtmUtilsValores;

/***
 * 
mvn clean install
mvn spring-boot:run
 *
 */

/**
 * Clase de inicio de la aplicacion y carga de las propiedadas y datos de configuracion basicos
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = "microservicio.atm")
@EnableJpaRepositories(basePackages = "microservicio.atm.services.interfaces")
@EntityScan(basePackages = "microservicio.atm.persistence.entities")
public class MicroservicioAtmApplication {
	
	public static void main(String[] args) throws Exception {
		MicroservicioAtmApplication.inicializarAplicacion(args);
		
		MicroservicioAtmClientApplication.getInstance().inicializaAplicacionCliente();
	}
	
	/**
	 * Inicializa la lógica de CMZ igual que lo haría el POS.
	 */
	public static void inicializarAplicacion(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(MicroservicioAtmApplication.class, args);
		MicroservicioAtmUtilsConstantes.getInstance().setContext(context);
		
		//Leemos las propiedades de configuracion de la aplicacion
		MicroservicioAtmConfigProperties.getInstance();
				
		//Cargamos el formato de los valores a usar
		MicroservicioAtmUtilsValores.getInstance();
	}
}
