package object3D;

import java.util.LinkedHashMap;

import object2D.Rettangolo;
import objectBase.ObjectBase;
import objectBase.IdObj;
import paintimage.PointResObj;
import utility.Utility;

public class Parallelepipedo extends ObjectBase {

	private double bas;
	private double alt;
	private double prof;
	Rettangolo rettangoloX,rettangoloX2;
	Rettangolo rettangoloY,rettangoloY2;
	Rettangolo rettangoloZ,rettangoloZ2;
	LinkedHashMap<String,ObjectBase> listObjSub;
	double[][] rotMatrixObj = new double[3][3];
//	double[][] rotMatrixInv = new double[3][3];
	//private int subId;
	
	public Parallelepipedo(int Id, double xxc, double yyc, double zzc,double bas,double alt,double prof, double alp, double bet, double gam, int red, int green, int blue, double reflexCoef,double rifrazCoef) {
		super(Id, xxc, yyc, zzc, alp, bet, gam, red, green, blue, reflexCoef, rifrazCoef);
		// TODO Auto-generated constructor stub
		//subId=Id;
		this.bas=bas;
		this.alt=alt;
		this.prof=prof;
		////calcMatrix(alp,bet,gam);//
		//calcMatrix(alp,bet,gam,rotMatrixObj);
		double[] vetOut;
		double[] vetIn = new double[3];
		// Y
		vetIn[0] = 0;
		vetIn[1] = -prof/2;
		vetIn[2] = 0;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
		rettangoloY = new Rettangolo(Id*100, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], bas,alt, 0, 0, 0, red, green, blue, reflexCoef,rifrazCoef,rotMatrix);
		// X	
		vetIn[0] = -bas/2;
		vetIn[1] = 0;
		vetIn[2] = 0;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
    	rettangoloX = new Rettangolo(Id*100+1, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], prof,alt, (Utility.PI)/2, 0, 0, red, green, blue, reflexCoef,rifrazCoef,rotMatrix);
		// Z
    	vetIn[0] = 0;
		vetIn[1] = 0;
		vetIn[2] = +alt/2;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
    	rettangoloZ = new Rettangolo(Id*100+2, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], bas,prof, 0, 0, (Utility.PI)/2, red, green, blue,reflexCoef,rifrazCoef,rotMatrix);
    	// Y2
		vetIn[0] = 0;
		vetIn[1] = +prof/2;
		vetIn[2] = 0;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
		rettangoloY2 = new Rettangolo(Id*100+3, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], bas,alt, 0, 0, Utility.PI, red, green, blue, reflexCoef,rifrazCoef,rotMatrix);
		// X	2
		vetIn[0] = +bas/2;
		vetIn[1] = 0;
		vetIn[2] = 0;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
    	rettangoloX2 = new Rettangolo(Id*100+4, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], prof,alt, (Utility.PI)/2, Utility.PI, 0, red, green, blue, reflexCoef,rifrazCoef,rotMatrix);
		// Z2
    	vetIn[0] = 0;
		vetIn[1] = 0;
		vetIn[2] = -alt/2;
		vetOut=Utility.MatrVett(rotMatrix, vetIn, 3);
    	rettangoloZ2 = new Rettangolo(Id*100+5, xxc+vetOut[0], yyc+vetOut[1], zzc+vetOut[2], bas,prof, 0, 0, ((Utility.PI)/2)+Utility.PI, red, green, blue, reflexCoef,rifrazCoef,rotMatrix);
		    	
		listObjSub = new LinkedHashMap<String,ObjectBase>();
    	listObjSub.put("ret1",rettangoloY);
    	listObjSub.put("ret2",rettangoloX);
    	listObjSub.put("ret3",rettangoloZ);
    	listObjSub.put("ret4",rettangoloY2);
    	listObjSub.put("ret5",rettangoloX2);
    	listObjSub.put("ret6",rettangoloZ2);
	}
	
		public boolean detect(double xx1,double yy1,double zz1,double xx0,double yy0,double zz0,double xinc, double yinc, double zinc,double distmin0,PointResObj pointResIO,IdObj objId,int tipo){

			boolean btmp=false;
			boolean breal=false;
			boolean brealombra=false;
			double xiR=0,yiR=0,ziR=0;
			double XrR=0,YrR=0,ZrR=0;
			double XgR=0,YgR=0,ZgR=0;
			double dist=0;
			double distmin=distmin0;
			IdObj objIdTmp=null;
			
			objIdTmp=objId.getSubObj();
			if(objId.getId()!=this.getIdObj().getId() || objIdTmp==null)
				objIdTmp=objId;
			
			
			if(tipo==Utility.REFLEX || tipo==Utility.TRASP){
			
				for(ObjectBase objtmp : listObjSub.values()){
					btmp=false;
					pointResIO.setXi(0);
					pointResIO.setYi(0);
					pointResIO.setZi(0);
					pointResIO.setXr(0);
					pointResIO.setYr(0);
					pointResIO.setZr(0);
					pointResIO.setXg(0);
					pointResIO.setYg(0);
					pointResIO.setZg(0);
					btmp = objtmp.detect(xx1,yy1,zz1,xx0,yy0,zz0,xinc,yinc,zinc,distmin0,pointResIO,objIdTmp,tipo);
					if(btmp){
						double xi1tmp=pointResIO.getXi();
						double yi1tmp=pointResIO.getYi();
						double zi1tmp=pointResIO.getZi();
				    	dist = Math.sqrt(Math.pow(xi1tmp-xx0,2.0)+Math.pow(yi1tmp-yy0,2.0)+Math.pow(zi1tmp-zz0,2.0));
						if(dist<distmin){
							xiR=pointResIO.getXi();
	    			    	yiR=pointResIO.getYi();
	    			    	ziR=pointResIO.getZi();
	    			    	XrR=pointResIO.getXr();
	    			    	YrR=pointResIO.getYr();
	    			    	ZrR=pointResIO.getZr();
	    			    	XgR=pointResIO.getXg();
	    			    	YgR=pointResIO.getYg();
	    			    	ZgR=pointResIO.getZg();
	    			    	this.setBlue(objtmp.getB(xiR,yiR,ziR));
	    			    	this.setGreen(objtmp.getG(xiR,yiR,ziR));
	    			    	this.setRed(objtmp.getR(xiR,yiR,ziR));
							distmin = dist;
							//objIdnew = objtmp.getId();
							//this.setId(objtmp.getId());
							this.getIdObj().setSubObj(objtmp.getIdObj().getId());
							this.setReflexCoef(objtmp.getReflexCoef());
							breal = true;
						}
					}
				}
			}
			else if(tipo==Utility.OMBRA){
				
				brealombra = false;
				for(ObjectBase objtmp : listObjSub.values()){
					pointResIO.setXi(0);
					pointResIO.setYi(0);
					pointResIO.setZi(0);
					pointResIO.setXr(0);
					pointResIO.setYr(0);
					pointResIO.setZr(0);
					pointResIO.setXg(0);
					pointResIO.setYg(0);
					pointResIO.setZg(0);
					brealombra = objtmp.detect(xx1,yy1,zz1,xx0,yy0,zz0,xinc,yinc,zinc,distmin0,pointResIO,objId,Utility.OMBRA);
					if(brealombra)
						break;
				}
				if(brealombra){
					breal = true;
	    		}
			}
			
		pointResIO.setXi(xiR);
	   	pointResIO.setYi(yiR);
	   	pointResIO.setZi(ziR);
	   	pointResIO.setXr(XrR);
	   	pointResIO.setYr(YrR);
	   	pointResIO.setZr(ZrR);
	   	pointResIO.setXg(XgR);
	   	pointResIO.setYg(YgR);
	   	pointResIO.setZg(ZgR);
		return breal;
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

		public double getProf() {
			return prof;
		}

		public void setProf(double prof) {
			this.prof = prof;
		}

		public boolean isInterno(double xiI, double ziI,double xxcI, double zzcI){
			boolean breal=false;
				
			return breal;
		}
		
		@Override
		public void initMapping(){
//			int Wi=(int)(bas/100);
//			int Hi=(int)(alt/100);
//			this.mapR = new double[Wi][Hi];
//			this.mapG = new double[Wi][Hi];
//			this.mapB = new double[Wi][Hi];
		}
		
		@Override
		public double[] getObjMapping(double x,double y,double z){
			double[] result = null;

			ObjectBase objRif = Utility.getObjFromId(listObjSub,this.getIdObj().getSubObj().getId());
			result=objRif.getObjMapping(x, y, z);
			return result;
		}
		
		@Override
		public boolean setObjMapping(double x,double y,double z,double colR,double colG,double colB){

			ObjectBase objRif = Utility.getObjFromId(listObjSub,this.getIdObj().getSubObj().getId());
			objRif.setObjMapping(x, y, z, colR, colG, colB);
			
			return true;
		}
		
		public int[] obj2Map(double x,double y,double z){
			int[] result = null;
			ObjectBase objRif = Utility.getObjFromId(listObjSub,this.getIdObj().getSubObj().getId());
			result=objRif.obj2Map(x, y, z);
			return result;
		}
		
		public double[] map2Obj(int x,int y){
			double[] result = null;
			ObjectBase objRif = Utility.getObjFromId(listObjSub,this.getIdObj().getSubObj().getId());
			result=objRif.map2Obj(x, y);
			return result;
		}
		
		public double[] coordPolari(double x,double y,double z){
			double[] result = new double[2];
			result[0]=0;
			result[1]=0;
			
			return result;
		}

}
