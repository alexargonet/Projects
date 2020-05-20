package ricostruzione;
//*** Multilayer B-Spline Algorithm
//*** Ricostruzione di dati sparsi con algoritmo B-Spline multilivello B=bicubica,c2 (derivata seconda continua) 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import utility.Utility;



public class MBAN2D {
	
	int Hi = 0;
	int Wi = 0;
	int kmax = 0;
	double horig =0;
	int offset=1;
	int[][] imageIn=null;
	Hashtable<Integer, double[][]> Phi_map=null;
	Hashtable<Integer, int[][]> imgStep_map=null;
	Hashtable<Integer, BicubicFunct> Bfunct_map=null;

	public MBAN2D(int[][] imageOrig,int Hi,int Wi,int lattice) {
		this.Hi = Hi;
		this.Wi = Wi;
		//sample=sample*lattice;
		//horig=Math.round(h);
		horig=Math.min(Hi,Wi);
		horig=Math.min(horig,lattice);
		imageIn=imageOrig;
		Bfunct_map = new Hashtable<Integer, BicubicFunct>();
		Bfunct_map.put(0, new B0());
		Bfunct_map.put(1, new B1());
		Bfunct_map.put(2, new B2());
		Bfunct_map.put(3, new B3());
		//if(sample<Hi && sample<Wi)
		kmax = clacolaCicloMax(horig);
		
		
	}
	
	public void initialize(int[][] imageOrig,int Hi,int Wi,int lattice) {
		this.Hi = Hi;
		this.Wi = Wi;
		//horig=Math.round(lattice);
		//sample=sample*lattice;
		//horig=Math.round(h);
		horig=Math.min(Hi,Wi);
		horig=Math.min(horig,lattice);
		imageIn=imageOrig;
		//if(sample<Hi && sample<Wi)
		kmax = clacolaCicloMax(horig);
	}
	
	public double[][] calcoloMBA(){
		
		double hxk = 0;
		double hyk = hxk;
		double[][] Phik=null;
		double[][] Psik_old=null;
		double[][] Psik_First=null;
		double[][] Psik=null;
		double[][] P= new double[Wi][Hi];
		double[][] Fk= new double[Wi][Hi];
		double[][] Fk_zero= new double[Wi][Hi];
		int mk=0,mk_old=0;
		int nk=0,nk_old=0;
		
		for(int ii=0;ii<Wi;ii++){
			for(int jj=0;jj<Hi;jj++){
				Fk[ii][jj]=0;
				Fk_zero[ii][jj]=0;
				P[ii][jj]=imageIn[ii][jj];
			}
		}
		
		for(int stepk=0;stepk<=kmax;stepk++){
			
			Phik=calcoloBA(stepk, P, Fk);
			
			hxk = horig/power2(stepk);
			hyk = hxk;
			
			mk= (int) Math.round(Math.ceil(Wi/hxk));
			nk= (int) Math.round(Math.ceil(Hi/hyk));
			
			Psik = new double[mk+3][nk+3];
			
			
			if(stepk>0){
				Psik_First = new double[mk+3][nk+3];
				int offset=1;
				for(int ik=-1;ik<mk_old+2;ik++){
					for(int jk=-1;jk<nk_old+2;jk++){
						if((2*ik+offset>=0) && (2*jk+offset>=0) && (2*ik+offset<mk+3) && (2*jk+offset<nk+3) && (ik-1+offset>=0) && (jk-1+offset>=0) && (jk+1+offset<nk_old+3) && (ik+1+offset<mk_old+3) && (jk+offset<nk_old+3) && (ik+offset<mk_old+3))
							Psik_First[2*ik+offset][2*jk+offset]=(Psik_old[ik-1+offset][jk-1+offset]+Psik_old[ik-1+offset][jk+1+offset]+Psik_old[ik+1+offset][jk-1+offset]+Psik_old[ik+1+offset][jk+1+offset]+6*(Psik_old[ik-1+offset][jk+offset]+Psik_old[ik+offset][jk-1+offset]+Psik_old[ik+offset][jk+1+offset]+Psik_old[ik+1+offset][jk+offset])+36*Psik_old[ik+offset][jk+offset])/64;
						if((2*ik+offset>=0) && (2*jk+1+offset>=0) && (2*ik+offset<mk+3) && (2*jk+1+offset<nk+3) && (ik-1+offset>=0) && (jk+offset>=0) && (jk+1+offset<nk_old+3) && (ik+1+offset<mk_old+3) && (jk+offset<nk_old+3) && (ik+offset<mk_old+3))
							Psik_First[2*ik+offset][2*jk+1+offset]=(Psik_old[ik-1+offset][jk+offset]+Psik_old[ik-1+offset][jk+1+offset]+Psik_old[ik+1+offset][jk+offset]+Psik_old[ik+1+offset][jk+1+offset]+6*(Psik_old[ik+offset][jk+offset]+Psik_old[ik+offset][jk+1+offset]))/16;
						if((2*ik+1+offset>=0) && (2*jk+offset>=0) && (2*ik+1+offset<mk+3) && (2*jk+offset<nk+3) && (ik+offset>=0) && (jk-1+offset>=0) && (jk+1+offset<nk_old+3) && (ik+1+offset<mk_old+3) && (jk+offset<nk_old+3) && (ik+offset<mk_old+3))
							Psik_First[2*ik+1+offset][2*jk+offset]=(Psik_old[ik+offset][jk-1+offset]+Psik_old[ik+offset][jk+1+offset]+Psik_old[ik+1+offset][jk-1+offset]+Psik_old[ik+1+offset][jk+1+offset]+6*(Psik_old[ik+offset][jk+offset]+Psik_old[ik+1+offset][jk+offset]))/16;
						if((2*ik+1+offset>=0) && (2*jk+1+offset>=0) && (2*ik+1+offset<mk+3) && (2*jk+1+offset<nk+3) && (ik+offset>=0) && (jk+offset>=0) && (jk+1+offset<nk_old+3) && (ik+1+offset<mk_old+3) && (jk+offset<nk_old+3) && (ik+offset<mk_old+3))
							Psik_First[2*ik+1+offset][2*jk+1+offset]=(Psik_old[ik+offset][jk+offset]+Psik_old[ik+offset][jk+1+offset]+Psik_old[ik+1+offset][jk+offset]+Psik_old[ik+1+offset][jk+1+offset])/4;
					}
				}
				for(int ik=0;ik<mk+3;ik++){
					for(int jk=0;jk<nk+3;jk++){
						Psik[ik][jk]=Psik_First[ik][jk]+Phik[ik][jk];
					}
				}
			}
			else{
				for(int ik=0;ik<mk+3;ik++){
					for(int jk=0;jk<nk+3;jk++){
						Psik[ik][jk]=Phik[ik][jk];
					}
				}
			}
					
			Psik_old=Psik;	
			mk_old=mk;
			nk_old=nk;
			
			// se sono nell'ultimo passo di step calcolo la Fk per tutti i punti
			if(stepk==kmax)
				Fk=calcolaFunctk(Psik,stepk,true);
			else
				Fk=calcolaFunctk(Phik,stepk,false);
				
		}
		return Fk;
	}
	
	private double[][] calcolaFunctk(double[][] Phik,int stepk,boolean bcont){
		double[][] imagetmp = new double[(int)Wi][(int)Hi];
		//imgStep_map.put(stepk, imagetmp);
		double hxk = horig/power2(stepk);
		double hyk = hxk;
		double x = 0;
		double y = 0;
		int i=0;
		int j=0;
		int xint=0;
		int yint=0;
		double s = 0;
		double t = 0;
		double Bk=0;
		double Bl=0;
		double Wkl=0;
		double FunctTot=0;
		double[][] Phi_kl_tmp=null;
		
	
		for(int ii=0;ii<Wi;ii++){
			for(int jj=0;jj<Hi;jj++){
				imagetmp[ii][jj]=0;
			}
		}
		
		i=0;
		j=0;
		for(int ii=0;ii<Wi;ii++){
			for(int jj=0;jj<Hi;jj++){
				if(imageIn[ii][jj]!=-1 || bcont){
					x = ii/hxk;
					y = jj/hyk;
					xint=(int) (Math.floor(x));
					yint=(int) (Math.floor(y));
					i=xint -1 + offset;
					j=yint -1 + offset;
					s = x - xint;
					t = y - yint;
					FunctTot=0;
					Phi_kl_tmp=Phik;
					for(int k=0;k<=3;k++){
						for(int l=0;l<=3;l++){
							Bk = Bfunct_map.get(k).value(s);
							Bl = Bfunct_map.get(l).value(t);
							Wkl=Bk*Bl;
							FunctTot = FunctTot + Wkl*Phi_kl_tmp[i+k][j+l];
						}
					}
					//imagetmp[ii][jj]=(int) Math.round(FunctTot);
					imagetmp[ii][jj]=FunctTot;
				}
			}
		}
		return imagetmp;
	}
	
	public double[][] calcoloBA(int stepk,double[][] P,double[][] Fk){
		double hxk = 0;
		double hyk = hxk;
		double xc = 0;
		double yc = 0;
		int i=0;
		int j=0;
		int xcint,ycint;
		double s = 0;
		double t = 0;
		double WabSqrd=0;
		double deltaZck = 0;
		double Bk=0;
		double Bl=0;
		double Wkl=0;
		double Phi_kl=0;
		double[][] Phik=null;
		
		hxk = horig/power2(stepk);
		hyk = hxk;
		
		int mk= (int) Math.round(Math.ceil(Wi/hxk));
		int nk= (int) Math.round(Math.ceil(Hi/hyk));
		
		Phik = new double[mk+3][nk+3];
		double[][] Dk = new double[mk+3][nk+3];
		double[][] Wk = new double[mk+3][nk+3];
		
		for(int ik=0;ik<mk+3;ik++){
			for(int jk=0;jk<nk+3;jk++){
				Phik[ik][jk]=0;
				Dk[ik][jk]=0;
				Wk[ik][jk]=0;
			}
		}
		
		i=0;
		j=0;
		try{
			for(int ii=0;ii<Wi;ii++){
				for(int jj=0;jj<Hi;jj++){
					//*** scarto i valori che non fanno parte dei dati campionati
					if(imageIn[ii][jj]!=-1){
						xc = ii/hxk;
						yc = jj/hyk;
						xcint = (int)Math.floor(xc);
						ycint = (int)Math.floor(yc);
						i= xcint -1 + offset;
						j= ycint -1 + offset;
						s = xc - xcint;
						t = yc - ycint;
						//Color img_color = new Color(imageIn[ii][jj]);
						WabSqrd=calcolaWabSqrd(s,t);
						//deltaZck = calcolaDeltaZc_Ref(stepk,ii,jj);
						P[ii][jj]=P[ii][jj]-Fk[ii][jj];
						deltaZck=P[ii][jj];
						for(int k=0;k<=3;k++){
							for(int l=0;l<=3;l++){
								Bk = Bfunct_map.get(k).value(s);
								Bl = Bfunct_map.get(l).value(t);
								Wkl=Bk*Bl;
								Phi_kl = (Wkl*deltaZck)/WabSqrd;
								Dk[i+k][j+l]=Dk[i+k][j+l]+Wkl*Wkl*Phi_kl;
								Wk[i+k][j+l]=Wk[i+k][j+l]+Wkl*Wkl;
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		for(int ik=0;ik<mk+3;ik++){
			for(int jk=0;jk<nk+3;jk++){
				if(Wk[ik][jk]!=0.0){
					Phik[ik][jk]=Dk[ik][jk]/Wk[ik][jk];
				}
			}
		}
		return Phik;
	}
	
	private double calcolaWabSqrd(double s,double t){
		double Wabres =0;
		double Ba=0;
		double Bb=0;
		for(int a=0;a<=3;a++){
			for(int b=0;b<=3;b++){
				Ba = Bfunct_map.get(a).value(t);
				Bb = Bfunct_map.get(b).value(t);
				Ba = Ba*Ba;
				Bb = Bb*Bb;
				Wabres = Wabres + Ba*Bb;
			}
		}
		return Wabres;
	}
	
	private int clacolaCicloMax(double h){
		int km=0;
		double htmp=h;
		while(htmp>1){
			htmp = htmp/2;
			km++;
		}
		return km;
	}
	
	public double power2(int k){
		double po2=1;
		for(int i=0;i<=k;i++){
			if(i==0)
				po2=1;
			else
				po2 = po2 * 2;
		}
		return po2;
	}
}
