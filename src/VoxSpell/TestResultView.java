package VoxSpell;

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

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TestResultView implements ActionListener , Card{
	
	private HiddenFilesModel _hiddenFilesModel;

	private JButton _btnKeepGoing;
	private JButton _btnTestAgain;
	private JButton _btnVideoReward;
	private JButton _btnHome;

	private JLabel _labelScoreTitle;
	private JLabel _labelScore;

	private JLabel _labelTestRecorded;
	private JLabel _labelVideoReward;
	private JLabel _labelKeepGoing;


	private int _score;
	private JLabel _labelQuizMode;
	private JLabel _labelSubheading;
	private String _courseName;
	private JLabel _labelHeading;

	public TestResultView( String courseName,int score) {
		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_courseName = courseName;
		_score = score;

		_labelQuizMode = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Test Completed!"+ "</font></html>"));

		_labelQuizMode.setFont(new Font("SansSerif", Font.ITALIC,30));

		_labelSubheading = new JLabel (("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Score: " + "</font></html>"));

		_labelSubheading.setFont(new Font("SansSerif", Font.ITALIC,17));

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Course: " + _courseName + "</font></html>"));

		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,17));

		_labelScoreTitle  = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Score "+"</font></html>"));

		_labelScoreTitle.setFont(new Font("SansSerif", Font.ITALIC,20));

		_labelScore= new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ _score +"</font></html>"));

		_labelScore.setFont(new Font("SansSerif", Font.ITALIC,45));
		
		
		//======================================================================
		
		
		_labelTestRecorded= new JLabel("Good Effort, this test is recorded (View Test Score)");
		_labelTestRecorded.setFont(new Font("SansSerif", Font.ITALIC,15));;
		
		_labelVideoReward= new JLabel("You can play your video reward now!");
		_labelVideoReward.setFont(new Font("SansSerif", Font.ITALIC,15));;
		
		_labelKeepGoing= new JLabel("Continue test to update your personal score!");
		_labelKeepGoing.setFont(new Font("SansSerif", Font.ITALIC,15));;
		
		//=======================================================================

		_btnKeepGoing = new JButton("Continue Test");
		_btnTestAgain = new JButton("Try Again");
		_btnVideoReward = new JButton ("Video Reward");
		_btnHome = new JButton("Home");
	}


	@Override
	public JPanel createAndGetPanel() {

		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);


		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelQuizMode, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,0,0,0);
		resultPanel.add(_labelSubheading, c);

		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,0,0,0);
		resultPanel.add(_labelScore, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 2;
		c.gridwidth = 4;
		//c.weightx = 0.3
		c.insets = new Insets(0,25,0,0);
		resultPanel.add(_labelTestRecorded, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = 2;
		c.gridwidth = 4;
		//c.weightx = 0.3;
		c.insets = new Insets(5,25,0,5);
		resultPanel.add(_labelVideoReward, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.gridheight = 4;
		//c.weightx = 0.3;
		c.insets = new Insets(5,25,0,5);
		resultPanel.add(_labelKeepGoing, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(150,-55,0,5);
		resultPanel.add(_btnKeepGoing, c);
		_btnKeepGoing.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(150,0,0,5);
		resultPanel.add(_btnTestAgain, c);
		_btnTestAgain.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(150,0,0,5);
		resultPanel.add(_btnVideoReward, c);
		_btnVideoReward.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 9;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(150,0,0,5);
		resultPanel.add(_btnHome, c);
		_btnHome.addActionListener(this);


		return resultPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	

		if (e.getSource() == _btnTestAgain){
			//show quiz card of this level again, CLEAR SCORE

			QuizView quizView = new TestQuizView(null,_courseName, 0);//level not needed for test view
			QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/"+_courseName));

			quizModel.setView(quizView);

			ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/"+_courseName);
			for(int i = 0; i < allWords.size(); i++){
				if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
					allWords.remove(allWords.get(i));
				}
			}
			quizModel.setAllWords(allWords, 10); //just 10 words always for test mode

			quizView.setModel(quizModel);
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnKeepGoing){
			//show quiz card of this level again, KEEP SCORE

			QuizView quizView = new TestQuizView(null,_courseName, _score);//level not needed for test view
			QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/"+_courseName));

			quizModel.setView(quizView);

			ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/"+_courseName);
			for(int i = 0; i < allWords.size(); i++){
				if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
					allWords.remove(allWords.get(i));
				}
			}
			quizModel.setAllWords(allWords, 10); //just 10 words always for test mode

			quizView.setModel(quizModel);
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
			quizModel.getRandomWords();
		}else if (e.getSource() == _btnVideoReward){


		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}

}
