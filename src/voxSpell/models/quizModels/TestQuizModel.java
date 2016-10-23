package voxSpell.models.quizModels;

import java.util.Vector;

import voxSpell.views.resultViews.TestResultView;
import voxSpell.models.resultModels.TestResultModel;
import voxSpell.audioPlayer.AudioPlayer;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.views.quizViews.TestQuizView;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;

/**
 * A child class of QuizModel that manages the logic for test quizzes specifically
 * @author chen
 *
 */
public class TestQuizModel extends QuizModel{
	private HiddenFilesModel _hiddenFilesModel;

	public TestQuizModel(Vector<String> allLevelNames) {
		super(allLevelNames);

		_hiddenFilesModel = HiddenFilesModel.getInstance();
	}

	/**
	 * Check spelling needs the functionality to write stats when checking spelling for test mode
	 */
	@Override
	public void checkSpelling(String userInput) {

		//gets called every time the checkSpelling button is clicked
		_userInput = userInput;

		if (!isValidUserInput()){
			//check that the input only contains valid letters 

			//tell view to show a pop up window 
			_quizView.showInvalidInputPopUp();

			_festivalModel.speakCurrentWord(_currentWord);

			return;

		}else{

			//add word to history first
			_hiddenFilesModel.addWordToHistFile(_quizView.getCourseName(), _currentWord);

			if (isSameWord()){
				//if word is spelt correctly
				
				_currentCorrectCount ++;

				//allocate score according to time left
				int timeLeft = ((TestQuizView)_quizView).getTimerValue();
	
				if ((20 >= timeLeft) && (timeLeft >= 16)){
					//add 5 points
					((TestQuizView)_quizView).updateScore(5);
				}else if ((15 >= timeLeft) && (timeLeft >= 11)){
					//add 4 points
					((TestQuizView)_quizView).updateScore(4);
				}else if ((10 >= timeLeft) && (timeLeft >= 5)){
					//add 3 points
					((TestQuizView)_quizView).updateScore(3);
				}else if ((4 >= timeLeft) && (timeLeft >= 0)){
					//add 2 points
					((TestQuizView)_quizView).updateScore(2);
				}
				
				((TestQuizView) _quizView).updateFeedback(true);

				//_festivalModel.correctVoice();
				//play correct sound effect
				AudioPlayer audioPlayer = new AudioPlayer();
				audioPlayer.playAudio("./media/correct.wav");

				//write word to correct history file
				_hiddenFilesModel.addWordToCorrectIncorrectFile(HiddenFilesModel._testCorrectFolderPath+_quizView.getCourseName(), 
						_currentWord);

				moveOnToNextWord();

			}else {
				
				//play incorrect sound effect
				AudioPlayer audioPlayer = new AudioPlayer();
				audioPlayer.playAudio("./media/incorrect.wav");
				
				((TestQuizView) _quizView).updateFeedback(false);
				//word is not spelt correctly 
				_hiddenFilesModel.addWordToCorrectIncorrectFile(HiddenFilesModel._testIncorrectFolderPath+_quizView.getCourseName(), 
						_currentWord);

				//write word to correct history file

				//_festivalModel.failedVoice();
				moveOnToNextWord();

			}
		}
	}

	/**
	 * Test mode needs to reset timer every time it moves on to next word
	 * 
	 */

	@Override
	public void moveOnToNextWord() {

		super.moveOnToNextWord();

		//reset timer
		((TestQuizView)_quizView).resetTimer();

	}

	/**
	 * Test mode needs to show a different end of level card
	 */
	@Override
	protected void endOfLevel() {
		
		//stop timer
		
		((TestQuizView)_quizView).stopTimer();
		
		
		//test resultview does not need information about levels : therefore null parameter
		TestResultView testResultView = new TestResultView(_quizView.getCourseName(),((TestQuizView)_quizView).getScore());

		//result model for test result view needs different information
		//i.e. _countCorrectList
		TestResultModel resultModel = new TestResultModel(_randomWords, _countCorrectList);

		testResultView.setModel(resultModel);
		
		VoxSpellGui.getInstance().showCard(((Card) testResultView).createAndGetPanel(), "Result");
	}

}
