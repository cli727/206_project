package VoxSpell;

import java.util.Vector;


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
			//compare userInput with currentWord
			_countChecks ++;
			
			write word to history file of this course

			if (isSameWord()){
				//if word is spelt correctly
				_festivalModel.correctVoice();

				write word to correct history file
				
				moveOnToNextWord();

			}else {
				//word is not spelt correctly 
				
				write word to correct history file

				_festivalModel.failedVoice(_currentWord);
				moveOnToNextWord();

			}
		}
	}


}
