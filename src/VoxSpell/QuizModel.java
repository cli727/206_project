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

	//arraylist that only stores the words that should be quizzed on for the current level
	protected List<String> _randomWords;

	//one arrayList for all words in each level
	//words can be from either wordlist or failed word file
	private ArrayList<String> _lvlOneWords;
	private ArrayList<String> _lvlTwoWords;
	private ArrayList<String> _lvlThreeWords;
	private ArrayList<String> _lvlFourWords;
	private ArrayList<String> _lvlFiveWords;
	private ArrayList<String> _lvlSixWords;
	private ArrayList<String> _lvlSevenWords;
	private ArrayList<String> _lvlEightWords;
	private ArrayList<String> _lvlNineWords;
	private ArrayList<String> _lvlTenWords;
	private ArrayList<String> _lvlElevenWords;

	//list of list structure for all words of all levels
	private static ArrayList<ArrayList<String>> _allWords;

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
		_allWords = new ArrayList<ArrayList<String>>();

		_lvlOneWords = new ArrayList<String>();
		_lvlTwoWords = new ArrayList<String>();
		_lvlThreeWords = new ArrayList<String>();
		_lvlFourWords = new ArrayList<String>();
		_lvlFiveWords = new ArrayList<String>();
		_lvlSixWords = new ArrayList<String>();
		_lvlSevenWords = new ArrayList<String>();
		_lvlEightWords = new ArrayList<String>();
		_lvlNineWords = new ArrayList<String>();
		_lvlTenWords = new ArrayList<String>();
		_lvlElevenWords = new ArrayList<String>();

		//set up the list of list structure
		_allWords.add(_lvlOneWords);
		_allWords.add(_lvlTwoWords);
		_allWords.add(_lvlThreeWords);
		_allWords.add(_lvlFourWords);
		_allWords.add(_lvlFiveWords);
		_allWords.add(_lvlSixWords);
		_allWords.add(_lvlSevenWords);
		_allWords.add(_lvlEightWords);
		_allWords.add(_lvlNineWords);
		_allWords.add(_lvlTenWords);
		_allWords.add(_lvlElevenWords);

		_hiddenFilesModel = HiddenFilesModel.getInstance();

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

	public void setAllWords(ArrayList<ArrayList<String>> allWords){
		_allWords = allWords;

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

		if ((_allWords.get(_level-1).size() < 10) && (_allWords.get(_level-1).size() > 0)){

			//less than 10 words, just get all of them 
			_randomWords = _allWords.get(_level-1);


		}else if (_allWords.get(_level-1).size() >= 10){

			//chooses 10 random words from list from current level array list
			while (_randomWords.size() < 10){
				Random r = new Random();
				String randomWord = _allWords.get(_level-1).get(r.nextInt(_allWords.get(_level-1).size()));//generate a random word

				if (! _randomWords.contains(randomWord)){
					//only add randomWord if it is not already in the list, avoid repetition
					_randomWords.add(randomWord);
				}
			}
		}

		//for (int j = 0; j < _randomWords.size(); j ++){
		//System.out.println("Rndome word " + _randomWords.get(j));
		//}

		//start spelling the FIRST word
		moveOnToNextWord();

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


