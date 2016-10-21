package voxSpell.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import voxSpell.models.festivalManager.FestivalModel;
import voxSpell.status.QuizStatus;
import voxSpell.views.levelViews.ChooseLevelView;
import voxSpell.views.courseViews.ChooseCourseView;
import voxSpell.views.courseViews.ImportWordListView;
import voxSpell.views.levelViews.ChooseLevelReviewView;

public class VoxSpellGui implements Card,ActionListener{

	private static VoxSpellGui _voxSpellGui;

	private static JFrame _frame;

	private BufferedImage _banner;
	private BufferedImage _footer;

	private static JPanel _welcomePanel;
	private JButton _btnPracticeQuiz;
	private JButton _btnTestQuiz;
	private JButton _btnReview;
	private JButton _btnTestScore;


	private static JPanel _footerPanel;

	private static JPanel _cardsPanel = new JPanel();
	private static CardLayout _cardLayout = new CardLayout();
	protected static FestivalModel _festivalModel;

	//public final fields for determining quizMode for the game
	public static QuizStatus STATUS = null;

	/**
	 * Singleton class, deals with card handling
	 */
	private VoxSpellGui() {
		_frame = new JFrame("VoxSpell");
		buildGUI(); //Instantiate GUI objects and build GUI;
	}

	private void buildGUI() {

		//header session that stays throughout all menus
		try {
			_banner = ImageIO.read(new File("./media/banner-01.png"));
			_footer = ImageIO.read(new File("./media/footer-02.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

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
				g.drawImage(_footer, 0, 0,910,60, null);
			}
		};


		_frame.getContentPane().setBackground(Color.white);

		//Create the static cards, from singleton classes
		JPanel cardMainMenu = createAndGetPanel();
		JPanel cardImportCourse = ImportWordListView.getInstance().createAndGetPanel();

		//Build the GUI section that has interchangeable components (cards)
		_cardsPanel.setLayout(_cardLayout);
		_cardsPanel.add( cardMainMenu, "Main Menu");
		_cardsPanel.add(cardImportCourse, "Import Wordlist");

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
		c.ipady = 50;
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
		
		//set button image
		final ImageIcon practiceImg = new ImageIcon("./media/practice.png");
		final ImageIcon practiceHover = new ImageIcon("./media/practice_hover.png");
		_btnPracticeQuiz = new JButton(practiceImg);
		Dimension size = new Dimension(practiceImg.getIconWidth(), practiceImg.getIconHeight());
		_btnPracticeQuiz.setPreferredSize(size);
		_btnPracticeQuiz.setBackground(Color.white);
		_btnPracticeQuiz.setBorderPainted(false);
		
		_btnPracticeQuiz.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnPracticeQuiz.setIcon( practiceHover);
				
				}else{
					_btnPracticeQuiz.setIcon(practiceImg);
				}
			}
		});

		final ImageIcon testImg = new ImageIcon("./media/test.png");
		final ImageIcon testHover = new ImageIcon("./media/test_hover.png");
		_btnTestQuiz  = new JButton( testImg );
		_btnTestQuiz .setPreferredSize(size);
		_btnTestQuiz .setBackground(Color.white);
		_btnTestQuiz.setBorderPainted(false);
		
		_btnTestQuiz.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnTestQuiz.setIcon(testHover);
				
				}else{
					_btnTestQuiz.setIcon( testImg);
				}
			}
		});

		
		final ImageIcon reviewImg = new ImageIcon("./media/review.png");
		final ImageIcon reviewHover= new ImageIcon("./media/review_hover.png");
		_btnReview = new JButton( reviewImg);
		_btnReview.setPreferredSize(size);
		_btnReview.setBackground(Color.white);
		_btnReview.setBorderPainted(false);

		_btnReview.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnReview.setIcon(reviewHover);
				
				}else{
					_btnReview.setIcon( reviewImg );
				}
			}
		});

		final ImageIcon scoreImg = new ImageIcon("./media/score.png");
		final ImageIcon scoreHover = new ImageIcon("./media/score_hover.png");
		_btnTestScore = new JButton(scoreImg);
		_btnTestScore.setPreferredSize(size);
		_btnTestScore.setBackground(Color.white);
		_btnTestScore.setBorderPainted(false);
		
		_btnTestScore.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnTestScore.setIcon( scoreHover);
				
				}else{
					_btnTestScore.setIcon(scoreImg);
				}
			}
		});


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
		//c.weightx = 0.3;S
		/*c.ipady = 200;
		c.ipadx = 300;*/
		c.insets = new Insets(0,50,5,5);
		mainPanel.add(_btnPracticeQuiz, c);
		_btnPracticeQuiz.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,5,50);
		mainPanel.add(_btnTestQuiz, c);
		_btnTestQuiz.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(0,50,5,5);
		mainPanel.add(_btnReview, c);
		_btnReview.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,0,5,50);
		mainPanel.add(_btnTestScore, c);
		_btnTestScore.addActionListener(this);

		return mainPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnTestScore) {

			STATUS = QuizStatus.SCORE;

			//show choose course card, so that the score of one course can be shown
			showCourseChooser(null); // does not need previous course name
			
		}else if (e.getSource() == _btnPracticeQuiz){
			STATUS = QuizStatus.NEW;

			//show card to select number of words / levels(headings)
			ChooseLevelView cardChooseLevel = new ChooseLevelView("KET"); //KET is the default course
			showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

		}else if (e.getSource() == _btnReview){
			STATUS = QuizStatus.REVIEW;
			//ChooseLevelReviewView object instead of ChooseLevelView
			ChooseLevelView cardChooseLevel = new ChooseLevelReviewView("KET"); //default course to review
			showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

		}else if (e.getSource() == _btnTestQuiz){
			STATUS = QuizStatus.TEST;

			//since no level chooser is needed for test mode (all levels included), just should course chooser
			showCourseChooser(null);//does not need previous course name
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
	}

	public static void showMainMenu(){
		setHeaderFooterColor(Color.white); //always reset header/footer color back to white when showing card
		_cardLayout.show(_cardsPanel, "Main Menu");
	}


	public static void showCourseChooser(String courseName){
		ChooseCourseView chooseCourseView = new ChooseCourseView(courseName);

		JPanel cardChooseCourse = chooseCourseView.createAndGetPanel();
		_cardsPanel.add(cardChooseCourse, "Choose Course");
		_cardLayout.show(_cardsPanel, "Choose Course");
		//change main menu footer/header background color so that it is consistent with this background color
		setHeaderFooterColor(new Color(17,103,172));
	}


	public static void showImportWordListView(){
		_cardLayout.show(_cardsPanel, "Import Wordlist");
		//change main menu footer/header background color so that it is consistent with this background color
		setHeaderFooterColor(Color.white);
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
