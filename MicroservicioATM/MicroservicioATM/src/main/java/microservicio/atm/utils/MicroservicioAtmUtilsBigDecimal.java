package microservicio.atm.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MicroservicioAtmUtilsBigDecimal {
	
	private static MicroservicioAtmUtilsBigDecimal instance = new MicroservicioAtmUtilsBigDecimal();
	
	public static MicroservicioAtmUtilsBigDecimal getInstance() {
		if (instance == null) {
			instance = new MicroservicioAtmUtilsBigDecimal();
		}
		return instance;
	}
	
	public static final BigDecimal CIEN = new BigDecimal(100);
	
	public BigDecimal convertDoubleToBigDecimal(double value) {
		return convertDoubleToBigDecimal(value, 2);
	}
	
	public BigDecimal convertDoubleToBigDecimal(double value, int places) {
		return convertDoubleToBigDecimal(value, RoundingMode.HALF_DOWN, places);
	}
	
	public BigDecimal convertDoubleToBigDecimal(double value, RoundingMode r, int places) {
		BigDecimal valueBD = new BigDecimal(value);
		return valueBD.setScale(places, r);
	}

	public String convertDoubleToString(double value) {
		BigDecimal valueBD = convertDoubleToBigDecimal(value);
		return valueBD.toString();
	}
	
	public String convertDoubleToString(double value, int places) {
		return convertDoubleToString(value, RoundingMode.HALF_DOWN, places);
	}
	
	public String convertDoubleToString(double value, RoundingMode r, int places) {
		BigDecimal valueBD = convertDoubleToBigDecimal(value, r, places);
		return valueBD.toString();
	}
	
    public BigDecimal porcentaje(BigDecimal cantidad, BigDecimal porcentaje){
        return cantidad.multiply(porcentaje).divide(CIEN);
    }

    public BigDecimal menosPorcentaje(BigDecimal cantidad, BigDecimal porcentaje){
    	return cantidad.subtract(porcentaje(cantidad, porcentaje));
    }
    
    public BigDecimal masPorcentaje(BigDecimal cantidad, BigDecimal porcentaje){
    	return cantidad.add(porcentaje(cantidad, porcentaje));
    }
    
    
    public BigDecimal redondear(BigDecimal cantidad){
        if (cantidad == null){
            return null;
        }
        
        return redondear(cantidad, 2, RoundingMode.HALF_UP);
    }
    public BigDecimal redondear4(BigDecimal cantidad){
        if (cantidad == null){
            return null;
        }
        return redondear(cantidad, 4, RoundingMode.HALF_UP);
    }
    public BigDecimal redondear(BigDecimal cantidad, int decimales){
        if (cantidad == null){
            return null;        
        }
        return redondear(cantidad, decimales, RoundingMode.HALF_UP);
    }
    
    public BigDecimal redondear(BigDecimal cantidad, int decimales, RoundingMode roundingmode){
        if (cantidad == null){
            return null;
        }
        return cantidad.setScale(decimales, roundingmode);
    }
 
    
    public BigDecimal multiplica(BigDecimal bigDecimal, Integer integer){
        return bigDecimal.multiply(new BigDecimal(integer));
    }
    
    public boolean isMenor (BigDecimal a, BigDecimal b){
        return a.compareTo(b) < 0;
    }
    public boolean isMenorACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) < 0;
    }
    public boolean isMayor (BigDecimal a, BigDecimal b){
        return a.compareTo(b) > 0;
    }
    public boolean isMayorACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) > 0;
    }
    public boolean isIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) == 0;
    }
    public boolean isIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) == 0;
    }
    public boolean isMenorOrIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) <= 0;
    }
    public boolean isMenorOrIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) <= 0;
    }
    public boolean isMayorOrIgual (BigDecimal a, BigDecimal b){
        return a.compareTo(b) >= 0;
    }
    public boolean isMayorOrIgualACero (BigDecimal a){
        return a.compareTo(BigDecimal.ZERO) >= 0;
    }
}
