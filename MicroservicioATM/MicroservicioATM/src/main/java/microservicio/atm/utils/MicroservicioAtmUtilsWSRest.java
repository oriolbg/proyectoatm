package microservicio.atm.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Clase que gestiona la configuracion y consumo de los diferentes WS
 */
public class MicroservicioAtmUtilsWSRest {
	
	  protected static final Logger log = Logger.getLogger(MicroservicioAtmUtilsWSRest.class);
	  
	  //Request Methods
	  public final static String REQUEST_METHOD_GET 	= "GET";
	  public final static String REQUEST_METHOD_POST 	= "POST";
	  public final static String REQUEST_METHOD_PUT 	= "PUT";
	  public final static String REQUEST_METHOD_DELETE 	= "DELETE";

	  //Response object
	  public final static String CODE = "code";
	  public final static String ISOK = "OK";
	  public final static String MESSAGE_CODE = "messageCode";
	  public final static String RESPONSE_MESSAGE = "responseMessage";
	  
	  //Configuration connection
	  public final static int DEFAULT_TIMEOUT = 10000;
	  
	  
	  /**
	   * Método creado para consumir servicios HTTP
	   */
	  public static HashMap<String, String> methodWSConsumoExterno(String sUrl, String requestMethod, HashMap<String, String> hmCabecera, String sData, int timeOutMs) {

		HashMap<String, String> dataResponse = new HashMap<String, String>();

		try {
			log.debug("url: " + sUrl);
			URL url = new URL(sUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			if(timeOutMs > 0){ //Si es 0, se usa el timeOut por defecto
				conn.setReadTimeout(timeOutMs);
				conn.setConnectTimeout(timeOutMs);
			}else{
				conn.setReadTimeout(DEFAULT_TIMEOUT);
				conn.setConnectTimeout(DEFAULT_TIMEOUT);
			}
			conn.setRequestMethod(requestMethod);
			
			// Añadimos las properties de cabecera
			if(hmCabecera!=null && hmCabecera.size()>0){
				for (Map.Entry<String, String> entry : hmCabecera.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
					log.debug("key: " +entry.getKey() + " - value: "+entry.getValue());
				}
			}
			
			if(StringUtils.isNotBlank(sData)) {
				log.debug("data: " + sData);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
				bw.write(sData);
				bw.flush();
				bw.close();
			}

			InputStream response = null;
			
			// Comprobamos el estado del response
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
				log.debug("OK : HTTP : " + conn.getResponseCode() + " Message " + conn.getResponseMessage());
				dataResponse.put(ISOK, "true");
				response = conn.getInputStream();
			} else {
				log.error("Failed : HTTP error code : " + conn.getResponseCode() + " Message " + conn.getResponseMessage());
				dataResponse.put(ISOK, "false");
				response = conn.getErrorStream();
			}

			// Guardamos el Code y el Message Code
			dataResponse.put(CODE, String.valueOf(conn.getResponseCode()));
			dataResponse.put(MESSAGE_CODE, conn.getResponseMessage());

			// Recuperamos los mensajes de output
			BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8));
			String line = "";
			StringBuilder sMessageResponse = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sMessageResponse.append(line);
			}
			
			if(StringUtils.isNotBlank(sMessageResponse.toString())){
				log.debug(RESPONSE_MESSAGE + ": " + sMessageResponse.toString());
			}

			dataResponse.put(RESPONSE_MESSAGE, sMessageResponse.toString());

			conn.disconnect();

		} catch (Exception e) {
			log.error("methodWSConsumoExterno() - No ha sido posible establecer la comunicación con el servicio - Tipo Error: " + e.getClass().getSimpleName() + " - Error: " + e.getMessage());
			dataResponse = null;
		}

		return dataResponse;
	}
}