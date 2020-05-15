package graphicxtd;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
enum EllispseState {
    INI, FIN, INNER, INIFIN
 }

public class Ellipse2Dextd {
	EllispseState state=EllispseState.INI;
	Ellipse2D ellipse=null;

	public Ellipse2Dextd(Ellipse2D ellipse1, EllispseState ini) {
		// TODO Auto-generated constructor stub
		state=ini;
		ellipse=ellipse1;
	}

	public EllispseState getState() {
		return state;
	}

	public void setState(EllispseState state) {
		this.state = state;
	}

	public Ellipse2D getEllipse() {
		return ellipse;
	}

	public void setEllipse(Ellipse2D ellipse) {
		this.ellipse = ellipse;
	}

}
