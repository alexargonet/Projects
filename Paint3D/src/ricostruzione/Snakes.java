package ricostruzione;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import graphicxtd.Ellipse2Dextd;
import graphicxtd.JPanelControlPoint;
import graphicxtd.JPanelControlPointBase;
import paintimage.PaintImage;
import utility.FourPointInt;
//import paintimage.PaintImage.MenuItemListener;
import utility.ImageU;
import utility.Utility;

public class Snakes extends JPanelControlPointBase{
	JPopupMenu popupMenu = null;
	ImageU imgInfo=null;
	ArrayList<Point2D> snakePoints=null;
	private static final Color LINE_COLOR_2 = Color.green;

	public Snakes(ImageU img, PaintImage originFrame) {
		super(img, originFrame);
		imgInfo = img;
		// TODO Auto-generated constructor stub
		//************************************
        // POPUP MENU
        //************************************
        popupMenu = new JPopupMenu("Snakes PopUpMenu"); 
        //add(popupMenu); 
        popupMenu.addMouseListener(this);
        
        JMenuItem snakeStartMenuItem = new JMenuItem("Start");
        snakeStartMenuItem.setActionCommand("Start");

        //JMenuItem morph2MenuItem = new JMenuItem("Morphing");
        //morph2MenuItem.setActionCommand("Morphing");
        
        //JMenuItem spline3MenuItem = new JMenuItem("MBASpline");
        //spline3MenuItem.setActionCommand("MBASpline");

        /*JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setActionCommand("Paste");*/

        MenuItemListener menuItemListener = new MenuItemListener(this);

        snakeStartMenuItem.addActionListener(menuItemListener);
        //morph2MenuItem.addActionListener(menuItemListener);
        //spline3MenuItem.addActionListener(menuItemListener);
        //pasteMenuItem.addActionListener(menuItemListener);
        //morph2MenuItem.setEnabled(false);

        popupMenu.add(snakeStartMenuItem);
        //popupMenu.add(morph2MenuItem);
        //popupMenu.add(spline3MenuItem);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		if(e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 2) {
			popupMenu.show(e.getComponent(),e.getX(), e.getY());
			//popupMenu.setVisible(true);
		}
		
	}
	
	class MenuItemListener implements ActionListener {
		Snakes snake = null;
		public MenuItemListener(Snakes snake) {
			super();
			this.snake = snake;
		}
		
	      public void actionPerformed(ActionEvent e) {            
	         //statusLabel.setText(e.getActionCommand() + " MenuItem clicked.");
	    	  System.out.println(e.getActionCommand() + " MenuItem clicked.");
	    	  if(e.getActionCommand().equals("Start")) {
	    		  startSnake();
	    	  }
	    	  /*else if(e.getActionCommand().equals("Morphing")) {
	    		  JPanelControlPoint jpcp = (JPanelControlPoint)getVisibileComponent();
	    		  if(morph!=null)
	    			  MorphMBADispl(morph.getImageInput(),jpcp.getDisplacement());
	    	  }
	    	  else if(e.getActionCommand().equals("MBASpline")) {
	    		  JPanelControlPoint jpcp = (JPanelControlPoint)getVisibileComponent();
	    		  MBASpline(jpcp.getControlPoint());
	      	  }*/
	     }  
	      
	    //******************************************************//
	    //* 25 mag 2020-16:50:40-		startSnake             *//	   
	    //******************************************************//
	    public void startSnake() {
	    	BufferedImage imgBuf = imgInfo.getImgBuf();
	    	int imageType=1;
	    	 double[][] gauFilter = new double[5][5];
	    	 
	    	 
	    	 
	    	 gauFilter[0][0]=2;
	    	 gauFilter[0][1]=4;
	    	 gauFilter[0][2]=5;
	    	 gauFilter[0][3]=4;
	    	 gauFilter[0][4]=2;
	    	 
	    	 gauFilter[1][0]=4;
	    	 gauFilter[1][1]=9;
	    	 gauFilter[1][2]=12;
	    	 gauFilter[1][3]=9;
	    	 gauFilter[1][4]=4;
	    	 
	    	 gauFilter[2][0]=5;
	    	 gauFilter[2][1]=12;
	    	 gauFilter[2][2]=15;
	    	 gauFilter[2][3]=12;
	    	 gauFilter[2][4]=5;
	    	 
	    	 gauFilter[3][0]=4;
	    	 gauFilter[3][1]=9;
	    	 gauFilter[3][2]=12;
	    	 gauFilter[3][3]=9;
	    	 gauFilter[3][4]=4;
	    	 
	    	 gauFilter[4][0]=2;
	    	 gauFilter[4][1]=4;
	    	 gauFilter[4][2]=5;
	    	 gauFilter[4][3]=4;
	    	 gauFilter[4][4]=2;
	    	 
	    	 double Nwei=0;
	    	 double wint=1;
	    	 //double sigma=0.84089642;
	    	 double sigma=2;
	    	 double expn=0;
	    	 double signmq = sigma*sigma;
	    	 double signmq4 = signmq*signmq;
	    	 double signm2 = signmq*2;
	    	 double signm2pi = signm2*Math.PI;
	    	 int dimMat=(int)Math.ceil(6*sigma);
	    	 if(dimMat%2==0)
	    		 dimMat++;
	    	 int dimMath=(int)Math.ceil(dimMat/2);
	    	 double[][] GAU = new double[dimMat][dimMat];
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
		    		GAU[ii+(dimMath)][jj+(dimMath)] = /*sigma*Math.sqrt(2*Math.PI)**/(Math.exp(expn))/signm2pi;
		    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
		    		Nwei=Nwei+GAU[ii+(dimMath)][jj+(dimMath)];
		    	}
		     }
	    	 
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		GAU[ii+(dimMath)][jj+(dimMath)] /= Nwei;
		    		
		    	}
		     }
	    	 //**********************************************
	    	 //* GRADIENT OF GAUSSIAN
	    	 //**********************************************
	    	 double[][] GRoG = new double[dimMat][dimMat];
	    	 double Nweig=0;
	    	 double amp=0;
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
		    		amp = -(jj+ii)/(signm2pi*signm2);
		    		GRoG[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
		    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
		    		Nweig=Nweig+GRoG[ii+(dimMath)][jj+(dimMath)];
		    	}
		     }
	    	 
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		GRoG[ii+(dimMath)][jj+(dimMath)] /= Nweig;
		    		
		    	}
		     }
	    	 
	    	 
	    	 double[][] GRoGX = new double[dimMat][dimMat];
	    	 amp=0;
	    	 double Nweix=0,Nweiy=0;
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
		    		amp = -(jj)/(signm2pi*signm2);
		    		GRoGX[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
		    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
		    		Nweix=Nweix+GRoGX[ii+(dimMath)][jj+(dimMath)];
		    	}
		     }
	    	 
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		GRoGX[ii+(dimMath)][jj+(dimMath)] /= Nweix;
		    		
		    	}
		     }
	    	 
	    	 double[][] GRoGY = new double[dimMat][dimMat];
	    	 amp=0;
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
		    		amp = -(ii)/(signm2pi*signm2);
		    		GRoGY[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
		    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
		    		Nweiy=Nweiy+GRoGY[ii+(dimMath)][jj+(dimMath)];
		    	}
		     }
	    	 
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		GRoGY[ii+(dimMath)][jj+(dimMath)] /= Nweiy;
		    		
		    	}
		     }
	    	 
	    	 //*****************************************************
	    	 //* LAPLACIAN of GAUSSIAN
	    	 //*****************************************************
	    	 
	    	 double[][] LoG = new double[dimMat][dimMat];
	    	 amp=0;
	    	 Nwei=0;
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		expn = -(Math.pow(ii,2)+Math.pow(jj,2))/signm2;
		    		amp = (Math.pow(ii,2)+Math.pow(jj,2)-signm2)/(signm2pi*signmq4);
		    		LoG[ii+(dimMath)][jj+(dimMath)] = amp*(Math.exp(expn));
		    		//gauFilterD[ii+(dimMath)][jj+(dimMath)] = 1;
		    		Nwei=Nwei+LoG[ii+(dimMath)][jj+(dimMath)];
		    	}
		     }
	    	 
	    	 for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
		    		LoG[ii+(dimMath)][jj+(dimMath)] /= Nwei;
		    	}
		     }
	    	 
	    	 
	    	 
	    	 double[][] sobelFilter = new double[5][5];
	    	 sobelFilter[0][0]=0;
	    	 sobelFilter[0][1]=0;
	    	 sobelFilter[0][2]=-1;
	    	 sobelFilter[0][3]=0;
	    	 sobelFilter[0][4]=0;
	    	 
	    	 sobelFilter[1][0]=0;
	    	 sobelFilter[1][1]=-1;
	    	 sobelFilter[1][2]=-2;
	    	 sobelFilter[1][3]=-1;
	    	 sobelFilter[1][4]=0;
	    	 
	    	 sobelFilter[2][0]=-1;
	    	 sobelFilter[2][1]=-2;
	    	 sobelFilter[2][2]=16;
	    	 sobelFilter[2][3]=-2;
	    	 sobelFilter[2][4]=-1;
	    	 
	    	 sobelFilter[3][0]=0;
	    	 sobelFilter[3][1]=-1;
	    	 sobelFilter[3][2]=-2;
	    	 sobelFilter[3][3]=-1;
	    	 sobelFilter[3][4]=0;
	    	 
	    	 sobelFilter[4][0]=0;
	    	 sobelFilter[4][1]=0;
	    	 sobelFilter[4][2]=-1;
	    	 sobelFilter[4][3]=0;
	    	 sobelFilter[4][4]=0;
	    	 
	    	 //******************************
	    	 //* LAPLACIANO
	    	 //******************************
	    	 sobelFilter[0][0]=0;
	    	 sobelFilter[0][1]=0;
	    	 sobelFilter[0][2]=0;
	    	 sobelFilter[0][3]=0;
	    	 sobelFilter[0][4]=0;
	    	 
	    	 sobelFilter[1][0]=0;
	    	 sobelFilter[1][1]=1;
	    	 sobelFilter[1][2]=1;
	    	 sobelFilter[1][3]=1;
	    	 sobelFilter[1][4]=0;
	    	 
	    	 sobelFilter[2][0]=0;
	    	 sobelFilter[2][1]=1;
	    	 sobelFilter[2][2]=-8;
	    	 sobelFilter[2][3]=1;
	    	 sobelFilter[2][4]=0;
	    	 
	    	 sobelFilter[3][0]=0;
	    	 sobelFilter[3][1]=1;
	    	 sobelFilter[3][2]=1;
	    	 sobelFilter[3][3]=1;
	    	 sobelFilter[3][4]=0;
	    	 
	    	 sobelFilter[4][0]=0;
	    	 sobelFilter[4][1]=0;
	    	 sobelFilter[4][2]=0;
	    	 sobelFilter[4][3]=0;
	    	 sobelFilter[4][4]=0;
	    	 
	    	 
	    	 //float gaufilterdiv = 159*5;
	    	 float gaufilterdiv = 1;
	    	 
	    	 int Hi = imgBuf.getHeight();
	    	 int Wi = imgBuf.getWidth();
	    	 
	    	 double[][] ImgR = new double[Hi][Wi];
	    	 double[][] ImgG = new double[Hi][Wi];
	    	 double[][] ImgB = new double[Hi][Wi];
	    	 
	    	 double[][] ImgBN = new double[Hi][Wi];
	    	 double[][] gradIX = new double[Hi][Wi];	    	 
	    	 double[][] gradIY = new double[Hi][Wi];
	    	 double[][] grad2IX = new double[Hi][Wi];
	    	 double[][] grad2IY = new double[Hi][Wi];
	    	 double[][] Eedge = new double[Hi][Wi];
	    	 double[][] EedgeG = new double[Hi][Wi];
	    	 double[][] EedgeLoG = new double[Hi][Wi];
	    	 double[][] EedgeSob = new double[Hi][Wi];
	    	 double[][] gradEedgeX = new double[Hi][Wi];
	    	 double[][] gradEedgeY = new double[Hi][Wi];
	    	 
	    	 double[][] GX = new double[3][3];
	    	 double[][] GY = new double[3][3];
	    	 double[][] GIX = new double[Hi][Wi];
    	    double[][] GIY = new double[Hi][Wi];
    	    double[][] F = new double[Hi][Wi];
    	    double[][] G = new double[Hi][Wi];
    	    double[][] GASS = new double[Hi][Wi];
	    	 
	    	GX[0][0] = 1;
    	    GX[0][1] = 0;
    	    GX[0][2] = -1;
    	    GX[1][0] = 1;
    	    GX[1][1] = 0;
    	    GX[1][2] = -1;
    	    GX[2][0] = 1;
    	    GX[2][1] = 0;
    	    GX[2][2] = -1;
    	    
    	    GY[0][0] = 1;
    	    GY[0][1] = 1;
    	    GY[0][2] = 1;
    	    GY[1][0] = 0;
    	    GY[1][1] = 0;
    	    GY[1][2] = 0;
    	    GY[2][0] = -1;
    	    GY[2][1] = -1;
    	    GY[2][2] = -1;
	    	 
	    	 Color img_color;
	    	 
	    	 for(int i=1;(i<(Hi-1));i++){
		    	for(int j=1;j<(Wi-1);j++){
		    		img_color = new Color(imgBuf.getRGB(j,i));
		    		ImgR[i][j] = img_color.getRed();
		    		ImgG[i][j] = img_color.getGreen();
		    		ImgB[i][j] = img_color.getBlue();
			    	 if(imageType!=3 && ImgR[i][j]!=ImgG[i][j] || ImgR[i][j]!=ImgB[i][j]){
							imageType=3;
							break;
					 }
		    	}
		    	if(imageType==3)
					break;
	    	 }
	    	 
	    	 if(imageType==3) {
		    	 
		    	 for(int i=1;(i<(Hi-1));i++){
		    		for(int j=1;j<(Wi-1);j++){
		    			img_color = new Color(imgBuf.getRGB(j,i));
		    			ImgBN[i][j]= (0.2126 * img_color.getRed()+0.7152 * img_color.getGreen()+ 0.0722 * img_color.getBlue());
		    			/*if(ImgBN[i][j]<=0.0031308)
		    				ImgBN[i][j]= 12.92 * ImgBN[i][j];
		    			else
		    				ImgBN[i][j]= 1.055 * Math.pow(ImgBN[i][j],1.0/2.4) - 0.055;*/
		    		}
		    	 }
	    	 }
	    	 else {
	    		for(int i=0;(i<(Hi));i++){
		    		for(int j=0;j<(Wi);j++){
		    			ImgBN[i][j]=ImgR[i][j];
		    		}
			    }    		 
	    	 }
	    	 BufferedImage imagetmp = new BufferedImage(Wi, Hi, BufferedImage.TYPE_INT_RGB);
	    	
	    	 
	    	 
	    	 //*************************************
		    //* DERIVATA PRIMA
		    //************************************* 
	    	for(int i=0;(i<(Hi-1));i++){
    			for(int j=0;j<(Wi-1);j++){   				
    				gradIX[i][j]=ImgBN[i][j+1]-ImgBN[i][j];
    				gradIY[i][j]=ImgBN[i+1][j]-ImgBN[i][j];    				
    			}
    			
	    	}
	    	
	    	//*************************************
	    	//* DERIVATA SECONDA
	    	//*************************************
	    	for(int i=1;(i<(Hi-1));i++){
    			for(int j=1;j<(Wi-1);j++){	    				
    				grad2IX[i][j]=ImgBN[i][j-1]-2*ImgBN[i][j]+ImgBN[i][j+1];
    				grad2IY[i][j]=ImgBN[i-1][j]-2*ImgBN[i][j]+ImgBN[i+1][j];   				
    			}
	    	}
	    	//**************************************
	    	//* ENERGIA EDGE
	    	//**************************************
	    	double GXt=0,GYt=0;
	    	for(int i=2;(i<(Hi-2));i++){
    			for(int j=2;j<(Wi-2);j++){	
    				GXt=GYt=0;
    				for(int ii=-2; ii<3;ii++) {
    			    	for(int jj=-2;jj<3;jj++) {
    			    		GXt = GXt + gauFilter[ii+2][jj+2]*grad2IX[i-ii][j-jj]/gaufilterdiv;
    			    		GYt = GYt + gauFilter[ii+2][jj+2]*grad2IY[i-ii][j-jj]/gaufilterdiv;
    			    	}
    			    }
    				//GXt = GXt;
    				//GYt = GYt;
    				Eedge[i][j]= -1 * Math.pow(GXt, 2) + Math.pow(GYt, 2); 	
    				
					/*if(Eedge[i][j]<-255)
						Eedge[i][j]=-255;
					img_color = new Color((int)Math.round(-1*Eedge[i][j]),(int)Math.round(-1*Eedge[i][j]),(int)Math.round(-1*Eedge[i][j]));
    				
    				imagetmp.setRGB(j, i, img_color.getRGB());*/
    			}
	    	}
	    	//getOriginFrame().addImage(imagetmp, "Energia");
	    	
	    	//**********************
	    	// SOBEL
	    	//*********************
	    	double Edgemin=0;
	    	double EdgeminG=0;
	    	for(int i=2;(i<(Hi-2));i++){
    			for(int j=2;j<(Wi-2);j++){	
    				GXt=GYt=0;
    				for(int ii=-2; ii<3;ii++) {
    			    	for(int jj=-2;jj<3;jj++) {
    			    		//GXt = GXt + gauFilter[ii+2][jj+2]*sobelFilter[ii+2][jj+2]*ImgBN[i+ii][j+jj];
    			    		GYt = GYt + sobelFilter[ii+2][jj+2]*ImgBN[i-ii][j-jj];
    			    	}
    			    }
    				//Eedge[i][j]= -GXt*GXt;
    				//Eedge[i][j]= -GYt*GYt; 
    				Eedge[i][j]= GYt;
    				//Eedge[i][j]= GXt;
    				if(Eedge[i][j]<Edgemin)
    					Edgemin=Eedge[i][j];
    			}
	    	}
	    	//*******************************************************
	    	//** da CANNY EDGE
	    	//********************************************************
	    	for(int i=1;(i<(Hi-1));i++){
				for(int j=1;j<(Wi-1);j++){
					GXt=GYt=0;
					for(int ii=-1; ii<2;ii++) {
				    	for(int jj=-1;jj<2;jj++) {
				    		GXt = GXt + GX[jj+1][ii+1]*ImgBN[i-ii][j-jj];
				    		GYt = GYt + GY[jj+1][ii+1]*ImgBN[i-ii][j-jj];
				    		/*GXt = GXt + GX[ii+1][jj+1]*F[i+ii][j+jj];
				    		GYt = GYt + GY[ii+1][jj+1]*F[i+ii][j+jj];*/
				    	}
				    }
					GIX[i][j]=GXt;
					GIY[i][j]=GYt;
				}
			}
 	
		 	//*****************************************************
		    // metto il gradinte al quadrato per essere filtrato
		    //*****************************************************
		    for(int i=1;(i<(Hi-1));i++){
				for(int j=1;j<(Wi-1);j++){
					G[i][j] = Math.sqrt(Math.pow(GIX[i][j],2) + Math.pow(GIY[i][j],2));
					//G[i][j] = Math.sqrt(Math.pow(gradIX[i][j],2) + Math.pow(gradIY[i][j],2));
					
					//F[i][j] = (int)Math.round((Math.pow(GIX[i][j],2) + Math.pow(GIY[i][j],2)));
					/*int col = (int) Math.round(G[i][j]);
				    if(col>255)
				    	col=255;
				    if(col<0)
				    	col=0;
				    img_color = new Color(col,col,col);
					imagetmp12.setRGB(j, i, img_color.getRGB());*/
				}
			}
		    //*************************************
	    	//* GAUSSIAN FILTER APPLIED
	    	//*************************************
		    BufferedImage imagetmpGF = new BufferedImage(Wi, Hi, BufferedImage.TYPE_INT_RGB);
	    	Edgemin=0;
	    	int col=0;
	    	int ind=0,jnd=0;
	    	for(int i=0;i<(Hi);i++){
    			for(int j=0;j<(Wi);j++){	
    				GXt=GYt=0;
    				//for(int ii=-2; ii<3;ii++) {
    			    	//for(int jj=-2;jj<3;jj++) {
		    		//for(int ii=-(int)sigma; ii<(int)sigma+1;ii++) {
				    	//for(int jj=-(int)sigma;jj<(int)sigma+1;jj++) {
    			    		//GXt = GXt + gauFilter[ii+2][jj+2]*Eedge[i+ii][j+jj];
    			    		//GYt = GYt + sobelFilter[ii+2][jj+2]*ImgBN[i+ii][j+jj];
		    			for(int ii=-(dimMath); ii<=dimMath;ii++) {
		    		    	for(int jj=-(dimMath);jj<=dimMath;jj++) {
					    		if(i-ii>=0 && i-ii<Hi && j-jj>=0 && j-jj<Wi) {	    			
					    			//GXt = GXt + gauFilterD[ii+dimMath][jj+dimMath]*G[i+ii][j+jj];
					    			//GXt = GXt + gauFilterD[ii+(dimMath)][jj+(dimMath)]*ImgBN[i-ii][j-jj];
					    			//GXt = GXt + gauFilterD[ii+(dimMath)][jj+(dimMath)]*Eedge[i-ii][j-jj];
					    			GXt = GXt + LoG[ii+(dimMath)][jj+(dimMath)]*ImgBN[i-ii][j-jj];
					    		}
					    		else{
					    			if(i-ii<0)
					    				ind=0;
					    			if(i-ii>=Hi)
					    				ind=Hi-1;
					    			if(j-jj<0)
					    				jnd=0;
					    			if(j-jj>=Wi)
					    				jnd=Wi-1;
					    			//GXt = GXt + gauFilterD[ii+dimMath][jj+dimMath]*G[ind][jnd];
					    			//GXt = GXt + gauFilterD[ii+(dimMath)][jj+(dimMath)]*ImgBN[ind][jnd];
					    			//GXt = GXt + gauFilterD[ii+(dimMath)][jj+(dimMath)]*Eedge[ind][jnd];
					    			GXt = GXt + LoG[ii+(dimMath)][jj+(dimMath)]*ImgBN[ind][jnd];
					    		}
				    		}
    			    		//GXt = GXt + G[i+ii][j+jj]/25;
    			    	}
		    		EedgeG[i][j]=-GXt*GXt*wint;
		    		//EedgeG[i][j]= -GXt*GXt*wint;
    				//Eedge[i][j]= -GYt*GYt; 
    				EedgeLoG[i][j]= GXt;
    				if(Math.abs(GXt)>Edgemin)
    					Edgemin=Math.abs(GXt);
    				
    				//col = (int)Math.round(GXt);
    				//img_color = new Color(col,col,col);
    				//imagetmpGF.setRGB(j, i, img_color.getRGB());
    				
    			}
	    	}
	    	
	    	
	    	
	    	//Edgemin=0;
	    	double xg=0,yg=0;
	    	for(int i=1;i<(Hi-2);i++){
    			for(int j=1;j<(Wi-2);j++){	
    				GXt=GYt=0;
    				GXt=G[i][j]-G[i][j-1];
    				GYt=G[i][j]-G[i-1][j];
    				xg=Math.pow(GXt, 2);
    				yg=Math.pow(GYt, 2);
    				//EedgeG[i][j]=-(xg+yg);
		    		//EedgeG[i][j]= -GXt*GXt*wint;
    				//Eedge[i][j]= -GYt*GYt;
    				//EedgeG[i][j]= -Math.abs(G[i][j]); 
    				//EedgeG[i][j]= -(Math.abs(GXt));
    				if(EedgeG[i][j]<EdgeminG)
    					EdgeminG=EedgeG[i][j];
    			    
    				if((EedgeLoG[i][j])<0)
    					col = (int)Math.round(127.5-127.5*Math.abs(EedgeLoG[i][j]/Edgemin));
    				else
    					col = (int)Math.round(127.5+127.5*Math.abs(EedgeLoG[i][j]/Edgemin));
    				//if(col<0)
    				img_color = new Color(col,col,col);
    				imagetmpGF.setRGB(j, i, img_color.getRGB());
    			}
	    	}
	    	
	    	getOriginFrame().addImage(imagetmpGF, "LoG Filter");
	    	//getOriginFrame().addImage(imagetmpGF, "Gaussian Filter");
	    	
	    	img_color = new Color(0,0,0);
	    	

	    	//* Gradiente Gaussiano dell'immagine
	    	double[][] DGImgX = Utility.filter(GAU,gradIX, Hi, Wi, dimMat);
	    	
	    	double[][] DGImgY = Utility.filter(GAU,gradIY, Hi, Wi, dimMat);
	    	
	    	
	    	//double[][] DGImg = Utility.filter(GRoG,ImgBN, Hi, Wi, dimMat);
	    	
	    	
	    	for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					G[i][j] = 1/(1+Math.sqrt(Math.pow(DGImgX[i][j],2))+Math.pow(DGImgY[i][j],2));
				}
			}
	    	
	    	BufferedImage imagetmpG = new BufferedImage(Wi, Hi, BufferedImage.TYPE_INT_RGB);
	    	for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					col = (int)Math.round(255*(G[i][j]));
    				
    				//if(col<0)
    				img_color = new Color(col,col,col);
    				imagetmpG.setRGB(j, i, img_color.getRGB());
				}
	    	}
	    	getOriginFrame().addImage(imagetmpG, "Energy");
	    	
	    	
	    	for(int i=1;(i<(Hi-2));i++){
    			for(int j=1;j<(Wi-2);j++){	
    				//gradEedgeX[i][j]=(EedgeG[i][j+1]-EedgeG[i][j-1])/2;
    				//gradEedgeY[i][j]=(EedgeG[i+1][j]-EedgeG[i-1][j])/2;  
    				
    				gradEedgeX[i][j]=(DGImgX[i][j]-DGImgX[i][j-1]);
    				gradEedgeY[i][j]=(DGImgY[i][j]-DGImgY[i-1][j]);  
    				
    				/*if(gradEedgeX[i][j]>255) {
    					//Eedge[i][j]=255;
    					img_color = new Color(0,0,255);
    					
    				}
    				else if(gradEedgeX[i][j]>0) {
    					img_color = new Color(0,0,(int)Math.round(gradEedgeX[i][j]));
    					//img_color = new Color(0,0,255);
    					if((int)Math.round(gradEedgeX[i][j])>1) {
    						i=i;
    					}
    				}*/
    				/*if(Eedge[i][j]<-255) {
    					img_color = new Color(255,0,0);
    					
    				}else*/ if(EedgeG[i][j]<0) {
    					img_color = new Color((int)Math.round(255*EedgeG[i][j]/EdgeminG),0,0);
    					//img_color = new Color(255,0,0);
    				}
    				
    				
    				imagetmp.setRGB(j, i, img_color.getRGB());
    				img_color = new Color(0,0,0);
    			}
	    	}
	    	getOriginFrame().addImage(imagetmp, "Energia LoG");

	    	
	    	
	    	
	    	if(snakePoints==null) {
		    	snakePoints = new ArrayList<Point2D>();
		    	int step=0,numInter=10;
		    	FourPointInt ctrlPoint0=null;
		    	double hx=0,hy=0;
		    	ArrayList<FourPointInt> listCtrlPoin = getControlPoint();
		    	for(FourPointInt ctrlPoint : listCtrlPoin) {
		    		
		    		if(step>0) {
		    			hx=(double)1.0*(ctrlPoint.getX()-ctrlPoint0.getX())/numInter;
		    			hy=(double)1.0*(ctrlPoint.getY()-ctrlPoint0.getY())/numInter;
		    			for(int k=1;k<=numInter;k++) {
		    				Point2D pointSnake = new Point2D.Double(ctrlPoint0.getX()+k*hx,ctrlPoint0.getY()+k*hy);
		    				snakePoints.add(pointSnake);
		    			}
		    		}
		    		else {
		    			Point2D pointSnake = new Point2D.Double(ctrlPoint.getX(),ctrlPoint.getY());
		    			snakePoints.add(pointSnake);
		    		}
		    		step++;
		    		ctrlPoint0=ctrlPoint;
		    	}
	    	}
	    	ArrayList<Point2D> snakePointsTmp = new ArrayList<Point2D>();
	    	int x,y,stepSnakes=10,intStep=0,snakeSize=snakePoints.size();
	    	double gamm=1,alp=1,bet=1,xs1,ys1;
	    	double E,Emin,percMov=0;
	    	int xxmin=0,yymin=0,numMod=0;
	    	Point2D ps1,ps_1,ps2,ps_2;
	    	double ForceDx=0,ForceDy=0;
	    	int snakeDim = snakePoints.size();
	    	double minDeltaX=0,minDeltaY=0;
	    	double[][] A = new double[snakeDim][snakeDim];
	    	double[][] Ainv = new double[snakeDim][snakeDim];
	    	double[] Bx = new double[snakeDim];
	    	double[] By = new double[snakeDim];
	    	double[] Xt = new double[snakeDim];
	    	double[] Yt = new double[snakeDim];
	    	double[] Arow = new double[snakeDim];
	    	double[] Atmp =null;
	    	for(int is=0;is<snakeDim;is++) {
	    		Arow[is]=0;
	    		//Atmp[is]=0;
	    	}
	    	Arow[0]=bet;
	    	Arow[1]=-alp-4*bet;
	    	Arow[2]=2*alp+6*bet+gamm;
	    	Arow[3]=-alp-4*bet;
	    	Arow[4]=bet;
	    	for(int is=0;is<snakeDim;is++) {
    			Atmp=Utility.shift_array(Arow, is-2);
    			for(int js=0;js<snakeDim;js++) {
					/*if(is-js-2==0)
						A[is][js]=bet;
					else if(is-js-1==0)
						A[is][js]=-alp-4*bet;
    				else if(js-is==0)
						A[is][js]=2*alp+6*bet+gamm;
    				else if(is-js+1==0)
    					A[is][js]=-alp-4*bet;
    				else if(is-js+2==0)
    					A[is][js]=bet;
    				else
    					A[is][js]=0;*/
    				A[is][js]=Atmp[js];
    				
    			}
    		}
    		// con questo blocco impongo che i punti estremi sono uguali altrimenti, ma senza questo blocco lo snake si morde la coda
    		if(1==1) {
	    		A[0][snakeDim-2]=A[0][snakeDim-1]=0;
	    		A[1][snakeDim-1]=0;
	    		A[snakeDim-2][0]=0;
	    		A[snakeDim-1][0]=A[snakeDim-1][1]=0;
	    		A[0][0]=3*bet+alp+gamm;
	    		A[1][0]=-3*bet-alp;
	    		A[snakeDim-2][snakeDim-1]=-3*bet-alp;
	    		A[snakeDim-1][snakeDim-1]=3*bet+alp+gamm;
    		}
    		Ainv = Utility.matrixInverse(A);
	    	//for(int is=0;is<snakeDim;is++) {
	    	for(int isn=0;isn<stepSnakes;isn++) {
	    		intStep=0;
	    		numMod=0;
	    		for(int is=0;is<snakeDim;is++) {
	    			Point2D ps = snakePoints.get(is);
	    			x=(int)Math.round(ps.getX());
		    		y=(int)Math.round(ps.getY());
	    			Bx[is]=ps.getX()*gamm + gradEedgeX[y][x];
	    			By[is]=ps.getY()*gamm + gradEedgeY[y][x];
		    		/*ForceDx=0;
		    		ForceDy=0;
		    		if(is==15) {
		    			ForceDx=-2*(ps.getX()-Wi/2);
		    			ForceDy=-2*(ps.getY()-Hi/2);
		    		}
		    		
		    		Bx[is]=ps.getX() + ForceDx;
	    			By[is]=ps.getY() + ForceDy;*/
	    			//Atmp=Utility.shift_array(Arow, is-2);
	    			//for(int js=0;js<snakeDim;js++) {
    					/*if(is-js-2==0)
    						A[is][js]=bet;
    					else if(is-js-1==0)
    						A[is][js]=-alp-4*bet;
	    				else if(js-is==0)
    						A[is][js]=2*alp+6*bet+gamm;
	    				else if(is-js+1==0)
	    					A[is][js]=-alp-4*bet;
	    				else if(is-js+2==0)
	    					A[is][js]=bet;
	    				else
	    					A[is][js]=0;*/
	    				//A[is][js]=Atmp[js];
	    				
	    			//}
	    		}
	    		// con questo blocco impongo che i punti estremi sono uguali altrimenti, ma senza questo blocco lo snake si morde la coda
	    		/*if(1==1) {
		    		A[0][snakeDim-2]=A[0][snakeDim-1]=0;
		    		A[1][snakeDim-1]=0;
		    		A[snakeDim-2][0]=0;
		    		A[snakeDim-1][0]=A[snakeDim-1][1]=0;
		    		A[0][0]=3*bet+alp+gamm;
		    		A[1][0]=-3*bet-alp;
		    		A[snakeDim-2][snakeDim-1]=-3*bet-alp;
		    		A[snakeDim-1][snakeDim-1]=3*bet+alp+gamm;
	    		}*/
	    		//Ainv = Utility.matrixInverse(A);
	    		Xt = Utility.matrixMultVect(Ainv, Bx);
	    		Yt = Utility.matrixMultVect(Ainv, By);
	    		
	    		for(Point2D ps : snakePoints) {
	    			xs1=Xt[intStep];
	    			ys1=Yt[intStep];
		    		
		    		ps.setLocation(xs1,ys1);
		    				    		
		    		//numMod++;
		    				    		
		    		//percMov = ((double)numMod)/snakePoints.size();
		    				    		
		    		if(Math.abs(xs1-Bx[intStep])<minDeltaX) {
		    			minDeltaX=Math.abs(Xt[intStep]-Bx[intStep]);
		    		}
		    		if(Math.abs(ys1-By[intStep])<minDeltaY) {
		    			minDeltaY=Math.abs(Yt[intStep]-By[intStep]);
		    		}
		    		
		    		intStep++;
	    		}
	    		
		    	/*for(Point2D ps : snakePoints) {
		    		ps_2=ps_1=ps1=ps2=ps;
		    		if(intStep==0) {
		    			//ps_2=snakePoints.get(snakeSize-2);
		    			//ps_1=snakePoints.get(snakeSize-1);
		    			ps1=snakePoints.get(1);
		    			ps2=snakePoints.get(2);
		    			
		    		}
		    		else if(intStep==1) {
		    			//ps_2=snakePoints.get(snakeSize-1);
		    			ps_1=snakePoints.get(0);
		    			ps1=snakePoints.get(intStep+1);
		    			ps2=snakePoints.get(intStep+2);
		    			
		    		}
		    		else if(intStep>=2 && intStep<snakeSize-2) {
		    			ps_2=snakePoints.get(intStep-2);
		    			ps_1=snakePoints.get(intStep-1);
		    			ps1=snakePoints.get(intStep+1);
		    			ps2=snakePoints.get(intStep+2);
		    			
		    		}
		    		else if(intStep==snakeSize-2) {
		    			ps_2=snakePoints.get(intStep-2);
		    			ps_1=snakePoints.get(intStep-1);
		    			ps1=snakePoints.get(snakeSize-1);
		    			//ps2=snakePoints.get(0);
		    			
		    		}
		    		else if(intStep==snakeSize-1) {
		    			ps_2=snakePoints.get(intStep-2);
		    			ps_1=snakePoints.get(intStep-1);
		    			//ps1=snakePoints.get(0);
		    			//ps2=snakePoints.get(1);
		    			
		    		}
		    		
		    		double h1 = (Math.pow(ps.getX()-ps1.getX(), 2) + Math.pow(ps.getY()-ps1.getY(), 2));
		    		double h0 = (Math.pow(ps.getX()-ps_1.getX(), 2) + Math.pow(ps.getY()-ps_1.getY(), 2));
		    		double h= Math.max(h0, h1);
		    		int hmin= (int)Math.floor(Math.min(h0, h1)/2);
		    		if(hmin>3) {
		    			hmin=3;
		    		}
		    		if(hmin<1) {
		    			hmin=1;
		    		}
		    		//h=1;
		    		x=(int)Math.round(ps.getX());
		    		y=(int)Math.round(ps.getY());
		    		//xs1 = ps.getX() - gamm * (wint * (alp*(ps_1.getX()-2*ps.getX()+ps1.getX())/h+bet*(ps_2.getX()-4*ps_1.getX()+6*ps.getX()-4*ps1.getX()+ps2.getX())/(h*h)) + gradEedgeX[y][x]);
		    		//ys1 = ps.getY() - gamm * (wint * (alp*(ps_1.getY()-2*ps.getY()+ps1.getY())/h+bet*(ps_2.getY()-4*ps_1.getY()+6*ps.getY()-4*ps1.getY()+ps2.getY())/(h*h)) + gradEedgeY[y][x]);
		    		
		    		//xs1 = (ps.getX() - gradEedgeX[y][x])/(wint * (alp*(ps_1.getX()-2*ps.getX()+ps1.getX())/h+bet*(ps_2.getX()-4*ps_1.getX()+6*ps.getX()-4*ps1.getX()+ps2.getX())/(h*h))-gamm);
		    		//ys1 = (ps.getY() - gradEedgeY[y][x])/(wint * (alp*(ps_1.getY()-2*ps.getY()+ps1.getY())/h+bet*(ps_2.getY()-4*ps_1.getY()+6*ps.getY()-4*ps1.getY()+ps2.getY())/(h*h))-gamm);
		    		
		    		Emin=0;
		    		xxmin=0;
		    		yymin=0;
		    		for(int xx=-hmin;xx<(hmin+1);xx++) {
		    			for(int yy=-3;yy<4;yy++) {
		    				E = alp*(Math.pow(ps.getX()+xx-ps_1.getX(),2)+Math.pow(ps.getY()+yy-ps_1.getY(), 2))/(2*h) + bet*(Math.pow(ps_1.getX()-2*(ps.getX()+xx)+ps1.getX(),2)+Math.pow(ps_1.getY()-2*(ps.getY()+yy)+ps1.getY(), 2))/(2*h*h) + EedgeG[y+yy][x+xx];
		    				if(E < Emin) {
		    					Emin = E;
		    					xxmin=xx;
		    					yymin=yy;
		    				}
		    			}
		    		}
		    		xs1 = ps.getX() + xxmin;
		    		ys1 = ps.getY() + yymin;
		    		//snakePointsTmp.add(intStep,new Point2D.Double(xs1,ys1));
		    		if(xxmin!=0 && yymin!=0) {
		    			ps.setLocation(xs1,ys1);
		    			numMod++;
		    		}
		    		percMov = ((double)numMod)/snakePoints.size();
		    		intStep++;
		    	}*/
		    	//int ind=0;
		    	/*for(Point2D ps : snakePoints) {
		    		ps.setLocation(snakePointsTmp.get(ind));
		    		ind++;
		    	}
		    	snakePointsTmp.clear();
		    	*/
		    	repaint();
	    	}		    	
	     }
	}  
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Stroke initStroke = g2.getStroke();
		if(snakePoints!=null) {
			g2.setColor(LINE_COLOR_2);
			Point2D ps0 = null;
			for(Point2D ps : snakePoints) {
				if (ps0 != null) {
					Line2D line = new Line2D.Double(ps0, ps);
					g2.draw(line);
				}
				ps0 = ps;
			}
		}
			
		g2.setStroke(initStroke);
    }

}
