package texture;

import objectBase.ObjectBase;

public abstract class TextureBase {

	public TextureBase() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract int getR(double x, double y, double z);
	public abstract int getG(double x, double y, double z);
	public abstract int getB(double x, double y, double z);
	
	// Puntatore Oggetto
	protected ObjectBase objPointer=null;

	public ObjectBase getObjPointer() {
		return objPointer;
	}

	public void setObjPointer(ObjectBase objPointer) {
		this.objPointer = objPointer;
	}
}
