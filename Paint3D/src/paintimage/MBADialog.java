package paintimage;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MBADialog extends JFrame implements MouseListener {
	JLabel jLabel = null;
	JPanel topPanel = null;
	

	public MBADialog() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
		setTitle("MBA Dialog");
        setSize(500, 500);
		jLabel = new JLabel();
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(jLabel);
		topPanel.setBounds(0, 0, 500, 500);
		topPanel.setLocation(0, 0);
		getContentPane().add(topPanel);
		//pack();
	    //setLocationRelativeTo(null);
	    //setVisible(true);
	    //repaint();
		this.addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e){
	        	dispose();
	        }
	    });
	
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setpos(int x, int y) {
		// TODO Auto-generated method stub
		jLabel.setText("x="+x+" y="+y);
	}

}
