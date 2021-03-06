package voxSpell.views.levelViews;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.views.courseViews.ShowAllCourseWordsView;
import voxSpell.views.quizViews.QuizView;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.status.NumWordStatus;
import voxSpell.status.QuizStatus;

/**
 * A view that shows the practice set up screen. It is a Card object.
 * @author chen
 *
 */
public class ChooseLevelView implements Card, ActionListener{
	protected HiddenFilesModel _hiddenFilesModel; 

	private Color _bgColor ;

	protected JPanel _chooseLevelPanel;

	protected JLabel _labelHeading;
	private JLabel _labelViewChangeCourse; 
	protected JButton _btnViewWordList;
	protected JButton _btnChangeCourse;
	protected JLabel _labelChooseLevel;

	protected JComboBox<String> _comboBox;

	protected  JLabel _labelChooseNumWords;
	protected JRadioButton _btnAllWords;
	protected JRadioButton _btnTenWords;
	protected  JRadioButton _btnTwentyWords;
	protected JRadioButton _btnFortyWords;
	protected JRadioButton _btnFiftyWords;

	protected JButton _btnStartQuiz;
	protected  JButton _btnBackToMain;

	protected String _courseName;

	protected  int _numWordsToQuiz;

	public static NumWordStatus _numWordStatus ;


	public ChooseLevelView (String courseName){
		//initialise fields

		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_bgColor = new Color(17,103,172);

		_courseName = courseName;

		Font headingFont = new Font("SansSerif", Font.ITALIC,30);

		//set up header label according to quiz mode, otherwise test and practice level chooser view serves same function
		if (VoxSpellGui.STATUS.equals(QuizStatus.TEST)){
			_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
					+ "<font color='white'>"
					+ "Set up your test...</font></html>"));
		}else{
			_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
					+ "<font color='white'>"
					+ "Set up your practice quiz...</font></html>"));
		}

		_labelHeading.setFont(headingFont);

		_labelViewChangeCourse = new JLabel("<html> <p style='text-align: center;font-size:13px;padding:2;'><font color=white>"+
				"Course: " + _courseName+"</font></html>");

		//set button image

		ButtonFactory btnFactory = new ButtonFactory();
		_btnViewWordList = btnFactory.getButton("./media/view_words.png","./media/view_words_hover.png");
		_btnViewWordList.setBackground(_bgColor);
		
		_btnChangeCourse = btnFactory.getButton("./media/change_course.png","./media/change_course_hover.png");
		_btnChangeCourse.setBackground(_bgColor);
		
		_labelChooseLevel = new JLabel("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
				"Choose a subgroup:"+"</font></html>");

		//items in combo box are all levels that are NOT empty
		Vector<String> nonEmptyLevels = _hiddenFilesModel.getAllLevelsFromCourse("./.course/"+_courseName);

		for (int i = 0; i < nonEmptyLevels.size(); i ++){
			//get words within this level, check if empty

			if (_hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName, nonEmptyLevels.get(i)).isEmpty()){
				//remove from list because it has no words to quiz

				nonEmptyLevels.remove(i);
			};
		}

		_comboBox = new JComboBox<String>(nonEmptyLevels);
		_comboBox.setPreferredSize(new Dimension(220, 25));

		_labelChooseNumWords = new JLabel("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
				"How many words would you like:"+"</font></html>");

		_btnTenWords = new JRadioButton("10 Random Words");
		_btnTenWords.setForeground(Color.white);
		_btnTwentyWords = new JRadioButton("20 Random Words");
		_btnTwentyWords.setForeground(Color.white);
		_btnFortyWords = new JRadioButton("40 Random Words");
		_btnFortyWords.setForeground(Color.white);
		_btnFiftyWords = new JRadioButton( "50 Random Words");
		_btnFiftyWords.setForeground(Color.white);
		_btnAllWords = new JRadioButton("All words from this subgroup");
		_btnAllWords.setForeground(Color.white);
		_btnAllWords.setSelected(true);//default select this option
		_numWordStatus = NumWordStatus.ALL;//since "all words" is the default selected button
		_numWordsToQuiz = _hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,(String) _comboBox.getSelectedItem()).size();


		_btnTenWords.setBackground(_bgColor);
		_btnTenWords.setFocusable(false);
		_btnTwentyWords.setBackground(_bgColor);
		_btnTwentyWords.setFocusable(false);
		_btnFortyWords.setBackground(_bgColor);
		_btnFortyWords.setFocusable(false);
		_btnFiftyWords.setBackground(_bgColor);
		_btnFiftyWords.setFocusable(false);
		_btnAllWords.setBackground(_bgColor);
		_btnAllWords.setFocusable(false);

		//only allow one radio button selection at a time
		ButtonGroup group = new ButtonGroup();
		group.add(_btnTenWords);
		group.add(_btnTwentyWords);
		group.add(_btnFortyWords);
		group.add(_btnFiftyWords);
		group.add(_btnAllWords);

		_btnStartQuiz = btnFactory.getButton("./media/start.png","./media/start_hover.png");
		_btnStartQuiz.setBackground(_bgColor);
		
		_btnBackToMain = btnFactory.getButton("./media/back.png", "./media/back_hover.png");
		_btnBackToMain.setBackground(_bgColor);

		//change main menu footer/header background color so that it is consistent with this background color
		VoxSpellGui.setHeaderFooterColor(_bgColor);
	}

	@Override
	public JPanel createAndGetPanel() {
		_chooseLevelPanel = new JPanel();
		_chooseLevelPanel.setBackground(_bgColor);

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
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,0,10,0);
		_chooseLevelPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(20,150,10,0);
		_chooseLevelPanel.add(_labelViewChangeCourse, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,80,10,0);
		_chooseLevelPanel.add(_btnViewWordList, c);
		_btnViewWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,0,10,0);
		_chooseLevelPanel.add(_btnChangeCourse, c);
		_btnChangeCourse.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(20,150,10,0);
		_chooseLevelPanel.add(_labelChooseLevel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;

		c.insets = new Insets(0,160,10,0);
		_chooseLevelPanel.add(_comboBox, c);
		_comboBox.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(20,150,10,0);
		_chooseLevelPanel.add(_labelChooseNumWords, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 1;

		c.insets = new Insets(0,160,5,0);
		_chooseLevelPanel.add(_btnTenWords, c);
		_btnTenWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,160,5,0);
		_chooseLevelPanel.add(_btnTwentyWords, c);
		_btnTwentyWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,160,5,0);
		_chooseLevelPanel.add(_btnFortyWords, c);
		_btnFortyWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,160,5,0);
		_chooseLevelPanel.add(_btnFiftyWords, c);
		_btnFiftyWords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,160,10,0);
		_chooseLevelPanel.add(_btnAllWords, c);
		_btnAllWords.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.ipady = 10;
		c.insets = new Insets(25,90,0,0);
		_chooseLevelPanel.add(_btnStartQuiz, c);
		_btnStartQuiz.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 11;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.insets = new Insets(25,65,0,0);
		_chooseLevelPanel.add(_btnBackToMain, c);
		_btnBackToMain.addActionListener(this);

		//disable number of word radiobuttons accordingly
		//get number of words in selected level
		disableNumWordsButtons();

		return _chooseLevelPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showMainMenu();
			

		}else if(e.getSource() == _btnViewWordList) {

			//create frame to show all words in the selected course
			new ShowAllCourseWordsView(_courseName, _chooseLevelPanel);			
			//disable this frame
			VoxSpellGui.getFrame().setEnabled(false);

		}else if (e.getSource() == _btnChangeCourse){

			VoxSpellGui.showCourseChooser(_courseName);
		}else if (e.getSource() == _comboBox){

			//disable number of word radiobuttons accordingly
			//get number of words in selected level
			disableNumWordsButtons();
			
			_btnAllWords.setSelected(true); //all words is the default option when a new subgroup is selected
			_numWordStatus = NumWordStatus.ALL;
			_numWordsToQuiz = _hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,(String) _comboBox.getSelectedItem()).size();

		}else if (e.getSource() == _btnTenWords){

			_numWordsToQuiz = 10;
			_numWordStatus= NumWordStatus.TEN;
		}else if (e.getSource() == _btnTwentyWords){

			_numWordsToQuiz = 20;
			_numWordStatus = NumWordStatus.TWENTY;
		}else if (e.getSource() == _btnFortyWords){

			_numWordsToQuiz = 40;
			_numWordStatus = NumWordStatus.FORTY;
		}else if (e.getSource() == _btnFiftyWords){

			_numWordsToQuiz = 50;
			_numWordStatus = NumWordStatus.FIFTY;
		}else if (e.getSource() == _btnAllWords){

			_numWordsToQuiz = _hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,(String) _comboBox.getSelectedItem()).size();
			_numWordStatus = NumWordStatus.ALL;
		}else if (e.getSource() == _btnStartQuiz){

			String level =  (String) _comboBox.getSelectedItem();

			QuizView quizView =null;
			QuizModel quizModel = null;

			//create regular quiz view otherwise
			quizView = new QuizView(level, _courseName);
			quizModel = new QuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/" + _courseName));

			quizModel.setView(quizView);
			quizModel.setAllWords(_hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,level), _numWordsToQuiz);

			quizView.setModel(quizModel);
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Practice Quiz");
			quizModel.getRandomWords();
		}

	}

	/**
	 * Disable some "number of word to quiz" options based on how many words the currently selected subgroup has
	 */
	protected void disableNumWordsButtons() {

		_numWordsToQuiz=_hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,(String) _comboBox.getSelectedItem()).size();
		//disable buttons accordingly based on numWOrds
		if (_numWordsToQuiz < 50){
			_btnFiftyWords.setEnabled(false);
			_btnFiftyWords.setForeground(Color.gray);
		}else{
			_btnFiftyWords.setEnabled(true);
			_btnFiftyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 40){
			_btnFortyWords.setEnabled(false);
			_btnFortyWords.setForeground(Color.gray);
		}else{
			_btnFortyWords.setEnabled(true);
			_btnFortyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 20){
			_btnTwentyWords.setEnabled(false);
			_btnTwentyWords.setForeground(Color.gray);
		}else{
			_btnTwentyWords.setEnabled(true);
			_btnTwentyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 10){
			_btnTenWords.setEnabled(false);
			_btnTenWords.setForeground(Color.gray);
		}else{
			_btnTenWords.setEnabled(true);
			_btnTenWords.setForeground(Color.white);
		}

		//always enable 'get all words' button, unless there is no words
		if (_numWordsToQuiz == 0){
			//disable all buttons
			_btnAllWords.setEnabled(false);
			_btnStartQuiz.setEnabled(false);

			//tell user that there is no word to quiz 
			_labelChooseLevel.setText("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
					"This subgroup has no available words!"+"</font></html>");
		}else{
			_btnAllWords.setEnabled(true);
			_btnStartQuiz.setEnabled(true);
			_labelChooseLevel.setText("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
					"Choose a subgroup:"+"</font></html>");
		}
	}
}
