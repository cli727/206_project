package VoxSpell;

/**
 * A model that handles the back stage logic for a review quiz, a child of QuizModel
 * @author chen
 */
public class ReviewQuizModel extends QuizModel{

	/**
	 * Checks whether a word is spelt correctly and manages files for the review quiz game
	 * CODE REUSED AND MODIFIED FROM CHEN LI'S ASSIGNMENT 2 SUBMISSION
	 */
	@Override
	protected void checkSpelling(String userInput) {

		//gets called every time the checkSpelling button is clicked
		_userInput = userInput;

		if (! isValidUserInput()){
			//check that the input only contains valid letters 

			//tell view to show a pop up window 
			_quizView.showInvalidInputPopUp();

			_festivalModel.speakCurrentWord(_currentWord);

			return;
		}else{
			//compare userInput with currentWord
			_countChecks ++;

			if (isSameWord()){
				//if word is spelt correctly, mastered/faulted, the word is removed from failed list
				_festivalModel.correctVoice();

				if (_countChecks == 1){
					//mastered
					_currentCorrectCount ++; 
					_totalCorrectCount ++;

					storeWordInStatsFile(WordMastery.MASTERED);
					_hiddenFilesModel.removeWordFromReviewWordsFile(_currentWord, _level);

				}else if (_countChecks == 2){
					//faulted

					storeWordInStatsFile(WordMastery.FAULTED);
					_hiddenFilesModel.removeWordFromReviewWordsFile(_currentWord, _level);
				}

				updateAccuracyRate();

				moveOnToNextWord();//_countChecks cleared!!!

			}else {
				//word is not spelt correctly, word is not removed 
				if (_countChecks == 1){
					//give user another chance
					_festivalModel.faultedVoice(_currentWord);

					//accuracy rate is updated once the first try of the word is finished
					//i.e. both faulted and failed are "incorrect"
					updateAccuracyRate();

				}else if (_countChecks == 2){
					//failed
					_hiddenFilesModel.addWordToReviewWordsFile(_currentWord, _level);
					storeWordInStatsFile(WordMastery.FAILED);

					_festivalModel.failedVoice();

					moveOnToNextWord();
				}
			}
		}
	}

	/**
	 * Check if the current word is the last word in the quiz, continues game if not; shows results if yes
	 */
	@Override
	protected void moveOnToNextWord() {

		_countChecks = 0;

		if (  _currentIndex == (_randomWords.size()-1)){

			//if it is the end of level, i.e.  the last word for REVIEW (NO WORD COUNT FOR REVIEW MODE, USER REVIEWS ALL WORDS)
			endOfLevel();
		}else if (_currentIndex != (_randomWords.size()-1)){

			//set currentWord to the next word if this is not the last word in randomWords list
			_currentIndex ++;
			_attemptedCount ++;
			_currentWord = _randomWords.get(_currentIndex);

			//updates word progress on the gui, clears input area
			_quizView.updateWordLabel(VoxSpellGui.REVIEW,_currentIndex+1, _randomWords.size(),_level);
			_quizView.clearInputArea();

			//update tips label in case of case sensitivity
			_quizView.updateTipsLabel(isCaseSensitive());
			
			_festivalModel.speakCurrentWord(_currentWord);
		}
	}
	
	/**
	 * No concept of leveling, go back to main menu after end of review level
	 */
	@Override
	protected void endOfLevel() {
		_quizView.showReviewEndPopUp(_currentCorrectCount, (_randomWords.size() - _currentCorrectCount));
		_quizView.showMainMenu();
	}


}
