package paintimage;

import objectBase.IdObj;
import objectBase.ObjectBase;
import utility.Utility;

public class Scena3D {
	public Scena3D(double xrif, double yrif, double zrif,double x0I, double y0I, double z0I,double xw, double yh,double alp,double bet,double gam) {
		// coordinate riferimento in coord globali (angolo in alto a sinistra del fotogramma) 
		this.xrif = xrif;
		this.yrif = yrif;
		this.zrif = zrif;
		// centro proiezione coordinate locali
		this.x0I = x0I;
		this.y0I = y0I;
		this.z0I = z0I;
		this.xw = xw;
		this.yh = yh;
		this.alp = alp;
	   	this.bet = bet;
	   	this.gam = gam;
	   	calcMatrix(alp,bet,gam);
	   	double[] vout;
	   	vout=img2fix(x0I,y0I,z0I);
	   	x0 = vout[0];
		y0 = vout[1];
		z0 = vout[2];
	   	 
	}
	// Camera
	double x0I,y0I,z0I;// centro di proiezione in coordinate Locali
	private double x0,y0,z0;// centro di proiezione in coordinate globali
	double xrif,yrif,zrif;// angolo in alto a sinistra della camera o fotogramma (origine camera) in coordinate Globali
	double xw,yh;// dimensione fotogramma
	// angoli di rotazione rispetto agli assi Z,Y,X rispettivamente
	double alp = 0;
	double bet = 0;
	double gam = 0;
	// Matrici di Rotazione
	protected double[][] rotMatrix = new double[3][3];
	protected double[][] rotMatrixInv = new double[3][3];
	
	
	public void calcMatrix(double alp,double bet,double gam){
		double tmp1=0,tmp2=0,tmp3=0,tmp4=0,tmp5=0;
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
	
	// dai pixel x,y del fotogramma della Camera ai punti 3D rispetto al riferimento fisso.
	public double[] img2fix(double x,double y,double z){
		double[] vin = new double[3];
		double[] vout = new double[3];
		vin[0]=x;
		vin[1]=y;
		vin[2]=z;
		// dai punti in coord locali a quelli Globali
		vout = Utility.MatrVett(rotMatrix,vin,3);
		vout[0]=xrif + vout[0];
		vout[1]=yrif + vout[1];
		vout[2]=zrif + vout[2];
		return vout;
	}
	// da un punto nella scena 3D alla sua proiezione nel fotogramma secondo il centro di proiezione
	public double[] proiezioneInv(double x,double y, double z){
		double[] vin = new double[3];
		double[] vout = new double[3];
		double kxzI,kyzI;
		vin[0]=x-xrif;
		vin[1]=y-yrif;
		vin[2]=z-zrif;
		// dai punti in coord Globali a quelli Locali
		vout = Utility.MatrVett(rotMatrixInv,vin,3);
		double x1I=vout[0];
		double y1I=vout[1];
		double z1I=vout[2];
		double dist=Math.sqrt((x1I-x0I)*(x1I-x0I)+(y1I-y0I)*(y1I-y0I)+(z1I-z0I)*(z1I-z0I));
		if(dist>Utility.EPS){
			kxzI = (x1I-x0I)/(z1I-z0I);
			kyzI = (y1I-y0I)/(z1I-z0I);
			vout[0] = -z0I*kxzI+x0I; // j
		   	vout[1] = -z0I*kyzI+y0I; // i
		   	vout[2] = 0;
		}
		else{
			vout[0] = x1I; // j
		   	vout[1] = y1I; // i
		   	vout[2] = 0;
		}
	   	 	   			
		return vout;
	}
	// dai pixel x,y del fotogramma della Camera ai punti 3D dell'oggetto che incontra.
		public double[] img2Obj(double x,double y,double z,double distmin0,ObjectBase objtmp){
			PointResObj pointResObj = new PointResObj();
			double[] vin = new double[3];
			double[] vout = new double[3];
			boolean btmp=false;
			vin[0]=x;
			vin[1]=y;
			vin[2]=z;
			// dai punti in coord locali a quelli Globali
			vout = Utility.MatrVett(rotMatrix,vin,3);
			double x1=xrif + vout[0];
			double y1=yrif + vout[1];
			double z1=zrif + vout[2];
    		
    		double Xr=x1-x0;
    		double Yr=y1-y0;
    		double Zr=z1-z0;
			
			pointResObj.setXi(0);
			pointResObj.setYi(0);
			pointResObj.setZi(0);
			pointResObj.setXr(0);
			pointResObj.setYr(0);
			pointResObj.setZr(0);
			pointResObj.setXg(0);
			pointResObj.setYg(0);
			pointResObj.setZg(0);
			btmp = objtmp.detect(x1,y1,z1,x0,y0,z0,Xr,Yr,Zr,distmin0,pointResObj,new IdObj(0),Utility.REFLEX);
			if(btmp){
				vout[0]=pointResObj.getXi();
				vout[1]=pointResObj.getYi();
				vout[2]=pointResObj.getZi();
			}
			return vout;
		}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	public double getZ0() {
		return z0;
	}

	public void setZ0(double z0) {
		this.z0 = z0;
	}
	
}
