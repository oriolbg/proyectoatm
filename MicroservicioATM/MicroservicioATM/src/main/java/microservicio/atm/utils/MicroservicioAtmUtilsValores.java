package microservicio.atm.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MicroservicioAtmUtilsValores {
	
	private static MicroservicioAtmUtilsValores instance = new MicroservicioAtmUtilsValores();
	
    private NumberFormat currencyFormatter;
    private NumberFormat numberFormatter;
    private DateTimeFormatter fileDateFormatter;
    private DateTimeFormatter logDateFormatter;
    private DateTimeFormatter infoDateFormatter;
    
	private MicroservicioAtmUtilsValores() {
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMaximumFractionDigits(2);
        currencyFormatter.setRoundingMode(RoundingMode.HALF_UP);

        // Build the number formatter
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMaximumFractionDigits(2);
        numberFormatter.setRoundingMode(RoundingMode.HALF_UP);

        // Build the date formatters
        fileDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        logDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        infoDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

	}
	
	public static MicroservicioAtmUtilsValores getInstance() {
		if (instance == null) {
			instance = new MicroservicioAtmUtilsValores();
		}
		return instance;
	}
	

    public String formatUSD(double value) {
        return currencyFormatter.format(value);
    }
    
    public BigDecimal parseUSD(String value) throws ParseException {
        Float f =  currencyFormatter.parse(value).floatValue();
        return new BigDecimal(f);
    }
    
    public BigDecimal convertStringToBigDecimal(String value) throws ParseException {
        Double d =  numberFormatter.parse(value).doubleValue();
        return new BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN);
    }

    public String formatNumber(double value) {
        return numberFormatter.format(value);
    }

    public String fileDate() {
        return fileDateFormatter.format(LocalDateTime.now());
    }

    public String logDate() {
        return logDateFormatter.format(LocalDateTime.now());
    }

    public String infoDate(LocalDateTime date) {
        return infoDateFormatter.format(date);
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T devuelveClaseFromJson(String json, Class clazz) throws JsonMappingException, JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
	    return (T) mapper.readValue(json, clazz);
	}
    
	public String devuelveJsonFromClase(Object object){
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		    return mapper.writeValueAsString(object);
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
    public String getPropertySystem(String key) {
    	return (String) System.getProperties().get(key);
    }
}