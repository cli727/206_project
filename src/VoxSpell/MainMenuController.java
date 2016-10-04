package VoxSpell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import VoxSpell.VoxSpellGui.LEVEL;

public class MainMenuController implements ActionListener {
	
	private MainMenuView _view;
	private MainMenuModel _model;

	public void setViewModel(MainMenuView view, MainMenuModel model){
		_view = view;
		_model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("_btnScoreHistory")) {
			//new FullStatsView(_frame);
		}
		else if (e.getActionCommand().equals("_btnNewQuiz" )){
			
			_model.newQuiz();
			
		}else if (e.getActionCommand().equals("_btnReview")){
			_model.reviewQuiz();
		}
	}

	public void showNoAvailabeWordsPopUP(String quizMode, boolean allEmpty, int level) {
		_view.noAvailableWordsPopUp(quizMode, allEmpty, level);
		
	}

	public int createAndShowLevelPopUp(String quizMode) {
		int level =_view.levelPopUp(quizMode);
		return level;
	}
	

	

}
