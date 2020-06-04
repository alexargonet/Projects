package utility;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import paintimage.PaintImage;
	
public class ImageU {
	private String name;
	private BufferedImage imgBuf;
	
	public ImageU(JFrame parent) {
		File file=null;
		//BufferedImage image = null;

		JFileChooser fc = new JFileChooser();
            
    	int returnVal = fc.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) { 
            file = fc.getSelectedFile();
            System.out.println("Opening: " + file.getName() + ".\n");
            name = file.getName();
        }
        
        if(file == null)
        	return;
    	try
        {
    		imgBuf = ImageIO.read(file);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.exit(1);
        } 	
        //return image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BufferedImage getImgBuf() {
		return imgBuf;
	}
	public void setIngBuf(BufferedImage imgBuf) {
		this.imgBuf = imgBuf;
	}
}
