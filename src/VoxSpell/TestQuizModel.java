package VoxSpell;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class TestQuizModel extends QuizModel{
	private HiddenFilesModel _hiddenFilesModel;

	public TestQuizModel(Vector<String> allLevelNames) {
		super(allLevelNames);

		_hiddenFilesModel = HiddenFilesModel.getInstance();
	}

	/**
	 * Check spelling needs the functionality to write stats when checking spelling
	 */
	@Override
	protected void checkSpelling(String userInput) {

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
				System.out.println("time left : " + timeLeft);

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

				_festivalModel.correctVoice();

				//write word to correct history file
				_hiddenFilesModel.addWordToCorrectIncorrectFile(HiddenFilesModel._testCorrectFolderPath+_quizView.getCourseName(), 
						_currentWord);

				//System.out.println(HiddenFilesModel._testCorrectFolderPath+_quizView.getCourseName());
				moveOnToNextWord();

			}else {
				//word is not spelt correctly 
				_hiddenFilesModel.addWordToCorrectIncorrectFile(HiddenFilesModel._testIncorrectFolderPath+_quizView.getCourseName(), 
						_currentWord);

				//write word to correct history file

				_festivalModel.failedVoice();
				moveOnToNextWord();

			}
		}
	}

	/**
	 * Test mode needs to reset timer every time it moves on to next word
	 * 
	 */

	@Override
	protected void moveOnToNextWord() {

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
		ResultView testResultView = new TestResultView(null,_quizView.getCourseName(),null,((TestQuizView)_quizView).getScore());

		//result model for test result view needs different information
		//i.e. _countCorrectList
		ResultModel resultModel = new TestResultModel(_randomWords, _countCorrectList,null,_quizView.getCourseName());

		testResultView.setModel(resultModel);
		
		VoxSpellGui.getInstance().showCard(testResultView.createAndGetPanel(), "Result");
	}

}
