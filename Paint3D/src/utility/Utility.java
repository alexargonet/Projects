package utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

import org.apache.commons.math3.linear.*;

import objectBase.ObjectBase;
import paintimage.PointResEqua;
//import sun.security.util.ArrayUtil;

public class Utility {
	public static final double EPS= 1E-5;
	public static final double EPS2= 1E-10;
	public static final double EPS3= 1E-15;
	public static final double EPS4= 1E-20;
	public static final double EPS5= 1E-25;
	public static final double EPSM= 1E-323;
	public static final int REFLEX= 2;
	public static final int OMBRA= 1;
	public static final int NUMMAXREFLEX= 20;
	public static final double PI= 3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823;
	public static final int NUMMAXTRASM = 20;
	public static final int TRASP = 3;
	public static boolean debugGrafico=false;
	public static final int REFLEX_OPACO = 0;
	public static final int REFLEX_TOT_TRASP = 1;
	public static final int REFLEX_PARZ_TRASP = 2;
	public static final int RIFRAZIONE_OPACO = 3;
	public static final int RIFRAZIONE_TRASP = 4;
	// zoom radiosity: aumenta il campionamento della luce
	public static final int Zoom = 1;
	public static final int RED=1;
	public static final int GREEN=2;
	public static final int BLUE=3;
	public static final double POWER_LEVEL=0.0001;
	
	
	
	public static BigDecimal sqrtBD(BigDecimal in, int scale){
	    BigDecimal sqrtres = new BigDecimal(1);
	    sqrtres.setScale(scale + 3, RoundingMode.FLOOR);
	    BigDecimal store = new BigDecimal(in.toString());
	    boolean first = true;
	    do{
	        if (!first){
	            store = new BigDecimal(sqrtres.toString());
	        }
	        else first = false;
	        store.setScale(scale + 3, RoundingMode.FLOOR);
	        sqrtres = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide(
	                BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
	    }while (!store.equals(sqrtres));
	    return sqrtres.setScale(scale, RoundingMode.FLOOR);
	}
	
	public static void EquQuaNew(double av,double bv,double cv,double dv,double ev,PointResEqua pointRes){
		
		double A=0,B=0,C=0,pq=0,xn=0,fn=0,dn=0,tn=0,A3=0,B3=0,rv1=0,rv2=0,rv3=0,rv4=0;
//		char r1[64];
//		char r2[64];
//		char r3[64];
//		char r4[64];
		double rrv1m=0,rrv1p=0,rrv2m=0,rrv2p=0,rrv3m=0,rrv3p=0,rrv4m=0,rrv4p=0;
		double rrv1mR=0,rrv1pR=0,rrv2mR=0,rrv2pR=0,rrv3mR=0,rrv3pR=0,rrv4mR=0,rrv4pR=0;
		double rcv1m=0,rcv1p=0,rcv2m=0,rcv2p=0,rcv3m=0,rcv3p=0;
		double a=0,b=0,c=0,H=0,res=0,tmp=0,re=0;
		double res1=10,res2=10,res3=10,res4=10,res5=10,res6=10,res7=10,res8=10;
		double app=0,pm=0,qm=0,pp=0,qp=0,p=0,q=0,r=0,s=0;
		double[] xvect=new double[15];
		boolean breal=false,brealR=false;
		PointResEqua pointResTmp= new PointResEqua();
		
		double rv1m=pointRes.getRv1m();
    	double rv1p=pointRes.getRv1p();
    	double rv2m=pointRes.getRv2m();
    	double rv2p=pointRes.getRv2p();
    	double rv3m=pointRes.getRv3m();
    	double rv3p=pointRes.getRv3p();
    	double rv4m=pointRes.getRv4m();
    	double rv4p=pointRes.getRv4p();
		
//		memset(r1,0,sizeof(r1));
//		memset(r2,0,sizeof(r2));
//		memset(r3,0,sizeof(r3));
//		memset(r4,0,sizeof(r4));
		
		av = 1*av;
		bv = bv/av;
		cv = cv/av;
		dv = dv/av;
		ev = ev/av;
		av = 1;

		A = cv - (bv*bv*3)/8;
		B = -(bv*cv)/2 + dv + bv*bv*bv/8;
		C = -(3*bv*bv*bv*bv)/256 + (bv*bv*cv)/16 - (dv*bv)/4 + ev;
		tmp = A*A-(C*4);
		
		pointResTmp.setRv1m(rcv1m);
    	pointResTmp.setRv1p(rcv1p);
    	pointResTmp.setRv2m(rcv2m);
    	pointResTmp.setRv2p(rcv2p);
    	pointResTmp.setRv3m(rcv3m);
    	pointResTmp.setRv3p(rcv3p);
		breal = EquCub(1,(A*0.5),(tmp*0.0625),(-(B*B*0.015625)),pointResTmp);
		rcv1m=pointResTmp.getRv1m();
		rcv1p=pointResTmp.getRv1p();
    	rcv2m=pointResTmp.getRv2m();
    	rcv2p=pointResTmp.getRv2p();
    	rcv3m=pointResTmp.getRv3m();
    	rcv3p=pointResTmp.getRv3p();
    	
		if(breal){
			if(rcv1m>0 && rcv2m>0 && (Math.abs(rcv1m*rcv2m)>Math.abs(rcv2m*rcv3m) && Math.abs(rcv1m*rcv2m)>Math.abs(rcv1m*rcv3m))){
				p = Math.sqrt(rcv1m);
				q = Math.sqrt(rcv2m);
				r = -B/(8*p*q);
				s = bv/4;
			}
			else if(rcv2m>0 && rcv3m>0 && (Math.abs(rcv2m*rcv3m)>Math.abs(rcv1m*rcv2m) && Math.abs(rcv2m*rcv3m)>Math.abs(rcv1m*rcv3m))){
				p = Math.sqrt(rcv2m);
				q = Math.sqrt(rcv3m);
				r = -B/(8*p*q);
				s = bv/4;
			}
			else if(rcv1m>0 && rcv3m>0 && (Math.abs(rcv1m*rcv3m)>Math.abs(rcv1m*rcv2m) && Math.abs(rcv1m*rcv3m)>Math.abs(rcv2m*rcv3m))){
				p = Math.sqrt(rcv1m);
				q = Math.sqrt(rcv3m);
				r = -B/(8*p*q);
				s = bv/4;
			}
			else if(rcv1m>0 && rcv2m<0 && rcv3m<0){
				
				H = Math.sqrt(2*(rcv1m));

				re = H*H/4 + 2*A - B/(2*H);
				if(re<=0){
					a = 1;
					b = H;
					c = 2*A + rcv1m - (H*B)/(4*(rcv1m));
					pointResTmp.setRv1m(rrv1m);
			    	pointResTmp.setRv1p(rrv1p);
			    	pointResTmp.setRv2m(rrv2m);
			    	pointResTmp.setRv2p(rrv2p);
					EquSec(a,b,c,pointResTmp);
					rrv1m=pointResTmp.getRv1m();
					rcv1p=pointResTmp.getRv1p();
					rrv2m=pointResTmp.getRv2m();
					rrv2p=pointResTmp.getRv2p();
					tmp = rrv1m;
					rrv1m = rrv1m - bv/(4);
					rrv1p = 0;
					tmp = rrv2m;
					rrv2m = rrv2m - bv/(4);
					rrv2p = 0;
				}
				else{
					rrv1p = 999;
					rrv2p = 999;
				}
				
				re = H*H/4 + 2*A + B/(2*H);
				if(re<=0){
					a = 1;
					b = -H;
					c = 2*A + rcv1m + (H*B)/(4*(rcv1m));
					pointResTmp.setRv1m(rrv3m);
			    	pointResTmp.setRv1p(rrv3p);
			    	pointResTmp.setRv2m(rrv4m);
			    	pointResTmp.setRv2p(rrv4p);
					EquSec(a,b,c,pointResTmp);
					rrv3m=pointResTmp.getRv1m();
					rcv3p=pointResTmp.getRv1p();
					rrv4m=pointResTmp.getRv2m();
					rrv4p=pointResTmp.getRv2p();
					tmp = rrv3m;
					rrv3m = rrv3m - bv/(4);
					rrv3p = 0;
					tmp = rrv4m;
					rrv4m = rrv4m - bv/(4);
					rrv4p = 0;
				}
				else{
					rrv3p = 999;
					rrv4p = 999;
				}
				breal = false;
			}
			else if(rcv1m<0 && rcv2m>0 && rcv3m<0){
				
			}
			else if(rcv1m<0 && rcv2m<0 && rcv3m>0){
				
			}
		
			if(breal){
				rv1m = + p + q + r - s;
				rv2m = + p - q - r - s;
				rv3m = - p + q - r - s;
				rv4m = - p - q + r - s;
			}else{
				rv1m = rrv1m;
				rv2m = rrv2m;
				rv3m = rrv3m;
				rv4m = rrv4m;
				rv1p = rrv1p;
				rv2p = rrv2p;
				rv3p = rrv3p;
				rv4p = rrv4p;
			}
		}
		else{
			pm = Math.sqrt(rcv2m);
			pp = rcv2p/2;
			qm = Math.sqrt(rcv3m);
			qp = rcv3p/2;
			r = -B/(8*pm*qm);
			s = bv/4;
			rv1m = + 2*pm*Math.cos(pp) + r - s;
			rv2m = Math.sqrt(Math.pow(2*pm*Math.sin(pp),2)+Math.pow(-r-s,2));
			rv2p = Math.atan2(2*pm*Math.sin(pp),-r-s);
			rv3m = rv2m;
			rv3p = -rv2p;
			rv4m = - 2*pm*Math.cos(pp) + r - s;
		}
		pointRes.setRv1m(rv1m);
    	pointRes.setRv1p(rv1p);
    	pointRes.setRv2m(rv2m);
    	pointRes.setRv2p(rv2p);
    	pointRes.setRv3m(rv3m);
    	pointRes.setRv3p(rv3p);
    	pointRes.setRv4m(rv4m);
    	pointRes.setRv4p(rv4p);
		
	}
	
	public static void EquSec(double av,double bv,double cv,PointResEqua pointRes){
    	double dis=0,res=0;
    	double bvq=0,avq=0,av2=0,avq4=0,tmp=0,tmp2=0;
    	double rv1m=pointRes.getRv1m();
    	double rv1p=pointRes.getRv1p();
    	double rv2m=pointRes.getRv2m();
    	double rv2p=pointRes.getRv2p();

    	bv = bv/av;
    	cv = cv/av;
    	av = 1;
    	
    	bvq = bv*bv;
    	if((bvq>Math.abs(10*cv)) && bv>0){
    		dis = bvq - 4*cv;
    		tmp = Math.sqrt(dis);
    		rv1m = 2*cv/(-bv-tmp);
    		rv1p = 0.0;
    		rv2m = (-bv-tmp)/2;
    		rv2p = -rv1p;
    	}
    	else if((bvq>Math.abs(10*cv)) && bv<0){
    		dis = bvq - 4*cv;
    		tmp = Math.sqrt(dis);
    		rv1m = (-bv+tmp)/2;
    		rv1p = 0.0;
    		rv2m = 2*cv/(-bv+tmp);
    		rv2p = -rv1p;
    	}
    	else{
    		dis = bvq - 4*cv;
    		tmp2 = bv/2.0;
    		if(dis<0){
    			//*rv1m = Math..sqrt(Math..pow((-bv/(2*av)),2.0) + Math.abs(dis)/(4*av*av));
    			dis = -dis;
    			tmp = Math.sqrt(dis)/2.0;
    			rv1m = Math.sqrt((bvq/4) + (dis)/4);
    			rv1p = Math.atan2(tmp,tmp2);
    			rv2m = rv1m;
    			rv2p = -rv1p;
    		}
    		else{
    			tmp = Math.sqrt(dis)/2.0;
    			rv1m = -tmp2 + tmp;
    			rv1p = 0.0;
    			rv2m = -tmp2 - tmp;
    			rv2p = -rv1p;
    			/*
    			res = (*rv1m)*(*rv1m)*av + (*rv1m)*bv + cv;
    			res = (*rv2m)*(*rv2m)*av + (*rv2m)*bv + cv;
    			*/
    		}
    	}
    	pointRes.setRv1m(rv1m);
    	pointRes.setRv1p(rv1p);
    	pointRes.setRv2m(rv2m);
    	pointRes.setRv2p(rv2p);
    }
	
	static boolean EquCub(double av,double bv,double cv,double dv,PointResEqua pointRes){
		
		double p=0,q=0,A=0,B=0,pq=0,xn=0,fn=0,dn=0,tn=0,A3=0,B3=0,rv1=0,rv2=0,rv3=0,rv4=0,res=0;
		double Are=0,Aim=0,Tre=0,Tim=0,Am=0,Ap=0,tmp=0,tmp2=0,tmp3=0,tmp4=0;
		double[] xvect= new double[15];	
		int count=0;
		
		//double *rv1m,double *rv1p,double *rv2m,double *rv2p,double *rv3m,double *rv3p
		double rv1m=pointRes.getRv1m();
    	double rv1p=pointRes.getRv1p();
    	double rv2m=pointRes.getRv2m();
    	double rv2p=pointRes.getRv2p();
    	double rv3m=pointRes.getRv3m();
    	double rv3p=pointRes.getRv3p();

		bv = bv/av;
		cv = cv/av;
		dv = dv/av;
		av = 1;

		p = cv/3 - (bv*bv)/9;
		
		q = (bv*cv)/6 - (bv*bv*bv)/27 - dv/2;
		
		pq = q*q + p*p*p;
		
		
		if(pq<0){// complesso 3 radici reali
			Tre = q;
			Tim = Math.sqrt(-pq);
			reset_vect(xvect,15);
			xvect[0] = Tre*Tre;
			xvect[1] = Tim*Tim;
			tmp = kahan_sum(xvect,2);
			Am  = Math.pow(Math.sqrt(tmp),1.0/3.0);
			Ap  = Math.atan2(Tim,Tre)/3.0;
			Are = Am*Math.cos(Ap);
			Aim = Am*Math.sin(Ap);
			
			tmp = (-bv)/3;
			tmp2 = tmp - Are;
			tmp3 = Aim*Math.sqrt(3.0);
			rv1m = tmp + (2*Are);
			rv2m = tmp2 - tmp3;
			rv3m = tmp2 + tmp3;
			
			pointRes.setRv1m(rv1m);
	    	pointRes.setRv1p(rv1p);
	    	pointRes.setRv2m(rv2m);
	    	pointRes.setRv2p(rv2p);
	    	pointRes.setRv3m(rv3m);
	    	pointRes.setRv3p(rv3p);
			
			return true;
		}
		else{// 1 radice reale e due CMP CNJ
			A3 = q + Math.sqrt(pq);
			if(A3<0)
				A = -Math.pow(-A3,1.0/3.0);
			else
				A = Math.pow(A3,1.0/3.0);

			B3 = q - Math.sqrt(pq);
			if(B3<0)
				B = -Math.pow(-B3,1.0/3.0);
			else
				B = Math.pow(B3,1.0/3.0);
			
			tmp = (-bv)/3;
			tmp2 = (A + B);
			tmp3 = (A - B);
			tmp4 = Math.sqrt(3.0);
			rv1m = tmp + tmp2;
			rv2m = Math.sqrt(Math.pow(tmp-(0.5)*tmp2,2.0) + Math.pow((0.5)*tmp3*tmp4,2.0));
			rv2p = Math.atan2((0.5)*tmp3*tmp4,tmp-(0.5)*tmp2);

			rv3m = rv2m;
			rv3p = -rv2p;
			
			pointRes.setRv1m(rv1m);
	    	pointRes.setRv1p(rv1p);
	    	pointRes.setRv2m(rv2m);
	    	pointRes.setRv2p(rv2p);
	    	pointRes.setRv3m(rv3m);
	    	pointRes.setRv3p(rv3p);
			
			return false;
		}	

	}
	
	public static void EquSecBig(double av,double bv,double cv,PointResEqua pointRes){
    	BigDecimal dis=new BigDecimal(0),res=new BigDecimal(0);
    	BigDecimal bvq=new BigDecimal(0),avq=new BigDecimal(0),av2=new BigDecimal(0),avq4=new BigDecimal(0),tmp=new BigDecimal(0),tmp2=new BigDecimal(0);
    	BigDecimal rv1m=new BigDecimal(pointRes.getRv1m());
    	BigDecimal rv1p=new BigDecimal(pointRes.getRv1p());
    	BigDecimal rv2m=new BigDecimal(pointRes.getRv2m());
    	BigDecimal rv2p=new BigDecimal(pointRes.getRv2p());
    	BigDecimal avBD = new BigDecimal(av);
    	BigDecimal bvBD = new BigDecimal(bv);
    	BigDecimal cvBD = new BigDecimal(cv);
    	BigDecimal const10 = new BigDecimal(10);
    	BigDecimal const4 = new BigDecimal(4);
    	BigDecimal const2 = new BigDecimal(2);

    	bvBD = bvBD.divide(avBD, 15, BigDecimal.ROUND_HALF_EVEN);//bv = bv/av;
    	cvBD = cvBD.divide(avBD, 15, BigDecimal.ROUND_HALF_EVEN);//cv = cv/av;
    	avBD = new BigDecimal(1.0);//av = 1;
    	
    	bvq = bvBD.pow(2);
    	if((bvq.compareTo(cvBD.multiply(const10).abs())>0) && bvBD.compareTo(BigDecimal.ZERO)>0){
    		dis = bvq.subtract(cvBD.multiply(const4));
    		tmp = Utility.sqrtBD(dis,15);
    		rv1m = cvBD.multiply(const2).divide(bvBD.negate().subtract(tmp), 15, BigDecimal.ROUND_HALF_EVEN);//2*cv/(-bv-tmp);
    		rv1p =  new BigDecimal(0.0);
    		rv2m = bvBD.negate().subtract(tmp).divide(const2, 15, BigDecimal.ROUND_HALF_EVEN);//(-bv-tmp)/2;
    		rv2p = rv1p.negate();
    	}
    	//else if((bvq>Math.abs(10*cv)) && bv<0){
    	if((bvq.compareTo(cvBD.multiply(const10).abs())>0) && bvBD.compareTo(BigDecimal.ZERO)<0){
    		dis = dis = bvq.subtract(cvBD.multiply(const4));//bvq - 4*cv;
    		tmp = Utility.sqrtBD(dis,15);//Math.sqrt(dis);
    		rv1m = tmp.subtract(bvBD).divide(const2, 15, BigDecimal.ROUND_HALF_EVEN);//(-bv+tmp)/2;
    		rv1p = new BigDecimal(0.0);
    		rv2m = cvBD.multiply(const2).divide(tmp.subtract(bvBD), 15, BigDecimal.ROUND_HALF_EVEN);//2*cv/(-bv+tmp);
    		rv2p = rv1p.negate();//-rv1p;
    	}
    	else{
    		dis = bvq.subtract(cvBD.multiply(const4));//bvq - 4*cv;
    		tmp2 = bvBD.divide(const2,15, BigDecimal.ROUND_HALF_EVEN);//bv/2.0;
    		if(dis.compareTo(BigDecimal.ZERO)<0){
    			//*rv1m = Math.sqrt(Math.pow((-bv/(2*av)),2.0) + Math.abs(dis)/(4*av*av));
    			dis = dis.negate();
    			tmp = Utility.sqrtBD(dis,15).divide(const2, 15, BigDecimal.ROUND_HALF_EVEN);//Math.sqrt(dis)/2.0;
    			rv1m = Utility.sqrtBD(bvq.divide(const4, 15, BigDecimal.ROUND_HALF_EVEN).add(dis.divide(const4, 15, BigDecimal.ROUND_HALF_EVEN)),15);//Math.sqrt((bvq/4) + (dis)/4);
    			rv1p = BigDecimal.valueOf(Math.atan2(tmp.doubleValue(),tmp2.doubleValue()));
    			rv2m = rv1m;
    			rv2p = rv1p.negate();
    		}
    		else{
    			tmp = Utility.sqrtBD(dis,15).divide(const2, 15, BigDecimal.ROUND_HALF_EVEN);//Math.sqrt(dis)/2.0;
    			rv1m = tmp.subtract(tmp2);//-tmp2 + tmp;
    			rv1p = new BigDecimal(0.0);
    			rv2m = tmp.negate().subtract(tmp2);//-tmp2 - tmp;
    			rv2p = rv1p.negate();
    			/*
    			res = (*rv1m)*(*rv1m)*av + (*rv1m)*bv + cv;
    			res = (*rv2m)*(*rv2m)*av + (*rv2m)*bv + cv;
    			*/
    		}
    	}
    	pointRes.setRv1m(rv1m.doubleValue());
    	pointRes.setRv1p(rv1p.doubleValue());
    	pointRes.setRv2m(rv2m.doubleValue());
    	pointRes.setRv2p(rv2p.doubleValue());
    }
    
    
    public static double round(double val){
    	double low=0;
    	low = Math.floor(val);
    	if(Math.abs(val-low)<0.5)
    		return low;
    	else
    		return Math.ceil(val);
    }
	
	public static double kahan_sum(double[] x,int deg){
    	double s=0,c=0,t=0,y=0;
    	int i=0;

    	s = x[0];
    	for(i=1;i<deg;i++){
    		y=x[i]-c;
    		t=s+y;
    		c=(t-s)-y;
    		s=t;
    	}
    	s = s-c;
    	return s;
    }
    
    public static int reset_vect(double[] x,int maxn){
    	int i=0;
    	for(i=0;i<maxn;i++){
    		x[i]=0.0;
    	}
    	return maxn;
    }
    
    public static double cos(double x){
    	double cosret = Math.cos(x);
    	if(Math.abs(cosret)<EPS3)
    		return 0.0;
    	else
    		return cosret;
    }
    
    public static double sin(double x){
    	double sinret = Math.sin(x);
    	if(Math.abs(sinret)<EPS3)
    		return 0.0;
    	else
    		return sinret;
    }
    
    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
    
    public static double[] MatrVett(double[][] matrix,double[] vet,int dim){
		int i=0,j=0;
		double[] vetRes = new double[3];
		for(i=0;i<dim;i++){
			vetRes[i]=0;
		}
		for(i=0;i<dim;i++){
			for(j=0;j<dim;j++){
				vetRes[i]=vetRes[i]+matrix[i][j]*vet[j];
			}
		}
		return vetRes;
	}
	
	public static double[][] MatrProd(double[][] matrixa,double[][] matrixb,int dim){
		int i=0,j=0,k=0;
		double[][] matrRes = new double[3][3];
		for(i=0;i<dim;i++){
			for(j=0;j<dim;j++){
				matrRes[i][j]=0;
			}
		}
		for(i=0;i<dim;i++){
			for(j=0;j<dim;j++){
				for(k=0;k<dim;k++){
					matrRes[i][j]=matrRes[i][j]+matrixa[i][k]*matrixb[k][j];
				}
			}
		}
		return matrRes;
	}
	
	public static void MatrTrasp(double[][] matrixa,double[][] matrixb,int dim){
//		matrixb[0][0]=matrixa[0][0];
//		matrixb[0][1]=matrixa[1][0];
//		matrixb[0][2]=matrixa[2][0];
//		matrixb[1][0]=matrixa[0][1];
//		matrixb[1][1]=matrixa[1][1];
//		matrixb[1][2]=matrixa[2][1];
//		matrixb[2][0]=matrixa[0][2];
//		matrixb[2][1]=matrixa[1][2];
//		matrixb[2][2]=matrixa[2][2];
		int i=0,j=0;
		for(i=0;i<dim;i++){
			for(j=0;j<dim;j++){
				matrixb[i][j]=matrixa[j][i];
			}
		}
	}
	
	public static ObjectBase getObjFromId(LinkedHashMap<String, ObjectBase> listObj, int idObj) {
    	for(ObjectBase objtmp : listObj.values()){
    		if(objtmp.getIdObj().getId()==idObj)
    			return objtmp;
    	}
		return null;
	}
	//********************************************************
	//* MATRICI
	//********************************************************
	public static double[][] matrixInverse(double[][] a) {
		RealMatrix Arm = MatrixUtils.createRealMatrix(a);
	    LUDecomposition decomposition = new LUDecomposition(Arm);
	    return decomposition.getSolver().getInverse().getData();
	}
	
	public static double[] matrixMultVect(double[][] a,double[] b) {
		RealMatrix Arm = MatrixUtils.createRealMatrix(a);
		RealMatrix Brm = MatrixUtils.createColumnRealMatrix(b);
	    return Arm.multiply(Brm).getColumn(0);
	}
	//*********************************************************
	//* SHIFT VETTORE
	//*********************************************************
	
	public static double[]  shift_array(double[] A, int n) {
	    int N = A.length;
	    double[] B = new double[N];
	    n %= N;
	    if(n < 0)
	        n = N + n;
	    int d = gcd(N, n);
	    for(int i = 0; i < d; i++) {
	        double temp = A[i];
	        if(n>0) {
		        for(int j = i - n + N; j != i; j = (j - n + N) % N)
		            B[(j + n) % N] = A[j];
		        }
	        B[(i + n) % N] = temp;
	    }
	    return B;
	}
	
	private static int gcd(int a, int b) {
	    while(b != 0) {
	        int c = a;
	        a = b;
	        b = c % a;
	    }
	    return a;
	}
	//**************************************************************
	//* FILTERING
	//**************************************************************
	
	public static double[][] filter(double[][] Kernel,double[][] matrix,int Hi,int Wi,int dimKernel) {
		double[][] G = new double[Hi][Wi];
		double Gtmp = 0;
		int dimMath=(int)Math.ceil(dimKernel/2);
		int ind=0,jnd=0;
    	for(int i=0;i<(Hi);i++){
			for(int j=0;j<(Wi);j++){	
				Gtmp=0;
				for(int ii=-(dimMath); ii<=dimMath;ii++) {
	    		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
				    		if(i-ii>=0 && i-ii<Hi && j-jj>=0 && j-jj<Wi) {	    			
				    			Gtmp = Gtmp + Kernel[ii+(dimMath)][jj+(dimMath)]*matrix[i-ii][j-jj];
				    		}
				    		else{
				    			if(i-ii<0)
				    				ind=0;
				    			if(i-ii>=Hi)
				    				ind=Hi-1;
				    			if(j-jj<0)
				    				jnd=0;
				    			if(j-jj>=Wi)
				    				jnd=Wi-1;
				    			Gtmp = Gtmp + Kernel[ii+(dimMath)][jj+(dimMath)]*matrix[ind][jnd];
				    		}
			    		}
			    	}
	    		G[i][j]= Gtmp;
			}
    	}
    	return G;
	}
	
	//***********************************************************
	//*  GADIENTE X of GAUSSIAN
	//***********************************************************
	public static double[][] gradGauX(int sigma){
		double expn=0;
	   	 double signmq = sigma*sigma;
	   	 double signmq4 = signmq*signmq;
	   	 double signm2 = signmq*2;
	   	 double signm2pi = signm2*Math.PI;
	   	 int dimMat=(int)Math.ceil(6*sigma);
	   	 if(dimMat%2==0)
	   		 dimMat++;
	   	 int dimMath=(int)Math.ceil(dimMat/2);
		 double[][] GRoGX = new double[dimMat][dimMat];
	   	 double amp=0;
	   	 double Nweix=0;
	   	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
	    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
	    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
	    		amp = -(2*jj)/(signm2pi*signm2);
	    		GRoGX[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
	    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
	    		if(jj<0)
	    			Nweix=Nweix+GRoGX[ii+(dimMath)][jj+(dimMath)];
	    	}
	     }
	   	for(int ii=-(dimMath); ii<=dimMath;ii++) {
	    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
	    		GRoGX[ii+(dimMath)][jj+(dimMath)] /= Nweix;
	    		
	    	}
	     }
	   	 return GRoGX;
	}
	//***********************************************************
	//*  GADIENTE Y of GAUSSIAN
	//***********************************************************
	public static double[][] gradGauY(int sigma){
		double expn=0;
	   	 double signmq = sigma*sigma;
	   	 double signmq4 = signmq*signmq;
	   	 double signm2 = signmq*2;
	   	 double signm2pi = signm2*Math.PI;
	   	 int dimMat=(int)Math.ceil(6*sigma);
	   	 if(dimMat%2==0)
	   		 dimMat++;
	   	 int dimMath=(int)Math.ceil(dimMat/2);
	   	 double[][] GRoGY = new double[dimMat][dimMat];
	   	 double amp=0;
	   	 double Nweiy=0;
	   	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
	    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
	    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
	    		amp = -(2*ii)/(signm2pi*signm2);
	    		GRoGY[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
	    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
	    		if(ii<0)
	    			Nweiy=Nweiy+GRoGY[ii+(dimMath)][jj+(dimMath)];
	    	}
	     }
	   	for(int ii=-(dimMath); ii<=dimMath;ii++) {
	    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
	    		GRoGY[ii+(dimMath)][jj+(dimMath)] /= Nweiy;
	    		
	    	}
	     }
	   	 return GRoGY;
	}
}
