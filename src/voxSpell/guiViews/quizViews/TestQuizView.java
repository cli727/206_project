package voxSpell.guiViews.quizViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import voxSpell.audioPlayer.AudioPlayer;
import voxSpell.guiViews.VoxSpellGui;
import voxSpell.models.festivalManager.FestivalModel;
import voxSpell.models.festivalManager.FestivalModel.Voice;

public class TestQuizView extends QuizView{

	private JProgressBar _timerBar;
	private Timer _timer;

	private JPanel _scorePanel;
	private JLabel _scoreTitle;
	private JLabel _updateScore;

	private JLabel _labelFeedBack;

	private int _counter;
	private boolean _finished = false;

	public TestQuizView(String level, String courseName, int score) {
		super(level, courseName);

		_labelSubheading.setText("Testing all levels");

		_counter = 20;
		_timerBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 20);
		_timerBar.setValue(20);

		_timer = new Timer(1000, this);

		_scorePanel = new JPanel();
		_scorePanel.setBackground(Color.white);

		_labelFeedBack = new JLabel("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ " Correct!"+"</font></html>"); //text depends on whether user gets a word correct or not

		_scoreTitle = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Score "+"</font></html>"));

		_scoreTitle.setFont(new Font("SansSerif", Font.ITALIC,25));

		_updateScore = new JLabel(Integer.toString(score));

		_updateScore.setFont(new Font("SansSerif", Font.ITALIC,45));

	}

	@Override
	public JPanel createAndGetPanel() {

		setBackground(Color.white);

		_cb.setEditable(false);
		//If it's the very first game in the session, default voice is American voice
		//Otherwise (e.g. selected a new level in the middle of a game), selected voice remains the same as the previous game's voice selection.
		if (FestivalModel._currentVoice == Voice.AMERICAN) {
			_cb.setSelectedItem("American (default)");
		}
		else {
			_cb.setSelectedItem("New Zealand");
		}
		_cb.addItemListener(_festivalModel);

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
		c.gridwidth = 4;
		c.gridheight = 1;
		add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.insets = new Insets(5,0,50,0);
		add(_labelSubheading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 4;
		c.ipadx = 80;
		c.insets = new Insets(25,0,20,0);
		add(_timerBar, c);

		_scorePanel.add(_scoreTitle,BorderLayout.EAST);
		_scorePanel.add(_updateScore, BorderLayout.SOUTH);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.ipadx = 0;
		//c.weightx = 0.3;
		c.insets = new Insets(-70,120,5,0);
		add(_scoreTitle, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 3;
		c.gridheight = 2;
		c.gridwidth = 2;
		//c.weightx = 0.3;
		c.insets = new Insets(-60,120,5,0);
		add(_updateScore, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		c.ipadx = 0;
		c.ipady = 0;
		c.insets = new Insets(25,0,20,0);
		add(_updateWordPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.ipadx = 0;
		//c.weightx = 0.3;
		c.insets = new Insets(10,240,0,0);
		add(_labelFeedBack, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.ipadx = 30;
		c.insets = new Insets(0,35,10,0);
		add(_btnRelisten, c);
		_btnRelisten.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 6;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.ipadx = 320;
		c.insets = new Insets(0,5,10,0);
		add(_inputArea, c);
		_inputArea.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 4;
		c.gridy = 6;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.ipadx = 0;
		//c.weightx = 0.7;
		c.insets = new Insets(0,5,10,0);
		add(_tipsLabel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(10,35,60,5);
		add(_cb, c);
		//add item listener

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(10,180,60,0);
		add(_btnCheckWord, c);
		_btnCheckWord.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 8;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		c.insets = new Insets(10,65,0,0);
		add(_btnBack, c);
		_btnBack.addActionListener(this);

		_timer.start();

		return this;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		//with the addition of timer listener
		if (e.getSource() == _timer){
			//System.out.println(_counter);
			_counter--;
			_timerBar.setValue(_counter);

			if (!_finished){

				_labelFeedBack.setText("<html> <p style='text-align:center;'>"
						+ "<font color='white'>"
						+ " Correct!"+"</font></html>");//so that feedback label shows up for 1 second only

				if (_counter<1) {
					//time up, fail this word and move on to next word
					
					//_festivalModel.failedVoice();
					AudioPlayer audioPlayer = new AudioPlayer();
					audioPlayer.playAudio("./incorrect.wav");
					
					_labelFeedBack.setText("<html> <p style='text-align:center;'>"
							+ "<font color=red'>"
							+ "Incorrect!"+"</font></html>");
					
					_quizModel.moveOnToNextWord();
				} 
			}else{
				_timer.stop();
			}
		}
	}


	//need to pause timer when this shows up
	@Override
	protected void gameInProgressPopUp(){
		_timer.stop();

		JPanel popUpPanel = new JPanel();

		int dialogResult  = JOptionPane.showOptionDialog(popUpPanel, 
				("<html>Warning: <BR>" + "<BR>" +
						"You have a quiz in progress. <BR>" + 
						"Are you sure you want to leave? <BR>" + 
						"   </html>"),
				"Quiz In Progress", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				new String[]{"Stop", "Resume"}, // this is the array
				"default");

		if(dialogResult == JOptionPane.YES_OPTION){


			VoxSpellGui.showMainMenu();

		}else{
			_timer.start();
		}

	}

	//need to stop timer
	@Override
	public void showInvalidInputPopUp(){
		_timer.stop();
		JOptionPane.showMessageDialog(this, "Non alphabetical character(s) detected! \n"
				+ "Make sure you do not have unintended white spaces.", 
				"Warning: Invalid Input", JOptionPane.INFORMATION_MESSAGE);
		_timer.start();
	}

	public void resetTimer() {
		_timer.stop();
		_timerBar.setValue(20);
		_counter = 21;//reset countera bit longer so that festival cans speak its word
		_timer.start();
	}

	/**
	 * view does not know when quiz has ended so model will help stop the timer
	 */
	public void stopTimer(){
		_finished  = true;
	}
	/**
	 * method for its model to get timer's value, so that model can allocate a score
	 */
	public int getTimerValue(){
		return _counter;
	}

	public void updateScore(int addMarks){
		int score = Integer.parseInt(_updateScore.getText()); //get current score

		score = score + addMarks;
		_updateScore.setText(Integer.toString(score));
	}

	public void updateFeedback(boolean correct){
		if (correct){
			_labelFeedBack.setText(("<html> <p style='text-align:center;'>"
					+ "<font color='green'>"
					+ " Correct!"+"</font></html>"));

		}else {
			_labelFeedBack.setText(("<html> <p style='text-align:center;'>"
					+ "<font color='red'>"
					+ "Incorrect"+"</font></html>"));
		}
	}

	public int getScore() {
		return Integer.parseInt(_updateScore.getText()); //get current score
	}
}
