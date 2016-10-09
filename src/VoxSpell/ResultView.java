package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import VoxSpell.FestivalModel.Voice;

public class ResultView implements Card, ActionListener {

	private JLabel _labelHeading;

	private JPanel _resultPanel;
	private JTable _resultTable;
	private JButton _btnNextLevel;
	private JButton _btnRaceLevel;
	private JButton _btnPracticeAgain;
	private JButton _btnHome;
	
	private int _numOfButtons; //an int indicating how many JCheckBox buttons are needed

	private ResultModel _model;

	public ResultView(int numOfButtons){
		_numOfButtons = numOfButtons;

		_labelHeading = new JLabel(("<html> <p style='text-align:center;'>"
				+ "<font color='black'>"
				+ "Practice Quiz Completed!"+ "</font></html>"));

		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,30));

		_resultPanel = new JPanel();

		_btnPracticeAgain = new JButton("Practice Again");
		_btnNextLevel = new JButton("Practice Next Level");
		_btnRaceLevel = new JButton("Test This Level");
		_btnHome = new JButton("Home");
	}
	
	public void setModel(ResultModel model){
		_model = model;
	}

	@Override
	public JPanel createAndGetPanel() {
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.white);

		
		resultPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		resultPanel.add(_labelHeading, c);


		//create result table from the set model
		_resultTable = new JTable(_model);
		_resultTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
		//_resultTable.setFillsViewportHeight(true);
		_resultPanel.add(_resultTable);
		JScrollPane scrollPane = new JScrollPane(_resultTable);
		_resultPanel.add(scrollPane, BorderLayout.NORTH);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 4;
		c.gridheight = 3;
		c.insets = new Insets(5,0,0,0);
		resultPanel.add(_resultPanel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.3;
		//c.insets = new Insets(25,0,20,0);
		resultPanel.add(_btnPracticeAgain, c);
		_btnPracticeAgain.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
	//	c.insets = new Insets(5,55,30,0);
		resultPanel.add(_btnNextLevel, c);
		_btnNextLevel.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.3;
		//c.insets = new Insets(5,55,0,0);
		resultPanel.add(_btnRaceLevel, c);
		_btnRaceLevel.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		//c.weightx = 0.2;
		//c.insets = new Insets(0,55,10,0);
		resultPanel.add(_btnHome, c);
		_btnHome.addActionListener(this);


		return resultPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == _btnPracticeAgain){

			
		}else if (e.getSource() == _btnNextLevel){

			
		}else if (e.getSource() == _btnRaceLevel){


		}else if (e.getSource() == _btnHome){
			VoxSpellGui.showMainMenu();
		}
	}

}
