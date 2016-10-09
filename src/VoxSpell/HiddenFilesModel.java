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

		/*Path filePath = null;

		switch (file) {
		case REVIEW:
			filePath = _reviewFilePath;
			break;
		case HISTORY:
			filePath = _historyFilePath;
			break;
		}*/


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
			System.out.println("next index: " +nextLvlIndex + " total: " + allWords.size());
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

		/*String wordOnNewLine = word + "\n";

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
		}*/
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
		/*else { //it is the "review mistakes" mode			
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
		}*/

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

	/**
	 * Check if there are quiz words for the level chosen
	 * @param level
	 * @param quizMode
	 * @return
	 */
	protected boolean levelEmpty(int level, String quizMode){

		if(quizMode.equals(VoxSpellGui.REVIEW)){

			ArrayList<String> allWords = _hiddenFilesModel.readFileToArray(coursePath);

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
