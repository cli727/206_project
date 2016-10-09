package VoxSpell;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChooseLevelReviewView extends ChooseLevelView {


	public ChooseLevelReviewView(String courseName) {
		
		super(courseName);
		//redefine some fields
		_labelHeading.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ "Set up your review quiz...</font></html>"));

	}
	
	//review mode will need to set the coursePath differently
	@Override
	public void setModel(ChooseLevelModel model){
		_model = model;
		_model.setCoursePath("./.review/"+_courseName+"Review");
	}

}
