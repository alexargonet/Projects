package ricostruzione;

public class B2 implements BicubicFunct {

	@Override
	public double value(double t) {
		double res=0;
		res= (-3*t*t*t + 3*t*t + 3*t + 1)/6;
		
		return res;
	}

}
