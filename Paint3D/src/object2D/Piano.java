package object2D;

import objectBase.ObjectBase;
import objectBase.IdObj;
import paintimage.PointResEqua;
import paintimage.PointResObj;
import utility.Utility;

public class Piano extends ObjectBase{
	
	public Piano(int Id, double xxc, double yyc, double zzc, double alp,
			double bet, double gam, int red, int green, int blue,
			double reflexCoef,double rifrazCoef) {
		super(Id, xxc, yyc, zzc, alp, bet, gam, red, green, blue, reflexCoef,rifrazCoef);
		// TODO Auto-generated constructor stub
	}
	public Piano(int Id, double xxc, double yyc, double zzc, double alp,
			double bet, double gam, int red, int green, int blue,
			double reflexCoef,double rifrazCoef,double[][] matrixEred) {
		super(Id, xxc, yyc, zzc, alp, bet, gam, red, green, blue, reflexCoef,rifrazCoef,matrixEred);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean detect(double xx1, double yy1, double zz1, double xx0,
			double yy0, double zz0,double xinc, double yinc, double zinc, double distmin0, PointResObj pointResSfer,
			IdObj objId, int tipo) {
		// TODO Auto-generated method stub
	 	
	   	double dist=0,distmin=0,det=0;
	   	double av=0,bv=0,cv=0,dv=0,ev=0,kx=0,kz=0;
	   	double kxyI=1000,kzyI=1000,kxyqI=1000,kzyqI=1000;
	   	double kyxI=1000,kzxI=1000,kyxqI=1000,kzxqI=1000;
	   	double kxzI=1000,kyzI=1000,kxzqI=1000,kyzqI=1000;
	   	//double r1mre=0,r2mre=0,r3mre=0,r4mre=0;
	   	double a=0,b=0,c=0,alpha=0;
	   	//double a11=0,a12=0,a13=0,a21=0,a22=0,a23=0,a31=0,a32=0,a33=0;
	   	//double a11I=0,a12I=0,a13I=0,a21I=0,a22I=0,a23I=0,a31I=0,a32I=0,a33I=0;
	   	double xiI=0,yiI=0,ziI=0;
	   	//double xxcI=0,yycI=0,zzcI=0;
	   	double xxLI=0,yyLI=0,zzLI=0;
	   	//double xcs=0,ycs=0,zcs=0,xcsI=0,ycsI=0,zcsI=0;
	   	double xx0I=0,yy0I=0,zz0I=0,xx1I=0,yy1I=0,zz1I=0;
	   	//double xxlI=0,yylI=0,zzlI=0;
	   	//double beta=0,cosalpha=0,teta=0;
	   	double XgI=0,YgI=0,ZgI=0;
	   	double tmp1=0,tmp2=0,tmp3=0,tmp4=0,tmp5=0,tmp6=0;
	   	double[] xvect=new double[15];
	   	boolean breal = false;
	   	int numsol=0,tern=0;
	   	boolean bself=(objId.getId()==this.getIdObj().getId());
	   	PointResEqua pointRes = new PointResEqua();
	   	
	   	//** inizializazione centro oggetto 
	   	double xxc=this.getXxc();
	   	double yyc=this.getYyc();
	   	double zzc=this.getZzc();
	   	double alp=this.getAlp();
	   	double bet=this.getBet();
	   	double gam=this.getGam();
	   	
	   	double a1q=1;
	   	double a2q=1;
	   	double a3q=1;
	   	
	   	double xi=pointResSfer.getXi();
	   	double yi=pointResSfer.getYi();
	   	double zi=pointResSfer.getZi();
	   	double Xr=pointResSfer.getXr();
	   	double Yr=pointResSfer.getYr();
	   	double Zr=pointResSfer.getZr();
	   	double Xg=pointResSfer.getXg();
	   	double Yg=pointResSfer.getYg();
	   	double Zg=pointResSfer.getZg();
	   	
	   	// centro di proiezione in coord trasformate
//	   	xx0I = xx0*a11+yy0*a21+zz0*a31;
//	   	yy0I = xx0*a12+yy0*a22+zz0*a32;
//	   	zz0I = xx0*a13+yy0*a23+zz0*a33;
	   	double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=xx0;
	   	vetIn[1]=yy0;
	   	vetIn[2]=zz0;
	   	//vetOut=MatrVett(rotMatrix,vetIn,3);
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	xx0I = vetOut[0];
	   	yy0I = vetOut[1];
	   	zz0I = vetOut[2];
	   	
	   	// punto sul piano di proiezione in coord trasformate
//	   	xx1I = xx1*a11+yy1*a21+zz1*a31;
//	   	yy1I = xx1*a12+yy1*a22+zz1*a32;
//	   	zz1I = xx1*a13+yy1*a23+zz1*a33;
	   	vetIn[0]=xx1;
	   	vetIn[1]=yy1;
	   	vetIn[2]=zz1;
	   	//vetOut=MatrVett(rotMatrix,vetIn,3);
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	xx1I = vetOut[0];
	   	yy1I = vetOut[1];
	   	zz1I = vetOut[2];
	   	
	   	// centro del solido in coord trasformate
//	   	xxcI = xxc*a11+yyc*a21+zzc*a31;
//	   	yycI = xxc*a12+yyc*a22+zzc*a32;
//	   	zzcI = xxc*a13+yyc*a23+zzc*a33;
	   	
	   	if(Math.abs((xx1I-xx0I))>Utility.EPSM){//soluzione in X
	   		kyxI = (yy1I-yy0I)/(xx1I-xx0I);
	   		kzxI = (zz1I-zz0I)/(xx1I-xx0I);
	   		if(Math.abs(kyxI)<=1 && Math.abs(kzxI)<=1)
	   			tern += 1;
	   	}
	   	if(Math.abs((yy1I-yy0I))>Utility.EPSM){//soluzione in Y
	   		kxyI = (xx1I-xx0I)/(yy1I-yy0I);
	   		kzyI = (zz1I-zz0I)/(yy1I-yy0I);
	   		if(Math.abs(kxyI)<=1 && Math.abs(kzyI)<=1)
	   			tern += 2;
	   	}
	   	if(Math.abs((zz1I-zz0I))>Utility.EPSM){//soluzione in Z
	   		kxzI = (xx1I-xx0I)/(zz1I-zz0I);
	   		kyzI = (yy1I-yy0I)/(zz1I-zz0I);
	   		if(Math.abs(kxzI)<=1 && Math.abs(kyzI)<=1)
	   			tern += 4;
	   	}
	   	if(tern==0){
	   		pointResSfer.setXi(xi);
	       	pointResSfer.setYi(yi);
	       	pointResSfer.setZi(zi);
	       	pointResSfer.setXr(Xr);
	       	pointResSfer.setYr(Yr);
	       	pointResSfer.setZr(Zr);
	       	pointResSfer.setXg(Xg);
	       	pointResSfer.setYg(Yg);
	       	pointResSfer.setZg(Zg);
	   		return false;
	   	}
	   	
	   	distmin = distmin0;
	   	
	   	xiI=kxyI * (yycI-yy0I) + xx0I;
	   	yiI=yycI;
	   	ziI=kzyI * (yycI-yy0I) + zz0I;
	   	
	   	breal=isInterno(xiI,ziI,xxcI,zzcI);
	   	
	   	if(breal){
	   		breal=false;
		   	dist = Math.sqrt(Math.pow(xiI-xx0I,2.0)+Math.pow(yiI-yy0I,2.0)+Math.pow(ziI-zz0I,2.0));
			// centro sfera passante per il punto incidente in coord originali
			XgI = 0;
			YgI = -1;
			ZgI = 0;
			
			if(tipo==Utility.REFLEX){
				if(((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)>0){
					XgI = 0;
					YgI = 1;
					ZgI = 0;
				}
				if(dist<distmin && /*((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 &&*/ ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>2))){
					distmin = dist;
					breal = true;
					//numsol = 3;
				}
			}
			else if(tipo==Utility.TRASP){
				if(((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)>0){
					XgI = 0;
					YgI = 1;
					ZgI = 0;
				}
   				if(dist<distmin && /*((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 &&*/ ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>Utility.EPS))){
   					distmin = dist;
   					breal = true;
   					//numsol = 1;
   				}
   			}
			else if(tipo==Utility.OMBRA){
//				double xincI = xinc*a11+yinc*a21+zinc*a31;
//				double yincI = xinc*a12+yinc*a22+zinc*a32;
//				double zincI = xinc*a13+yinc*a23+zinc*a33;
				vetIn[0]=xinc;
			   	vetIn[1]=yinc;
			   	vetIn[2]=zinc;
			   	//vetOut=MatrVett(rotMatrix,vetIn,3);
			   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
			   	double xincI = vetOut[0];
			   	double yincI = vetOut[1];
			   	double zincI = vetOut[2];
//				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (!bself || (dist>=Utility.EPSM))){
//					distmin = dist;
//					breal = true;
//					//numsol = 3;
//				}// punto cento proiezione appartenente all'oggetto
//				else if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (((xx1I-xx0I)*XgI+(yy1I-yy0I)*YgI+(zz1I-zz0I)*ZgI)*(xincI*XgI+yincI*YgI+zincI*ZgI))>0 && (!bself || (dist<Utility.EPSM))){
//					distmin = dist;
//					breal = true;
//				}
				//   (((xx1I-xx0I)*XgI+(yy1I-yy0I)*YgI+(zz1I-zz0I)*ZgI)*(xincI*XgI+yincI*YgI+zincI*ZgI))>0 indica se la componente luce e vettore incidente, lungo la direzione del gradiente, hanno segno concorde
				//   ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0  indica che il punto trovato è tra il centro diproiezione relativa e il punto luce.(cade nel segmento luce= punto luce - punto incidente(o centro di proiezione relativo))
				breal = false;
				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>=Utility.EPS2))){
					distmin = dist;
					breal = true;
					//numsol = 3;
				}// punto cento proiezione appartenente all'oggetto
				else if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (((xx1I-xx0I)*XgI+(yy1I-yy0I)*YgI+(zz1I-zz0I)*ZgI)*(xincI*XgI+yincI*YgI+zincI*ZgI))>0 && (!bself || (dist<Utility.EPS2))){
					distmin = dist;
					breal = true;
				}
			}
		   	
//			xi = xiI*a11I+yiI*a21I+ziI*a31I;
//			yi = xiI*a12I+yiI*a22I+ziI*a32I;
//			zi = xiI*a13I+yiI*a23I+ziI*a33I;
			vetIn[0]=xiI;
		   	vetIn[1]=yiI;
		   	vetIn[2]=ziI;
		   	//vetOut=MatrVett(rotMatrixInv,vetIn,3);
		   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);
		   	xi = vetOut[0];
		   	yi = vetOut[1];
		   	zi = vetOut[2];
			
			if(breal && zi>=0){
				
//				Xg = XgI*a21I;
//				Yg = YgI*a22I;
//				Zg = -a23I;
				
//				Xg = YgI*a21I;
//				Yg = YgI*a22I;
//				Zg =YgI*a23I;
				vetIn[0]=XgI;
			   	vetIn[1]=YgI;
			   	vetIn[2]=ZgI;
			   	//vetOut=MatrVett(rotMatrixInv,vetIn,3);
			   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);
			   	Xg = vetOut[0];
			   	Yg = vetOut[1];
			   	Zg = vetOut[2];
//			   	if(tipo==Utility.TRASP){
//	   				Xg = -Xg;
//	   	   			Yg = -Yg;
//	   	   			Zg = -Zg;
//	   			}
				
				alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg))*2.0;
				//alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg)/moduloVettInc)*2.0;
				Xr = - alpha*Xg + (xi-xx0);
				Yr = - alpha*Yg + (yi-yy0);
				Zr = - alpha*Zg + (zi-zz0);
				
			}
			else
				breal=false;
	   	}
		pointResSfer.setXi(xi);
	   	pointResSfer.setYi(yi);
	   	pointResSfer.setZi(zi);
	   	pointResSfer.setXr(Xr);
	   	pointResSfer.setYr(Yr);
	   	pointResSfer.setZr(Zr);
	   	pointResSfer.setXg(Xg);
	   	pointResSfer.setYg(Yg);
	   	pointResSfer.setZg(Zg);
		return breal;
	}

	
	
//	@Override
//	public int getR(double x, double y, double z) {
//		// TODO Auto-generated method stub
//		if(Math.signum(x*y)>=0){
//			if((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50)){
//				return 128; 
//			}
//			else{
//				return 255;
//			}
//		}
//		else{
//			if(!((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50))){
//				return 128; 
//			}
//			else{
//				return 255;
//			}
//		}
//	}
//
//	@Override
//	public int getG(double x, double y, double z) {
//		// TODO Auto-generated method stub
//		if(Math.signum(x*y)>=0){
//			if((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50)){
//				return 128; 
//			}
//			else{
//				return 255;
//			}
//		}
//		else{
//			if(!((Math.abs(Math.round(x))%100<50 && Math.abs(Math.round(y))%100<50) || (Math.abs(Math.round(x))%100>=50 && Math.abs(Math.round(y))%100>=50))){
//				return 128; 
//			}
//			else{
//				return 255;
//			}
//		}
//	}
//
//	@Override
//	public int getB(double x, double y, double z) {
//		// TODO Auto-generated method stub
//				return 255;
//	}
	
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
	
	public boolean isInterno(double xiI, double ziI,double xxcI, double zzcI){
		boolean breal=false;
		breal=true;
		return breal;
	}
	
	@Override
	public void initMapping(){
//		int Wi=(int)(bas/100);
//		int Hi=(int)(alt/100);
//		this.mapR = new double[Wi][Hi];
//		this.mapG = new double[Wi][Hi];
//		this.mapB = new double[Wi][Hi];
	}
	
	@Override
	public double[] getObjMapping(double x,double y,double z){
		double[] result = new double[3];
		result[0]=0;
		result[1]=0;
		result[2]=0;
		
//		double[] vetOut;
//	   	double[] vetIn = new double[3];
//	   	vetIn[0]=x;
//	   	vetIn[1]=y;
//	   	vetIn[2]=z;
//	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
//	   	double xI = vetOut[0] + bas/2;
//	   	double yI = vetOut[1];
//	   	double zI = vetOut[2] + alt/2;
//	   	
//	   	int xm = (int) Math.floor(xI);
//	   	int ym = (int) Math.floor(zI);
//	   	
//	   	result[0]=this.mapR[xm][ym];
//	   	result[1]=this.mapG[xm][ym];
//	   	result[2]=this.mapB[xm][ym];
		
		return result;
	}
	
	@Override
	public boolean setObjMapping(double x,double y,double z,double colR,double colG,double colB){
//		double[] result = new double[3];
//		result[0]=colR;
//		result[1]=colG;
//		result[2]=colB;
//		
//		double[] vetOut;
//	   	double[] vetIn = new double[3];
//	   	vetIn[0]=x;
//	   	vetIn[1]=y;
//	   	vetIn[2]=z;
//	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
//	   	double xI = vetOut[0] + bas/2;
//	   	double yI = vetOut[1];
//	   	double zI = vetOut[2] + alt/2;
//	   	
//	   	int xm = (int) Math.floor(xI);
//	   	int ym = (int) Math.floor(zI);
//	   	
//	   	this.mapR[xm][ym]=result[0];
//	   	this.mapG[xm][ym]=result[1];
//	   	this.mapB[xm][ym]=result[2];
		
		return true;
	}
	
	public int[] obj2Map(double x,double y,double z){
		int[] result = new int[2];
		result[0]=0;
		result[1]=0;
		
//		double[] vetOut;
//	   	double[] vetIn = new double[3];
//	   	vetIn[0]=x;
//	   	vetIn[1]=y;
//	   	vetIn[2]=z;
//	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
//	   	double xI = vetOut[0];
//	   	double yI = vetOut[1];
//	   	double zI = vetOut[2];
//	   	
//	   	//double ym = (zI - this.zzcI)/Math.sqrt((xI - this.xxcI)*(xI - this.xxcI)+(yI - this.yycI)*(yI - this.yycI)+(zI - this.zzcI)*(zI - this.zzcI));
//	   	double ymt=0;
//	   	if((zI - this.zzcI)>=0)
//	   		ymt = (Math.acos((zI - this.zzcI)/r));
//	   	else if((zI - this.zzcI)<0)
//	   		ymt = (Math.PI - Math.acos((zI - this.zzcI)/r));
//	   	
//		double xmt=0;
//	   	
//		if((yI - this.yycI)>=0)
//			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI)));
//		else if((yI - this.yycI)<0)
//			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI))+2*Math.PI);
//		
//	   	
//		result[0]=xmt;
//		result[1]=ymt;
		
		return result;
	}
	
	public double[] map2Obj(int j,int i){
		double[] result = new double[3];
		result[0]=0;
		result[1]=0;
		result[2]=0;
		
		return result;
	}
	
	public double[] coordPolari(double x,double y,double z){
		double[] result = new double[2];
		result[0]=0;
		result[1]=0;
		
		return result;
	}

}
