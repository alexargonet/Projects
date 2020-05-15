package paintimage;

import objectBase.IdObj;

public class PointResObj {
	double Xi,Yi,Zi;
	double Xr,Yr,Zr;
	double Xg,Yg,Zg;
	boolean ombra;
	private IdObj idObj;
	double cosalpha;
	double cosLamb;
	
	
	public double getXi() {
		return Xi;
	}
	public void setXi(double xi) {
		Xi = xi;
	}
	public double getYi() {
		return Yi;
	}
	public void setYi(double yi) {
		Yi = yi;
	}
	public double getZi() {
		return Zi;
	}
	public void setZi(double zi) {
		Zi = zi;
	}
	public double getXr() {
		return Xr;
	}
	public void setXr(double xr) {
		Xr = xr;
	}
	public double getYr() {
		return Yr;
	}
	public void setYr(double yr) {
		Yr = yr;
	}
	public double getZr() {
		return Zr;
	}
	public void setZr(double zr) {
		Zr = zr;
	}
	public double getXg() {
		return Xg;
	}
	public void setXg(double xg) {
		Xg = xg;
	}
	public double getYg() {
		return Yg;
	}
	public void setYg(double yg) {
		Yg = yg;
	}
	public double getZg() {
		return Zg;
	}
	public void setZg(double zg) {
		Zg = zg;
	}
	public boolean isOmbra() {
		return ombra;
	}
	public void setOmbra(boolean ombra) {
		this.ombra = ombra;
	}
	public double getCosLamb() {
		return cosLamb;
	}
	public void setCosLamb(double cosLamb) {
		this.cosLamb = cosLamb;
	}
	public double getCosalpha() {
		return cosalpha;
	}
	public void setCosalpha(double cosalpha) {
		this.cosalpha = cosalpha;
	}
	public void setIdObj(IdObj idObj) {
		this.idObj = idObj;
	}
	public IdObj getIdObj() {
		return idObj;
	}
	

}
