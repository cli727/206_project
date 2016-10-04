package VoxSpell;

import java.util.ArrayList;

public class MainMenuModel {

	private MainMenuController _controller;
	private static String _currentCard;
	private static HiddenFilesModel _hiddenFilesModel = HiddenFilesModel.getInstance();
	protected static FestivalModel _festivalModel = FestivalModel.getInstance();;

	public void setController(MainMenuController controller){
		_controller = controller;
	}

	public void newQuiz(){		

		//check if entire file is empty
		if (_hiddenFilesModel.allEmpty(VoxSpellGui.NEW)){	
			_controller.showNoAvailabeWordsPopUP(VoxSpellGui.NEW,true,0);
			return;
		}else {
			//file not empty, show level
			int level = _controller.createAndShowLevelPopUp(VoxSpellGui.NEW);
			
			//check level empty
			if (_hiddenFilesModel.levelEmpty(level,VoxSpellGui.NEW)){
				showNoAvailableWordsPopUp(quizMode,false,level);
				return;
			}

		}

	}

	public void reviewQuiz(){

		//check if entire file is empty
		if (_hiddenFilesModel.allEmpty(VoxSpellGui.REVIEW)){
			_controller.showNoAvailabeWordsPopUP(VoxSpellGui.REVIEW,true,0);
			return;
		}else {
			//file not empty,show level
			int level = _controller.createAndShowLevelPopUp(VoxSpellGui.REVIEW);
			
			//check level empty
			if (_hiddenFilesModel.levelEmpty(level,quizMode)){
				showNoAvailableWordsPopUp(quizMode,false,level);
				return;
			}

		}


		if (level != 0){

			
			

			//create a new quiz card
			Card cardNewOrReviewQuiz = new QuizView(level); 

			//Set their model, view relationship using setters
			//Model and View share the same Festival Model
			quiz.setView((QuizView) cardNewOrReviewQuiz);
			quiz.setInitialLevel(level);
			((QuizView)cardNewOrReviewQuiz).setModel(quiz);
			_festivalModel = FestivalModel.getInstance();
			quiz.setFestivalModel(_festivalModel);
			((QuizView)cardNewOrReviewQuiz).setFestivalModel(_festivalModel);

			//Show quiz panel accordingly
			VoxSpellGui.showCard(cardNewOrReviewQuiz, cardName);

			//Generate quiz words using hiddenFilesModel, start quiz
			ArrayList<ArrayList<String>> allWords = _hiddenFilesModel.readFileToArray(quizMode);
			quiz.setAllWords(allWords);
			quiz.getRandomWords();
		}
	}
}
