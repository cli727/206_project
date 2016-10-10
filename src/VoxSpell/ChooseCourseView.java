package VoxSpell;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Singleton class. Showing the window for choosing vocabulary course/wordlists
 * @author chen
 *
 */

public class ChooseCourseView implements Card, ActionListener{

	private Color _bgColor;

	private static ChooseCourseView _courseChooser;

	private JLabel _labelHeading;
	private JButton _btnKEYwords;
	private JButton _btnWordListTwo;
	private JButton _btnIELTSwords;
	private JButton _btnWordListFour;
	private JButton _btnImportWordList;
	private JButton _btnCreateWordList;
	private JButton _btnBackToMain;

	private HiddenFilesModel _hiddenFilesModel;

	private ChooseCourseView(){
		_hiddenFilesModel = HiddenFilesModel.getInstance();
		
		//set background colour for this card
		_bgColor = new Color(129,224,253);

		//create new Font
		Font headingFont = new Font("SansSerif", Font.ITALIC,30);

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='white'>"
				+ "Select your course...</font></html>"));

		_labelHeading.setFont(headingFont);

		/**
		 * CODE FOR CHANGING BUTTON BACKGROUND WHEN HOVERED OVER SOURCED FROM STACK OVERFLOW:
		 * http://stackoverflow.com/questions/18574375/jbutton-with-background-image-changing-on-mouse-hover
		 */
		_btnKEYwords = new JButton("KEY");
		//_btnKEYwords.setToolTipText("NOOOOO");
		_btnKEYwords.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					//_btnKEYwords.setIcon(icon());
					_btnKEYwords.setText("IM HOVERED ");
				}else{
					_btnKEYwords.setText("KEY");
				}
			}
		});

		_btnIELTSwords = new JButton("IELTS");
		_btnImportWordList = new JButton("Import WordList");
		_btnCreateWordList = new JButton("CreateWordLIst");
		_btnBackToMain = new JButton("Back");
	}

	public static synchronized ChooseCourseView getInstance(){

		if (_courseChooser == null){
			_courseChooser = new ChooseCourseView();
		}
		return _courseChooser;
	}

	@Override
	public JPanel createAndGetPanel() {


		/*add(new JLabel("<html> <p style='text-align: center;font-size:13px;padding:8;'>"
					+ " Welcome To VOXSPELL!</html>", 
					JLabel.CENTER),BorderLayout.NORTH);*/
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(_bgColor);
		/**
		 * DECLARATION: THE FOLLOWING METHOD ON JAVA GRIDBAG LAYOUT ARE SOURCED 
		 * AND EDITED FROM THE ORACLE TUTORIAL WEBPAGE
		 * URL: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
		 */
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 6;
		c.gridheight = 2;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		c.insets = new Insets(0,40,0,0);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.ipady = 180;
		c.ipadx = 310;
		c.insets = new Insets(40,10,5,5);
		mainPanel.add(_btnKEYwords, c);
		_btnKEYwords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(40,0,5,10);
		mainPanel.add(_btnIELTSwords, c);
		_btnIELTSwords.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,10,0,5);
		mainPanel.add(_btnCreateWordList, c);
		_btnCreateWordList.addActionListener(this);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,0,10);
		mainPanel.add(_btnImportWordList, c);
		_btnImportWordList.addActionListener(this);

		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBackToMain){

			VoxSpellGui.showMainMenu();
		}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.TEST)){
			//test mode, show quiz view after
			System.out.println(VoxSpellGui.STATUS);
			if (e.getSource() == _btnKEYwords){

				QuizView quizView = new TestQuizView(null,"KEY");//level not needed for test view
				QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/KEY"));

				quizModel.setView(quizView);
				
				ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/KEY");
				for(int i = 0; i < allWords.size(); i++){
					if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
						allWords.remove(allWords.get(i));
					}
				}
				quizModel.setAllWords(allWords, 10); //just 10 words always for test mode

				quizView.setModel(quizModel);
				VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
				quizModel.getRandomWords();


			}else if(e.getSource() == _btnIELTSwords){

				
			}

		}else if (! VoxSpellGui.STATUS.equals(VoxSpellGui.TEST)){
			//new/review mode, show level chooser after
			if (e.getSource() == _btnKEYwords){

				//show card to select number of words / levels(headings)
				ChooseLevelView cardChooseLevel = null;

				if(VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){

					cardChooseLevel = new ChooseLevelView("KEY");
				}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){

					cardChooseLevel = new ChooseLevelReviewView("KEY");
				}
				
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");

			}else if(e.getSource() == _btnIELTSwords){

				//show card to select number of words / levels(headings)
				ChooseLevelView cardChooseLevel = null;

				if(VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){

					cardChooseLevel = new ChooseLevelView("IELTS");
				}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){

					cardChooseLevel = new ChooseLevelReviewView("IELTS");
				}
				
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
			}
		}

	}

}
