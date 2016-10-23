package voxSpell.views.quizViews;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import voxSpell.views.levelViews.ChooseLevelReviewView;
import voxSpell.views.levelViews.ChooseLevelView;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.models.festivalManager.FestivalModel;
import voxSpell.models.festivalManager.FestivalModel.Voice;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.status.QuizStatus;

/**
 * QuizView that is registered for a practice or review quiz; offers methods that updates the GUI according
 * to back stage logic results i.e. the model. It is a Card object.
 * @author chen
 *
 */
@SuppressWarnings("serial")
public class QuizView extends JPanel implements Card, ActionListener {

	protected String _level;
	protected String _courseName;

	protected QuizModel _quizModel;
	protected FestivalModel _festivalModel = FestivalModel.getInstance();

	protected JPanel _headingPanel;
	protected JLabel _labelHeading;
	protected JLabel _labelSubheading;

	protected JPanel _updateWordPanel;
	protected JLabel _labelToUpdateWordNum;
	protected JLabel _labelTotalWord;

	protected JTextField _inputArea ;

	private JPanel _answerPanel;
	private JLabel _labelCorrectSpelling; //"correct spelling:"
	private JLabel _labelAnswer; // actual answer 

	protected JButton _btnRelisten;

	protected JButton _btnCheckWord;
	private JButton _btnSkipWord;
	private JButton _btnShowAnswer;

	protected JPanel _tipsPanel;
	protected JLabel _caseLabel;
	protected JLabel _specialCharLabel;

	protected JButton _btnBack;

	final protected String _comboBoxItems[] = {"American (default)", "New Zealand"};
	final protected JComboBox<String> _cb;

	public QuizView (String level, String courseName){
		//set up fields
		_level = level;
		_courseName = courseName;

		//===heading======================================================
		//_headingPanel = new JPanel();
		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Course: " + courseName+ "</font></html>"));

		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,30));

		_labelSubheading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Subgroup : " + _level + "</font></html>"));
		_labelSubheading.setFont((new Font("SansSerif", Font.ITALIC,20)));

		//===word progress=================================================
		_updateWordPanel = new JPanel();
		_updateWordPanel.setBackground(Color.WHITE);

		_labelToUpdateWordNum = new JLabel();
		_labelTotalWord = new JLabel();

		_labelToUpdateWordNum.setFont(new Font("SansSerif", Font.BOLD,25));
		_labelTotalWord.setFont(new Font("SansSerif", Font.BOLD,17));

		_updateWordPanel.add(_labelToUpdateWordNum);
		_updateWordPanel.add(_labelTotalWord);

		//=====show answer=================================================
		_answerPanel = new JPanel();
		_answerPanel.setBackground(Color.WHITE);

		_labelCorrectSpelling = new JLabel();
		_labelCorrectSpelling.setFont((new Font("SansSerif", Font.PLAIN,13)));

		_labelAnswer = new JLabel();
		_labelAnswer.setFont((new Font("SansSerif", Font.BOLD,13)));

		_answerPanel.add(_labelCorrectSpelling);
		_answerPanel.add(_labelAnswer);

		_inputArea = new JTextField();
		_inputArea.setPreferredSize(new Dimension(50,30));
		//=======buttons==============================================
		ButtonFactory btnFactory = new ButtonFactory();
		_btnRelisten = btnFactory.getButton("./media/relisten.jpg","./media/relisten_hover.jpg");
		_btnRelisten.setBackground(Color.white);

		_btnCheckWord = btnFactory.getButton("./media/check_spelling.png","./media/check_spelling_hover.png");
		_btnCheckWord.setBackground(Color.white);

		_btnSkipWord = btnFactory.getButton("./media/skip.png","./media/skip_hover.png");
		_btnSkipWord.setBackground(Color.white);

		_btnShowAnswer = btnFactory.getButton("./media/answer.png","./media/answer_hover.png");
		_btnShowAnswer.setBackground(Color.white);

		_btnBack = btnFactory.getButton("./media/back_whitebg.png", "./media/back_hover.png");
		_btnBack.setBackground(Color.white);

		_tipsPanel = new JPanel(new GridBagLayout());
		_tipsPanel.setBackground(Color.white);
		_caseLabel = new JLabel();
		_specialCharLabel = new JLabel();
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		_tipsPanel.add(_caseLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		_tipsPanel.add(_specialCharLabel, c);

		//=======================================================

		_cb = new JComboBox<String>(_comboBoxItems);

		//change main menu footer/header background color so that it is consistent with this background color
		VoxSpellGui.setHeaderFooterColor(Color.white);
	}

	/**
	 * Create the quiz panel for new or review
	 */
	@Override
	public JPanel createAndGetPanel() {

		setBackground(Color.white);

		_cb.setEditable(false);
		//If it's the very first game in the session, default voice is American voice
		//Otherwise (e.g. selected a new level in the middle of a game), selected voice remains the same as the previous game's voice selection.
		if (FestivalModel._currentVoice == Voice.AMERICAN) {
			_cb.setSelectedItem("American (default)");
		}
		else {
			_cb.setSelectedItem("New Zealand");
		}
		_cb.addItemListener(_festivalModel);

		/**
		 * DECLARATION: THE FOLLOWING METHOD ON JAVA GRIDBAG LAYOUT ARE SOURCED 
		 * AND EDITED FROM THE ORACLE TUTORIAL WEBPAGE
		 * URL: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 1;
		add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.gridheight = 1;
		c.insets = new Insets(5,0,50,0);
		add(_labelSubheading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(25,0,20,0);
		add(_updateWordPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
		//c.weightx = 0.7;
		c.insets = new Insets(25,15,20,0);
		add(_tipsPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(5,55,-20,0);
		add(_answerPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,55,10,0);
		add(_btnRelisten, c);
		_btnRelisten.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 1;

		c.insets = new Insets(0,5,10,0);
		add(_inputArea, c);
		_inputArea.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(10,55,60,5);
		add(_cb, c);
		//add item listener

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10,15,60,0);
		add(_btnCheckWord, c);
		_btnCheckWord.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10,10,60,0);
		add(_btnSkipWord, c);
		_btnSkipWord.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(10,10,60,0);
		add(_btnShowAnswer, c);
		_btnShowAnswer.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(10,65,0,0);
		add(_btnBack, c);
		_btnBack.addActionListener(this);

		return this;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnCheckWord || e.getSource() == _inputArea){

			_quizModel.checkSpelling(_inputArea.getText());
		}else if (e.getSource() == _btnRelisten){

			_quizModel.relisten();
		}else if (e.getSource() == _btnBack){

			//popUp confirming user decision
			gameInProgressPopUp();

		}else if (e.getSource() == _btnSkipWord){
			//move on to next word
			_quizModel.moveOnToNextWord();
		}else if (e.getSource() == _btnShowAnswer){

			//set text colour to green
			_labelCorrectSpelling.setText(("<html> <p style='text-align:center;'>"
					+ "<font color='green'>Correct Spelling: </font></html>"));

			_labelAnswer.setText(("<html> <p style='text-align:center;'>"
					+ "<font color='green'>"
					+ _quizModel.getCorrectSpelling() + "</font></html>"));
		}

	}

	/**
	 *Make text disappear by changing font color to white. Otherwise the GUI would resize everytime the text is shown.
	 */
	public void disableAnswer() {


		_labelCorrectSpelling.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>Correct Spelling: </font></html>"));

		_labelAnswer.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ _quizModel.getCorrectSpelling() + "</font></html>"));
	}

	/**
	 * Register this view with its model.
	 * @param quiz
	 */
	public void setModel(QuizModel quiz){
		_quizModel = quiz;
	}

	/**
	 * A pop up warns user of invalid characters when the quizzed word does not contain any special characters
	 */
	public void showInvalidInputPopUp(){

		JOptionPane.showMessageDialog(this, "Non alphabetical character(s) detected! \n"
				+ "Make sure you do not have unintended white spaces.", 
				"Warning: Invalid Input", JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Update word progress on GUI
	 * @param currentWord
	 */
	public void updateWordLabel(int currentWord){

		_labelToUpdateWordNum.setText("<html> <font color='orange'>"
				+Integer.toString(currentWord)+ "</font></html>");

		_labelTotalWord.setText(" / "+Integer.toString(_quizModel.getTotalWordNum()));

	}


	/** 
	 * Shows a pop up window if the user leaves in the middle of a game
	 */
	protected void gameInProgressPopUp(){

		int dialogResult  = JOptionPane.showOptionDialog(VoxSpellGui.getFrame(), 
				("<html>Warning: <BR>" + "<BR>" +
						"You have a quiz in progress. <BR>" + 
						"Are you sure you want to leave? <BR>" + 
						"   </html>"),
				"Quiz In Progress", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				new String[]{"Stop", "Resume"}, // this is the array
				"default");

		if(dialogResult == JOptionPane.YES_OPTION){
			//show level choosing card again, according to game mode

			ChooseLevelView cardChooseLevel = null;
			if (VoxSpellGui.STATUS.equals(QuizStatus.NEW)){
				cardChooseLevel = new ChooseLevelView(_courseName);
			}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){
				cardChooseLevel = new ChooseLevelReviewView(_courseName);
			}

			VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

		}//else dispose panel and carry on

	}

	/**
	 * Decides whether to show the hints for case sensitivity or special characters.
	 * This method is called by the model.
	 * @param caseSensitive
	 * @param specialChar
	 */
	public void updateTipsLabel(boolean caseSensitive, boolean specialChar){
		if (caseSensitive){
			_caseLabel.setText("<html><font color='gray'>( Hint: this word is case sensitive! )</font></html>");
		}else{
			_caseLabel.setText("<html><font color='white'>( Hint: this word is case sensitive! )</font></html>");
		}

		if(specialChar){
			_specialCharLabel.setText("<html><font color='gray'>( Hint: this word has special character(s)! )</font></html>");
		}else{
			_specialCharLabel.setText("<html><font color='white'>( Hint: this word has special character(s)! )</font></html>");

		}
	}

	public void clearInputArea(){
		_inputArea.setText("");
	}

	public void showMainMenu(){
		VoxSpellGui.showMainMenu();
	}

	public String getLevelName(){
		return _level;
	}

	public String getCourseName(){
		return _courseName;
	}

	protected void setFestivalModel(FestivalModel model) {
		_festivalModel = model;
	}
}
