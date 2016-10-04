package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import VoxSpell.VoxSpellGui.LEVEL;
import VoxSpell.FestivalModel;

public class MainMenuView extends JPanel implements Card{

	private JPanel _welcomePanel;
	private JLabel _welcomeLabel;
	private JButton _btnHowItWorks;
	private JButton _btnNewQuiz;
	private JButton _btnReview;
	private JButton _btnScoreHistory;
	private JButton _btnImportWordList;
	private JButton _btnCreateWordList;

	private  MainMenuController _controller;
	
	public MainMenuView () {
		//initialise fields
		_welcomePanel = new JPanel();
		_welcomeLabel = new JLabel("SOME WELCOME MESSAGE HERE");
		ImageIcon water = new ImageIcon("image.png");
		_btnHowItWorks = new JButton("How It Works");
		_btnNewQuiz = new JButton("New Quiz");
		_btnReview = new JButton("Review Mistakes");
		_btnScoreHistory = new JButton("Score History");
		_btnImportWordList = new JButton("Import Wordlist");
		_btnCreateWordList = new JButton("Create Wordlist");

		_welcomePanel.add(_welcomeLabel);
	}
	
	public void setController(MainMenuController controller){
		_controller = controller;
	}

	@Override
	public JPanel createAndGetPanel() {

		/*add(new JLabel("<html> <p style='text-align: center;font-size:13px;padding:8;'>"
				+ " Welcome To VOXSPELL!</html>", 
				JLabel.CENTER),BorderLayout.NORTH);*/

		this.setBackground(new Color(47,145,195));

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
		c.gridwidth = 9;
		c.gridheight = 2;
		c.ipady = 100;
		c.insets = new Insets(5,5,5,5);
		add(_welcomePanel, c);	

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(5,10,5,5);
		c.ipady = 180;
		c.ipadx = 200;
		add(_btnHowItWorks, c);
		_btnHowItWorks.addActionListener(_controller);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.insets = new Insets(5,0,5,5);
		add(_btnNewQuiz, c);
		_btnNewQuiz.addActionListener(_controller);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(5,0,5,10);
		add(_btnReview, c);
		_btnReview.addActionListener(_controller);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.5;
		c.insets = new Insets(0,10,40,5);
		add(_btnImportWordList, c);
		_btnImportWordList.addActionListener(_controller);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,0,40,5);
		add(_btnCreateWordList, c);
		_btnCreateWordList.addActionListener(_controller);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 6;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,0,40,10);
		add(_btnScoreHistory, c);
		_btnScoreHistory.addActionListener(_controller);

		return this;
	}

	
	/**
	 * Shows a pop up telling user that the selected game/level has no possible quiz words
	 * @param quizMode
	 * @param allEmpty
	 * @param level
	 */
	public void noAvailableWordsPopUp(String quizMode, boolean allEmpty, int level){

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

		JOptionPane.showMessageDialog(this, message, 
				"No Available Words", JOptionPane.INFORMATION_MESSAGE);

		showMainMenu();
	}

	/** shows the pop up window that allows user to select window
	 ** returns an integer that represents the level user has selected
	 ** 0 means the user has chosen to cancel
	 *
	 * Reference URL : http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html#input
	 */
	public int levelPopUp(String quizMode){

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

		int result = JOptionPane.showConfirmDialog(this, popUpPanel, "Choose level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

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
	
	public static void showMainMenu(){
		//SpellingAidApp.showCard(this, "Main Menu");
	}

	//freeze the frame so that user cannot interact while video is playing
	public static void disableMain(){
		//setEnabled(false);
	}

	public static void enableMain(){
		//setEnabled(true);
	}

}
