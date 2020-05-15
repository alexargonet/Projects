package object3D;

import paintimage.PointResEqua;
import paintimage.PointResObj;
import objectBase.IdObj;
import utility.Utility;

import java.awt.Color;
import java.lang.Math;

import objectBase.ObjectBase;

public class Sfera extends ObjectBase{
	
	//** Raggio sfera;
	private double r;
	double scalax=Math.PI;
	double scalay=2;
	public Sfera(int Id,double xxc,double yyc,double zzc,double r,double alp,double bet,double gam,int red,int green,int blue,double reflexCoef,double rifrazCoef) {
		super(Id,xxc,yyc,zzc,alp, bet, gam,red,green,blue,reflexCoef, rifrazCoef);
		this.r=r;
		this.Wscan=2*r*scalax;
		this.Hscan=r*scalay;
		// TODO Auto-generated constructor stub//
		// inizialializzo la mappatura dell'oggetto solo se l'oggetto non è trasparente
		if(rifrazCoef<=0)
			initMapping();
	}
	
	@Override
	public boolean detect(double xx1,double yy1,double zz1,double xx0,double yy0,double zz0,double xinc,double yinc,double zinc,double distmin0,PointResObj pointResSfer,IdObj objId,int tipo){
   	
   	double dist=0,distmin=0,Rq=0;
   	double r1m=0.0,r1p=0.0,r2m=0.0,r2p=0.0;
   	double av=0,bv=0,cv=0;
   	double kxyI=1000,kzyI=1000,kxyqI=1000,kzyqI=1000;
   	double kyxI=1000,kzxI=1000,kyxqI=1000,kzxqI=1000;
   	double kxzI=1000,kyzI=1000,kxzqI=1000,kyzqI=1000;
   	double a=0,b=0,c=0,alpha=0;
   	double xiI=0,yiI=0,ziI=0;
   	double xcs=0,ycs=0,zcs=0,xcsI=0,ycsI=0,zcsI=0;;
   	double xx0I=0,yy0I=0,zz0I=0,xx1I=0,yy1I=0,zz1I=0;
   	double beta=0,teta=0;
   	double XgI=0,YgI=0,ZgI=0;
   	double[] xvect=new double[15];
   	boolean breal = false;
   	int numsol=0,tern=0;
   	boolean bself=(objId.getId()==this.getIdObj().getId());
   	PointResEqua pointRes = new PointResEqua();
   	
   	//** inizializazione centro oggetto 
   	double xxc=this.getXxc();
   	double yyc=this.getYyc();
   	double zzc=this.getZzc();
   	double R=0;//** raggio maggiore non usato in sfera
   	r=this.getR();//** raggio sfera
   	
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
   	

   	Rq = R*R;
//   	xx0I = xx0*a11+yy0*a21+zz0*a31;
//   	yy0I = xx0*a12+yy0*a22+zz0*a32;
//   	zz0I = xx0*a13+yy0*a23+zz0*a33;
   	double[] vetOut;
   	double[] vetIn = new double[3];
   	vetIn[0]=xx0;
   	vetIn[1]=yy0;
   	vetIn[2]=zz0;
   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
   	xx0I = vetOut[0];
   	yy0I = vetOut[1];
   	zz0I = vetOut[2];
   	
   	// punto sul piano di proiezione in coord trasformate
//   	xx1I = xx1*a11+yy1*a21+zz1*a31;
//   	yy1I = xx1*a12+yy1*a22+zz1*a32;
//   	zz1I = xx1*a13+yy1*a23+zz1*a33;
   	vetIn[0]=xx1;
   	vetIn[1]=yy1;
   	vetIn[2]=zz1;
   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
   	xx1I = vetOut[0];
   	yy1I = vetOut[1];
   	zz1I = vetOut[2];
   	
   	// centro del solido in coord trasformate
//   	xxcI = xxc*a11+yyc*a21+zzc*a31;
//   	yycI = xxc*a12+yyc*a22+zzc*a32;
//   	zzcI = xxc*a13+yyc*a23+zzc*a33;

   	// centro di illuminazione in coord trasformate
   	/*
   	xxlI = xl*a11+yl*a21+zl*a31;
   	yylI = xl*a12+yl*a22+zl*a32;
   	zzlI = xl*a13+yl*a23+zl*a33;
   	*/
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
   	if(tern==1 || tern==3 || tern==5){// X,XY,XZ
   		kyxqI = kyxI*kyxI;
   		kzxqI = kzxI*kzxI;
   		a = kyxqI/a1q + kzxqI/a3q + 1/a2q;
   		b = (kyxI*(yy0I-yycI)*2.0)/a1q + (kzxI*(zz0I-zzcI)*2.0)/a3q - (xxcI*2.0)/a2q - ((kyxqI+kzxqI)*xx0I*2.0)/a2q;
   		c = (kyxqI*xx0I*xx0I)/a2q + (kzxqI*xx0I*xx0I)/a2q + (xxcI*xxcI)/a2q - (xx0I*kyxI*(yy0I-yycI)*2.0)/a1q - (xx0I*kzxI*(zz0I-zzcI)*2.0)/a3q + ((yy0I-yycI)*(yy0I-yycI))/a1q + ((zz0I-zzcI)*(zz0I-zzcI))/a3q - (Rq + r*r);
   		av = a;
   		bv = b;
   		cv = c;
   		tern=1;
   	}
   	else if(tern==2 || tern==6){// Y,YZ
   		kxyqI = kxyI*kxyI;
   		kzyqI = kzyI*kzyI;
   		a = kxyqI/a1q + kzyqI/a3q + 1/a2q;
   		b = (kxyI*(xx0I-xxcI)*2.0)/a1q + (kzyI*(zz0I-zzcI)*2.0)/a3q - (yycI*2.0)/a2q - ((kxyqI+kzyqI)*yy0I*2.0)/a2q;
   		c = (kxyqI*yy0I*yy0I)/a2q + (kzyqI*yy0I*yy0I)/a2q + (yycI*yycI)/a2q - (yy0I*kxyI*(xx0I-xxcI)*2.0)/a1q - (yy0I*kzyI*(zz0I-zzcI)*2.0)/a3q + ((xx0I-xxcI)*(xx0I-xxcI))/a1q + ((zz0I-zzcI)*(zz0I-zzcI))/a3q - (Rq + r*r);
   		av = a;
   		bv = b;
   		cv = c;
   		tern=2;
   	}
   	else{// Z
   		kxzqI = kxzI*kxzI;
   		kyzqI = kyzI*kyzI;
   		a = kxzqI/a1q + kyzqI/a3q + 1/a2q;
   		b = (kxzI*(xx0I-xxcI)*2.0)/a1q + (kyzI*(yy0I-yycI)*2.0)/a3q - (zzcI*2.0)/a2q - ((kxzqI+kyzqI)*zz0I*2.0)/a2q;
   		c = (kxzqI*zz0I*zz0I)/a2q + (kyzqI*zz0I*zz0I)/a2q + (zzcI*zzcI)/a2q - (zz0I*kxzI*(xx0I-xxcI)*2.0)/a1q - (zz0I*kyzI*(yy0I-yycI)*2.0)/a3q + ((xx0I-xxcI)*(xx0I-xxcI))/a1q + ((yy0I-yycI)*(yy0I-yycI))/a3q - (Rq + r*r);
   		av = a;
   		bv = b;
   		cv = c;
   		tern=4;
   	}
   	
   	//{
   		// risoluzione equazione
   		
   		r1m=r1p=r2m=r2p=0.0;
   		//EquQuaNew(av,bv,cv,dv,ev,&r1m,&r1p,&r2m,&r2p,&r3m,&r3p,&r4m,&r4p);
   		pointRes.setRv1m(r1m);
       	pointRes.setRv1p(r1p);
       	pointRes.setRv2m(r2m);
       	pointRes.setRv2p(r2p);
   		Utility.EquSec(av,bv,cv,pointRes);
       	r1m=pointRes.getRv1m();
       	r1p=pointRes.getRv1p();
       	r2m=pointRes.getRv2m();
       	r2p=pointRes.getRv2p();
   		// punto si intersezione retta di proiezione-solido in coord trasformate
   		distmin = distmin0;
   		if(Math.abs(r1p)<Utility.EPS2 || Math.abs(Math.abs(r1p)-Math.PI)<(Utility.EPS)){
   			// punto di intersezione retta di proiezione-solido in coord trasformate
   			if(tern==1){
   				xiI = r1m*Math.cos(r1p);
   				yiI = yy0I + (xiI-xx0I)*kyxI;
   				ziI = zz0I + (xiI-xx0I)*kzxI;
   			}
   			else if(tern==2){
   				yiI = r1m*Math.cos(r1p);
   				xiI = xx0I + (yiI-yy0I)*kxyI;
   				ziI = zz0I + (yiI-yy0I)*kzyI;
   			}
   			else if(tern==4){
   				ziI = r1m*Math.cos(r1p);
   				xiI = xx0I + (ziI-zz0I)*kxzI;
   				yiI = yy0I + (ziI-zz0I)*kyzI;
   			}
   			// punto di intersezione retta di proiezione-solido in coord originali
   			dist = Math.sqrt(Math.pow(xiI-xx0I,2.0)+Math.pow(yiI-yy0I,2.0)+Math.pow(ziI-zz0I,2.0));
   			// centro sfera passante per il punto incidente in coord originali
   			if(tipo==Utility.REFLEX){
   				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
   				xcsI = xxcI + R*Math.cos(teta);
   				ycsI = yycI + R*Math.sin(teta);
   				zcsI = zzcI;
   				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
   				XgI = (xiI-xcsI)/(beta*a1q);
   				YgI = (yiI-ycsI)/(beta*a2q);
   				ZgI = (ziI-zcsI)/(beta*a3q);
   				//**************************
   				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>2))){
   					distmin = dist;
   					breal = true;
   					numsol = 1;
   				}
   			}
   			else if(tipo==Utility.TRASP){
   				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
   				xcsI = xxcI + R*Math.cos(teta);
   				ycsI = yycI + R*Math.sin(teta);
   				zcsI = zzcI;
   				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
   			// cambio segno al gradiente perchè sono nell'interno dell'oggetto trasparente
   				XgI = -(xiI-xcsI)/(beta*a1q);
   				YgI = -(yiI-ycsI)/(beta*a2q);
   				ZgI = -(ziI-zcsI)/(beta*a3q);
   				//**************************
   				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>Utility.EPS))){
   					distmin = dist;
   					breal = true;
   					numsol = 1;
   				}
   			}
   			else if(tipo==Utility.OMBRA){
   				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (!bself || (dist>2))){
   					distmin = dist;
   					breal = true;
   					numsol = 1;
   				}
   			}
   			//breal = true;
   		}
   		if(Math.abs(r2p)<Utility.EPS2  || Math.abs(Math.abs(r2p)-Math.PI)<(Utility.EPS)){
   			// punto di intersezione retta di proiezione-solido in coord trasformate
   			if(tern==1){
   				xiI = r2m*Math.cos(r2p);
   				yiI = yy0I + (xiI-xx0I)*kyxI;
   				ziI = zz0I + (xiI-xx0I)*kzxI;
   			}
   			else if(tern==2){
   				yiI = r2m*Math.cos(r2p);
   				xiI = xx0I + (yiI-yy0I)*kxyI;
   				ziI = zz0I + (yiI-yy0I)*kzyI;
   			}
   			else if(tern==4){
   				ziI = r2m*Math.cos(r2p);
   				xiI = xx0I + (ziI-zz0I)*kxzI;
   				yiI = yy0I + (ziI-zz0I)*kyzI;
   			}
   			// punto di intersezione retta di proiezione-solido in coord originali
   			dist = Math.sqrt(Math.pow(xiI-xx0I,2.0)+Math.pow(yiI-yy0I,2.0)+Math.pow(ziI-zz0I,2.0));
   			// centro sfera passante per il punto incidente in coord originali
   			if(tipo==Utility.REFLEX){
   				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
   				xcsI = xxcI + R*Math.cos(teta);
   				ycsI = yycI + R*Math.sin(teta);
   				zcsI = zzcI;
   				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
   				XgI = (xiI-xcsI)/(beta*a1q);
   				YgI = (yiI-ycsI)/(beta*a2q);
   				ZgI = (ziI-zcsI)/(beta*a3q);
   				//**************************
   				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>2))){
   					distmin = dist;
   					breal = true;
   					numsol = 2;
   				}
   			}
   			else if(tipo==Utility.TRASP){
   				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
   				xcsI = xxcI + R*Math.cos(teta);
   				ycsI = yycI + R*Math.sin(teta);
   				zcsI = zzcI;
   				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
   				// cambio segno al gradiente perchè sono nell'interno dell'oggetto trasparente
   				XgI = -(xiI-xcsI)/(beta*a1q);
   				YgI = -(yiI-ycsI)/(beta*a2q);
   				ZgI = -(ziI-zcsI)/(beta*a3q);
   				//**************************
   				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>Utility.EPS))){
   					distmin = dist;
   					breal = true;
   					numsol = 2;
   				}
   			}
   			else if(tipo==Utility.OMBRA){
   				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (!bself || (dist>2))){
   					distmin = dist;
   					breal = true;
   					numsol = 2;
   				}
   			}
   			//breal = true;
   		}
   		
   		// punto di intersezione retta di proiezione-solido in coord originali
   		if(breal){
   			switch(numsol){
   			case 1:
   				if(tern==1){
   					xiI = r1m*Math.cos(r1p);
   					yiI = yy0I + (xiI-xx0I)*kyxI;
   					ziI = zz0I + (xiI-xx0I)*kzxI;
   				}
   				else if(tern==2){
   					yiI = r1m*Math.cos(r1p);
   					xiI = xx0I + (yiI-yy0I)*kxyI;
   					ziI = zz0I + (yiI-yy0I)*kzyI;
   				}
   				else if(tern==4){
   					ziI = r1m*Math.cos(r1p);
   					xiI = xx0I + (ziI-zz0I)*kxzI;
   					yiI = yy0I + (ziI-zz0I)*kyzI;
   				}
   			break;
   			case 2:
   				if(tern==1){
   					xiI = r2m*Math.cos(r2p);
   					yiI = yy0I + (xiI-xx0I)*kyxI;
   					ziI = zz0I + (xiI-xx0I)*kzxI;
   				}
   				else if(tern==2){
   					yiI = r2m*Math.cos(r2p);
   					xiI = xx0I + (yiI-yy0I)*kxyI;
   					ziI = zz0I + (yiI-yy0I)*kzyI;
   				}
   				else if(tern==4){
   					ziI = r2m*Math.cos(r2p);
   					xiI = xx0I + (ziI-zz0I)*kxzI;
   					yiI = yy0I + (ziI-zz0I)*kyzI;
   				}
   			break;
   			}
//   			xi = xiI*a11I+yiI*a21I+ziI*a31I;
//   			yi = xiI*a12I+yiI*a22I+ziI*a32I;
//   			zi = xiI*a13I+yiI*a23I+ziI*a33I;
   			vetIn[0]=xiI;
		   	vetIn[1]=yiI;
		   	vetIn[2]=ziI;
		   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);
		   	xi = vetOut[0];
		   	yi = vetOut[1];
		   	zi = vetOut[2];
   		}
   	//}

   	if(yiI<=(yycI+R+r+1) && yiI>=(yycI-R-r-1) && ziI<=(zzcI+r+1) && ziI>=(zzcI-r-1) && xiI<=(xxcI+R+r+1) && xiI>=(xxcI-R-r-1) && breal && zi>=0){
   		// ricerca centro sfera passante per il punto incidente in coord originali
   		teta = Math.atan2((yiI-yycI),(xiI-xxcI));
   		// centro sfera passante per il punto incidente in coord originali
   		xcsI = xxcI + R*Math.cos(teta);
   		ycsI = yycI + R*Math.sin(teta);
   		zcsI = zzcI;
   		Utility.reset_vect(xvect,15);
   		xvect[0] = xcsI*xcsI;
   		xvect[1] = xiI*xiI;
   		xvect[2] = -2*xcsI*xiI;
   		xvect[3] = ycsI*ycsI;
   		xvect[4] = yiI*yiI;
   		xvect[5] = -2*ycsI*yiI;
   		xvect[6] = zcsI*zcsI;
   		xvect[7] = ziI*ziI;
   		xvect[8] = -2*zcsI*ziI;
   		dist = Utility.kahan_sum(xvect,9);
   		dist = Math.sqrt(dist);
   		if((r-1<=dist) && (dist<=r+1)){
   			// centro sfera passante per il punto incidente in coord originali
//   			xcs = xcsI*a11I+ycsI*a21I+zcsI*a31I;
//   			ycs = xcsI*a12I+ycsI*a22I+zcsI*a32I;
//   			zcs = xcsI*a13I+ycsI*a23I+zcsI*a33I;
   			vetIn[0]=xcsI;
		   	vetIn[1]=ycsI;
		   	vetIn[2]=zcsI;
		   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);
		   	xcs = vetOut[0];
		   	ycs = vetOut[1];
		   	zcs = vetOut[2];
   			// versore gradiente Sfera in coord originali
   			beta = Math.sqrt((xi-xcs)*(xi-xcs)/(a1q*a1q)+(yi-ycs)*(yi-ycs)/(a2q*a2q)+(zi-zcs)*(zi-zcs)/(a3q*a3q));
   			Xg = (xi-xcs)/(beta*a1q);
   			Yg = (yi-ycs)/(beta*a2q);
   			Zg = (zi-zcs)/(beta*a3q);
   			// cambio segno al gradiente perchè sono nell'interno dell'oggetto trasparente
   			if(tipo==Utility.TRASP){
   				Xg = -Xg;
   	   			Yg = -Yg;
   	   			Zg = -Zg;
   			}
   			// vettore riflesso con centro di illuminazione coincidente con il centro di proiezione
   			//double moduloVettInc = Math.sqrt((xi-xx0)*(xi-xx0)+(yi-yy0)*(yi-yy0)+(zi-zz0)*(zi-zz0));
				//alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg)/(Xg*Xg+Yg*Yg+Zg*Zg))*2.0;
   			alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg))*2.0;
				//alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg)/moduloVettInc)*2.0;
   			Xr = - alpha*Xg + (xi-xx0);
   			Yr = - alpha*Yg + (yi-yy0);
   			Zr = - alpha*Zg + (zi-zz0);
   		}
   		else
   			breal = false;
   	}
   	else
   		breal = false;

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
    
//*********************************************************************
//  SFERA BIGDECIMAL
//*********************************************************************    
//boolean Sfera2Big(BigDecimal xxc,BigDecimal yyc,BigDecimal zzc,BigDecimal xx1,BigDecimal yy1,BigDecimal zz1,BigDecimal xx0,BigDecimal yy0,BigDecimal zz0,BigDecimal R,BigDecimal r,BigDecimal distmin0,BigDecimal a1q,BigDecimal a2q,BigDecimal a3q,BigDecimal alp,BigDecimal bet,BigDecimal gam,PointResSfera pointResSfer,boolean bself,int tipo){
//    	
//    	BigDecimal dist,distmin,Rq,det;
//    	BigDecimal r1m,r1p,r2m,r2p,r3m,r3p,r4m,r4p;
//    	BigDecimal av,bv,cv,dv,ev,kx,kz;
//    	BigDecimal kxyI=1000,kzyI=1000,kxyqI=1000,kzyqI=1000;
//    	BigDecimal kyxI=1000,kzxI=1000,kyxqI=1000,kzxqI=1000;
//    	BigDecimal kxzI=1000,kyzI=1000,kxzqI=1000,kyzqI=1000;
//    	BigDecimal r1mre=0,r2mre=0,r3mre=0,r4mre=0;
//    	BigDecimal a=0,b=0,c=0,alpha=0;
//    	BigDecimal a11=0,a12=0,a13=0,a21=0,a22=0,a23=0,a31=0,a32=0,a33=0;
//    	BigDecimal a11I=0,a12I=0,a13I=0,a21I=0,a22I=0,a23I=0,a31I=0,a32I=0,a33I=0;
//    	BigDecimal xiI=0,yiI=0,ziI=0;
//    	BigDecimal xxcI=0,yycI=0,zzcI=0;
//    	BigDecimal xxLI=0,yyLI=0,zzLI=0;
//    	BigDecimal xcs=0,ycs=0,zcs=0,xcsI=0,ycsI=0,zcsI=0;;
//    	BigDecimal xx0I=0,yy0I=0,zz0I=0,xx1I=0,yy1I=0,zz1I=0;
//    	//BigDecimal xxlI=0,yylI=0,zzlI=0;
//    	BigDecimal beta=0,cosalpha=0,teta=0;
//    	BigDecimal XgI=0,YgI=0,ZgI=0;
//    	BigDecimal tmp1=0,tmp2=0,tmp3=0,tmp4=0,tmp5=0,tmp6=0;
//    	BigDecimal[] xvect=new BigDecimal[15];
//    	boolean breal = false;
//    	int numsol=0,tern=0;
//    	PointResEquaSec pointRes = new PointResEquaSec();
//    	
//    	BigDecimal xi=pointResSfer.getXi();
//    	BigDecimal yi=pointResSfer.getYi();
//    	BigDecimal zi=pointResSfer.getZi();
//    	BigDecimal Xr=pointResSfer.getXr();
//    	BigDecimal Yr=pointResSfer.getYr();
//    	BigDecimal Zr=pointResSfer.getZr();
//    	BigDecimal Xg=pointResSfer.getXg();
//    	BigDecimal Yg=pointResSfer.getYg();
//    	BigDecimal Zg=pointResSfer.getZg();
//    	
//
//    	Rq = R.pow(2);//R*R;
//    	// matrice di rotazione - trasformazione x,y,z -> xI,yI,zI
//    	tmp1 = Math.cos(alp.doubleValue()); 
//    	tmp2 = Math.cos(bet.doubleValue()); 
//    	a11 = tmp1.multiply(tmp2);//tmp1*tmp2;
//    	tmp1 = Math.sin(alp.doubleValue()); 
//    	tmp2 = Math.cos(gam.doubleValue()); 
//    	tmp3 = Math.sin(gam.doubleValue()); 
//    	tmp4 = Math.cos(alp.doubleValue()); 
//    	tmp5 = Math.sin(bet.doubleValue()); 
//    	//a12 = Math.sin(alp)*Math.cos(gam)-Math.sin(gam)*Math.cos(alp)*Math.sin(bet);
//    	a12 = tmp1.multiply(tmp2).subtract(tmp3.multiply(tmp4.multiply(tmp5)));//tmp1*tmp2-tmp3*tmp4*tmp5;
//    	tmp1 = Math.sin(alp); 
//    	tmp2 = Math.sin(gam); 
//    	tmp3 = Math.cos(gam); 
//    	tmp4 = Math.cos(alp); 
//    	tmp5 = Math.sin(bet); 
//    	//a13 = Math.sin(alp)*Math.sin(gam)+Math.cos(gam)*Math.cos(alp)*Math.sin(bet);
//    	a13 = tmp1*tmp2+tmp3*tmp4*tmp5;
//    	tmp1 = Math.sin(alp); 
//    	tmp2 = Math.cos(bet); 
//    	//a21 = -Math.sin(alp)*Math.cos(bet);
//    	a21 = -tmp1*tmp2;
//    	tmp1 = Math.cos(alp); 
//    	tmp2 = Math.cos(gam); 
//    	tmp3 = Math.sin(gam); 
//    	tmp4 = Math.sin(alp); 
//    	tmp5 = Math.sin(bet); 
//    	//a22 = Math.cos(alp)*Math.cos(gam)+Math.sin(gam)*Math.sin(alp)*Math.sin(bet);
//    	a22 = tmp1*tmp2+tmp3*tmp4*tmp5;
//    	tmp1 = Math.cos(alp); 
//    	tmp2 = Math.sin(gam); 
//    	tmp3 = Math.cos(gam); 
//    	tmp4 = Math.sin(alp); 
//    	tmp5 = Math.sin(bet); 
//    	//a23 = Math.cos(alp)*Math.sin(gam)-Math.cos(gam)*Math.sin(alp)*Math.sin(bet);
//    	a23 = tmp1*tmp2-tmp3*tmp4*tmp5;
//    	
//    	a31 = -Math.sin(bet);
//    	
//    	tmp1 = Math.cos(bet); 
//    	tmp2 = Math.sin(gam); 
//    	//a32 = -Math.cos(bet)*Math.sin(gam);
//    	a32 = -tmp1*tmp2;
//    	
//    	tmp1 = Math.cos(bet); 
//    	tmp2 = Math.cos(gam); 
//    	//a33 = Math.cos(bet)*Math.cos(gam);
//    	a33 = tmp1*tmp2;
//
//    	// matrice di rotazione Inversa - - trasformazione xI,yI,zI -> x,y,z
//    	a11I = a11;
//    	a12I = a21;
//    	a13I = a31;
//    	a21I = a12;
//    	a22I = a22;
//    	a23I = a32;
//    	a31I = a13;
//    	a32I = a23;
//    	a33I = a33;
//    	
//    	xx0I = xx0*a11+yy0*a21+zz0*a31;
//    	yy0I = xx0*a12+yy0*a22+zz0*a32;
//    	zz0I = xx0*a13+yy0*a23+zz0*a33;
//    	
//    	// punto sul piano di proiezione in coord trasformate
//    	xx1I = xx1*a11+yy1*a21+zz1*a31;
//    	yy1I = xx1*a12+yy1*a22+zz1*a32;
//    	zz1I = xx1*a13+yy1*a23+zz1*a33;
//    	
//    	// centro del solido in coord trasformate
//    	xxcI = xxc*a11+yyc*a21+zzc*a31;
//    	yycI = xxc*a12+yyc*a22+zzc*a32;
//    	zzcI = xxc*a13+yyc*a23+zzc*a33;
//
//    	// centro di illuminazione in coord trasformate
//    	/*
//    	xxlI = xl*a11+yl*a21+zl*a31;
//    	yylI = xl*a12+yl*a22+zl*a32;
//    	zzlI = xl*a13+yl*a23+zl*a33;
//    	*/
//    	if(Math.abs((xx1I-xx0I))>Utility.EPSM){//soluzione in X
//    		kyxI = (yy1I-yy0I)/(xx1I-xx0I);
//    		kzxI = (zz1I-zz0I)/(xx1I-xx0I);
//    		if(Math.abs(kyxI)<=1 && Math.abs(kzxI)<=1)
//    			tern += 1;
//    	}
//    	if(Math.abs((yy1I-yy0I))>Utility.EPSM){//soluzione in Y
//    		kxyI = (xx1I-xx0I)/(yy1I-yy0I);
//    		kzyI = (zz1I-zz0I)/(yy1I-yy0I);
//    		if(Math.abs(kxyI)<=1 && Math.abs(kzyI)<=1)
//    			tern += 2;
//    	}
//    	if(Math.abs((zz1I-zz0I))>Utility.EPSM){//soluzione in Z
//    		kxzI = (xx1I-xx0I)/(zz1I-zz0I);
//    		kyzI = (yy1I-yy0I)/(zz1I-zz0I);
//    		if(Math.abs(kxzI)<=1 && Math.abs(kyzI)<=1)
//    			tern += 4;
//    	}
//    	if(tern==0){
//    		pointResSfer.setXi(xi);
//        	pointResSfer.setYi(yi);
//        	pointResSfer.setZi(zi);
//        	pointResSfer.setXr(Xr);
//        	pointResSfer.setYr(Yr);
//        	pointResSfer.setZr(Zr);
//        	pointResSfer.setXg(Xg);
//        	pointResSfer.setYg(Yg);
//        	pointResSfer.setZg(Zg);
//    		return false;
//    	}
//    	if(tern==1 || tern==3 || tern==5){// X,XY,XZ
//    		kyxqI = kyxI*kyxI;
//    		kzxqI = kzxI*kzxI;
//    		a = kyxqI/a1q + kzxqI/a3q + 1/a2q;
//    		b = (kyxI*(yy0I-yycI)*2.0)/a1q + (kzxI*(zz0I-zzcI)*2.0)/a3q - (xxcI*2.0)/a2q - ((kyxqI+kzxqI)*xx0I*2.0)/a2q;
//    		c = (kyxqI*xx0I*xx0I)/a2q + (kzxqI*xx0I*xx0I)/a2q + (xxcI*xxcI)/a2q - (xx0I*kyxI*(yy0I-yycI)*2.0)/a1q - (xx0I*kzxI*(zz0I-zzcI)*2.0)/a3q + ((yy0I-yycI)*(yy0I-yycI))/a1q + ((zz0I-zzcI)*(zz0I-zzcI))/a3q - (Rq + r*r);
//    		av = a;
//    		bv = b;
//    		cv = c;
//    		tern=1;
//    	}
//    	else if(tern==2 || tern==6){// Y,YZ
//    		kxyqI = kxyI*kxyI;
//    		kzyqI = kzyI*kzyI;
//    		a = kxyqI/a1q + kzyqI/a3q + 1/a2q;
//    		b = (kxyI*(xx0I-xxcI)*2.0)/a1q + (kzyI*(zz0I-zzcI)*2.0)/a3q - (yycI*2.0)/a2q - ((kxyqI+kzyqI)*yy0I*2.0)/a2q;
//    		c = (kxyqI*yy0I*yy0I)/a2q + (kzyqI*yy0I*yy0I)/a2q + (yycI*yycI)/a2q - (yy0I*kxyI*(xx0I-xxcI)*2.0)/a1q - (yy0I*kzyI*(zz0I-zzcI)*2.0)/a3q + ((xx0I-xxcI)*(xx0I-xxcI))/a1q + ((zz0I-zzcI)*(zz0I-zzcI))/a3q - (Rq + r*r);
//    		av = a;
//    		bv = b;
//    		cv = c;
//    		tern=2;
//    	}
//    	else{// Z
//    		kxzqI = kxzI*kxzI;
//    		kyzqI = kyzI*kyzI;
//    		a = kxzqI/a1q + kyzqI/a3q + 1/a2q;
//    		b = (kxzI*(xx0I-xxcI)*2.0)/a1q + (kyzI*(yy0I-yycI)*2.0)/a3q - (zzcI*2.0)/a2q - ((kxzqI+kyzqI)*zz0I*2.0)/a2q;
//    		c = (kxzqI*zz0I*zz0I)/a2q + (kyzqI*zz0I*zz0I)/a2q + (zzcI*zzcI)/a2q - (zz0I*kxzI*(xx0I-xxcI)*2.0)/a1q - (zz0I*kyzI*(yy0I-yycI)*2.0)/a3q + ((xx0I-xxcI)*(xx0I-xxcI))/a1q + ((yy0I-yycI)*(yy0I-yycI))/a3q - (Rq + r*r);
//    		av = a;
//    		bv = b;
//    		cv = c;
//    		tern=4;
//    	}
//    	
//    	//{
//    		// risoluzione equazione
//    		
//    		r1m=r1p=r2m=r2p=r3m=r3p=r4m=r4p=0.0;
//    		//EquQuaNew(av,bv,cv,dv,ev,&r1m,&r1p,&r2m,&r2p,&r3m,&r3p,&r4m,&r4p);
//    		pointRes.setRv1m(r1m);
//        	pointRes.setRv1p(r1p);
//        	pointRes.setRv2m(r2m);
//        	pointRes.setRv2p(r2p);
//    		EquSec(av,bv,cv,pointRes);
//    		r1m=pointRes.getRv1m();
//        	r1p=pointRes.getRv1p();
//        	r2m=pointRes.getRv2m();
//        	r2p=pointRes.getRv2p();
//    		// punto si intersezione retta di proiezione-solido in coord trasformate
//    		distmin = distmin0;
//    		if(Math.abs(r1p)<Utility.EPS2 || Math.abs(Math.abs(r1p)-Math.PI)<(Utility.EPS)){
//    			// punto di intersezione retta di proiezione-solido in coord trasformate
//    			if(tern==1){
//    				xiI = r1m*Math.cos(r1p);
//    				yiI = yy0I + (xiI-xx0I)*kyxI;
//    				ziI = zz0I + (xiI-xx0I)*kzxI;
//    			}
//    			else if(tern==2){
//    				yiI = r1m*Math.cos(r1p);
//    				xiI = xx0I + (yiI-yy0I)*kxyI;
//    				ziI = zz0I + (yiI-yy0I)*kzyI;
//    			}
//    			else if(tern==4){
//    				ziI = r1m*Math.cos(r1p);
//    				xiI = xx0I + (ziI-zz0I)*kxzI;
//    				yiI = yy0I + (ziI-zz0I)*kyzI;
//    			}
//    			// punto di intersezione retta di proiezione-solido in coord originali
//    			dist = Math.sqrt(Math.pow(xiI-xx0I,2.0)+Math.pow(yiI-yy0I,2.0)+Math.pow(ziI-zz0I,2.0));
//    			// centro sfera passante per il punto incidente in coord originali
//    			if(tipo==Utility.REFLEX){
//    				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
//    				xcsI = xxcI + R*Math.cos(teta);
//    				ycsI = yycI + R*Math.sin(teta);
//    				zcsI = zzcI;
//    				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
//    				XgI = (xiI-xcsI)/(beta*a1q);
//    				YgI = (yiI-ycsI)/(beta*a2q);
//    				ZgI = (ziI-zcsI)/(beta*a3q);
//    				//**************************
//    				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>2))){
//    					distmin = dist;
//    					breal = true;
//    					numsol = 1;
//    				}
//    			}
//    			else if(tipo==Utility.OMBRA){
//    				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (!bself || (dist>2))){
//    					distmin = dist;
//    					breal = true;
//    					numsol = 1;
//    				}
//    			}
//    			//breal = true;
//    		}
//    		if(Math.abs(r2p)<Utility.EPS2  || Math.abs(Math.abs(r2p)-Math.PI)<(Utility.EPS)){
//    			// punto di intersezione retta di proiezione-solido in coord trasformate
//    			if(tern==1){
//    				xiI = r2m*Math.cos(r2p);
//    				yiI = yy0I + (xiI-xx0I)*kyxI;
//    				ziI = zz0I + (xiI-xx0I)*kzxI;
//    			}
//    			else if(tern==2){
//    				yiI = r2m*Math.cos(r2p);
//    				xiI = xx0I + (yiI-yy0I)*kxyI;
//    				ziI = zz0I + (yiI-yy0I)*kzyI;
//    			}
//    			else if(tern==4){
//    				ziI = r2m*Math.cos(r2p);
//    				xiI = xx0I + (ziI-zz0I)*kxzI;
//    				yiI = yy0I + (ziI-zz0I)*kyzI;
//    			}
//    			// punto di intersezione retta di proiezione-solido in coord originali
//    			dist = Math.sqrt(Math.pow(xiI-xx0I,2.0)+Math.pow(yiI-yy0I,2.0)+Math.pow(ziI-zz0I,2.0));
//    			// centro sfera passante per il punto incidente in coord originali
//    			if(tipo==Utility.REFLEX){
//    				teta = Math.atan2((yiI-yycI),(xiI-xxcI));
//    				xcsI = xxcI + R*Math.cos(teta);
//    				ycsI = yycI + R*Math.sin(teta);
//    				zcsI = zzcI;
//    				beta = Math.sqrt((xiI-xcsI)*(xiI-xcsI)/(a1q*a1q)+(yiI-ycsI)*(yiI-ycsI)/(a2q*a2q)+(ziI-zcsI)*(ziI-zcsI)/(a3q*a3q));
//    				XgI = (xiI-xcsI)/(beta*a1q);
//    				YgI = (yiI-ycsI)/(beta*a2q);
//    				ZgI = (ziI-zcsI)/(beta*a3q);
//    				//**************************
//    				if(dist<distmin && ((xiI-xx0I)*XgI+(yiI-yy0I)*YgI+(ziI-zz0I)*ZgI)<=0 && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>0 && (!bself || (dist>2))){
//    					distmin = dist;
//    					breal = true;
//    					numsol = 2;
//    				}
//    			}
//    			else if(tipo==Utility.OMBRA){
//    				if(dist<distmin && ((xiI-xx0I)*(xx1I-xx0I)+(yiI-yy0I)*(yy1I-yy0I)+(ziI-zz0I)*(zz1I-zz0I))>=0 && (!bself || (dist>2))){
//    					distmin = dist;
//    					breal = true;
//    					numsol = 2;
//    				}
//    			}
//    			//breal = true;
//    		}
//    		
//    		// punto di intersezione retta di proiezione-solido in coord originali
//    		if(breal){
//    			switch(numsol){
//    			case 1:
//    				if(tern==1){
//    					xiI = r1m*Math.cos(r1p);
//    					yiI = yy0I + (xiI-xx0I)*kyxI;
//    					ziI = zz0I + (xiI-xx0I)*kzxI;
//    				}
//    				else if(tern==2){
//    					yiI = r1m*Math.cos(r1p);
//    					xiI = xx0I + (yiI-yy0I)*kxyI;
//    					ziI = zz0I + (yiI-yy0I)*kzyI;
//    				}
//    				else if(tern==4){
//    					ziI = r1m*Math.cos(r1p);
//    					xiI = xx0I + (ziI-zz0I)*kxzI;
//    					yiI = yy0I + (ziI-zz0I)*kyzI;
//    				}
//    			break;
//    			case 2:
//    				if(tern==1){
//    					xiI = r2m*Math.cos(r2p);
//    					yiI = yy0I + (xiI-xx0I)*kyxI;
//    					ziI = zz0I + (xiI-xx0I)*kzxI;
//    				}
//    				else if(tern==2){
//    					yiI = r2m*Math.cos(r2p);
//    					xiI = xx0I + (yiI-yy0I)*kxyI;
//    					ziI = zz0I + (yiI-yy0I)*kzyI;
//    				}
//    				else if(tern==4){
//    					ziI = r2m*Math.cos(r2p);
//    					xiI = xx0I + (ziI-zz0I)*kxzI;
//    					yiI = yy0I + (ziI-zz0I)*kyzI;
//    				}
//    			break;
//    			}
//    			xi = xiI*a11I+yiI*a21I+ziI*a31I;
//    			yi = xiI*a12I+yiI*a22I+ziI*a32I;
//    			zi = xiI*a13I+yiI*a23I+ziI*a33I;
//    		}
//    	//}
//
//    	if(yiI<=(yycI+R+r+1) && yiI>=(yycI-R-r-1) && ziI<=(zzcI+r+1) && ziI>=(zzcI-r-1) && xiI<=(xxcI+R+r+1) && xiI>=(xxcI-R-r-1) && breal && zi>=0){
//    		// ricerca centro sfera passante per il punto incidente in coord originali
//    		teta = Math.atan2((yiI-yycI),(xiI-xxcI));
//    		// centro sfera passante per il punto incidente in coord originali
//    		xcsI = xxcI + R*Math.cos(teta);
//    		ycsI = yycI + R*Math.sin(teta);
//    		zcsI = zzcI;
//    		reset_vect(xvect,15);
//    		xvect[0] = xcsI*xcsI;
//    		xvect[1] = xiI*xiI;
//    		xvect[2] = -2*xcsI*xiI;
//    		xvect[3] = ycsI*ycsI;
//    		xvect[4] = yiI*yiI;
//    		xvect[5] = -2*ycsI*yiI;
//    		xvect[6] = zcsI*zcsI;
//    		xvect[7] = ziI*ziI;
//    		xvect[8] = -2*zcsI*ziI;
//    		dist = kahan_sum(xvect,9);
//    		dist = Math.sqrt(dist);
//    		if((r-1<=dist) && (dist<=r+1)){
//    			// centro sfera passante per il punto incidente in coord originali
//    			xcs = xcsI*a11I+ycsI*a21I+zcsI*a31I;
//    			ycs = xcsI*a12I+ycsI*a22I+zcsI*a32I;
//    			zcs = xcsI*a13I+ycsI*a23I+zcsI*a33I;
//    			// versore gradiente Sfera in coord originali
//    			beta = Math.sqrt((xi-xcs)*(xi-xcs)/(a1q*a1q)+(yi-ycs)*(yi-ycs)/(a2q*a2q)+(zi-zcs)*(zi-zcs)/(a3q*a3q));
//    			Xg = (xi-xcs)/(beta*a1q);
//    			Yg = (yi-ycs)/(beta*a2q);
//    			Zg = (zi-zcs)/(beta*a3q);
//    			// vettore riflesso con centro di illuminazione coincidente con il centro di proiezione
//    			alpha = (((xi-xx0)*Xg+(yi-yy0)*Yg+(zi-zz0)*Zg)/(Xg*Xg+Yg*Yg+Zg*Zg))*2.0;
//    			Xr = - alpha*Xg + (xi-xx0);
//    			Yr = - alpha*Yg + (yi-yy0);
//    			Zr = - alpha*Zg + (zi-zz0);
//    		}
//    		else
//    			breal = false;
//    	}
//    	else
//    		breal = false;
//
//    	pointResSfer.setXi(xi);
//    	pointResSfer.setYi(yi);
//    	pointResSfer.setZi(zi);
//    	pointResSfer.setXr(Xr);
//    	pointResSfer.setYr(Yr);
//    	pointResSfer.setZr(Zr);
//    	pointResSfer.setXg(Xg);
//    	pointResSfer.setYg(Yg);
//    	pointResSfer.setZg(Zg);
//    	return breal;
//    }
	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
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
	
	public boolean isInterno(double xiI, double ziI,double xxcI, double zzcI){
		boolean breal=false;
			
		return breal;
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
	   	double xI = vetOut[0];
	   	double yI = vetOut[1];
	   	double zI = vetOut[2];
	   	
	   	//double ym = (zI - this.zzcI)/Math.sqrt((xI - this.xxcI)*(xI - this.xxcI)+(yI - this.yycI)*(yI - this.yycI)+(zI - this.zzcI)*(zI - this.zzcI));
	   	double ymt=0;
	   	ymt = (Math.acos((zI - this.zzcI)/r))*r*(1/Math.PI);
	   	
		double xmt=0;
	   	
		if((yI - this.yycI)>=0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI)))*r*(1/Math.PI);
		else if((yI - this.yycI)<0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI))+2*Math.PI)*r*(1/Math.PI);
		
	   	
	   	int xm = (int) Math.round(xmt*scalax);
	   	int ym = (int) Math.round(ymt*scalay);
	   	
	   	result[0]=this.mapR[xm][ym];
	   	result[1]=this.mapG[xm][ym];
	   	result[2]=this.mapB[xm][ym];
		
		return result;
	}
	
	@Override
	public boolean setObjMapping(double x,double y,double z,double colR,double colG,double colB){
		double[] result = new double[3];
		result[0]=colR;
		result[1]=colG;
		result[2]=colB;
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=x;
	   	vetIn[1]=y;
	   	vetIn[2]=z;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	double xI = vetOut[0];
	   	double yI = vetOut[1];
	   	double zI = vetOut[2];
	   	
	   	//double ym = (zI - this.zzcI)/Math.sqrt((xI - this.xxcI)*(xI - this.xxcI)+(yI - this.yycI)*(yI - this.yycI)+(zI - this.zzcI)*(zI - this.zzcI));
	   	double ymt=0;
	   	ymt = (Math.acos((zI - this.zzcI)/r))*r*(1/Math.PI);
	   	
		double xmt=0;
	   
		if((yI - this.yycI)>=0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI)))*r*(1/Math.PI);
		else if((yI - this.yycI)<0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI))+2*Math.PI)*r*(1/Math.PI);
		
	   	
		int xm = (int) Math.round(xmt*scalax);
	   	int ym = (int) Math.round(ymt*scalay);
	   	
	   	this.mapR[xm][ym]=result[0];
	   	this.mapG[xm][ym]=result[1];
	   	this.mapB[xm][ym]=result[2];
		
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
	   	double xI = vetOut[0];
	   	double yI = vetOut[1];
	   	double zI = vetOut[2];
	   	
	   	//double ym = (zI - this.zzcI)/Math.sqrt((xI - this.xxcI)*(xI - this.xxcI)+(yI - this.yycI)*(yI - this.yycI)+(zI - this.zzcI)*(zI - this.zzcI));
	   	double ymt=0;
	   	ymt = (Math.acos((zI - this.zzcI)/r))*r*(1/Math.PI);
	   	
		double xmt=0;
	   	if((yI - this.yycI)>=0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI)))*r*(1/Math.PI);
		else if((yI - this.yycI)<0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI))+2*Math.PI)*r*(1/Math.PI);
				
	   	
		result[0] = (int) Math.round(xmt*scalax);
		result[1] = (int) Math.round(ymt*scalay);
		
		return result;
	}
	
	public double[] map2Obj(int x,int y){
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	
	   	vetIn[0]=Math.cos((x/scalax)*Math.PI/r)*r + xxcI;
	   	vetIn[1]=Math.sin((x/scalax)*Math.PI/r)*r + yycI;
	   	
	   	vetIn[2]=Math.cos((y/scalay)*Math.PI/r)*r + zzcI;
	   	vetOut=Utility.MatrVett(rotMatrix,vetIn,3);	
	   			
		return vetOut;
	}
	
	public double[] coordPolari(double x,double y,double z){
		double[] result = new double[2];
		result[0]=0;
		result[1]=0;
		
		double[] vetOut;
	   	double[] vetIn = new double[3];
	   	vetIn[0]=x;
	   	vetIn[1]=y;
	   	vetIn[2]=z;
	   	vetOut=Utility.MatrVett(rotMatrixInv,vetIn,3);
	   	double xI = vetOut[0];
	   	double yI = vetOut[1];
	   	double zI = vetOut[2];
	   	
	   	//double ym = (zI - this.zzcI)/Math.sqrt((xI - this.xxcI)*(xI - this.xxcI)+(yI - this.yycI)*(yI - this.yycI)+(zI - this.zzcI)*(zI - this.zzcI));
	   	double ymt=0;
	   	ymt = (Math.acos((zI - this.zzcI)/r));
	   	
		double xmt=0;
	   	
		if((yI - this.yycI)>=0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI)));
		else if((yI - this.yycI)<0)
			xmt = (Math.atan2((yI - this.yycI),(xI - this.xxcI))+2*Math.PI);
		
	   	
		result[0]=xmt;
		result[1]=ymt;
		
		return result;
	}
}