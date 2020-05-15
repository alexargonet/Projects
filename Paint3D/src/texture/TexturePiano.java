package texture;

public class TexturePiano extends TextureBase{

	public TexturePiano() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getR(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(Math.signum(x*y)>=0){
			if((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50)){
				return 128; 
			}
			else{
				return 255;
			}
		}
		else{
			if(!((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50))){
				return 128; 
			}
			else{
				return 255;
			}
		}
	}

	@Override
	public int getG(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(Math.signum(x*y)>=0){
			if((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50)){
				return 128; 
			}
			else{
				return 255;
			}
		}
		else{
			if(!((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50))){
				return 128; 
			}
			else{
				return 255;
			}
		}
	}

	@Override
	public int getB(double x, double y, double z) {
		// TODO Auto-generated method stub
				return 255;
	}

}
