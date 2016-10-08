package VoxSpell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton class that manages reading/writing actions to the hidden files
 * necessary for running the SpellingAidApp.
 * @author echa232
 *
 */
public class HiddenFilesModel {

	protected enum StatsFile {
		MASTERED_WORDS, FAULTED_WORDS, FAILED_WORDS
	}

	private static HiddenFilesModel _hiddenFilesModel;

	protected static Path _courseFolderPath;

	protected static Path _reviewFolderPath;
	protected static Path _reviewWordsLevel01FilePath;
	protected static Path _reviewWordsLevel02FilePath;
	protected static Path _reviewWordsLevel03FilePath;
	protected static Path _reviewWordsLevel04FilePath;
	protected static Path _reviewWordsLevel05FilePath;
	protected static Path _reviewWordsLevel06FilePath;
	protected static Path _reviewWordsLevel07FilePath;
	protected static Path _reviewWordsLevel08FilePath;
	protected static Path _reviewWordsLevel09FilePath;
	protected static Path _reviewWordsLevel10FilePath;
	protected static Path _reviewWordsLevel11FilePath;	

	protected static Path _statsFolderPath;
	protected static Path _masteredWordsFilePath;
	protected static Path _faultedWordsFilePath;
	protected static Path _failedWordsFilePath;

	protected static Path _festivalFolderPath;
	protected static Path _slowPacedVoiceFilePath;
	protected static Path _americanVoiceFilePath;
	protected static Path _newZealandVoiceFilePath;

	private HiddenFilesModel()
	{
		setUpHiddenFiles();
	}

	public static synchronized HiddenFilesModel getInstance()
	{
		if (_hiddenFilesModel == null){
			_hiddenFilesModel = new HiddenFilesModel();
		}		
		return _hiddenFilesModel;
	}

	/**
	 * Create the necessary hidden files/folders for running the SpellingAidApp
	 * These hidden files/folders are created in the same directory where the SpellingAidApp is executed.
	 */
	private void setUpHiddenFiles() {
		try {

			//Create file paths for the hidden files
			//For hidden files related to review, store file path in an array for ease of access in the future
			_reviewFolderPath = Paths.get("./.review");
			_reviewWordsLevel01FilePath = Paths.get("./.review/ReviewWordsLevel01");
			_reviewWordsLevel02FilePath = Paths.get("./.review/ReviewWordsLevel02");
			_reviewWordsLevel03FilePath = Paths.get("./.review/ReviewWordsLevel03");
			_reviewWordsLevel04FilePath = Paths.get("./.review/ReviewWordsLevel04");
			_reviewWordsLevel05FilePath = Paths.get("./.review/ReviewWordsLevel05");
			_reviewWordsLevel06FilePath = Paths.get("./.review/ReviewWordsLevel06");
			_reviewWordsLevel07FilePath = Paths.get("./.review/ReviewWordsLevel07");
			_reviewWordsLevel08FilePath = Paths.get("./.review/ReviewWordsLevel08");
			_reviewWordsLevel09FilePath = Paths.get("./.review/ReviewWordsLevel09");
			_reviewWordsLevel10FilePath = Paths.get("./.review/ReviewWordsLevel10");
			_reviewWordsLevel11FilePath = Paths.get("./.review/ReviewWordsLevel11");

			_statsFolderPath = Paths.get("./.stats");
			_masteredWordsFilePath = Paths.get("./.stats/MasteredWords");
			_faultedWordsFilePath = Paths.get("./.stats/FaultedWords");
			_failedWordsFilePath = Paths.get("./.stats/FailedWords");

			_festivalFolderPath = Paths.get("./.festival");
			_slowPacedVoiceFilePath = Paths.get("./.festival/slowPacedVoice.scm");
			_americanVoiceFilePath = Paths.get("./.festival/americanVoice.scm");
			_newZealandVoiceFilePath = Paths.get("./.festival/newZealandVoice.scm");

			//ACTUALLY THIS ISNT NEEDED
			_courseFolderPath = Paths.get("./.course");

			//Create stats folder and mastered/faulted/failed words files only if it doesn't already exists. 
			//Assumption: if stats folder already exists, then mastered/faulted/failed words file already exists within stats folder too
			if (Files.notExists(_statsFolderPath)) {
				Files.createDirectory(_statsFolderPath);
				Files.createFile(_masteredWordsFilePath);
				Files.createFile(_faultedWordsFilePath);
				Files.createFile(_failedWordsFilePath);
			}			

			//Create reviewWords folder only if it doesn't already exists
			//Assumption: if reviewWords folder already exists, then the reviewWordsLevelXX files for all levels already exists within reviewWords folder too
			if (Files.notExists(_reviewFolderPath)) {
				Files.createDirectory(_reviewFolderPath);
				Files.createFile(_reviewWordsLevel01FilePath);
				Files.createFile(_reviewWordsLevel02FilePath);
				Files.createFile(_reviewWordsLevel03FilePath);
				Files.createFile(_reviewWordsLevel04FilePath);
				Files.createFile(_reviewWordsLevel05FilePath);
				Files.createFile(_reviewWordsLevel06FilePath);
				Files.createFile(_reviewWordsLevel07FilePath);
				Files.createFile(_reviewWordsLevel08FilePath);
				Files.createFile(_reviewWordsLevel09FilePath);
				Files.createFile(_reviewWordsLevel10FilePath);
				Files.createFile(_reviewWordsLevel11FilePath);				
			}

			//Directory for storing scm files to be given as input to festival
			if (Files.notExists(_festivalFolderPath)) {
				Files.createDirectory(_festivalFolderPath);
				Files.createFile(_slowPacedVoiceFilePath);
				Files.createFile(_americanVoiceFilePath);
				Files.createFile(_newZealandVoiceFilePath);
				writeSCMCodeToVoiceFiles();
			}

			//Course folder which contains different wordlists for different courses
			if (Files.notExists(_courseFolderPath)){
				Files.createDirectory(_courseFolderPath);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Given a word, append it as a new line in the specified file.
	 * @param file
	 * @param word
	 */
	protected void addWordToStatsFile(StatsFile file, String word) {
		String wordOnNewLine = word + "\n";
		try {
			switch (file) {
			case MASTERED_WORDS:
				Files.write(_masteredWordsFilePath, wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			case FAULTED_WORDS:
				Files.write(_faultedWordsFilePath, wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			case FAILED_WORDS:
				Files.write(_failedWordsFilePath, wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	protected List<String> getWordsFromStatsFile(StatsFile file) {
		List<String> words = null;
		try {
			switch(file) {
			case MASTERED_WORDS:
				words = Files.readAllLines(_masteredWordsFilePath, StandardCharsets.ISO_8859_1);
				break;
			case FAULTED_WORDS:
				words = Files.readAllLines(_faultedWordsFilePath, StandardCharsets.ISO_8859_1);
				break;
			case FAILED_WORDS:
				words = Files.readAllLines(_failedWordsFilePath, StandardCharsets.ISO_8859_1);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return words;


	}
	protected void clearStats() {
		//First delete all files hidden files, then recreate them as blank files.
		try {
			Files.deleteIfExists(_masteredWordsFilePath);
			Files.deleteIfExists(_faultedWordsFilePath);		
			Files.deleteIfExists(_failedWordsFilePath);
			Files.deleteIfExists(_statsFolderPath);

			Files.deleteIfExists(_reviewWordsLevel01FilePath);
			Files.deleteIfExists(_reviewWordsLevel02FilePath);
			Files.deleteIfExists(_reviewWordsLevel03FilePath);
			Files.deleteIfExists(_reviewWordsLevel04FilePath);
			Files.deleteIfExists(_reviewWordsLevel05FilePath);
			Files.deleteIfExists(_reviewWordsLevel06FilePath);
			Files.deleteIfExists(_reviewWordsLevel07FilePath);
			Files.deleteIfExists(_reviewWordsLevel08FilePath);
			Files.deleteIfExists(_reviewWordsLevel09FilePath);
			Files.deleteIfExists(_reviewWordsLevel10FilePath);
			Files.deleteIfExists(_reviewWordsLevel11FilePath);		
			Files.deleteIfExists(_reviewFolderPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setUpHiddenFiles();		
	}

	protected void removeWordFromReviewWordsFile(String word, int level) {		
		Path reviewWordsFilePath = getReviewWordsFilePath(level); //Which Review File to remove the word from depends on what level it is.
		try {
			List<String> reviewWordsAtGivenLevel = Files.readAllLines(reviewWordsFilePath, StandardCharsets.ISO_8859_1);//First read in all words from reviewWords file
			List<String> tempList = new ArrayList<String>();
			tempList.add(word); 
			reviewWordsAtGivenLevel.removeAll(tempList); //Then remove all occurrences of the given word

			//Recreate a blank file in the same path as the reviewWords file & rewrite words into the file.
			Files.delete(reviewWordsFilePath);
			Files.createFile(reviewWordsFilePath);			
			Files.write(reviewWordsFilePath, reviewWordsAtGivenLevel, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void addWordToReviewWordsFile(String word, int level) {
		String wordOnNewLine = word + "\n";

		Path reviewWordsFilePath = getReviewWordsFilePath(level);

		try {
			//First, check if given word already exists in the file
			List<String> allReviewWords = Files.readAllLines(reviewWordsFilePath, StandardCharsets.ISO_8859_1);
			if (allReviewWords.contains(wordOnNewLine)) {
				return; //if it already exists, there is no need to add it to the file.
			}
			else { //if it doesn't already exist, then write it to the file.
				Files.write(reviewWordsFilePath, wordOnNewLine.getBytes(), StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

	protected BufferedReader getWordsForReview(int level) {
		BufferedReader br = null;
		Path reviewWordsFilePath = getReviewWordsFilePath(level); //Words for review depends on what the given level is.

		try {
			br = Files.newBufferedReader(reviewWordsFilePath, StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return br;
	}

	/**
	 * A private helper method to retrieve the reviewWords file at the corresponding level. 
	 * @param level
	 * @return
	 */
	protected Path getReviewWordsFilePath(int level) {

		switch (level) {
		case 1:
			return _reviewWordsLevel01FilePath;
		case 2:
			return _reviewWordsLevel02FilePath;
		case 3:
			return _reviewWordsLevel03FilePath;
		case 4:
			return _reviewWordsLevel04FilePath;
		case 5:
			return _reviewWordsLevel05FilePath;
		case 6:
			return _reviewWordsLevel06FilePath;
		case 7:
			return _reviewWordsLevel07FilePath;
		case 8:
			return _reviewWordsLevel08FilePath;
		case 9:
			return _reviewWordsLevel09FilePath;
		case 10:
			return _reviewWordsLevel10FilePath;
		case 11:
			return _reviewWordsLevel11FilePath;
		default:
			return null;
		}
	}

	protected static Path getFestivalFolderPath() {
		return _festivalFolderPath;
	}

	/**
	 * Read the contents of a file according to quizMode, into a list of list of strings
	 * array structure
	 * 
	 * CODE REUSED FROM CHEN LI'S ASSIGNMENT 2 SUBMISSION
	 * @param quizMode
	 * @return
	 */
	protected ArrayList<String> readFileToArray(String quizMode, String coursePath){ 

		BufferedReader reader;

		ArrayList<String> allWords = new ArrayList<String>();

		if ( quizMode.equals(VoxSpellGui.NEW)){

			//read from "wordlist" if this is a new quiz
			int wordListIndex = -1;//keep tracks of which list to update in _allWords

			//fills allWords
			try {
				reader = new BufferedReader(new FileReader(coursePath)); 
				String line = reader.readLine();

				while (line != null) {

					allWords.add(line.trim()); //trims all leading and trailing white spaces in a word, i.e. "this"

					line = reader.readLine();
				}

				reader.close();

			} catch (FileNotFoundException e) {
				//do nothing, the GUI will be telling user about empty word list
			} catch (IOException e) {
				//do nothing
			}
		} else { //it is the "review mistakes" mode			
			for (int i = 0; i < 11; i++) { //for each level... (current specifications state there are 11 levels only)
				try {
					BufferedReader br = _hiddenFilesModel.getWordsForReview(i+1);

					String line;
					//add lists of words at each level in corresponding position in allWords (e.g. position 0 of allWords list is review words for level 1
					while ((line = br.readLine()) != null) {
						if (allWords.contains(line)) { //if the word read has already been recorded in allWords previously
							continue;
						}
						else {
							allWords.add(line);
						}						
					}
					br.close();

				} catch (IOException e) {
					// do nothing
				}

			}
		}

		return allWords;
	}

	protected ArrayList<String> readLevelOfFile(String filepath){
		ArrayList<String> allLevels = new ArrayList<String>();
		return allLevels;

	}

	/**
	 * Method to check if the wordlist file/review files are empty given the quizMode
	 * (No quiz words at all for any levels)
	 * @param quizMode
	 * @return
	 */
	protected boolean allEmpty(String quizMode){
		int nonEmptyLevel = 0;

		ArrayList<ArrayList<String>> allWords = _hiddenFilesModel.readFileToArray(quizMode);

		for (int i = 0; i < allWords.size();i++){

			if (! allWords.get(i).isEmpty()){
				nonEmptyLevel ++;
			}
		}

		if (nonEmptyLevel == 0){
			//no level is not empty -> all empty
			return true;
		}

		return false;
	}

	/**
	 * Check if there are quiz words for the level chosen
	 * @param level
	 * @param quizMode
	 * @return
	 */
	protected boolean levelEmpty(int level, String quizMode){

		if(quizMode.equals(VoxSpellGui.REVIEW)){

			ArrayList<ArrayList<String>> allWords = _hiddenFilesModel.readFileToArray(quizMode);

			if ( allWords.get(level-1).isEmpty()){
				return true;
			}

			return false;

		}else {
			//NEW should not have empty levels according to assignment3 specification
			return false;
		}
	}

	private void writeSCMCodeToVoiceFiles() {
		try {
			//For slower-paced voice
			ArrayList<String> slowCommand = new ArrayList<String>();
			slowCommand.add("(Parameter.set 'Duration_Stretch 1.5)");
			Files.write(_slowPacedVoiceFilePath, slowCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

			//For American voice
			ArrayList<String> americanVoiceCommand = new ArrayList<String>();
			americanVoiceCommand.add("(voice_kal_diphone)");
			Files.write(_americanVoiceFilePath, americanVoiceCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

			//For NZ Voice
			ArrayList<String> newZealandVoiceCommand = new ArrayList<String>();
			newZealandVoiceCommand.add("(voice_akl_nz_jdt_diphone)");
			Files.write(_newZealandVoiceFilePath, newZealandVoiceCommand, StandardCharsets.ISO_8859_1, StandardOpenOption.APPEND);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete a temporary video output file generated for ffmpeg in the VideoPlayer
	 * @param videoPath
	 */
	public void deleteVideoFile (String videoPath){
		Path path = Paths.get(videoPath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
