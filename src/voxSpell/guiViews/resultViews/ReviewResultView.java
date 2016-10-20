package voxSpell.guiViews.resultViews;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;

import voxSpell.guiViews.VoxSpellGui;
import voxSpell.guiViews.quizViews.QuizView;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.models.resultModels.ResultModel;

public class ReviewResultView extends ResultView{

	public ReviewResultView(String levelName, String courseName, Vector<String> allLevelNames) {
		super(levelName, courseName, allLevelNames);

		_labelQuizMode.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Revision Completed!"+ "</font></html>"));
		
		_labelTableInfo.setText("(Selected items will be removed from revision list)");
	}

	/**
	 * this method needs to be different from its parent for review mode
	 * next level is disabled when next level review words are empty
	 */
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
	
	/**
	 * Review mode reads from a different file when buttons are pressed
	 * Review mode also needs to disable the 'next level' button if the user selects all words in the table
	 * i.e. remove all words from review list, therefore nothing to 'practice again'
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//regardless of the button pressed, keep a record of the words to review
		((ResultModel) _model).keepRecordOfSelectedWords();

		if (e.getSource() == _btnPracticeAgain){
			//show quiz card of this level again

			QuizView quizView = new QuizView(_thisLevelName, _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			quizModel.setAllWords(_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_thisLevelName), _model.getRowCount());

			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Practice Again Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnNextLevel){
			//this button is clicked so this button must not be disabled
			//show quizView of next level

			//get words for next level
			ArrayList<String> newWords = _hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1));

			QuizView quizView = new QuizView(_allLevelNames.get(_allLevelNames.indexOf(_thisLevelName)+1), _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			quizModel.setAllWords(newWords, _model.getRowCount()); 

			//show next level quiz card
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Next Level Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}
}
