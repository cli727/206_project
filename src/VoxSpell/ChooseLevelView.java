package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import VoxSpell.VoxSpellGui.LEVEL;

public class ChooseLevelView implements Card, ActionListener{

	private JLabel _labelHeading;
	private JButton _btnViewWordList;
	private JLabel _labelChooseLevel;
	private DefaultComboBoxModel<Integer> _model;
	private JComboBox<Integer> _comboBox;
	private JButton _btnBackToMain;
	private String _courseName;

	public ChooseLevelView (String courseName){
		//initialise fields
		_courseName = courseName;
		_labelHeading = new JLabel("VIEW AND CHOOSE YOUR GOAL FOR " + _courseName);
		_btnViewWordList = new JButton("VIEW WORDS IN THIS COURSE");
		_labelChooseLevel = new JLabel("Please select the level");
		_model = new DefaultComboBoxModel<Integer>();
		_comboBox = new JComboBox<Integer>(_model);
		_btnBackToMain = new JButton("Back");
	}

	@Override
	public JPanel createAndGetPanel() {
		JPanel mainPanel = new JPanel();

		//add all ENUM elements to drop down menu for combo box
		for (LEVEL i : LEVEL.values()){
			_model.addElement(i.getLevel());
		}




		int level;

		/*switch (result) {
		case JOptionPane.OK_OPTION:
			for (LEVEL i : LEVEL.values()){

				//compare value of selected item to int values of LEVEL items
				if (_comboBox.getSelectedItem().equals(i.getLevel())){
					level = i.getLevel();
				}
			}
		}*/

		/**
		 * DECLARATION: THE FOLLOWING METHOD ON JAVA GRIDBAG LAYOUT ARE SOURCED 
		 * AND EDITED FROM THE ORACLE TUTORIAL WEBPAGE
		 * URL: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
		 */
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_btnViewWordList, c);
		_btnViewWordList.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_labelChooseLevel, c);
		//	_btnViewWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_comboBox, c);
		//	_btnViewWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_btnBackToMain, c);
		_btnBackToMain.addActionListener(this);

		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showCourseChooser();
		}

	}

	/**
	 * Shows a pop up telling user that the selected game/level has no possible quiz words
	 * @param quizMode
	 * @param allEmpty
	 * @param level
	 */
	private void showNoAvailableWordsPopUp(String quizMode, boolean allEmpty, int level){

		String message;
		if(allEmpty){
			if (quizMode.equals(REVIEW)){

				message = "There is no word to review at all levels!\n"+
						"What about starting a New Quiz :)";
			}else {
				message = "No wordlist found. Please ensure that the \n"+
						"'wordlist' file is in the working directory!";
			}

		}else{
			//level empty
			message = "There is no word to review for Level " + level + " !\n" +
					"Please select another level to review.";
		}

		JOptionPane.showMessageDialog(_frame, message, 
				"No Available Words", JOptionPane.INFORMATION_MESSAGE);

		showMainMenu();
	}

	/** shows the pop up window that allows user to select window
	 ** returns an integer that represents the level user has selected
	 ** 0 means the user has chosen to cancel
	 *
	 * Reference URL : http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html#input
	 */
	private int createAndShowLevelPopUp(String quizMode){

		JPanel popUpPanel = new JPanel();
		JPanel cbPanel = new JPanel();

		popUpPanel.setLayout(new BorderLayout());
		cbPanel.setLayout(new BorderLayout());

		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>();

		//add all ENUM elements to drop down menu for combo box
		for (LEVEL i : LEVEL.values()){
			model.addElement(i.getLevel());
		}

		JComboBox<Integer> comboBox = new JComboBox<Integer>(model);

		cbPanel.add(new JLabel("Start your " + quizMode +" quiz at level :     "),BorderLayout.BEFORE_LINE_BEGINS);
		cbPanel.add(comboBox,BorderLayout.CENTER);

		//add comboBox panel to popup window
		popUpPanel.add(cbPanel,BorderLayout.AFTER_LAST_LINE);

		int result = JOptionPane.showConfirmDialog(VoxSpellGui.getFrame(), popUpPanel, "Choose level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		switch (result) {
		case JOptionPane.OK_OPTION:
			for (LEVEL i : LEVEL.values()){

				//compare value of selected item to int values of LEVEL items
				if (comboBox.getSelectedItem().equals(i.getLevel())){
					return i.getLevel();
				}
			}
		}
		return 0;
	}
}
