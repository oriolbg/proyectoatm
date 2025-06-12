package microservicio.atm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Clase de carga en propiedades del sistema los datos registrados en el archivo application.properties
 */
public class MicroservicioAtmConfigProperties {
	
	private static final Logger log = Logger.getLogger(MicroservicioAtmConfigProperties.class.getName());
	
	private static MicroservicioAtmConfigProperties instance = null;
	
    private MicroservicioAtmConfigProperties() {
    	//Listando propiedades del sistema
    	Properties props = System.getProperties();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println();
		for (Enumeration<Object> e = props.keys() ; e.hasMoreElements() ;) {
            String key = (String)e.nextElement();
            String val = (String)props.get(key);
            pw.println(key + "=" + val);
        }
		StringBuffer sb = sw.getBuffer();
		log.debug("ProductoAtmConfigProperties() - Listando propiedes del sistema: " + sb.toString());   	
    	
        // Cargar el ResourceBundle con el archivo application.properties
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        Set<String> keys = bundle.keySet();

		sw = new StringWriter();
		pw = new PrintWriter(sw);
		pw.println();
		for (String key : keys) {
			pw.println(key + "=" + bundle.getString(key));
			System.setProperty(key, bundle.getString(key));
		}
		sb = sw.getBuffer();
		log.debug("ProductoAtmConfigProperties() - Listando propiedes de la aplicacion:" + sb.toString());
    }

    public static MicroservicioAtmConfigProperties getInstance() {
        if (instance == null) {
            instance = new MicroservicioAtmConfigProperties();
        }
        return instance;
    }
    
}
