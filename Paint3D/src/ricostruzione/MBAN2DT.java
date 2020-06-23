package ricostruzione;
//*** Multilayer B-Spline Algorithm
//*** Ricostruzione di dati sparsi con algoritmo B-Spline multilivello B=bicubica,c2 (derivata seconda continua) 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import utility.Utility;

//********************************************************
//* MBA con matrice righe per colonne - versione corretta
//********************************************************

public class MBAN2DT {
	
	int Wi = 0;
	int Hi = 0;
	int kmax = 0;
	double horig =0;
	int offset=1;
	int[][] imageIn=null;
	Hashtable<Integer, double[][]> PWi_map=null;
	Hashtable<Integer, int[][]> imgStep_map=null;
	Hashtable<Integer, BicubicFunct> Bfunct_map=null;

	public MBAN2DT(int[][] imageOrig,int Wi,int Hi,int lattice) {
		this.Wi = Wi;
		this.Hi = Hi;
		//sample=sample*lattice;
		//horig=Math.round(h);
		horig=Math.min(Wi,Hi);
		horig=Math.min(horig,lattice);
		imageIn=imageOrig;
		Bfunct_map = new Hashtable<Integer, BicubicFunct>();
		Bfunct_map.put(0, new B0());
		Bfunct_map.put(1, new B1());
		Bfunct_map.put(2, new B2());
		Bfunct_map.put(3, new B3());
		//if(sample<Wi && sample<Hi)
		kmax = clacolaCicloMax(horig);
		
		
	}
	
	public void initialize(int[][] imageOrig,int Wi,int Hi,int lattice) {
		this.Wi = Wi;
		this.Hi = Hi;
		//horig=Math.round(lattice);
		//sample=sample*lattice;
		//horig=Math.round(h);
		horig=Math.min(Wi,Hi);
		horig=Math.min(horig,lattice);
		imageIn=imageOrig;
		//if(sample<Wi && sample<Hi)
		kmax = clacolaCicloMax(horig);
	}
	
	public double[][] calcoloMBA(){
		
		double hxk = 0;
		double hyk = hxk;
		double[][] PWik=null;
		double[][] Psik_old=null;
		double[][] Psik_First=null;
		double[][] Psik=null;
		double[][] P= new double[Hi][Wi];
		double[][] Fk= new double[Hi][Wi];
		double[][] Fk_zero= new double[Hi][Wi];
		int mk=0,mk_old=0;
		int nk=0,nk_old=0;
		
		for(int yi=0;yi<Hi;yi++){
			for(int xj=0;xj<Wi;xj++){
				Fk[yi][xj]=0;
				Fk_zero[yi][xj]=0;
				P[yi][xj]=imageIn[yi][xj];
			}
		}
		
		for(int stepk=0;stepk<=kmax;stepk++){
			
			PWik=calcoloBA(stepk, P, Fk);
			
			hxk = horig/power2(stepk);
			hyk = hxk;
			
			mk= (int) Math.round(Math.ceil(Hi/hyk));
			nk= (int) Math.round(Math.ceil(Wi/hxk));
			
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
						Psik[ik][jk]=Psik_First[ik][jk]+PWik[ik][jk];
					}
				}
			}
			else{
				for(int ik=0;ik<mk+3;ik++){
					for(int jk=0;jk<nk+3;jk++){
						Psik[ik][jk]=PWik[ik][jk];
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
				Fk=calcolaFunctk(PWik,stepk,false);
				
		}
		return Fk;
	}
	
	private double[][] calcolaFunctk(double[][] PWik,int stepk,boolean bcont){
		double[][] imagetmp = new double[(int)Hi][(int)Wi];
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
		double[][] PWi_kl_tmp=null;
		
	
		for(int yi=0;yi<Hi;yi++){
			for(int xj=0;xj<Wi;xj++){
				imagetmp[yi][xj]=0;
			}
		}
		
		i=0;
		j=0;
		for(int yi=0;yi<Hi;yi++){
			for(int xj=0;xj<Wi;xj++){
				if(imageIn[yi][xj]!=-1 || bcont){
					y = yi/hyk;
					x = xj/hxk;
					xint=(int) (Math.floor(x));
					yint=(int) (Math.floor(y));
					i=yint -1 + offset;
					j=xint -1 + offset;
					s = x - xint;
					t = y - yint;
					FunctTot=0;
					PWi_kl_tmp=PWik;
					for(int k=0;k<=3;k++){
						for(int l=0;l<=3;l++){
							Bk = Bfunct_map.get(k).value(s);
							Bl = Bfunct_map.get(l).value(t);
							Wkl=Bk*Bl;
							FunctTot = FunctTot + Wkl*PWi_kl_tmp[i+k][j+l];
						}
					}
					//imagetmp[yi][xj]=(int) Math.round(FunctTot);
					imagetmp[yi][xj]=FunctTot;
				}
			}
		}
		return imagetmp;
	}
	
	
	public double[][] calcolaFunctkXY(double[][] PWik,double Xing,double Ying,int stepk,boolean bcont){
		double[][] imagetmp = new double[(int)Hi][(int)Wi];
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
		double[][] PWi_kl_tmp=null;
		
	
		for(int yi=0;yi<Hi;yi++){
			for(int xj=0;xj<Wi;xj++){
				imagetmp[yi][xj]=0;
			}
		}
		
		i=0;
		j=0;
		for(int yi=0;yi<Hi;yi++){
			for(int xj=0;xj<Wi;xj++){
				if(imageIn[yi][xj]!=-1 || bcont){
					y = yi/hyk;
					x = xj/hxk;
					xint=(int) (Math.floor(x));
					yint=(int) (Math.floor(y));
					i=yint -1 + offset;
					j=xint -1 + offset;
					s = x - xint;
					t = y - yint;
					FunctTot=0;
					PWi_kl_tmp=PWik;
					for(int k=0;k<=3;k++){
						for(int l=0;l<=3;l++){
							Bk = Bfunct_map.get(k).value(s);
							Bl = Bfunct_map.get(l).value(t);
							Wkl=Bk*Bl;
							FunctTot = FunctTot + Wkl*PWi_kl_tmp[i+k][j+l];
						}
					}
					//imagetmp[yi][xj]=(int) Math.round(FunctTot);
					imagetmp[yi][xj]=FunctTot;
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
		double PWi_kl=0;
		double[][] PWik=null;
		
		hxk = horig/power2(stepk);
		hyk = hxk;
		
		int mk= (int) Math.round(Math.ceil(Hi/hxk));
		int nk= (int) Math.round(Math.ceil(Wi/hyk));
		
		PWik = new double[mk+3][nk+3];
		double[][] Dk = new double[mk+3][nk+3];
		double[][] Wk = new double[mk+3][nk+3];
		
		for(int ik=0;ik<mk+3;ik++){
			for(int jk=0;jk<nk+3;jk++){
				PWik[ik][jk]=0;
				Dk[ik][jk]=0;
				Wk[ik][jk]=0;
			}
		}
		
		i=0;
		j=0;
		try{
			for(int yi=0;yi<Hi;yi++){
				for(int xj=0;xj<Wi;xj++){
					//*** scarto i valori che non fanno parte dei dati campionati
					if(imageIn[yi][xj]!=-1){
						xc = xj/hxk;
						yc = yi/hyk;
						xcint = (int)Math.floor(xc);
						ycint = (int)Math.floor(yc);
						i= ycint -1 + offset;
						j= xcint -1 + offset;
						s = xc - xcint;
						t = yc - ycint;
						//Color img_color = new Color(imageIn[yi][xj]);
						WabSqrd=calcolaWabSqrd(s,t);
						//deltaZck = calcolaDeltaZc_Ref(stepk,yi,xj);
						P[yi][xj]=P[yi][xj]-Fk[yi][xj];
						deltaZck=P[yi][xj];
						for(int k=0;k<=3;k++){
							for(int l=0;l<=3;l++){
								Bk = Bfunct_map.get(k).value(s);
								Bl = Bfunct_map.get(l).value(t);
								Wkl=Bk*Bl;
								PWi_kl = (Wkl*deltaZck)/WabSqrd;
								Dk[i+k][j+l]=Dk[i+k][j+l]+Wkl*Wkl*PWi_kl;
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
					PWik[ik][jk]=Dk[ik][jk]/Wk[ik][jk];
				}
			}
		}
		return PWik;
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
