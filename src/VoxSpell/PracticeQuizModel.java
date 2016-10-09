package VoxSpell;

import java.util.ArrayList;

import video_player.VideoPlayer;

/**
 * A model that handles the back stage logic for a new quiz, a child of QuizModel
 * @author chen
 */

public class PracticeQuizModel extends QuizModel{

	private ArrayList<Integer> _countCheckList; //keeps track of attempted times of every word 
	
	public PracticeQuizModel(){
		//constructor, initialise private field
		super();
		 _countCheckList = new ArrayList<Integer>();
	}
	
	/**
	 * CheckSpelling for Practice mode. No such concept as mastered/faulted/failed
	 * The quiz moves on only if the user gets a word right / skip the word
	 * Words written to history (not review)
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

			if (isSameWord()){
				//if word is spelt correctly
				_festivalModel.correctVoice();

				//store to history
				/*
				if (_countChecks == 1){
					//mastered
					_currentCorrectCount ++; 
					_totalCorrectCount ++;
					////storeWordInStatsFile(WordMastery.MASTERED);

				}else if (_countChecks == 2){
					//faulted
					storeWordInStatsFile(WordMastery.FAULTED);
				}*/

				//updateAccuracyRate();

				moveOnToNextWord();//_countChecks cleared!!!

			}else {
				//word is not spelt correctly 

				_festivalModel.faultedVoice(_currentWord);

			}
		}
	}

	/**
	 * Check if the current word is the last word in the quiz, continues game if not; shows results if yes
	 */
	@Override
	protected void moveOnToNextWord() {
		
		if (_currentIndex != -1){
			
			//add current count check in list
			_countCheckList.add(_countChecks);
		}
		
		_countChecks = 0;

		System.out.println("currentINdex: "+ _currentIndex + " current word: " + _currentWord);

		
		if ( _currentIndex == (_randomWords.size()-1)){

			//last word in randomWords list
			endOfLevel();
		}else if (_currentIndex != (_randomWords.size()-1)){

			//set currentWord to the next word if this is not the last word in randomWords list
			_currentIndex ++;
			_attemptedCount ++;
			_currentWord = _randomWords.get(_currentIndex);

			//updates word progress on the gui, clears input area, disable answer panel
			_quizView.updateWordLabel("Spell ", _currentIndex+1, _randomWords.size(),_level);
			_quizView.clearInputArea();
			_quizView.disableAnswer();

			//update tips label in case of case sensitivity
			_quizView.updateTipsLabel(isCaseSensitive());

			_festivalModel.speakCurrentWord(_currentWord);
		}

	}

	/**
	 * Manages the end of a level,shows the user the result card
	 */
	@Override
	protected void endOfLevel() {
		//show result card
		ResultView resultView = new ResultView(_randomWords.size());
		ResultModel resultModel = new ResultModel(_randomWords,_countCheckList);
		
		resultView.setModel(resultModel);
		
		VoxSpellGui.getInstance().showCard(resultView.createAndGetPanel(), "Result");
	}


	public void ifLevelUp(){
		if (  _level != 11){
			//if not the last level

			boolean levelUp = _quizView.showLevelUpPopUP(_level);

			//reset currentCorrectCount whether the user choose to stay or level up
			_currentCorrectCount = 0;
			if (levelUp){
				//level up, get random words on the new level
				_level ++;

				_attemptedCount = 0;
				_accuracyRate = 100;
				_totalCorrectCount = 0;

				_quizView.updateJLabel(_level, "--");
			}
			getRandomWords();

		}else {
			//if last level
			//goes back to main menu
			_quizView.showMainMenu();
		}
	}
}

