package voxSpell.models.quizModels;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import voxSpell.views.resultViews.ResultView;
import voxSpell.views.resultViews.ReviewResultView;
import voxSpell.models.resultModels.ResultModel;
import voxSpell.status.QuizStatus;
import voxSpell.views.VoxSpellGui;
import voxSpell.views.quizViews.QuizView;
import voxSpell.models.festivalManager.FestivalModel;

/**
 * The model/logic for practice and review quizzes, which the quiz views use to handle user events
 * @author chen
 *
 */
public class QuizModel {

	protected QuizView _quizView;

	protected FestivalModel _festivalModel;

	//arraylist that only stores the words that should be quizzed for this game
	protected ArrayList<String> _randomWords;

	//list of list structure for all words of this level
	private static ArrayList<String> _allWords;

	private int _numWordsToQuiz;

	//a word in the randomWord list that is currently being compared
	protected String _currentWord;
	private int _currentIndex;

	private int _totalCorrectCount;
	protected int _currentCorrectCount;

	private int _attemptedCount;
	private double _accuracyRate; 

	//the user input that is currently being compared
	protected String _userInput;

	//keeps track of how many times it took to get the word spelt correctly
	protected int _countChecks;
	
	protected ArrayList<String> _countCheckList; //keeps track of attempted times of every word 
	protected ArrayList<String> _countCorrectList; //keeps track of correct times of every word, can be 1 or 0, needed for test model
	
	private Vector<String> _allLevelNames;

	public QuizModel(Vector<String> allLevelNames){
		//initialise fields		
		_countCheckList = new ArrayList<String>();
		_countCorrectList = new ArrayList<String>();
		
		_allLevelNames = allLevelNames; //without % in front
		
		_randomWords = new ArrayList<String>();

		_festivalModel = FestivalModel.getInstance();

		_currentIndex = -1;

		_totalCorrectCount = 0;
		_currentCorrectCount = 0;

		_attemptedCount = 0;
		_accuracyRate = 100; 

		//keeps track of how many times it took to get the word spelt correctly
		_countChecks = 0;
	}

	/**
	 * Register a view with this model
	 * @param view
	 */
	public void setView(QuizView view){
		_quizView = view;
	}
	
	/**
	 * Set the words to quiz from for the current quiz, as well as the number of words to quiz
	 * @param allWords
	 * @param numWordsToQuiz
	 */
	public void setAllWords(ArrayList<String> allWords, int numWordsToQuiz){
		_allWords = allWords;
		_numWordsToQuiz = numWordsToQuiz;
	}

	/**
	 * CheckSpelling for Practice mode. 
	 * The quiz moves on only if the user gets a word right / skip the word
	 */
	public void checkSpelling(String userInput) {

		//gets called every time the checkSpelling button is clicked
		_userInput = userInput;

		if (!isValidUserInput()){
			//check that the input only contains valid letters 

			//tell view to show a pop up window 
			_quizView.showInvalidInputPopUp();

			_festivalModel.speakCurrentWord(_currentWord);

			return;

		}else if (userInput.isEmpty()){
			//user has not typed anything, do nothing and wait for user to type again
		}else{
			//compare userInput with currentWord
			_countChecks ++;

			if (isSameWord()){
				//if word is spelt correctly
				_festivalModel.correctVoice();

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
	public void moveOnToNextWord() {

		if (_currentIndex != -1){

			//add current count check in list
			_countCheckList.add(Integer.toString(_countChecks));
			
			//add correct times in list
			 _countCorrectList.add(Integer.toString(_currentCorrectCount));
		}

		_countChecks = 0;
		_currentCorrectCount = 0;

		//System.out.println("currentINdex: "+ _currentIndex + " current word: " + _currentWord);


		if ( _currentIndex == (_randomWords.size()-1)){

			//last word in randomWords list
			endOfLevel();
		}else if (_currentIndex != (_randomWords.size()-1)){

			//set currentWord to the next word if this is not the last word in randomWords list
			_currentIndex ++;
			_attemptedCount ++;
			_currentWord = _randomWords.get(_currentIndex);

			//updates word progress on the gui, clears input area, disable answer panel
			_quizView.updateWordLabel(_currentIndex+1);
			_quizView.clearInputArea();
			_quizView.disableAnswer();

			//update tips label in case of case sensitivity
			_quizView.updateTipsLabel(isCaseSensitive(), containsSpecialCharacter());

			_festivalModel.speakCurrentWord(_currentWord);
		}

	}

	/**
	 * Manages the end of a level,shows the user the result card
	 */
	protected void endOfLevel() {
		//show result card according to game mode
		ResultView resultView = null;
		
		if (VoxSpellGui.STATUS.equals(QuizStatus.NEW)){
			
			resultView = new ResultView(_quizView.getLevelName(), _quizView.getCourseName(),_allLevelNames);
		}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){
			
			resultView = new ReviewResultView(_quizView.getLevelName(), _quizView.getCourseName(),_allLevelNames);
		}
		ResultModel resultModel = new ResultModel(_randomWords,_countCheckList,_quizView.getLevelName(),_quizView.getCourseName());

		resultView.setModel(resultModel);
		resultModel.setView(resultView);

		VoxSpellGui.getInstance().showCard(resultView.createAndGetPanel(), "Result");
	}
	

	/**
	 * Get appropriate number of random words from current level, given the allWordsList
	 */
	public void getRandomWords(){

		//The method aims to avoid duplicates in the randomly generated words, which may lead to infinite loop if
		//the words within the level are duplicated in the wordlist
		
		//chooses _numWordsToQuiz number of random words from allWords
	   
		while (_randomWords.size() < _numWordsToQuiz){
			Random r = new Random();
			String randomWord = _allWords.get(r.nextInt(_allWords.size()));//generate a random word

			if (! _randomWords.contains(randomWord)){
				
				//only add randomWord if it is not already in the list, avoid repetition
				_randomWords.add(randomWord);
			}
		}

		//start spelling the FIRST word
		moveOnToNextWord();

	}
	/**
	 * helper method for view to get the number of words, so that it can be shown on the GUI
	 * @return
	 */
	public int getTotalWordNum(){
		return _randomWords.size();
	}


	/**
	 * Helper method for the view to update GUI with correct word spelling when the user chooses "show answer"
	 * @return
	 */
	public String getCorrectSpelling(){
		return _currentWord;
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
	 * If word contains special character, does not detect user input as invalid
	 * If word does not special character and user gives special characters in the input, it is invalid input
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
	
	/**
	 * Checks if a word has special characters. Update tips on view if so.
	 * @return
	 */
	protected boolean containsSpecialCharacter(){
		for (int i = 0; i < _currentWord.length(); i ++){
			if (! Character.isLetter(_currentWord.charAt(i))){
				//this word contains special characters
				return true;
			}
		}
		return false;
		
	}

	/**
	 * Redirects the call for FestivalModel to manage relisten
	 */
	public void relisten() {
		_festivalModel.relistenWord(_currentWord);
	}

	protected void setFestivalModel(FestivalModel model) {
		_festivalModel = model;
	}

}


