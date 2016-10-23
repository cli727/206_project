package voxSpell.models.hiddenFilesManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * A Singleton class that manages reading/writing actions to the hidden files
 * necessary for running the SpellingAidApp.
 * @author echa232
 *
 */
public class HiddenFilesModel {
	public static final String _reviewFolderPath = "./.review/";
	public static final String _testHistoryFolderPath = "./.testHistory/";
	public static final String _testCorrectFolderPath = "./.testCorrect/";
	public static final String _testIncorrectFolderPath = "./.testIncorrect/";
	public static final String _highScoreFolderPath = "./.highScore/";

	protected enum StatsFile {
		REVIEW, HISTORY
	}

	private static HiddenFilesModel _hiddenFilesModel;

	protected static Path _courseFolderPath;

	protected static Path _reviewFilePath;
	protected static Path _historyFilePath;

	protected static Path _festivalFolderPath;
	public static Path _slowPacedVoiceFilePath;
	public static Path _americanVoiceFilePath;
	public static Path _newZealandVoiceFilePath;

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

			_festivalFolderPath = Paths.get("./.festival");
			_slowPacedVoiceFilePath = Paths.get("./.festival/slowPacedVoice.scm");
			_americanVoiceFilePath = Paths.get("./.festival/americanVoice.scm");
			_newZealandVoiceFilePath = Paths.get("./.festival/newZealandVoice.scm");


			//create .review folder if it does not exist
			if (Files.notExists(Paths.get(_reviewFolderPath))) {
				Files.createDirectory(Paths.get(_reviewFolderPath));
			}			

			//create .testHistory folder it it does not exist
			if(Files.notExists(Paths.get(_testHistoryFolderPath))){
				Files.createDirectories(Paths.get(_testHistoryFolderPath));
			}

			//create .testCorrect folder if it doesnot exist
			if(Files.notExists(Paths.get(_testCorrectFolderPath))){
				Files.createDirectories(Paths.get(_testCorrectFolderPath));
			}

			//create .testIncorrect folder if it does not exist
			if(Files.notExists(Paths.get(_testIncorrectFolderPath))){
				Files.createDirectories(Paths.get(_testIncorrectFolderPath));
			}

			//create .highScore folder if it does not exist
			if(Files.notExists(Paths.get(_highScoreFolderPath))){
				Files.createDirectories(Paths.get(_highScoreFolderPath));
			}

			File folder = new File("./.course");
			File[] listOfFiles = folder.listFiles(); //all file names within ./.course folder

			//for every file in the ./course folder
			for (int i = 0; i < listOfFiles.length; i++ ){

				//==========create each course's review file if it does not exist==========================
				Path thisReviewFilePath = Paths.get(_reviewFolderPath + listOfFiles[i].getName()+"Review");

				//create the review version of the course file, consisting of only level names to begin with
				if (Files.notExists(thisReviewFilePath)) {

					//write to this file the level names only, for appending purpose later 

					//read all words from the COURSE file first
					ArrayList<String> allWords = readFileToArray("./.course/"+ listOfFiles[i].getName());
					ArrayList<String> levelWords = new ArrayList<String>();

					//find level names
					for (int j = 0; j < allWords.size(); j ++){
						if (Character.toString(allWords.get(j).charAt(0)).equals("%")){
							levelWords.add(allWords.get(j));
						}
					}

					//write these level names to file
					try {
						File stats = new File(thisReviewFilePath.toString());
						stats.createNewFile();

						FileWriter writer = new FileWriter(stats); 

						for(int k = 0; k < levelWords.size();k++){

							writer.write(levelWords.get(k)+"\n"); 	
						}
						writer.flush();
						writer.close();
					} catch (IOException e) {

						e.printStackTrace();
					}

				}

				//==========create each course's test history file if it does not exist==========================
				Path thisTestHistoryFilePath = Paths.get(_testHistoryFolderPath + listOfFiles[i].getName());

				if (Files.notExists(thisTestHistoryFilePath)) {

					Files.createFile(thisTestHistoryFilePath);
				}

				//==========create each course's test correct file if it does not exist==========================
				Path thistestCorrectFilePath = Paths.get(_testCorrectFolderPath+ listOfFiles[i].getName());

				if (Files.notExists(thistestCorrectFilePath)) {

					Files.createFile(thistestCorrectFilePath);
				}

				//==========create each course's test incorrect file if it does not exist==========================
				Path thistestIncorrectFilePath = Paths.get(_testIncorrectFolderPath + listOfFiles[i].getName());

				if (Files.notExists(thistestIncorrectFilePath)) {

					Files.createFile(thistestIncorrectFilePath);
				}

				//==========create each course's high score file if it does not exist==========================
				Path thisHighScoreFilePath = Paths.get(_highScoreFolderPath + listOfFiles[i].getName());

				if (Files.notExists(thisHighScoreFilePath )) {
					//recreate the review file with the added word, should replace the original file
					try {
						File stats = new File(thisHighScoreFilePath.toString());
						stats.createNewFile();

						FileWriter writer = new FileWriter(stats); 

						writer.write("0"+"\n"); 	

						writer.flush();
						writer.close();
					} catch (IOException e) {

						e.printStackTrace();
					}
				}


			}

			//Directory for storing scm files to be given as input to festival
			if (Files.notExists(_festivalFolderPath)) {
				Files.createDirectory(_festivalFolderPath);
				Files.createFile(_slowPacedVoiceFilePath);
				Files.createFile(_americanVoiceFilePath);
				Files.createFile(_newZealandVoiceFilePath);
				writeSCMCodeToVoiceFiles();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * searches through ./.course directory and finds course names that are not "KET" or "IELTS"
	 * @return
	 */
	public ArrayList<String> getImportedCourseNames() {
		ArrayList<String> importedCourse = new ArrayList<String>();

		File folder = new File("./.course");
		File[] listOfFiles = folder.listFiles(); //all file names within ./.course folder

		//for every file in the ./course folder
		for (int i = 0; i < listOfFiles.length; i++ ){
			String thisCourse = listOfFiles[i].getName();

			if (! thisCourse.equals("KET") && ! thisCourse.equals("IELTS")){
				//not the built in lists
				importedCourse.add(thisCourse);
			}
		}
		return importedCourse;
	}


	/**
	 * append a word to end of its course history, avoid duplicates	
	 * @param courseName
	 * @param word
	 */
	public void addWordToHistFile(String courseName, String word) {

		List<String> allWords = new ArrayList<String>();

		try {

			allWords = Files.readAllLines(Paths.get(_testHistoryFolderPath+courseName), StandardCharsets.ISO_8859_1);
			if(! allWords.contains(word)){

				BufferedWriter writer = new BufferedWriter(new FileWriter((_testHistoryFolderPath+courseName).toString(), true));
				writer.append(word+"\n");
				writer.close();

			}

		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public ArrayList<String> getHistroyWords(String _courseName) {
		ArrayList<String> allWords = readFileToArray(_testHistoryFolderPath+_courseName);

		Collections.sort(allWords); //sort in alphabetical order;
		return allWords;
	}

	/**
	 * Get correct/incorrect counts of all words in a history file 
	 * @param _courseName
	 * @param getCorrect
	 * @return
	 */
	public ArrayList<Integer> getCorrectIncorrectCount (String _courseName, boolean getCorrect){
		ArrayList<String> histWords = getHistroyWords(_courseName);

		ArrayList<String> countFile;

		if(getCorrect){
			//get correct counts
			countFile = readFileToArray(_testCorrectFolderPath+_courseName);
		}else {
			//get incorrect counts
			countFile = readFileToArray(_testIncorrectFolderPath+_courseName);
		}

		ArrayList<Integer> totalCount = new ArrayList<Integer>();


		for (int i = 0; i < histWords.size(); i ++){
			int count = 0;

			//count appearance of each history word in the count hidden file
			for(int j = 0; j <countFile.size(); j ++){

				if (countFile.get(j).equals(histWords.get(i))){
					count ++;
				}
			}

			totalCount.add(count);
		}

		return totalCount;
	}

	/**
	 * Adding to correct / incorrect allows duplications, therefore count number of times a word is correct/incorrect
	 * during a test
	 * @param filePath
	 * @param word
	 */
	public void addWordToCorrectIncorrectFile(String filePath, String word){

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter((filePath).toString(), true));
			writer.append(word+"\n");
			writer.close();


		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	protected void clearStats() {
		//First delete all files hidden files, then recreate them as blank files.
		try {
			Files.deleteIfExists(_historyFilePath);
			Files.deleteIfExists(_reviewFilePath);		

		} catch (IOException e) {
			e.printStackTrace();
		}

		setUpHiddenFiles();		
	}

	/**
	 * Removes a word from review file of corresponding course
	 * ASSUMES NO DUPLICATION OF THE SAME WORD IN REVIEW 
	 * @param word
	 * @param level
	 * @param courseName
	 */
	public void removeWordFromReviewWordsFile(String word, String level, String courseName) {

		String coursePath = "./.review/"+courseName+"Review";
		ArrayList<String> allWords = new ArrayList<String>();
		allWords = readFileToArray(coursePath);

		allWords.remove(word);

		//recreate the review file with the added word, should replace the original file
		try {
			File stats = new File(coursePath);
			stats.createNewFile();

			FileWriter writer = new FileWriter(stats); 

			for(int i = 0; i < allWords.size();i++){
				writer.write(allWords.get(i)+"\n"); 	
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * add a word to review list
	 * ASSUMES NO DUPLICATION OF WORDS (INTER AND INTRO LEVELS), OTHERWISE ONLY ADDED ONCE
	 * @param word
	 * @param level
	 * @param courseName
	 */
	public void addWordToReviewWordsFile(String word, String level, String courseName) {


		String coursePath = "./.review/"+courseName+"Review";
		ArrayList<String> allWords = new ArrayList<String>();
		allWords = readFileToArray(coursePath);

		if (! allWords.contains(word)){
			//add word

			//find index of this level
			int index = allWords.indexOf("%"+level);

			int nextLvlIndex = allWords.size();

			for (int i = index + 1; i < allWords.size(); i ++){
				//starting after this index, find the position of next level
				if(Character.toString(allWords.get(i).charAt(0)).equals("%")){
					nextLvlIndex = i;

					break;
				}
			}

			//add word at position of next level (i.e. so it pushes next level further)

			allWords.add(nextLvlIndex, word);
		}

		//recreate the review file with the added word, should replace the original file
		try {
			File stats = new File(coursePath);
			stats.createNewFile();

			FileWriter writer = new FileWriter(stats); 

			for(int i = 0; i < allWords.size();i++){
				writer.write(allWords.get(i)+"\n"); 	
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}	

	/**
	 * A method that gets the  user selected wordlist and copy it as a wordlist
	 * Returns a boolean indicating whether the user selected file matches with required wordlist format
	 * 
	 * Invalid format include:
	 * 1. file that does not start with "%" i.e. a level name
	 * 2. empty file
	 * 3. file that contains duplicate words in a level (getRandomWords may break, because randomWords keeps
	 * adding non duplicated words, repeated words may cause infinite while loop)
	 * 4. file not named KET or IELTS
	 * 5. file should not have an empty level
	 * 
	 * @param path
	 */
	public boolean copyToCourse(String userWordListpath, String fileName) {

		ArrayList<String> allWords = new ArrayList<String>();
		allWords = readFileToArray(userWordListpath);

		//check if the file matches with required format
		if (allWords.size() == 0){
			//empty file
			return false;
		}else if (! Character.toString(allWords.get(0).charAt(0)).equals("%")){
			//does not start with a level
			return false;
		}else if (fileName.equals("KET") || fileName.equals("IELTS")){
			//do not want to override the build in lists
			return false;
		}

		//check for duplicates in all levels, also check for empty levels

		Vector<String> allLevelNames = getAllLevelsFromCourse(userWordListpath); //get all level names first

		for(int j = 0; j < allLevelNames.size(); j ++){
			//get all words from level
			ArrayList<String> wordsFromLevel = getLevelWordsFromCourse(userWordListpath,allLevelNames.get(j));

			//a quick way to find out whether there are duplicates in the list using sets
			Set<String> set = new HashSet<String>(wordsFromLevel);

			if(set.size() < wordsFromLevel.size()){
				//duplicates 
				return false;
			}

			if(set.size() == 0){
				//empty level
				return false;
			}

		}

		//valid file, copy it to .course directory

		try {
			File newCourse = new File("./.course/"+fileName);
			newCourse.createNewFile();

			FileWriter writer = new FileWriter(newCourse); 

			for(int k = 0; k < allWords.size();k++){

				writer.write(allWords.get(k)+"\n"); 	
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		//set up its review, testHistory, test Correct, testIncorrect , high score hidden files
		setUpHiddenFiles();

		return true;//successful
	}

	/**
	 * Delete a course and all its statistics hidden files
	 * returns true indicating success, false indicating error
	 * 
	 * @param courseName
	 * @return 
	 */
	public boolean deleteCourse(String courseName) {

		try {
			//delete course file
			Files.deleteIfExists(Paths.get("./.course/"+courseName));

			//delete .review file
			Files.deleteIfExists(Paths.get(_reviewFolderPath + courseName + "Review"));

			//delete .testHistory file
			Files.deleteIfExists(Paths.get(_testHistoryFolderPath + courseName));

			//delete .testCorrect file
			Files.deleteIfExists(Paths.get(_testCorrectFolderPath + courseName));

			//delete test Incorrect file
			Files.deleteIfExists(Paths.get(_testIncorrectFolderPath + courseName));

			//delete highScore file
			Files.deleteIfExists(Paths.get(_highScoreFolderPath + courseName));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean deleteCourseStats(String courseName) {

		try {

			//delete .review file
			Files.deleteIfExists(Paths.get(_reviewFolderPath + courseName + "Review"));

			//delete .testHistory file
			Files.deleteIfExists(Paths.get(_testHistoryFolderPath + courseName));

			//delete .testCorrect file
			Files.deleteIfExists(Paths.get(_testCorrectFolderPath + courseName));

			//delete test Incorrect file
			Files.deleteIfExists(Paths.get(_testIncorrectFolderPath + courseName));

			//delete highScore file
			Files.deleteIfExists(Paths.get(_highScoreFolderPath + courseName));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		setUpHiddenFiles(); //reset files, i.e. .highscore to 0, testhistory to empty

		return true;
	}

	protected static Path getFestivalFolderPath() {
		return _festivalFolderPath;
	}

	/**
	 * Read the contents of a file according to quizMode, into a list of strings
	 * array structure
	 * 
	 * CODE REUSED FROM CHEN LI'S ASSIGNMENT 2 SUBMISSION
	 * @param quizMode
	 * @return
	 */
	public ArrayList<String> readFileToArray(String coursePath){ 

		BufferedReader reader;

		ArrayList<String> allWords = new ArrayList<String>();

		//fills allWords
		try {
			reader = new BufferedReader(new FileReader(coursePath)); 
			String line = reader.readLine();

			while (line != null) {
				if (!line.equals("") &&  !line.trim().equals("")){//ignore empty lines
					
					allWords.add(line.trim()); //trims all leading and trailing white spaces in a word, i.e. "this"
				}
				line = reader.readLine();
			}

			reader.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return allWords;
	}

	public Vector<String> getAllLevelsFromCourse(String _coursePath){
		Vector<String>_allLevelsFromCourse = new Vector<String>();

		for (int i = 0; i < readFileToArray( _coursePath).size(); i ++){

			if (Character.toString(readFileToArray( _coursePath).get(i).charAt(0)).equals("%")){
				//if the first char of this string is %, this is a new level
				//add the level name in without the % sign
				_allLevelsFromCourse.add(readFileToArray( _coursePath).get(i).substring(1));
			}
		}
		return _allLevelsFromCourse;
	}

	public ArrayList<String> getLevelWordsFromCourse(String _coursePath,String level) {
		ArrayList<String> allWords = readFileToArray( _coursePath);
		ArrayList<String> levelWords = new ArrayList<String>();

		level = "%" + level;
		int startIndex = 0;

		//find the beginning of the level in the allWord list of the course
		for (int i = 0; i < allWords.size(); i ++){
			if (allWords.get(i).equals(level)){
				startIndex = i;
				break;
			}
		}

		//starting from level position + 1, to the position before the next level
		//these words are the words needed for this level
		for (int j = startIndex + 1; j < allWords.size(); j ++){
			if (Character.toString(allWords.get(j).charAt(0)).equals("%")){
				break;
			}else {
				levelWords.add(allWords.get(j));
			}
		}

		return levelWords;

	}

	/**
	 * Return the current best test score of a course
	 * @param _courseName
	 * @return
	 */
	public int getHighScore(String _courseName) {
		String highscore = readFileToArray(_highScoreFolderPath+_courseName).get(0); //Array should have only one number

		return Integer.parseInt(highscore);

	}

	/**
	 * Set new high score in hidden file
	 */
	public void setNewScore(String _courseName, int _score) {
		String coursePath = _highScoreFolderPath + _courseName;

		//recreate the highscore file with the new score, should replace the original file
		try {
			File stats = new File(coursePath);
			stats.createNewFile();

			FileWriter writer = new FileWriter(stats); 

			writer.write(_score+"\n"); 	

			writer.flush();
			writer.close();
		} catch (IOException e) {

			e.printStackTrace();
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
