package VoxSpell;

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
import java.util.List;
import java.util.Vector;

/**
 * A Singleton class that manages reading/writing actions to the hidden files
 * necessary for running the SpellingAidApp.
 * @author echa232
 *
 */
public class HiddenFilesModel {

	protected enum StatsFile {
		REVIEW, HISTORY
	}

	private static HiddenFilesModel _hiddenFilesModel;

	protected static Path _courseFolderPath;

	protected static Path _reviewFilePath;
	protected static Path _historyFilePath;

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

			Path _reviewFolderPath = Paths.get("./.review");
			Path _historyFilePath = Paths.get("./.history");;

			_festivalFolderPath = Paths.get("./.festival");
			_slowPacedVoiceFilePath = Paths.get("./.festival/slowPacedVoice.scm");
			_americanVoiceFilePath = Paths.get("./.festival/americanVoice.scm");
			_newZealandVoiceFilePath = Paths.get("./.festival/newZealandVoice.scm");


			if (Files.notExists(_reviewFolderPath)) {
				Files.createDirectory(_reviewFolderPath);
			}			
			//set up a review file for each wordlist/course within ./.course

			File folder = new File("./.course");
			File[] listOfFiles = folder.listFiles(); //all file names within ./.course folder

			//for every file in the ./course folder
			for (int i = 0; i < listOfFiles.length; i++ ){

				Path thisReviewFilePath = Paths.get("./.review/" + listOfFiles[i].getName()+"Review");

				//create the review version of the course file, consisting of only level names to begin with
				if (Files.notExists(thisReviewFilePath)) {

					//write to this file the level names only, for appending purpose later 

					//read all words from the COURSE file first
					ArrayList<String> allWords = readFileToArray("./.course/"+ listOfFiles[i].getName());
					ArrayList<String> levelWords = new ArrayList<String>();

					//find level names
					for (int j = 0; j < allWords.size(); j ++){
						if (Character.toString(allWords.get(j).charAt(0)).equals("%")){
							System.out.println("save to level words:" + allWords.get(j));
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
			}

			if (Files.notExists(_historyFilePath)) {
				Files.createFile(_historyFilePath);				
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
	 * add a word into either review / history file, avoid duplications
	 */
	protected void addWordToStatsFile(String filePath, String word) {

		List<String> allWords = new ArrayList<String>();

		try {
			allWords = Files.readAllLines(Paths.get(filePath), StandardCharsets.ISO_8859_1);
			if(! allWords.contains(word)){
				//only add _currentWord into the corresponding file if it does not already exist

				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString(), true));
				writer.append(word+"\n");
				writer.close();

			}

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
	protected void removeWordFromReviewWordsFile(String word, String level, String courseName) {

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
	protected void addWordToReviewWordsFile(String word, String level, String courseName) {

		
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
	 * A private helper method to retrieve the reviewWords file at the corresponding level. 
	 * @param level
	 * @return
	 */

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
	protected ArrayList<String> readFileToArray(String coursePath){ 

		BufferedReader reader;

		ArrayList<String> allWords = new ArrayList<String>();

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

		/*System.out.println("printing level words");
		for (int k = 0; k < levelWords.size(); k ++){
			System.out.println(levelWords.get(k));
		}*/
		return levelWords;

	}

	/**
	 * Method to check if the wordlist file/review files are empty given the quizMode
	 * (No quiz words at all for any levels)
	 * @param quizMode
	 * @return
	 */
	protected boolean allEmpty(String quizMode){
		int nonEmptyLevel = 0;

		ArrayList<String> allWords = _hiddenFilesModel.readFileToArray(quizMode);

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

	//public void checkDuplicates()

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
