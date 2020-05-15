package objectBase;

public class ColorResult {
	private double luceDirIncR=0.0; 
	private double luceDirIncG=0.0;
	private double luceDirIncB=0.0;
	private double cosB=0.0; // coseno angolo incidente
	private double cosC=0.0; // coseno angolo riflesso
	private double cosT=0.0; // coseno angolo Rifratto
	private boolean ombra=false;
	private IdObj idObj=new IdObj(-1);
	private int tipoReflex=-1;
	
	
	public double getLuceDirIncR() {
		return luceDirIncR;
	}
	public void setLuceDirIncR(double luceDirIncR) {
		this.luceDirIncR = luceDirIncR;
	}
	public double getLuceDirIncG() {
		return luceDirIncG;
	}
	public void setLuceDirIncG(double luceDirIncG) {
		this.luceDirIncG = luceDirIncG;
	}
	public double getLuceDirIncB() {
		return luceDirIncB;
	}
	public void setLuceDirIncB(double luceDirIncB) {
		this.luceDirIncB = luceDirIncB;
	}
	public double getCosB() {
		return cosB;
	}
	public void setCosB(double cosB) {
		this.cosB = cosB;
	}
	public double getCosC() {
		return cosC;
	}
	public void setCosC(double cosC) {
		this.cosC = cosC;
	}
	public boolean isOmbra() {
		return ombra;
	}
	public void setOmbra(boolean ombra) {
		this.ombra = ombra;
	}
	public double getCosT() {
		return cosT;
	}
	public void setCosT(double cosT) {
		this.cosT = cosT;
	}
	public int getTipoReflex() {
		return tipoReflex;
	}
	public void setTipoReflex(int tipoReflex) {
		this.tipoReflex = tipoReflex;
	}
	public void setIdObj(IdObj idObj) {
		this.idObj = idObj;
	}
	public IdObj getIdObj() {
		return idObj;
	}

}
