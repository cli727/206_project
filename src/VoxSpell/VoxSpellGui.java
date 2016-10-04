package VoxSpell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
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
import javax.swing.SwingUtilities;

import VoxSpell.VoxSpellGui.LEVEL;

public class VoxSpellGui implements Card,ActionListener{

	private static JFrame _frame;

	private JPanel _welcomePanel;
	private JLabel _welcomeLabel;
	private JButton _btnHowItWorks;
	private JButton _btnNewQuiz;
	private JButton _btnReview;
	private JButton _btnScoreHistory;
	
	private static JPanel _cardsPanel = new JPanel();
	private static CardLayout _cardLayout = new CardLayout();
/*	private static FlowLayout _flowLayout = new FlowLayout();*/
	private static HiddenFilesModel _hiddenFilesModel = HiddenFilesModel.getInstance();
	private static QuizModel quiz;
	protected static FestivalModel _festivalModel;
	private static String _currentCard;
	
	//public final fields for determining quizMode for the game
	public static final String NEW = "New";
	public static final String REVIEW = "Review";

	/**
	 * public enum class representing all possible levels
	 */
	public static enum LEVEL {
		ONE(1), TWO(2), THREE(3),FOUR(4),FIVE(5),SIX(6),SEVEN(7),EIGHT(8),NINE(9),TEN(10),ELEVEN(11);

		private final int _level;
		private LEVEL(int level) {
			_level = level;
		}

		public int getLevel() {
			return _level;
		}
	}


	public VoxSpellGui() {
		_frame = new JFrame("VoxSpell");
		buildGUI(); //Instantiate GUI objects and build GUI;
	}

	private void buildGUI() {
		//Create the cards
		JPanel cardMainMenu = createAndGetPanel();

		//header session that stays throughout all menus
		_welcomePanel = new JPanel();
		_welcomeLabel = new JLabel("SOME WELCOME MESSAGE HERE");
		_welcomePanel.add(_welcomeLabel);
		_frame.getContentPane().add(_welcomePanel, BorderLayout.NORTH);
		
		//Build the GUI section that has interchangeable components (cards)
		_cardsPanel.setLayout(_cardLayout);
		_cardsPanel.add( cardMainMenu, "Main Menu");

		_frame.getContentPane().add(_cardsPanel, BorderLayout.SOUTH);

		//begin with showing the main menu screen	
		showMainMenu(); 
	}



	@Override
	public JPanel createAndGetPanel() {

		/*add(new JLabel("<html> <p style='text-align: center;font-size:13px;padding:8;'>"
				+ " Welcome To VOXSPELL!</html>", 
				JLabel.CENTER),BorderLayout.NORTH);*/
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(new Color(47,145,195));

		ImageIcon water = new ImageIcon("image.png");
		_btnHowItWorks = new JButton("How It Works");
		_btnNewQuiz = new JButton("New Quiz");
		_btnReview = new JButton("Review Mistakes");
		_btnScoreHistory = new JButton("Score History");
		
		/**
		 * DECLARATION: THE FOLLOWING METHOD ON JAVA GRIDBAG LAYOUT ARE SOURCED 
		 * AND EDITED FROM THE ORACLE TUTORIAL WEBPAGE
		 * URL: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
		 */
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
/*
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 2;
		c.ipady = 100;
		c.insets = new Insets(5,5,5,5);
		mainPanel.add(_welcomePanel, c);	*/
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.ipady = 250;
		c.ipadx = 350;
		c.insets = new Insets(40,10,5,5);
		mainPanel.add(_btnNewQuiz, c);
		_btnNewQuiz.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(40,0,5,10);
		mainPanel.add(_btnReview, c);
		_btnReview.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,10,40,5);
		mainPanel.add(_btnScoreHistory, c);
		_btnScoreHistory.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,40,10);
		mainPanel.add(_btnHowItWorks, c);
		_btnHowItWorks.addActionListener(this);


/*
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.5;
		c.insets = new Insets(0,10,40,5);
		mainPanel.add(_btnImportWordList, c);
		_btnImportWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,0,40,5);
		mainPanel.add(_btnCreateWordList, c);
		_btnCreateWordList.addActionListener(this);*/

		return mainPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnScoreHistory) {
			new FullStatsView(_frame);
		}
		else if (e.getSource() == _btnNewQuiz || e.getSource() == _btnReview){

			String quizMode = null;
			String cardName = null;
			if (e.getSource() == _btnNewQuiz) {

				//show choose course card
				
				
				/*quizMode = VoxSpellGui.NEW;  
				cardName = "New Quiz";

				if (_hiddenFilesModel.allEmpty(VoxSpellGui.NEW)){	
					showNoAvailableWordsPopUp(VoxSpellGui.NEW,true,0);
					return;
				}*/
			} else {

				quizMode = VoxSpellGui.REVIEW; 
				cardName = "Review Mistakes";

				if (_hiddenFilesModel.allEmpty(VoxSpellGui.REVIEW)){
					showNoAvailableWordsPopUp(VoxSpellGui.REVIEW,true,0);
					return;
				}
			}

			//string variable indicating quiz mode
			int level = createAndShowLevelPopUp(quizMode); //get level value from the pop up window

			if (level != 0){

				if (e.getSource() == _btnNewQuiz) {

					quiz = new NewQuizModel();
				} else {

					quiz = new ReviewQuizModel();
				}

				if (_hiddenFilesModel.levelEmpty(level,quizMode)){
					showNoAvailableWordsPopUp(quizMode,false,level);
					return;
				}
				

				//create a new quiz card
				Card cardNewOrReviewQuiz = new QuizView(level); 

				//Set their model, view relationship using setters
				//Model and View share the same Festival Model
				quiz.setView((QuizView) cardNewOrReviewQuiz);
				quiz.setInitialLevel(level);
				((QuizView)cardNewOrReviewQuiz).setModel(quiz);
				_festivalModel = FestivalModel.getInstance();
				quiz.setFestivalModel(_festivalModel);
				((QuizView)cardNewOrReviewQuiz).setFestivalModel(_festivalModel);

				//Show quiz panel accordingly
				_cardsPanel.add(cardNewOrReviewQuiz.createAndGetPanel(), cardName);
				_cardLayout.show(_cardsPanel, e.getActionCommand());
				_currentCard = e.getActionCommand();

				//Generate quiz words using hiddenFilesModel, start quiz
				ArrayList<ArrayList<String>> allWords = _hiddenFilesModel.readFileToArray(quizMode);
				quiz.setAllWords(allWords);
				quiz.getRandomWords();
			}
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

		if (_currentCard.equals("New Spelling Quiz") || _currentCard.equals("Review Mistakes")){

			popUpPanel.add(new JLabel("<html>Warning: <BR>" + "<BR>" +
					"You have another quiz in progress. <BR>" + 
					"If you proceed, all of its progress <BR> will be lost! <BR>" + 
					"<BR>"+" Or <BR><BR> Press 'Cancel' to go back. <BR>" + "   </html>")
					,BorderLayout.NORTH);

			popUpPanel.add(new JLabel("-------------------------------------------------------- "),BorderLayout.CENTER);

		}

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

		int result = JOptionPane.showConfirmDialog(_frame, popUpPanel, "Choose level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

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

		int result = JOptionPane.showConfirmDialog(_frame, popUpPanel, "Choose level", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

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
		_cardLayout.show(_cardsPanel, "Main Menu");
		_currentCard = "Main Mennu";
	}

	//freeze the frame so that user cannot interact while video is playing
	public static void disableMain(){
		_frame.setEnabled(false);
	}

	public static void enableMain(){
		_frame.setEnabled(true);
	}

	/**
	 * copied from echa232_206_A2
	 */
	private static void createAndShowGUI() {
		//create and show GUI window
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.pack(); //Layout frame's components according to their preferred sizes.
		_frame.setLocationRelativeTo(null); //puts window at the center of screen
		_frame.setVisible(true);//Display the window
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VoxSpellGui();
				createAndShowGUI();
			}
		});
	}


}
