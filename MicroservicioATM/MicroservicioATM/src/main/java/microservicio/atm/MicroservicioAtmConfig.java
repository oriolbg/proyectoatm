package microservicio.atm;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicroservicioAtmConfig {

	/**
	 * Inicializa el bean del mapper para convertir entre diferentes tipos Java Beans
	 * @return
	 */
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
}
