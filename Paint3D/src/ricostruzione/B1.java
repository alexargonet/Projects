package ricostruzione;

public class B1 implements BicubicFunct {

	@Override
	public double value(double t) {
		double res=0;
		res= (3*t*t*t - 6*t*t + 4)/6;
		
		return res;
	}
	
	public double der1val(double t) {
		double res=0;
		res= (3*t*t - 4*t)/2;
		
		return res;
	}

}
