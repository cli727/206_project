package VoxSpell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ShowImportedWordListView implements Card, ActionListener, ListSelectionListener{


	private JLabel _labelHeading;

	private JLabel _courseHeading;
	private JScrollPane _coursePane;

	private JButton _btnUseList;
	private JButton _btnDeleteList;
	private JButton _btnBack;

	private HiddenFilesModel _hiddenFilesModel;

	JList _courseList; 

	private static ShowImportedWordListView _importedWordListView;

	public ShowImportedWordListView(){
		//constructor, read example word list image

		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_labelHeading = new JLabel("Your uploaded courses...");
		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,30));

		_courseHeading= new JLabel("Choose your course from below: ");
		_courseHeading.setFont(new Font("SansSerif", Font.BOLD,15));;

		_coursePane= new JScrollPane();

		_btnUseList = new JButton("Set Course");
		_btnDeleteList = new JButton ("Delete Course");
		_btnBack = new JButton("Back");

		//_coursePane.setBounds(113, 63, 151, 184);

		//get list of imported courses from hiddenFilesModel and display them
		final ArrayList<String> courseNames =  _hiddenFilesModel.getImportedCourseNames();

		//final String[] courseNamesArray = (String[]) courseNames.toArray();

		_courseList = new JList();
		_courseList.addListSelectionListener(this);
		_courseList.setModel(new AbstractListModel() {

			public int getSize() {
				return courseNames.size();
			}
			public Object getElementAt(int index) {
				return courseNames.get(index);
			}
		});

		if (courseNames.size() == 0){
			//no imported course
			_courseHeading.setText("No currently available course.");

			_btnUseList.setEnabled(false);
			_btnDeleteList.setEnabled(false);
			
			//an empty model, for formatting sake
			JList _emptyList = new JList();
			_emptyList.setModel(new AbstractListModel() {

				public int getSize() {
					return 1;
				}
				public Object getElementAt(int index) {
					return " ";
				}
			});

			_coursePane.setViewportView(_emptyList);
			
		}else{
			_coursePane.setViewportView(_courseList);
		}



		_hiddenFilesModel = HiddenFilesModel.getInstance();

		if (_courseList.isSelectionEmpty()){
			_btnUseList.setEnabled(false);
			_btnDeleteList.setEnabled(false);
		}
	}

	@Override
	public JPanel createAndGetPanel() {
		JPanel mainPanel = new JPanel();

		mainPanel.setBackground(Color.white);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(0,-150,0,0);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(10,0,2,0);
		mainPanel.add(_courseHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.ipadx = 500;
		c.ipady = 280;
		c.insets = new Insets(0,0,0,0);
		mainPanel.add(_coursePane, c);


		c.ipadx = 0;
		c.ipady = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(30,0,0,0);
		mainPanel.add(_btnUseList, c);
		_btnUseList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(30,10,0,0);
		mainPanel.add(_btnDeleteList, c);
		_btnDeleteList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(30,10,0,0);
		mainPanel.add(_btnBack, c);
		_btnBack.addActionListener(this);

		return mainPanel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBack){
			VoxSpellGui.showCourseChooser("KET");
		}else if (e.getSource() == _btnUseList){
			//show card  based on quiz status
			
			String courseName = _courseList.getSelectedValue().toString();
			
			ChooseLevelView cardChooseLevel = null;

			if(VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){

				cardChooseLevel = new ChooseLevelView(courseName);
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
				
			}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){

				cardChooseLevel = new ChooseLevelReviewView(courseName);
				VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
				
			}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.TEST)){
				
				//test view
				QuizView quizView = new TestQuizView(null,courseName,0);//level not needed for test view
				QuizModel quizModel = new TestQuizModel(_hiddenFilesModel.getAllLevelsFromCourse("./.course/"+courseName));

				quizModel.setView(quizView);

				ArrayList<String> allWords = _hiddenFilesModel.readFileToArray("./.course/"+courseName);
				for(int i = 0; i < allWords.size(); i++){
					if (Character.toString(allWords.get(i).charAt(0)).equals("%")){
						allWords.remove(allWords.get(i));
					}
				}
				
				//get 10 words for test, all words in course if the course has less than 10 words
				int numWordsToQuiz = 10;

				if(allWords.size() < 10){
					numWordsToQuiz = allWords.size();
				}
				quizModel.setAllWords(allWords, numWordsToQuiz); 

				quizView.setModel(quizModel);
				VoxSpellGui.getInstance().showCard(quizView.createAndGetPanel(), "Test Quiz");
				quizModel.getRandomWords();
			}

		}else if (e.getSource() == _btnDeleteList){
			//warning message

			int dialogResult  = JOptionPane.showOptionDialog(VoxSpellGui.getFrame(), 
					("<html>Warning: <BR>" + "<BR>" +
							"Are you sure you want to delete this course?  <BR>" + 
							"All of its statistics will be deleted.<BR>" + 
							"   </html>"),
					"Delete Course", 
					JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.INFORMATION_MESSAGE, 
					null, 
					new String[]{"Delete", "Cancel"}, 
					"default");

			String courseName = _courseList.getSelectedValue().toString();

			if(dialogResult == JOptionPane.YES_OPTION){
				//delete its stats
				if (_hiddenFilesModel.deleteCourseStats(courseName)){
					//successful
					//successful, inform user using pop up pane
					String message = "Course Deleted! \n";

					JOptionPane.showMessageDialog(VoxSpellGui.getFrame(), message, 
							"Deletion Successful", JOptionPane.INFORMATION_MESSAGE);

					//show this card again, refreshing jlist
					ShowImportedWordListView importedWordListView = new ShowImportedWordListView();
					VoxSpellGui.getInstance().showCard(importedWordListView.createAndGetPanel(), "Show Imported WordList");
				}

			}

		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		//enable buttons
		if (! _courseList.isSelectionEmpty()){
			_btnUseList.setEnabled(true);
			_btnDeleteList.setEnabled(true);
		}


	}
}
