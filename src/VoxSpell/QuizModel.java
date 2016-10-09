package VoxSpell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import VoxSpell.HiddenFilesModel.StatsFile;

/**
 * Parent class for new and review quizzes, offers methods that are used for both mode of games
 * @author chen
 *
 */
abstract public class QuizModel {

	public enum WordMastery {
		MASTERED, FAULTED, FAILED
	}

	protected int _level;
	protected QuizView _quizView;

	protected HiddenFilesModel _hiddenFilesModel;
	protected FestivalModel _festivalModel;

	//arraylist that only stores the words that should be quizzed for this game
	protected ArrayList<String> _randomWords;

	//list of list structure for all words of this level
	private static ArrayList<String> _allWords;

	private int _numWordsToQuiz;

	//a word in the randomWord list that is currently being compared
	protected String _currentWord;
	protected int _currentIndex;

	protected int _totalCorrectCount;
	protected int _currentCorrectCount;

	protected int _attemptedCount;
	protected double _accuracyRate; 

	//the user input that is currently being compared
	protected String _userInput;

	//keeps track of how many times it took to get the word spelt correctly
	protected int _countChecks;

	//Methods to be implemented by child classes, implementation depends on the current quiz mode
	abstract protected void checkSpelling(String userInput);
	abstract protected void moveOnToNextWord();
	abstract protected void endOfLevel();

	public QuizModel(){
		//initialise fields		
		_randomWords = new ArrayList<String>();

		_hiddenFilesModel = HiddenFilesModel.getInstance();
		_festivalModel = FestivalModel.getInstance();

		_currentIndex = -1;

		_totalCorrectCount = 0;
		_currentCorrectCount = 0;

		_attemptedCount = 0;
		_accuracyRate = 100; 

		//keeps track of how many times it took to get the word spelt correctly
		_countChecks = 0;
	}

	public void setView(QuizView view){
		_quizView = view;
	}

	public void setInitialLevel(int level){
		_level = level;
	}

	public void setAllWords(ArrayList<String> allWords, int numWordsToQuiz){
		_allWords = allWords;
		_numWordsToQuiz = numWordsToQuiz;
	}

	/**
	 * REUSED CODE FROM ASSIGNMENT 2
	 * MOFIDIED TO SUIT ASSIGENMENT 3
	 * AUTHOR: CHEN LI
	 */

	/**
	 * Get appropriate number of random words from current level, given the allWordsList
	 */
	protected void getRandomWords(){

		if ((_allWords.size() < _numWordsToQuiz) && (_allWords.size() > 0)){

			//less than wanted words, just get all of them 
			_randomWords.addAll(_allWords);


		}else if (_allWords.size() >= 10){

			//chooses _numWordsToQuiz number of random words from allWords
			while (_randomWords.size() < _numWordsToQuiz){
				Random r = new Random();
				String randomWord = _allWords.get(r.nextInt(_allWords.size()));//generate a random word

				if (! _randomWords.contains(randomWord)){
					//only add randomWord if it is not already in the list, avoid repetition
					_randomWords.add(randomWord);
				}
			}
		}

		for (int j = 0; j < _randomWords.size(); j ++){
			System.out.println("Rndome word " + _randomWords.get(j));
		}

		//start spelling the FIRST word
		moveOnToNextWord();

	}
	/**
	 * helper method for view to get the number of words, so that it can be shown on the GUI
	 * @return
	 */
	protected int getTotalWordNum(){
		return _randomWords.size();
	}
	

	/**
	 * Helper method for the view to update GUI with correct word spelling when the user chooses "show answer"
	 * @return
	 */
	protected String getCorrectSpelling(){
		return _currentWord;
	}

	/**
	 * Update JLabel on its registered view to show the updated accuracy rate once a word is determined
	 * as "correct" or "incorrect"
	 */
	protected void updateAccuracyRate() {
		_accuracyRate = (_totalCorrectCount / (_attemptedCount + 0.0)) * 1000.0;
		_accuracyRate = Math.round(_accuracyRate);
		_accuracyRate = _accuracyRate/10;

		String accuracy = "%"+_accuracyRate;

		if (_accuracyRate == 100){
			accuracy = "%100"; //no decimal place for formatting purpose
		}else if (_level < 10){
			accuracy = accuracy + " "; //add one extra space for formatting purpose
		}

		_quizView.updateJLabel(_level, accuracy);

	}

	/**
	 * Checks if a word has capital letter(s) in it, determining if the 
	 * spell check should be case sensitive or not.
	 * @return
	 */
	protected boolean isCaseSensitive(){
		boolean hasCap = false;

		for (int i = 0; i < _currentWord.length(); i ++){
			//see if the correct word has any capital letters in it
			if ( Character.isUpperCase( _currentWord.charAt(i))){
				hasCap = true;
				break;
			}
		}
		return hasCap;
	}

	/**
	 * If the word contains no capital letters the spell check is non case sensitive
	 * The spell check is case sensitive if the word contains capital letters
	 */
	protected boolean isSameWord(){
		boolean caseSensitive = isCaseSensitive();

		if (caseSensitive){

			//case sensitive word check

			if(_userInput.equals(_currentWord)){
				return true;
			}
			return false;
		}else{

			//non case sensitive word check

			if(_userInput.toLowerCase().equals(_currentWord.toLowerCase())){

				return true;
			}
			return false;
		}

	}

	/**
	 * If word contains apostrophe, does not detect it as invalid
	 * If word does not apostrophe and user gives an apostrophe in the input, it is invalid input
	 * @return
	 */
	protected boolean isValidUserInput(){

		char[] inputChars = _userInput.toCharArray();
		char[] answerChars = _currentWord.toCharArray();

		ArrayList<Character> invalidChars = new ArrayList<Character>();

		for (int i = 0; i < _currentWord.length(); i ++){
			//see if the correct word has any non letter characters and add to list (avoid duplicates)
			if (! Character.isLetter(answerChars[i])){

				if (! invalidChars.contains(answerChars[i])){
					invalidChars.add(answerChars[i]);
				}

			}
		}

		for (int a = 0 ; a < inputChars.length; a ++){
			if(! Character.isLetter(inputChars[a])) {
				//if encounter a potential invalid character, check if invalidChars contains it
				if (! invalidChars.contains(inputChars[a])){
					return false;
				}
			}
		}
		return true;
	}

	public void relisten() {
		_festivalModel.relistenWord(_currentWord);
	}

	/**
	 * Depending on whether the user mastered/faulted/failed spelling of word, store in the appropriate file.
	 * @param masteryLevel
	 */
	protected void storeWordInStatsFile(WordMastery masteryLevel) {
		switch(masteryLevel) {
		case MASTERED:
			_hiddenFilesModel.addWordToStatsFile(StatsFile.MASTERED_WORDS, _currentWord);
			break;
		case FAULTED:
			_hiddenFilesModel.addWordToStatsFile(StatsFile.FAULTED_WORDS, _currentWord);
			break;
		case FAILED:
			_hiddenFilesModel.addWordToStatsFile(StatsFile.FAILED_WORDS, _currentWord);
			break;
		}
	}

	protected void setFestivalModel(FestivalModel model) {
		_festivalModel = model;
	}

}


