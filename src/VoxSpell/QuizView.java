package VoxSpell;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import VoxSpell.FestivalModel.Voice;

/**
 * QuizView that is registered for a new or review quiz; offers methods that updates the GUI accoridng
 * to back stage logic results i.e. the model
 * @author chen
 *
 */
@SuppressWarnings("serial")
public class QuizView extends JPanel implements Card, ActionListener {

	private String _level = null;
	private QuizModel _quizModel;
	private FestivalModel _festivalModel;

	private JLabel _labelSpellWord;
	private JLabel _spellHere;
	private JTextField _inputArea ;

	private JLabel _labelDefinition;

	private JButton _btnRelisten;
	
	private JButton _btnCheckWord;
	private JButton _btnSkipWord;
	private JButton _btnShowAnswer;

	private JLabel _labelFesVoice ;
	private JLabel _labelAccuracy;

	private JLabel _tipsLabel;
	
	private JButton _btnBack;

	final protected String _comboBoxItems[] = {"American (default)", "New Zealand"};
	final protected JComboBox<String> _cb;
	


	public QuizView (String level, String courseName){
		//set up fields
		_level = level;

		_labelSpellWord = new JLabel("Spell Word 1 of 10 on Level " + _level + ": " + "in course : " + courseName);
		_spellHere = new JLabel("Enter here: ");
		_inputArea = new JTextField();

		_labelDefinition = new JLabel("I am defi");

		_btnRelisten = new JButton("Relisten");
		_btnCheckWord = new JButton("Check spelling");
		_btnSkipWord = new JButton("Skip");
		_btnShowAnswer = new JButton ("Answer");
		/*
 _labelFesVoice = new JLabel("Choose your preferred voice below :");
	 _labelAccuracy = new JLabel("Accuracy Rates:     ");*/

		_tipsLabel = new JLabel();
		_cb = new JComboBox<String>(_comboBoxItems);
		_btnBack = new JButton("FUCK MY LIFE");
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
		//	c.insets = new Insets(10,45,6,0);
		add(_labelSpellWord, c);	

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 4;
		//c.weightx = 0.3;
		//c.insets = new Insets(5,55,6,0);
		add(_spellHere, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 2;
		//c.weightx = 0.3;
		//c.insets = new Insets(5,0,6,10);
		add(_labelDefinition, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(10,55,3,0);
		add(_btnRelisten, c);
		_btnRelisten.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 1;
		//c.weightx = 0.5;
		//c.insets = new Insets(10,15,3,10);
		add(_inputArea, c);
		_inputArea.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 4;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.7;
		//c.insets = new Insets(15,55,60,0);
		add(_tipsLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.insets = new Insets(11,55,3,5);
		add(_cb, c);
		//add item listener

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(10,55,3,0);
		add(_btnCheckWord, c);
		_btnCheckWord.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(10,55,3,0);
		add(_btnSkipWord, c);
		_btnSkipWord.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(10,55,3,0);
		add(_btnShowAnswer, c);
		_btnShowAnswer.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(10,55,3,0);
		add(_btnBack, c);
		_btnBack.addActionListener(this);
		
		/*// a new panel for the accuracy rate labels
		JPanel accuracyPanel = new JPanel();
		accuracyPanel.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		//c.ipadx = 10;
		c.gridwidth = 2;
		c.gridheight = 7;
		//c.weightx = 1;
		c.insets = new Insets(10,5,3,20);
		add(accuracyPanel, c);*/

		return this;
	}

	public void setModel(QuizModel quiz){
		_quizModel = quiz;
	}

	public void showInvalidInputPopUp(){
		JOptionPane.showMessageDialog(this, "Non alphabetical character(s) detected! \n"
				+ "Make sure you do not have unintended white spaces.", 
				"Warning: Invalid Input", JOptionPane.INFORMATION_MESSAGE);

	}

	public boolean showPassLevelPopUP(int correct, int incorrect){

		boolean playVideo = false;

		String message = "Level " + _level +" Completed: \n" + "\n" +
				"You got " + correct+ " words correct.\n" + 
				"You got " + incorrect + " words incorrect. \n\n"+
				"Congratulations on passing the level! \n"
				+ "Would you like to play your video reward?";

		String title="Level Succeeded";

		if (_level.getLevel() == 11){
			message = "Level ELEVEN" +" Completed: \n" + "\n" +
					"You got " + correct+ " words correct.\n" + 
					"You got " + incorrect + " words incorrect. \n\n"+
					"Congratulations! \n"
					+ "You have completed all levels!\n"+
					"Would you like to play your special video reward?";

			title = "All Levels Completed";
		}

		int dialogResult = JOptionPane.showConfirmDialog (this, message,title,JOptionPane.YES_NO_OPTION);

		if(dialogResult == JOptionPane.YES_OPTION){
			playVideo = true;
		}

		//add comboBox panel to popup window
		return playVideo;

	}

	public void showFailLevelPopUp(int correct, int incorrect){

		String message = "Level " + _level +" Completed: \n" + "\n" +
				"You got " + correct+ " words correct.\n" + 
				"You got " + incorrect + " words incorrect. \n\n"+
				"Sorry you did not pass this level! \n"
				+ "(You need to get 9 or more words correct to pass)\n"+
				"Don't give up! \nTry starting on a lower level or review your mistakes!";

		JOptionPane.showMessageDialog(this, message, 
				"Level Failed", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean showLevelUpPopUP(int currentLevel){

		boolean levelUp = false;

		JPanel popUpPanel = new JPanel();


		//ask them if they want to play their reward video

		int dialogResult  = JOptionPane.showOptionDialog(popUpPanel, 
				("<html>Would you like to: <BR><BR>"+
						"Move on to the next Level ( level " + (currentLevel+1) +" )<BR>" + 
						"Or <BR>"+
						"Try more words from this level? <BR>    </html>"),
				"Level Up", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				new String[]{"Level Up", "Stay"}, // this is the array
				"default");

		if(dialogResult == JOptionPane.YES_OPTION){
			//update level
			int level = _level.getLevel();
			level++;
			for (LEVEL i : LEVEL.values()){
				if (level == i.getLevel()){
					_level = i;
					break;
				}
			}
			levelUp = true;
		}else if(dialogResult == JOptionPane.NO_OPTION){
			levelUp = false;
		}else{
			//if the user closes the window 
			showLevelUpPopUP(currentLevel);
		}

		return levelUp;
	}

	public void showReviewEndPopUp(int correct, int incorrect){

		String message = "Level " + _level +" Review Completed: \n" + "\n" +
				"You got " + correct+ " words correct.\n" + 
				"You got " + incorrect + " words incorrect. \n\n"+
				"Good Effort! \n";

		JOptionPane.showMessageDialog(this, message, 
				"Review Completed", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Update accuracy rates of a level
	 * @param level
	 */
	public void updateJLabel(int level, String accuracy){
		JLabel labelToUpdate = _accuracyLabels.get(level -1);

		labelToUpdate.setText("* Level "+ level +": "+accuracy);
	}

	public void updateWordLabel(String quizMode,int currentWord,int totalWord, int level){


		_labelSpellWord.setText(( quizMode +" Word "+ (currentWord) + " of " +totalWord+ " on Level " + _level + ": "));
	}

	public void updateTipsLabel(boolean caseSensitive){
		if (caseSensitive){
			_tipsLabel.setText("(Hint: this word is case sensitive!)");
		}else {
			_tipsLabel.setText("<html> <BR> </html>");
		}
	}

	public void clearInputArea(){
		_inputArea.setText("");
	}

	public void showMainMenu(){
		VoxSpellGui.showMainMenu();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnCheckWord || e.getSource() == _inputArea){
			_quizModel.checkSpelling(_inputArea.getText());
		}else if (e.getSource() == _btnRelisten){
			_quizModel.relisten();
		}else if (e.getSource() == _btnBack){
			//show level choosing card again
			ChooseLevelView cardChooseLevel = new ChooseLevelView("wordlistOne");
			ChooseLevelModel chooseLevelModel = new ChooseLevelModel();
			cardChooseLevel.setModel(chooseLevelModel);
			VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
		}

	}

	protected void setFestivalModel(FestivalModel model) {
		_festivalModel = model;
	}
}
