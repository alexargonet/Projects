package graphicxtd;

import java.awt.geom.Ellipse2D;

public class EllipseDrag {
	private Ellipse2Dextd ellipseb=null;
	private Ellipse2Dextd ellipsec=null;
	private Ellipse2Dextd ellipsep=null;
	
	
	public EllipseDrag(Ellipse2Dextd ellipseb, Ellipse2Dextd ellipsec, Ellipse2Dextd ellipsep) {
		super();
		this.ellipseb = ellipseb;
		this.ellipsec = ellipsec;
		this.ellipsep = ellipsep;
		
	}
	public EllipseDrag() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ellipse2Dextd getEllipseb() {
		return ellipseb;
	}
	public void setEllipseb(Ellipse2Dextd ellipseb) {
		this.ellipseb = ellipseb;
	}
	public Ellipse2Dextd getEllipsec() {
		return ellipsec;
	}
	public void setEllipsec(Ellipse2Dextd ellipsec) {
		this.ellipsec = ellipsec;
	}
	public Ellipse2Dextd getEllipsep() {
		return ellipsep;
	}
	public void setEllipsep(Ellipse2Dextd ellipsep) {
		this.ellipsep = ellipsep;
	}
	
	
}
