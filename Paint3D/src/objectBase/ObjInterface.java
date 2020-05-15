package objectBase;

import paintimage.PointResObj;

public interface ObjInterface {
	public boolean detect(double xx1,double yy1,double zz1,double xx0,double yy0,double zz0,double xinc,double yinc,double zinc,double distmin0,PointResObj pointResSfer,IdObj objId,int tipo);

	public int getR(double x,double y,double z);
	public int getG(double x,double y,double z);
	public int getB(double x,double y,double z);
}
