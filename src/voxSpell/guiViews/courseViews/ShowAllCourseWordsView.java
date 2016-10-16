package voxSpell.guiViews.courseViews;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxSpell.guiViews.VoxSpellGui;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class ShowAllCourseWordsView extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ShowAllCourseWordsView(String courseName, final ArrayList<String> allWords, final JPanel parentPanel) {
		
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
		

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new BorderLayout(5,5));
		
		JLabel lblNewLabel = new JLabel(courseName);
		contentPane.add(lblNewLabel,BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
	
		JList list = new JList();
		list.setModel(new AbstractListModel() {

			public int getSize() {
				return allWords.size();
			}
			public Object getElementAt(int index) {
				return allWords.get(index);
			}
		});
		scrollPane.setViewportView(list);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VoxSpellGui.getFrame().setEnabled(true);
				dispose();
			}
		});

		contentPane.add(btnOkay, BorderLayout.SOUTH);
		
		setVisible(true);
	}
}
