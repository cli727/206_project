package voxSpell.views.courseViews;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxSpell.models.hiddenFilesManager.HiddenFilesModel;
import voxSpell.status.QuizStatus;
import voxSpell.views.VoxSpellGui;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class ShowAllCourseWordsView extends JFrame {

	private JPanel contentPane;
	private HiddenFilesModel _hiddenFilesModel;

	/**
	 * Create the frame.
	 */
	public ShowAllCourseWordsView(String courseName, final JPanel parentPanel) {

		setLocationRelativeTo(VoxSpellGui.getFrame());
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 100, 700, 600);
		setBackground(Color.WHITE);

		//set the frame to re-enable the chooseLevelView panel when closed
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				VoxSpellGui.getFrame().setEnabled(true);
			}
		});

		_hiddenFilesModel = HiddenFilesModel.getInstance();
		Vector<String> allLevelNames = new Vector<String>();

		allLevelNames =  _hiddenFilesModel.getAllLevelsFromCourse("./.course/"+courseName);

		contentPane = new JPanel(new GridLayout(1, 1));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.WHITE);


		JTabbedPane tabbedPane = new JTabbedPane();

		for (int i = 0; i < allLevelNames.size(); i++){
			//create a tab for all levels
			
			JComponent panel = null;

			if (VoxSpellGui.STATUS.equals(QuizStatus.NEW)){
				panel = makeTextPanel(_hiddenFilesModel.getLevelWordsFromCourse("./.course/"+courseName, allLevelNames.get(i)));

			}else if (VoxSpellGui.STATUS.equals(QuizStatus.REVIEW)){

				panel = makeTextPanel(_hiddenFilesModel.getLevelWordsFromCourse("./.review/"+courseName+"Review", allLevelNames.get(i)));
			}
			
			tabbedPane.addTab(allLevelNames.get(i),null, panel,
					allLevelNames.get(i));
		}


		//Add the tabbed pane to this panel.
		contentPane.add(tabbedPane);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);


		setVisible(true);
	}


	protected JComponent makeTextPanel(final ArrayList<String> levelWords) {

		JScrollPane scrollPane = new JScrollPane();

		JList list = new JList();
		list.setModel(new AbstractListModel() {

			public int getSize() {
				return levelWords.size();
			}
			public Object getElementAt(int index) {
				return levelWords.get(index);
			}
		});
		scrollPane.setViewportView(list);
		
		return scrollPane;
	}
}
