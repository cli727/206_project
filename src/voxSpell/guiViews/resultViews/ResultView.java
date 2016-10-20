package voxSpell.guiViews.resultViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import voxSpell.guiViews.Card;
import voxSpell.guiViews.VoxSpellGui;
import voxSpell.guiViews.quizViews.QuizView;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.models.resultModels.ResultModel;

public class ResultView extends JTableView implements Card, ActionListener {
	protected HiddenFilesModel _hiddenFilesModel;

	protected Vector<String> _allLevelNames;
	protected String _thisLevelName;
	protected String _courseName;

	private JLabel _labelHeading;
	private JLabel _labelSubheading;
	protected JLabel _labelQuizMode;

	protected JLabel _labelTableInfo;

	protected JButton _btnNextLevel;
	protected JButton _btnPracticeAgain;
	protected JButton _btnHome;

	protected int _numWordsToQuiz;

	protected ArrayList<String> _nextLevelWords;


	public ResultView(String levelName, String courseName, Vector<String> allLevelNames){
		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_allLevelNames = allLevelNames; // so that the card knows of the next level (if user presses the button)
		_thisLevelName = levelName;
		_courseName = courseName;

		_labelQuizMode = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Practice Completed!"+ "</font></html>"));
		_labelQuizMode.setFont((new Font("SansSerif", Font.ITALIC,30)));

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Course: " + _courseName + "</font></html>"));

		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,17));

		_labelSubheading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Subgroup : " + _thisLevelName + "</font></html>"));
		_labelSubheading.setFont((new Font("SansSerif", Font.ITALIC,17)));

		_tablePanel = new JPanel();

		_labelTableInfo = new JLabel("(Selected Items will be added to your revision list)");

		_btnPracticeAgain = new JButton("Practice Again");

		_btnNextLevel = new JButton("Practice Next Level");

		
		_nextLevelWords = _hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1));
		
		_numWordsToQuiz = 0;

		_btnHome = new JButton("Home");
	}

	@Override
	public JPanel createAndGetPanel() {
		//=================setting button tool tip
		
		//tell user information about next level when hovered over
		int nextLevelWordcount = _nextLevelWords.size();
		if(nextLevelWordcount < _model.getRowCount()){
			//use all words next level has, if it is not as much as this level's word count
			_numWordsToQuiz = nextLevelWordcount;
		}else{
			_numWordsToQuiz = _model.getRowCount();
		}
		_btnNextLevel.setToolTipText(_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1)+" ,"+_numWordsToQuiz + " Words");

		
		//===================================================================
		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);

		_tablePanel.setBackground(Color.white);

		//create result table from the set model
		_resultTable = new JTable(_model);
		_resultTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
		//_resultTable.setFillsViewportHeight(true);
		_tablePanel.add(_resultTable);
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_tablePanel.add(scrollPane, BorderLayout.NORTH);


		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelQuizMode, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(20,-85,0,0);
		resultPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelSubheading, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.insets = new Insets(20,0,0,0);
		resultPanel.add(_tablePanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 3;
		//c.weightx = 0.3
		c.insets = new Insets(0,60,0,0);
		resultPanel.add(_labelTableInfo, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(25,0,0,5);
		resultPanel.add(_btnPracticeAgain, c);
		_btnPracticeAgain.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(25,0,0,5);
		resultPanel.add(_btnNextLevel, c);
		_btnNextLevel.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(25,0,0,5);
		resultPanel.add(_btnHome, c);
		_btnHome.addActionListener(this);

		//check if the next level button needs to be disabled
		if (ifDisableNextLevel()){
			_btnNextLevel.setEnabled(false);
		}

		return resultPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//regardless of the button pressed, keep a record of the words to review
		((ResultModel) _model).keepRecordOfSelectedWords();

		if (e.getSource() == _btnPracticeAgain){
			//show quiz card of this level again

			QuizView quizView = new QuizView(_thisLevelName, _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			quizModel.setAllWords(_hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,_thisLevelName), _model.getRowCount()); //num of row in table is the same num as word to quiz

			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Practice Again Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnNextLevel){
			//this button is clicked so this button must not be disabled
			//show quizView of next level

			QuizView quizView = new QuizView(_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1), _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);


			quizModel.setAllWords(_nextLevelWords, _numWordsToQuiz); 

			//show next level quiz card
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Next Level Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}

	protected boolean ifDisableNextLevel(){

		if (_allLevelNames.indexOf(_thisLevelName) != (_allLevelNames.size()-1) ){
			// if not last item in list
			return false;
		}else {
			//disable this button
			return true;
		}
	}
}
