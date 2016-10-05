package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import VoxSpell.VoxSpellGui.LEVEL;

public class ChooseLevelView implements Card, ActionListener{

	private JLabel _labelHeading;
	private JButton _btnViewWordList;
	private JLabel _labelChooseLevel;
	private DefaultComboBoxModel<Integer> _model;
	private JComboBox<Integer> _comboBox;
	private JLabel _labelChooseNumWords;
	private JRadioButton _btnTenWords;
	private JRadioButton _btnTwentyWords;
	private JRadioButton _btnFortyWords;
	private JRadioButton _btnFiftyWords;
	
	private JButton _btnStartQuiz;
	private JButton _btnBackToMain;
	
	private String _courseName;
	private int _numWordsToQuiz;

	public ChooseLevelView (String courseName){
		//initialise fields
		_courseName = courseName;
		
		_labelHeading = new JLabel("VIEW AND CHOOSE YOUR GOAL FOR " + _courseName);
		_btnViewWordList = new JButton("VIEW WORDS IN THIS COURSE");
		
		_labelChooseLevel = new JLabel("<html> <p style='text-align: center;font-size:11px;padding:2;'>"+
				"<font color='white'>"
				+ " Choose Level Below!</font></html>");
		
		_model = new DefaultComboBoxModel<Integer>();
		_comboBox = new JComboBox<Integer>(_model);
		
		_labelChooseNumWords = new JLabel("How many words you would like to be tested on:");
		_btnTenWords = new JRadioButton("10 Random Words");
        _btnTenWords.setSelected(true);
        
        _btnTwentyWords = new JRadioButton("20 Random Words");
        _btnFortyWords = new JRadioButton("40 Random Words");
        _btnFiftyWords = new JRadioButton("50 Random Words");
        
        //only allow one radio button selection at a time
        ButtonGroup group = new ButtonGroup();
        group.add(_btnTenWords);
        group.add(_btnTwentyWords);
        group.add(_btnFortyWords);
        group.add(_btnFiftyWords);
        
        _btnStartQuiz = new JButton("Start!");
		
		_btnBackToMain = new JButton("Back");
	}

	@Override
	public JPanel createAndGetPanel() {
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(new Color(6,149,255));
		
		//add all ENUM elements to drop down menu for combo box
		for (LEVEL i : LEVEL.values()){
			_model.addElement(i.getLevel());
		}


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
		c.insets = new Insets(20,10,20,10);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,10);
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
		c.insets = new Insets(0,10,20,10);
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
		c.insets = new Insets(0,10,20,10);
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
		c.insets = new Insets(0,10,20,10);
		mainPanel.add(_labelChooseNumWords, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,10,10);
		mainPanel.add(_btnTenWords, c);
		_btnTenWords.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,10,10);
		mainPanel.add(_btnTwentyWords, c);
		_btnTwentyWords.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,10,10);
		mainPanel.add(_btnFortyWords, c);
		_btnFortyWords.addActionListener(this);
		 
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,10);
		mainPanel.add(_btnFiftyWords, c);
		_btnFiftyWords.addActionListener(this);
		

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,10);
		mainPanel.add(_btnStartQuiz, c);
		_btnStartQuiz.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 10;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,10);
		mainPanel.add(_btnBackToMain, c);
		_btnBackToMain.addActionListener(this);
		 
		

		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showCourseChooser();
			
		}else if (e.getSource() == _btnTenWords){
			
			_numWordsToQuiz = 10;
		}else if (e.getSource() == _btnTwentyWords){
			
			_numWordsToQuiz = 20;
		}else if (e.getSource() == _btnFortyWords){
			
			_numWordsToQuiz = 40;
		}else if (e.getSource() == _btnFiftyWords){
			
			_numWordsToQuiz = 50;
		}else if (e.getSource() == _btnStartQuiz){
			
			Object level =  _comboBox.getSelectedItem();
			System.out.println("level "+level);
			System.out.println("words " + _numWordsToQuiz);
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
