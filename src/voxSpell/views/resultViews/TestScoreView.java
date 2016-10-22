package voxSpell.views.resultViews;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
import voxSpell.models.resultModels.TestScoreModel;

public class TestScoreView extends JTableView implements Card, ActionListener{
	
	private HiddenFilesModel _hiddenFilesModel;

	private String _courseName;
	
	private JLabel _labelTableInfo;

	private JLabel _labelScoreTitle;
	private JLabel _labelScore;
	private JLabel _labelQuizMode;

	private JButton _btnChangeCourse;
	private JButton _btnClearStats;

	public TestScoreView(String courseName,  int score) {
		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_courseName = courseName;

		_labelQuizMode = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Course: " + _courseName + "</font></html>"));
		_labelQuizMode.setFont((new Font("SansSerif", Font.ITALIC,30)));

		_labelScoreTitle = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Best Score" + "</font></html>"));

		_labelScoreTitle.setFont(new Font("SansSerif", Font.ITALIC,20));

		_labelScore = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ +score+ "</font></html>"));
		_labelScore.setFont((new Font("SansSerif", Font.ITALIC,45)));
		
		_labelTableInfo = new JLabel(); //info depends on whether the stats is empty
		_labelTableInfo.setFont(new Font("SansSerif" , Font.PLAIN, 15));

		_tablePanel = new JPanel();

		ButtonFactory btnFactory = new ButtonFactory();
		_btnClearStats = btnFactory.getButton("./media/clear_statistics.png","./media/clear_statistics_hover.png");
		_btnClearStats.setBackground(Color.white);


		_btnChangeCourse = btnFactory.getButton("./media/back_whitebg.png", "./media/back_hover.png");
		_btnChangeCourse.setBackground(Color.white);

	}
	
	@Override
	public JPanel createAndGetPanel() {
		
		if (((TestScoreModel) _model).emptyStats()){
			_labelTableInfo.setText("No history available. Let's start a test.");
			_btnClearStats.setEnabled(false);
		}else{
			_labelTableInfo.setText("Attempted word history in alphabetical order.");
		}

		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);

		_tablePanel.setBackground(Color.white);

		//create result table from the set model
		_resultTable = new JTable(_model);
		_resultTable.setPreferredScrollableViewportSize(new Dimension(500, 330));
		//_resultTable.setFillsViewportHeight(true);
		_tablePanel.add(_resultTable);
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_tablePanel.add(scrollPane, BorderLayout.NORTH);
		

		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(20,-15,0,0);
		resultPanel.add(_labelQuizMode, c);
		

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(30,60,0,0);
		resultPanel.add(_labelTableInfo, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,180,0,0);
		resultPanel.add(_labelScoreTitle, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,205,0,0);
		resultPanel.add(_labelScore, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.insets = new Insets(0,-10,0,0);
		resultPanel.add(_tablePanel, c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(15,150,0,0);
		resultPanel.add(_btnClearStats, c);
		_btnClearStats.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		c.insets = new Insets(15,50,0,150);
		resultPanel.add(_btnChangeCourse, c);
		_btnChangeCourse.addActionListener(this);


		return resultPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnChangeCourse){
			VoxSpellGui.showCourseChooser(null);
		}else if(e.getSource() == _btnClearStats){
			
			//warning message

			int dialogResult  = JOptionPane.showOptionDialog(VoxSpellGui.getFrame(), 
					("<html>Warning: <BR>" + "<BR>" +
							"Are you sure you want to clear the statistics of this course?  <BR>" + 
							"This will affect its revision list and entire test history.  </html>"),
					"Clear Course Statistics", 
					JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.INFORMATION_MESSAGE, 
					null, 
					new String[]{"Clear", "Cancel"}, 
					"default");

			if(dialogResult == JOptionPane.YES_OPTION){
				//delete its stats
				if (_hiddenFilesModel.deleteCourseStats(_courseName)){
					//successful
					//successful, inform user using pop up pane
					String message = "Course Statistics Cleared! \n";

					JOptionPane.showMessageDialog(VoxSpellGui.getFrame(), message, 
							"Successful", JOptionPane.INFORMATION_MESSAGE);

					//show this card again, refreshing jlist
					//show stats view
					TestScoreView testScoreView = new TestScoreView(_courseName,  _hiddenFilesModel.getHighScore(_courseName));
					TestScoreModel testScoreModel = new TestScoreModel(_courseName);
					
					testScoreView.setModel(testScoreModel);
									
					VoxSpellGui.getInstance().showCard(testScoreView.createAndGetPanel(), "Test Scores");
				}

			}
		}
		
	}
}
