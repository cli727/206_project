package VoxSpell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VoxSpellGui {

	//public final fields for determining quizMode for the game
	public static final String NEW = "New";
	public static final String REVIEW = "Review";

	private static JFrame _frame;

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

	private static JPanel _menuButtonsPanel = new JPanel();
	private static JPanel _cardsPanel = new JPanel();


	private static CardLayout _cardLayout = new CardLayout();
	/*	private static FlowLayout _flowLayout = new FlowLayout();

	private static JButton _newQuizButton = new JButton("New Spelling Quiz");
	private static JButton _reviewButton = new JButton("Review Mistakes");
	private static JButton _viewStatsButton = new JButton("View Statistics");*/


	public VoxSpellGui() {
		_frame = new JFrame("VOXSPELL");
		buildGUI(); //Instantiate GUI objects and build GUI;
	}

	private void buildGUI() {
		//Create the main menu card
		MainMenuView cardMainMenu = new MainMenuView();
		MainMenuModel mainMenuModel = new MainMenuModel();
		MainMenuController mainMenuController = new MainMenuController();
		
		mainMenuController.setViewModel(cardMainMenu, mainMenuModel);
		cardMainMenu.setController(mainMenuController);
		
		//Card cardQuizView = new QuizView();

		//Build the GUI section that has interchangeable components (cards)
		_cardsPanel.setLayout(_cardLayout);
		
		_cardsPanel.add(cardMainMenu.createAndGetPanel(), "Main Menu");
		//_cardsPanel.add(comp, constraints);
		
		_frame.getContentPane().add(_cardsPanel);

		//		//Build the GUI section with the menu buttons
		//		_menuButtonsPanel.setLayout(_flowLayout);
		//		_flowLayout.setAlignment(FlowLayout.CENTER); 
		//
		//		_menuButtonsPanel.add(_newQuizButton);
		//		_newQuizButton.addActionListener(this);
		//
		//		_menuButtonsPanel.add(_reviewButton);
		//		_reviewButton.addActionListener(this);
		//
		//		_menuButtonsPanel.add(_viewStatsButton);
		//		_viewStatsButton.addActionListener(this);
		//
		//		_menuButtonsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//		_frame.getContentPane().add(_menuButtonsPanel, BorderLayout.SOUTH);

		//begin with showing the main menu screen	
		showCard(cardMainMenu,"Main Menu");
	}

	public static void showCard(Card card, String cardName){
		//Show quiz panel accordingly
		//_cardsPanel.add((JPanel) card, cardName);
		_cardLayout.show(_cardsPanel,cardName);
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
