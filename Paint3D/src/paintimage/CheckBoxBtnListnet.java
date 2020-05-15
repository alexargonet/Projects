package paintimage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import utility.Utility;


public class CheckBoxBtnListnet implements ActionListener{

	private PaintImage paintimg;
	public CheckBoxBtnListnet(PaintImage paintImage) {
		// TODO Auto-generated constructor stub
		paintimg = paintImage;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//JOptionPane.showMessageDialog(null, ((JRadioButtonMenuItem)(arg0.getSource())).getText(), "InfoBox: PaintImage", JOptionPane.INFORMATION_MESSAGE);
		String menuDescr = ((JCheckBoxMenuItem)(arg0.getSource())).getText();
		menuDescr = menuDescr.substring(0, menuDescr.indexOf(" -"));
		if(Utility.isNumeric(menuDescr)){
			paintimg.setVisibleImage(Integer.parseInt(menuDescr));
		}
	}

}
