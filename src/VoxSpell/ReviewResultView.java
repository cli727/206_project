package VoxSpell;

import java.util.Vector;

import javax.swing.JLabel;

public class ReviewResultView extends ResultView{

	public ReviewResultView(String levelName, String courseName, Vector<String> allLevelNames) {
		super(levelName, courseName, allLevelNames);

		_labelQuizMode.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Revision Completed!"+ "</font></html>"));
		
		_labelTableInfo.setText("(Selected items will be removed from revision list)");
	}

	//this method needs to be different from its parent for review mode
	//next level is disabled when next level review words are empty
	@Override
	protected boolean ifDisableNextLevel(){
		
		if ( _hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1)).size() != 0 ){
			// if next level word size is not 0
			return false;
		}else {
			//disable this button
			return true;
		}
	}
}
