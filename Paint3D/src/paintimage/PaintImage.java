package paintimage;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import graphicxtd.JPanelControlPoint;
import object2D.Piano;
import object2D.Rettangolo;
import object3D.Parallelepipedo;
//import object3D.Piano;
//import object3D.Scena3D;
import object3D.Sfera;
import object3D.Toroide;
import objectBase.ColorResult;
import objectBase.IdObj;
import objectBase.ObjectBase;
import ricostruzione.MBA;
import ricostruzione.MBA_new;
import utility.BinaryTree;
import utility.ColorArray;
import utility.FourPointInt;
import utility.TreeNode;
import utility.Utility;
import utility.Vettore;
 
public class PaintImage extends JFrame implements MouseMotionListener,MouseListener{
	private JFileChooser fc; 
	static private String newline = "\n";
	private ButtonGroup bg=null;
	private boolean bdebug=false;
	private int jpos=-1;
	private int ipos=-1;
	BinaryTree binaryTreeDebug=null;
	BinaryTree binaryTreeColor=null;
	JScrollPane scrollpane=null;
	JLayeredPane layeredPane=null;
	JPopupMenu popupMenu=null;
	
	MBADialog frameMBA=null;
	
	Scena3D scena=null;
	BufferedImage imageRif=null;
	
	
	
	private RadioBtnListnet radioBtnListner = new RadioBtnListnet(this);
	private CheckBoxBtnListnet checkBoxBtnListner = new CheckBoxBtnListnet(this);
    public PaintImage() {
         
        setTitle("Menu Example");
        setSize(800, 650);
        layeredPane = new JLayeredPane();
        // se non metto il setLayout il JPanel esteso JPanelControlPoint non viene visualizzato
        layeredPane.setLayout(new BorderLayout());
        scrollpane = new JScrollPane(layeredPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollpane.setViewportView(layeredPane);
        scrollpane.getViewport().setPreferredSize(new Dimension(800, 650));
        //getContentPane().add(scrollpane);
        add(scrollpane);
        //getContentPane().add(scrollpane);
        //addMouseListener(this);
        // Creates a menubar for a JFrame
        JMenuBar menuBar = new JMenuBar();
         
        // Add the menubar to the frame
        setJMenuBar(menuBar);
         
        // Define and add two drop down menu to the menubar
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu sceneMenu = new JMenu("Scena");
        JMenu filtriMenu = new JMenu("Filtri");
        JMenu stitisticheMenu = new JMenu("Utility");
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(sceneMenu);
        menuBar.add(filtriMenu);
        menuBar.add(stitisticheMenu);
         
        // Create and add simple menu item to one of the drop down menu
        JMenuItem newAction = new JMenuItem("ReOpen First");
        JMenuItem openAction = new JMenuItem("Open");
        JMenuItem saveAction = new JMenuItem("Save As");
        JMenuItem exitAction = new JMenuItem("Exit");
        JMenuItem cutAction = new JMenuItem("Cut");
        JMenuItem copyAction = new JMenuItem("Copy");
        JMenuItem renderingAction = new JMenuItem("Rendering");
        // scena
        JMenuItem radiositaAction = new JMenuItem("Radiosità");
        JMenuItem rad_renderingAction = new JMenuItem("Radiosità + Rendering");
        // ricostruzione
        JMenuItem MBAAction = new JMenuItem("MBA");
        // morphing
        JMenuItem MorphMBAAction = new JMenuItem("Morphing MBA");
        // utility
        JMenuItem imgDiffAction = new JMenuItem("Image Diff");
        JMenuItem sincAction = new JMenuItem("Sinc function generator");
        // Create and add CheckButton as a menu item to one of the drop down
        // menu
        JCheckBoxMenuItem checkAction = new JCheckBoxMenuItem("Debug OFF");
        // Create and add Radio Buttons as simple menu items to one of the drop
        // down menu
//        JRadioButtonMenuItem radioAction1 = new JRadioButtonMenuItem(
//                "Radio Button1");
//        JRadioButtonMenuItem radioAction2 = new JRadioButtonMenuItem(
//                "Radio Button2");
        // Create a ButtonGroup and add both radio Button to it. Only one radio
        // button in a ButtonGroup can be selected at a time.
        //ButtonGroup bg = new ButtonGroup();
        bg = new ButtonGroup();
        //bg.add(radioAction1);
        //bg.add(radioAction2);
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        fileMenu.add(newAction);
        fileMenu.addSeparator();
        fileMenu.add(saveAction);
        fileMenu.addSeparator();
        fileMenu.add(checkAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(renderingAction);
        editMenu.addSeparator();
        // scena
        sceneMenu.add(radiositaAction);
        sceneMenu.add(rad_renderingAction);
        // scena
        filtriMenu.add(MBAAction);
        filtriMenu.add(MorphMBAAction);
        // statistiche
        stitisticheMenu.add(imgDiffAction);
        stitisticheMenu.add(sincAction);
//        editMenu.add(radioAction1);
//        editMenu.add(radioAction2);
        //************************************
        // POPUP MENU
        //************************************
        popupMenu = new JPopupMenu("PopUpMenu"); 
        //add(popupMenu); 
        popupMenu.addMouseListener(this);
        
        JMenuItem morph1MenuItem = new JMenuItem("Punti di Controllo");
        morph1MenuItem.setActionCommand("Punti");

        JMenuItem morph2MenuItem = new JMenuItem("Morphing");
        morph2MenuItem.setActionCommand("Morphing");

        /*JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setActionCommand("Paste");*/

        MenuItemListener menuItemListener = new MenuItemListener();

        morph1MenuItem.addActionListener(menuItemListener);
        morph2MenuItem.addActionListener(menuItemListener);
        //pasteMenuItem.addActionListener(menuItemListener);
        morph2MenuItem.setEnabled(false);

        popupMenu.add(morph1MenuItem);
        popupMenu.add(morph2MenuItem);
        //popupMenu.add(pasteMenuItem);   
        //********************************************************
        //********************************************************
        // Add a listener to the New menu item. actionPerformed() method will
        // invoked, if user triggred this menu item
        newAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action");
                setVisibleImage(1);
            }
        });
        saveAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the save action");
                //setVisibleImage(1);
                if (fc == null) 
                    fc = new JFileChooser();
                    
            	int returnVal = fc.showSaveDialog(PaintImage.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    saveImage(file);
                    //This is where a real application would open the file.
                    System.out.println("Saving: " + file.getName() + "." + newline);
                } else {
                	System.out.println("Save As command cancelled by user." + newline);
                }
                System.out.println("You have clicked on the Save As action");
                fc.setSelectedFile(null);
                
            }
        });
        
        openAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	if (fc == null) 
                    fc = new JFileChooser();
                    
            	int returnVal = fc.showOpenDialog(PaintImage.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                	openImage(file);
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName() + "." + newline);
                } else {
                	System.out.println("Open command cancelled by user." + newline);
                }
                System.out.println("You have clicked on the open action");
                fc.setSelectedFile(null);
            }
        });
        renderingAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                java.awt.Component compImg=getVisibileComponent();
            	ObjContainer objContainer = new ObjContainer(compImg);
                renderImage(objContainer);
            }
        });
        radiositaAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action radiosità");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	java.awt.Component compImg=getVisibileComponent();
                	ObjContainer objContainer = new ObjContainer(compImg);
                	radiosita_new(objContainer);
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
        rad_renderingAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action radiosità + rendering");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	java.awt.Component compImg=getVisibileComponent();
                	ObjContainer objContainer = new ObjContainer(compImg);
                	radiosita(objContainer);
                	renderImage(objContainer);
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
        checkAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
	                System.out.println("You have clicked on the check action");
	                AbstractButton aButton = (AbstractButton) event.getSource();
	                boolean selected = aButton.getModel().isSelected();
	                String newLabel;
	                //Icon newIcon;
	                if (selected) {
	                  newLabel = "Debug ON";
	                  bdebug=true;
	                } else {
	                  newLabel = "Debug OFF";
	                  bdebug=false;
	                }
	                aButton.setText(newLabel);
                }
            });
        MBAAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action MBA");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	MBATest();
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
        MorphMBAAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action MBA");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	/*frameMBA = new MBADialog();
                	//frameMBA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                	frameMBA.setLocationRelativeTo(null);
                	frameMBA.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                	frameMBA.setVisible(true);
                	//frameMBA.set
                	 * */
                	 
                	//MorphMBATest();
                	loadCtrlPanel();
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
        sincAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the new action MBA");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	generaSinct();
                	//MBATest_2();
                	generaReticolo();
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
        imgDiffAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("You have clicked on the Image Diff");
                binaryTreeDebug = new BinaryTree();
                binaryTreeColor = new BinaryTree();
                try{
                	imageDiff();
    			}
                catch(Exception e){
                	e.printStackTrace();            			
    			}
            }
        });
    }
//*********************************************************************
//   OPEN IMAGE
//*********************************************************************//
    private void openImage(File fileimg){
    	BufferedImage image = null;
    	JPanel canvas;
        try
        {
          image = ImageIO.read(fileimg);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.exit(1);
        }
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.addMouseListener(this);
        jLabel.addMouseMotionListener(this);
        jLabel.setIcon(imageIcon);
        jLabel.setName(fileimg.getName());
        // ripulisce dalle immagini precedenti
        /*for (java.awt.Component comp : this.getContentPane().getComponents()){
        	comp.setVisible(false);
        	
        }*/
        
        JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(jLabel);
		topPanel.setBounds(0, 0, image.getWidth(), image.getHeight());
		topPanel.setLocation(0, 0);
		topPanel.setName(jLabel.getName());
//		getContentPane().add( topPanel );
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.getViewport().add( jLabel );
//		topPanel.setName(jLabel.getName());
//		topPanel.add( scrollPane, BorderLayout.CENTER );
		//this.getContentPane().add(jLabel, BorderLayout.CENTER);
        //this.getContentPane().validate();
        
        /*for (java.awt.Component comp : this.getContentPane().getComponents()){
      		//if(comp instanceof JLabel)
      			//comp.setVisible(false);
    	   if(comp instanceof JScrollPane)
    		   ((JScrollPane)comp).add(jLabel);
      }*/
        //layeredPane.add(topPanel);
		
		for(java.awt.Component layer : layeredPane.getComponents()){
			layer.setVisible(false);
		}
        //layeredPane.add(topPanel,JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(topPanel,layeredPane.getComponents().length);
        layeredPane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        

        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
        updateMenuOpenImg();
    }
//*********************************************************************
//  ADD IMAGE
//*********************************************************************//    
   private void addImage(BufferedImage image,String nomeImg){
   	
       ImageIcon imageIcon = new ImageIcon(image);
       JLabel jLabel = new JLabel();
       jLabel.addMouseListener(this);
       jLabel.addMouseMotionListener(this);
       jLabel.setIcon(imageIcon);
       jLabel.setName(nomeImg);
       // ripulisce dalle immagini precedenti
       
       
       /*JPanel topPanel = new JPanel();
       topPanel.add( jLabel );
       topPanel.setName(jLabel.getName());
		//topPanel.setLayout( new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane(topPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setBounds(150, 130, 300, 300);
		//scrollPane.getViewport().add( );
		
		//topPanel.add( scrollPane);
		//scrollPane.add(topPanel);
		
		this.getContentPane().setPreferredSize(new Dimension(500, 400));
		this.getContentPane().add( scrollPane );
		*/
       
       //this.getContentPane().add(jLabel, BorderLayout.CENTER);
       //this.getContentPane().validate();
       /*for (java.awt.Component comp : this.getContentPane().getComponents()){
      		//if(comp instanceof JLabel)
      			//comp.setVisible(false);
    	   if(comp instanceof JScrollPane)
    		   ((JScrollPane)comp).add(jLabel);
      }*/
       JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(jLabel);
		topPanel.setBounds(0, 0, image.getWidth(), image.getHeight());
		topPanel.setLocation(0, 0);
		topPanel.setName(jLabel.getName());
//		getContentPane().add( topPanel );
//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.getViewport().add( jLabel );
//		topPanel.setName(jLabel.getName());
//		topPanel.add( scrollPane, BorderLayout.CENTER );
		//this.getContentPane().add(jLabel, BorderLayout.CENTER);
       //this.getContentPane().validate();
       
       /*for (java.awt.Component comp : this.getContentPane().getComponents()){
     		//if(comp instanceof JLabel)
     			//comp.setVisible(false);
   	   if(comp instanceof JScrollPane)
   		   ((JScrollPane)comp).add(jLabel);
     }*/
		for(java.awt.Component layer : layeredPane.getComponents()){
			layer.setVisible(false);
		}

        
       layeredPane.add(topPanel,JLayeredPane.DEFAULT_LAYER);
       layeredPane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

       this.pack();
       this.setLocationRelativeTo(null);
       this.setVisible(true);
       this.repaint();
       updateMenuOpenImg();
   }
//*********************************************************************
//	SAVE IMAGE
//*********************************************************************//    
    private void saveImage(File fileOut){
    	java.awt.Component compImg=getVisibileComponent();
    	
    	BufferedImage imagetmp = new BufferedImage(compImg.getWidth(), compImg.getHeight(), BufferedImage.TYPE_INT_RGB);
    	Graphics g = imagetmp.getGraphics();
        compImg.paintAll(g);
        try {
			ImageIO.write(imagetmp, "png", fileOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
		}
    }
//*********************************************************************
//     SET VISIBLE IMAGE
//*********************************************************************//    
     public void setVisibleImage(int i){
    	
    	int count=1;
    	for (java.awt.Component layer : this.layeredPane.getComponents()){
        	//this.getContentPane().remove(comp);
    		if(count==i){
    			layer.setVisible(true);
    			//comp.doLayout();
    			//comp.validate();
    			//comp.doLayout();
    			//comp.validate();
    			this.layeredPane.setPreferredSize(new Dimension(layer.getWidth(), layer.getHeight()));
    		}
    		else{
    			layer.setVisible(false);
    			layer.invalidate();
    			//comp.invalidate();
    		}
        	count++;
        	//comp.setVisible(true);        	
        }   
    	
    	//this.getContentPane().validate();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();
        //this.pack();
    }
//*********************************************************************
//  UPDATE MENU OPENED IMAGE
//*********************************************************************
    private void updateMenuOpenImg(){
    	int itemMenu = this.getJMenuBar().getMenu(1).getItemCount();
    	int i = 0;
    	/*for(i=4; i<itemMenu; i++){
    		this.getJMenuBar().getMenu(1).getItem(i);
    	}*/
    	int CountComp=0;
    	for (java.awt.Component layer : this.layeredPane.getComponents()){
        	//this.getContentPane().remove(comp);
    		
        	//comp.setVisible(true);   
    		//for(i=4; i< java.lang.Math.min(itemMenu,4+CountComp); i++){
//    		if(itemMenu>(4+CountComp)){
//    			if(this.getJMenuBar().getMenu(1).getItem(4+CountComp).getActionListeners()==null)
//    				this.getJMenuBar().getMenu(1).getItem(4+CountComp).addActionListener(radioBtnListner);
//        		this.getJMenuBar().getMenu(1).getItem(4+CountComp).setText(comp.getName());
//        	}
    		if(CountComp>=(itemMenu-4)){
    			//JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem();
    			JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
    			//bg.add(menuItem);
    			//menuItem.addActionListener(radioBtnListner);
    			menuItem.addActionListener(checkBoxBtnListner);
    			menuItem.setText("" + (CountComp+1) + " - " + layer.getName());
    			menuItem.setSelected(true);
    			this.getJMenuBar().getMenu(1).add(menuItem);
    		}
    		CountComp++;
        }   
    }
//*********************************************************************
//  GET VISIBLE COMPONENT
//*********************************************************************
    private java.awt.Component getVisibileComponent(){
    	java.awt.Component compImg=null; 
    	for (java.awt.Component layer : this.layeredPane.getComponents()){
         	//this.getContentPane().remove(comp);
         	if(layer.isVisible()){
         		compImg = layer;
         		break;
         	}
         }
    	return compImg;
    }
//*********************************************************************
//	RENDERING SFERICO
//*********************************************************************
    private void renderImage(ObjContainer objContainer){

    	//java.awt.Component compImg=getVisibileComponent();
    	java.awt.Component compImg=objContainer.getCompImg();
    	int i=0,j=0,m=0,Hmax=0;
    	double A=0,D=0,L=0,P=0,DD=0,DT=0,H=0,Hmin=0,Hi=0,Wi=0,zi=0,xi=0,yi=0,kx,kz;
    	double angi=0,Xr=0,Yr=0,Zr=0,Xg=0,Yg=0,Zg=0,Xg1=0,Yg1=0,Zg1=0,Xg2=0,Yg2=0,Zg2=0,R=0,Rq=0,cosalpha=0;
    	double alpha=0,xpos=0,ypos=0;
    	double r=0;
    	double xxc=0,yyc=0,zzc=0;
    	double xx0=0,yy0=0,zz0=0,xx1=0,yy1=0,zz1=0,xx1I=0,yy1I=0,zz1I=0,xxL=0,yyL=0,zzL=0;
    	double alp=0,bet=0,gam=0,dist=0,distmin=0,distmin0=0,modulus=0;
    	double zi1=0.0,xi1=0.0,yi1=0.0,zi2=0.0,xi2=0.0,yi2=0.0,Xr1=0.0,Yr1=0.0,Zr1=0.0,Xr2=0.0,Yr2=0.0,Zr2=0.0,XrL=0.0,YrL=0.0,ZrL=0.0;
    	double zi3=0.0,xi3=0.0,yi3=0.0,Xr3=0.0,Yr3=0.0,Zr3=0.0,Xg3=0.0,Yg3=0.0,Zg3=0.0;
    	double alphareflex1ground=0,cosalphaground=0,cosLambground=0;
    	double alphareflex=0,alphareflex1=0,cosalphatmp=0;
    	double cosLamb=0,cosLambreflexground=0;
    	double XrG=0,YrG=0,ZrG=0;
    	IdObj objId = new IdObj(0);
    	boolean breal = false,bombraG=false;
    	boolean btmp = false;
    	double prec=0;
    	double val=0;
    	int red=0,gre=0,blu=0,red2=0;
    	int green=0,blue=0;
    	PointResObj pointResObj = new PointResObj();
    	int blueTor = 0;
    	int greenTor = 201;
    	int redTor = 239;
    	boolean bombra = false;
    	TreeNode treeNodeDebug=null;
    	TreeNode treeNodeColor=null;
    	LinkedHashMap<String, ObjectBase> listObj = objContainer.getListObj();
    	
    	double XiR,YiR,ZiR,XgR, YgR, ZgR, XrR, YrR, ZrR;
    	PointResObj pointResObjOut = new PointResObj();
    	
//*******************************************************
// settaggio parametri scena
//*******************************************************    
    	
    	//prec = machinePrec();
    	Hi = compImg.getHeight();//bitmapinfo->bmiHeader.biHeight;
    	Wi = compImg.getWidth();//bitmapinfo->bmiHeader.biWidth;
    	
    	//Hm = 0.0;
    	Hmin = 100000;
    	D = ((double)Hi/2.0);// + 100*Hi;// distanza del piano dell'immagine dal piano XZ
    	L = (double)Wi/2.0;// coordinata del centro sull'asse orizzontale X
    	P = 0;//(double)bitmapinfo->bmiHeader.biHeight/2.0;// altezza del piano sezionale dell'immagine originale
  
    	Hmax = 1;
    	Hmin = 0;
    	A = Hi/1.0 + P;// altezza del centro sull'asse Z
    	H = Hi + Hmin;
    	DD = 0;// distanza dell'immagine originale dal piano di proiezione
    	DD = (D*P/Hi)*2;
    	DT = D + DD + Hi;
    	angi = Math.PI/4;
    	
    	r = Hi/3.0;
    	R = 3*r;//Hi/2.0-r;
    	Rq = R*R;
    	
    	
    	// centro del solido in coord originali
    	xxc = L-Hi/2.0;
    	/*yyc = D + DD + Hi/2;*/
    	//yyc = D + DD + r;
    	yyc = D + DD + 2*r;
    	zzc = r;
    	// centro di proiezione in coord originali
//    	xx0 = L;
//    	yy0 = 0;
//    	zz0 = A;
    	// punto luce in coord originali
    	xxL=-10.5*Hi;
    	yyL=-10.5*Hi;
    	zzL=10.5*Hi;
    	
    	// angoli di rotazione rispetto agli assi Z,Y,X rispettivamente
    	//alp = (5*PI/4)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	alp = 0;//(PI)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	bet = 0;//-PI/2.0;
    	gam = 0;//((PI)/2.0);
    	//Scena3D scena = new Scena3D(0,D,H,L, 0, -D,Wi,Hi,alp,bet,gam-Utility.PI/2);
    	Scena3D scena = new Scena3D(0,-10*Hi,12.5*Hi,L, Hi/2, -10*Hi,Wi,Hi,alp,bet,gam-Utility.PI/2-Utility.PI/4);
    	setScena(scena);
    	double[] vout;
    	xx0 = scena.getX0();
    	yy0 = scena.getY0();
    	zz0 = scena.getZ0();
    	
//    	//Sfera sfera1 = new Sfera(1,xxc+4*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Sfera sfera1 = new Sfera(1,xxc,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Sfera sfera2 = new Sfera(2,xxc+2*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	//Sfera sfera3 = new Sfera(10,xxc+3*r,yyc-(Math.sqrt(3))*r,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	//Sfera sfera4 = new Sfera(11,xxc+3*r,yyc-(1.0/Math.sqrt(3))*r,zzc+(Math.sqrt(8.0/3.0)+2)*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Toroide toroide = new Toroide(3,xxc,yyc,zzc+0*r,r,R, alp, bet, gam,239,201,0,0.8,1.9);
//    	Parallelepipedo parallelepip = new Parallelepipedo(7, xxc+4*r, yyc-r, zzc+r, r,r,r, alp, bet, gam, 255, 255, 255, 0.5,0.0);
//    	Parallelepipedo parallelepip2 = new Parallelepipedo(8, xxc+2*r, yyc-r, zzc+r, r,r,r, alp+(Utility.PI)/6, bet+(Utility.PI)/6, gam+(Utility.PI)/6, 255, 255, 255, 0.5,0.0);
//    	Parallelepipedo parallelepip3 = new Parallelepipedo(9, xxc+0.5*r, yyc+2.5*r, zzc-0.5*r, r,r,r, alp+(Utility.PI)/6, bet, gam, 255, 255, 255, 0.5,0.0);
//    	Piano piano = new Piano(99, xxc, yyc,0 , alp, bet, gam+(Utility.PI)/2, 255, 255, 255, 0.5,0.0);
//    	LinkedHashMap<String, ObjectBase> listObj = new LinkedHashMap<String, ObjectBase>();
//    	listObj.put("sfera1", sfera1);
//    	listObj.put("sfera2", sfera2);
//    	//listObj.put("sfera3", sfera3);
//    	//listObj.put("sfera4", sfera4);
//    	listObj.put("toroide1", toroide);
//    	//listObj.put("rettangolo1", rettangolo1);
//    	//listObj.put("rettangolo2", rettangolo2);
//    	//listObj.put("rettangolo3", rettangolo3);
//    	//listObj.put("parallelepipedo1", parallelepip);
//    	//listObj.put("parallelepipedo2", parallelepip2);
//    	//listObj.put("parallelepipedo3", parallelepip3);
//    	listObj.put("pavimento", piano);
    	LinkedList<ColorResult> listColorRes = new LinkedList<ColorResult>();
    	
    	distmin0 = Math.sqrt((Math.pow((xxc)-xx0,2.0)+Math.pow((yyc)-yy0,2.0)+Math.pow((zzc)-zz0,2.0))) + 16*R + 16*r;
    	//res = xiI*a11I+yiI*a21I+ziI*a31I;;

    	
    	//imgtmp = (JSAMPLE *) new JSAMPLE[(Hi * (img_width))];
    	BufferedImage imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	setImageRif(imagetmp);
    	Graphics g = imagetmp.getGraphics();
    	g.setColor(compImg.getForeground());
        setFont(compImg.getFont());
        //compImg.paintAll(g);
        //g.drawOval(100, 100, 50, 20);
		for(m=0;m<Hmax;m++){		
    			for(i=0;(i<(Hi));i++){
    			//for(i=(Hi-1);(i>=0);i--){
    				for(j=0;j<(Wi);j++){
    					if(bdebug && ((i==ipos && j==jpos) || (i==381 && j==515))/*(i==413 && j==181)*//*(i==206 && j==379)*//*(i==278 && j==248)*//*(i==540 && j==688)*//*(i==123 && j==517)*//*(i==152 && j==457)*//*(i==230 && j==347)*//*(i==155 && j==535)*//*(i==65 && j==193)*//*(i==197 && j==125)*/ /*|| (i==61 && j==215) || (i==77 && j==232) || (i==155 && j==246) || (i==49 && j==100) || (i==202 && j==208) || (i==41 && j==184) || (i==146 && j==72) || (i==156 && j==49) || (i==154 && j==451)*/){
    						i= i+0;
    						Utility.debugGrafico=true;
    					}
    					breal = false;
    					// punto sul piano di proiezione in coord originali
//    					xx1 = j;
//    					yy1 = D;
//    					zz1 = H-i;
    					
    					vout=scena.img2fix(j, i, 0);
    					xx1 = vout[0];
    					yy1 = vout[1];
    					zz1 = vout[2];
//    					if(Math.abs(xx1-vout[0])>Utility.EPSM || Math.abs(yy1-vout[1])>Utility.EPSM || Math.abs(zz1-vout[2])>Utility.EPSM){
//    						i= i+0;
//    					}
    					
    					if(Utility.debugGrafico){
    	    				treeNodeDebug = new TreeNode();
    	    				binaryTreeDebug.setRootNode(treeNodeDebug);
    	    				binaryTreeDebug.setCurrentNode(treeNodeDebug);
    	    				Vettore vet = new Vettore(xx0,yy0,zz0);
    	    				treeNodeDebug.setElement(vet);
    	    			}
    					{
    						treeNodeColor = new TreeNode();
    						binaryTreeColor.setCurrentNode(treeNodeColor);
    						treeNodeColor.setElement(new ColorResult());
    						binaryTreeColor.setRootNode(treeNodeColor);
    					}

    					// coff. angolari retta di proiezione in coord originali
    					kx = (xx1-xx0)/(yy1-yy0);
    					kz = (zz1-zz0)/(yy1-yy0);
    					modulus = Math.sqrt((xx1-xx0)*(xx1-xx0)+(yy1-yy0)*(yy1-yy0)+(zz1-zz0)*(zz1-zz0));
    					distmin = distmin0;

    					Xr=xx1-xx0;
    					Yr=yy1-yy0;
    					Zr=zz1-zz0;
    					pointResObjOut.setXi(0);
    					pointResObjOut.setYi(0);
    					pointResObjOut.setZi(0);
    					pointResObjOut.setXr(0);
    					pointResObjOut.setYr(0);
    					pointResObjOut.setZr(0);
    					pointResObjOut.setXg(0);
    					pointResObjOut.setYg(0);
    					pointResObjOut.setZg(0);
    					/*if(breal && zi>=0)*/{
    						riflessione(xx0,yy0,zz0,xx1,yy1,zz1,xxL,yyL,zzL,Xg,Yg,Zg,Xr,Yr,Zr,i,j,imagetmp,compImg,DT,distmin0,objId,0,1,listObj,pointResObjOut,false,listColorRes,1);
    						XiR=pointResObjOut.getXi();
        			    	YiR=pointResObjOut.getYi();
        			    	ZiR=pointResObjOut.getZi();
        			    	XrR=pointResObjOut.getXr();
        			    	YrR=pointResObjOut.getYr();
        			    	ZrR=pointResObjOut.getZr();
        			    	XgR=pointResObjOut.getXg();
        			    	YgR=pointResObjOut.getYg();
        			    	ZgR=pointResObjOut.getZg();
        			    	bombra = pointResObjOut.isOmbra();
        			    	cosalpha=pointResObjOut.getCosalpha();
        			    	cosLamb=pointResObjOut.getCosLamb();
        			    	objId=pointResObjOut.getIdObj();
    					}
    					{
    	            		double resR=0.1;// colore background-cielo
    	            		double resG=0.1;
    	            		double resB=0.1;
//    	            		double luceReflexR=0;
//    	            		double luceReflexG=0;
//    	            		double luceReflexB=0;
//    	            		double coefReflex=0;
//    	            		double colObjR=0;
//    	            		double colObjG=0;
//    	            		double colObjB=0;
//    	            		
//    	            		
//    	            		Iterator<ColorResult> iter = listColorRes.descendingIterator();
//    	            		while (iter.hasNext()) {
//    	            			ColorResult colRes = iter.next();
//    	            			ObjectBase objTmp = getObjFromId(listObj,colRes.getIdObj());
//    	            			if(objTmp!=null){
//    	            				coefReflex=objTmp.getReflexCoef();
//    	            				colObjR=objTmp.getRed();
//    	            				colObjG=objTmp.getGreen();
//    	            				colObjB=objTmp.getBlue();
//    	            			}
//    	            			luceReflexR = resR*(coefReflex+(colObjR/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
//    	            			luceReflexG = resG*(coefReflex+(colObjG/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
//    	            			luceReflexB = resB*(coefReflex+(colObjB/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
//    	            			resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
//    	            			resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
//    	            			resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
//    	            		}
    	            		
    	            		double[] resArray=calcolaLuceColore(binaryTreeColor.getRootNode(),listObj,true);
    	            		resR=resArray[0];
    	            		resG=resArray[1];
    	            		resB=resArray[2];
    	            		
    	            		red2=(int)Math.round(255*(resR>1?1:resR));
    	            		gre=(int)Math.round(255*(resG>1?1:resG));
    	            		blu=(int)Math.round(255*(resB>1?1:resB));
    	            		if(red2<0)
    	            			red2=0;
    	            		if(gre<0)
    	            			gre=0;
    	            		if(blu<0)
    	            			blu=0;
    	            		Color c = new Color(red2,gre,blu);
							imagetmp.setRGB(j, i,c.getRGB());
							listColorRes.clear();
    	    			}
    					if(Utility.debugGrafico){
    						Utility.debugGrafico=false;
    	    			}
    				}
    			}
    			if(binaryTreeDebug.getRootNode()!=null){
   				    disegnaTraiettoria(binaryTreeDebug.getRootNode(),null,true);
    			}
		}
      addImage(imagetmp,"prova_copia_img");
    }
    
    
    
	boolean riflessione(double x0,double y0,double z0,double x1,double y1,double z1,double xl,double yl,double zl,double Xg,double Yg,double Zg,double Xr,double Yr,double Zr,int i,int j,BufferedImage imagetmp,java.awt.Component compImg,double DT,double distmin0,IdObj objId,int numreflex,double power,LinkedHashMap<String,ObjectBase> listObj,PointResObj pointResObjOut,boolean bombraOut,LinkedList<ColorResult> listColorRes,double coeffRifrazFrom){
    	double alphareflex=0,cosaA=0,cosB=0.0,cosCL=0,cosCR=0,reflexCoef=0.0,traspCoef=0.0;
    	double XrL=0,YrL=0,ZrL=0;
    	double xiR=0,yiR=0,ziR=0,XrR=0,YrR=0,ZrR=0,XgR=0,YgR=0,ZgR=0;
    	double distmin=0,dist=0,modulus=0,blueObj=0,greenObj=0,redObj=0;
    	TreeNode treeNode=null;
    	TreeNode treeNodeColor=null;
    	boolean btmp=false;
    	boolean breal=false,bombra=false;
    	IdObj objIdnew= new IdObj(0);
    	double coeffRifrazNew=0;
    	PointResObj pointResObj = new PointResObj();
    	distmin = distmin0;
    	double coeffRifrazFromTmp=1;
    	
    	if(power<Utility.POWER_LEVEL)
			return false;
    	
    	if(numreflex==0){
	    	pointResObjOut.setXi(x0);
			pointResObjOut.setYi(y0);
			pointResObjOut.setZi(z0);
			x1=x0;
			y1=y0;
			z1=z0;
    	}
    	else
    	{
        	pointResObjOut.setXi(x1);
    		pointResObjOut.setYi(y1);
    		pointResObjOut.setZi(z1);
        }
		pointResObjOut.setXr(Xr);
		pointResObjOut.setYr(Yr);
		pointResObjOut.setZr(Zr);
		pointResObjOut.setXg(Xg);
		pointResObjOut.setYg(Yg);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setOmbra(bombraOut);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setIdObj(objId);
		
    	
		
		
    	btmp=false;

    	/*if(!breal)*/// se non c'è ombra calcola la riflessione
    		breal = false;
    		distmin = distmin0;
    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    		


    		for(ObjectBase objtmp : listObj.values()){
				btmp=false;
				pointResObj.setXi(0);
				pointResObj.setYi(0);
				pointResObj.setZi(0);
				pointResObj.setXr(0);
				pointResObj.setYr(0);
				pointResObj.setZr(0);
				pointResObj.setXg(0);
				pointResObj.setYg(0);
				pointResObj.setZg(0);
				btmp = objtmp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.REFLEX);
				if(btmp && numreflex<Utility.NUMMAXREFLEX){
					double xi1tmp=pointResObj.getXi();
					double yi1tmp=pointResObj.getYi();
					double zi1tmp=pointResObj.getZi();
			    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
					if(dist<distmin){
						xiR=pointResObj.getXi();
    			    	yiR=pointResObj.getYi();
    			    	ziR=pointResObj.getZi();
    			    	XrR=pointResObj.getXr();
    			    	YrR=pointResObj.getYr();
    			    	ZrR=pointResObj.getZr();
    			    	XgR=pointResObj.getXg();
    			    	YgR=pointResObj.getYg();
    			    	ZgR=pointResObj.getZg();
    			    	blueObj = objtmp.getB(xiR,yiR,ziR);
        				greenObj = objtmp.getG(xiR,yiR,ziR);
        				redObj = objtmp.getR(xiR,yiR,ziR);
        				objtmp.setRed((int)redObj);
        				objtmp.setGreen((int)greenObj);
        				objtmp.setBlue((int)blueObj);
						distmin = dist;
						objIdnew.cloneId(objtmp.getIdObj());
						reflexCoef=objtmp.getReflexCoef();
						//traspCoef=objtmp.getTraspCoef();
						coeffRifrazNew=objtmp.getRifrazCoef();
						if(coeffRifrazNew>0){
							// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
		    				coeffRifrazFromTmp=1;
		    				reflexCoef=(Math.abs(coeffRifrazFromTmp-coeffRifrazNew))/(coeffRifrazFromTmp+coeffRifrazNew);
							reflexCoef=reflexCoef*reflexCoef;
							//reflexCoef=0;
							objtmp.setReflexCoef(reflexCoef);
						}
						breal = true;
					}
					else if(numreflex>=Utility.NUMMAXREFLEX){
						numreflex=numreflex+0;
					}
				}
			}
    		
    		
    		
    		if(breal){
    			if(Utility.debugGrafico){
    				treeNode = new TreeNode();
    				Vettore vet = new Vettore(xiR,yiR,ziR);
    				treeNode.setElement(vet);
    				treeNode.setParent(binaryTreeDebug.getCurrentNode());
    				binaryTreeDebug.getCurrentNode().setRightChild(treeNode);
    			}
	    		
	        	//** coseno angolo tra gradiente oggetto e vettore Luce incidente negativo (L)
    			cosCL = -(((xiR-xl)*XgR+(yiR-yl)*YgR+(ziR-zl)*ZgR))/(Math.sqrt((xiR-xl)*(xiR-xl)+(yiR-yl)*(yiR-yl)+(ziR-zl)*(ziR-zl)));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Riflesso Incidente (R)
	        	cosCR = ((XrR*XgR+YrR*YgR+ZrR*ZgR))/(Math.sqrt(XrR*XrR+YrR*YrR+ZrR*ZrR));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Incidente Inverso (-I)
	        	cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/(Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr));
	        	//** Calcolo Vettore riflesso XrL del punto luce sul punto incidente dell'oggetto con il vettore Luce incidente (retta luce)
	        	//alphareflex = (((x1-xl)*Xg+(y1-yl)*Yg+(z1-zl)*Zg)*2.0)/(Xg*Xg+Yg*Yg+Zg*Zg);
	        	//alphareflex = (((x1-xl)*XgR+(y1-yl)*YgR+(z1-zl)*ZgR)*2.0);
	    		alphareflex = (((xiR-xl)*XgR+(yiR-yl)*YgR+(ziR-zl)*ZgR)*2.0);
	        	XrL = (xiR-xl) - alphareflex*XgR;
	        	YrL = (yiR-yl) - alphareflex*YgR;
	        	ZrL = (ziR-zl) - alphareflex*ZgR;
	        	//** componente di luce che va verso il centro di proiezione
	        	//** coseno angolo tra vettore Luce riflesso (LR) e vettore Incidente Negativo (-I)
	        	cosaA = -(((Xr)*XrL+(Yr)*YrL+(Zr)*ZrL)/(Math.sqrt(XrL*XrL+YrL*YrL+ZrL*ZrL)*Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr)));
	        	if(cosaA<0)
	        		cosaA=0;
	        	if(cosCL<0)
	        		cosCL=0;
	        	if(Math.abs(cosaA-1)>=0.00001)
	        		cosaA=0;
	        	//cosalpha = Math.pow(cosalpha,1000);
	        	//pointResObjOut.setCosalpha(cosaA);
	        	//pointResObjOut.setCosLamb(cosB);
	        	
	        	//OMBREGGIATURA
	    		boolean brealombra = false;
	        	//** distanza punto incidente oggetto dal punto luce
	    		modulus = Math.sqrt((xl-xiR)*(xl-xiR)+(yl-yiR)*(yl-yiR)+(zl-ziR)*(zl-ziR));
	    		//xi1o=yi1o=zi1o=Xr1o=Yr1o=Zr1o=Xg1o=Yg1o=Zg1o=0;
	    		//** test se punto sull'oggetto è coperto dalla luce ( in ombra) dall'oggetto stesso: il gradiente forma un angolo maggiore di PI/2 con il vettore Luce negativo (da oggetto a luce) 
	    		if(((xl-xiR)*XgR+(yl-yiR)*YgR+(zl-ziR)*ZgR<=0))
	    			brealombra = true;
	    		else{
	    		//** se non è coperto da se stesso controllo se è coperto dagli altri oggetti

	    			brealombra = false;
					for(ObjectBase objtmp : listObj.values()){
						pointResObj.setXi(0);
						pointResObj.setYi(0);
						pointResObj.setZi(0);
						pointResObj.setXr(0);
						pointResObj.setYr(0);
						pointResObj.setZr(0);
						pointResObj.setXg(0);
						pointResObj.setYg(0);
						pointResObj.setZg(0);
						//if(objtmp.getIdObj().getId()!=objIdnew.getId())
							brealombra = objtmp.detect(xiR+(xl-xiR)/modulus,yiR+(yl-yiR)/modulus,ziR+(zl-ziR)/modulus,xiR,yiR,ziR,Xr,Yr,Zr,distmin0,pointResObj,objIdnew,Utility.OMBRA);
						if(brealombra)
							break;
					}
	    		}
	    		if(brealombra){
	    			bombra = true;
	    		}
	    		
	    		double rad_LT_R=0;
	    		double rad_LT_G=0;
	    		double rad_LT_B=0;
	    		double xVLT=0,XrLT=0;
	    		double yVLT=0,YrLT=0;
	    		double zVLT=0,ZrLT=0;
	    		double cosaA_LT=0;
	    		boolean bombraLuceDir=bombra;
	    		if(objIdnew.getId()==99 && Utility.getObjFromId(listObj,99).getArrayImg()!=null){
	    			
	    			double[] vetOutTo=null;
            		vetOutTo=scena.proiezioneInv(xiR, yiR, ziR);
            		Color img_color=null;
            		int j_new=(int)Math.round(vetOutTo[0]);
            		int i_new=(int)Math.round(vetOutTo[1]);
            		if((j_new>=0 && j_new<imagetmp.getWidth())&&(i_new>=0 && i_new<imagetmp.getHeight())){
            			int res_col = Utility.getObjFromId(listObj,99).getArrayImg()[j_new][i_new];
            			if(res_col!=-1){
		            		img_color = new Color(res_col);
		            		rad_LT_R=img_color.getRed()/255.0;
		            		rad_LT_G=img_color.getGreen()/255.0;
		            		rad_LT_B=img_color.getBlue()/255.0;
		//            		if(!bombra){
		//	    				radR=radR+1;
		//	            		radG=radG+1;
		//	            		radB=radB+1;
		//            		}
		            		xVLT=Utility.getObjFromId(listObj,99).getxVLT()[j_new][i_new];
		            		yVLT=Utility.getObjFromId(listObj,99).getyVLT()[j_new][i_new];
		            		zVLT=Utility.getObjFromId(listObj,99).getzVLT()[j_new][i_new];
		            		//** coseno angolo tra gradiente oggetto e vettore Luce Trasmessa incidente negativo (L)
		        			//** non serve perchè l'inclinazione della luce trasmessa è riportata in ArrayImg. 
		            		//cosCL_LT = -(((xVLT)*XgR+(yVLT)*YgR+(zVLT)*ZgR))/(Math.sqrt((xVLT)*(xVLT)+(yVLT)*(yVLT)+(zVLT)*(zVLT)));
		        			alphareflex = (((xVLT)*XgR+(yVLT)*YgR+(zVLT)*ZgR)*2.0);
		    	        	XrLT = (xVLT) - alphareflex*XgR;
		    	        	YrLT = (yVLT) - alphareflex*YgR;
		    	        	ZrLT = (zVLT) - alphareflex*ZgR;
		    	        	//** componente di luce che va verso il centro di proiezione
		    	        	//** coseno angolo tra vettore Luce Trasmessa riflesso (LR) e vettore Incidente Negativo (-I)
		    	        	cosaA_LT = -(((Xr)*XrLT+(Yr)*YrLT+(Zr)*ZrLT)/(Math.sqrt(XrLT*XrLT+YrLT*YrLT+ZrLT*ZrLT)*Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr)));
		    	        	if(cosaA_LT<0)
		    	        		cosaA_LT=0;
//		    	        	if(cosCL_LT<0)
//		    	        		cosCL_LT=0;
		    	        	if(Math.abs(cosaA_LT-1)>=0.00001)
		    	        		cosaA_LT=0;
		    	        	if(cosaA_LT>0)
		    	        		i=i;
            			}
            		}
        			if(bombra)
            			bombra=false;
            		
	    		}
	    		//pointResObjOut.setOmbra(bombra);
        		
        		ColorResult colorRes = new ColorResult();
        		//assert(coeffRifrazNew==1);
        		if(coeffRifrazNew>0.0){
        			// se il materiale è trasparente si ipotizza per semplicità che viene assorbita o trattenuta, (dal mezzo trasparente), la stessa quantità di luce che viene riflessa
//	        		colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL); 
//	        		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
//	        		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
        			colorRes.setLuceDirIncR(cosaA*reflexCoef); 
	        		colorRes.setLuceDirIncG(cosaA*reflexCoef);
	        		colorRes.setLuceDirIncB(cosaA*reflexCoef);
        		}
        		else{
        			colorRes.setLuceDirIncR((bombraLuceDir?0:(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB*cosCL))+rad_LT_R*(cosaA_LT*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB)); 
            		colorRes.setLuceDirIncG((bombraLuceDir?0:(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB*cosCL))+rad_LT_R*(cosaA_LT*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB));
            		colorRes.setLuceDirIncB((bombraLuceDir?0:(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB*cosCL))+rad_LT_B*(cosaA_LT*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB));
        			
//        			colorRes.setLuceDirIncR(cosaA*reflexCoef+(rad_LT_R)*(redObj/255.0)*(1-reflexCoef)*cosB*cosCL); 
//            		colorRes.setLuceDirIncG(cosaA*reflexCoef+(rad_LT_R)*(greenObj/255.0)*(1-reflexCoef)*cosB*cosCL);
//            		colorRes.setLuceDirIncB(cosaA*reflexCoef+(rad_LT_B)*(blueObj/255.0)*(1-reflexCoef)*cosB*cosCL);
        		}
        		colorRes.setCosB(cosB); 
        		colorRes.setOmbra(bombra);
        		colorRes.setIdObj(objIdnew);
        		colorRes.setCosC(cosCR);
        		listColorRes.add(colorRes);
        		colorRes.setTipoReflex(Utility.REFLEX_OPACO);
        		{
					treeNodeColor = new TreeNode();
					treeNodeColor.setElement(colorRes);
					treeNodeColor.setParent(binaryTreeDebug.getCurrentNode());
					binaryTreeColor.getCurrentNode().setRightChild(treeNodeColor);
				}
        		
//        		if(reflexCoef>0.0){
//        			if(Utility.debugGrafico){
//        				binaryTreeDebug.setCurrentNode(treeNode);
//        			}
//        			
//        			binaryTreeColor.setCurrentNode(treeNodeColor);
//        			riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreflex,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
//        		}
    			if(coeffRifrazNew>0.0){
    				// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
    				coeffRifrazFromTmp=1;
    				double XtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*XgR;
    				double YtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*YgR;
    				double ZtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*ZgR;
    				double tRNormMod=Math.sqrt(XtRNorm*XtRNorm+YtRNorm*YtRNorm+ZtRNorm*ZtRNorm);
    				
    				double XtRTang=Xr-XtRNorm;
    				double YtRTang=Yr-YtRNorm;
    				double ZtRTang=Zr-ZtRNorm;
    				double tRTangMod=Math.sqrt(XtRTang*XtRTang+YtRTang*YtRTang+ZtRTang*ZtRTang);
    				
    				XtRTang=XtRTang/tRTangMod;
    				YtRTang=YtRTang/tRTangMod;
    				ZtRTang=ZtRTang/tRTangMod;
    				
    				double cosI=-(XgR*Xr+YgR*Yr+ZgR*Zr)/Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    				double angoloI=Math.acos(cosI);
    				double senR = Math.sin(angoloI)*(coeffRifrazFromTmp/coeffRifrazNew);
    				double angoloR = Math.asin(senR);
    				    				
    				double XtR=XtRNorm+XtRTang*Math.tan(angoloR)*tRNormMod;
    				double YtR=YtRNorm+YtRTang*Math.tan(angoloR)*tRNormMod;
    				double ZtR=ZtRNorm+ZtRTang*Math.tan(angoloR)*tRNormMod;
    				
    				double cosT=-(XgR*XtR+YgR*YtR+ZgR*ZtR)/Math.sqrt(XtR*XtR+YtR*YtR+ZtR*ZtR);
    				colorRes.setCosC(cosT);
    				
    				if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
    				colorRes.setTipoReflex(Utility.RIFRAZIONE_OPACO);
    				binaryTreeColor.setCurrentNode(treeNodeColor);
    				trasmissione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XtR,YtR,ZtR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreflex,power*(1-reflexCoef),listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
    			}
    			if(reflexCoef>0.0){
        			if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
        			
        			binaryTreeColor.setCurrentNode(treeNodeColor);
        			riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreflex,power*(reflexCoef),listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
        		}
    		}
    	
    	return breal;
    }
	
	boolean trasmissione(double x0,double y0,double z0,double x1,double y1,double z1,double xl,double yl,double zl,double Xg,double Yg,double Zg,double Xr,double Yr,double Zr,int i,int j,BufferedImage imagetmp,java.awt.Component compImg,double DT,double distmin0,IdObj objId,int numreTrasm,double power,LinkedHashMap<String,ObjectBase> listObj,PointResObj pointResObjOut,boolean bombraOut,LinkedList<ColorResult> listColorRes,double coeffRifrazFrom){
    	double alphareflex=0,cosaA=0,cosB=0.0,cosCL=0,cosCR=0,reflexCoef=0.0;
    	double XrL=0,YrL=0,ZrL=0;
    	double xiR=0,yiR=0,ziR=0,XrR=0,YrR=0,ZrR=0,XgR=0,YgR=0,ZgR=0;
    	double distmin=0,dist=0,modulus=0,blueObj=0,greenObj=0,redObj=0;
    	double traspCoef=0;
    	boolean btmp=false;
    	boolean breal=false,bombra=false;
    	IdObj objIdnew= new IdObj(0);
    	double coeffRifrazNew=1;
    	PointResObj pointResObj = new PointResObj();
    	distmin = distmin0;
    	TreeNode treeNode=null;
    	TreeNode treeNodeColor=null;
    	
    	if(power<Utility.POWER_LEVEL)
			return false;
    	
    	if(numreTrasm==0){
	    	pointResObjOut.setXi(x0);
			pointResObjOut.setYi(y0);
			pointResObjOut.setZi(z0);
			x1=x0;
			y1=y0;
			z1=z0;
    	}
    	else
    	{
        	pointResObjOut.setXi(x1);
    		pointResObjOut.setYi(y1);
    		pointResObjOut.setZi(z1);
        }
		pointResObjOut.setXr(Xr);
		pointResObjOut.setYr(Yr);
		pointResObjOut.setZr(Zr);
		pointResObjOut.setXg(Xg);
		pointResObjOut.setYg(Yg);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setOmbra(bombraOut);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setIdObj(objId);
    	
    	btmp=false;

    		breal = false;
    		distmin = distmin0;
    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    		
//    		if(objId>100)
//    			objId=objId/100;
    		ObjectBase objTrasp=null;
    		objTrasp = Utility.getObjFromId(listObj, objId.getId());
    		pointResObj.setXi(0);
			pointResObj.setYi(0);
			pointResObj.setZi(0);
			pointResObj.setXr(0);
			pointResObj.setYr(0);
			pointResObj.setZr(0);
			pointResObj.setXg(0);
			pointResObj.setYg(0);
			pointResObj.setZg(0);
			try{
				btmp = objTrasp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.TRASP);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(btmp && numreTrasm<Utility.NUMMAXTRASM){
				double xi1tmp=pointResObj.getXi();
				double yi1tmp=pointResObj.getYi();
				double zi1tmp=pointResObj.getZi();
		    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
				//if(dist<distmin){
					xiR=pointResObj.getXi();
			    	yiR=pointResObj.getYi();
			    	ziR=pointResObj.getZi();
			    	XrR=pointResObj.getXr();
			    	YrR=pointResObj.getYr();
			    	ZrR=pointResObj.getZr();
			    	XgR=pointResObj.getXg();
			    	YgR=pointResObj.getYg();
			    	ZgR=pointResObj.getZg();
			    	blueObj = objTrasp.getB(xiR,yiR,ziR);
    				greenObj = objTrasp.getG(xiR,yiR,ziR);
    				redObj = objTrasp.getR(xiR,yiR,ziR);
					distmin = dist;
					objIdnew.cloneId(objTrasp.getIdObj());
					reflexCoef=objTrasp.getReflexCoef();
					//traspCoef=objTrasp.getTraspCoef();
					coeffRifrazNew=objTrasp.getRifrazCoef();
					
					breal = true;
				//}
//				else if(numreflex>=Utility.NUMMAXTRASM){
//					numretrasm=numretrasm+0;
//				}
			}
			if(breal){
				if(Utility.debugGrafico){
    				treeNode = new TreeNode();
    				Vettore vet = new Vettore(xiR,yiR,ziR);
    				treeNode.setElement(vet);
    				treeNode.setParent(binaryTreeDebug.getCurrentNode());
    				binaryTreeDebug.getCurrentNode().setLeftChild(treeNode);
    			}
	    		for(ObjectBase objtmp : listObj.values()){
					btmp=false;
					pointResObj.setXi(0);
					pointResObj.setYi(0);
					pointResObj.setZi(0);
					pointResObj.setXr(0);
					pointResObj.setYr(0);
					pointResObj.setZr(0);
					pointResObj.setXg(0);
					pointResObj.setYg(0);
					pointResObj.setZg(0);
					if(objtmp.getIdObj().getId()!=objId.getId())
						btmp = objtmp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.REFLEX);
					if(btmp && numreTrasm<Utility.NUMMAXTRASM){
						double xi1tmp=pointResObj.getXi();
						double yi1tmp=pointResObj.getYi();
						double zi1tmp=pointResObj.getZi();
				    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
						if(dist<=distmin){
							xiR=pointResObj.getXi();
	    			    	yiR=pointResObj.getYi();
	    			    	ziR=pointResObj.getZi();
	    			    	XrR=pointResObj.getXr();
	    			    	YrR=pointResObj.getYr();
	    			    	ZrR=pointResObj.getZr();
	    			    	XgR=pointResObj.getXg();
	    			    	YgR=pointResObj.getYg();
	    			    	ZgR=pointResObj.getZg();
	    			    	blueObj = objtmp.getB(xiR,yiR,ziR);
	        				greenObj = objtmp.getG(xiR,yiR,ziR);
	        				redObj = objtmp.getR(xiR,yiR,ziR);
							distmin = dist;
							//objIdnew = objtmp.getIdObj();
							objIdnew.cloneId(objtmp.getIdObj());
							reflexCoef=objtmp.getReflexCoef();
							//traspCoef=objtmp.getTraspCoef();
							coeffRifrazNew=objtmp.getRifrazCoef();
							breal = true;
						}
						else if(numreTrasm>=Utility.NUMMAXTRASM){
							numreTrasm=numreTrasm+0;
						}
					}
				}
			}
    		// Se objIdnew = Id oggetto trasparente significa che si esce dal mezzo trasparente e ci spropaga in aria o vuoto
    		if(objIdnew.getId() == objTrasp.getIdObj().getId()){
	    		if(breal){
	    			// rendo negativo il gradinete perchè sono nell'interno dell'oggetto trasparente
//	    			XgR=-XgR;
//	    			YgR=-YgR;
//	    			ZgR=-ZgR;
	    			// esco in aria o vuoto per cui
	    			double coeffRifrazNewTmp = 1;
	    			
		        	//** coseno angolo tra gradiente oggetto e vettore Luce incidente negativo (-L=-(xiR-xl,yiR-yl,ziR-zl))
	    			cosCL = -(((xiR-xl)*XgR+(yiR-yl)*YgR+(ziR-zl)*ZgR))/(Math.sqrt((xiR-xl)*(xiR-xl)+(yiR-yl)*(yiR-yl)+(ziR-zl)*(ziR-zl)));
		        	//** coseno angolo tra gradiente oggetto (G) e vettore Riflesso Incidente (R=(XrR,YrR,ZrR)
		        	cosCR = ((XrR*XgR+YrR*YgR+ZrR*ZgR))/(Math.sqrt(XrR*XrR+YrR*YrR+ZrR*ZrR));
		        	//** coseno angolo tra gradiente oggetto (G) e vettore Incidente Inverso (-I=-(Xr,Yr,Zr))
		        	cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/(Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr));
		        	
		        	if(cosCL<0)
		        		cosCL=0;
		        	//OMBREGGIATURA
		    		boolean brealombra = false;
		        	//** distanza punto incidente oggetto dal punto luce
		    		modulus = Math.sqrt((xl-xiR)*(xl-xiR)+(yl-yiR)*(yl-yiR)+(zl-ziR)*(zl-ziR));
		    		//xi1o=yi1o=zi1o=Xr1o=Yr1o=Zr1o=Xg1o=Yg1o=Zg1o=0;
		    		//** test se punto sull'oggetto è coperto dalla luce ( in ombra) dall'oggetto stesso: il gradiente (interno perchè siamo dentro un oggetto trasparente) forma un angolo minore di PI/2 con il vettore Luce negativo (da oggetto a luce) 
		    		if(((xl-xiR)*XgR+(yl-yiR)*YgR+(zl-ziR)*ZgR>=0))
		    			brealombra = true;
		    		else{
		    		//** se non è coperto da se stesso controllo se è coperto dagli altri oggetti

		    			brealombra = false;
						for(ObjectBase objtmp : listObj.values()){
							pointResObj.setXi(0);
							pointResObj.setYi(0);
							pointResObj.setZi(0);
							pointResObj.setXr(0);
							pointResObj.setYr(0);
							pointResObj.setZr(0);
							pointResObj.setXg(0);
							pointResObj.setYg(0);
							pointResObj.setZg(0);
							// da prevedere di escludere gli oggetti trasparenti, da rivedere quando si implementa la Luminosità
							//if(objtmp.getIdObj().getId()!=objIdnew.getId())
								brealombra = objtmp.detect(xiR+(xl-xiR)/modulus,yiR+(yl-yiR)/modulus,ziR+(zl-ziR)/modulus,xiR,yiR,ziR,Xr,Yr,Zr,distmin0,pointResObj,objIdnew,Utility.OMBRA);
							if(brealombra)
								break;
						}
		    		}
		    		if(brealombra){
		    			bombra = true;
		    		}
		    		
		    		//pointResObjOut.setOmbra(bombra);
	        		
	        		ColorResult colorRes = new ColorResult();
	        		// metto cosA a zero per ora perchè la luce diretta sul mezzo trasparente verrà implementata con la Luminosità
	        		cosaA=0;
	        		//assert(coeffRifrazNew==1);
	        		if(coeffRifrazNew>0.0){
	        			reflexCoef=(Math.abs(coeffRifrazFrom-coeffRifrazNewTmp))/(coeffRifrazFrom+coeffRifrazNewTmp);
						reflexCoef=reflexCoef*reflexCoef;
						objTrasp.setReflexCoef(reflexCoef);
						// se il materiale è trasparente si ipotizza per semplicità che viene assorbita o trattenuta, (dal mezzo trasparente), la stessa quantità di luce che viene riflessa
//		        		colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL); 
//		        		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
//		        		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
		        		colorRes.setLuceDirIncR(cosaA*reflexCoef); 
		        		colorRes.setLuceDirIncG(cosaA*reflexCoef);
		        		colorRes.setLuceDirIncB(cosaA*reflexCoef);
	        		}
	        		else{
	        			colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB*cosCL); 
	            		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB*cosCL);
	            		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB*cosCL);
	        		}
	        		colorRes.setCosB(cosB); 
	        		colorRes.setCosC(cosCR);
	        		colorRes.setOmbra(bombra);
	        		colorRes.setIdObj(objIdnew);
	        		listColorRes.add(colorRes);
		        	if(Math.abs(cosB)>1)
		        		cosB=1*Math.signum(cosB);
		        	double angoloIncidente=Math.acos(cosB);
		        	double angoloLimite=Math.asin(coeffRifrazNewTmp/coeffRifrazFrom);
		        	
		        	{
						treeNodeColor = new TreeNode();
						treeNodeColor.setElement(colorRes);
						treeNodeColor.setParent(binaryTreeDebug.getCurrentNode());
						binaryTreeColor.getCurrentNode().setLeftChild(treeNodeColor);
					}
		        	
		        	if(angoloIncidente < angoloLimite){
		        		// esco in aria e calcolo tragitto del vettore rifratto
		        		coeffRifrazNewTmp=1;
		        		double XtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*XgR;
	    				double YtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*YgR;
	    				double ZtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*ZgR;
	    				double tRNormMod=Math.sqrt(XtRNorm*XtRNorm+YtRNorm*YtRNorm+ZtRNorm*ZtRNorm);
	    				
	    				double XtRTang=Xr-XtRNorm;
	    				double YtRTang=Yr-YtRNorm;
	    				double ZtRTang=Zr-ZtRNorm;
	    				double tRTangMod=Math.sqrt(XtRTang*XtRTang+YtRTang*YtRTang+ZtRTang*ZtRTang);
	    				
	    				XtRTang=XtRTang/tRTangMod;
	    				YtRTang=YtRTang/tRTangMod;
	    				ZtRTang=ZtRTang/tRTangMod;
	    				
	    				double cosI=-(XgR*Xr+YgR*Yr+ZgR*Zr)/Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
	    				double angoloI=Math.acos(cosI);
	    				double senR = Math.sin(angoloI)*(coeffRifrazFrom/coeffRifrazNewTmp);
	    				double angoloR = Math.asin(senR);
	    				    				
	    				double XtR=XtRNorm+XtRTang*Math.tan(angoloR)*tRNormMod;
	    				double YtR=YtRNorm+YtRTang*Math.tan(angoloR)*tRNormMod;
	    				double ZtR=ZtRNorm+ZtRTang*Math.tan(angoloR)*tRNormMod;
	    				
	    				double cosT=-(XgR*XtR+YgR*YtR+ZgR*ZtR)/Math.sqrt(XtR*XtR+YtR*YtR+ZtR*ZtR);
	    				colorRes.setCosT(cosT);
	    				colorRes.setTipoReflex(Utility.RIFRAZIONE_TRASP);
	    				if(Utility.debugGrafico){
	        				binaryTreeDebug.setCurrentNode(treeNode);
	        			}
	    				binaryTreeColor.setCurrentNode(treeNodeColor);
	    				riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XtR,YtR,ZtR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreTrasm,power*(1-reflexCoef),listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNewTmp);
	    				
	    				
	    				// se sono in rifrazione tengo conto anche della frazione di luce riflessa dall'interfaccia tra oggetto trasparente e aria
//			        	if(Utility.debugGrafico){
//		        			binaryTreeDebug.setCurrentNode(treeNode);
//		        		}
//			        	binaryTreeColor.setCurrentNode(treeNodeColor);
//		    			trasmissione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreTrasm,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
			        	
		        	}
		        	else
		        	{
		        		// rimango dentro l'oggetto trasparente, riflessione totale
		        		if(Utility.debugGrafico){
	        				binaryTreeDebug.setCurrentNode(treeNode);
	        			}
		        		binaryTreeColor.setCurrentNode(treeNodeColor);
		        		colorRes.setTipoReflex(Utility.REFLEX_TOT_TRASP);
		        		trasmissione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreTrasm,power,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
		        	}
		        	
		        	//riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreTrasm,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
		        	
	    		}
    		}
    		else{
    			// incontro altro oggetto interno o attaccato al bordo dell'oggetto trasparente
    			// se l'oggetto che incontro non è trasparente ho riflessione totale e proseguo la trasmissione nello stesso oggetto trasparente
    			// mentre, se l'oggetto che incontro è trasparente ho trasmissione nel secondo oggetto sempre che ho angolo minore dell'angolo limite 
    		}
    	
    	return true;
    }

//*********************************************************************
//	RADIOSITA'
//*********************************************************************
    private void radiosita(ObjContainer objContainer){

    	//java.awt.Component compImg=getVisibileComponent();
    	java.awt.Component compImg=objContainer.getCompImg();
    	int i=0,j=0,m=0,Hmax=0;
    	double A=0,D=0,L=0,P=0,DD=0,DT=0,H=0,Hmin=0,Hi=0,Wi=0,zi=0,xi=0,yi=0,kx,kz;
    	double angi=0,Xr=0,Yr=0,Zr=0,Xg=0,Yg=0,Zg=0,Xg1=0,Yg1=0,Zg1=0,Xg2=0,Yg2=0,Zg2=0,R=0,Rq=0,cosalpha=0;
    	double alpha=0,xpos=0,ypos=0;
    	double r=0;
    	double xxc=0,yyc=0,zzc=0;
    	double xx0=0,yy0=0,zz0=0,xx1=0,yy1=0,zz1=0,xx1I=0,yy1I=0,zz1I=0,xxL=0,yyL=0,zzL=0;
    	double alp=0,bet=0,gam=0,dist=0,distmin=0,distmin0=0,modulus=0;
    	double zi1=0.0,xi1=0.0,yi1=0.0,zi2=0.0,xi2=0.0,yi2=0.0,Xr1=0.0,Yr1=0.0,Zr1=0.0,Xr2=0.0,Yr2=0.0,Zr2=0.0,XrL=0.0,YrL=0.0,ZrL=0.0;
    	double zi3=0.0,xi3=0.0,yi3=0.0,Xr3=0.0,Yr3=0.0,Zr3=0.0,Xg3=0.0,Yg3=0.0,Zg3=0.0;
    	double alphareflex1ground=0,cosalphaground=0,cosLambground=0;
    	double alphareflex=0,alphareflex1=0,cosalphatmp=0;
    	double cosLamb=0,cosLambreflexground=0;
    	double XrG=0,YrG=0,ZrG=0;
    	IdObj objId = new IdObj(0);
    	boolean breal = false,bombraG=false;
    	boolean btmp = false;
    	double prec=0;
    	double val=0;
    	int red=0,gre=0,blu=0,red2=0;
    	int green=0,blue=0;
    	PointResObj pointResObj = new PointResObj();
    	int blueTor = 0;
    	int greenTor = 201;
    	int redTor = 239;
    	boolean bombra = false;
    	TreeNode treeNodeDebug=null;
    	TreeNode treeNodeColor=null;
    	LinkedHashMap<String, ObjectBase> listObj = objContainer.getListObj();
    	
    	double XiR,YiR,ZiR,XgR, YgR, ZgR, XrR, YrR, ZrR;
    	PointResObj pointResObjOut = new PointResObj();
    	
//*******************************************************
// settaggio parametri scena
//*******************************************************    
    	
    	//prec = machinePrec();
    	Hi = compImg.getHeight();//bitmapinfo->bmiHeader.biHeight;
    	Wi = compImg.getWidth();//bitmapinfo->bmiHeader.biWidth;
    	int[][] arrayImgOutValueMap = new int[(int)Wi][(int)Hi];
    	int[][] arrayImgOut = new int[(int)Wi][(int)Hi];
    	double[][] arrayImgOutR = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutG = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutB = new double[(int)Wi][(int)Hi];
    	ColorArray colorArray = new ColorArray();
    	colorArray.setArrayImgOutR(arrayImgOutR);
    	colorArray.setArrayImgOutG(arrayImgOutG);
    	colorArray.setArrayImgOutB(arrayImgOutB);
    	colorArray.setArrayImgOutValueMap(arrayImgOutValueMap);
    	double[][] xVLT = new double[(int)Wi][(int)Hi];
    	double[][] yVLT = new double[(int)Wi][(int)Hi];
    	double[][] zVLT = new double[(int)Wi][(int)Hi];
    	for(int i1=0;(i1<(Hi));i1++){
			for(int j1=0;j1<(Wi);j1++){
				arrayImgOutR[j1][i1]=0;
				arrayImgOutG[j1][i1]=0;
				arrayImgOutB[j1][i1]=0;
				arrayImgOut[j1][i1]=-1;
				arrayImgOutValueMap[j1][i1]=-1;
				xVLT[j1][i1]=-1;
				yVLT[j1][i1]=-1;
				zVLT[j1][i1]=-1;
    		}
    	}
    	objContainer.getObjFromId(99).setArrayImg(arrayImgOut);
    	objContainer.getObjFromId(99).setxVLT(xVLT);
    	objContainer.getObjFromId(99).setyVLT(yVLT);
    	objContainer.getObjFromId(99).setzVLT(zVLT);
    	
    	
    	//Hm = 0.0;
    	Hmin = 100000;
    	D = ((double)Hi/2.0);// + 100*Hi;// distanza del piano dell'immagine dal piano XZ
    	L = (double)Wi/2.0;// coordinata del centro sull'asse orizzontale X
    	P = 0;//(double)bitmapinfo->bmiHeader.biHeight/2.0;// altezza del piano sezionale dell'immagine originale
  
    	Hmax = 1;
    	Hmin = 0;
    	A = Hi/1.0 + P;// altezza del centro sull'asse Z
    	H = Hi + Hmin;
    	DD = 0;// distanza dell'immagine originale dal piano di proiezione
    	DD = (D*P/Hi)*2;
    	DT = D + DD + Hi;
    	angi = Math.PI/4;
    	
    	r = Hi/3.0;
    	R = 3*r;//Hi/2.0-r;
    	Rq = R*R;
    	
    	
    	// centro del solido in coord originali
    	xxc = L-Hi/2.0;
    	/*yyc = D + DD + Hi/2;*/
    	//yyc = D + DD + r;
    	yyc = D + DD + 2*r;
    	zzc = r;
    	// centro di proiezione in coord originali
//    	xx0 = L;
//    	yy0 = 0;
//    	zz0 = A;
    	// fonte luminosa, punto luce in coord originali
    	xxL=-10.5*Hi;
    	yyL=-10.5*Hi;
    	zzL=10.5*Hi;
    	
    	// angoli di rotazione rispetto agli assi Z,Y,X rispettivamente
    	//alp = (5*PI/4)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	alp = 0;//(PI)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	bet = 0;//-PI/2.0;
    	gam = 0;//((PI)/2.0);
    	//Scena3D scena = new Scena3D(0,D,H,L, 0, -D,Wi,Hi,alp,bet,gam-Utility.PI/2);
    	Scena3D scena = new Scena3D(0,-10*Hi,12.5*Hi,L, Hi/2, -10*Hi,Wi,Hi,alp,bet,gam-Utility.PI/2-Utility.PI/4);
    	setScena(scena);
    	double[] vout;
    	xx0 = scena.getX0();
    	yy0 = scena.getY0();
    	zz0 = scena.getZ0();
    	// nuovo centro di proiezione è la fonte luminosa
    	xx0 = xxL;
    	yy0 = yyL;
    	zz0 = zzL;
    	// punto osservazione, in coord originali
    	double xxV = scena.getX0();
    	double yyV = scena.getY0();
    	double zzV = scena.getZ0();
    	
//    	//Sfera sfera1 = new Sfera(1,xxc+4*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Sfera sfera1 = new Sfera(1,xxc,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Sfera sfera2 = new Sfera(2,xxc+2*r,yyc,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.5);
//    	//Sfera sfera3 = new Sfera(10,xxc+3*r,yyc-(Math.sqrt(3))*r,zzc+2*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	//Sfera sfera4 = new Sfera(11,xxc+3*r,yyc-(1.0/Math.sqrt(3))*r,zzc+(Math.sqrt(8.0/3.0)+2)*r,r, alp, bet, gam,180,180,255,0.8,1.9);
//    	Toroide toroide = new Toroide(3,xxc,yyc,zzc+0*r,r,R, alp, bet, gam,239,201,0,0.8,1.9);
//    	Parallelepipedo parallelepip = new Parallelepipedo(7, xxc+4*r, yyc-r, zzc+r, r,r,r, alp, bet, gam, 255, 255, 255, 0.5,0.0);
//    	Parallelepipedo parallelepip2 = new Parallelepipedo(8, xxc+2*r, yyc-r, zzc+r, r,r,r, alp+(Utility.PI)/6, bet+(Utility.PI)/6, gam+(Utility.PI)/6, 255, 255, 255, 0.5,0.0);
//    	Parallelepipedo parallelepip3 = new Parallelepipedo(9, xxc+0.5*r, yyc+2.5*r, zzc-0.5*r, r,r,r, alp+(Utility.PI)/6, bet, gam, 255, 255, 255, 0.5,0.0);
//    	Piano piano = new Piano(99, xxc, yyc,0 , alp, bet, gam+(Utility.PI)/2, 255, 255, 255, 0.5,0.0);
//    	LinkedHashMap<String, ObjectBase> listObj = new LinkedHashMap<String, ObjectBase>();
//    	listObj.put("sfera1", sfera1);
//    	listObj.put("sfera2", sfera2);
//    	//listObj.put("sfera3", sfera3);
//    	//listObj.put("sfera4", sfera4);
//    	listObj.put("toroide1", toroide);
//    	//listObj.put("rettangolo1", rettangolo1);
//    	//listObj.put("rettangolo2", rettangolo2);
//    	//listObj.put("rettangolo3", rettangolo3);
//    	//listObj.put("parallelepipedo1", parallelepip);
//    	//listObj.put("parallelepipedo2", parallelepip2);
//    	//listObj.put("parallelepipedo3", parallelepip3);
//    	listObj.put("pavimento", piano);
    	LinkedList<ColorResult> listColorRes = new LinkedList<ColorResult>();
    	
    	distmin0 = Math.sqrt((Math.pow((xxc)-xx0,2.0)+Math.pow((yyc)-yy0,2.0)+Math.pow((zzc)-zz0,2.0))) + 16*R + 16*r;
    	//res = xiI*a11I+yiI*a21I+ziI*a31I;;

    	
    	//imgtmp = (JSAMPLE *) new JSAMPLE[(Hi * (img_width))];
    	BufferedImage imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	setImageRif(imagetmp);
    	Graphics g = imagetmp.getGraphics();
    	//g.setColor(compImg.getForeground());
    	g.setColor(new Color(0,0,0));
        setFont(compImg.getFont());
        //compImg.paintAll(g);
        //g.drawOval(100, 100, 50, 20);
        int zoomL = Utility.Zoom;
		for(m=0;m<Hmax;m++){		
    			for(i=0;(i<(Hi));i++){
	    			for(int ii=0;(ii<(zoomL));ii++){
	    				for(j=0;j<(Wi);j++){
	    					if(bdebug && ((i==ipos && j==jpos)) /*|| (i==148 && j==720))*//*(i==413 && j==181)*//*(i==206 && j==379)*//*(i==278 && j==248)*//*(i==540 && j==688)*//*(i==123 && j==517)*//*(i==152 && j==457)*//*(i==230 && j==347)*//*(i==155 && j==535)*//*(i==65 && j==193)*//*(i==197 && j==125)*/ /*|| (i==61 && j==215) || (i==77 && j==232) || (i==155 && j==246) || (i==49 && j==100) || (i==202 && j==208) || (i==41 && j==184) || (i==146 && j==72) || (i==156 && j==49) || (i==154 && j==451)*/){
	    						i= i+0;
	    						Utility.debugGrafico=true;
	    					}
		    				for(int jj=0;(jj<(zoomL));jj++){
		    					breal = false;
		    					// punto sul piano di proiezione in coord originali
		//    					vout=scena.img2fix(j, i, 0);
		//    					xx1 = vout[0];
		//    					yy1 = vout[1];
		//    					zz1 = vout[2];
		    					double new_i=i+(1.0*ii)/zoomL;
		    					double new_j=j+(1.0*jj)/zoomL;
		    					// punto sul piano (pavimento) in coord originali
		    					//vout=scena.img2Obj(new_j, new_i, 0,distmin0,piano);
		    					vout=scena.img2Obj(new_j, new_i, 0,distmin0,objContainer.getObjFromId(99));
		    					
		    					xx1 = vout[0];
		    					yy1 = vout[1];
		    					zz1 = vout[2];
		//    					if(Math.abs(xx1-vout[0])>Utility.EPSM || Math.abs(yy1-vout[1])>Utility.EPSM || Math.abs(zz1-vout[2])>Utility.EPSM){
		//    						i= i+0;
		//    					}
		    					
		    					if(Utility.debugGrafico){
		    	    				treeNodeDebug = new TreeNode();
		    	    				binaryTreeDebug.setRootNode(treeNodeDebug);
		    	    				binaryTreeDebug.setCurrentNode(treeNodeDebug);
		    	    				Vettore vet = new Vettore(xx0,yy0,zz0);
		    	    				treeNodeDebug.setElement(vet);
		    	    			}
		    					{
		    						treeNodeColor = new TreeNode();
		    						binaryTreeColor.setCurrentNode(treeNodeColor);
		    						treeNodeColor.setElement(new ColorResult());
		    						binaryTreeColor.setRootNode(treeNodeColor);
		    					}
		
		    					// coff. angolari retta di proiezione in coord originali
		    					kx = (xx1-xx0)/(yy1-yy0);
		    					kz = (zz1-zz0)/(yy1-yy0);
		    					modulus = Math.sqrt((xx1-xx0)*(xx1-xx0)+(yy1-yy0)*(yy1-yy0)+(zz1-zz0)*(zz1-zz0));
		    					distmin = distmin0;
		
		    					Xr=xx1-xx0;
		    					Yr=yy1-yy0;
		    					Zr=zz1-zz0;
		    					pointResObjOut.setXi(0);
		    					pointResObjOut.setYi(0);
		    					pointResObjOut.setZi(0);
		    					pointResObjOut.setXr(0);
		    					pointResObjOut.setYr(0);
		    					pointResObjOut.setZr(0);
		    					pointResObjOut.setXg(0);
		    					pointResObjOut.setYg(0);
		    					pointResObjOut.setZg(0);
		    					/*if(breal && zi>=0)*/{
		    						ColorResult colorRes_R = new ColorResult();
		    						colorRes_R.setLuceDirIncR(1.0/(Utility.Zoom*Utility.Zoom));
		    						colorRes_R.setLuceDirIncG(1.0/(Utility.Zoom*Utility.Zoom));
		    						colorRes_R.setLuceDirIncB(1.0/(Utility.Zoom*Utility.Zoom));
		    						R_riflessione(xx0,yy0,zz0,xx1,yy1,zz1,xxV,yyV,zzV,Xg,Yg,Zg,Xr,Yr,Zr,i,j,(int)Hi,(int)Wi,colorArray,compImg,DT,distmin0,objId,0,listObj,pointResObjOut,false,listColorRes,1,colorRes_R,false);
		    						XiR=pointResObjOut.getXi();
		        			    	YiR=pointResObjOut.getYi();
		        			    	ZiR=pointResObjOut.getZi();
		        			    	XrR=pointResObjOut.getXr();
		        			    	YrR=pointResObjOut.getYr();
		        			    	ZrR=pointResObjOut.getZr();
		        			    	XgR=pointResObjOut.getXg();
		        			    	YgR=pointResObjOut.getYg();
		        			    	ZgR=pointResObjOut.getZg();
		        			    	bombra = pointResObjOut.isOmbra();
		        			    	cosalpha=pointResObjOut.getCosalpha();
		        			    	cosLamb=pointResObjOut.getCosLamb();
		        			    	objId=pointResObjOut.getIdObj();
		    					}
		    					if(1==0)
		    					{
		    	            		double resR=0.1;// colore background-cielo
		    	            		double resG=0.1;
		    	            		double resB=0.1;
		    	            		
		    	            		double[] resArray=calcolaLuceColore(binaryTreeColor.getRootNode(),listObj,true);
		    	            		resR=resArray[0];
		    	            		resG=resArray[1];
		    	            		resB=resArray[2];
		    	            		
		    	            		red2=(int)Math.round(255*(resR>1?1:resR));
		    	            		gre=(int)Math.round(255*(resG>1?1:resG));
		    	            		blu=(int)Math.round(255*(resB>1?1:resB));
		    	            		if(red2<0)
		    	            			red2=0;
		    	            		if(gre<0)
		    	            			gre=0;
		    	            		if(blu<0)
		    	            			blu=0;
		    	            		Color c = new Color(red2,gre,blu);
									imagetmp.setRGB(j, i,c.getRGB());
									listColorRes.clear();
		    	    			}
		    					listColorRes.clear();
		    					if(Utility.debugGrafico){
		    						Utility.debugGrafico=false;
		    	    			}
		    				}
	    				}
	    			}
    			}
//    			if(binaryTreeDebug.getRootNode()!=null){
//   				    disegnaTraiettoria(binaryTreeDebug.getRootNode(),null,true);
//    			}
		}
//		int fuctValR=0;
//		int fuctValG=0;
//		int fuctValB=0;
//		Color img_color;
//		for(int i1=0;(i1<((int)Hi));i1++){
//			for(int j1=0;j1<((int)Wi);j1++){
//				fuctValR=(int)Math.round(colorArray.getArrayImgOutR()[j1][i1]);
//				if(fuctValR>255)
//					fuctValR=255;
//				if(fuctValR<0)
//					fuctValR=0;
//				fuctValG=(int)Math.round(colorArray.getArrayImgOutG()[j1][i1]);
//				if(fuctValG>255)
//					fuctValG=255;
//				if(fuctValG<0)
//					fuctValG=0;
//				fuctValB=(int)Math.round(colorArray.getArrayImgOutB()[j1][i1]);
//				if(fuctValB>255)
//					fuctValB=255;
//				if(fuctValB<0)
//					fuctValB=0;
//				img_color = new Color(fuctValR,fuctValG,fuctValB);
//				imagetmp.setRGB(j1, i1, img_color.getRGB());
//				arrayImgOut[j1][i1]=img_color.getRGB();
//			}
//		}
		fillImage(colorArray,imagetmp,arrayImgOut);
		if(binaryTreeDebug.getRootNode()!=null){
			    disegnaTraiettoria(binaryTreeDebug.getRootNode(),null,true);
		}
	    addImage(imagetmp,"radiosity_raw_img");
	    
	    // Foto oggetto
	    ObjectBase objref=Utility.getObjFromId(listObj, 99);
	    snapShotObj(objref,(int)Wi,(int)Hi,distmin0);
	    // esperimento con MBA
        if(1==0){
	      int[][] imageOrigR = new int[(int)Wi][(int)Hi];
	      int[][] imageOrigG = new int[(int)Wi][(int)Hi];
	      int[][] imageOrigB = new int[(int)Wi][(int)Hi];
	      Color img_color;

	      int channel=Utility.RED;
		  	for(int i1=0;(i1<((int)Hi));i1++){
					for(int j1=0;j1<((int)Wi);j1++){
						if(colorArray.getArrayImgOutValueMap()[j1][i1]!=-1){
							img_color = new Color(arrayImgOut[j1][i1]);
							imageOrigR[j1][i1] = img_color.getRed();
							imageOrigG[j1][i1] = img_color.getGreen();
							imageOrigB[j1][i1] = img_color.getBlue();
							//soglia rumore
		//					if(imageOrigR[j1][i1]<9 && imageOrigG[j1][i1]<9 && imageOrigB[j1][i1]<9){
		//						imageOrigR[j1][i1]=-1;
		//						imageOrigG[j1][i1]=-1;
		//						imageOrigB[j1][i1]=-1;
		//					}
						}
						else{
							imageOrigR[j1][i1]=-1;
							imageOrigG[j1][i1]=-1;
							imageOrigB[j1][i1]=-1;
						}
		//				if(imageOrigR[j1][i1]!=imageOrigG[j1][i1] || imageOrigR[j1][i1]!=imageOrigB[j1][i1]){
		//					imageType=3;
		//				}
		  		}
		  	}
		  	
		  	MBA_new mba_new = new MBA_new(imageOrigR,(int)Hi,(int)Wi,32);
			long initime =  Calendar.getInstance().getTimeInMillis();
			double[][] arrayOutTmpR=mba_new.calcoloMBA();
			mba_new.initialize(imageOrigG,(int)Hi,(int)Wi,32);
			double[][] arrayOutTmpG=mba_new.calcoloMBA();
			mba_new.initialize(imageOrigB,(int)Hi,(int)Wi,32);
			double[][] arrayOutTmpB=mba_new.calcoloMBA();
			
			BufferedImage imagetmpMBAout = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
			int fuctValR=0;
			int fuctValG=0;
			int fuctValB=0;
			double fuctValRDouble=0;
			for(int i1=0;(i1<(Hi));i1++){
				for(int j1=0;j1<(Wi);j1++){
					fuctValR=(int)Math.round(arrayOutTmpR[j1][i1]);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					fuctValG=(int)Math.round(arrayOutTmpG[j1][i1]);
					if(fuctValG>255)
						fuctValG=255;
					if(fuctValG<0)
						fuctValG=0;
					fuctValB=(int)Math.round(arrayOutTmpB[j1][i1]);
					if(fuctValB>255)
						fuctValB=255;
					if(fuctValB<0)
						fuctValB=0;
					img_color = new Color(fuctValR,fuctValG,fuctValB);
					imagetmpMBAout.setRGB(j1, i1, img_color.getRGB());
				}
			}
			long fintime = Calendar.getInstance().getTimeInMillis();
			JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
			addImage(imagetmpMBAout,"radiosity_MBA_img");
		    //return arrayImgOut;
        }
    }
    
    boolean R_riflessione(double x0,double y0,double z0,double x1,double y1,double z1,double xV,double yV,double zV,double Xg,double Yg,double Zg,double Xr,double Yr,double Zr,int i,int j,int Hi,int Wi,ColorArray colorArray,java.awt.Component compImg,double DT,double distmin0,IdObj objId,int numreflex,LinkedHashMap<String,ObjectBase> listObj,PointResObj pointResObjOut,boolean bombraOut,LinkedList<ColorResult> listColorRes,double coeffRifrazFrom,ColorResult colorResInput,boolean bfromTrasm){
    	double alphareflex=0,cosaA=0,cosB=0.0,cosCV=0,cosCRL=0,reflexCoef=0.0,traspCoef=0.0;
    	double XrV=0,YrV=0,ZrV=0;
    	double xiR=0,yiR=0,ziR=0,XrR=0,YrR=0,ZrR=0,XgR=0,YgR=0,ZgR=0;
    	double distmin=0,dist=0,modulus=0,blueObj=0,greenObj=0,redObj=0;
    	TreeNode treeNode=null;
    	TreeNode treeNodeColor=null;
    	boolean btmp=false;
    	boolean breal=false,bombra=false;
    	IdObj objIdnew= new IdObj(0);
    	double coeffRifrazNew=0;
    	PointResObj pointResObj = new PointResObj();
    	distmin = distmin0;
    	double coeffRifrazFromTmp=1;
    	double cosxvLTold=0;
    	double cosyvLTold=0;
    	double coszvLTold=0;
    	
    	if(numreflex==0){
	    	pointResObjOut.setXi(x0);
			pointResObjOut.setYi(y0);
			pointResObjOut.setZi(z0);
			x1=x0;
			y1=y0;
			z1=z0;
    	}
    	else
    	{
        	pointResObjOut.setXi(x1);
    		pointResObjOut.setYi(y1);
    		pointResObjOut.setZi(z1);
        }
		pointResObjOut.setXr(Xr);
		pointResObjOut.setYr(Yr);
		pointResObjOut.setZr(Zr);
		pointResObjOut.setXg(Xg);
		pointResObjOut.setYg(Yg);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setOmbra(bombraOut);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setIdObj(objId);
		
    	
    	btmp=false;

    	/*if(!breal)*/// se non c'è ombra calcola la riflessione
    		breal = false;
    		distmin = distmin0;
    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    		


    		for(ObjectBase objtmp : listObj.values()){
				btmp=false;
				pointResObj.setXi(0);
				pointResObj.setYi(0);
				pointResObj.setZi(0);
				pointResObj.setXr(0);
				pointResObj.setYr(0);
				pointResObj.setZr(0);
				pointResObj.setXg(0);
				pointResObj.setYg(0);
				pointResObj.setZg(0);
				btmp = objtmp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.REFLEX);
				if(btmp && numreflex<Utility.NUMMAXREFLEX){
					double xi1tmp=pointResObj.getXi();
					double yi1tmp=pointResObj.getYi();
					double zi1tmp=pointResObj.getZi();
			    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
					if(dist<distmin){
						xiR=pointResObj.getXi();
    			    	yiR=pointResObj.getYi();
    			    	ziR=pointResObj.getZi();
    			    	XrR=pointResObj.getXr();
    			    	YrR=pointResObj.getYr();
    			    	ZrR=pointResObj.getZr();
    			    	XgR=pointResObj.getXg();
    			    	YgR=pointResObj.getYg();
    			    	ZgR=pointResObj.getZg();
    			    	blueObj = objtmp.getB(xiR,yiR,ziR);
        				greenObj = objtmp.getG(xiR,yiR,ziR);
        				redObj = objtmp.getR(xiR,yiR,ziR);
        				objtmp.setRed((int)redObj);
        				objtmp.setGreen((int)greenObj);
        				objtmp.setBlue((int)blueObj);
						distmin = dist;
						//objIdnew = objtmp.getIdObj();
						objIdnew.cloneId(objtmp.getIdObj());
						reflexCoef=objtmp.getReflexCoef();
						//traspCoef=objtmp.getTraspCoef();
						coeffRifrazNew=objtmp.getRifrazCoef();
						if(coeffRifrazNew>0){
							// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
		    				coeffRifrazFromTmp=1;
		    				reflexCoef=(Math.abs(coeffRifrazFromTmp-coeffRifrazNew))/(coeffRifrazFromTmp+coeffRifrazNew);
							reflexCoef=reflexCoef*reflexCoef;
							//reflexCoef=0;
							objtmp.setReflexCoef(reflexCoef);
						}
						breal = true;
					}
					else if(numreflex>=Utility.NUMMAXREFLEX){
						numreflex=numreflex+0;
					}
				}
			}
    		
    		if(breal){
    			if(Utility.debugGrafico){
    				treeNode = new TreeNode();
    				Vettore vet = new Vettore(xiR,yiR,ziR);
    				treeNode.setElement(vet);
    				treeNode.setParent(binaryTreeDebug.getCurrentNode());
    				binaryTreeDebug.getCurrentNode().setRightChild(treeNode);
    			}
    			// modulo Veettore Incidente (vettore Luce in questo caso)
    			double moduloVR= Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
	    		
	        	//** coseno angolo tra gradiente oggetto e vettore Punto di Osservazione negativo (-V)
    			cosCV = -(((xiR-xV)*XgR+(yiR-yV)*YgR+(ziR-zV)*ZgR))/(Math.sqrt((xiR-xV)*(xiR-xV)+(yiR-yV)*(yiR-yV)+(ziR-zV)*(ziR-zV)));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Luce Riflesso Incidente (RL)
	        	cosCRL = ((XrR*XgR+YrR*YgR+ZrR*ZgR))/(Math.sqrt(XrR*XrR+YrR*YrR+ZrR*ZrR));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Luce Incidente Inverso (-L)
	        	//cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/(Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr));
	        	cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/moduloVR;
	        	//** Calcolo Vettore riflesso XrV del punto Osservazione sul punto incidente dell'oggetto
	        	//alphareflex = (((x1-xl)*Xg+(y1-yl)*Yg+(z1-zl)*Zg)*2.0)/(Xg*Xg+Yg*Yg+Zg*Zg);
	        	//alphareflex = (((x1-xl)*XgR+(y1-yl)*YgR+(z1-zl)*ZgR)*2.0);
	    		alphareflex = (((xiR-xV)*XgR+(yiR-yV)*YgR+(ziR-zV)*ZgR)*2.0);
	        	XrV = (xiR-xV) - alphareflex*XgR;
	        	YrV = (yiR-yV) - alphareflex*YgR;
	        	ZrV = (ziR-zV) - alphareflex*ZgR;
	        	//** componente di luce che va verso il centro di proiezione
	        	//** coseno angolo tra vettore Luce riflesso (RL) e vettore Punto di Osservazione Negativo (-V)
	        	cosaA = -(((Xr)*XrV+(Yr)*YrV+(Zr)*ZrV)/(Math.sqrt(XrV*XrV+YrV*YrV+ZrV*ZrV)*Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr)));
	        	if(cosaA<0)
	        		cosaA=0;
	        	if(cosCV<0)
	        		cosCV=0;
	        	if(Math.abs(cosaA-1)>=0.00001)
	        		cosaA=0;
	        	//cosalpha = Math.pow(cosalpha,1000);
	        	//pointResObjOut.setCosalpha(cosaA);
	        	//pointResObjOut.setCosLamb(cosB);
	        	
	        	//OMBREGGIATURA
	    		boolean brealombra = false;
	        	//** distanza punto incidente oggetto dal punto luce
	    		modulus = Math.sqrt((xV-xiR)*(xV-xiR)+(yV-yiR)*(yV-yiR)+(zV-ziR)*(zV-ziR));
	    		//xi1o=yi1o=zi1o=Xr1o=Yr1o=Zr1o=Xg1o=Yg1o=Zg1o=0;
	    		//** test se punto sull'oggetto è coperto dalla luce ( in ombra) dall'oggetto stesso: il gradiente forma un angolo maggiore di PI/2 con il vettore Luce negativo (da oggetto a luce) 
	    		if(((xV-xiR)*XgR+(yV-yiR)*YgR+(zV-ziR)*ZgR<=0))
	    			brealombra = true;
	    		else{
	    		//** se non è coperto da se stesso controllo se è coperto dagli altri oggetti

	    			brealombra = false;
					for(ObjectBase objtmp : listObj.values()){
						pointResObj.setXi(0);
						pointResObj.setYi(0);
						pointResObj.setZi(0);
						pointResObj.setXr(0);
						pointResObj.setYr(0);
						pointResObj.setZr(0);
						pointResObj.setXg(0);
						pointResObj.setYg(0);
						pointResObj.setZg(0);
						//if(objtmp.getIdObj().getId()!=objIdnew.getId())
							brealombra = objtmp.detect(xiR+(xV-xiR)/modulus,yiR+(yV-yiR)/modulus,ziR+(zV-ziR)/modulus,xiR,yiR,ziR,Xr,Yr,Zr,distmin0,pointResObj,objIdnew,Utility.OMBRA);
						if(brealombra)
							break;
					}
	    		}
	    		if(brealombra){
	    			bombra = true;
	    		}
	    		
	    		//pointResObjOut.setOmbra(bombra);
        		
        		ColorResult colorRes = new ColorResult();
        		ColorResult colorRes_R = new ColorResult();
        		//assert(coeffRifrazNew==1);
        		if(coeffRifrazNew>0.0){
        			// se il materiale è trasparente si ipotizza per semplicità che viene assorbita o trattenuta, (dal mezzo trasparente), la stessa quantità di luce che viene riflessa
//	        		colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV); 
//	        		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV);
//	        		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV);
	        		colorRes.setLuceDirIncR(cosaA*reflexCoef*colorResInput.getLuceDirIncR()); 
	        		colorRes.setLuceDirIncG(cosaA*reflexCoef*colorResInput.getLuceDirIncG());
	        		colorRes.setLuceDirIncB(cosaA*reflexCoef*colorResInput.getLuceDirIncB());
        		}
        		else{
//        			colorRes.setLuceDirIncR((cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncR()); 
//            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncG());
//            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncB());
            		
            		colorRes.setLuceDirIncR((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncR()); 
            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncG());
            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncB());
        		}
        		colorRes.setCosB(cosB); 
        		colorRes.setOmbra(bombra);
        		colorRes.setIdObj(objIdnew);
        		colorRes.setCosC(cosCRL);
        		listColorRes.add(colorRes);
        		{
					treeNodeColor = new TreeNode();
					treeNodeColor.setElement(colorRes);
					treeNodeColor.setParent(binaryTreeDebug.getCurrentNode());
					binaryTreeColor.getCurrentNode().setRightChild(treeNodeColor);
				}
        		/*if(!bombra && numreflex>0 && objIdnew==99)*/
        		if(numreflex>0 && objIdnew.getId()==99)
        		{
        			ObjectBase objPav = Utility.getObjFromId(listObj,99);
        			// calcolo pixel del fotogramma con luce visibile
        			double[] vetOutTo=null;
        			double[] colorOut=null;
        			double resR=0;
        			double resG=0;
        			double resB=0;
        			double red2=0;
        			double gre=0;
        			double blu=0;
            		
            		vetOutTo=scena.proiezioneInv(xiR, yiR, ziR);
            		Color img_color=null;
            		int j_new=(int)Math.round(vetOutTo[0]);
            		int i_new=(int)Math.round(vetOutTo[1]);
            		if(j_new==370 && i_new==492){
            			i=i;
            		}
            		if((j_new>=0 && j_new<Wi)&&(i_new>=0 && i_new<Hi)){
   		
	            		resR=colorRes.getLuceDirIncR();
	        			resG=colorRes.getLuceDirIncG();
	        			resB=colorRes.getLuceDirIncB();
    			
	        			red2=(255.0)*(resR>1?1:resR) + colorArray.getArrayImgOutR()[j_new][i_new];
	        			gre=(255.0)*(resG>1?1:resG) + colorArray.getArrayImgOutG()[j_new][i_new];
	        			blu=(255.0)*(resB>1?1:resB) + colorArray.getArrayImgOutB()[j_new][i_new];
	            		
	           			colorArray.getArrayImgOutR()[j_new][i_new]=red2;
	        			colorArray.getArrayImgOutG()[j_new][i_new]=gre;
	        			colorArray.getArrayImgOutB()[j_new][i_new]=blu;
	        			colorArray.getArrayImgOutValueMap()[j_new][i_new]=0;
	           			
	           			// se provengo da una trasmissione allora setto i coseni direttori del vettore Luce incidente.  
	           			if(bfromTrasm){
           					i=i;
           					objPav.getxVLT()[j_new][i_new]= Xr/moduloVR;
    	           			objPav.getyVLT()[j_new][i_new]= Yr/moduloVR;
    	           			objPav.getzVLT()[j_new][i_new]= Zr/moduloVR;
           				}
            		}
            		resR=colorRes.getLuceDirIncR()*(Utility.Zoom*Utility.Zoom);
        			resG=colorRes.getLuceDirIncG()*(Utility.Zoom*Utility.Zoom);
        			resB=colorRes.getLuceDirIncB()*(Utility.Zoom*Utility.Zoom);
            		colorOut=objPav.getObjMapping(xiR, yiR, ziR);
            		red2=(255.0)*(resR>1?1:resR) + colorOut[0];
        			gre=(255.0)*(resG>1?1:resG) + colorOut[1];
        			blu=(255.0)*(resB>1?1:resB) + colorOut[2];
            		objPav.setObjMapping(xiR, yiR, ziR, red2, gre, blu);
        		}

    			if(coeffRifrazNew>0.0){
    				// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
    				coeffRifrazFromTmp=1;
    				double XtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*XgR;
    				double YtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*YgR;
    				double ZtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*ZgR;
    				double tRNormMod=Math.sqrt(XtRNorm*XtRNorm+YtRNorm*YtRNorm+ZtRNorm*ZtRNorm);
    				
    				double XtRTang=Xr-XtRNorm;
    				double YtRTang=Yr-YtRNorm;
    				double ZtRTang=Zr-ZtRNorm;
    				double tRTangMod=Math.sqrt(XtRTang*XtRTang+YtRTang*YtRTang+ZtRTang*ZtRTang);
    				
    				XtRTang=XtRTang/tRTangMod;
    				YtRTang=YtRTang/tRTangMod;
    				ZtRTang=ZtRTang/tRTangMod;
    				
    				double cosI=-(XgR*Xr+YgR*Yr+ZgR*Zr)/Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    				double angoloI=Math.acos(cosI);
    				double senR = Math.sin(angoloI)*(coeffRifrazFromTmp/coeffRifrazNew);
    				double angoloR = Math.asin(senR);
    				    				
    				double XtR=XtRNorm+XtRTang*Math.tan(angoloR)*tRNormMod;
    				double YtR=YtRNorm+YtRTang*Math.tan(angoloR)*tRNormMod;
    				double ZtR=ZtRNorm+ZtRTang*Math.tan(angoloR)*tRNormMod;
    				
    				double cosT=-(XgR*XtR+YgR*YtR+ZgR*ZtR)/Math.sqrt(XtR*XtR+YtR*YtR+ZtR*ZtR);
    				colorRes.setCosC(cosT);
    				
    				if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
    				binaryTreeColor.setCurrentNode(treeNodeColor);
    				// Luce trasmessa dall'Oggetto trasparente
    				colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*(redObj/255.0)*(1-reflexCoef)); 
    				colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*(greenObj/255.0)*(1-reflexCoef));
    				colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*(blueObj/255.0)*(1-reflexCoef));
    				R_trasmissione(x1,y1,z1,xiR,yiR,ziR,xV,yV,zV,XgR,YgR,ZgR,XtR,YtR,ZtR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreflex,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R);
    			}
    			if(reflexCoef>0.0){
        			if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
        			binaryTreeColor.setCurrentNode(treeNodeColor);
        			// Luce riflessa dall'Oggetto
            		colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*reflexCoef); 
            		colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*reflexCoef);
            		colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*reflexCoef);
        			R_riflessione(x1,y1,z1,xiR,yiR,ziR,xV,yV,zV,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreflex,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R,false);
        		}
    		}
    	
    	return true;
    }
	
	boolean R_trasmissione(double x0,double y0,double z0,double x1,double y1,double z1,double xl,double yl,double zl,double Xg,double Yg,double Zg,double Xr,double Yr,double Zr,int i,int j,int Hi,int Wi,ColorArray colorArray,java.awt.Component compImg,double DT,double distmin0,IdObj objId,int numreTrasm,LinkedHashMap<String,ObjectBase> listObj,PointResObj pointResObjOut,boolean bombraOut,LinkedList<ColorResult> listColorRes,double coeffRifrazFrom,ColorResult colorResInput){
    	double alphareflex=0,cosaA=0,cosB=0.0,cosCL=0,cosCR=0,reflexCoef=0.0;
    	double XrL=0,YrL=0,ZrL=0;
    	double xiR=0,yiR=0,ziR=0,XrR=0,YrR=0,ZrR=0,XgR=0,YgR=0,ZgR=0;
    	double distmin=0,dist=0,modulus=0,blueObj=0,greenObj=0,redObj=0;
    	double traspCoef=0;
    	boolean btmp=false;
    	boolean breal=false,bombra=false;
    	IdObj objIdnew= new IdObj(0);
    	double coeffRifrazNew=1;
    	PointResObj pointResObj = new PointResObj();
    	distmin = distmin0;
    	TreeNode treeNode=null;
    	TreeNode treeNodeColor=null;
    	
    	if(numreTrasm==0){
	    	pointResObjOut.setXi(x0);
			pointResObjOut.setYi(y0);
			pointResObjOut.setZi(z0);
			x1=x0;
			y1=y0;
			z1=z0;
    	}
    	else
    	{
        	pointResObjOut.setXi(x1);
    		pointResObjOut.setYi(y1);
    		pointResObjOut.setZi(z1);
        }
		pointResObjOut.setXr(Xr);
		pointResObjOut.setYr(Yr);
		pointResObjOut.setZr(Zr);
		pointResObjOut.setXg(Xg);
		pointResObjOut.setYg(Yg);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setOmbra(bombraOut);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setIdObj(objId);
    	
    	btmp=false;

    		breal = false;
    		distmin = distmin0;
    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    		
    		ObjectBase objTrasp = Utility.getObjFromId(listObj, objId.getId());
    		pointResObj.setXi(0);
			pointResObj.setYi(0);
			pointResObj.setZi(0);
			pointResObj.setXr(0);
			pointResObj.setYr(0);
			pointResObj.setZr(0);
			pointResObj.setXg(0);
			pointResObj.setYg(0);
			pointResObj.setZg(0);
			btmp = objTrasp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.TRASP);
			if(btmp && numreTrasm<Utility.NUMMAXTRASM){
				double xi1tmp=pointResObj.getXi();
				double yi1tmp=pointResObj.getYi();
				double zi1tmp=pointResObj.getZi();
		    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
				//if(dist<distmin){
					xiR=pointResObj.getXi();
			    	yiR=pointResObj.getYi();
			    	ziR=pointResObj.getZi();
			    	XrR=pointResObj.getXr();
			    	YrR=pointResObj.getYr();
			    	ZrR=pointResObj.getZr();
			    	XgR=pointResObj.getXg();
			    	YgR=pointResObj.getYg();
			    	ZgR=pointResObj.getZg();
			    	blueObj = objTrasp.getB(xiR,yiR,ziR);
    				greenObj = objTrasp.getG(xiR,yiR,ziR);
    				redObj = objTrasp.getR(xiR,yiR,ziR);
					distmin = dist;
					//objIdnew = objTrasp.getIdObj();
					objIdnew.cloneId(objTrasp.getIdObj());
					reflexCoef=objTrasp.getReflexCoef();
					//traspCoef=objTrasp.getTraspCoef();
					coeffRifrazNew=objTrasp.getRifrazCoef();
					breal = true;
				//}
//				else if(numreflex>=Utility.NUMMAXTRASM){
//					numretrasm=numretrasm+0;
//				}
			}
			if(breal){
				if(Utility.debugGrafico){
    				treeNode = new TreeNode();
    				Vettore vet = new Vettore(xiR,yiR,ziR);
    				treeNode.setElement(vet);
    				treeNode.setParent(binaryTreeDebug.getCurrentNode());
    				binaryTreeDebug.getCurrentNode().setLeftChild(treeNode);
    			}
	    		for(ObjectBase objtmp : listObj.values()){
					btmp=false;
					pointResObj.setXi(0);
					pointResObj.setYi(0);
					pointResObj.setZi(0);
					pointResObj.setXr(0);
					pointResObj.setYr(0);
					pointResObj.setZr(0);
					pointResObj.setXg(0);
					pointResObj.setYg(0);
					pointResObj.setZg(0);
					if(objtmp.getIdObj().getId()!=objId.getId())
						btmp = objtmp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.REFLEX);
					if(btmp && numreTrasm<Utility.NUMMAXTRASM){
						double xi1tmp=pointResObj.getXi();
						double yi1tmp=pointResObj.getYi();
						double zi1tmp=pointResObj.getZi();
				    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
						if(dist<=distmin){
							xiR=pointResObj.getXi();
	    			    	yiR=pointResObj.getYi();
	    			    	ziR=pointResObj.getZi();
	    			    	XrR=pointResObj.getXr();
	    			    	YrR=pointResObj.getYr();
	    			    	ZrR=pointResObj.getZr();
	    			    	XgR=pointResObj.getXg();
	    			    	YgR=pointResObj.getYg();
	    			    	ZgR=pointResObj.getZg();
	    			    	blueObj = objtmp.getB(xiR,yiR,ziR);
	        				greenObj = objtmp.getG(xiR,yiR,ziR);
	        				redObj = objtmp.getR(xiR,yiR,ziR);
							distmin = dist;
							//objIdnew = objtmp.getIdObj();
							objIdnew.cloneId(objtmp.getIdObj());
							reflexCoef=objtmp.getReflexCoef();
							//traspCoef=objtmp.getTraspCoef();
							coeffRifrazNew=objtmp.getRifrazCoef();
							breal = true;
						}
						else if(numreTrasm>=Utility.NUMMAXTRASM){
							numreTrasm=numreTrasm+0;
						}
					}
				}
			}
    		// Se objIdnew = Id oggetto trasparente significa che si esce dal mezzo trasparente e ci spropaga in aria o vuoto
    		if(objIdnew.getId() == objTrasp.getIdObj().getId()){
	    		if(breal){
	    			// rendo negativo il gradinete perchè sono nell'interno dell'oggetto trasparente
//	    			XgR=-XgR;
//	    			YgR=-YgR;
//	    			ZgR=-ZgR;
	    			// esco in aria o vuoto per cui
	    			double coeffRifrazNewTmp = 1;
	    			
		        	//** coseno angolo tra gradiente oggetto e vettore Luce incidente negativo (-L=-(xiR-xl,yiR-yl,ziR-zl))
	    			cosCL = -(((xiR-xl)*XgR+(yiR-yl)*YgR+(ziR-zl)*ZgR))/(Math.sqrt((xiR-xl)*(xiR-xl)+(yiR-yl)*(yiR-yl)+(ziR-zl)*(ziR-zl)));
		        	//** coseno angolo tra gradiente oggetto (G) e vettore Riflesso Incidente (R=(XrR,YrR,ZrR)
		        	cosCR = ((XrR*XgR+YrR*YgR+ZrR*ZgR))/(Math.sqrt(XrR*XrR+YrR*YrR+ZrR*ZrR));
		        	//** coseno angolo tra gradiente oggetto (G) e vettore Incidente Inverso (-I=-(Xr,Yr,Zr))
		        	cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/(Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr));
		        	
		        	if(cosCL<0)
		        		cosCL=0;
		        	//OMBREGGIATURA
		    		boolean brealombra = false;
		        	//** distanza punto incidente oggetto dal punto luce
		    		modulus = Math.sqrt((xl-xiR)*(xl-xiR)+(yl-yiR)*(yl-yiR)+(zl-ziR)*(zl-ziR));
		    		//xi1o=yi1o=zi1o=Xr1o=Yr1o=Zr1o=Xg1o=Yg1o=Zg1o=0;
		    		//** test se punto sull'oggetto è coperto dalla luce ( in ombra) dall'oggetto stesso: il gradiente (interno perchè siamo dentro un oggetto trasparente) forma un angolo minore di PI/2 con il vettore Luce negativo (da oggetto a luce) 
		    		if(((xl-xiR)*XgR+(yl-yiR)*YgR+(zl-ziR)*ZgR>=0))
		    			brealombra = true;
		    		else{
		    		//** se non è coperto da se stesso controllo se è coperto dagli altri oggetti

		    			brealombra = false;
						for(ObjectBase objtmp : listObj.values()){
							pointResObj.setXi(0);
							pointResObj.setYi(0);
							pointResObj.setZi(0);
							pointResObj.setXr(0);
							pointResObj.setYr(0);
							pointResObj.setZr(0);
							pointResObj.setXg(0);
							pointResObj.setYg(0);
							pointResObj.setZg(0);
							// da prevedere di escludere gli oggetti trasparenti, da rivedere quando si implementa la Luminosità
							//if(objtmp.getIdObj().getId()!=objIdnew.getId())
								brealombra = objtmp.detect(xiR+(xl-xiR)/modulus,yiR+(yl-yiR)/modulus,ziR+(zl-ziR)/modulus,xiR,yiR,ziR,Xr,Yr,Zr,distmin0,pointResObj,objIdnew,Utility.OMBRA);
							if(brealombra)
								break;
						}
		    		}
		    		if(brealombra){
		    			bombra = true;
		    		}
		    		
		    		//pointResObjOut.setOmbra(bombra);
	        		
	        		ColorResult colorRes = new ColorResult();
	        		ColorResult colorRes_R = new ColorResult();
	        		// metto cosA a zero per ora perchè la luce diretta sul mezzo trasparente verrà implementata con la Luminosità
	        		cosaA=0;
	        		//assert(coeffRifrazNew==1);
	        		if(coeffRifrazNew>0.0){
	        			reflexCoef=(Math.abs(coeffRifrazFrom-coeffRifrazNewTmp))/(coeffRifrazFrom+coeffRifrazNewTmp);
						reflexCoef=reflexCoef*reflexCoef;
						objTrasp.setReflexCoef(reflexCoef);
	        			// se il materiale è trasparente si ipotizza per semplicità che viene assorbita o trattenuta, (dal mezzo trasparente), la stessa quantità di luce che viene riflessa
//		        		colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL); 
//		        		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
//		        		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCL);
		        		colorRes.setLuceDirIncR(cosaA*reflexCoef*colorResInput.getLuceDirIncR()); 
		        		colorRes.setLuceDirIncG(cosaA*reflexCoef*colorResInput.getLuceDirIncG());
		        		colorRes.setLuceDirIncB(cosaA*reflexCoef*colorResInput.getLuceDirIncB());
	        		}
	        		else{
//	        			colorRes.setLuceDirIncR((cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB*cosCL)*colorResInput.getLuceDirIncR());
//	            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB*cosCL)*colorResInput.getLuceDirIncG());
//	            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB*cosCL)*colorResInput.getLuceDirIncB());
	            		
	            		colorRes.setLuceDirIncR((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncR());
	            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncG());
	            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncB());
	        		}
	        		colorRes.setCosB(cosB); 
	        		colorRes.setCosC(cosCR);
	        		colorRes.setOmbra(bombra);
	        		colorRes.setIdObj(objIdnew);
	        		listColorRes.add(colorRes);
		        	if(Math.abs(cosB)>1)
		        		cosB=1*Math.signum(cosB);
		        	double angoloIncidente=Math.acos(cosB);
		        	double angoloLimite=Math.asin(coeffRifrazNewTmp/coeffRifrazFrom);
		        	
		        	{
						treeNodeColor = new TreeNode();
						treeNodeColor.setElement(colorRes);
						treeNodeColor.setParent(binaryTreeDebug.getCurrentNode());
						binaryTreeColor.getCurrentNode().setLeftChild(treeNodeColor);
					}
		        	/*if(!bombra && objIdnew==99)*/
		        	if(objIdnew.getId()==99)
		        	{
		        		ObjectBase objPav = Utility.getObjFromId(listObj,99);
		        		// calcolo pixel del fotogramma con luce visibile
		        		double[] vetOutTo=null;
	        			double[] colorOut=null;
	        			double resR=0;
	        			double resG=0;
	        			double resB=0;
	        			double red2=0;
	        			double gre=0;
	        			double blu=0;
	            		
	            		vetOutTo=scena.proiezioneInv(xiR, yiR, ziR);
	            		Color img_color=null;
	            		int j_new=(int)Math.round(vetOutTo[0]);
	            		int i_new=(int)Math.round(vetOutTo[1]);
	            		if((j_new>=0 && j_new<Wi)&&(i_new>=0 && i_new<Hi)){
//	            			try{
//		            			img_color = new Color(imagetmp.getRGB(j_new, i_new));
//		            		}
//		            		catch(Exception e){
//		            			e.printStackTrace();
//		            		}
		            		
		            		resR=colorRes.getLuceDirIncR();
		        			resG=colorRes.getLuceDirIncG();
		        			resB=colorRes.getLuceDirIncB();
		        			
		        			red2=(255.0)*(resR>1?1:resR) + colorArray.getArrayImgOutR()[j_new][i_new];
		        			gre=(255.0)*(resG>1?1:resG) + colorArray.getArrayImgOutG()[j_new][i_new];
		        			blu=(255.0)*(resB>1?1:resB) + colorArray.getArrayImgOutB()[j_new][i_new];
		            		            		
		        			colorArray.getArrayImgOutR()[j_new][i_new]=red2;
		        			colorArray.getArrayImgOutG()[j_new][i_new]=gre;
		        			colorArray.getArrayImgOutB()[j_new][i_new]=blu;
		        			colorArray.getArrayImgOutValueMap()[j_new][i_new]=0;
	            		}
	            		resR=colorRes.getLuceDirIncR()*(Utility.Zoom*Utility.Zoom);
	        			resG=colorRes.getLuceDirIncG()*(Utility.Zoom*Utility.Zoom);
	        			resB=colorRes.getLuceDirIncB()*(Utility.Zoom*Utility.Zoom);
	            		colorOut=objPav.getObjMapping(xiR, yiR, ziR);
	            		red2=(255.0)*(resR>1?1:resR) + colorOut[0];
	        			gre=(255.0)*(resG>1?1:resG) + colorOut[1];
	        			blu=(255.0)*(resB>1?1:resB) + colorOut[2];
	            		objPav.setObjMapping(xiR, yiR, ziR, red2, gre, blu);
	        		}
		        	
		        	if(angoloIncidente < angoloLimite){
		        		// esco in aria e calcolo tragitto del vettore rifratto
		        		coeffRifrazNew=1;
		        		double XtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*XgR;
	    				double YtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*YgR;
	    				double ZtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*ZgR;
	    				double tRNormMod=Math.sqrt(XtRNorm*XtRNorm+YtRNorm*YtRNorm+ZtRNorm*ZtRNorm);
	    				
	    				double XtRTang=Xr-XtRNorm;
	    				double YtRTang=Yr-YtRNorm;
	    				double ZtRTang=Zr-ZtRNorm;
	    				double tRTangMod=Math.sqrt(XtRTang*XtRTang+YtRTang*YtRTang+ZtRTang*ZtRTang);
	    				
	    				XtRTang=XtRTang/tRTangMod;
	    				YtRTang=YtRTang/tRTangMod;
	    				ZtRTang=ZtRTang/tRTangMod;
	    				
	    				double cosI=-(XgR*Xr+YgR*Yr+ZgR*Zr)/Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
	    				double angoloI=Math.acos(cosI);
	    				double senR = Math.sin(angoloI)*(coeffRifrazFrom/coeffRifrazNew);
	    				double angoloR = Math.asin(senR);
	    				    				
	    				double XtR=XtRNorm+XtRTang*Math.tan(angoloR)*tRNormMod;
	    				double YtR=YtRNorm+YtRTang*Math.tan(angoloR)*tRNormMod;
	    				double ZtR=ZtRNorm+ZtRTang*Math.tan(angoloR)*tRNormMod;
	    				
	    				double cosT=-(XgR*XtR+YgR*YtR+ZgR*ZtR)/Math.sqrt(XtR*XtR+YtR*YtR+ZtR*ZtR);
	    				colorRes.setCosT(cosT);
	    				
	    				if(Utility.debugGrafico){
	        				binaryTreeDebug.setCurrentNode(treeNode);
	        			}
	    				binaryTreeColor.setCurrentNode(treeNodeColor);
	    				
	            		colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*(1-reflexCoef)); 
	    				colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*(1-reflexCoef));
	    				colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*(1-reflexCoef));
	    				R_riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XtR,YtR,ZtR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreTrasm,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R,true);
		        	}
		        	else
		        	{
		        		// rimango dentro l'oggetto trasparente, riflessione totale
		        		if(Utility.debugGrafico){
	        				binaryTreeDebug.setCurrentNode(treeNode);
	        			}
		        		binaryTreeColor.setCurrentNode(treeNodeColor);
		        		colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*reflexCoef); 
	            		colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*reflexCoef);
	            		colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*reflexCoef);
		        		R_trasmissione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreTrasm,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R);
		        	}
		        	

	    			//riflessione(x1,y1,z1,xiR,yiR,ziR,xl,yl,zl,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,imagetmp,compImg,DT,distmin0,objIdnew,++numreTrasm,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew);
	    		}
    		}
    		else{
    			// incontro altro oggetto interno o attaccato al bordo dell'oggetto trasparente
    			// se l'oggetto che incontro non è trasparente ho riflessione totale e proseguo la trasmissione nello stesso oggetto trasparente
    			// mentre, se l'oggetto che incontro è trasparente ho trasmissione nel secondo oggetto sempre che ho angolo minore dell'angolo limite 
    		}
    	
    	return true;
    }
	
//*********************************************************************
//	RADIOSITA' NEW
//*********************************************************************
    private void radiosita_new(ObjContainer objContainer){

    	//java.awt.Component compImg=getVisibileComponent();
    	java.awt.Component compImg=objContainer.getCompImg();
    	int i=0,j=0,m=0,Hmax=0;
    	double A=0,D=0,L=0,P=0,DD=0,DT=0,H=0,Hmin=0,Hi=0,Wi=0,zi=0,xi=0,yi=0,kx,kz;
    	double angi=0,Xr=0,Yr=0,Zr=0,Xg=0,Yg=0,Zg=0,Xg1=0,Yg1=0,Zg1=0,Xg2=0,Yg2=0,Zg2=0,R=0,Rq=0,cosalpha=0;
    	double alpha=0,xpos=0,ypos=0;
    	double r=0;
    	double xxc=0,yyc=0,zzc=0;
    	double xx0=0,yy0=0,zz0=0,xx1=0,yy1=0,zz1=0,xx1I=0,yy1I=0,zz1I=0,xxL=0,yyL=0,zzL=0;
    	double alp=0,bet=0,gam=0,dist=0,distmin=0,distmin0=0,modulus=0;
    	double zi1=0.0,xi1=0.0,yi1=0.0,zi2=0.0,xi2=0.0,yi2=0.0,Xr1=0.0,Yr1=0.0,Zr1=0.0,Xr2=0.0,Yr2=0.0,Zr2=0.0,XrL=0.0,YrL=0.0,ZrL=0.0;
    	double zi3=0.0,xi3=0.0,yi3=0.0,Xr3=0.0,Yr3=0.0,Zr3=0.0,Xg3=0.0,Yg3=0.0,Zg3=0.0;
    	double alphareflex1ground=0,cosalphaground=0,cosLambground=0;
    	double alphareflex=0,alphareflex1=0,cosalphatmp=0;
    	double cosLamb=0,cosLambreflexground=0;
    	double XrG=0,YrG=0,ZrG=0;
    	IdObj objId = new IdObj(0);
    	boolean breal = false,bombraG=false;
    	boolean btmp = false;
    	double prec=0;
    	double val=0;
    	int red=0,gre=0,blu=0,red2=0;
    	int green=0,blue=0;
    	PointResObj pointResObj = new PointResObj();
    	int blueTor = 0;
    	int greenTor = 201;
    	int redTor = 239;
    	boolean bombra = false;
    	TreeNode treeNodeDebug=null;
    	TreeNode treeNodeColor=null;
    	LinkedHashMap<String, ObjectBase> listObj = objContainer.getListObj();
    	
    	double XiR,YiR,ZiR,XgR, YgR, ZgR, XrR, YrR, ZrR;
    	PointResObj pointResObjOut = new PointResObj();
    	
//*******************************************************
// settaggio parametri scena
//*******************************************************    
    	
    	//prec = machinePrec();
    	Hi = compImg.getHeight();//bitmapinfo->bmiHeader.biHeight;
    	Wi = compImg.getWidth();//bitmapinfo->bmiHeader.biWidth;
    	int[][] arrayImgOutValueMap = new int[(int)Wi][(int)Hi];
    	int[][] arrayImgOut = new int[(int)Wi][(int)Hi];
    	double[][] arrayImgOutR = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutG = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutB = new double[(int)Wi][(int)Hi];
    	ColorArray colorArray = new ColorArray();
    	colorArray.setArrayImgOutR(arrayImgOutR);
    	colorArray.setArrayImgOutG(arrayImgOutG);
    	colorArray.setArrayImgOutB(arrayImgOutB);
    	colorArray.setArrayImgOutValueMap(arrayImgOutValueMap);
    	double[][] xVLT = new double[(int)Wi][(int)Hi];
    	double[][] yVLT = new double[(int)Wi][(int)Hi];
    	double[][] zVLT = new double[(int)Wi][(int)Hi];
    	for(int i1=0;(i1<(Hi));i1++){
			for(int j1=0;j1<(Wi);j1++){
				arrayImgOutR[j1][i1]=0;
				arrayImgOutG[j1][i1]=0;
				arrayImgOutB[j1][i1]=0;
				arrayImgOut[j1][i1]=-1;
				arrayImgOutValueMap[j1][i1]=-1;
				xVLT[j1][i1]=-1;
				yVLT[j1][i1]=-1;
				zVLT[j1][i1]=-1;
    		}
    	}
    	objContainer.getObjFromId(99).setArrayImg(arrayImgOut);
    	objContainer.getObjFromId(99).setxVLT(xVLT);
    	objContainer.getObjFromId(99).setyVLT(yVLT);
    	objContainer.getObjFromId(99).setzVLT(zVLT);
    	
    	
    	//Hm = 0.0;
    	Hmin = 100000;
    	D = ((double)Hi/2.0);// + 100*Hi;// distanza del piano dell'immagine dal piano XZ
    	L = (double)Wi/2.0;// coordinata del centro sull'asse orizzontale X
    	P = 0;//(double)bitmapinfo->bmiHeader.biHeight/2.0;// altezza del piano sezionale dell'immagine originale
  
    	Hmax = 1;
    	Hmin = 0;
    	A = Hi/1.0 + P;// altezza del centro sull'asse Z
    	H = Hi + Hmin;
    	DD = 0;// distanza dell'immagine originale dal piano di proiezione
    	DD = (D*P/Hi)*2;
    	DT = D + DD + Hi;
    	angi = Math.PI/4;
    	
    	r = Hi/3.0;
    	R = 3*r;//Hi/2.0-r;
    	Rq = R*R;
    	
    	
    	// centro del solido in coord originali
    	xxc = L-Hi/2.0;
    	/*yyc = D + DD + Hi/2;*/
    	//yyc = D + DD + r;
    	yyc = D + DD + 2*r;
    	zzc = r;
    	// centro di proiezione in coord originali
//    	xx0 = L;
//    	yy0 = 0;
//    	zz0 = A;
    	// fonte luminosa, punto luce in coord originali
    	xxL=-10.5*Hi;
    	yyL=-10.5*Hi;
    	zzL=10.5*Hi;
    	
    	// angoli di rotazione rispetto agli assi Z,Y,X rispettivamente
    	//alp = (5*PI/4)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	alp = 0;//(PI)+ Phase ;//+ Phase;//((PI)/2.0);//Phase;
    	bet = 0;//-PI/2.0;
    	gam = 0;//((PI)/2.0);
    	//Scena3D scena = new Scena3D(0,D,H,L, 0, -D,Wi,Hi,alp,bet,gam-Utility.PI/2);
    	Scena3D scena = new Scena3D(0,-10*Hi,12.5*Hi,L, Hi/2, -10*Hi,Wi,Hi,alp,bet,gam-Utility.PI/2-Utility.PI/4);
    	setScena(scena);
    	double[] vout;
    	xx0 = scena.getX0();
    	yy0 = scena.getY0();
    	zz0 = scena.getZ0();
    	// nuovo centro di proiezione è la fonte luminosa
    	xx0 = xxL;
    	yy0 = yyL;
    	zz0 = zzL;
    	// punto osservazione, in coord originali
    	double xxV = scena.getX0();
    	double yyV = scena.getY0();
    	double zzV = scena.getZ0();
    	
    	LinkedList<ColorResult> listColorRes = new LinkedList<ColorResult>();
    	
    	distmin0 = Math.sqrt((Math.pow((xxc)-xx0,2.0)+Math.pow((yyc)-yy0,2.0)+Math.pow((zzc)-zz0,2.0))) + 16*R + 16*r;
    	//res = xiI*a11I+yiI*a21I+ziI*a31I;;

    	
    	//imgtmp = (JSAMPLE *) new JSAMPLE[(Hi * (img_width))];
    	BufferedImage imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	setImageRif(imagetmp);
    	Graphics g = imagetmp.getGraphics();
    	//g.setColor(compImg.getForeground());
    	g.setColor(new Color(0,0,0));
        setFont(compImg.getFont());
        //compImg.paintAll(g);
        //g.drawOval(100, 100, 50, 20);
        int zoomL = Utility.Zoom;
        double HiObjMap=0;
        double WiObjMap=0;
        for(ObjectBase objscan : listObj.values()){
			btmp=false;
			HiObjMap=objscan.getHscan();
			WiObjMap=objscan.getWscan();
			for(i=0;(i<(HiObjMap));i++){
    			for(int ii=0;(ii<(zoomL));ii++){
    				for(j=0;j<(WiObjMap);j++){
    					if(bdebug && ((i==ipos && j==jpos)) /*|| (i==148 && j==720))*//*(i==413 && j==181)*//*(i==206 && j==379)*//*(i==278 && j==248)*//*(i==540 && j==688)*//*(i==123 && j==517)*//*(i==152 && j==457)*//*(i==230 && j==347)*//*(i==155 && j==535)*//*(i==65 && j==193)*//*(i==197 && j==125)*/ /*|| (i==61 && j==215) || (i==77 && j==232) || (i==155 && j==246) || (i==49 && j==100) || (i==202 && j==208) || (i==41 && j==184) || (i==146 && j==72) || (i==156 && j==49) || (i==154 && j==451)*/){
    						i= i+0;
    						Utility.debugGrafico=true;
    					}
	    				for(int jj=0;(jj<(zoomL));jj++){
	    					breal = false;
	    					
	    					objId.cloneId(objscan.getIdObj());
	    					// punto sull'oggetto in coord originali
	    					//vout=scena.img2Obj(new_j, new_i, 0,distmin0,piano);
	    					vout=objscan.map2Obj(j, i);
	    					
	    					xx1 = vout[0];
	    					yy1 = vout[1];
	    					zz1 = vout[2];
	//    					if(Math.abs(xx1-vout[0])>Utility.EPSM || Math.abs(yy1-vout[1])>Utility.EPSM || Math.abs(zz1-vout[2])>Utility.EPSM){
	//    						i= i+0;
	//    					}
	    					
	    					if(Utility.debugGrafico){
	    	    				treeNodeDebug = new TreeNode();
	    	    				binaryTreeDebug.setRootNode(treeNodeDebug);
	    	    				binaryTreeDebug.setCurrentNode(treeNodeDebug);
	    	    				Vettore vet = new Vettore(xx0,yy0,zz0);
	    	    				treeNodeDebug.setElement(vet);
	    	    			}
	    					{
	    						treeNodeColor = new TreeNode();
	    						binaryTreeColor.setCurrentNode(treeNodeColor);
	    						treeNodeColor.setElement(new ColorResult());
	    						binaryTreeColor.setRootNode(treeNodeColor);
	    					}
	
	    					// coff. angolari retta di proiezione in coord originali
	    					kx = (xx1-xx0)/(yy1-yy0);
	    					kz = (zz1-zz0)/(yy1-yy0);
	    					modulus = Math.sqrt((xx1-xx0)*(xx1-xx0)+(yy1-yy0)*(yy1-yy0)+(zz1-zz0)*(zz1-zz0));
	    					distmin = distmin0;
	
	    					Xr=xx1-xx0;
	    					Yr=yy1-yy0;
	    					Zr=zz1-zz0;
	    					pointResObjOut.setXi(0);
	    					pointResObjOut.setYi(0);
	    					pointResObjOut.setZi(0);
	    					pointResObjOut.setXr(0);
	    					pointResObjOut.setYr(0);
	    					pointResObjOut.setZr(0);
	    					pointResObjOut.setXg(0);
	    					pointResObjOut.setYg(0);
	    					pointResObjOut.setZg(0);
	    					/*if(breal && zi>=0)*/{
	    						ColorResult colorRes_R = new ColorResult();
	    						colorRes_R.setLuceDirIncR(1.0/(Utility.Zoom*Utility.Zoom));
	    						colorRes_R.setLuceDirIncG(1.0/(Utility.Zoom*Utility.Zoom));
	    						colorRes_R.setLuceDirIncB(1.0/(Utility.Zoom*Utility.Zoom));
	    						//R_new_riflessione(xx0,yy0,zz0,xx1,yy1,zz1,xxV,yyV,zzV,Xg,Yg,Zg,Xr,Yr,Zr,i,j,(int)Hi,(int)Wi,colorArray,compImg,DT,distmin0,objId,0,listObj,pointResObjOut,false,listColorRes,1,colorRes_R,false);
	    						R_new_riflessione(xx0,yy0,zz0,xx0,yy0,zz0,xxV,yyV,zzV,Xg,Yg,Zg,Xr,Yr,Zr,i,j,(int)Hi,(int)Wi,colorArray,compImg,DT,distmin0,objId,0,listObj,pointResObjOut,false,listColorRes,1,colorRes_R,false);
	    						XiR=pointResObjOut.getXi();
	        			    	YiR=pointResObjOut.getYi();
	        			    	ZiR=pointResObjOut.getZi();
	        			    	XrR=pointResObjOut.getXr();
	        			    	YrR=pointResObjOut.getYr();
	        			    	ZrR=pointResObjOut.getZr();
	        			    	XgR=pointResObjOut.getXg();
	        			    	YgR=pointResObjOut.getYg();
	        			    	ZgR=pointResObjOut.getZg();
	        			    	bombra = pointResObjOut.isOmbra();
	        			    	cosalpha=pointResObjOut.getCosalpha();
	        			    	cosLamb=pointResObjOut.getCosLamb();
	        			    	objId=pointResObjOut.getIdObj();
	    					}
	    					if(1==0)
	    					{
	    	            		double resR=0.1;// colore background-cielo
	    	            		double resG=0.1;
	    	            		double resB=0.1;
	    	            		
	    	            		double[] resArray=calcolaLuceColore(binaryTreeColor.getRootNode(),listObj,true);
	    	            		resR=resArray[0];
	    	            		resG=resArray[1];
	    	            		resB=resArray[2];
	    	            		
	    	            		red2=(int)Math.round(255*(resR>1?1:resR));
	    	            		gre=(int)Math.round(255*(resG>1?1:resG));
	    	            		blu=(int)Math.round(255*(resB>1?1:resB));
	    	            		if(red2<0)
	    	            			red2=0;
	    	            		if(gre<0)
	    	            			gre=0;
	    	            		if(blu<0)
	    	            			blu=0;
	    	            		Color c = new Color(red2,gre,blu);
								imagetmp.setRGB(j, i,c.getRGB());
								listColorRes.clear();
	    	    			}
	    					listColorRes.clear();
	    					if(Utility.debugGrafico){
	    						Utility.debugGrafico=false;
	    	    			}
	    				}
    				}
    			}
			}
		}

		fillImage(colorArray,imagetmp,arrayImgOut);
		if(binaryTreeDebug.getRootNode()!=null){
			    disegnaTraiettoria(binaryTreeDebug.getRootNode(),null,true);
		}
	    addImage(imagetmp,"radiosity_raw_img");
	    
	    // Foto oggetto
	    ObjectBase objref=Utility.getObjFromId(listObj, 99);
	    snapShotObj(objref,(int)Wi,(int)Hi,distmin0);
	    // esperimento con MBA
        if(1==0){
	      int[][] imageOrigR = new int[(int)Wi][(int)Hi];
	      int[][] imageOrigG = new int[(int)Wi][(int)Hi];
	      int[][] imageOrigB = new int[(int)Wi][(int)Hi];
	      Color img_color;

	      int channel=Utility.RED;
		  	for(int i1=0;(i1<((int)Hi));i1++){
					for(int j1=0;j1<((int)Wi);j1++){
						if(colorArray.getArrayImgOutValueMap()[j1][i1]!=-1){
							img_color = new Color(arrayImgOut[j1][i1]);
							imageOrigR[j1][i1] = img_color.getRed();
							imageOrigG[j1][i1] = img_color.getGreen();
							imageOrigB[j1][i1] = img_color.getBlue();
							//soglia rumore
		//					if(imageOrigR[j1][i1]<9 && imageOrigG[j1][i1]<9 && imageOrigB[j1][i1]<9){
		//						imageOrigR[j1][i1]=-1;
		//						imageOrigG[j1][i1]=-1;
		//						imageOrigB[j1][i1]=-1;
		//					}
						}
						else{
							imageOrigR[j1][i1]=-1;
							imageOrigG[j1][i1]=-1;
							imageOrigB[j1][i1]=-1;
						}
		//				if(imageOrigR[j1][i1]!=imageOrigG[j1][i1] || imageOrigR[j1][i1]!=imageOrigB[j1][i1]){
		//					imageType=3;
		//				}
		  		}
		  	}
		  	
		  	MBA_new mba_new = new MBA_new(imageOrigR,(int)Hi,(int)Wi,32);
			long initime =  Calendar.getInstance().getTimeInMillis();
			double[][] arrayOutTmpR=mba_new.calcoloMBA();
			mba_new.initialize(imageOrigG,(int)Hi,(int)Wi,32);
			double[][] arrayOutTmpG=mba_new.calcoloMBA();
			mba_new.initialize(imageOrigB,(int)Hi,(int)Wi,32);
			double[][] arrayOutTmpB=mba_new.calcoloMBA();
			
			BufferedImage imagetmpMBAout = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
			int fuctValR=0;
			int fuctValG=0;
			int fuctValB=0;
			double fuctValRDouble=0;
			for(int i1=0;(i1<(Hi));i1++){
				for(int j1=0;j1<(Wi);j1++){
					fuctValR=(int)Math.round(arrayOutTmpR[j1][i1]);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					fuctValG=(int)Math.round(arrayOutTmpG[j1][i1]);
					if(fuctValG>255)
						fuctValG=255;
					if(fuctValG<0)
						fuctValG=0;
					fuctValB=(int)Math.round(arrayOutTmpB[j1][i1]);
					if(fuctValB>255)
						fuctValB=255;
					if(fuctValB<0)
						fuctValB=0;
					img_color = new Color(fuctValR,fuctValG,fuctValB);
					imagetmpMBAout.setRGB(j1, i1, img_color.getRGB());
				}
			}
			long fintime = Calendar.getInstance().getTimeInMillis();
			JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
			addImage(imagetmpMBAout,"radiosity_MBA_img");
		    //return arrayImgOut;
        }
    }	

    boolean R_new_riflessione(double x0,double y0,double z0,double x1,double y1,double z1,double xV,double yV,double zV,double Xg,double Yg,double Zg,double Xr,double Yr,double Zr,int i,int j,int Hi,int Wi,ColorArray colorArray,java.awt.Component compImg,double DT,double distmin0,IdObj objId,int numreflex,LinkedHashMap<String,ObjectBase> listObj,PointResObj pointResObjOut,boolean bombraOut,LinkedList<ColorResult> listColorRes,double coeffRifrazFrom,ColorResult colorResInput,boolean bfromTrasm){
    	double alphareflex=0,cosaA=0,cosB=0.0,cosCV=0,cosCRL=0,reflexCoef=0.0,traspCoef=0.0;
    	double XrV=0,YrV=0,ZrV=0;
    	double xiR=0,yiR=0,ziR=0,XrR=0,YrR=0,ZrR=0,XgR=0,YgR=0,ZgR=0;
    	double distmin=0,dist=0,modulus=0,blueObj=0,greenObj=0,redObj=0;
    	TreeNode treeNode=null;
    	TreeNode treeNodeColor=null;
    	boolean btmp=false;
    	boolean breal=false,bombra=false;
    	IdObj objIdnew= new IdObj(0);
    	double coeffRifrazNew=0;
    	PointResObj pointResObj = new PointResObj();
    	distmin = distmin0;
    	double coeffRifrazFromTmp=1;
    	double cosxvLTold=0;
    	double cosyvLTold=0;
    	double coszvLTold=0;
    	
    	if(numreflex==0){
	    	pointResObjOut.setXi(x0);
			pointResObjOut.setYi(y0);
			pointResObjOut.setZi(z0);
			x1=x0;
			y1=y0;
			z1=z0;
    	}
    	else
    	{
        	pointResObjOut.setXi(x1);
    		pointResObjOut.setYi(y1);
    		pointResObjOut.setZi(z1);
        }
		pointResObjOut.setXr(Xr);
		pointResObjOut.setYr(Yr);
		pointResObjOut.setZr(Zr);
		pointResObjOut.setXg(Xg);
		pointResObjOut.setYg(Yg);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setOmbra(bombraOut);
		pointResObjOut.setZg(Zg);
		pointResObjOut.setIdObj(objId);
		
    	
    	btmp=false;

    	/*if(!breal)*/// se non c'è ombra calcola la riflessione
    		breal = false;
    		distmin = distmin0;
    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    		


    		for(ObjectBase objtmp : listObj.values()){
				btmp=false;
				pointResObj.setXi(0);
				pointResObj.setYi(0);
				pointResObj.setZi(0);
				pointResObj.setXr(0);
				pointResObj.setYr(0);
				pointResObj.setZr(0);
				pointResObj.setXg(0);
				pointResObj.setYg(0);
				pointResObj.setZg(0);
				btmp = objtmp.detect(x1+Xr/modulus,y1+Yr/modulus,z1+Zr/modulus,x1,y1,z1,Xr,Yr,Zr,distmin0,pointResObj,objId,Utility.REFLEX);
				if(btmp && numreflex<Utility.NUMMAXREFLEX){
					double xi1tmp=pointResObj.getXi();
					double yi1tmp=pointResObj.getYi();
					double zi1tmp=pointResObj.getZi();
			    	dist = Math.sqrt(Math.pow(xi1tmp-x1,2.0)+Math.pow(yi1tmp-y1,2.0)+Math.pow(zi1tmp-z1,2.0));
					if(dist<distmin){
						xiR=pointResObj.getXi();
    			    	yiR=pointResObj.getYi();
    			    	ziR=pointResObj.getZi();
    			    	XrR=pointResObj.getXr();
    			    	YrR=pointResObj.getYr();
    			    	ZrR=pointResObj.getZr();
    			    	XgR=pointResObj.getXg();
    			    	YgR=pointResObj.getYg();
    			    	ZgR=pointResObj.getZg();
    			    	blueObj = objtmp.getB(xiR,yiR,ziR);
        				greenObj = objtmp.getG(xiR,yiR,ziR);
        				redObj = objtmp.getR(xiR,yiR,ziR);
        				objtmp.setRed((int)redObj);
        				objtmp.setGreen((int)greenObj);
        				objtmp.setBlue((int)blueObj);
						distmin = dist;
						//objIdnew = objtmp.getIdObj();
						objIdnew.cloneId(objtmp.getIdObj());
						reflexCoef=objtmp.getReflexCoef();
						//traspCoef=objtmp.getTraspCoef();
						coeffRifrazNew=objtmp.getRifrazCoef();
						if(coeffRifrazNew>0){
							// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
		    				coeffRifrazFromTmp=1;
		    				reflexCoef=(Math.abs(coeffRifrazFromTmp-coeffRifrazNew))/(coeffRifrazFromTmp+coeffRifrazNew);
							reflexCoef=reflexCoef*reflexCoef;
							//reflexCoef=0;
							objtmp.setReflexCoef(reflexCoef);
						}
						breal = true;
					}
					else if(numreflex>=Utility.NUMMAXREFLEX){
						numreflex=numreflex+0;
					}
				}
			}
    		// al primo giro verifico se il punto appartiene all'oggetto in elaborazione (objScan), lo scarto se tra la luce e l'oggetto scan c'è un aoltro oggetto.
    		if(breal && (numreflex==0)){
    			if(!objId.isUguale(objIdnew))
    				breal = false;
    		}
    		
    		if(breal){
    			if(Utility.debugGrafico){
    				treeNode = new TreeNode();
    				Vettore vet = new Vettore(xiR,yiR,ziR);
    				treeNode.setElement(vet);
    				treeNode.setParent(binaryTreeDebug.getCurrentNode());
    				binaryTreeDebug.getCurrentNode().setRightChild(treeNode);
    			}
    			// modulo Veettore Incidente (vettore Luce in questo caso)
    			double moduloVR= Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
	    		
	        	//** coseno angolo tra gradiente oggetto e vettore Punto di Osservazione negativo (-V)
    			cosCV = -(((xiR-xV)*XgR+(yiR-yV)*YgR+(ziR-zV)*ZgR))/(Math.sqrt((xiR-xV)*(xiR-xV)+(yiR-yV)*(yiR-yV)+(ziR-zV)*(ziR-zV)));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Luce Riflesso Incidente (RL)
	        	cosCRL = ((XrR*XgR+YrR*YgR+ZrR*ZgR))/(Math.sqrt(XrR*XrR+YrR*YrR+ZrR*ZrR));
	        	//** coseno angolo tra gradiente oggetto (G) e vettore Luce Incidente Inverso (-L)
	        	//cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/(Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr));
	        	cosB = -(Xr*XgR+Yr*YgR+Zr*ZgR)/moduloVR;
	        	//** Calcolo Vettore riflesso XrV del punto Osservazione sul punto incidente dell'oggetto
	        	//alphareflex = (((x1-xl)*Xg+(y1-yl)*Yg+(z1-zl)*Zg)*2.0)/(Xg*Xg+Yg*Yg+Zg*Zg);
	        	//alphareflex = (((x1-xl)*XgR+(y1-yl)*YgR+(z1-zl)*ZgR)*2.0);
	    		alphareflex = (((xiR-xV)*XgR+(yiR-yV)*YgR+(ziR-zV)*ZgR)*2.0);
	        	XrV = (xiR-xV) - alphareflex*XgR;
	        	YrV = (yiR-yV) - alphareflex*YgR;
	        	ZrV = (ziR-zV) - alphareflex*ZgR;
	        	//** componente di luce che va verso il centro di proiezione
	        	//** coseno angolo tra vettore Luce riflesso (RL) e vettore Punto di Osservazione Negativo (-V)
	        	cosaA = -(((Xr)*XrV+(Yr)*YrV+(Zr)*ZrV)/(Math.sqrt(XrV*XrV+YrV*YrV+ZrV*ZrV)*Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr)));
	        	if(cosaA<0)
	        		cosaA=0;
	        	if(cosCV<0)
	        		cosCV=0;
	        	if(Math.abs(cosaA-1)>=0.00001)
	        		cosaA=0;
	        	//cosalpha = Math.pow(cosalpha,1000);
	        	//pointResObjOut.setCosalpha(cosaA);
	        	//pointResObjOut.setCosLamb(cosB);
	        	
	        	//OMBREGGIATURA
	    		boolean brealombra = false;
	        	//** distanza punto incidente oggetto dal punto luce
	    		modulus = Math.sqrt((xV-xiR)*(xV-xiR)+(yV-yiR)*(yV-yiR)+(zV-ziR)*(zV-ziR));
	    		//xi1o=yi1o=zi1o=Xr1o=Yr1o=Zr1o=Xg1o=Yg1o=Zg1o=0;
	    		//** test se punto sull'oggetto è coperto dalla luce ( in ombra) dall'oggetto stesso: il gradiente forma un angolo maggiore di PI/2 con il vettore Luce negativo (da oggetto a luce) 
	    		if(((xV-xiR)*XgR+(yV-yiR)*YgR+(zV-ziR)*ZgR<=0))
	    			brealombra = true;
	    		else{
	    		//** se non è coperto da se stesso controllo se è coperto dagli altri oggetti

	    			brealombra = false;
					for(ObjectBase objtmp : listObj.values()){
						pointResObj.setXi(0);
						pointResObj.setYi(0);
						pointResObj.setZi(0);
						pointResObj.setXr(0);
						pointResObj.setYr(0);
						pointResObj.setZr(0);
						pointResObj.setXg(0);
						pointResObj.setYg(0);
						pointResObj.setZg(0);
						//if(objtmp.getIdObj().getId()!=objIdnew.getId())
							brealombra = objtmp.detect(xiR+(xV-xiR)/modulus,yiR+(yV-yiR)/modulus,ziR+(zV-ziR)/modulus,xiR,yiR,ziR,Xr,Yr,Zr,distmin0,pointResObj,objIdnew,Utility.OMBRA);
						if(brealombra)
							break;
					}
	    		}
	    		if(brealombra){
	    			bombra = true;
	    		}
	    		
	    		//pointResObjOut.setOmbra(bombra);
	    		// al primo giro se il primo punto è in ombra lo scarto
	    		if(bombra && (numreflex==0)){
	    			return false;
	    		}
        		
        		ColorResult colorRes = new ColorResult();
        		ColorResult colorRes_R = new ColorResult();
        		//assert(coeffRifrazNew==1);
        		if(coeffRifrazNew>0.0){
        			// se il materiale è trasparente si ipotizza per semplicità che viene assorbita o trattenuta, (dal mezzo trasparente), la stessa quantità di luce che viene riflessa
//	        		colorRes.setLuceDirIncR(cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV); 
//	        		colorRes.setLuceDirIncG(cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV);
//	        		colorRes.setLuceDirIncB(cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*reflexCoef*cosB*cosCV);
	        		colorRes.setLuceDirIncR(cosaA*reflexCoef*colorResInput.getLuceDirIncR()); 
	        		colorRes.setLuceDirIncG(cosaA*reflexCoef*colorResInput.getLuceDirIncG());
	        		colorRes.setLuceDirIncB(cosaA*reflexCoef*colorResInput.getLuceDirIncB());
        		}
        		else{
//        			colorRes.setLuceDirIncR((cosaA*reflexCoef+(redObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncR()); 
//            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(greenObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncG());
//            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(blueObj/255.0)*(1-reflexCoef)*cosB*cosCV)*colorResInput.getLuceDirIncB());
            		
            		colorRes.setLuceDirIncR((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncR()); 
            		colorRes.setLuceDirIncG((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncG());
            		colorRes.setLuceDirIncB((cosaA*reflexCoef+(1-reflexCoef)*cosB)*colorResInput.getLuceDirIncB());
        		}
        		colorRes.setCosB(cosB); 
        		colorRes.setOmbra(bombra);
        		colorRes.setIdObj(objIdnew);
        		colorRes.setCosC(cosCRL);
        		listColorRes.add(colorRes);
        		{
					treeNodeColor = new TreeNode();
					treeNodeColor.setElement(colorRes);
					treeNodeColor.setParent(binaryTreeDebug.getCurrentNode());
					binaryTreeColor.getCurrentNode().setRightChild(treeNodeColor);
				}
        		/*if(!bombra && numreflex>0 && objIdnew==99)*/
        		if(numreflex>0 && objIdnew.getId()==99)
        		{
        			ObjectBase objPav = Utility.getObjFromId(listObj,99);
        			//ObjectBase objPav = Utility.getObjFromId(listObj,objIdnew.getId());
        			// calcolo pixel del fotogramma con luce visibile
        			double[] vetOutTo=null;
        			double[] colorOut=null;
        			double resR=0;
        			double resG=0;
        			double resB=0;
        			double red2=0;
        			double gre=0;
        			double blu=0;
            		
            		vetOutTo=scena.proiezioneInv(xiR, yiR, ziR);
            		Color img_color=null;
            		int j_new=(int)Math.round(vetOutTo[0]);
            		int i_new=(int)Math.round(vetOutTo[1]);
            		if(j_new==370 && i_new==492){
            			i=i;
            		}
            		if((j_new>=0 && j_new<Wi)&&(i_new>=0 && i_new<Hi)){
   		
	            		resR=colorRes.getLuceDirIncR();
	        			resG=colorRes.getLuceDirIncG();
	        			resB=colorRes.getLuceDirIncB();
    			
	        			red2=(255.0)*(resR>1?1:resR) + colorArray.getArrayImgOutR()[j_new][i_new];
	        			gre=(255.0)*(resG>1?1:resG) + colorArray.getArrayImgOutG()[j_new][i_new];
	        			blu=(255.0)*(resB>1?1:resB) + colorArray.getArrayImgOutB()[j_new][i_new];
	            		
	           			colorArray.getArrayImgOutR()[j_new][i_new]=red2;
	        			colorArray.getArrayImgOutG()[j_new][i_new]=gre;
	        			colorArray.getArrayImgOutB()[j_new][i_new]=blu;
	        			colorArray.getArrayImgOutValueMap()[j_new][i_new]=0;
	           			
	           			// se provengo da una trasmissione allora setto i coseni direttori del vettore Luce incidente.  
	           			if(bfromTrasm){
           					i=i;
           					objPav.getxVLT()[j_new][i_new]= Xr/moduloVR;
    	           			objPav.getyVLT()[j_new][i_new]= Yr/moduloVR;
    	           			objPav.getzVLT()[j_new][i_new]= Zr/moduloVR;
           				}
            		}
            		resR=colorRes.getLuceDirIncR()*(Utility.Zoom*Utility.Zoom);
        			resG=colorRes.getLuceDirIncG()*(Utility.Zoom*Utility.Zoom);
        			resB=colorRes.getLuceDirIncB()*(Utility.Zoom*Utility.Zoom);
            		colorOut=objPav.getObjMapping(xiR, yiR, ziR);
            		red2=(255.0)*(resR>1?1:resR) + colorOut[0];
        			gre=(255.0)*(resG>1?1:resG) + colorOut[1];
        			blu=(255.0)*(resB>1?1:resB) + colorOut[2];
            		objPav.setObjMapping(xiR, yiR, ziR, red2, gre, blu);
        		}

    			if(coeffRifrazNew>0.0){
    				// quando si entra in un oggetto trasparente venendo da una riflessione si presume sempre di provenire dal vuoto o aria.
    				coeffRifrazFromTmp=1;
    				double XtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*XgR;
    				double YtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*YgR;
    				double ZtRNorm=(XgR*Xr+YgR*Yr+ZgR*Zr)*ZgR;
    				double tRNormMod=Math.sqrt(XtRNorm*XtRNorm+YtRNorm*YtRNorm+ZtRNorm*ZtRNorm);
    				
    				double XtRTang=Xr-XtRNorm;
    				double YtRTang=Yr-YtRNorm;
    				double ZtRTang=Zr-ZtRNorm;
    				double tRTangMod=Math.sqrt(XtRTang*XtRTang+YtRTang*YtRTang+ZtRTang*ZtRTang);
    				
    				XtRTang=XtRTang/tRTangMod;
    				YtRTang=YtRTang/tRTangMod;
    				ZtRTang=ZtRTang/tRTangMod;
    				
    				double cosI=-(XgR*Xr+YgR*Yr+ZgR*Zr)/Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
    				double angoloI=Math.acos(cosI);
    				double senR = Math.sin(angoloI)*(coeffRifrazFromTmp/coeffRifrazNew);
    				double angoloR = Math.asin(senR);
    				    				
    				double XtR=XtRNorm+XtRTang*Math.tan(angoloR)*tRNormMod;
    				double YtR=YtRNorm+YtRTang*Math.tan(angoloR)*tRNormMod;
    				double ZtR=ZtRNorm+ZtRTang*Math.tan(angoloR)*tRNormMod;
    				
    				double cosT=-(XgR*XtR+YgR*YtR+ZgR*ZtR)/Math.sqrt(XtR*XtR+YtR*YtR+ZtR*ZtR);
    				colorRes.setCosC(cosT);
    				
    				if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
    				binaryTreeColor.setCurrentNode(treeNodeColor);
    				// Luce trasmessa dall'Oggetto trasparente
    				colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*(redObj/255.0)*(1-reflexCoef)); 
    				colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*(greenObj/255.0)*(1-reflexCoef));
    				colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*(blueObj/255.0)*(1-reflexCoef));
    				R_trasmissione(x1,y1,z1,xiR,yiR,ziR,xV,yV,zV,XgR,YgR,ZgR,XtR,YtR,ZtR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreflex,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R);
    			}
    			if(reflexCoef>0.0){
        			if(Utility.debugGrafico){
        				binaryTreeDebug.setCurrentNode(treeNode);
        			}
        			binaryTreeColor.setCurrentNode(treeNodeColor);
        			// Luce riflessa dall'Oggetto
            		colorRes_R.setLuceDirIncR(colorResInput.getLuceDirIncR()*reflexCoef); 
            		colorRes_R.setLuceDirIncG(colorResInput.getLuceDirIncG()*reflexCoef);
            		colorRes_R.setLuceDirIncB(colorResInput.getLuceDirIncB()*reflexCoef);
        			R_riflessione(x1,y1,z1,xiR,yiR,ziR,xV,yV,zV,XgR,YgR,ZgR,XrR,YrR,ZrR,i,j,Hi,Wi,colorArray,compImg,DT,distmin0,objIdnew,++numreflex,listObj,pointResObjOut,bombra,listColorRes,coeffRifrazNew,colorRes_R,false);
        		}
    		}
    	
    	return breal;
    }    
    
//*********************************************************************
//	MBA Test
//*********************************************************************
    private void MBATest(){

    	java.awt.Component compImg=getVisibileComponent();
    	int Hi=0,Wi=0;
    	Color img_color=null;
    	Color img_color_tmp= new Color(0,0,0);
    	double[][] arrayOutTmp=null;
    	double[][] arrayOutTmpR=null;
    	double[][] arrayOutTmpG=null;
    	double[][] arrayOutTmpB=null;
    	BufferedImage imagetmp = null;
    	int imageType=1;
    	
    	Integer[] options = {1,2,4,8,16,32,64,128};
        Integer n = (Integer)JOptionPane.showInputDialog(this, "Seleziona passo di campionamento:",
                "Passo di campionamento in pixel", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n==null)
        	return;
    	int sample=n;
    	
    	
    	//Integer[] optionsL = {1,2,4,8,16};
    	int lattice=0;
        String lat = JOptionPane.showInputDialog(this, "Seleziona larghezza reticolo:",
                "Larghezza reticolo", JOptionPane.QUESTION_MESSAGE);
        if(lat==null)
        	return;
        try{
        	lattice=Integer.parseInt(lat);
        }catch(NumberFormatException e){
        	JOptionPane.showMessageDialog(this, "Il valore inserito non è un numero: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        if(lattice<=0){
        	JOptionPane.showMessageDialog(this, "Il valore inserito è un numero minore di 0: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        
       	
    	Hi=compImg.getHeight();
    	Wi=compImg.getWidth();
    	Graphics graphicOrig = compImg.getGraphics();
    	// creo una nuova immagine
    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics g = imagetmp.getGraphics();
        g.setColor(compImg.getForeground());
        g.setFont(compImg.getFont());
        compImg.paintAll(g);
        
        int[][] imageOrigR = new int[Wi][Hi];
        int[][] imageOrigG = new int[Wi][Hi];
        int[][] imageOrigB = new int[Wi][Hi];
    	
        int channel=Utility.RED;
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				if(sample==1){
					img_color = new Color(imagetmp.getRGB(j,i));
					// caso di ricostruzione immagine danneggiata, si prende il nero come parte mancante da ricostruire
			    	if(img_color.getRed()==0 && img_color.getGreen()==0 && img_color.getBlue()==0){
			    		imageOrigR[j][i]=-1;
						imageOrigG[j][i]=-1;
						imageOrigB[j][i]=-1;
			    	}
			    	else{
			    		imageOrigR[j][i] = img_color.getRed();
						//else if(channel==Utility.GREEN)
						imageOrigG[j][i] = img_color.getGreen();
						//else if(channel==Utility.BLUE)
						imageOrigB[j][i] = img_color.getBlue();	
			    	}
			    	//imagetmp.setRGB(j, i, img_color_tmp.getRGB());
				}
				else{
					// campiono l'immagine originale nella nuova immagine com passo 'sample'
					if(i%sample==0 && j%sample==0){
							
			    		img_color = new Color(imagetmp.getRGB(j,i));
						//imageOrig[j][i]=img_color.getRGB();
						//if(channel==Utility.RED)
							imageOrigR[j][i] = img_color.getRed();
						//else if(channel==Utility.GREEN)
							imageOrigG[j][i] = img_color.getGreen();
						//else if(channel==Utility.BLUE)
							imageOrigB[j][i] = img_color.getBlue();					    		
					}
					else{
						imageOrigR[j][i]=-1;
						imageOrigG[j][i]=-1;
						imageOrigB[j][i]=-1;
						imagetmp.setRGB(j, i, img_color_tmp.getRGB());
					}
				}
				// determino se l'immagine è a colori
				if(imageType!=3 && imageOrigR[j][i]!=imageOrigG[j][i] || imageOrigR[j][i]!=imageOrigB[j][i]){
					imageType=3;
				}
    		}
    	}
    	addImage(imagetmp,"sampled_img_orig");
    	
    	long initime=0;
    	long fintime=0;
    	int fuctValR=0;
    	int fuctValG=0;
    	int fuctValB=0;
    	
    	double fuctValRDouble=0;
    	double fuctValGDouble=0;
    	double fuctValBDouble=0;
    	if(imageType==1){
	    	MBA mba = new MBA(imageOrigR,Hi,Wi,lattice);
	    	
	    	initime =  Calendar.getInstance().getTimeInMillis();
	    	
	    	Hashtable<Integer, double[][]> fuctArray = mba.calcoloFunctArray();
	    	fuctValR=0;
	    	fuctValRDouble=0;
	    	for(int stepk=0; stepk<fuctArray.size(); stepk++){
	    		arrayOutTmp=fuctArray.get(stepk);
	    		imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    		for(int i=0;(i<(Hi));i++){
	    			for(int j=0;j<(Wi);j++){
	    				fuctValR=(int)Math.round(arrayOutTmp[j][i]);
	    				if(fuctValR>255)
	    					fuctValR=255;
	    				if(fuctValR<0)
	    					fuctValR=0;
	    				img_color = new Color(fuctValR,fuctValR,fuctValR);
						imagetmp.setRGB(j, i, img_color.getRGB());
	        		}
	        	}
	    		addImage(imagetmp,"mba_img_out_" + stepk);
	    	}
	    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    	
			for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					fuctValRDouble=mba.calcolaFunctk(j,i);
					fuctValR = (int)Math.round(fuctValRDouble);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					img_color = new Color(fuctValR,fuctValR,fuctValR);
					imagetmp.setRGB(j, i, img_color.getRGB());
					arrayOutTmp[j][i]=fuctValRDouble;
				}
			}
	
	    	fintime = Calendar.getInstance().getTimeInMillis();
	    	JOptionPane.showMessageDialog(null, "Tempo elaborazione: " + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
	    	addImage(imagetmp,"mba_img_out_total");
    	}
    	else if(imageType==3){
    		//***********
    		// RED
    		//***********
    		MBA mbaR = new MBA(imageOrigR,Hi,Wi,lattice);
	    	initime =  Calendar.getInstance().getTimeInMillis();
	    	Hashtable<Integer, double[][]> fuctArrayR = mbaR.calcoloFunctArray();
	    	fuctValR=0;
	    	fuctValRDouble=0;
	    	//***********
    		// GREEN
    		//***********
	    	MBA mbaG = new MBA(imageOrigG,Hi,Wi,lattice);
	    	//initime =  Calendar.getInstance().getTimeInMillis();
	    	Hashtable<Integer, double[][]> fuctArrayG = mbaG.calcoloFunctArray();
	    	fuctValG=0;
	    	fuctValGDouble=0;
	    	//***********
    		// BLUE
    		//***********
	    	MBA mbaB = new MBA(imageOrigB,Hi,Wi,lattice);
	    	//initime =  Calendar.getInstance().getTimeInMillis();
	    	Hashtable<Integer, double[][]> fuctArrayB = mbaB.calcoloFunctArray();
	    	fuctValB=0;
	    	fuctValBDouble=0;
	    	for(int stepk=0; stepk<fuctArrayB.size(); stepk++){
	    		arrayOutTmpR=fuctArrayR.get(stepk);
	    		arrayOutTmpG=fuctArrayG.get(stepk);
	    		arrayOutTmpB=fuctArrayB.get(stepk);
	    		imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    		for(int i=0;(i<(Hi));i++){
	    			for(int j=0;j<(Wi);j++){
	    				fuctValR=(int)Math.round(arrayOutTmpR[j][i]);
	    				if(fuctValR>255)
	    					fuctValR=255;
	    				if(fuctValR<0)
	    					fuctValR=0;
	    				fuctValG=(int)Math.round(arrayOutTmpG[j][i]);
	    				if(fuctValG>255)
	    					fuctValG=255;
	    				if(fuctValG<0)
	    					fuctValG=0;
	    				fuctValB=(int)Math.round(arrayOutTmpB[j][i]);
	    				if(fuctValB>255)
	    					fuctValB=255;
	    				if(fuctValB<0)
	    					fuctValB=0;
	    				img_color = new Color(fuctValR,fuctValG,fuctValB);
						imagetmp.setRGB(j, i, img_color.getRGB());
	        		}
	        	}
	    		addImage(imagetmp,"mba_img_out_" + stepk);
	    	}	
	    	
	    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    	
			for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					fuctValRDouble=mbaR.calcolaFunctk(j,i);
					fuctValR = (int)Math.round(fuctValRDouble);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					fuctValGDouble=mbaG.calcolaFunctk(j,i);
					fuctValG = (int)Math.round(fuctValGDouble);
					if(fuctValG>255)
						fuctValG=255;
					if(fuctValG<0)
						fuctValG=0;
					fuctValBDouble=mbaB.calcolaFunctk(j,i);
					fuctValB = (int)Math.round(fuctValBDouble);
					if(fuctValB>255)
						fuctValB=255;
					if(fuctValB<0)
						fuctValB=0;
					img_color = new Color(fuctValR,fuctValG,fuctValB);
					imagetmp.setRGB(j, i, img_color.getRGB());
					//arrayOutTmp[j][i]=fuctValRDouble;
				}
			}
	
	    	fintime = Calendar.getInstance().getTimeInMillis();
	    	JOptionPane.showMessageDialog(null, "Tempo elaborazione: " + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
	    	addImage(imagetmp,"mba_img_out_total");
    		
    	}
    	
    	if(imageType==1){
	    	MBA_new mba_new = new MBA_new(imageOrigR,Hi,Wi,lattice);
	    	initime =  Calendar.getInstance().getTimeInMillis();
	    	double[][] arrayOutTmpGrey=mba_new.calcoloMBA();
	    	
	    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    	
	    	for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					fuctValR=(int)Math.round(arrayOutTmpGrey[j][i]);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					img_color = new Color(fuctValR,fuctValR,fuctValR);
					imagetmp.setRGB(j, i, img_color.getRGB());
	    		}
	    	}
	    	fintime = Calendar.getInstance().getTimeInMillis();
	    	JOptionPane.showMessageDialog(null, "Tempo elaborazione: " + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
	    	addImage(imagetmp,"mba_img_out_total_new");
	    	boolean bdiff=false;
	    	for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					if(Math.abs(arrayOutTmp[j][i]-arrayOutTmpGrey[j][i])>Utility.EPS){
						bdiff=true;
						break;
					}
	    		}
				if(bdiff)
					break;
	    	}
	    	if(bdiff)
	    		JOptionPane.showMessageDialog(null, "Immagini differenti" , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
	    	else
	    		JOptionPane.showMessageDialog(null, "Immagini uguali" , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
    	}
    	else if(imageType==3){
    		MBA_new mba_new = new MBA_new(imageOrigR,Hi,Wi,lattice);
	    	initime =  Calendar.getInstance().getTimeInMillis();
	    	arrayOutTmpR=mba_new.calcoloMBA();
	    	mba_new.initialize(imageOrigG,Hi,Wi,lattice);
	    	arrayOutTmpG=mba_new.calcoloMBA();
	    	mba_new.initialize(imageOrigB,Hi,Wi,lattice);
	    	arrayOutTmpB=mba_new.calcoloMBA();
	    	
	    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	    	
	    	for(int i=0;(i<(Hi));i++){
				for(int j=0;j<(Wi);j++){
					fuctValR=(int)Math.round(arrayOutTmpR[j][i]);
					if(fuctValR>255)
						fuctValR=255;
					if(fuctValR<0)
						fuctValR=0;
					fuctValG=(int)Math.round(arrayOutTmpG[j][i]);
					if(fuctValG>255)
						fuctValG=255;
					if(fuctValG<0)
						fuctValG=0;
					fuctValB=(int)Math.round(arrayOutTmpB[j][i]);
					if(fuctValB>255)
						fuctValB=255;
					if(fuctValB<0)
						fuctValB=0;
					img_color = new Color(fuctValR,fuctValG,fuctValB);
					imagetmp.setRGB(j, i, img_color.getRGB());
	    		}
	    	}
	    	fintime = Calendar.getInstance().getTimeInMillis();
	    	JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
	    	addImage(imagetmp,"mba_img_out_total_new");
    	}
    }
    
//*********************************************************************
//	MORPHING MBA ORIGINAL TEST
//*********************************************************************
    private void MorphMBATest(){

    	java.awt.Component compImg=getVisibileComponent();
    	int Hi=0,Wi=0;
    	Color img_color=null;
    	Color img_color_tmp= new Color(0,0,0);
    	double[][] arrayOutTmpR=null;
    	double[][] arrayOutTmpG=null;
    	double[][] arrayOutTmpB=null;
    	double[][] arrayOutTmpRX=null;
    	double[][] arrayOutTmpGX=null;
    	double[][] arrayOutTmpBX=null;
    	double[][] arrayOutTmpRY=null;
    	double[][] arrayOutTmpGY=null;
    	double[][] arrayOutTmpBY=null;
    	BufferedImage imagetmp = null;
    	BufferedImage imagetmp_pre = null;
    	BufferedImage imagetmpX = null;
    	BufferedImage imagetmpY = null;
    	int imageType=1;
    	
    	Integer[] options = {1,2,4,8,16,32,64,128};
        Integer n = (Integer)JOptionPane.showInputDialog(this, "Seleziona passo di campionamento:",
                "Passo di campionamento in pixel", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n==null)
        	return;
    	int sample=n;
    	
    	
    	//Integer[] optionsL = {1,2,4,8,16};
    	int lattice=0;
        String lat = JOptionPane.showInputDialog(this, "Seleziona larghezza reticolo:",
                "Larghezza reticolo", JOptionPane.QUESTION_MESSAGE);
        if(lat==null)
        	return;
        try{
        	lattice=Integer.parseInt(lat);
        }catch(NumberFormatException e){
        	JOptionPane.showMessageDialog(this, "Il valore inserito non è un numero: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        if(lattice<=0){
        	JOptionPane.showMessageDialog(this, "Il valore inserito è un numero minore di 0: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        
       	
    	Hi=compImg.getHeight();
    	Wi=compImg.getWidth();
    	Graphics graphicOrig = compImg.getGraphics();
    	// creo una nuova immagine
    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	//imagetmpX = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	//imagetmpY = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics g = imagetmp.getGraphics();
        g.setColor(compImg.getForeground());
        g.setFont(compImg.getFont());
        compImg.paintAll(g);
        
        int[][] imageOrigR = new int[Wi][Hi];
        int[][] imageOrigG = new int[Wi][Hi];
        int[][] imageOrigB = new int[Wi][Hi];
        
        int[][] imageOrigRX = new int[Wi][Hi];
        int[][] imageOrigGX = new int[Wi][Hi];
        int[][] imageOrigBX = new int[Wi][Hi];
        
        int[][] imageOrigRY = new int[Wi][Hi];
        int[][] imageOrigGY = new int[Wi][Hi];
        int[][] imageOrigBY = new int[Wi][Hi];
    	int sgn=1;
       for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imageOrigR[j][i]=-1;
				imageOrigG[j][i]=-1;
				imageOrigB[j][i]=-1;	
				
				imageOrigRX[j][i]=-1;
				imageOrigGX[j][i]=-1;
				imageOrigBX[j][i]=-1;
				
				imageOrigRY[j][i]=-1;
				imageOrigGY[j][i]=-1;
				imageOrigBY[j][i]=-1;
				
				// spostamento
				if(i==Hi/2 && j%30==0 && j>0 && j<Wi-1){
					imageOrigRY[j][i]=sgn*20;
					imageOrigGY[j][i]=sgn*20;
					imageOrigBY[j][i]=sgn*20;
					sgn=sgn*1;
				}
				// bordi a zero non si muovono
				if(i==0 || i==Hi-1 || j==0 || j==Wi-1){
					imageOrigRX[j][i]=0;
					imageOrigGX[j][i]=0;
					imageOrigBX[j][i]=0;
					imageOrigRY[j][i]=0;
					imageOrigGY[j][i]=0;
					imageOrigBY[j][i]=0;
				}
					
				// determino se l'immagine è a colori
				if(imageType!=3 && imageOrigRX[j][i]!=imageOrigGX[j][i] || imageOrigRX[j][i]!=imageOrigB[j][i]){
					imageType=3;
				}
    		}
    	}
    	//addImage(imagetmp,"sampled_img_orig");
    	
    	long initime=0;
    	long fintime=0;
    	int fuctValR=0;
    	int fuctValG=0;
    	int fuctValB=0;
    	
    	double fuctValRDouble=0;
    	double fuctValGDouble=0;
    	double fuctValBDouble=0; 	
    	
    	
		MBA_new mba_new = new MBA_new(imageOrigRX,Hi,Wi,lattice);
    	initime =  Calendar.getInstance().getTimeInMillis();
    	arrayOutTmpRX=mba_new.calcoloMBA();
    	mba_new.initialize(imageOrigRY,Hi,Wi,lattice);
    	arrayOutTmpRY=mba_new.calcoloMBA();
    	//mba_new.initialize(imageOrigB,Hi,Wi,lattice);
    	//arrayOutTmpB=mba_new.calcoloMBA();
    	
    	imagetmp_pre = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	img_color = new Color(0,0,0);
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imagetmp_pre.setRGB(j, i, img_color.getRGB());
					
			}
		}
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpRX[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				/*if(fuctValR<0)
					fuctValR=0;*///tolto perchè non venivano valori in detrazione
				fuctValG=(int)Math.round(arrayOutTmpRY[j][i]);
				if(fuctValG>255)
					fuctValG=255;
				/*if(fuctValG<0)
					fuctValG=0;*/ //tolto perchè non venivano valori in detrazione
				/*fuctValB=(int)Math.round(arrayOutTmpB[j][i]);
				if(fuctValB>255)
					fuctValB=255;
				if(fuctValB<0)
					fuctValB=0;*/
				imageOrigRX[j][i]=fuctValR;
				imageOrigGX[j][i]=fuctValR;
				imageOrigBX[j][i]=fuctValR;
				
				imageOrigRY[j][i]=fuctValG;
				imageOrigGY[j][i]=fuctValG;
				imageOrigBY[j][i]=fuctValG;
				
				if(fuctValG>10 || fuctValR>10){
					fuctValG = fuctValG;
				}
				
				img_color = new Color(imagetmp.getRGB(j,i));
				// inserire controllo nuovi indici dentro dimensioni immagine
				imageOrigR[j+fuctValR][i+fuctValG] = img_color.getRed();
				imageOrigG[j+fuctValR][i+fuctValG] = img_color.getGreen();
				imageOrigB[j+fuctValR][i+fuctValG] = img_color.getBlue();	
				//img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp_pre.setRGB(j+fuctValR, i+fuctValG, img_color.getRGB());
				
    		}
    	}
    	addImage(imagetmp_pre,"morph_mba_pre");
    	
    	//imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	MBA_new mba_new_morph = new MBA_new(imageOrigR,Hi,Wi,lattice);
    	
    	arrayOutTmpR=mba_new_morph.calcoloMBA();
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpR[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				if(fuctValR<0)
					fuctValR=0;
				img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp.setRGB(j, i, img_color.getRGB());
			}
    	}
    	
    	fintime = Calendar.getInstance().getTimeInMillis();
    	JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
    	addImage(imagetmp,"morph_mba_total");
    	
    }

  //*********************************************************************
//	MORPHING MBA DISPLACEMENT
//*********************************************************************
    private void MorphMBADispl(BufferedImage imagetmp, ArrayList<FourPointInt> displPointList){

    	//java.awt.Component compImg=getVisibileComponent();
    	int Hi=0,Wi=0;
    	Color img_color=null;
    	Color img_color_tmp= new Color(0,0,0);
    	double[][] arrayOutTmpR=null;
    	double[][] arrayOutTmpG=null;
    	double[][] arrayOutTmpB=null;
    	double[][] arrayOutTmpRX=null;
    	double[][] arrayOutTmpGX=null;
    	double[][] arrayOutTmpBX=null;
    	double[][] arrayOutTmpRY=null;
    	double[][] arrayOutTmpGY=null;
    	double[][] arrayOutTmpBY=null;
    	//BufferedImage imagetmp = null;
    	BufferedImage imagetmp_pre = null;
    	BufferedImage imagetmpX = null;
    	BufferedImage imagetmpY = null;
    	int imageType=1;
    	
    	/*Integer[] options = {1,2,4,8,16,32,64,128};
        Integer n = (Integer)JOptionPane.showInputDialog(this, "Seleziona passo di campionamento:",
                "Passo di campionamento in pixel", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n==null)
        	return;*/
    	int sample=1;
    	
    	
    	//Integer[] optionsL = {1,2,4,8,16};
    	int lattice=0;
        String lat = JOptionPane.showInputDialog(this, "Seleziona larghezza reticolo:",
                "Larghezza reticolo", JOptionPane.QUESTION_MESSAGE);
        if(lat==null)
        	return;
        try{
        	lattice=Integer.parseInt(lat);
        }catch(NumberFormatException e){
        	JOptionPane.showMessageDialog(this, "Il valore inserito non è un numero: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        if(lattice<=0){
        	JOptionPane.showMessageDialog(this, "Il valore inserito è un numero minore di 0: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        
       	
    	Hi=imagetmp.getHeight();
    	Wi=imagetmp.getWidth();
    	
        int[][] imageOrigR = new int[Wi][Hi];
        int[][] imageOrigG = new int[Wi][Hi];
        int[][] imageOrigB = new int[Wi][Hi];
        
        int[][] imageOrigRX = new int[Wi][Hi];
        int[][] imageOrigGX = new int[Wi][Hi];
        int[][] imageOrigBX = new int[Wi][Hi];
        
        int[][] imageOrigRY = new int[Wi][Hi];
        int[][] imageOrigGY = new int[Wi][Hi];
        int[][] imageOrigBY = new int[Wi][Hi];
        
        
    	int sgn=1;
       for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imageOrigR[j][i]=-1;
				imageOrigG[j][i]=-1;
				imageOrigB[j][i]=-1;	
				
				imageOrigRX[j][i]=-1;
				imageOrigGX[j][i]=-1;
				imageOrigBX[j][i]=-1;
				
				imageOrigRY[j][i]=-1;
				imageOrigGY[j][i]=-1;
				imageOrigBY[j][i]=-1;
				
				// spostamento
				/*if(i==Hi/2 && j%30==0 && j>0 && j<Wi-1){
					imageOrigRY[j][i]=sgn*20;
					imageOrigGY[j][i]=sgn*20;
					imageOrigBY[j][i]=sgn*20;
					sgn=sgn*1;
				}*/
				// bordi a zero non si muovono
				if(i==0 || i==Hi-1 || j==0 || j==Wi-1){
					imageOrigRX[j][i]=0;
					imageOrigGX[j][i]=0;
					imageOrigBX[j][i]=0;
					imageOrigRY[j][i]=0;
					imageOrigGY[j][i]=0;
					imageOrigBY[j][i]=0;
				}
				
					
				// determino se l'immagine è a colori
				if(imageType!=3 && imageOrigRX[j][i]!=imageOrigGX[j][i] || imageOrigRX[j][i]!=imageOrigB[j][i]){
					imageType=3;
				}
    		}
    	}
       	for(FourPointInt dispPoint: displPointList) {
       		imageOrigRX[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDx()-dispPoint.getX();
			imageOrigGX[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDx()-dispPoint.getX();
			imageOrigBX[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDx()-dispPoint.getX();
			
			imageOrigRY[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDy()-dispPoint.getY();
			imageOrigGY[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDy()-dispPoint.getY();
			imageOrigBY[dispPoint.getX()][dispPoint.getY()]=dispPoint.getDy()-dispPoint.getY();
       	}
    	//addImage(imagetmp,"sampled_img_orig");
    	
    	long initime=0;
    	long fintime=0;
    	int fuctValR=0;
    	int fuctValG=0;
    	int fuctValB=0;
    	
    	double fuctValRDouble=0;
    	double fuctValGDouble=0;
    	double fuctValBDouble=0; 	
    	
    	
		MBA_new mba_new = new MBA_new(imageOrigRX,Hi,Wi,lattice);
    	initime =  Calendar.getInstance().getTimeInMillis();
    	arrayOutTmpRX=mba_new.calcoloMBA();
    	mba_new.initialize(imageOrigRY,Hi,Wi,lattice);
    	arrayOutTmpRY=mba_new.calcoloMBA();
    	//mba_new.initialize(imageOrigB,Hi,Wi,lattice);
    	//arrayOutTmpB=mba_new.calcoloMBA();
    	
    	imagetmp_pre = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	img_color = new Color(0,0,0);
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imagetmp_pre.setRGB(j, i, img_color.getRGB());
					
			}
		}
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpRX[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				/*if(fuctValR<0)
					fuctValR=0;*///tolto perchè non venivano valori in detrazione
				fuctValG=(int)Math.round(arrayOutTmpRY[j][i]);
				if(fuctValG>255)
					fuctValG=255;
				/*if(fuctValG<0)
					fuctValG=0;*/ //tolto perchè non venivano valori in detrazione
				/*fuctValB=(int)Math.round(arrayOutTmpB[j][i]);
				if(fuctValB>255)
					fuctValB=255;
				if(fuctValB<0)
					fuctValB=0;*/
				imageOrigRX[j][i]=fuctValR;
				imageOrigGX[j][i]=fuctValR;
				imageOrigBX[j][i]=fuctValR;
				
				imageOrigRY[j][i]=fuctValG;
				imageOrigGY[j][i]=fuctValG;
				imageOrigBY[j][i]=fuctValG;
				
				if(fuctValG>10 || fuctValR>10){
					fuctValG = fuctValG;
				}
				
				img_color = new Color(imagetmp.getRGB(j,i));
				// inserire controllo nuovi indici dentro dimensioni immagine
				imageOrigR[j+fuctValR][i+fuctValG] = img_color.getRed();
				imageOrigG[j+fuctValR][i+fuctValG] = img_color.getGreen();
				imageOrigB[j+fuctValR][i+fuctValG] = img_color.getBlue();	
				//img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp_pre.setRGB(j+fuctValR, i+fuctValG, img_color.getRGB());
				
    		}
    	}
    	addImage(imagetmp_pre,"morph_mba_pre");
    	
    	//imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	MBA_new mba_new_morph = new MBA_new(imageOrigR,Hi,Wi,lattice);
    	
    	arrayOutTmpR=mba_new_morph.calcoloMBA();
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpR[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				if(fuctValR<0)
					fuctValR=0;
				img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp.setRGB(j, i, img_color.getRGB());
			}
    	}
    	
    	fintime = Calendar.getInstance().getTimeInMillis();
    	JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
    	addImage(imagetmp,"morph_mba_total");
    	
    }    
 
//*************************************
// loadCtrlPanel
//*************************************
    private void loadCtrlPanel(){
    	java.awt.Component compImg=getVisibileComponent();
    	int Hi=0,Wi=0;
    	BufferedImage imagetmp = null;
    	
    	Hi=compImg.getHeight();
    	Wi=compImg.getWidth();
    	Graphics graphicOrig = compImg.getGraphics();
    	// creo una nuova immagine
    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics g = imagetmp.getGraphics();
        g.setColor(compImg.getForeground());
        g.setFont(compImg.getFont());
        compImg.paintAll(g);
       
        JPanelControlPoint jpanelCP= new JPanelControlPoint(imagetmp,this);
        
 		for(java.awt.Component layer : layeredPane.getComponents()){
 			layer.setVisible(false);
 		}

         
        layeredPane.add(jpanelCP,layeredPane.getComponents().length);
        layeredPane.setPreferredSize(new Dimension(Wi, Hi));

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        jpanelCP.repaint();
        this.repaint();
        updateMenuOpenImg();
        
    }
    
    /*
     private void MorphMBATest(){

    	java.awt.Component compImg=getVisibileComponent();
    	int Hi=0,Wi=0;
    	Color img_color=null;
    	Color img_color_tmp= new Color(0,0,0);
    	double[][] arrayOutTmpR=null;
    	double[][] arrayOutTmpG=null;
    	double[][] arrayOutTmpB=null;
    	double[][] arrayOutTmpRX=null;
    	double[][] arrayOutTmpGX=null;
    	double[][] arrayOutTmpBX=null;
    	double[][] arrayOutTmpRY=null;
    	double[][] arrayOutTmpGY=null;
    	double[][] arrayOutTmpBY=null;
    	BufferedImage imagetmp = null;
    	BufferedImage imagetmp_pre = null;
    	BufferedImage imagetmpX = null;
    	BufferedImage imagetmpY = null;
    	int imageType=1;
    	
    	Integer[] options = {1,2,4,8,16,32,64,128};
        Integer n = (Integer)JOptionPane.showInputDialog(this, "Seleziona passo di campionamento:",
                "Passo di campionamento in pixel", JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        if(n==null)
        	return;
    	int sample=n;
    	
    	
    	//Integer[] optionsL = {1,2,4,8,16};
    	int lattice=0;
        String lat = JOptionPane.showInputDialog(this, "Seleziona larghezza reticolo:",
                "Larghezza reticolo", JOptionPane.QUESTION_MESSAGE);
        if(lat==null)
        	return;
        try{
        	lattice=Integer.parseInt(lat);
        }catch(NumberFormatException e){
        	JOptionPane.showMessageDialog(this, "Il valore inserito non è un numero: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        if(lattice<=0){
        	JOptionPane.showMessageDialog(this, "Il valore inserito è un numero minore di 0: " + lat , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
        	return;
        }
        
       	
    	Hi=compImg.getHeight();
    	Wi=compImg.getWidth();
    	Graphics graphicOrig = compImg.getGraphics();
    	// creo una nuova immagine
    	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	//imagetmpX = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	//imagetmpY = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics g = imagetmp.getGraphics();
        g.setColor(compImg.getForeground());
        g.setFont(compImg.getFont());
        compImg.paintAll(g);
        
        int[][] imageOrigR = new int[Wi][Hi];
        int[][] imageOrigG = new int[Wi][Hi];
        int[][] imageOrigB = new int[Wi][Hi];
        
        int[][] imageOrigRX = new int[Wi][Hi];
        int[][] imageOrigGX = new int[Wi][Hi];
        int[][] imageOrigBX = new int[Wi][Hi];
        
        int[][] imageOrigRY = new int[Wi][Hi];
        int[][] imageOrigGY = new int[Wi][Hi];
        int[][] imageOrigBY = new int[Wi][Hi];
    	int sgn=1;
       for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imageOrigR[j][i]=-1;
				imageOrigG[j][i]=-1;
				imageOrigB[j][i]=-1;	
				
				imageOrigRX[j][i]=-1;
				imageOrigGX[j][i]=-1;
				imageOrigBX[j][i]=-1;
				
				imageOrigRY[j][i]=-1;
				imageOrigGY[j][i]=-1;
				imageOrigBY[j][i]=-1;
				
				// spostamento
				if(i==Hi/2 && j%30==0 && j>0 && j<Wi-1){
					imageOrigRY[j][i]=sgn*20;
					imageOrigGY[j][i]=sgn*20;
					imageOrigBY[j][i]=sgn*20;
					sgn=sgn*1;
				}
				// bordi a zero non si muovono
				if(i==0 || i==Hi-1 || j==0 || j==Wi-1){
					imageOrigRX[j][i]=0;
					imageOrigGX[j][i]=0;
					imageOrigBX[j][i]=0;
					imageOrigRY[j][i]=0;
					imageOrigGY[j][i]=0;
					imageOrigBY[j][i]=0;
				}
					
				// determino se l'immagine è a colori
				if(imageType!=3 && imageOrigRX[j][i]!=imageOrigGX[j][i] || imageOrigRX[j][i]!=imageOrigB[j][i]){
					imageType=3;
				}
    		}
    	}
    	//addImage(imagetmp,"sampled_img_orig");
    	
    	long initime=0;
    	long fintime=0;
    	int fuctValR=0;
    	int fuctValG=0;
    	int fuctValB=0;
    	
    	double fuctValRDouble=0;
    	double fuctValGDouble=0;
    	double fuctValBDouble=0; 	
    	
    	
		MBA_new mba_new = new MBA_new(imageOrigRX,Hi,Wi,lattice);
    	initime =  Calendar.getInstance().getTimeInMillis();
    	arrayOutTmpRX=mba_new.calcoloMBA();
    	mba_new.initialize(imageOrigRY,Hi,Wi,lattice);
    	arrayOutTmpRY=mba_new.calcoloMBA();
    	//mba_new.initialize(imageOrigB,Hi,Wi,lattice);
    	//arrayOutTmpB=mba_new.calcoloMBA();
    	
    	imagetmp_pre = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	img_color = new Color(0,0,0);
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				
				imagetmp_pre.setRGB(j, i, img_color.getRGB());
					
			}
		}
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpRX[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				//if(fuctValR<0)
					//fuctValR=0;///tolto perchè non venivano valori in detrazione
				fuctValG=(int)Math.round(arrayOutTmpRY[j][i]);
				if(fuctValG>255)
					fuctValG=255;
				//if(fuctValG<0)
					//fuctValG=0; //tolto perchè non venivano valori in detrazione
				//fuctValB=(int)Math.round(arrayOutTmpB[j][i]);
				//if(fuctValB>255)
					//fuctValB=255;
				//if(fuctValB<0)
					//fuctValB=0;
				imageOrigRX[j][i]=fuctValR;
				imageOrigGX[j][i]=fuctValR;
				imageOrigBX[j][i]=fuctValR;
				
				imageOrigRY[j][i]=fuctValG;
				imageOrigGY[j][i]=fuctValG;
				imageOrigBY[j][i]=fuctValG;
				
				if(fuctValG>10 || fuctValR>10){
					fuctValG = fuctValG;
				}
				
				img_color = new Color(imagetmp.getRGB(j,i));
				// inserire controllo nuovi indici dentro dimensioni immagine
				imageOrigR[j+fuctValR][i+fuctValG] = img_color.getRed();
				imageOrigG[j+fuctValR][i+fuctValG] = img_color.getGreen();
				imageOrigB[j+fuctValR][i+fuctValG] = img_color.getBlue();	
				//img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp_pre.setRGB(j+fuctValR, i+fuctValG, img_color.getRGB());
				
    		}
    	}
    	addImage(imagetmp_pre,"morph_mba_pre");
    	
    	//imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
    	
    	MBA_new mba_new_morph = new MBA_new(imageOrigR,Hi,Wi,lattice);
    	
    	arrayOutTmpR=mba_new_morph.calcoloMBA();
    	
    	for(int i=0;(i<(Hi));i++){
			for(int j=0;j<(Wi);j++){
				fuctValR=(int)Math.round(arrayOutTmpR[j][i]);
				if(fuctValR>255)
					fuctValR=255;
				if(fuctValR<0)
					fuctValR=0;
				img_color = new Color(fuctValR,fuctValR,fuctValR);
				imagetmp.setRGB(j, i, img_color.getRGB());
			}
    	}
    	
    	fintime = Calendar.getInstance().getTimeInMillis();
    	JOptionPane.showMessageDialog(null, "" + (fintime-initime) , "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
    	addImage(imagetmp,"morph_mba_total");
    	
    }
     */
    
//******************************************************
// genera funzione sin(x)/x)
//******************************************************
public void generaSinct(){
	//prende il componente immagine attuale caricato
	java.awt.Component compImg=getVisibileComponent();
	int Hi=0,Wi=0;
	int Hic=0,Wic=0; // centro immagine
	int T=5,Per=0;//periodo sinct
	double dist=0;//distanza centro immagine
	double vsinct=1;//valore sinct
	double vsinctMax=1,vsinctMin=-0.3;//valore sinct max e min
	double rangeSinct=0;// range max min val sinct
	Color img_color=null;
	Color img_color_tmp= new Color(0,0,0);// creo sfondo nero
	double[][] arrayOutTmp=null;
	BufferedImage imagetmp = null;
	int imageType=1;// tipo di immagine 1 B/N, 3 RGB
   	
	Hi=compImg.getHeight();
	Wi=compImg.getWidth();
	Hic = Hi/2;
	Wic = Wi/2;
	Per=Math.max(Hi,Wi)/T;
	Graphics graphicOrig = compImg.getGraphics();
	// creo una nuova immagine
	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	
	Graphics g = imagetmp.getGraphics();
    g.setColor(compImg.getForeground());
    g.setFont(compImg.getFont());
    compImg.paintAll(g);
    
    int[][] imageOrigR = new int[Wi][Hi];
    int[][] imageOrigG = new int[Wi][Hi];
    int[][] imageOrigB = new int[Wi][Hi];
	
    int channel=Utility.RED;
	for(int i=0;(i<(Hi));i++){
		for(int j=0;j<(Wi);j++){
			
			dist=Math.sqrt(Math.pow((j-Wic),2)+Math.pow((i-Hic),2));
			if(dist>Utility.EPS2){
				vsinct=Math.sin(2*Math.PI*dist/Per)/(2*Math.PI*dist/Per);
				/*if(vsinct>vsinctMax)
					vsinctMax=vsinct;
				if(vsinct>vsinctMin)
					vsinctMin=vsinct;
					*/
			}
			/*if(j==Wic && i==Hic){
				vsinct=vsinct;
			}*/
				
			rangeSinct=Math.round(((vsinct-vsinctMin)/(vsinctMax-vsinctMin))*255);
		
			
			img_color = new Color((int)rangeSinct,(int)rangeSinct,(int)rangeSinct);	
			
			imagetmp.setRGB(j, i, img_color.getRGB());
			
		}
	}
	addImage(imagetmp,"sinct_image");
}


//******************************************************
//genera funzione reticolo
//******************************************************
public void generaReticolo(){
	//prende il componente immagine attuale caricato
	java.awt.Component compImg=getVisibileComponent();
	int Hi=0,Wi=0;
	int Hic=0,Wic=0; // centro immagine
	int T=5,Per=0;//periodo sinct
	double dist=0;//distanza centro immagine
	double vsinct=1;//valore sinct
	double vsinctMax=1,vsinctMin=-0.3;//valore sinct max e min
	double rangeSinct=0;// range max min val sinct
	Color img_color=null;
	Color img_color_tmp= new Color(0,0,0);// creo sfondo nero
	double[][] arrayOutTmp=null;
	BufferedImage imagetmp = null;
	int imageType=1;// tipo di immagine 1 B/N, 3 RGB
	int color=0;
	
	Hi=compImg.getHeight();
	Wi=compImg.getWidth();
	Hic = Hi/2;
	Wic = Wi/2;
	Per=Math.max(Hi,Wi)/T;
	Graphics graphicOrig = compImg.getGraphics();
	// creo una nuova immagine
	imagetmp = new BufferedImage((int)Wi, (int)Hi, BufferedImage.TYPE_INT_RGB);
	
	Graphics g = imagetmp.getGraphics();
 g.setColor(compImg.getForeground());
 g.setFont(compImg.getFont());
 compImg.paintAll(g);
 
 int[][] imageOrigR = new int[Wi][Hi];
 int[][] imageOrigG = new int[Wi][Hi];
 int[][] imageOrigB = new int[Wi][Hi];
	
 int channel=Utility.RED;
	for(int i=0;(i<(Hi));i++){
		for(int j=0;j<(Wi);j++){
			
			if(i%20==0 || j%20==0)
				color=0;
			else
				color=255;
			
			img_color = new Color(color,color,color);	
			
			imagetmp.setRGB(j, i, img_color.getRGB());
			
		}
	}
	addImage(imagetmp,"sinct_image");
}
	
//    private ObjectBase getObjFromId(LinkedHashMap<String, ObjectBase> listObj, int idObj) {
//    	for(ObjectBase objtmp : listObj.values()){
//    		if(objtmp.getIdObj().getId()==idObj)
//    			return objtmp;
//    	}
//		return null;
//	}
    
    public void mousePressed(MouseEvent e){
    	if(e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 2 && bdebug)
	    {
    	 
    	  JOptionPane.showMessageDialog(null, "j=" + e.getX() + "; i=" + e.getY(), "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
    	  jpos=e.getX();
    	  ipos=e.getY();
	    }	 
    }
    
	

    
    public static void main(String[] args) {
    	PaintImage me = new PaintImage();
    	//me.setLayout(new FlowLayout());
        me.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        me.setLocationRelativeTo(null);
        
        
        //me.setSize(600, 600);
        //me.setLocationRelativeTo(null);
        me.setVisible(true);
    }
    
    private void disegnaTraiettoria(TreeNode treeNodeTmp,double[] vetOutFrom,boolean bRight){
    	if(treeNodeTmp!=null){
			Vettore vetTmp;
			double[] vetOutTo=null;
			Color c=null;
			vetTmp=(Vettore)treeNodeTmp.getElement();
			vetOutTo=getScena().proiezioneInv(vetTmp.getX(), vetTmp.getY(), vetTmp.getZ());
			if(vetOutFrom!=null){
				//disegna retta sull'immagine
				int x1=(int)Math.round(vetOutFrom[0]);
				int y1=(int)Math.round(vetOutFrom[1]);
				int x2=(int)Math.round(vetOutTo[0]);
				int y2=(int)Math.round(vetOutTo[1]);
				Graphics graph = getImageRif().getGraphics();
				if(bRight)
					c = new Color(255,255,255);
				else
					c = new Color(255,0,0);
				graph.setColor(c);
				graph.drawLine(x1, y1, x2, y2);
			}
			if(treeNodeTmp.getRightChild()!=null){
				disegnaTraiettoria(treeNodeTmp.getRightChild(),vetOutTo,true);
			}
			if(treeNodeTmp.getLeftChild()!=null){
				disegnaTraiettoria(treeNodeTmp.getLeftChild(),vetOutTo,false);
			}
		}
    }
    
    private Scena3D getScena(){
    	return this.scena;
    }
    private void setScena(Scena3D scena){
    	this.scena=scena;
    }
	public BufferedImage getImageRif() {
		return imageRif;
	}
	public void setImageRif(BufferedImage imageRif) {
		this.imageRif = imageRif;
	}
	public void addColor2Tree(Object color,boolean bRoot,boolean bRight){
		TreeNode treeNodeColor = new TreeNode();
		binaryTreeColor.setCurrentNode(treeNodeColor);
		treeNodeColor.setElement(color);
		if(bRoot){
			binaryTreeColor.setRootNode(treeNodeColor);
		}
		else{
			
		}
	}
	private double[] calcolaLuceColore(TreeNode treeNodeTmp,LinkedHashMap<String, ObjectBase> listObj,boolean bRight){
		double[] resOut=null;
		if(treeNodeTmp!=null){
			ColorResult colRes;
			resOut=new double[4];
			double[] resArray=null;
			double coefReflex=0.0;
			double colObjR=0.0;
			double colObjG=0.0;
			double colObjB=0.0;
			double resR=0;
			double resG=0;
			double resB=0;
			ObjectBase objTmp=null;
			
			colRes=(ColorResult)treeNodeTmp.getElement();
			
			objTmp = Utility.getObjFromId(listObj,colRes.getIdObj().getId());
			if(objTmp!=null){
				coefReflex=objTmp.getReflexCoef();
				colObjR=objTmp.getRed();
				colObjG=objTmp.getGreen();
				colObjB=objTmp.getBlue();
			}
			
			
			if(treeNodeTmp.getRightChild()!=null){
				resArray=calcolaLuceColore(treeNodeTmp.getRightChild(),listObj,true);
				if(objTmp!=null){
					// controllo se sono in un nodo traparente => colRes.getTipoReflex()==Utility.RIFRAZIONE
					if(colRes.getTipoReflex()==Utility.RIFRAZIONE_TRASP){
						// controllo se sono in un nodo traparente e provengo da on oggetto Opaco, in tal caso la luce viene filtrata secondo i colori dell'oggetto trasparente
						double colFilterR=colObjR/255.0;
						double colFilterG=colObjG/255.0;
						double colFilterB=colObjB/255.0;
						double luceReflexR = resArray[0]*((1-coefReflex)*colFilterR);
						double luceReflexG = resArray[1]*((1-coefReflex)*colFilterG);
						double luceReflexB = resArray[2]*((1-coefReflex)*colFilterB);
						resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
						resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
						resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
					}
					else if((colRes.getTipoReflex()==Utility.RIFRAZIONE_OPACO)){
						// controllo se l'oggetto attuale sia trasparente e provengo da on oggetto Opaco in Rifrazione
						double luceReflexR = resArray[0]*(coefReflex);
						double luceReflexG = resArray[1]*(coefReflex);
						double luceReflexB = resArray[2]*(coefReflex);
						resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
						resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
						resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
					}
					else if(colRes.getTipoReflex()==Utility.REFLEX_OPACO){
						// controllo se l'oggetto attuale sia Opaco e provengo da un oggetto Opaco/Riflettente
						double luceReflexR = resArray[0]*(coefReflex+(colObjR/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
						double luceReflexG = resArray[1]*(coefReflex+(colObjG/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
						double luceReflexB = resArray[2]*(coefReflex+(colObjB/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
						resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
						resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
						resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
					}
					else{
						int i=0;
					}
				}
				else{
					resR = resArray[0];
					resG = resArray[1];
					resB = resArray[2];
				}
				
			}
			
			if(treeNodeTmp.getLeftChild()!=null){
				resArray=calcolaLuceColore(treeNodeTmp.getLeftChild(),listObj,false);
				double colFilterR=1.0;
				double colFilterG=1.0;
				double colFilterB=1.0;
				
				if((colRes.getTipoReflex()==Utility.RIFRAZIONE_OPACO)){
					// controllo se l'oggetto attuale sia trasparente e provengo dall'interno dell'oggetto trasparente in Rifrazione
					double luceReflexR = resArray[0]*(1-coefReflex);
					double luceReflexG = resArray[1]*(1-coefReflex);
					double luceReflexB = resArray[2]*(1-coefReflex);
					resR += (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG += (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB += (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.RIFRAZIONE_TRASP)){
					// controllo se l'oggetto attuale sia trasparente e provengo dall'interno dell'oggetto trasparente in Riflessione.
					double luceReflexR = resArray[0]*(coefReflex);
					double luceReflexG = resArray[1]*(coefReflex);
					double luceReflexB = resArray[2]*(coefReflex);
					resR += (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG += (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB += (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.REFLEX_TOT_TRASP)){
					// controllo se l'oggetto attuale sia trasparente e provengo da un oggetto traparente e avvenga la Riflessione Totale.
					double luceReflexR = resArray[0];
					double luceReflexG = resArray[1];
					double luceReflexB = resArray[2];
					resR += (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG += (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB += (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.REFLEX_OPACO)){
					// controllo se l'oggetto attuale sia Opaco/Riflettente e provengo da un oggetto traparente
					double luceReflexR = resArray[0]*(coefReflex+(colObjR/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					double luceReflexG = resArray[1]*(coefReflex+(colObjG/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					double luceReflexB = resArray[2]*(coefReflex+(colObjB/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					resR += (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG += (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB += (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else{
					int i=0;
				}
//				double luceReflexR = resArray[0]*((1-coefReflex)+(colObjR/255.0)*(1-coefReflex)*coefReflex*colRes.getCosB()*colRes.getCosT());
//				double luceReflexG = resArray[1]*((1-coefReflex)+(colObjG/255.0)*(1-coefReflex)*coefReflex*colRes.getCosB()*colRes.getCosT());
//				double luceReflexB = resArray[2]*((1-coefReflex)+(colObjB/255.0)*(1-coefReflex)*coefReflex*colRes.getCosB()*colRes.getCosT());
//				resR += (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
//				resG += (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
//				resB += (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
			}
//			
			if(treeNodeTmp.getRightChild()==null && treeNodeTmp.getLeftChild()==null){
				//quando finisco i nodi a Dx e Sx vuol dire che sono in fuga verso il cielo
				resR = 0.1;
				resG = 0.1;
				resB = 0.1;
//				double costmp = colRes.getCosT()>0?colRes.getCosT():colRes.getCosC();
//				double luceReflexR = resR*(coefReflex+(colObjR/255.0)*(1-coefReflex)*colRes.getCosB()*costmp);
//				double luceReflexG = resG*(coefReflex+(colObjG/255.0)*(1-coefReflex)*colRes.getCosB()*costmp);
//				double luceReflexB = resB*(coefReflex+(colObjB/255.0)*(1-coefReflex)*colRes.getCosB()*costmp);
//				resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
//				resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
//				resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
//				resOut[0]=resR;
//				resOut[1]=resG;
//				resOut[2]=resB;
				//return resOut;
				
				if(colRes.getTipoReflex()==Utility.RIFRAZIONE_TRASP){
					// controllo se sono in un nodo traparente e provengo dallo Spazio/Buio, in tal caso la luce viene filtrata secondo i colori dell'oggetto trasparente
					double colFilterR=colObjR/255.0;
					double colFilterG=colObjG/255.0;
					double colFilterB=colObjB/255.0;
					double luceReflexR = resR*((1-coefReflex)*colFilterR);
					double luceReflexG = resG*((1-coefReflex)*colFilterG);
					double luceReflexB = resB*((1-coefReflex)*colFilterB);
					resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.RIFRAZIONE_OPACO)){
					// controllo se l'oggetto attuale sia trasparente e ho raggiungo il numero massimo di riflessioni, per cui mi fermo senza percorsi a Dx e Sx.
					double luceReflexR = resR*(coefReflex);
					double luceReflexG = resG*(coefReflex);
					double luceReflexB = resB*(coefReflex);
					resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.REFLEX_OPACO)){
					// controllo se l'oggetto attuale sia Opaco/Riflettente e provengo da un oggetto traparente
					double luceReflexR = resR*(coefReflex+(colObjR/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					double luceReflexG = resG*(coefReflex+(colObjG/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					double luceReflexB = resB*(coefReflex+(colObjB/255.0)*(1-coefReflex)*colRes.getCosB()*colRes.getCosC());
					resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else if((colRes.getTipoReflex()==Utility.REFLEX_TOT_TRASP)){
					// controllo se l'oggetto attuale sia trasparente e provengo da un oggetto traparente e avvenga la Riflessione Totale.
					// Anche questo caso si raggiunge solo per esaurimento delle riflessioni.
					double luceReflexR = 0;
					double luceReflexG = 0;
					double luceReflexB = 0;
					resR = (colRes.isOmbra()?0.0:colRes.getLuceDirIncR()) + luceReflexR;
					resG = (colRes.isOmbra()?0.0:colRes.getLuceDirIncG()) + luceReflexG;
					resB = (colRes.isOmbra()?0.0:colRes.getLuceDirIncB()) + luceReflexB;
				}
				else{
					int i=0;
				}
			}
			
			resOut[0]=resR;
			resOut[1]=resG;
			resOut[2]=resB;
			resOut[3]=colRes.getIdObj().getId();
		}
		return resOut;
    }
	
	private double calcolaPropagazTot(TreeNode treeNodeTmp,LinkedHashMap<String, ObjectBase> listObj){
		ColorResult colRes;
		ObjectBase objTmp=null;
		double colObjR=0.0;
		double colObjG=0.0;
		double colObjB=0.0;
		double coefReflex=0;
		double coefRifrazlex=0;
		double result=1;
		TreeNode treeNodeCurr=treeNodeTmp;
		
		while(treeNodeCurr!=null){
			colRes=(ColorResult)treeNodeCurr.getElement();
			
			objTmp = Utility.getObjFromId(listObj,colRes.getIdObj().getId());
			if(objTmp!=null){
				coefReflex=objTmp.getReflexCoef();
				colObjR=objTmp.getRed();
				colObjG=objTmp.getGreen();
				colObjB=objTmp.getBlue();
				coefRifrazlex=objTmp.getRifrazCoef();
				if(coefRifrazlex>0)
					result = result * (1-coefReflex);
				else
					result = result * (coefReflex);
				if((colRes.getTipoReflex()==Utility.REFLEX_OPACO)){
					result = result * (coefReflex);
				}
				else if((colRes.getTipoReflex()==Utility.RIFRAZIONE_OPACO)){
					result = result * 1;
				}
			}
			else
				result=0;
			
			treeNodeCurr=treeNodeCurr.getParent();
		}
			
		return result;
	}
	
	public void fillImage(ColorArray colorArray,BufferedImage imagetmp,int[][] arrayImgOut){
		int fuctValR=0;
		int fuctValG=0;
		int fuctValB=0;
		Color img_color;
		int Hi=imagetmp.getHeight();
		int Wi=imagetmp.getWidth();
		for(int i1=0;i1<Hi;i1++){
			for(int j1=0;j1<Wi;j1++){
				fuctValR=(int)Math.round(colorArray.getArrayImgOutR()[j1][i1]);
				if(fuctValR>255)
					fuctValR=255;
				if(fuctValR<0)
					fuctValR=0;
				fuctValG=(int)Math.round(colorArray.getArrayImgOutG()[j1][i1]);
				if(fuctValG>255)
					fuctValG=255;
				if(fuctValG<0)
					fuctValG=0;
				fuctValB=(int)Math.round(colorArray.getArrayImgOutB()[j1][i1]);
				if(fuctValB>255)
					fuctValB=255;
				if(fuctValB<0)
					fuctValB=0;
				img_color = new Color(fuctValR,fuctValG,fuctValB);
				imagetmp.setRGB(j1, i1, img_color.getRGB());
				arrayImgOut[j1][i1]=img_color.getRGB();
			}
		}
	}
	
	public void imageDiff(){
		int itemMenu = this.getJMenuBar().getMenu(1).getItemCount();
    	int count = 0;
    	int CountComp=0;
    	int Hi=0,Wi=0;
    	BufferedImage imagetmp;
    	Graphics g;
    	ArrayList<BufferedImage> imgList = new ArrayList<BufferedImage>(); 
    	
    	for(int i=4;i<itemMenu;i++){
    		JMenuItem menuItem = this.getJMenuBar().getMenu(1).getItem(i);
    		if(menuItem instanceof JCheckBoxMenuItem){
    			count=0;
    			if(((JCheckBoxMenuItem)menuItem).isSelected()){
    				//for (java.awt.Component comp : this.getContentPane().getComponents()){
    				for (java.awt.Component layer : this.layeredPane.getComponents()){
    		        	if(count==i-4){
    		        		layer.getName();
    		        		layer.setVisible(true);
    		        		Wi=layer.getWidth();
    		        		Hi=layer.getHeight();
    		        		imagetmp = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_RGB);
    		        		g = imagetmp.getGraphics();
    		        		g.setColor(layer.getForeground());
    		                g.setFont(layer.getFont());
    		                layer.paintAll(g);
    		        		imgList.add((imagetmp.getSubimage(0, 0, Wi, Hi)));
    		        		layer.setVisible(false);
    		    		}
    		    		else{
    		    			
    		    		}
    		        	count++;
    		        }   
    			}
    		}
    	}
//    	for (BufferedImage imageItem : imgList){
//    		addImage(imageItem,"diff" + count);
//    	}
    	int imgVal;
    	double red=0,gre=0,blu=0;
    	double red1=0,gre1=0,blu1=0;
    	double red2=0,gre2=0,blu2=0;
    	double red1t=0,gre1t=0,blu1t=0;
    	double red2t=0,gre2t=0,blu2t=0;
    	double red1Max=0,gre1Max=0,blu1Max=0;
    	double[][] arrayImgOutR = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutG = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutB = new double[(int)Wi][(int)Hi];
    	int[][] arrayImgOut = new int[(int)Wi][(int)Hi];
    	Color imgCol;
    	for(int i=0;i<Hi;i++){
    		for(int j=0;j<Wi;j++){
    			red=gre=blu=0;
    			for (BufferedImage imageItem : imgList){
    				imgVal = imageItem.getRGB(j, i);
    				imgCol = new Color(imgVal);
    				red = red + imgCol.getRed();    				
    				gre = gre + imgCol.getGreen();
    				blu = blu + imgCol.getBlue();
    			}
    			red = red/2;    				
				gre = gre/2;
				blu = blu/2;
				count=0;
				for (BufferedImage imageItem : imgList){
    				if(count==0){
						imgVal = imageItem.getRGB(j, i);
	    				imgCol = new Color(imgVal);
	    				if(imgCol.getRed()>0){
	    					red1 = Math.abs(red - imgCol.getRed())*100/imgCol.getRed();
	    					if(red1Max<red1)
	    						red1Max=red1;
	    					red1t = red1t + red1*red1;
	    					arrayImgOutR[j][i]=red1;
	    				}
	    				if(imgCol.getGreen()>0){
	    					gre1 = Math.abs(gre - imgCol.getGreen())*100/imgCol.getGreen();
	    					if(gre1Max<gre1)
	    						gre1Max=red1;
	    					gre1t = gre1t + gre1*gre1;
	    					arrayImgOutG[j][i]=gre1;
	    				}
	    				if(imgCol.getBlue()>0){
	    					blu1 = Math.abs(blu - imgCol.getBlue())*100/imgCol.getBlue();
	    					if(blu1Max<gre1)
	    						blu1Max=red1;
	    					blu1t = blu1t + blu1*blu1;
	    					arrayImgOutB[j][i]=blu1;
	    				}
    				}
//    				else if(count==1){
//						imgVal = imageItem.getRGB(j, i);
//	    				imgCol = new Color(imgVal);
//	    				red2 = Math.abs(red - imgCol.getRed())/imgCol.getRed();      				
//	    				gre2 = Math.abs(gre - imgCol.getGreen())/imgCol.getGreen();
//	    				blu2 = Math.abs(blu - imgCol.getBlue())/imgCol.getBlue();
//	    				red2t = red2t + red2*red2;    				
//	    				gre2t = gre2t + gre2*gre2;
//	    				blu2t = blu2t + blu2*blu2;
//    				}
    				count++;
    			}
    		}
    	}
//    	red2t = red2t / (Hi*Wi);    				
//		gre2t = gre2t / (Hi*Wi);
//		blu2t = blu2t / (Hi*Wi);
		
		red1t = Math.sqrt(red1t / (Hi*Wi));    				
		gre1t = Math.sqrt(gre1t / (Hi*Wi));
		blu1t = Math.sqrt(blu1t / (Hi*Wi));
		
		JOptionPane.showMessageDialog(null, "Valore Max.\nred Max = % " + red1Max + "\ngre Max = % " + gre1Max + "\nblu Max = % " + blu1Max + "\n\nDeviazione Standard\nred = % " + red1t + "\ngre = % " + gre1t + "\nblu = % " + blu1t , "Statistiche Differenza Immagini", JOptionPane.INFORMATION_MESSAGE);
		
		BufferedImage imagetmp2 = new BufferedImage(Wi, Hi, BufferedImage.TYPE_INT_RGB);
		
		ColorArray colorArray = new ColorArray();
		colorArray.setArrayImgOutR(arrayImgOutR);
		colorArray.setArrayImgOutG(arrayImgOutG);
		colorArray.setArrayImgOutB(arrayImgOutB);
		
		fillImage(colorArray,imagetmp2,arrayImgOut);
	    addImage(imagetmp2,"differenza_img");
	}
	
	private void snapShotObj(ObjectBase objRif,int Wi,int Hi,double distmin0){
		double[] rout=null;
		double[][] arrayImgOutR = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutG = new double[(int)Wi][(int)Hi];
    	double[][] arrayImgOutB = new double[(int)Wi][(int)Hi];
    	int[][] arrayImgOut = new int[(int)Wi][(int)Hi];
		ColorArray colorArray = new ColorArray();
		colorArray.setArrayImgOutR(arrayImgOutR);
		colorArray.setArrayImgOutG(arrayImgOutG);
		colorArray.setArrayImgOutB(arrayImgOutB);
		
		double Xr=0;
		double Yr=0;
		double Zr=0;
		double dist=0;
		double xiR,yiR,ziR;
		double XrR,YrR,ZrR;
		double XgR,YgR,ZgR;
		PointResObj pointResObj = new PointResObj();
		boolean btmp=false;
		boolean breal=false;
		double xx1 = 0;
		double yy1 = 0;
		double zz1 = 0;
		double[] vout;
		double xx0 = scena.getX0();
		double yy0 = scena.getY0();
		double zz0 = scena.getZ0();
		double modulus=0;
		double distmin = distmin0;
		int zoomL=1;
		
		for(int i=0;(i<(Hi));i++){
			for(int ii=0;(ii<(zoomL));ii++){
				for(int j=0;j<(Wi);j++){
    				for(int jj=0;(jj<(zoomL));jj++){
    					breal = false;
//    					double new_i=i+((ii)/zoomL)-((zoomL-1)/2*zoomL);
//    					double new_j=j+((jj)/zoomL)-((zoomL-1)/2*zoomL);
    					double new_i=i+((ii)/zoomL);
    					double new_j=j+((jj)/zoomL);

    					vout=scena.img2fix(new_j, new_i, 0);
						xx1 = vout[0];
						yy1 = vout[1];
						zz1 = vout[2];
						
						Xr=xx1-xx0;
						Yr=yy1-yy0;
						Zr=zz1-zz0;
						
			    		modulus = Math.sqrt(Xr*Xr+Yr*Yr+Zr*Zr);
					
						btmp=false;
						pointResObj.setXi(0);
						pointResObj.setYi(0);
						pointResObj.setZi(0);
						pointResObj.setXr(0);
						pointResObj.setYr(0);
						pointResObj.setZr(0);
						pointResObj.setXg(0);
						pointResObj.setYg(0);
						pointResObj.setZg(0);
						btmp = objRif.detect(xx1+Xr/modulus,yy1+Yr/modulus,zz1+Zr/modulus,xx1,yy1,zz1,Xr,Yr,Zr,distmin0,pointResObj,new IdObj(0),Utility.REFLEX);
						if(btmp){
							double xi1tmp=pointResObj.getXi();
							double yi1tmp=pointResObj.getYi();
							double zi1tmp=pointResObj.getZi();
					    	dist = Math.sqrt(Math.pow(xi1tmp-xx1,2.0)+Math.pow(yi1tmp-yy1,2.0)+Math.pow(zi1tmp-zz1,2.0));
							if(dist<distmin){
								xiR=pointResObj.getXi();
		    			    	yiR=pointResObj.getYi();
		    			    	ziR=pointResObj.getZi();
		    			    	XrR=pointResObj.getXr();
		    			    	YrR=pointResObj.getYr();
		    			    	ZrR=pointResObj.getZr();
		    			    	XgR=pointResObj.getXg();
		    			    	YgR=pointResObj.getYg();
		    			    	ZgR=pointResObj.getZg();
		    			    	breal = true;
		    			    	
		    			    	rout=objRif.getObjMapping(xiR, yiR, ziR);
		    			    	arrayImgOutR[j][i]=rout[0]/(zoomL*zoomL)+arrayImgOutR[j][i];
		    			    	arrayImgOutG[j][i]=rout[1]/(zoomL*zoomL)+arrayImgOutG[j][i];
		    			    	arrayImgOutB[j][i]=rout[2]/(zoomL*zoomL)+arrayImgOutB[j][i];
							}
						}
    				}
				}
			}
		}
		
		BufferedImage imagetmp2 = new BufferedImage(Wi, Hi, BufferedImage.TYPE_INT_RGB);
		fillImage(colorArray,imagetmp2,arrayImgOut);
	    addImage(imagetmp2,"snapShotObj_" + objRif.getIdObj().getId() + "_" + zoomL);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		/*if(frameMBA!=null && frameMBA.isVisible()){
			frameMBA.setpos(arg0.getX(),arg0.getY());
		}*/
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 2) {
			popupMenu.show(e.getComponent(),e.getX(), e.getY());
			//popupMenu.setVisible(true);
		}
		
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	
	class MenuItemListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {            
	         //statusLabel.setText(e.getActionCommand() + " MenuItem clicked.");
	    	  System.out.println(e.getActionCommand() + " MenuItem clicked.");
	    	  if(e.getActionCommand().equals("Punti")) {
	    		  if(getVisibileComponent() instanceof JPanelControlPoint) {
	    			  JPanelControlPoint jpcp = (JPanelControlPoint)getVisibileComponent();
	    			  ((JMenuItem)popupMenu.getSubElements()[0]).setEnabled(false);
	    			  jpcp.setbPhase1(false);
	    			  ((JMenuItem)popupMenu.getSubElements()[1]).setEnabled(true);
	    		  }
	    	  }
	    	  else if(e.getActionCommand().equals("Morphing")) {
	    		  JPanelControlPoint jpcp = (JPanelControlPoint)getVisibileComponent();
	    		  jpcp.getDisplacement();
	    	  }
	      }    
	   }   
	
	/*class PopClickListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    private void doPop(MouseEvent e) {
	        //PopUpDemo menu = new PopUpDemo();
	        popupMenu.show(e.getComponent(), e.getX(), e.getY());
	    }
	}*/
}