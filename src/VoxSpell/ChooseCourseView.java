package VoxSpell;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	private static ChooseCourseView _courseChooser;
	
	private JLabel _labelHeading;
	private JButton _btnWordListOne;
	private JButton _btnWordListTwo;
	private JButton _btnWordListThree;
	private JButton _btnWordListFour;
	private JButton _btnImportWordList;
	private JButton _btnCreateWordList;
	private JButton _btnBackToMain;

	private ChooseCourseView(){
		
		_labelHeading = new JLabel("PLEASE SELECT YOUR COURSE AYE");
		
		/**
		 * CODE FOR CHANGING BUTTON BACKGROUND WHEN HOVERED OVER SOURCED FROM STACK OVERFLOW:
		 * http://stackoverflow.com/questions/18574375/jbutton-with-background-image-changing-on-mouse-hover
		 */
		_btnWordListOne = new JButton("WordListOne");
		//_btnWordListOne.setToolTipText("NOOOOO");
		_btnWordListOne.getModel().addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()){
					//change to another image
					//_btnWordListOne.setIcon(icon());
					_btnWordListOne.setText("IM HOVERED ");
				}else{
					_btnWordListOne.setText("WordListOne");
				}
			}
		});
		
		_btnWordListTwo = new JButton("WordListTwo");
		_btnWordListThree = new JButton("WordListThree");
		_btnWordListFour = new JButton("WordLIstFOur");
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

		mainPanel.setBackground(new Color(6,149,255));
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
		c.gridwidth = 9;
		c.gridheight = 2;
		//c.weightx = 0.3;
		//c.ipady = 200;
		//c.ipadx = 190;
		//c.insets = new Insets(40,10,5,5);
		mainPanel.add(_labelHeading, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.3;
		c.ipady = 200;
		c.ipadx = 190;
		c.insets = new Insets(40,10,5,5);
		mainPanel.add(_btnWordListOne, c);
		_btnWordListOne.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(40,0,5,5);
		mainPanel.add(_btnWordListTwo, c);
		_btnWordListTwo.addActionListener(this);
		

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.2;
		c.insets = new Insets(40,0,5,10);
		mainPanel.add(_btnCreateWordList, c);
		_btnCreateWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		//c.weightx = 0.33;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 3;
		c.gridheight = 2;
		//c.weightx = 0.7;
		c.insets = new Insets(0,10,20,5);
		mainPanel.add(_btnWordListThree, c);
		_btnWordListThree.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,20,5);
		mainPanel.add(_btnWordListFour, c);
		_btnWordListFour.addActionListener(this);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 4;
		c.gridheight = 2;
		c.gridwidth = 3;
		//c.weightx = 0.3;
		c.insets = new Insets(0,0,20,10);
		mainPanel.add(_btnImportWordList, c);
		_btnImportWordList.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 8;
		c.gridy = 6;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 10;
		c.ipady = 10;
		c.weightx = 0.3;
		c.insets = new Insets(10,0,0,10);
		mainPanel.add(_btnBackToMain, c);
		_btnBackToMain.addActionListener(this);

		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBackToMain){
			
			VoxSpellGui.showMainMenu();
		}else if (e.getSource() == _btnWordListOne){
			//show card to select number of words / levels(headings)
			ChooseLevelView cardChooseLevel = new ChooseLevelView("wordlistOne");
			ChooseLevelModel chooseLevelModel = new ChooseLevelModel();
			cardChooseLevel.setModel(chooseLevelModel);
			VoxSpellGui.getInstance().showCard(cardChooseLevel.createAndGetPanel(), "Choose Level");
		}
	}

}
