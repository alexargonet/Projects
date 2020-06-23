package ricostruzione;

public class B2 implements BicubicFunct {

	@Override
	public double value(double t) {
		double res=0;
		res= (-3*t*t*t + 3*t*t + 3*t + 1)/6;
		
		return res;
	}
	
	public double der1val(double t) {
		double res=0;
		res= (-3*t*t + 2*t + 1)/2;
		
		return res;
	}

}
