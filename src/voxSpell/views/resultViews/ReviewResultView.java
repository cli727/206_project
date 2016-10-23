package voxSpell.views.resultViews;

import java.awt.event.ActionEvent;
import java.util.Vector;
import voxSpell.views.VoxSpellGui;
import voxSpell.views.quizViews.QuizView;
import voxSpell.models.quizModels.QuizModel;
import voxSpell.models.resultModels.ResultModel;

/**
 * View that shows the result at the end of a review quiz. It contains a JTable component. It is a Card Object.
 * It is a child of parent result view. 
 * @author chen
 *
 */
public class ReviewResultView extends ResultView{
	
	public ReviewResultView(String levelName, String courseName, Vector<String> allLevelNames) {
		super(levelName, courseName, allLevelNames);

		_labelQuizMode.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Revision Completed!"+ "</font></html>"));

		_labelTableInfo.setText("(Selected items will be removed from revision list)");

		if (! ifDisableNextLevel()){ //only get next words if not last level

			for(int i = _allLevelNames.indexOf(_thisLevelName)+1; i < _allLevelNames.size(); i++){ //loop through to find the next non empty level
				
				if (! _hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_allLevelNames.get(i)).isEmpty()){
					//if non empty level found, this is the next level
					_nextLevelWords = _hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_allLevelNames.get(i));
					_nextNonEmptyLevel = _allLevelNames.get(i);
					break;
				}else {
					_nextNonEmptyLevel = null;
				}
			}
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
			//show quiz card of this level again, with the remaining words 

			QuizView quizView = new QuizView(_thisLevelName, _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			//only words not remembered are quizzed, therefore number of NON SELECTED sells
			//NON SELECTED = TOTAL ROW COUNT - SELECTED
			int numWordToQuiz = _resultTable.getRowCount() - ((ResultModel) _model).getNumSelectedWords();
			
			quizModel.setAllWords(_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",_thisLevelName), numWordToQuiz);

			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Practice Again Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnNextLevel){
			//this button is clicked so this button must not be disabled
			//show quizView of next level

			//get words for next level

			QuizView quizView = new QuizView(_nextNonEmptyLevel, _courseName);
			QuizModel quizModel = new QuizModel(_allLevelNames);

			quizModel.setView(quizView);
			quizView.setModel(quizModel);

			quizModel.setAllWords(_nextLevelWords, _numWordsToQuiz); 

			//show next level quiz card
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Next Level Quiz");
			quizModel.getRandomWords();

		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}
	
	/**
	 * Called by its model to disable the "Retry" button, which is when all rows of the JTable are selected
	 */
	@Override
	public void ifDisableRetry(int count){
		if (count == _model.getRowCount()){
			_btnPracticeAgain.setEnabled(false);
		}else{
			_btnPracticeAgain.setEnabled(true);
		}
	}
}
