package texture;

import objectBase.ObjectBase;
import utility.Utility;

public class TextureSfera extends TextureBase{

	public TextureSfera() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getR(double x, double y, double z) {
		// TODO Auto-generated method stub
		int res=0;
		ObjectBase objRif = this.getObjPointer();
		int[] result;
		result=objRif.obj2Map( x, y, z);
		if(0<=result[0] && result[0]<=Math.PI/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(0<=result[0] && result[0]<=Math.PI/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		return res;
	}

	@Override
	public int getG(double x, double y, double z) {
		// TODO Auto-generated method stub
		int res=0;
		ObjectBase objRif = this.getObjPointer();
		int[] result;
		result=objRif.obj2Map( x, y, z);
		if(0<=result[0] && result[0]<=Math.PI/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(0<=result[0] && result[0]<=Math.PI/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		return res;
	}

	@Override
	public int getB(double x, double y, double z) {
		// TODO Auto-generated method stub
		int res=0;
		ObjectBase objRif = this.getObjPointer();
		int[] result;
		result=objRif.obj2Map( x, y, z);
		if(0<=result[0] && result[0]<=Math.PI/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(0<=result[0] && result[0]<=Math.PI/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && 0<=result[1] && result[1]<=Math.PI/2){
			res=0;
		}
		else if(Math.PI/2<result[0] && result[0]<=Math.PI && Math.PI/2<result[1] && result[1]<Math.PI){
			res=255;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(Math.PI<result[0] && result[0]<=Math.PI*3/2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && 0<=result[1] && result[1]<=Math.PI/2){
			res=255;
		}
		else if(Math.PI*3/2<result[0] && result[0]<=Math.PI*2 && Math.PI/2<result[1] && result[1]<Math.PI){
			res=0;
		}
		return res;
	}
}
