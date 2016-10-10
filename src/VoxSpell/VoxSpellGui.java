package VoxSpell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class VoxSpellGui implements Card,ActionListener{

	private static VoxSpellGui _voxSpellGui;

	private static JFrame _frame;

	private BufferedImage _banner;
	private BufferedImage _footer;

	private static JPanel _welcomePanel;
	private JLabel _welcomeLabel;
	private JButton _btnPracticeQuiz;
	private JButton _btnTestQuiz;
	private JButton _btnReview;
	private JButton _btnScoreHistory;


	private static JPanel _footerPanel;

	private static JPanel _cardsPanel = new JPanel();
	private static CardLayout _cardLayout = new CardLayout();
	protected static FestivalModel _festivalModel;
	private static String _currentCard;

	//public final fields for determining quizMode for the game
	public static String STATUS = null;
	public static final String NEW = "New";
	public static final String REVIEW = "Review";
	public static final String TEST = "Test";
	/**
	 * Singleton class, deals with card handling
	 */
	private VoxSpellGui() {
		_frame = new JFrame("VoxSpell");
		buildGUI(); //Instantiate GUI objects and build GUI;
	}

	private void buildGUI() {
		//Create the static cards
		JPanel cardMainMenu = createAndGetPanel();
		JPanel cardChooseCourse = ChooseCourseView.getInstance().createAndGetPanel();

		//header session that stays throughout all menus
		try {
			_banner = ImageIO.read(new File("./banner-01.png"));
			_footer = ImageIO.read(new File("./footer-02.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*ImageIcon banner = new ImageIcon("./banner.jpg");
		_welcomeLabel = new JLabel(banner);
		Dimension size = new Dimension(banner.getIconWidth(), banner.getIconHeight());
		_welcomeLabel.setPreferredSize(size);

		_welcomePanel.add(_welcomeLabel);*/

		_frame.getContentPane().setBackground(Color.white);

		//Build the GUI section that has interchangeable components (cards)
		_cardsPanel.setLayout(_cardLayout);
		_cardsPanel.add( cardMainMenu, "Main Menu");
		_cardsPanel.add(cardChooseCourse, "Choose Course");

		_welcomePanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(_banner, 0, 0,946,100, null);
			}
		};

		_footerPanel =new JPanel(){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(_footer, 0, 0,946,60, null);
			}
		};
		
		//_frame.setContentPane(_welcomePanel);
		
		_frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		/*
		 * USE WELCOME PANE AS BACKGROUND, ONLY ADD ONE CARD TO GRID BAG LAYOUT
		 */

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 80;
		c.weighty = 0.1;
		c.insets = new Insets(0,0,0,0);
		_frame.add(_cardsPanel,c);	

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = 100;
		//c.gridheight = 1;
		//c.weighty = 0;
		c.insets = new Insets(0,0,0,0);
		_frame.add(_welcomePanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 60;
		c.insets = new Insets(0,0,0,0);
		_frame.add(_footerPanel,c);	

		//begin with showing the main menu screen	
		showMainMenu(); 
	}


	public static synchronized VoxSpellGui getInstance(){
		return _voxSpellGui;
	}

	@Override
	public JPanel createAndGetPanel() {

		/*add(new JLabel("<html> <p style='text-align: center;font-size:13px;padding:8;'>"
				+ " Welcome To VOXSPELL!</html>", 
				JLabel.CENTER),BorderLayout.NORTH);*/
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(Color.WHITE);

		_btnPracticeQuiz = new JButton("Practice Words");
		_btnTestQuiz = new JButton("Test Mode");
		_btnReview = new JButton("Review Mistakes");
		_btnScoreHistory = new JButton("Score History");

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
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.ipady = 200;
		c.ipadx = 300;
		c.insets = new Insets(0,10,5,5);
		mainPanel.add(_btnPracticeQuiz, c);
		_btnPracticeQuiz.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,5,10);
		mainPanel.add(_btnTestQuiz, c);
		_btnTestQuiz.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(0,10,5,5);
		mainPanel.add(_btnReview, c);
		_btnReview.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,0,5,10);
		mainPanel.add(_btnScoreHistory, c);
		_btnScoreHistory.addActionListener(this);

		return mainPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnScoreHistory) {

			new FullStatsView(_frame);
		}else if (e.getSource() == _btnPracticeQuiz){
			STATUS = NEW;

			//show card to select number of words / levels(headings)
			ChooseLevelView cardChooseLevel = new ChooseLevelView("KEY"); //KEY is the default course
			VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

		}else if (e.getSource() == _btnReview){
			STATUS = REVIEW;
			//ChooseLevelReviewView object instead of ChooseLevelView
			ChooseLevelView cardChooseLevel = new ChooseLevelReviewView("KEY"); //default course to review
			VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
			
		}else if (e.getSource() == _btnTestQuiz){
			STATUS = TEST;
			
			//since no level chooser is needed for test mode (all levels included), just should course chooser
			showCourseChooser();
		}
	}

	/**
	 * Public method for to show any Card object
	 * @param cardPanel
	 * @param cardName
	 */
	public void showCard (JPanel cardPanel, String cardName){
		//show non static card object
		_cardsPanel.add(cardPanel, cardName);
		_cardLayout.show(_cardsPanel, cardName);
		_currentCard = cardName;

	}

	public static void showMainMenu(){
		setHeaderFooterColor(Color.white); //always reset header/footer color back to white when showing card
		_cardLayout.show(_cardsPanel, "Main Menu");
		_currentCard = "Main Menu";
	}


	public static void showCourseChooser(){
		_cardLayout.show(_cardsPanel, "Choose Course");
		_currentCard = "Choose Course";
	}
	
	public static void setHeaderFooterColor(Color color){
		_welcomePanel.setBackground(color);
		_footerPanel.setBackground(color);
	}

	//freeze the frame so that user cannot interact while video is playing
	public static void disableMain(){
		_frame.setEnabled(false);
	}

	public static void enableMain(){
		_frame.setEnabled(true);
	}

	public static JFrame getFrame() {
		return _frame;
	}

	/**
	 * copied from echa232_206_A2
	 */
	private static void createAndShowGUI() {
		//create and show GUI window
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//_frame.setResizable(false);
		_frame.pack(); //Layout frame's components according to their preferred sizes.
		_frame.setLocationRelativeTo(null); //puts window at the center of screen
		_frame.setVisible(true);//Display the window
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_voxSpellGui = new VoxSpellGui();
				createAndShowGUI();
			}
		});
	}
}
