package ricostruzione;

public class B3 implements BicubicFunct {

	@Override
	public double value(double t) {
		double res=0;
		res= (t*t*t)/6;
		
		return res;
	}
	
	public double der1val(double t) {
		double res=0;
		res= (t*t)/2;
		
		return res;
	}

}
