package paintimage;

import java.util.LinkedHashMap;

import object2D.Piano;
import object2D.Rettangolo;
import object3D.Parallelepipedo;
import object3D.Sfera;
import object3D.Toroide;
import objectBase.ObjectBase;
import texture.TexturePiano;
import texture.TextureSfera;
import utility.Utility;

public class ObjContainer {
	private LinkedHashMap<String, ObjectBase> listObj = null;
	private java.awt.Component compImg=null;

	public ObjContainer(java.awt.Component compImg) {
		this.compImg=compImg;
		int Hi = compImg.getHeight();//bitmapinfo->bmiHeader.biHeight;
    	int Wi = compImg.getWidth();//bitmapinfo->bmiHeader.biWidth;   	   	
    	//Hm = 0.0;
    	double Hmin = 100000;
    	double D = ((double)Hi/2.0);// + 100*Hi;// distanza del piano dell'immagine dal piano XZ
    	double L = (double)Wi/2.0;// coordinata del centro sull'asse orizzontale X
    	double P = 0;//(double)bitmapinfo->bmiHeader.biHeight/2.0;// altezza del piano sezionale dell'immagine originale
    	double alp = 0;//(PI)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	double bet = 0;//-PI/2.0;
    	double gam = 0;//((PI)/2.0);
    	Hmin = 0;
    	double DD = 0;// distanza dell'immagine originale dal piano di proiezione
    	DD = (D*P/Hi)*2;
    	
    	double r = Hi/3.0;
    	double R = 3*r;//Hi/2.0-r;
    	
    	 TexturePiano texturePiano= new TexturePiano();
    	 TextureSfera textureSfera= new TextureSfera();
		
    	
    	// centro del solido in coord originali
    	double xxc = L-Hi/2.0;
    	double yyc = D + DD + 2*r;
    	double zzc = r;
		//Sfera sfera1 = new Sfera(1,xxc+4*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
    	Sfera sfera1 = new Sfera(1,xxc,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
    	Sfera sfera2 = new Sfera(2,xxc+2*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.5);
//    	Sfera sfera3 = new Sfera(10,xxc+3*r,yyc-(Math.sqrt(3))*r,zzc+4*r,r, alp, bet, gam,180,180,255,0.8,0);
//    	sfera3.setTextureObj(textureSfera);
    	//Sfera sfera4 = new Sfera(11,xxc+3*r,yyc-(1.0/Math.sqrt(3))*r,zzc+(Math.sqrt(8.0/3.0)+2)*r,r, alp, bet, gam,180,180,255,0.8,1.9);
    	Toroide toroide = new Toroide(3,xxc,yyc,zzc+0*r,r,R, alp, bet, gam,239,201,0,0.8,1.9);
    	//Parallelepipedo parallelepip = new Parallelepipedo(7, xxc+3*r, yyc+2*r, zzc+r, 2*r,2*r,4*r, alp+(Utility.PI)/4, bet, gam, 255, 255, 255, 0.5,1.5);
    	//Parallelepipedo parallelepip2 = new Parallelepipedo(8, xxc+0.5*r, yyc+2*r, zzc+1.1*r, r,4*r,r, alp+(Utility.PI)/4, bet, gam, 255, 255, 255, 0.5,1.5);
    	//Parallelepipedo parallelepip3 = new Parallelepipedo(9, xxc+0.5*r, yyc+2.5*r, zzc-0.5*r, r,r,r, alp+(Utility.PI)/6, bet, gam, 255, 255, 255, 0.5,1.5);
    	//Piano piano = new Piano(99, xxc, yyc,0 , alp, bet, gam+(Utility.PI)/2, 255, 255, 255, 0.5,0.0);
    	Rettangolo piano = new Rettangolo(99, xxc, yyc,0 ,20*r ,20*r ,alp, bet, gam+(Utility.PI)/2, 255, 255, 255, 0.5,0.0);
    	piano.setTextureObj(texturePiano);
    	listObj = new LinkedHashMap<String, ObjectBase>();
    	//listObj.put("sfera1", sfera1);
    	//listObj.put("sfera2", sfera2);
    	//listObj.put("sfera3", sfera3);
    	//listObj.put("sfera4", sfera4);
    	listObj.put("toroide1", toroide);
    	//listObj.put("rettangolo1", rettangolo1);
    	//listObj.put("rettangolo2", rettangolo2);
    	//listObj.put("rettangolo3", rettangolo3);
    	//listObj.put("parallelepipedo1", parallelepip);
    	//listObj.put("parallelepipedo2", parallelepip2);
    	//listObj.put("parallelepipedo3", parallelepip3);
    	listObj.put("pavimento", piano);
	}

	 public ObjectBase getObjFromId(int idObj) {
	    	for(ObjectBase objtmp : this.listObj.values()){
	    		if(objtmp.getIdObj().getId()==idObj)
	    			return objtmp;
	    	}
			return null;
		}
	
	public LinkedHashMap<String, ObjectBase> getListObj() {
		return listObj;
	}

	public void setListObj(LinkedHashMap<String, ObjectBase> listObj) {
		this.listObj = listObj;
	}

	public java.awt.Component getCompImg() {
		return compImg;
	}

	public void setCompImg(java.awt.Component compImg) {
		this.compImg = compImg;
	}

}
