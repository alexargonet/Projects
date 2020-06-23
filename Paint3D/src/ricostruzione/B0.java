package ricostruzione;

public class B0 implements BicubicFunct {
	
	public double value(double t){
		double res=0;
		res= ((1-t)*(1-t)*(1-t))/6;
		
		return res;
	}
	
	public double der1val(double t){
		double res=0;
		res= -((1-t)*(1-t))/2;
		
		return res;
	}
}
