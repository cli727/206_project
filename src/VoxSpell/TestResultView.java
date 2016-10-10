package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TestResultView extends ResultView {
	
	private JButton _btnKeepGoing;
	private JButton _btnTestAgain;
	private JButton _btnVideoReward;
	private JButton _btnHome;

	public TestResultView(String levelName, String courseName, Vector<String> allLevelNames) {
		super(levelName, courseName, allLevelNames);
		
		_labelQuizMode.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Test Completed!"+ "</font></html>"));
		
		_labelSubheading.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Well Done" + "</font></html>"));
		
		_btnKeepGoing = new JButton("Continue Test");
		_btnTestAgain = new JButton("Try Again");
		_btnVideoReward = new JButton ("Video Reward");
		_btnHome = new JButton("Home");
	}
	
	@Override
	public JPanel createAndGetPanel() {

		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);

		_tabelPanel.setBackground(Color.white);

		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelQuizMode, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.insets = new Insets(20,-85,0,0);
		resultPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelSubheading, c);

		//create result table from the set model
		_resultTable = new JTable(_model);
		_resultTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
		//_resultTable.setFillsViewportHeight(true);
		_tabelPanel.add(_resultTable);
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_tabelPanel.add(scrollPane, BorderLayout.NORTH);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 4;
		c.gridheight = 3;
		c.insets = new Insets(20,0,0,0);
		resultPanel.add(_tabelPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 4;
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
		//c.weightx = 0.3;
		c.insets = new Insets(25,0,0,5);
		resultPanel.add(_btnRaceLevel, c);
		_btnRaceLevel.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
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
		_model.keepRecordOfSelectedWords();

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

			//get words for next level
			ArrayList<String> newWords = _hiddenFilesModel.getLevelWordsFromCourse("./.course/"+_courseName,_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1));

			QuizView quizView = new QuizView(_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1), _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			quizModel.setAllWords(newWords, _model.getRowCount()); //num of row in table is the same num as word to quiz

			//show next level quiz card
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Next Level Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnRaceLevel){


		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}

}
