package VoxSpell;

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
import VoxSpell.VoxSpellGui.LEVEL;

/**
 * QuizView that is registered for a new or review quiz; offers methods that updates the GUI accoridng
 * to back stage logic results i.e. the model
 * @author chen
 *
 */
@SuppressWarnings("serial")
public class QuizView extends JPanel implements Card, ActionListener {

	private LEVEL _level = null;
	private QuizModel _quizModel;
	private FestivalModel _festivalModel;

	private JButton btnRelisten = new JButton("Relisten");
	private JButton btnCheckWord = new JButton("Check spelling");

	private JTextField inputArea = new JTextField();

	private JLabel spellHere = new JLabel("Enter here: ");
	private JLabel labelSpellWord;

	private JLabel labelFesVoice = new JLabel("Choose your preferred voice below :");
	private JLabel labelAccuracy = new JLabel("Accuracy Rates:     ");

	private List<JLabel> _accuracyLabels = new ArrayList<JLabel>();

	private JLabel labelLvlOne = new JLabel();
	private JLabel labelLvlTwo = new JLabel();
	private JLabel labelLvlThree = new JLabel();
	private JLabel labelLvlFour = new JLabel();
	private JLabel labelLvlFive = new JLabel();
	private JLabel labelLvlSix = new JLabel();
	private JLabel labelLvlSeven = new JLabel();
	private JLabel labelLvlEight = new JLabel();
	private JLabel labelLvlNine = new JLabel();
	private JLabel labelLvlTen = new JLabel();
	private JLabel labelLvlEleven = new JLabel();
	
	private JLabel tipsLabel = new JLabel();

	final protected String comboBoxItems[] = {"American (default)", "New Zealand"};
	final protected JComboBox<String> cb = new JComboBox<String>(comboBoxItems);


	public QuizView (int level){
		//set up labels that need the level information
		for (LEVEL i : LEVEL.values()){

			if (i.getLevel() == level){
				_level = i;
				break;
			}
		}

		labelSpellWord = new JLabel("Spell Word 1 of 10 on Level " + _level + ": ");

		//add all accuracy labels to arraylist
		_accuracyLabels.add(labelLvlOne);
		_accuracyLabels.add(labelLvlTwo);
		_accuracyLabels.add(labelLvlThree);
		_accuracyLabels.add(labelLvlFour);
		_accuracyLabels.add(labelLvlFive);
		_accuracyLabels.add(labelLvlSix);
		_accuracyLabels.add(labelLvlSeven);
		_accuracyLabels.add(labelLvlEight);
		_accuracyLabels.add(labelLvlNine);
		_accuracyLabels.add(labelLvlTen);
		_accuracyLabels.add(labelLvlEleven);

		for (int i = 0; i < _accuracyLabels.size(); i++){
			if (i == (level - 1)){
				//start with 100% accuracy on the current level
				_accuracyLabels.get(i).setText("* Level "+ _level.getLevel() +": -- ");
			}else{
				_accuracyLabels.get(i).setText("* Level "+ (i+1) +": NA     ");
			}
		}
	}

	/**
	 * Create the quiz panel for new or review
	 */
	@Override
	public JPanel createAndGetPanel() {

		cb.setEditable(false);
		//If it's the very first game in the session, default voice is American voice
		//Otherwise (e.g. selected a new level in the middle of a game), selected voice remains the same as the previous game's voice selection.
		if (FestivalModel._currentVoice == Voice.AMERICAN) {
			cb.setSelectedItem("American (default)");
		}
		else {
			cb.setSelectedItem("New Zealand");
		}
		cb.addItemListener(_festivalModel);

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
		c.gridwidth = 2;
		c.gridheight = 1;
		
		c.insets = new Insets(10,45,6,0);
		add(labelSpellWord, c);	

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(5,55,6,0);
		add(spellHere, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(5,0,6,10);
		add(inputArea, c);
		inputArea.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(10,55,3,0);
		add(btnRelisten, c);
		btnRelisten.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.5;
		c.insets = new Insets(10,15,3,10);
		add(btnCheckWord, c);
		btnCheckWord.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.7;
		c.insets = new Insets(15,55,60,0);
		add(tipsLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.gridheight = 1;
		//c.weightx = 0.7;
		c.insets = new Insets(15,45,3,0);
		add(labelFesVoice, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(11,55,3,5);
		add(cb, c);
		//add item listener

		// a new panel for the accuracy rate labels
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
		add(accuracyPanel, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.insets = new Insets(0,14,3,10);
		accuracyPanel.add(labelAccuracy, c);	

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(10,5,3,10);
		accuracyPanel.add(labelLvlOne, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlTwo, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlThree, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlFour, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlFive, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlSix, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlSeven, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlEight, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlNine, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlTen, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 11;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(5,5,3,10);
		accuracyPanel.add(labelLvlEleven, c);

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

		for (LEVEL i : LEVEL.values()){
			if (level == i.getLevel()){
				_level = i;
			}
		}
		labelSpellWord.setText(( quizMode +" Word "+ (currentWord) + " of " +totalWord+ " on Level " + _level + ": "));
	}
	
	public void updateTipsLabel(boolean caseSensitive){
		if (caseSensitive){
			tipsLabel.setText("(Hint: this word is case sensitive!)");
		}else {
			tipsLabel.setText("<html> <BR> </html>");
		}
	}

	public void clearInputArea(){
		inputArea.setText("");
	}

	public void showMainMenu(){
		MainMenuView.showMainMenu();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnCheckWord || e.getSource() == inputArea){
			_quizModel.checkSpelling(inputArea.getText());
		}else if (e.getSource() == btnRelisten){
			_quizModel.relisten();
		}

	}
	
	protected void setFestivalModel(FestivalModel model) {
		_festivalModel = model;
	}
}
