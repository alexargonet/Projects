package graphicxtd;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import graphicxtd.Ellipse2Dextd;
import paintimage.PaintImage;
import utility.FourPointInt;
import utility.ImageU;

@SuppressWarnings("serial")
public class JPanelControlPointBase extends JPanel implements MouseMotionListener, MouseListener {
	private static final Color BACKGROUND = new Color(0, 0, 0, 0);
	private static final Color INITIAL_COLOUR = Color.BLACK;
	private static final int BORDER_WIDTH = 5;
	private static final int BORDER_CIRCLE_WIDTH = 1;
	private static final Color LINE_DRAWING_COLOR = new Color(200, 200, 255);
	private static final Color LINE_COLOR_1 = Color.blue;
	private static final Color LINE_COLOR_2 = Color.green;
	private static final Color DASH_COLOR = Color.red;
	private static final Stroke DRAWING_LINE_STROKE = new BasicStroke((float) BORDER_WIDTH);
	private static final Stroke DRAWING_CIRCLE_STROKE = new BasicStroke((float) BORDER_CIRCLE_WIDTH);
	public static final int ELLIPSE_DIAMETER = 10;
	private MouseState mouseState = MouseState.IDLE;
	private BufferedImage bufImage = null;
	private int width;
	private int height;
	private int gridDivisions;
	private ArrayList<ArrayList<Ellipse2D>> ellipseList = new ArrayList<ArrayList<Ellipse2D>>();
	private ArrayList<ArrayList<Ellipse2Dextd>> ellipseXtdList1 = new ArrayList<ArrayList<Ellipse2Dextd>>();
	private ArrayList<ArrayList<Ellipse2Dextd>> ellipseXtdList2 = new ArrayList<ArrayList<Ellipse2Dextd>>();
	ArrayList<Ellipse2Dextd> newEndPointsXtd1 = null;
	ArrayList<Ellipse2Dextd> newEndPointsXtd2 = null;
	private Line2D drawingLine = null;
	private Ellipse2D drawingCircle = null;

	private Line2D drawingLineb = null;
	private Line2D drawingLinep = null;
	private Ellipse2D drawingCirclec = null;

	private Point2D p0;
	private Point2D p1;
	private Point2D pb;
	private Point2D pc;
	private Point2D pp;

	private EllipseDrag ellipseDrag = null;

	public enum MouseState {
		IDLE, DRAGGING, DRAWING
	}

	final static float dash1[] = { 7, 3, 1, 3 };
	final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
			0.0f);
	PaintImage originFrame = null;
	private Boolean bPhase1 = true;
	//MyMouseAdapter mouseAdapter = null;

	public JPanelControlPointBase(int width, int height, int gridDivisions) {
		this.width = width;
		this.height = height;
		this.gridDivisions = gridDivisions;
		setBackground(Color.white);
		// setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

		//mouseAdapter = new MyMouseAdapter();
		addMouseListener(this);
		addMouseMotionListener(this);

	}

	public JPanelControlPointBase(ImageU imgin, PaintImage originFrame) {
		super.setName(imgin.getName());
		
		this.bufImage = imgin.getImgBuf();
		super.setBounds(0, 0, this.bufImage.getWidth(), this.bufImage.getHeight());
		this.width = this.bufImage.getWidth();
		this.height = this.bufImage.getHeight();
		this.originFrame = originFrame;
		// this.gridDivisions = gridDivisions;
		setBackground(Color.white);
		// setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));
		
		//MyMouseAdapter mouseAdapter = new MyMouseAdapter();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bufImage == null) {
			// bufImage = createGridImage();
		} else
			g.drawImage(bufImage, 0, 0, this);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Stroke initStroke = g2.getStroke();
		g2.setStroke(DRAWING_LINE_STROKE);
		if (mouseState == MouseState.DRAGGING && drawingLine != null) {
			g2.setColor(LINE_DRAWING_COLOR);
			g2.draw(drawingLine);
			g2.setStroke(DRAWING_CIRCLE_STROKE);
			g2.draw(drawingCircle);
		} else if (mouseState == MouseState.DRAGGING && drawingCirclec != null) {
			g2.setColor(DASH_COLOR);
			g2.setStroke(dashed);
			g2.draw(drawingCirclec);
			if (drawingLineb != null)
				g2.draw(drawingLineb);
			if (drawingLinep != null)
				g2.draw(drawingLinep);
		} else if (mouseState == MouseState.DRAWING && drawingCircle != null) {
			// g2.setStroke(DRAWING_CIRCLE_STROKE);
			g2.setColor(DASH_COLOR);
			g2.setStroke(dashed);
			g2.draw(drawingCircle);
			if (drawingLine != null)
				g2.draw(drawingLine);
		}
		// orginale
		g2.setColor(LINE_COLOR_1);
		for (ArrayList<Ellipse2D> ellipses : ellipseList) {
			Point2D p2d1 = new Point2D.Double(ellipses.get(0).getCenterX(), ellipses.get(0).getCenterY());
			Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(), ellipses.get(1).getCenterY());
			Line2D line = new Line2D.Double(p2d1, p2d2);
			g2.draw(line);
		}

		// poligonale1
		for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList1) {
			g2.setStroke(initStroke);
			if (ellipseDrag != null && ellipses.contains(ellipseDrag.getEllipsec())) {
				Point2D p2d0 = null;
				for (Ellipse2Dextd ellipse : ellipses) {
					if (!ellipse.equals(ellipseDrag.getEllipsec())) {
						Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(),
								ellipse.getEllipse().getCenterY());
						g2.draw(ellipse.getEllipse());
						if (p2d0 != null) {
							// Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(),
							// ellipses.get(1).getCenterY());
							Line2D line = new Line2D.Double(p2d0, p2d1);
							g2.draw(line);
						}
						p2d0 = p2d1;
					} else// inizio d'accapo come nuova linea
						p2d0 = null;
				}
			} else {
				Point2D p2d0 = null;
				for (Ellipse2Dextd ellipse : ellipses) {
					Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(),
							ellipse.getEllipse().getCenterY());
					g2.draw(ellipse.getEllipse());
					if (p2d0 != null) {
						// Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(),
						// ellipses.get(1).getCenterY());
						Line2D line = new Line2D.Double(p2d0, p2d1);
						g2.draw(line);
					}
					p2d0 = p2d1;
				}
			}
		}

		// poligonale2
		g2.setColor(LINE_COLOR_2);
		for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList2) {
			g2.setStroke(initStroke);
			if (ellipseDrag != null && ellipses.contains(ellipseDrag.getEllipsec())) {
				Point2D p2d0 = null;
				for (Ellipse2Dextd ellipse : ellipses) {
					if (!ellipse.equals(ellipseDrag.getEllipsec())) {
						Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(),
								ellipse.getEllipse().getCenterY());
						g2.draw(ellipse.getEllipse());
						if (p2d0 != null) {
							// Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(),
							// ellipses.get(1).getCenterY());
							Line2D line = new Line2D.Double(p2d0, p2d1);
							g2.draw(line);
						}
						p2d0 = p2d1;
					} else// inizio d'accapo come nuova linea
						p2d0 = null;
				}
			} else {
				Point2D p2d0 = null;
				for (Ellipse2Dextd ellipse : ellipses) {
					Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(),
							ellipse.getEllipse().getCenterY());
					g2.draw(ellipse.getEllipse());
					if (p2d0 != null) {
						// Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(),
						// ellipses.get(1).getCenterY());
						Line2D line = new Line2D.Double(p2d0, p2d1);
						g2.draw(line);
					}
					p2d0 = p2d1;
				}
			}
		}

		// g.drawImage(bufImage, 0, 0, this);
		g2.setStroke(initStroke);
	}

	private BufferedImage createGridImage() {
		BufferedImage gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = gridImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setBackground(BACKGROUND);
		g2.clearRect(0, 0, width, height);

		int divisionSize = width / gridDivisions;
		int grid = width * height;
		g2.setColor(Color.lightGray);
		for (int i = 1; i < grid; i++) {
			int x = i * divisionSize;
			g2.drawLine(x, 0, x, getSize().height);
		}
		for (int i = 1; i < grid; i++) {
			int y = i * divisionSize;
			g2.drawLine(0, y, getSize().width, y);
		}

		g2.dispose();
		return gridImage;
	}

	/*
	 * private class MyMouseAdapter extends MouseAdapter {
	 * 
	 * 
	 * private Point2D p0; private Point2D p1;
	 * 
	 * private Point2D pb; private Point2D pc; private Point2D pp;
	 * 
	 * //******************************************** // PULSANTE PRESSED MOUSE
	 * //********************************************
	 * 
	 * @Override public void mousePressed(MouseEvent e) { if(bPhase1) {
	 * 
	 * if (e.getButton() == MouseEvent.BUTTON1) {
	 * 
	 * // controlo se il punto cliccato appartiene a qualche spigolo della
	 * poligonale // nel caso recupera lo spigolo precedente e successivo se
	 * presenti //if(e.getClickCount() == 2) { for (ArrayList<Ellipse2Dextd>
	 * ellipses : ellipseXtdList1) { for (int i = 0; i < ellipses.size(); i++) {
	 * Ellipse2Dextd endPt = ellipses.get(i); // cotrolla se il punto del cliccato
	 * dal mouse è in qualche poligonale
	 * if(endPt.getEllipse().contains(e.getPoint())) { pb = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); pc = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); pp = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); ellipseDrag = new EllipseDrag(); // setta
	 * il punto centrale o cliccato ellipseDrag.setEllipsec(endPt);
	 * drawingLineb=null; drawingLinep=null; //recupera il punto precedente
	 * b=beforese presente if(i-1>=0){
	 * ellipseDrag.setEllipseb(ellipses.get(Math.abs(i - 1)));
	 * pb.setLocation(ellipses.get(Math.abs(i -
	 * 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i -
	 * 1)).getEllipse().getCenterY()); drawingLineb = new Line2D.Double(pb, pc); }
	 * // recupera il punto successivo p=post se presente if(i+1<ellipses.size()){
	 * ellipseDrag.setEllipsep(ellipses.get(Math.abs(i + 1)));
	 * pp.setLocation(ellipses.get(Math.abs(i +
	 * 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i +
	 * 1)).getEllipse().getCenterY()); drawingLinep = new Line2D.Double(pc, pp); }
	 * drawingCirclec = new Ellipse2D.Double(pc.getX()- ELLIPSE_DIAMETER /
	 * 2,pc.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER); //
	 * entra nello stato DRAGGING mouseState = MouseState.DRAGGING; repaint();
	 * return; } } } //}
	 * 
	 * p0=p1; p1 = e.getPoint(); repaint(); } else if(e.getButton() ==
	 * MouseEvent.BUTTON3) { if(mouseState == MouseState.DRAWING){ mouseState =
	 * MouseState.IDLE; drawingCircle=null; drawingLine=null; repaint(); } }
	 * 
	 * }else { if (e.getButton() == MouseEvent.BUTTON1) {
	 * 
	 * // controlo se il punto cliccato appartiene a qualche spigolo della
	 * poligonale // nel caso recupera lo spigolo precedente e successivo se
	 * presenti //if(e.getClickCount() == 2) { for (ArrayList<Ellipse2Dextd>
	 * ellipses : ellipseXtdList2) { for (int i = 0; i < ellipses.size(); i++) {
	 * Ellipse2Dextd endPt = ellipses.get(i); // cotrolla se il punto del cliccato
	 * dal mouse è in qualche poligonale
	 * if(endPt.getEllipse().contains(e.getPoint())) { pb = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); pc = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); pp = new
	 * Point2D.Double(endPt.getEllipse().getCenterX(),
	 * endPt.getEllipse().getCenterY()); ellipseDrag = new EllipseDrag(); // setta
	 * il punto centrale o cliccato ellipseDrag.setEllipsec(endPt);
	 * drawingLineb=null; drawingLinep=null; //recupera il punto precedente
	 * b=beforese presente if(i-1>=0){
	 * ellipseDrag.setEllipseb(ellipses.get(Math.abs(i - 1)));
	 * pb.setLocation(ellipses.get(Math.abs(i -
	 * 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i -
	 * 1)).getEllipse().getCenterY()); drawingLineb = new Line2D.Double(pb, pc); }
	 * // recupera il punto successivo p=post se presente if(i+1<ellipses.size()){
	 * ellipseDrag.setEllipsep(ellipses.get(Math.abs(i + 1)));
	 * pp.setLocation(ellipses.get(Math.abs(i +
	 * 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i +
	 * 1)).getEllipse().getCenterY()); drawingLinep = new Line2D.Double(pc, pp); }
	 * drawingCirclec = new Ellipse2D.Double(pc.getX()- ELLIPSE_DIAMETER /
	 * 2,pc.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER); //
	 * entra nello stato DRAGGING mouseState = MouseState.DRAGGING; repaint();
	 * return; } } } //}
	 * 
	 * p0=p1; p1 = e.getPoint(); repaint(); } } }
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
	 * 
	 * //**************************************************** // MOUSE MOVE
	 * //****************************************************
	 * 
	 * @Override public void mouseMoved(MouseEvent e){ if(bPhase1) { if(mouseState
	 * == MouseState.DRAWING){ drawingLine = new Line2D.Double(p1, e.getPoint());
	 * drawingCircle = new Ellipse2D.Double(e.getPoint().getX()- ELLIPSE_DIAMETER /
	 * 2, e.getPoint().getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER,
	 * ELLIPSE_DIAMETER); repaint(); } }else {
	 * 
	 * } }
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 * 
	 * //******************************* // MOUSE DRAGGED
	 * //*******************************
	 * 
	 * @Override public void mouseDragged(MouseEvent e) { if(bPhase1) { if
	 * (mouseState != MouseState.DRAGGING) { return; } if(mouseState ==
	 * MouseState.DRAWING){ drawingLine = new Line2D.Double(p0, p1); //drawingCircle
	 * = new Ellipse2D.Double(p0.getX()- ELLIPSE_DIAMETER / 2, p0.getY()-
	 * ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER); repaint(); p1 =
	 * e.getPoint(); } if(mouseState == MouseState.DRAGGING){ if(ellipseDrag!=null){
	 * drawingLineb=null; drawingLinep=null; pc.setLocation(e.getX(),e.getY());
	 * if(ellipseDrag.getEllipseb()!=null){
	 * pb.setLocation(ellipseDrag.getEllipseb().getEllipse().getCenterX(),
	 * ellipseDrag.getEllipseb().getEllipse().getCenterY()); drawingLineb = new
	 * Line2D.Double(pb, pc); } if(ellipseDrag.getEllipsep()!=null){
	 * pp.setLocation(ellipseDrag.getEllipsep().getEllipse().getCenterX(),
	 * ellipseDrag.getEllipsep().getEllipse().getCenterY()); drawingLinep = new
	 * Line2D.Double(pc, pp); } drawingCirclec = new Ellipse2D.Double(pc.getX()-
	 * ELLIPSE_DIAMETER / 2,pc.getY()- ELLIPSE_DIAMETER /
	 * 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER); repaint(); } } } else { if(mouseState
	 * == MouseState.DRAGGING){ if(ellipseDrag!=null){ drawingLineb=null;
	 * drawingLinep=null; pc.setLocation(e.getX(),e.getY());
	 * if(ellipseDrag.getEllipseb()!=null){
	 * pb.setLocation(ellipseDrag.getEllipseb().getEllipse().getCenterX(),
	 * ellipseDrag.getEllipseb().getEllipse().getCenterY()); drawingLineb = new
	 * Line2D.Double(pb, pc); } if(ellipseDrag.getEllipsep()!=null){
	 * pp.setLocation(ellipseDrag.getEllipsep().getEllipse().getCenterX(),
	 * ellipseDrag.getEllipsep().getEllipse().getCenterY()); drawingLinep = new
	 * Line2D.Double(pc, pp); } drawingCirclec = new Ellipse2D.Double(pc.getX()-
	 * ELLIPSE_DIAMETER / 2,pc.getY()- ELLIPSE_DIAMETER /
	 * 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER); repaint(); } } } }
	 * //***************************************** // MOUSE RELEASEd
	 * //*****************************************
	 * 
	 * @Override public void mouseReleased(MouseEvent e) { super.mouseReleased(e);
	 * 
	 * if(bPhase1) {
	 * 
	 * //********************************************************* // ADD A NEW
	 * POINT //*********************************************************
	 * if(e.getButton() == MouseEvent.BUTTON1 && mouseState != MouseState.DRAGGING){
	 * 
	 * p0=p1; p1 = e.getPoint();
	 * 
	 * if(mouseState == MouseState.IDLE){ newEndPointsXtd1 = new
	 * ArrayList<Ellipse2Dextd>(); ellipseXtdList1.add(newEndPointsXtd1); } double
	 * x1 = p1.getX() - ELLIPSE_DIAMETER / 2; double y1 = p1.getY() -
	 * ELLIPSE_DIAMETER / 2; Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1,
	 * ELLIPSE_DIAMETER, ELLIPSE_DIAMETER); Ellipse2Dextd ellipseextd1 = new
	 * Ellipse2Dextd(ellipse1,EllispseState.INI);
	 * newEndPointsXtd1.add(ellipseextd1); mouseState = MouseState.DRAWING;
	 * repaint();
	 * 
	 * }else if(e.getButton() == MouseEvent.BUTTON1 && mouseState ==
	 * MouseState.DRAGGING){ if(mouseState == MouseState.DRAGGING){
	 * if(ellipseDrag!=null){ double x1 = e.getX() - ELLIPSE_DIAMETER / 2; double y1
	 * = e.getY() - ELLIPSE_DIAMETER / 2;
	 * ellipseDrag.getEllipsec().getEllipse().setFrame(x1, y1, ELLIPSE_DIAMETER,
	 * ELLIPSE_DIAMETER); ellipseDrag=null; drawingCirclec=null; drawingLineb=null;
	 * drawingLinep=null; mouseState = MouseState.IDLE; repaint(); } } } else
	 * if(e.getButton() == MouseEvent.BUTTON3) { if(mouseState != MouseState.IDLE){
	 * mouseState = MouseState.IDLE; drawingCircle=null; drawingLine=null;
	 * ellipseDrag=null; drawingCirclec=null; drawingLineb=null; drawingLinep=null;
	 * repaint(); } } // propago gli eventi del mouse al parent
	 * //if(originFrame!=null) //originFrame.mouseReleased(e); } else {
	 * //************************************ // PHASE2
	 * //************************************ if(e.getButton() == MouseEvent.BUTTON1
	 * && mouseState == MouseState.DRAGGING){ if(mouseState == MouseState.DRAGGING){
	 * if(ellipseDrag!=null){ double x1 = e.getX() - ELLIPSE_DIAMETER / 2; double y1
	 * = e.getY() - ELLIPSE_DIAMETER / 2;
	 * ellipseDrag.getEllipsec().getEllipse().setFrame(x1, y1, ELLIPSE_DIAMETER,
	 * ELLIPSE_DIAMETER); ellipseDrag=null; drawingCirclec=null; drawingLineb=null;
	 * drawingLinep=null; mouseState = MouseState.IDLE; repaint(); } } } else
	 * if(e.getButton() == MouseEvent.BUTTON3) { if(mouseState != MouseState.IDLE){
	 * mouseState = MouseState.IDLE; drawingCircle=null; drawingLine=null;
	 * ellipseDrag=null; drawingCirclec=null; drawingLineb=null; drawingLinep=null;
	 * repaint(); } } // propago gli eventi del mouse al parent
	 * //if(originFrame!=null) //originFrame.mouseReleased(e);
	 * 
	 * } } }
	 */

	public Boolean getbPhase1() {
		return bPhase1;
	}

	public void setbPhase1(Boolean bPhase1) {
		this.bPhase1 = bPhase1;
		if (bPhase1) {

		} else {
			// **************************************************************
			// CLONO LA LISTA DEI POLIGONI1 NELLA LISTA DEI POLIGONI2
			// **************************************************************
			for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList1) {
				newEndPointsXtd2 = new ArrayList<Ellipse2Dextd>();
				ellipseXtdList2.add(newEndPointsXtd2);
				for (Ellipse2Dextd ellipse1 : ellipses) {
					Ellipse2D ellipse2 = new Ellipse2D.Double(ellipse1.getEllipse().getX(),
							ellipse1.getEllipse().getY(), ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
					Ellipse2Dextd ellipseextd2 = new Ellipse2Dextd(ellipse2, EllispseState.INI);
					newEndPointsXtd2.add(ellipseextd2);
				}
			}
			repaint();
		}
	}

	public ArrayList<FourPointInt> getDisplacement() {
		ArrayList<FourPointInt> morphDisplList = null;
		// se phase 2
		if (!bPhase1) {
			int i = 0;
			FourPointInt fourPointm = null;
			morphDisplList = new ArrayList<FourPointInt>();

			for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList1) {
				for (Ellipse2Dextd ellipse1 : ellipses) {
					fourPointm = new FourPointInt();
					fourPointm.setX((int) Math.round(ellipse1.getEllipse().getCenterX()));
					fourPointm.setY((int) Math.round(ellipse1.getEllipse().getCenterY()));
					morphDisplList.add(fourPointm);
				}
			}

			for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList2) {
				for (Ellipse2Dextd ellipse2 : ellipses) {
					morphDisplList.get(i).setDx((int) Math.round(ellipse2.getEllipse().getCenterX()));
					morphDisplList.get(i).setDy((int) Math.round(ellipse2.getEllipse().getCenterY()));
					i++;
				}
			}
		}
		return morphDisplList;
	}

	public ArrayList<FourPointInt> getControlPoint() {
		ArrayList<FourPointInt> morphDisplList = null;
		if (bPhase1) {
			int i = 0;
			FourPointInt fourPointm = null;
			morphDisplList = new ArrayList<FourPointInt>();
			for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList1) {
				for (Ellipse2Dextd ellipse1 : ellipses) {
					fourPointm = new FourPointInt();
					fourPointm.setX((int) Math.round(ellipse1.getEllipse().getCenterX()));
					fourPointm.setY((int) Math.round(ellipse1.getEllipse().getCenterY()));
					morphDisplList.add(fourPointm);
				}
			}
		}
		return morphDisplList;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

		if (bPhase1) {

			if (e.getButton() == MouseEvent.BUTTON1) {

				// controlo se il punto cliccato appartiene a qualche spigolo della poligonale
				// nel caso recupera lo spigolo precedente e successivo se presenti
				// if(e.getClickCount() == 2) {
				for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList1) {
					for (int i = 0; i < ellipses.size(); i++) {
						Ellipse2Dextd endPt = ellipses.get(i);
						// cotrolla se il punto del cliccato dal mouse è in qualche poligonale
						if (endPt.getEllipse().contains(e.getPoint())) {
							pb = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							pc = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							pp = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							ellipseDrag = new EllipseDrag();
							// setta il punto centrale o cliccato
							ellipseDrag.setEllipsec(endPt);
							drawingLineb = null;
							drawingLinep = null;
							// recupera il punto precedente b=beforese presente
							if (i - 1 >= 0) {
								ellipseDrag.setEllipseb(ellipses.get(Math.abs(i - 1)));
								pb.setLocation(ellipses.get(Math.abs(i - 1)).getEllipse().getCenterX(),
										ellipses.get(Math.abs(i - 1)).getEllipse().getCenterY());
								drawingLineb = new Line2D.Double(pb, pc);
							}
							// recupera il punto successivo p=post se presente
							if (i + 1 < ellipses.size()) {
								ellipseDrag.setEllipsep(ellipses.get(Math.abs(i + 1)));
								pp.setLocation(ellipses.get(Math.abs(i + 1)).getEllipse().getCenterX(),
										ellipses.get(Math.abs(i + 1)).getEllipse().getCenterY());
								drawingLinep = new Line2D.Double(pc, pp);
							}
							drawingCirclec = new Ellipse2D.Double(pc.getX() - ELLIPSE_DIAMETER / 2,
									pc.getY() - ELLIPSE_DIAMETER / 2, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
							// entra nello stato DRAGGING
							mouseState = MouseState.DRAGGING;
							repaint();
							return;
						}
					}
				}
				// }

				p0 = p1;
				p1 = e.getPoint();
				repaint();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				/*
				 * if(mouseState == MouseState.DRAWING){ mouseState = MouseState.IDLE;
				 * drawingCircle=null; drawingLine=null; repaint(); }
				 */
			}

		} else {
			if (e.getButton() == MouseEvent.BUTTON1) {

				// controlo se il punto cliccato appartiene a qualche spigolo della poligonale
				// nel caso recupera lo spigolo precedente e successivo se presenti
				// if(e.getClickCount() == 2) {
				for (ArrayList<Ellipse2Dextd> ellipses : ellipseXtdList2) {
					for (int i = 0; i < ellipses.size(); i++) {
						Ellipse2Dextd endPt = ellipses.get(i);
						// cotrolla se il punto del cliccato dal mouse è in qualche poligonale
						if (endPt.getEllipse().contains(e.getPoint())) {
							pb = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							pc = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							pp = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
							ellipseDrag = new EllipseDrag();
							// setta il punto centrale o cliccato
							ellipseDrag.setEllipsec(endPt);
							drawingLineb = null;
							drawingLinep = null;
							// recupera il punto precedente b=beforese presente
							if (i - 1 >= 0) {
								ellipseDrag.setEllipseb(ellipses.get(Math.abs(i - 1)));
								pb.setLocation(ellipses.get(Math.abs(i - 1)).getEllipse().getCenterX(),
										ellipses.get(Math.abs(i - 1)).getEllipse().getCenterY());
								drawingLineb = new Line2D.Double(pb, pc);
							}
							// recupera il punto successivo p=post se presente
							if (i + 1 < ellipses.size()) {
								ellipseDrag.setEllipsep(ellipses.get(Math.abs(i + 1)));
								pp.setLocation(ellipses.get(Math.abs(i + 1)).getEllipse().getCenterX(),
										ellipses.get(Math.abs(i + 1)).getEllipse().getCenterY());
								drawingLinep = new Line2D.Double(pc, pp);
							}
							drawingCirclec = new Ellipse2D.Double(pc.getX() - ELLIPSE_DIAMETER / 2,
									pc.getY() - ELLIPSE_DIAMETER / 2, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
							// entra nello stato DRAGGING
							mouseState = MouseState.DRAGGING;
							repaint();
							return;
						}
					}
				}
				// }

				p0 = p1;
				p1 = e.getPoint();
				repaint();
			}
		}
		if(originFrame!=null)
			originFrame.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//super.mouseReleased(e);

		if (bPhase1) {

			// *********************************************************
			// ADD A NEW POINT
			// *********************************************************
			if (e.getButton() == MouseEvent.BUTTON1 && mouseState != MouseState.DRAGGING) {

				p0 = p1;
				p1 = e.getPoint();

				if (mouseState == MouseState.IDLE) {
					newEndPointsXtd1 = new ArrayList<Ellipse2Dextd>();
					ellipseXtdList1.add(newEndPointsXtd1);
				}
				double x1 = p1.getX() - ELLIPSE_DIAMETER / 2;
				double y1 = p1.getY() - ELLIPSE_DIAMETER / 2;
				Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
				Ellipse2Dextd ellipseextd1 = new Ellipse2Dextd(ellipse1, EllispseState.INI);
				newEndPointsXtd1.add(ellipseextd1);
				mouseState = MouseState.DRAWING;
				repaint();

			} else if (e.getButton() == MouseEvent.BUTTON1 && mouseState == MouseState.DRAGGING) {
				if (mouseState == MouseState.DRAGGING) {
					if (ellipseDrag != null) {
						double x1 = e.getX() - ELLIPSE_DIAMETER / 2;
						double y1 = e.getY() - ELLIPSE_DIAMETER / 2;
						ellipseDrag.getEllipsec().getEllipse().setFrame(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
						ellipseDrag = null;
						drawingCirclec = null;
						drawingLineb = null;
						drawingLinep = null;
						mouseState = MouseState.IDLE;
						repaint();
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (mouseState != MouseState.IDLE) {
					mouseState = MouseState.IDLE;
					drawingCircle = null;
					drawingLine = null;
					ellipseDrag = null;
					drawingCirclec = null;
					drawingLineb = null;
					drawingLinep = null;
					repaint();
				}
			}
			// propago gli eventi del mouse al parent
			// if(originFrame!=null)
			// originFrame.mouseReleased(e);
		} else {
			// ************************************
			// PHASE2
			// ************************************
			if (e.getButton() == MouseEvent.BUTTON1 && mouseState == MouseState.DRAGGING) {
				if (mouseState == MouseState.DRAGGING) {
					if (ellipseDrag != null) {
						double x1 = e.getX() - ELLIPSE_DIAMETER / 2;
						double y1 = e.getY() - ELLIPSE_DIAMETER / 2;
						ellipseDrag.getEllipsec().getEllipse().setFrame(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
						ellipseDrag = null;
						drawingCirclec = null;
						drawingLineb = null;
						drawingLinep = null;
						mouseState = MouseState.IDLE;
						repaint();
					}
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				if (mouseState != MouseState.IDLE) {
					mouseState = MouseState.IDLE;
					drawingCircle = null;
					drawingLine = null;
					ellipseDrag = null;
					drawingCirclec = null;
					drawingLineb = null;
					drawingLinep = null;
					repaint();
				}
			}
			// propago gli eventi del mouse al parent
			// if(originFrame!=null)
			// originFrame.mouseReleased(e);

		}
	}

	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (bPhase1) {
			/*
			 * if (mouseState != MouseState.DRAGGING) { return; }
			 */
			/*
			 * if(mouseState == MouseState.DRAWING){ drawingLine = new Line2D.Double(p0,
			 * p1); //drawingCircle = new Ellipse2D.Double(p0.getX()- ELLIPSE_DIAMETER / 2,
			 * p0.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
			 * repaint(); p1 = e.getPoint(); }
			 */
			if (mouseState == MouseState.DRAGGING) {
				if (ellipseDrag != null) {
					drawingLineb = null;
					drawingLinep = null;
					pc.setLocation(e.getX(), e.getY());
					if (ellipseDrag.getEllipseb() != null) {
						pb.setLocation(ellipseDrag.getEllipseb().getEllipse().getCenterX(),
								ellipseDrag.getEllipseb().getEllipse().getCenterY());
						drawingLineb = new Line2D.Double(pb, pc);
					}
					if (ellipseDrag.getEllipsep() != null) {
						pp.setLocation(ellipseDrag.getEllipsep().getEllipse().getCenterX(),
								ellipseDrag.getEllipsep().getEllipse().getCenterY());
						drawingLinep = new Line2D.Double(pc, pp);
					}
					drawingCirclec = new Ellipse2D.Double(pc.getX() - ELLIPSE_DIAMETER / 2,
							pc.getY() - ELLIPSE_DIAMETER / 2, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
					repaint();
				}
			}
		} else {
			if (mouseState == MouseState.DRAGGING) {
				if (ellipseDrag != null) {
					drawingLineb = null;
					drawingLinep = null;
					pc.setLocation(e.getX(), e.getY());
					if (ellipseDrag.getEllipseb() != null) {
						pb.setLocation(ellipseDrag.getEllipseb().getEllipse().getCenterX(),
								ellipseDrag.getEllipseb().getEllipse().getCenterY());
						drawingLineb = new Line2D.Double(pb, pc);
					}
					if (ellipseDrag.getEllipsep() != null) {
						pp.setLocation(ellipseDrag.getEllipsep().getEllipse().getCenterX(),
								ellipseDrag.getEllipsep().getEllipse().getCenterY());
						drawingLinep = new Line2D.Double(pc, pp);
					}
					drawingCirclec = new Ellipse2D.Double(pc.getX() - ELLIPSE_DIAMETER / 2,
							pc.getY() - ELLIPSE_DIAMETER / 2, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
					repaint();
				}
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if (bPhase1) {
			if (mouseState == MouseState.DRAWING) {
				drawingLine = new Line2D.Double(p1, e.getPoint());
				drawingCircle = new Ellipse2D.Double(e.getPoint().getX() - ELLIPSE_DIAMETER / 2,
						e.getPoint().getY() - ELLIPSE_DIAMETER / 2, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
				repaint();
			}
		} else {

		}
	}

	public PaintImage getOriginFrame() {
		return originFrame;
	}

	public void setOriginFrame(PaintImage originFrame) {
		this.originFrame = originFrame;
	}
	
	public BufferedImage getBufImage() {
		return bufImage;
	}

	public void setBufImage(BufferedImage bufImage) {
		this.bufImage = bufImage;
	}
	

}
