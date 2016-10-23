package voxSpell.views.resultViews;

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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import voxSpell.videoPlayer.VideoPlayer;
import voxSpell.models.resultModels.TestResultModel;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.views.quizViews.QuizView;
import voxSpell.views.quizViews.TestQuizView;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.models.quizModels.TestQuizModel;

/**
 * View that shows the result at the end of a test quiz. It contains a JTable component. It is a Card Object.
 * @author chen
 *
 */
public class TestResultView extends JTableView implements ActionListener , Card{

	private HiddenFilesModel _hiddenFilesModel;

	private JButton _btnKeepGoing;
	private JButton _btnTestAgain;
	private JButton _btnVideoReward;
	private JButton _btnHome;

	private JLabel _labelScoreTitle;
	private JLabel _labelScore;

	private JLabel _labelResult;
	private JLabel _labelNewBest;

	private int _score;
	private JLabel _labelQuizMode;
	private String _courseName;
	private JLabel _labelHeading;



	public TestResultView( String courseName, int score) {

		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_courseName = courseName;
		_score = score;

		_labelQuizMode = new JLabel();

		_labelQuizMode.setFont(new Font("SansSerif", Font.ITALIC,30));

		_labelResult = new JLabel();

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

		_labelNewBest = new JLabel(" ");//only visible when user has created new best score
		_labelNewBest.setFont(new Font("SansSerif", Font.BOLD,18));

		if(_hiddenFilesModel.getHighScore(_courseName) < _score){
			//new best score
			_labelNewBest.setText("<html> <p style='text-align:center;'>"
					+ "<font color='green'>"
					+ "NEW PERSONAL BEST!"+"</font></html>");

			//set new best score
			_hiddenFilesModel.setNewScore(_courseName, _score);
		}

		ButtonFactory btnFactory = new ButtonFactory();
		
		_btnKeepGoing = btnFactory.getButton("./media/continue.png", "./media/continue_hover.png");
		_btnKeepGoing.setBackground(Color.white);
		
		_btnTestAgain = btnFactory.getButton("./media/retry.png", "./media/retry_hover.png");
		_btnTestAgain.setBackground(Color.white);
		
		_btnVideoReward = btnFactory.getButton("./media/video_reward.png", "./media/video_reward_hover.png");
		_btnVideoReward.setBackground(Color.white);
		
		_btnHome = btnFactory.getButton("./media/home.png", "./media/home_hover.png");
		_btnHome.setBackground(Color.white);
	}


	@Override
	public JPanel createAndGetPanel() {

		//create result table from the set model
		_resultTable = new JTable(_model);
		_resultTable.setPreferredScrollableViewportSize(new Dimension(500, 250));

		_tablePanel = new JPanel();
		_tablePanel.add(_resultTable);
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_tablePanel.add(scrollPane, BorderLayout.NORTH);
		_tablePanel.setBackground(Color.white);

		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);

		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		if (ifDisableNextLevel()){
			//failed
			_labelQuizMode.setText("<html> <p style='text-align:center;'>"
					+ "<font color='black'>"
					+  "Level failed..." + "</font></html>");

			_labelResult.setText("(You need to get at least 8 words correct to pass)");

			//disable 'next level', 'video reward' buttons
			_btnKeepGoing.setEnabled(false);
			_btnVideoReward.setEnabled(false);
		}else {
			//passed

			_labelQuizMode.setText("<html> <p style='text-align:center;'>"
					+ "<font color='black'>"
					+ "Level passed" + "</font></html>");
			_labelResult.setText("Continue test to update your personal best score!");
		}


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelQuizMode, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,80,0,0);
		resultPanel.add(_labelNewBest, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelHeading, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,-85,0,0);
		resultPanel.add(_labelResult ,c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,200,0,0);
		resultPanel.add(_labelScoreTitle, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(0,200,0,0);
		resultPanel.add(_labelScore, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 4;
		c.gridwidth = 4;
		//c.weightx = 0.3;
		c.insets = new Insets(5,0,0,5);
		resultPanel.add(_tablePanel, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(20,0,0,5);
		resultPanel.add(_btnTestAgain, c);
		_btnTestAgain.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(20,0,0,5);
		resultPanel.add(_btnKeepGoing, c);
		_btnKeepGoing.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(20,0,0,5);
		resultPanel.add(_btnVideoReward, c);
		_btnVideoReward.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(20,0,0,5);
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

			//playVideo, using ffmpeg to manipulate the video
			VideoPlayer mediaPlayer ;

			//play special bonus mark video with ffmpeg manipulations
			mediaPlayer = new VideoPlayer();	
			mediaPlayer.playVideo("./media/big_buck_bunny_1_minute.avi");

			//disable main GUI
			VoxSpellGui.getFrame().setEnabled(false);

		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}

	/**
	 * "Continue" button is disabled if the user has failed the test
	 * @return
	 */
	protected boolean ifDisableNextLevel(){
		//next 'level' is enabled if the user has gotten 8/10 words correct
		if (((TestResultModel) _model).getCorrectNumber() >= 8){

			return false;
		}else {
			//disable this button
			return true;
		}
	}
}
