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



@SuppressWarnings("serial")
public class JPanelControlPoint extends JPanel {
   private static final Color BACKGROUND = new Color(0, 0, 0, 0);
   private static final Color INITIAL_COLOUR = Color.BLACK;
   private static final int BORDER_WIDTH = 5;
   private static final int BORDER_CIRCLE_WIDTH = 1;
   private static final Color LINE_DRAWING_COLOR = new Color(200, 200, 255);
   private static final Color LINE_COLOR = Color.blue;
   private static final Color DASH_COLOR = Color.red;
   private static final Stroke DRAWING_LINE_STROKE = new BasicStroke((float)BORDER_WIDTH);
   private static final Stroke DRAWING_CIRCLE_STROKE = new BasicStroke((float)BORDER_CIRCLE_WIDTH);
   public static final int ELLIPSE_DIAMETER = 10;
   private MouseState mouseState = MouseState.IDLE;
   private BufferedImage bufImage = null;
   private int width;
   private int height;
   private int gridDivisions;
   private List<List<Ellipse2D>> ellipseList = new ArrayList<List<Ellipse2D>>();
   private List<List<Ellipse2Dextd>> ellipseXtdList = new ArrayList<List<Ellipse2Dextd>>();
   List<Ellipse2Dextd> newEndPointsXtd=null;
   private Line2D drawingLine = null;
   private Ellipse2D drawingCircle = null;
   
   private Line2D drawingLineb = null;
   private Line2D drawingLinep = null;
   private Ellipse2D drawingCirclec = null;
   
   private EllipseDrag ellipseDrag=null;

   public enum MouseState {
      IDLE, DRAGGING, DRAWING
   }
   
   final static float dash1[] = {7,3,1,3};
   final static BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
   

   public JPanelControlPoint(int width, int height, int gridDivisions) {
      this.width = width;
      this.height = height;
      this.gridDivisions = gridDivisions;
      setBackground(Color.white);
      //setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

      MyMouseAdapter mouseAdapter = new MyMouseAdapter();
      addMouseListener(mouseAdapter);
      addMouseMotionListener(mouseAdapter);
   }
   
   public JPanelControlPoint(BufferedImage imgin) {
	   	  this.bufImage=imgin;
	      this.width = this.bufImage.getWidth();
	      this.height = this.bufImage.getHeight();
	      //this.gridDivisions = gridDivisions;
	      setBackground(Color.white);
	      //setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

	      MyMouseAdapter mouseAdapter = new MyMouseAdapter();
	      addMouseListener(mouseAdapter);
	      addMouseMotionListener(mouseAdapter);
	   }
   
   

//   private static void createAndShowGui() {
//      int w = 600;
//      int h = w;
//      int gridDiv = 20;
//      JPanelControlPoin mainPanel = new JPanelControlPoin(w, h, gridDiv);
//
//      JFrame frame = new JFrame("Floor2");
//      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      frame.getContentPane().add(mainPanel);
//      frame.pack();
//      frame.setLocationRelativeTo(null);
//      frame.setVisible(true);
//   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(width, height);
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (bufImage == null) {
         //bufImage = createGridImage();
      }
      else 
    	  g.drawImage(bufImage, 0, 0, this);

      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

      Stroke initStroke = g2.getStroke();
      g2.setStroke(DRAWING_LINE_STROKE);
      if (mouseState == MouseState.DRAGGING && drawingLine != null) {
         g2.setColor(LINE_DRAWING_COLOR);
         g2.draw(drawingLine);
         g2.setStroke(DRAWING_CIRCLE_STROKE);
         g2.draw(drawingCircle);
      }
      else if(mouseState == MouseState.DRAGGING && drawingCirclec != null){
    	  g2.setColor(DASH_COLOR);
    	  g2.setStroke(dashed);  
          g2.draw(drawingCirclec);
          if(drawingLineb != null)
        	  g2.draw(drawingLineb);
          if(drawingLinep != null)
        	  g2.draw(drawingLinep);
      }
      else if(mouseState==MouseState.DRAWING && drawingCircle != null){
          //g2.setStroke(DRAWING_CIRCLE_STROKE);
    	  g2.setColor(DASH_COLOR);
          g2.setStroke(dashed);  
          g2.draw(drawingCircle);
          if(drawingLine != null)
        	  g2.draw(drawingLine);
      }
      // orginale
      g2.setColor(LINE_COLOR);
      for (List<Ellipse2D> ellipses : ellipseList) {
         Point2D p2d1 = new Point2D.Double(ellipses.get(0).getCenterX(), ellipses.get(0).getCenterY());
         Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(), ellipses.get(1).getCenterY());
         Line2D line = new Line2D.Double(p2d1, p2d2);
         g2.draw(line);
      }
      
    // poligonale
	for (List<Ellipse2Dextd> ellipses : ellipseXtdList) {
		g2.setStroke(initStroke);
		if(ellipseDrag!=null && ellipses.contains(ellipseDrag.getEllipsec())){
			Point2D p2d0=null;
			for (Ellipse2Dextd ellipse : ellipses) {
				if(!ellipse.equals(ellipseDrag.getEllipsec())){
				    Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(), ellipse.getEllipse().getCenterY());
				    g2.draw(ellipse.getEllipse());
				    if(p2d0!=null){
				        //Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(), ellipses.get(1).getCenterY());
				        Line2D line = new Line2D.Double(p2d0, p2d1);
				        g2.draw(line);
				    }
				    p2d0=p2d1;	 
				}
				else// inizio d'accapo come nuova linea
					p2d0=null;
			}		
		}
		else{
			Point2D p2d0=null;
			for (Ellipse2Dextd ellipse : ellipses) {
				    Point2D p2d1 = new Point2D.Double(ellipse.getEllipse().getCenterX(), ellipse.getEllipse().getCenterY());
				    g2.draw(ellipse.getEllipse());
				    if(p2d0!=null){
				        //Point2D p2d2 = new Point2D.Double(ellipses.get(1).getCenterX(), ellipses.get(1).getCenterY());
				        Line2D line = new Line2D.Double(p2d0, p2d1);
				        g2.draw(line);
				    }
				    p2d0=p2d1;
			}
		}
	}

      //g.drawImage(bufImage, 0, 0, this);
      g2.setStroke(initStroke);
   }

   private BufferedImage createGridImage() {
      BufferedImage gridImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = gridImage.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
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

   /*private class MyMouseAdapter extends MouseAdapter {
      private Point p1;

      @Override
      public void mousePressed(MouseEvent e) {
         if (e.getButton() != MouseEvent.BUTTON1) {
            return;
         }

         for (List<Ellipse2D> endPts : ellipseList) {
            // check if one of the ellipses has been selected
            // if so, remove it from elipseList
            // set drawingLine == to end points
            // setdragging = true
            // repaint
            // return
         }
         mouseState = MouseState.DRAGGING;
         p1 = e.getPoint();
      }

      @Override
      public void mouseDragged(MouseEvent e) {
         if (mouseState != MouseState.DRAGGING) {
            return;
         }
         drawingLine = new Line2D.Double(p1, e.getPoint());
         repaint();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
         if (drawingLine != null) {
            List<Ellipse2D> newEndPoints = new ArrayList<Ellipse2D>();

            double x1 = drawingLine.getX1() - ELLIPSE_DIAMETER / 2;
            double y1 = drawingLine.getY1() - ELLIPSE_DIAMETER / 2;
            Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
            x1 = drawingLine.getX2() - ELLIPSE_DIAMETER / 2;
            y1 = drawingLine.getY2() - ELLIPSE_DIAMETER / 2;
            Ellipse2D ellipse2 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
            newEndPoints.add(ellipse1);
            newEndPoints.add(ellipse2);
            ellipseList.add(newEndPoints);
            repaint();
         }

         mouseState = MouseState.IDLE;
         drawingLine = null;
      }
   }*/

   /*public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }*/
   
   private class MyMouseAdapter extends MouseAdapter {
	   private Point2D p0;
	   private Point2D p1;
	   
	   private Point2D pb;
	   private Point2D pc;
	   private Point2D pp;
	   

	   @Override
	   public void mousePressed(MouseEvent e) {
		   /*if (e.getButton() != MouseEvent.BUTTON1) {
	         return;
	      }

	      for (List<Ellipse2D> endPts : ellipseList) {
	         for (int i = 0; i < endPts.size(); i++) {
	            Ellipse2D endPt = endPts.get(i);
	            if (endPt.contains(e.getPoint())) {
	               Ellipse2D endPt2 = endPts.get(Math.abs(i - 1));
	               ellipseList.remove(endPts);

	               Point2D p2 = new Point2D.Double(endPt.getCenterX(), endPt.getCenterY());
	               p1 = new Point2D.Double(endPt2.getCenterX(), endPt2.getCenterY());
	               drawingLine = new Line2D.Double(p1, p2);
	               mouseState = MouseState.DRAGGING;
	               repaint();
	               return;
	            }
	         }
	      }
	      mouseState = MouseState.DRAGGING;
	      p1 = e.getPoint();*/

		   if (e.getButton() == MouseEvent.BUTTON1) {

			   /*if(mouseState != MouseState.DRAGGING){
				  p0=p1; 
				  p1 = e.getPoint();
				  mouseState = MouseState.DRAWING;
				  drawingCircle = new Ellipse2D.Double(p1.getX()- ELLIPSE_DIAMETER / 2, p1.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
				  repaint();

			  }*/
			   // controlo se il punto cliccato appartiene a qualche spigolo della poligonale
			   // nel caso recupera lo spigolo precedente e successivo se presenti
			   for (List<Ellipse2Dextd> ellipses : ellipseXtdList) {
				   for (int i = 0; i < ellipses.size(); i++) {
					   Ellipse2Dextd endPt = ellipses.get(i);
					   // cotrolla se il punto del cliccato dal mouse è in qualche poligonale
					   if(endPt.getEllipse().contains(e.getPoint())) {
						   pb = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
						   pc = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
						   pp = new Point2D.Double(endPt.getEllipse().getCenterX(), endPt.getEllipse().getCenterY());
						   ellipseDrag = new EllipseDrag();
						   // setta il punto centrale o cliccato
						   ellipseDrag.setEllipsec(endPt);
						   drawingLineb=null;
						   drawingLinep=null;
						   //recupera il punto precedente b=beforese presente
						   if(i-1>=0){
							   ellipseDrag.setEllipseb(ellipses.get(Math.abs(i - 1)));
							   pb.setLocation(ellipses.get(Math.abs(i - 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i - 1)).getEllipse().getCenterY());
							   drawingLineb = new Line2D.Double(pb, pc);
						   }
						   // recupera il punto successivo p=post se presente
						   if(i+1<ellipses.size()){
							   ellipseDrag.setEllipsep(ellipses.get(Math.abs(i + 1)));
							   pp.setLocation(ellipses.get(Math.abs(i + 1)).getEllipse().getCenterX(),ellipses.get(Math.abs(i + 1)).getEllipse().getCenterY());
							   drawingLinep = new Line2D.Double(pc, pp);
						   }
						   drawingCirclec = new Ellipse2D.Double(pc.getX()- ELLIPSE_DIAMETER / 2,pc.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER);
						   // entra nello stato DRAGGING
						   mouseState = MouseState.DRAGGING;
						   repaint();
						   return;
					   }
				   }
			   }

			   p0=p1; 
			   p1 = e.getPoint();
			   repaint();
		   }
		   else if(e.getButton() == MouseEvent.BUTTON3) {
			   /* if(mouseState == MouseState.DRAWING){
				  mouseState = MouseState.IDLE;
				  drawingCircle=null;
				  drawingLine=null;
				  repaint();
			  }*/
		   }
	   }


	   /* (non-Javadoc)
	    * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
	    */
	   @Override
	   public void mouseMoved(MouseEvent e){
		   if(mouseState == MouseState.DRAWING){
			   drawingLine = new Line2D.Double(p1, e.getPoint());
			   drawingCircle = new Ellipse2D.Double(e.getPoint().getX()- ELLIPSE_DIAMETER / 2, e.getPoint().getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
			   repaint();
		   }
	   }

	 /* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		/*if (mouseState != MouseState.DRAGGING) {
	         return;
	      }*/
		/*if(mouseState == MouseState.DRAWING){
		   drawingLine = new Line2D.Double(p0, p1);
		   //drawingCircle = new Ellipse2D.Double(p0.getX()- ELLIPSE_DIAMETER / 2, p0.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
		   repaint();
		   p1 = e.getPoint();
		  }*/
		if(mouseState == MouseState.DRAGGING){
			if(ellipseDrag!=null){
			   drawingLineb=null;
			   drawingLinep=null;
			   pc.setLocation(e.getX(),e.getY());
			   if(ellipseDrag.getEllipseb()!=null){
				   pb.setLocation(ellipseDrag.getEllipseb().getEllipse().getCenterX(),ellipseDrag.getEllipseb().getEllipse().getCenterY());
				   drawingLineb = new Line2D.Double(pb, pc);
			   }
			   if(ellipseDrag.getEllipsep()!=null){
				   pp.setLocation(ellipseDrag.getEllipsep().getEllipse().getCenterX(),ellipseDrag.getEllipsep().getEllipse().getCenterY());
				   drawingLinep = new Line2D.Double(pc, pp);
			   }
			   drawingCirclec = new Ellipse2D.Double(pc.getX()- ELLIPSE_DIAMETER / 2,pc.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER,ELLIPSE_DIAMETER);
			   repaint();
			}
		}
	}

	   @Override
	   public void mouseReleased(MouseEvent e) {
		   super.mouseReleased(e);
		   /*if (drawingLine != null) {
	         List<Ellipse2D> newEndPoints = new ArrayList<Ellipse2D>();

	         double x1 = drawingLine.getX1() - ELLIPSE_DIAMETER / 2;
	         double y1 = drawingLine.getY1() - ELLIPSE_DIAMETER / 2;
	         Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
	         x1 = drawingLine.getX2() - ELLIPSE_DIAMETER / 2;
	         y1 = drawingLine.getY2() - ELLIPSE_DIAMETER / 2;
	         Ellipse2D ellipse2 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
	         newEndPoints.add(ellipse1);
	         newEndPoints.add(ellipse2);
	         ellipseList.add(newEndPoints);
	         repaint();
	      }

	      mouseState = MouseState.IDLE;
	      drawingLine = null;*/

		   if(e.getButton() == MouseEvent.BUTTON1 && mouseState != MouseState.DRAGGING){
			   /*List<Ellipse2Dextd> newEndPoints = new ArrayList<Ellipse2Dextd>();

		         double x1 = drawingLine.getX1() - ELLIPSE_DIAMETER / 2;
		         double y1 = drawingLine.getY1() - ELLIPSE_DIAMETER / 2;
		         Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
		         x1 = drawingLine.getX2() - ELLIPSE_DIAMETER / 2;
		         y1 = drawingLine.getY2() - ELLIPSE_DIAMETER / 2;
		         Ellipse2D ellipse2 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
		         newEndPoints.add(ellipse1);
		         newEndPoints.add(ellipse2);
		         ellipseList.add(newEndPoints);
		         repaint();*/

			   p0=p1; 
			   p1 = e.getPoint();

			   //drawingCircle = new Ellipse2D.Double(p1.getX()- ELLIPSE_DIAMETER / 2, p1.getY()- ELLIPSE_DIAMETER / 2,ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);

			   if(mouseState == MouseState.IDLE){
				   newEndPointsXtd = new ArrayList<Ellipse2Dextd>();
				   ellipseXtdList.add(newEndPointsXtd);		  
			   }
			   double x1 = p1.getX() - ELLIPSE_DIAMETER / 2;
			   double y1 = p1.getY() - ELLIPSE_DIAMETER / 2;  
			   Ellipse2D ellipse1 = new Ellipse2D.Double(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
			   Ellipse2Dextd ellipseextd1 = new Ellipse2Dextd(ellipse1,EllispseState.INI);
			   newEndPointsXtd.add(ellipseextd1);
			   mouseState = MouseState.DRAWING;
			   repaint();

		   }else if(e.getButton() == MouseEvent.BUTTON1 && mouseState == MouseState.DRAGGING){
			   if(mouseState == MouseState.DRAGGING){
					if(ellipseDrag!=null){
						double x1 = e.getX() - ELLIPSE_DIAMETER / 2;
						double y1 = e.getY() - ELLIPSE_DIAMETER / 2;
						ellipseDrag.getEllipsec().getEllipse().setFrame(x1, y1, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
						ellipseDrag=null;
					    drawingCirclec=null;
					    drawingLineb=null;
					    drawingLinep=null;
					    mouseState = MouseState.IDLE;
					    repaint();
					}
			   }
		   }
		   else if(e.getButton() == MouseEvent.BUTTON3) {
			   if(mouseState != MouseState.IDLE){
				   mouseState = MouseState.IDLE;
				   drawingCircle=null;
				   drawingLine=null;
				   ellipseDrag=null;
				   drawingCirclec=null;
				   drawingLineb=null;
				   drawingLinep=null;
				   repaint();
			   }
		   }
	   }


   }
}

