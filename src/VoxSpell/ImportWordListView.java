package VoxSpell;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImportWordListView implements Card {

	@Override
	public JPanel createAndGetPanel() {

		_labelTestRecorded= new JLabel("Good Effort, this test is recorded (View Test Score)");
		_labelTestRecorded.setFont(new Font("SansSerif", Font.ITALIC,15));;
		
		_labelVideoReward= new JLabel("You can play your video reward now!");
		_labelVideoReward.setFont(new Font("SansSerif", Font.ITALIC,15));;
		
		_labelKeepGoing= new JLabel("Continue test to update your personal score!");
		_labelKeepGoing.setFont(new Font("SansSerif", Font.ITALIC,15));;
		return null;
	}

}
