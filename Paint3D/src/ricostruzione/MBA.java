package ricostruzione;
//*** Multilayer B-Spline Algorithm
//*** Ricostruzione di dati sparsi con algoritmo B-Spline multilivello B=bicubica,c2 (derivata seconda continua) 

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import utility.Utility;



public class MBA {
	
	int Hi = 0;
	int Wi = 0;
	int kmax = 0;
	double horig =0;
	int offset=1;
	int[][] imageIn=null;
	Hashtable<Integer, double[][]> Phi_map=null;
	Hashtable<Integer, double[][]> imgStep_map=null;
	Hashtable<Integer, BicubicFunct> Bfunct_map=null;

	public MBA(int[][] imageOrig,int Hi,int Wi,int lattice) {
		this.Hi = Hi;
		this.Wi = Wi;
		//horig=Math.round(h*lattice);
		horig=Math.min(Hi,Wi);
		horig=Math.min(horig,lattice);
		imageIn=imageOrig;
		Bfunct_map = new Hashtable<Integer, BicubicFunct>();
		Bfunct_map.put(0, new B0());
		Bfunct_map.put(1, new B1());
		Bfunct_map.put(2, new B2());
		Bfunct_map.put(3, new B3());
		// calcolo potenza del 2 ; es H=32 -> Kmax=5
		//if(h<Hi && h<Wi)
		kmax = clacolaCicloMax(horig);
		
	}
	
	public Hashtable<Integer, double[][]> calcoloFunctArray(){
		imgStep_map = new Hashtable<Integer, double[][]>();
		Phi_map = new Hashtable<Integer, double[][]>();
		double[][] Functk=null;
		double[][] Phik=null;
		
		for(int stepk=0;stepk<=kmax;stepk++){
			
			Phik=calcolaPhik(stepk);
			Phi_map.put(stepk, Phik);
			
			Functk=calcolaFunctk(stepk);
			imgStep_map.put(stepk, Functk);
			
		}
		return imgStep_map;
	}
	
	private double[][] calcolaPhik(int stepk){
		double hxk = horig/power2(stepk);
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
		int mk=0;
		int nk=0;
		
		
		mk= (int) Math.round(Math.ceil(Wi/hxk));
		nk= (int) Math.round(Math.ceil(Hi/hyk));
		
		
		double[][] Phik = new double[mk+3][nk+3];
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
						deltaZck = calcolaDeltaZc(stepk,ii,jj);
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
	
	private double[][] calcolaFunctk(int stepk){
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
					x = ii/hxk;
					y = jj/hyk;
					xint=(int) (Math.floor(x));
					yint=(int) (Math.floor(y));
					i=xint -1 + offset;
					j=yint -1 + offset;
					s = x - xint;
					t = y - yint;
					FunctTot=0;
					Phi_kl_tmp=Phi_map.get(stepk);
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
		return imagetmp;
	}
	
public double[][] calcoloPhiArray_Ref(){
		
		int[][] Functk=null;
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
		double[][] Psik_old=null;
		double[][] Psik_First=null;
		double[][] Psik=null;
		double[][] P= new double[Wi][Hi];
		double[][] Fk= new double[Wi][Hi];
		int mk=0,mk_old=0;
		int nk=0,nk_old=0;
		
		for(int ii=0;ii<Wi;ii++){
			for(int jj=0;jj<Hi;jj++){
				Fk[ii][jj]=0;
				P[ii][jj]=imageIn[ii][jj];
			}
		}
		
		for(int stepk=0;stepk<=kmax;stepk++){
			
			 hxk = horig/power2(stepk);
			 hyk = hxk;
			
			mk= (int) Math.round(Math.ceil(Wi/hxk));
			nk= (int) Math.round(Math.ceil(Hi/hyk));
			
			Phik = new double[mk+3][nk+3];
			Psik = new double[mk+3][nk+3];
			Psik_First = new double[mk+3][nk+3];
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
			
			Fk=calcolaFunctk_Ref(Phik,stepk,false);
			
			for(int ik=0;ik<mk_old+3;ik++){
				for(int jk=0;jk<nk_old+3;jk++){
					Psik_First[2*ik][2*jk]=(Psik_old[ik-1][jk-1]+Psik_old[ik-1][jk+1]+Psik_old[ik+1][jk-1]+Psik_old[ik+1][jk+1]+6*(Psik_old[ik-1][jk]+Psik_old[ik][jk-1]+Psik_old[ik][jk+1]+Psik_old[ik+1][jk])+36*Psik_old[ik][jk])/64;
					Psik_First[2*ik][2*jk+1]=(Psik_old[ik-1][jk]+Psik_old[ik-1][jk+1]+Psik_old[ik+1][jk]+Psik_old[ik+1][jk+1]+6*(Psik_old[ik][jk]+Psik_old[ik][jk+1]))/16;
					Psik_First[2*ik+1][2*jk]=(Psik_old[ik][jk-1]+Psik_old[ik][jk+1]+Psik_old[ik+1][jk-1]+Psik_old[ik+1][jk+1]+6*(Psik_old[ik][jk]+Psik_old[ik+1][jk]))/16;
					Psik_First[2*ik+1][2*jk+1]=(Psik_old[ik][jk]+Psik_old[ik][jk+1]+Psik_old[ik+1][jk]+Psik_old[ik+1][jk+1])/4;
				}
			}
			for(int ik=0;ik<mk+3;ik++){
				for(int jk=0;jk<nk+3;jk++){
					Psik[ik][jk]=Psik_First[ik][jk]+Phik[ik][jk];
				}
			}
					
			Psik_old=Psik;	
			mk_old=mk;
			nk_old=nk;
		}
		return Phik;
	}
	
	private double[][] calcolaFunctk_Ref(double[][] Phik,int stepk,boolean bcont){
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
	

	public double calcolaFunctk(int x,int y){
		double[][] imgStep_tmp=null;
		double FunctTot=0;
		for(int stepk=0;stepk<=kmax;stepk++){
			imgStep_tmp=imgStep_map.get(stepk);
			FunctTot = FunctTot + imgStep_tmp[x][y];
		}
		return FunctTot;
	}
	
	private double calcolaDeltaZc(int stepk,int xc,int yc){
		double deltaZc=0;
		Color img_color=null;
		
		for(int k=0;k<=stepk;k++){
			if(k==0){
//				img_color = new Color(imageIn[xc][yc]);
//				if(channel==Utility.RED)
//					deltaZc = img_color.getRed();
//				else if(channel==Utility.GREEN)
//					deltaZc = img_color.getGreen();
//				else if(channel==Utility.BLUE)
//					deltaZc = img_color.getBlue();
				deltaZc = imageIn[xc][yc];
				
			}
			else{
//				img_color = new Color((imgStep_map.get(k-1))[xc][yc]);
//				if(channel==RED)
//					deltaZc = deltaZc - img_color.getRed();
//				else if(channel==GREEN)
//					deltaZc = deltaZc - img_color.getGreen();
//				else if(channel==BLUE)
//					deltaZc = deltaZc - img_color.getBlue();
				
				deltaZc = deltaZc - (imgStep_map.get(k-1))[xc][yc];
			}
		}
		
		return deltaZc;
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
