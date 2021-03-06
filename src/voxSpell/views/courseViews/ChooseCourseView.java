package voxSpell.views.courseViews;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import voxSpell.views.quizViews.QuizView;
import voxSpell.views.levelViews.ChooseLevelReviewView;
import voxSpell.views.resultViews.TestScoreView;
import voxSpell.models.resultModels.TestScoreModel;
import voxSpell.status.QuizStatus;
import voxSpell.views.quizViews.TestQuizView;
import voxSpell.models.quizModels.TestQuizModel;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.views.levelViews.ChooseLevelView;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
/**
 * Singleton class. Showing the window for choosing vocabulary courses. It is a Card object.
 * @author chen
 *
 */

public class ChooseCourseView implements Card, ActionListener{

	private Color _bgColor;
	private Color _hoverColor;
	private JLabel _labelHeading;
	private JButton _btnKETwords;
	private JButton _btnIELTSwords;
	private JButton _btnImportWordList;
	private JButton _btnViewImportedWordList;
	private JButton _btnBack;

	private String _courseNameToGoBackTo;

	private HiddenFilesModel _hiddenFilesModel;

	public ChooseCourseView(String courseName){
		_courseNameToGoBackTo = courseName;

		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_bgColor = new Color(17,103,172);
		_hoverColor =  new Color(0,137,249);
		//create new Font
		Font headingFont = new Font("SansSerif", Font.ITALIC,30);

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ "Select your course...</font></html>"));

		_labelHeading.setFont(headingFont);

		/**
		 * CODE FOR CHANGING BUTTON BACKGROUND WHEN HOVERED OVER SOURCED FROM STACK OVERFLOW:
		 * http://stackoverflow.com/questions/18574375/jbutton-with-background-image-changing-on-mouse-hover
		 */
		//set button image
		final ImageIcon ketImg = new ImageIcon("./media/course-01.png");
		final ImageIcon ketHoverImg = new ImageIcon("./media/course-02.png");
		_btnKETwords = new JButton(ketImg);
		Dimension size = new Dimension(ketImg.getIconWidth(), ketImg.getIconHeight());
		_btnKETwords.setPreferredSize(size);
		_btnKETwords.setBackground(_bgColor);
		_btnKETwords.setBorderPainted(false);
		_btnKETwords.setFocusable(false);

		_btnKETwords.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnKETwords.setIcon(ketHoverImg);
					_btnKETwords.setBackground(_hoverColor);

				}else{
					_btnKETwords.setIcon(ketImg);
					_btnKETwords.setBackground(_bgColor);
				}
			}
		});

		final ImageIcon IELTSImg = new ImageIcon("./media/course-03.png");
		final ImageIcon IELTSHover = new ImageIcon("./media/course-04.png");
		_btnIELTSwords = new JButton(IELTSImg);
		_btnIELTSwords.setPreferredSize(size);
		_btnIELTSwords.setBackground(_bgColor);
		_btnIELTSwords.setBorderPainted(false);
		_btnIELTSwords.setFocusable(false);

		_btnIELTSwords.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnIELTSwords.setIcon( IELTSHover);
					_btnIELTSwords.setBackground(_hoverColor);

				}else{
					_btnIELTSwords.setIcon(IELTSImg);
					_btnIELTSwords.setBackground(_bgColor);
				}
			}
		});

		final ImageIcon myCourse = new ImageIcon("./media/course-07.png");
		_btnImportWordList = new JButton(myCourse);
		_btnImportWordList.setPreferredSize(size);
		_btnImportWordList.setBackground(_bgColor);
		_btnImportWordList.setBorderPainted(false);
		_btnImportWordList.setFocusable(false);

		_btnImportWordList.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					_btnImportWordList.setBackground(_hoverColor);

				}else{
					_btnImportWordList.setBackground(_bgColor);
				}
			}
		});

		final ImageIcon newCourse = new ImageIcon("./media/course-05.png");
		_btnViewImportedWordList = new JButton(newCourse);
		_btnViewImportedWordList.setPreferredSize(size);
		_btnViewImportedWordList.setBackground(_bgColor);
		_btnViewImportedWordList.setBorderPainted(false);
		_btnViewImportedWordList.setFocusable(false);

		_btnViewImportedWordList.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					_btnViewImportedWordList.setBackground(_hoverColor);
				}else{
					_btnViewImportedWordList.setBackground(_bgColor);
				}
			}
		});
		
		ButtonFactory btnFactory = new ButtonFactory();
		_btnBack = btnFactory.getButton("./media/back.png", "./media/back_hover.png");
		_btnBack.setBackground(_bgColor);
	}

	@Override
	public JPanel createAndGetPanel() {
	
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(_bgColor);
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
		c.gridwidth = 6;
		c.gridheight = 2;
		//c.weightx = 0.3;
		
		c.insets = new Insets(0,0,0,0);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		c.insets = new Insets(50,20,10,15);
		mainPanel.add(_btnKETwords, c);
		_btnKETwords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(50,0,10,20);
		mainPanel.add(_btnIELTSwords, c);
		_btnIELTSwords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,20,0,15);
		mainPanel.add(_btnViewImportedWordList, c);
		_btnViewImportedWordList.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,0,20);
		mainPanel.add(_btnImportWordList, c);
		_btnImportWordList.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(20,250,0,0);
		mainPanel.add(_btnBack, c);
		_btnBack.addActionListener(this);



		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBack){

			//decide which card to go back to based on quiz mode

			if (VoxSpellGui.STATUS.equals(QuizStatus.TEST) || VoxSpellGui.STATUS.equals(QuizStatus.SCORE)){
				VoxSpellGui.showMainMenu();
			}else if (VoxSpellGui.STATUS.equals(QuizStatus.NEW)){

				//show card to select number of words / levels(headings)
				ChooseLevelView cardChooseLevel = new ChooseLevelView(_courseNameToGoBackTo); 
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

			}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){

				//ChooseLevelReviewView object instead of ChooseLevelView
				ChooseLevelView cardChooseLevel = new ChooseLevelReviewView(_courseNameToGoBackTo); 
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

			}

		}else if (e.getSource() == _btnImportWordList){

			VoxSpellGui.showImportWordListView();

		}else if (e.getSource() == _btnViewImportedWordList){

			ShowImportedWordListView importedWordListView = new ShowImportedWordListView();
			VoxSpellGui.getInstance().showCard(importedWordListView.createAndGetPanel(), "Show Imported WordList");
			VoxSpellGui.setHeaderFooterColor(Color.white);

		}else if (e.getSource() == _btnKETwords){

			if (VoxSpellGui.STATUS.equals(QuizStatus.SCORE)){

				//show stats view
				TestScoreView testScoreView = new TestScoreView("KET",  _hiddenFilesModel.getHighScore("KET"));
				TestScoreModel testScoreModel = new TestScoreModel("KET");

				testScoreView.setModel(testScoreModel);

				VoxSpellGui.getInstance().showCard(testScoreView.createAndGetPanel(), "Test Scores");
				VoxSpellGui.setHeaderFooterColor(Color.white);

			}else if (VoxSpellGui.STATUS.equals(QuizStatus.TEST)){
				//test view

				QuizView quizView = new TestQuizView(null,"KET", 0);//level not needed for test view
				QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/KET"));

				quizModel.setView(quizView);

				ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/KET");
				for(int i = 0; i < allWords.size(); i++){
					if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
						allWords.remove(allWords.get(i));
					}
				}

				//just 10 words always for test mode
				//if the entire course does not have 10 words, just get all of them

				int numWordsToQuiz = 10;

				if(allWords.size() < 10){
					numWordsToQuiz = allWords.size();
				}
				quizModel.setAllWords(allWords, numWordsToQuiz); 

				quizView.setModel(quizModel);
				VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
				quizModel.getRandomWords();

			}else{
				//show card to select number of words / levels(headings)
				ChooseLevelView cardChooseLevel = null;

				if(VoxSpellGui.STATUS.equals(QuizStatus.NEW)){

					cardChooseLevel = new ChooseLevelView("KET");
				}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){

					cardChooseLevel = new ChooseLevelReviewView("KET");
				}

				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
			}

		}else if (e.getSource() == _btnIELTSwords){
			if (VoxSpellGui.STATUS.equals(QuizStatus.SCORE)){
				//show stats view
				TestScoreView testScoreView = new TestScoreView("IELTS",  _hiddenFilesModel.getHighScore("IELTS"));
				TestScoreModel testScoreModel = new TestScoreModel("IELTS");

				testScoreView.setModel(testScoreModel);

				VoxSpellGui.getInstance().showCard(testScoreView.createAndGetPanel(), "Test Scores");
				VoxSpellGui.setHeaderFooterColor(Color.white);

			}else if(VoxSpellGui.STATUS.equals(QuizStatus.TEST)){
				//test view
				QuizView quizView = new TestQuizView(null,"IELTS",0);//level not needed for test view
				QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/IELTS"));

				quizModel.setView(quizView);

				ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/IELTS");
				for(int i = 0; i < allWords.size(); i++){
					if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
						allWords.remove(allWords.get(i));
					}
				}

				//get 10 words for test, all words in course if the course has less than 10 words
				int numWordsToQuiz = 10;

				if(allWords.size() < 10){
					numWordsToQuiz = allWords.size();
				}
				quizModel.setAllWords(allWords, numWordsToQuiz); 

				quizView.setModel(quizModel);
				VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
				quizModel.getRandomWords();

			}else{
				// not test view
				//show card to select number of words / levels(headings)
				ChooseLevelView cardChooseLevel = null;

				if(VoxSpellGui.STATUS.equals(QuizStatus.NEW)){

					cardChooseLevel = new ChooseLevelView("IELTS");
				}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){

					cardChooseLevel = new ChooseLevelReviewView("IELTS");
				}

				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
			}
		}
	}

}


