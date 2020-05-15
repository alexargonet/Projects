package objectBase;

import paintimage.PointResObj;
import texture.TextureBase;
import utility.Utility;

abstract public class ObjectBase implements ObjInterface{
	//** coordinate centro oggetto 3D
	private double xxc,yyc,zzc;
	//** coordinate centro oggetto 3D in Coordinate locali
	protected double xxcI=0,yycI=0,zzcI=0;
	
	// angoli di rotazione rispetto agli assi Z,Y,X rispettivamente
	private double alp = 0;
	private double bet = 0;
	private double gam = 0;
	// colore primario oggetto
	private int red=0;
	private int green=0;
	private int blue=0;
	
	private int[][] arrayImg=null;
	// vettore luce trasmessa incidente
	private double[][] xVLT=null;
	private double[][] yVLT=null;
	private double[][] zVLT=null;
	// Mapping obj - mappa dei colori dati dalla radiosità
	protected double[][] mapR=null;
	protected double[][] mapG=null;
	protected double[][] mapB=null;
	// puntatore oggetto Texture
	protected TextureBase textureObj = null;
	// dimensioni di scanning oggetto
	protected double Wscan=0;
	protected double Hscan=0;
	
	private double reflexCoef=0.50;// coefficente di riflessione
	private double rifrazCoef=0.0;// indice di Rifrazione se = 0 non c'è trasparenza
	// identificativo oggetto
	private IdObj IdObj=null; 
	// matrici di rotazione proprie dell'oggetto
	protected double[][] rotMatrix = new double[3][3];
	protected double[][] rotMatrixInv = new double[3][3];
	// matrici di rotazione globale esterna
	protected double[][] rotMatrixEred = new double[3][3]; //** matrice ereditata oggetto composto
	protected double[][] rotMatrixEredInv = new double[3][3]; //** matrice ereditata oggetto composto Inv
	
	public ObjectBase(int Id, double xxc, double yyc, double zzc,double alp,double bet,double gam,int red,int green,int blue,double reflexCoef,double rifrazCoef) {
		this.IdObj = new IdObj(Id);
		this.xxc = xxc;
		this.yyc = yyc;
		this.zzc = zzc;
		this.alp = alp;
	   	this.bet = bet;
	   	this.gam = gam;
	   	this.red = red;
	   	this.green = green;
	   	this.blue = blue;
	   	this.reflexCoef = reflexCoef;
	   	this.rifrazCoef=rifrazCoef;
	   	calcMatrix(alp,bet,gam);
	   	initializeTrasf();
	}
	// costruttore con matrice rotazione ereditata per oggetti composti con coeff trasp
		public ObjectBase(int Id, double xxc, double yyc, double zzc,double alp,double bet,double gam,int red,int green,int blue,double reflexCoef,double traspCoef,double[][] rotMatrixEred) {
			this.IdObj = new IdObj(Id);
			this.xxc = xxc;
			this.yyc = yyc;
			this.zzc = zzc;
			this.alp = alp;
		   	this.bet = bet;
		   	this.gam = gam;
		   	this.red = red;
		   	this.green = green;
		   	this.blue = blue;
		   	this.reflexCoef = reflexCoef;
		   	this.rifrazCoef=traspCoef;
		   	this.rotMatrixEred = rotMatrixEred;
		   	Utility.MatrTrasp(this.rotMatrixEred,this.rotMatrixEredInv,3);
		   	calcMatrix(alp,bet,gam);
		   	//rotMatrix=MatrProd(rotMatrix,this.rotMatrixEred,3);
		   	rotMatrix=Utility.MatrProd(this.rotMatrixEred,rotMatrix,3);
		   	//rotMatrixInv=MatrProd(this.rotMatrixEredInv,rotMatrixInv,3);
		   	//rotMatrixInv=MatrProd(rotMatrixInv,this.rotMatrixEredInv,3);
		   	Utility.MatrTrasp(this.rotMatrix,this.rotMatrixInv,3);
		   	initializeTrasf();
		   	
		}

	public double getXxc() {
		return xxc;
	}

	public void setXxc(double xxc) {
		this.xxc = xxc;
	}

	public double getYyc() {
		return yyc;
	}

	public void setYyc(double yyc) {
		this.yyc = yyc;
	}

	public double getZzc() {
		return zzc;
	}

	public void setZzc(double zzc) {
		this.zzc = zzc;
	}

	public double getAlp() {
		return alp;
	}

	public void setAlp(double alp) {
		this.alp = alp;
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	public double getGam() {
		return gam;
	}

	public void setGam(double gam) {
		this.gam = gam;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public double getReflexCoef() {
		return reflexCoef;
	}

	public void setReflexCoef(double reflexCoef) {
		this.reflexCoef = reflexCoef;
	}
	
	public int getR(double x, double y, double z) {
		// TODO Auto-generated method stub
		return this.red;
	}

	public int getG(double x, double y, double z) {
		// TODO Auto-generated method stub
		return this.green;
	}

	public int getB(double x, double y, double z) {
		// TODO Auto-generated method stub
		return this.blue;
	}
	
	abstract public boolean detect(double xx1,double yy1,double zz1,double xx0,double yy0,double zz0,double xinc,double yinc,double zinc,double distmin0,PointResObj pointResSfer,IdObj objId,int tipo);
	abstract public boolean isInterno(double xiI, double ziI,double xxcI, double zzcI);
	abstract public void initMapping(); 
	abstract public double[] getObjMapping(double x,double y,double z); 
	abstract public boolean setObjMapping(double x,double y,double z,double colR,double colG,double colB);
	abstract public int[] obj2Map(double x,double y,double z);
	abstract public double[] map2Obj(int x,int y);
	abstract public double[] coordPolari(double x,double y,double z);
	//abstract public int getR(double x,double y,double z);
	//abstract public int getG(double x,double y,double z);
	//abstract public int getB(double x,double y,double z);

	public void calcMatrix(double alp,double bet,double gam){
		double tmp1=0,tmp2=0,tmp3=0,tmp4=0,tmp5=0,tmp6=0;
		double a11=0,a12=0,a13=0,a21=0,a22=0,a23=0,a31=0,a32=0,a33=0;
		double a11I=0,a12I=0,a13I=0,a21I=0,a22I=0,a23I=0,a31I=0,a32I=0,a33I=0;
		
		// matrice di rotazione - trasformazione x,y,z -> xI,yI,zI
		tmp1 = Utility.cos(alp); 
		tmp2 = Utility.cos(bet); 
		a11 = tmp1*tmp2;
		tmp1 = Utility.sin(alp); 
		tmp2 = Utility.cos(gam); 
		tmp3 = Utility.sin(gam); 
		tmp4 = Utility.cos(alp); 
		tmp5 = Utility.sin(bet); 
		//a12 = Utility.sin(alp)*Utility.cos(gam)-Utility.sin(gam)*Utility.cos(alp)*Utility.sin(bet);
		//a12 = tmp1*tmp2-tmp3*tmp4*tmp5;
		a12 = -tmp1*tmp2+tmp3*tmp4*tmp5;
		tmp1 = Utility.sin(alp); 
		tmp2 = Utility.sin(gam); 
		tmp3 = Utility.cos(gam); 
		tmp4 = Utility.cos(alp); 
		tmp5 = Utility.sin(bet); 
		//a13 = Utility.sin(alp)*Utility.sin(gam)+Utility.cos(gam)*Utility.cos(alp)*Utility.sin(bet);
		a13 = tmp1*tmp2+tmp3*tmp4*tmp5;
		tmp1 = Utility.sin(alp); 
		tmp2 = Utility.cos(bet); 
		//a21 = -Utility.sin(alp)*Utility.cos(bet);
		//a21 = -tmp1*tmp2;
		a21 = tmp1*tmp2;
		tmp1 = Utility.cos(alp); 
		tmp2 = Utility.cos(gam); 
		tmp3 = Utility.sin(gam); 
		tmp4 = Utility.sin(alp); 
		tmp5 = Utility.sin(bet); 
		//a22 = Utility.cos(alp)*Utility.cos(gam)+Utility.sin(gam)*Utility.sin(alp)*Utility.sin(bet);
		a22 = tmp1*tmp2+tmp3*tmp4*tmp5;
		tmp1 = Utility.cos(alp); 
		tmp2 = Utility.sin(gam); 
		tmp3 = Utility.cos(gam); 
		tmp4 = Utility.sin(alp); 
		tmp5 = Utility.sin(bet); 
		//a23 = Utility.cos(alp)*Utility.sin(gam)-Utility.cos(gam)*Utility.sin(alp)*Utility.sin(bet);
		//a23 = tmp1*tmp2-tmp3*tmp4*tmp5;
		a23 = -tmp1*tmp2+tmp3*tmp4*tmp5;
		
		a31 = -Utility.sin(bet);
		
		tmp1 = Utility.cos(bet); 
		tmp2 = Utility.sin(gam); 
		//a32 = -Utility.cos(bet)*Utility.sin(gam);
		//a32 = -tmp1*tmp2;
		a32 = tmp1*tmp2;
		
		tmp1 = Utility.cos(bet); 
		tmp2 = Utility.cos(gam); 
		//a33 = Utility.cos(bet)*Utility.cos(gam);
		a33 = tmp1*tmp2;
		
		rotMatrix[0][0]=a11;
		rotMatrix[0][1]=a12;
		rotMatrix[0][2]=a13;
		rotMatrix[1][0]=a21;
		rotMatrix[1][1]=a22;
		rotMatrix[1][2]=a23;
		rotMatrix[2][0]=a31;
		rotMatrix[2][1]=a32;
		rotMatrix[2][2]=a33;
		// matrice di rotazione Inversa - - trasformazione xI,yI,zI -> x,y,z
		a11I = a11;
		a12I = a21;
		a13I = a31;
		a21I = a12;
		a22I = a22;
		a23I = a32;
		a31I = a13;
		a32I = a23;
		a33I = a33;
		
		rotMatrixInv[0][0]=a11I;
		rotMatrixInv[0][1]=a12I;
		rotMatrixInv[0][2]=a13I;
		rotMatrixInv[1][0]=a21I;
		rotMatrixInv[1][1]=a22I;
		rotMatrixInv[1][2]=a23I;
		rotMatrixInv[2][0]=a31I;
		rotMatrixInv[2][1]=a32I;
		rotMatrixInv[2][2]=a33I;
	}
	
//	public void calcMatrix(double alp,double bet,double gam,double[][] matrix){
//		double tmp1=0,tmp2=0,tmp3=0,tmp4=0,tmp5=0,tmp6=0;
//		double a11=0,a12=0,a13=0,a21=0,a22=0,a23=0,a31=0,a32=0,a33=0;
//		double a11I=0,a12I=0,a13I=0,a21I=0,a22I=0,a23I=0,a31I=0,a32I=0,a33I=0;
//		
//		// matrice di rotazione - trasformazione x,y,z -> xI,yI,zI
//		tmp1 = Utility.cos(alp); 
//		tmp2 = Utility.cos(bet); 
//		a11 = tmp1*tmp2;
//		tmp1 = Utility.sin(alp); 
//		tmp2 = Utility.cos(gam); 
//		tmp3 = Utility.sin(gam); 
//		tmp4 = Utility.cos(alp); 
//		tmp5 = Utility.sin(bet); 
//		//a12 = Utility.sin(alp)*Utility.cos(gam)-Utility.sin(gam)*Utility.cos(alp)*Utility.sin(bet);
//		a12 = tmp1*tmp2-tmp3*tmp4*tmp5;
//		tmp1 = Utility.sin(alp); 
//		tmp2 = Utility.sin(gam); 
//		tmp3 = Utility.cos(gam); 
//		tmp4 = Utility.cos(alp); 
//		tmp5 = Utility.sin(bet); 
//		//a13 = Utility.sin(alp)*Utility.sin(gam)+Utility.cos(gam)*Utility.cos(alp)*Utility.sin(bet);
//		a13 = tmp1*tmp2+tmp3*tmp4*tmp5;
//		tmp1 = Utility.sin(alp); 
//		tmp2 = Utility.cos(bet); 
//		//a21 = -Utility.sin(alp)*Utility.cos(bet);
//		a21 = -tmp1*tmp2;
//		tmp1 = Utility.cos(alp); 
//		tmp2 = Utility.cos(gam); 
//		tmp3 = Utility.sin(gam); 
//		tmp4 = Utility.sin(alp); 
//		tmp5 = Utility.sin(bet); 
//		//a22 = Utility.cos(alp)*Utility.cos(gam)+Utility.sin(gam)*Utility.sin(alp)*Utility.sin(bet);
//		a22 = tmp1*tmp2+tmp3*tmp4*tmp5;
//		tmp1 = Utility.cos(alp); 
//		tmp2 = Utility.sin(gam); 
//		tmp3 = Utility.cos(gam); 
//		tmp4 = Utility.sin(alp); 
//		tmp5 = Utility.sin(bet); 
//		//a23 = Utility.cos(alp)*Utility.sin(gam)-Utility.cos(gam)*Utility.sin(alp)*Utility.sin(bet);
//		a23 = tmp1*tmp2-tmp3*tmp4*tmp5;
//		
//		a31 = -Utility.sin(bet);
//		
//		tmp1 = Utility.cos(bet); 
//		tmp2 = Utility.sin(gam); 
//		//a32 = -Utility.cos(bet)*Utility.sin(gam);
//		a32 = -tmp1*tmp2;
//		
//		tmp1 = Utility.cos(bet); 
//		tmp2 = Utility.cos(gam); 
//		//a33 = Utility.cos(bet)*Utility.cos(gam);
//		a33 = tmp1*tmp2;
//		
//		matrix[0][0]=a11;
//		matrix[0][1]=a12;
//		matrix[0][2]=a13;
//		matrix[1][0]=a21;
//		matrix[1][1]=a22;
//		matrix[1][2]=a23;
//		matrix[2][0]=a31;
//		matrix[2][1]=a32;
//		matrix[2][2]=a33;
//		// matrice di rotazione Inversa - - trasformazione xI,yI,zI -> x,y,z
////		a11I = a11;
////		a12I = a21;
////		a13I = a31;
////		a21I = a12;
////		a22I = a22;
////		a23I = a32;
////		a31I = a13;
////		a32I = a23;
////		a33I = a33;
////		
////		rotMatrixInv[0][0]=a11I;
////		rotMatrixInv[0][1]=a12I;
////		rotMatrixInv[0][2]=a13I;
////		rotMatrixInv[1][0]=a21I;
////		rotMatrixInv[1][1]=a22I;
////		rotMatrixInv[1][2]=a23I;
////		rotMatrixInv[2][0]=a31I;
////		rotMatrixInv[2][1]=a32I;
////		rotMatrixInv[2][2]=a33I;
//	}
	
	public void initializeTrasf(){
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=xxc;
	   	vetIn[1]=yyc;
	   	vetIn[2]=zzc;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	xxcI = vetOut[0];
	   	yycI = vetOut[1];
	   	zzcI = vetOut[2];
	}

	public double getRifrazCoef() {
		return rifrazCoef;
	}

	public void setRifrazCoef(double rifrazCoef) {
		this.rifrazCoef = rifrazCoef;
	}
	public int[][] getArrayImg() {
		return arrayImg;
	}
	public void setArrayImg(int[][] arrayImg) {
		this.arrayImg = arrayImg;
	}
	public double[][] getxVLT() {
		return xVLT;
	}
	public void setxVLT(double[][] xVLT) {
		this.xVLT = xVLT;
	}
	public double[][] getyVLT() {
		return yVLT;
	}
	public void setyVLT(double[][] yVLT) {
		this.yVLT = yVLT;
	}
	public double[][] getzVLT() {
		return zVLT;
	}
	public void setzVLT(double[][] zVLT) {
		this.zVLT = zVLT;
	}
	public IdObj getIdObj() {
		return IdObj;
	}
	public void setIdObj(IdObj idObj) {
		IdObj = idObj;
	}
	public TextureBase getTextureObj() {
		return textureObj;
	}
	public void setTextureObj(TextureBase textureObj) {
		this.textureObj = textureObj;
		this.textureObj.setObjPointer(this);
	}
	public double getWscan() {
		return Wscan;
	}
	public void setWscan(double wscan) {
		Wscan = wscan;
	}
	public double getHscan() {
		return Hscan;
	}
	public void setHscan(double hscan) {
		Hscan = hscan;
	}

	


}
