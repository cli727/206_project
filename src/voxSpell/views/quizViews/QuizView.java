package voxSpell.views.quizViews;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import voxSpell.views.levelViews.ChooseLevelReviewView;
import voxSpell.views.levelViews.ChooseLevelView;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.models.festivalManager.FestivalModel;
import voxSpell.models.festivalManager.FestivalModel.Voice;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.status.QuizStatus;

/**
 * QuizView that is registered for a new or review quiz; offers methods that updates the GUI accoridng
 * to back stage logic results i.e. the model
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

	protected JLabel _tipsLabel;

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

		_labelToUpdateWordNum.setFont(new Font("SansSerif", Font.BOLD,17));

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
		final ImageIcon relisten = new ImageIcon("./media/relisten.jpg");
		_btnRelisten = new JButton(relisten);
		Dimension size = new Dimension(relisten.getIconWidth(), relisten.getIconHeight());
		_btnRelisten.setPreferredSize(size);
		_btnRelisten.setBackground(Color.white);
		_btnRelisten.setBorderPainted(false);
		_btnRelisten.setFocusable(false);

		_btnRelisten.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnRelisten.setIcon(new ImageIcon("./media/relisten_hover.jpg"));				
				}else{
					_btnRelisten.setIcon(relisten);
				}
			}
		});
		
		final ImageIcon check = new ImageIcon("./media/check_spelling.png");
		_btnCheckWord = new JButton(check);
		size = new Dimension(check.getIconWidth(), check.getIconHeight());
		_btnCheckWord.setPreferredSize(size);
		_btnCheckWord.setBackground(Color.white);
		_btnCheckWord.setBorderPainted(false);

		_btnCheckWord.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnCheckWord.setIcon(new ImageIcon("./media/check_spelling_hover.png"));				
				}else{
					_btnCheckWord.setIcon(check);
				}
			}
		});
		
		final ImageIcon skip = new ImageIcon("./media/skip.png");
		_btnSkipWord = new JButton(skip);
		 size = new Dimension(skip.getIconWidth(), skip.getIconHeight());
		_btnSkipWord.setPreferredSize(size);
		_btnSkipWord.setBackground(Color.white);
		_btnSkipWord.setBorderPainted(false);

		_btnSkipWord.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnSkipWord.setIcon(new ImageIcon("./media/skip_hover.png"));				
				}else{
					_btnSkipWord.setIcon(skip);
				}
			}
		});
		
		final ImageIcon answer = new ImageIcon("./media/answer.png");
		_btnShowAnswer = new JButton(answer);
		 size = new Dimension(answer.getIconWidth(), answer.getIconHeight());
		 _btnShowAnswer.setPreferredSize(size);
		_btnShowAnswer.setBackground(Color.white);
		_btnShowAnswer.setBorderPainted(false);

		_btnShowAnswer.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnShowAnswer.setIcon(new ImageIcon("./media/answer_hover.png"));				
				}else{
					_btnShowAnswer.setIcon(answer);
				}
			}
		});
		
		final ImageIcon back = new ImageIcon("./media/back_whitebg.png");
		 size = new Dimension(back.getIconWidth(), back.getIconHeight());
		 _btnBack = new JButton(back);
		 _btnBack.setPreferredSize(size);
		 _btnBack.setBorderPainted(false);
		 _btnBack.setBackground(Color.white);
		 
		 _btnBack.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnBack.setIcon(new ImageIcon("./media/back_hover.png"));

				}else{
					_btnBack.setIcon(back);
				}
			}
		});

		_tipsLabel = new JLabel();
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
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(5,55,0,0);
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
		c.ipadx = 0;
		c.insets = new Insets(0,5,10,0);
		add(_inputArea, c);
		_inputArea.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 4;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.7;
		c.insets = new Insets(0,5,10,0);
		add(_tipsLabel, c);

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
		c.insets = new Insets(10,5,60,0);
		add(_btnCheckWord, c);
		_btnCheckWord.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10,5,60,0);
		add(_btnSkipWord, c);
		_btnSkipWord.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(10,5,60,0);
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

	public void disableAnswer() {

		//make text 'disappear by changing font color to white
		_labelCorrectSpelling.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>Correct Spelling: </font></html>"));

		_labelAnswer.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ _quizModel.getCorrectSpelling() + "</font></html>"));
	}

	public void setModel(QuizModel quiz){
		_quizModel = quiz;
	}

	public void showInvalidInputPopUp(){
		
		JOptionPane.showMessageDialog(this, "Non alphabetical character(s) detected! \n"
				+ "Make sure you do not have unintended white spaces.", 
				"Warning: Invalid Input", JOptionPane.INFORMATION_MESSAGE);
		
	}

	public void updateWordLabel(int currentWord){

		_labelToUpdateWordNum.setText("<html> <font color='orange'>"
				+Integer.toString(currentWord)+ "</font></html>");

		_labelTotalWord.setText(" / "+Integer.toString(_quizModel.getTotalWordNum()));

	}


	/** shows a pop up window if the user leaves in the middle of a game
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

	public void showReviewEndPopUp(int correct, int incorrect){

		String message = "Level " + _level +" Review Completed: \n" + "\n" +
				"You got " + correct+ " words correct.\n" + 
				"You got " + incorrect + " words incorrect. \n\n"+
				"Good Effort! \n";

		JOptionPane.showMessageDialog(this, message, 
				"Review Completed", JOptionPane.INFORMATION_MESSAGE);
	}

	public void updateTipsLabel(boolean caseSensitive){
		if (caseSensitive){
			_tipsLabel.setText("<html><font color='gray'>(Hint: this word is case sensitive!)</font></html>");
		}else {
			_tipsLabel.setText("<html><font color='white'>(Hint: this word is case sensitive!)</font></html>");
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
