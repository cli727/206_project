package VoxSpell;

import java.util.ArrayList;
import java.util.Vector;

public class ChooseLevelModel {

	private static HiddenFilesModel _hiddenFilesModel ;
	private String _coursePath;


	public ChooseLevelModel(){
		//initialise fields
		_hiddenFilesModel = HiddenFilesModel.getInstance();
	}

	public void setCoursePath(String courseName) {
		_coursePath = courseName;
	}

	public ArrayList<String> getAllWordsFromCourse(){
		ArrayList<String> allWordsFromCourse = _hiddenFilesModel.readFileToArray( _coursePath);
		return allWordsFromCourse;
	}

	public Vector<String> getAllLevelsFromCourse(){
		Vector<String>_allLevelsFromCourse = new Vector<String>();

		for (int i = 0; i < getAllWordsFromCourse().size(); i ++){

			if (Character.toString(getAllWordsFromCourse().get(i).charAt(0)).equals("%")){
				//if the first char of this string is %, this is a new level
				//add the level name in without the % sign
				_allLevelsFromCourse.add(getAllWordsFromCourse().get(i).substring(1));
			}
		}
		return _allLevelsFromCourse;
	}

	public ArrayList<String> getLevelWordsFromCourse(String level) {
		ArrayList<String> allWords = getAllWordsFromCourse();
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


}
