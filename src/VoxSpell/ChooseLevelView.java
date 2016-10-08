package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChooseLevelView implements Card, ActionListener{

	private JPanel _chooseLevelPanel;

	private JLabel _labelHeading;
	private JLabel _labelViewChangeCourse;
	private JButton _btnViewWordList;
	private JButton _btnChangeCourse;
	private JLabel _labelChooseLevel;

	//private ComboBoxModel<String> _model;
	private JComboBox<String> _comboBox;

	private JLabel _labelChooseNumWords;
	private JRadioButton _btnAllWords;
	private JRadioButton _btnTenWords;
	private JRadioButton _btnTwentyWords;
	private JRadioButton _btnFortyWords;
	private JRadioButton _btnFiftyWords;

	private JButton _btnStartQuiz;
	private JButton _btnBackToPrevious;
	private JButton _btnBackToMain;

	private String _courseName;

	private int _numWordsToQuiz;

	private ChooseLevelModel _model;

	private boolean _quizAllWords ;

	public ChooseLevelView (String courseName){
		//initialise fields

		_quizAllWords = false;
		_numWordsToQuiz = 10; //if user doesnt change the setting, the wordsToQuiz is 10
		_courseName = courseName;

		Font headingFont = new Font("SansSerif", Font.ITALIC,50);

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ "Set up your practice quiz...</font></html>"));

		_labelHeading.setFont(headingFont);

		_labelViewChangeCourse = new JLabel("VIEW AND CHOOSE YOUR GOAL FOR " + _courseName);
		_btnViewWordList = new JButton("VIEW WORDS");
		_btnChangeCourse = new JButton("Change Course");

		_labelChooseLevel = new JLabel("<html> <p style='text-align: center;font-size:11px;padding:2;'>"+
				"<font color='white'>"
				+ "Choose a subgroup</font></html>");

		_labelChooseNumWords = new JLabel("How many words you would like to be tested on:");
		
		_btnTenWords = new JRadioButton("10 Random Words");
		_btnTenWords.setSelected(true);//default select this option

		_btnTwentyWords = new JRadioButton("20 Random Words");
		_btnFortyWords = new JRadioButton("40 Random Words");
		_btnFiftyWords = new JRadioButton("50 Random Words");
		_btnAllWords = new JRadioButton("All words from this subgroup");

		//only allow one radio button selection at a time
		ButtonGroup group = new ButtonGroup();
		group.add(_btnTenWords);
		group.add(_btnTwentyWords);
		group.add(_btnFortyWords);
		group.add(_btnFiftyWords);
		group.add(_btnAllWords);

		_btnStartQuiz = new JButton("Start!");

		//_btnBackToPrevious = new JButton("Back");
		_btnBackToMain = new JButton("Home");
	}

	@Override
	public JPanel createAndGetPanel() {
		_chooseLevelPanel = new JPanel();
		_chooseLevelPanel.setBackground(new Color(6,149,255));

		//items in combo box are all levels 
		_comboBox = new JComboBox<String>(_model.getAllLevelsFromCourse());

		/**
		 * DECLARATION: THE FOLLOWING METHOD ON JAVA GRIDBAG LAYOUT ARE SOURCED 
		 * AND EDITED FROM THE ORACLE TUTORIAL WEBPAGE
		 * URL: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
		 */
		_chooseLevelPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		_chooseLevelPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(20,90,20,10);
		_chooseLevelPanel.add(_labelViewChangeCourse, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_btnViewWordList, c);
		_btnViewWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,0,20,10);
		_chooseLevelPanel.add(_btnChangeCourse, c);
		_btnChangeCourse.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_labelChooseLevel, c);
		//	_btnViewWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_comboBox, c);
		//	_btnViewWordList.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_labelChooseNumWords, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,10,10);
		_chooseLevelPanel.add(_btnTenWords, c);
		_btnTenWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,10,10);
		_chooseLevelPanel.add(_btnTwentyWords, c);
		_btnTwentyWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,10,10);
		_chooseLevelPanel.add(_btnFortyWords, c);
		_btnFortyWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,10,10);
		_chooseLevelPanel.add(_btnFiftyWords, c);
		_btnFiftyWords.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 10;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_btnAllWords, c);
		_btnAllWords.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 11;
		c.gridwidth = 2;
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.ipady = 10;
		//c.ipadx = 190;
		c.insets = new Insets(0,90,20,10);
		_chooseLevelPanel.add(_btnStartQuiz, c);
		_btnStartQuiz.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 13;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,10);
		_chooseLevelPanel.add(_btnBackToMain, c);
		_btnBackToMain.addActionListener(this);

		/*c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 11;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,10,20,0);
		_chooseLevelPanel.add(_btnBackToPrevious, c);
		_btnBackToPrevious.addActionListener(this);*/

		return _chooseLevelPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showMainMenu();

		}else if(e.getSource() == _btnViewWordList) {

			//create frame to show all words in the selected course
			new ShowAllCourseWordsView(_courseName, _model.getAllWordsFromCourse(), _chooseLevelPanel);			
			//disable this frame
			VoxSpellGui.getFrame().setEnabled(false);

		}else if (e.getSource() == _btnChangeCourse){

			VoxSpellGui.showCourseChooser();
		}else if (e.getSource() == _btnTenWords){
			
			_quizAllWords  = false;
			_numWordsToQuiz = 10;
		}else if (e.getSource() == _btnTwentyWords){
			
			_quizAllWords  = false;
			_numWordsToQuiz = 20;
		}else if (e.getSource() == _btnFortyWords){
			
			_quizAllWords  = false;
			_numWordsToQuiz = 40;
		}else if (e.getSource() == _btnFiftyWords){
			
			_quizAllWords  = false;
			_numWordsToQuiz = 50;
		}else if (e.getSource() == _btnAllWords){
			
			_quizAllWords  = true;
		}else if (e.getSource() == _btnStartQuiz){

			String level =  (String) _comboBox.getSelectedItem();
			
			if (_quizAllWords){
				_numWordsToQuiz = _model.getLevelWordsFromCourse(level).size();
			}
			System.out.println("level "+level);
			System.out.println("words " + _numWordsToQuiz);

			QuizView quizView = new QuizView(level, _courseName);

			QuizModel quizModel = null;

			//create model of quizView according to quiz mode
			if (VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){
				//words from course wordlist
				quizModel = new PracticeQuizModel();
				quizModel.setView(quizView);
				quizModel.setAllWords(_model.getLevelWordsFromCourse(level), _numWordsToQuiz);
			}else{
				//words from note book
			}

			quizView.setModel(quizModel);
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "New Quiz");
			quizModel.getRandomWords();
		}

	}

	public void setModel(ChooseLevelModel model){
		_model = model;
		_model.setCoursePath(_courseName);
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
			if (quizMode.equals(VoxSpellGui.REVIEW)){

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

		JOptionPane.showMessageDialog(VoxSpellGui.getFrame(), message, 
				"No Available Words", JOptionPane.INFORMATION_MESSAGE);

		VoxSpellGui.showMainMenu();
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
