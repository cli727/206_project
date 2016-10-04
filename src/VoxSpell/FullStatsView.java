package VoxSpell;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Shows statistics in a pop-up dialog window.
 * Statistics are shown in a scrollable table format.
 * While statistics is being shown, user may not interact with other aspects 
 * (e.g. start new spelling quiz etc) of the Spelling Aid App.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class FullStatsView extends JDialog implements ActionListener {
	
	JTable _statsTable;
	JButton _clearStatsButton;
	JButton _returnButton;
	
	HiddenFilesModel _hiddenFilesModel;

	public FullStatsView(JFrame owner) {
		super(owner, "Statistics", true);
		createAndBuildDialog();
	}

	private void createAndBuildDialog() {

		showStatistics();
		setupButtons();

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void showStatistics() {
		_statsTable = new JTable(new StatsModel());
		_statsTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
		_statsTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(_statsTable);
		this.getContentPane().add(scrollPane, BorderLayout.NORTH);
	}

	private void setupButtons() {
		JPanel buttonsPanel = new JPanel();
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.CENTER);
		
		buttonsPanel.setLayout(flowLayout);
		
		_clearStatsButton = new JButton("Clear all statistics");
		_clearStatsButton.addActionListener(this);
		buttonsPanel.add(_clearStatsButton);

		_returnButton = new JButton("Return");
		_returnButton.addActionListener(this);
		buttonsPanel.add(_returnButton);
		
		this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _clearStatsButton) {
			int clear = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all statistics?", "Confirm Clear Statistics", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (clear == JOptionPane.YES_OPTION) {
				HiddenFilesModel.getInstance().clearStats();
				JOptionPane.showMessageDialog(null, "All statistics cleared.");
				closeStatsView();
			}
		} else if (e.getSource() == _returnButton) {
			closeStatsView();
		}
	}
	
	private void closeStatsView() {
		this.setVisible(false);
		this.dispose();
	}
}
