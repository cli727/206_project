package voxSpell.guiViews.levelViews;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import voxSpell.guiViews.levelViews.ChooseLevelView;
import voxSpell.guiViews.VoxSpellGui;
import voxSpell.guiViews.courseViews.ShowAllCourseWordsView;
import voxSpell.guiViews.quizViews.QuizView;
import voxSpell.models.quizModels.QuizModel;

public class ChooseLevelReviewView extends ChooseLevelView implements ActionListener {

	boolean _noReviewWords;

	public ChooseLevelReviewView(String courseName) {

		super(courseName);
		//redefine some fields
		_labelHeading.setText(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ "Set up your review quiz...</font></html>"));

		//items in combo box are all levels that are NOT empty
		Vector<String> allLevels = _hiddenFilesModel.getAllLevelsFromCourse("./.review/"+_courseName+"Review");
		Vector<String> nonEmptyLevels = new Vector<String>();

		for (int i = 0; i < allLevels.size(); i ++){
			//get words within this level, check if empty
			ArrayList<String> levelWords =_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review", allLevels.get(i));

			if (levelWords.size() != 0){
				//only add non empty levels
				nonEmptyLevels.add(allLevels.get(i));
			}
		}


		if(nonEmptyLevels.size() == 0){
			//if no words to review at all
			_labelChooseLevel.setText("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
					"No available review words!"+"</font></html>");

			_btnAllWords.setEnabled(false);
			_btnStartQuiz.setEnabled(false);

			_noReviewWords = true;

		}

		//this block just ensures that there are texts inside combo box at all times, for format issues
		if ( _noReviewWords){
			nonEmptyLevels.clear();
			nonEmptyLevels.add("(try a practice quiz first)");
		}

		_comboBox = new JComboBox<String>(nonEmptyLevels);

	}

	//review mode will need to set the coursePath differently when buttons are clicked
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showMainMenu();

		}else if(e.getSource() == _btnViewWordList) {

			System.out.println("./.review/"+_courseName);
			for(int i = 0; i < _hiddenFilesModel.readFileToArray("./.review/"+_courseName+"Review").size();i++){
				System.out.println("whats in list"+_hiddenFilesModel.readFileToArray("./.review/"+_courseName+"Review").get(i));
			}
			//create frame to show all words in the selected course
			new ShowAllCourseWordsView(_courseName, _hiddenFilesModel.readFileToArray("./.review/"+_courseName+"Review"), _chooseLevelPanel);			
			//disable this frame
			VoxSpellGui.getFrame().setEnabled(false);

		}else if (e.getSource() == _btnChangeCourse){

			VoxSpellGui.showCourseChooser(_courseName);
		}else if (e.getSource() == _comboBox){

			//disable number of word radiobuttons accordingly
			//get number of words in selected level

			disableNumWordsButtons();

		}else if (e.getSource() == _btnTenWords){

			_numWordsToQuiz = 10;
		}else if (e.getSource() == _btnTwentyWords){

			_numWordsToQuiz = 20;
		}else if (e.getSource() == _btnFortyWords){

			_numWordsToQuiz = 40;
		}else if (e.getSource() == _btnFiftyWords){

			_numWordsToQuiz = 50;
		}else if (e.getSource() == _btnAllWords){

			_numWordsToQuiz = _hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName,(String) _comboBox.getSelectedItem()).size();
		}else if (e.getSource() == _btnStartQuiz){

			String level =  (String) _comboBox.getSelectedItem();

			/*if (_quizAllWords){
				_numWordsToQuiz = _model.getLevelWordsFromCourse(level).size();
			}*/
			System.out.println("level "+level);
			System.out.println("words " + _numWordsToQuiz);

			QuizView quizView =null;
			QuizModel quizModel = null;

			//create regular quiz view otherwise
			quizView = new QuizView(level, _courseName);
			quizModel = new QuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.review/" + _courseName+"Review"));

			quizModel.setView(quizView);
			quizModel.setAllWords(_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",level), _numWordsToQuiz);

			quizView.setModel(quizModel);
			VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Practice Quiz");
			quizModel.getRandomWords();
		}

	}

	/**
	 * For review mode, the number of words to quiz reads from a different file
	 * It also needs to deal with the case where there is no words to quiz from all levels, 
	 * which is not an issue for practice  or test mode
	 */
	@Override	
	protected void disableNumWordsButtons() {

		_numWordsToQuiz=_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+_courseName+"Review",(String) _comboBox.getSelectedItem()).size();
		//disable buttons accordingly based on numWOrds
		if (_numWordsToQuiz < 50){
			_btnFiftyWords.setEnabled(false);
			_btnFiftyWords.setForeground(Color.gray);
		}else{
			_btnFiftyWords.setEnabled(true);
			_btnFiftyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 40){
			_btnFortyWords.setEnabled(false);
			_btnFortyWords.setForeground(Color.gray);
		}else{
			_btnFortyWords.setEnabled(true);
			_btnFortyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 20){
			_btnTwentyWords.setEnabled(false);
			_btnTwentyWords.setForeground(Color.gray);
		}else{
			_btnTwentyWords.setEnabled(true);
			_btnTwentyWords.setForeground(Color.white);
		}

		if (_numWordsToQuiz < 10){
			_btnTenWords.setEnabled(false);
			_btnTenWords.setForeground(Color.gray);
		}else{
			_btnTenWords.setEnabled(true);
			_btnTenWords.setForeground(Color.white);
		}

		if (!_noReviewWords){
			//always enable 'get all words' button, unless there is no words
			if (_numWordsToQuiz == 0){
				//disable all buttons
				_btnAllWords.setEnabled(false);
				_btnStartQuiz.setEnabled(false);

				//tell user that there is no word to quiz 
				_labelChooseLevel.setText("<html> <p style='text-align: center;font-size:13px;padding:2;'<font color=white>"+
						"This subgroup has no available words!"+"</font></html>");
			}else{
				_btnAllWords.setEnabled(true);
				_btnStartQuiz.setEnabled(true);
				_labelChooseLevel.setText("<html> <p style='text-align: center;font-size:13px;padding:2;'>"+
						"<font color='white'>"
						+ "Choose a subgroup</font></html>");
			}
		}
	}
}
