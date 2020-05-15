package object2D;

import utility.Utility;

public class Rettangolo extends Piano {

	private double bas;
	private double alt;
	public Rettangolo(int Id, double xxc, double yyc, double zzc, double bas,
			double alt, double alp, double bet, double gam, int red, int green,
			int blue, double reflexCoef,double rifrazCoef) {
		super(Id, xxc, yyc, zzc, alp, bet, gam, red, green, blue, reflexCoef, rifrazCoef);
		// TODO Auto-generated constructor stub
		this.bas=bas;
		this.alt=alt;
		this.Wscan=bas;
		this.Hscan=alt;
		// inizialializzo la mappatura dell'oggetto solo se l'oggetto non è trasparente
		if(rifrazCoef<=0)
			initMapping();
	}
	public Rettangolo(int Id, double xxc, double yyc, double zzc, double bas,
			double alt, double alp, double bet, double gam, int red, int green,
			int blue, double reflexCoef,double rifrazCoef,double[][] matrixEred) {
		super(Id, xxc, yyc, zzc, alp, bet, gam, red, green, blue, reflexCoef, rifrazCoef,matrixEred);
		// TODO Auto-generated constructor stub
		this.bas=bas;
		this.alt=alt;
		// inizialializzo la mappatura dell'oggetto solo se l'oggetto non è trasparente
		if(rifrazCoef<=0)
			initMapping();
	}
	public double getBas() {
		return bas;
	}

	public void setBas(double bas) {
		this.bas = bas;
	}

	public double getAlt() {
		return alt;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}
	
	@Override
	public boolean isInterno(double xiI, double ziI,double xxcI, double zzcI){
		boolean breal=false;
		if(bas>0 && alt>0){
			if((Math.abs(xiI-xxcI)-(bas/2.0)<=Utility.EPS) && (Math.abs(ziI-zzcI)-(alt/2.0)<=Utility.EPS))
				breal=true;
			else
				breal=false;
		}
		else if(bas>0 && alt<=0){
			if((Math.abs(xiI-xxcI)-(bas/2.0)<=Utility.EPS))
				breal=true;
			else
				breal=false;
		}
		else if(bas<=0 && alt>0){
			if((Math.abs(ziI-zzcI)-(alt/2.0)<=Utility.EPS))
				breal=true;
			else
				breal=false;
		}
		else// piano infinito
			breal=true;
		
		return breal;
	
	}
	
	@Override
	public int getR(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(textureObj!=null)
			return textureObj.getR(x, y, z);
		else
			return this.getRed();
	}

	@Override
	public int getG(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(textureObj!=null)
			return textureObj.getG(x, y, z);
		else
			return this.getGreen();
	}

	@Override
	public int getB(double x, double y, double z) {
		// TODO Auto-generated method stub
		if(textureObj!=null)
			return textureObj.getB(x, y, z);
		else
			return this.getBlue();
	}
	
	@Override
	public void initMapping(){
		int Wi=(int)(Wscan);
		int Hi=(int)(Hscan);
		this.mapR = new double[Wi][Hi];
		this.mapG = new double[Wi][Hi];
		this.mapB = new double[Wi][Hi];
		for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				this.mapR[j][i]=0;
				this.mapG[j][i]=0;
				this.mapB[j][i]=0;
    		}
    	}
	}
	
	@Override
	public double[] getObjMapping(double x,double y,double z){
		double[] result = new double[3];
		result[0]=0;
		result[1]=0;
		result[2]=0;
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=x;
	   	vetIn[1]=y;
	   	vetIn[2]=z;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	double xI = vetOut[0] - (this.xxcI - bas/2);
	   	double yI = vetOut[1];
	   	double zI = (this.zzcI + alt/2) - vetOut[2] ;
	   	
	   	int xm = (int) Math.round(xI);
	   	int ym = (int) Math.round(zI);
	   	
	   	if(0<=xm && xm<(int)bas && 0<=ym && ym<(int)alt){
		   	result[0]=this.mapR[xm][ym];
		   	result[1]=this.mapG[xm][ym];
		   	result[2]=this.mapB[xm][ym];
	   	}
		
		return result;
	}
	
	@Override
	public boolean setObjMapping(double x,double y,double z,double colR,double colG,double colB){
//		double[] result = new double[3];
//		result[0]=colR;
//		result[1]=colG;
//		result[2]=colB;
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=x;
	   	vetIn[1]=y;
	   	vetIn[2]=z;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	double xI = vetOut[0] - (this.xxcI - bas/2);
	   	double yI = vetOut[1];
	   	double zI = (this.zzcI + alt/2) - vetOut[2] ;
	   	
	   	int xm = (int) Math.round(xI);
	   	int ym = (int) Math.round(zI);
	   	
	   	//double xmFrac = xm - xI;
	   	//double ymFrac = ym - zI;
	   	
	   	
	   	if(0<=xm && xm<(int)bas && 0<=ym && ym<(int)alt){
	   	   	this.mapR[xm][ym]=colR;
		   	this.mapG[xm][ym]=colG;
		   	this.mapB[xm][ym]=colB;
	   	}
		
		return true;
	}
	
	public int[] obj2Map(double x,double y,double z){
		int[] result = new int[2];
		result[0]=0;
		result[1]=0;
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=x;
	   	vetIn[1]=y;
	   	vetIn[2]=z;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	double xI = vetOut[0] - (this.xxcI - bas/2);
	   	double yI = vetOut[1];
	   	double zI = (this.zzcI + alt/2) - vetOut[2] ;
	   	
	   	int xm = (int) Math.round(xI);
	   	int ym = (int) Math.round(zI);
	   	
	   	result[0]=xm;
	   	result[1]=ym;
		
		return result;
	}
	
	public double[] map2Obj(int x,int y){
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]= x + (this.xxcI - bas/2);
	   	vetIn[1]= this.yycI;
	   	vetIn[2]= (this.zzcI + alt/2) - y;
	   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);   	
	   			
		return vetOut;
	}
	
	
}