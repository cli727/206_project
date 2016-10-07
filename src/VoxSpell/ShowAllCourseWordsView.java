package VoxSpell;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ShowAllCourseWordsView extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ShowAllCourseWordsView(String courseName, final ArrayList<String> allWords, final JPanel parentPanel) {
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		//set the frame to re-enable the chooseLevelView panel when closed
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				System.out.println("Im closed ");
				VoxSpellGui.getFrame().setEnabled(true);
			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 30, 403, 162);
		contentPane.add(scrollPane);

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

		JLabel lblNewLabel = new JLabel(courseName);
		lblNewLabel.setBounds(10, 10, 242, 15);
		contentPane.add(lblNewLabel);

		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VoxSpellGui.getFrame().setEnabled(true);
				dispose();
			}
		});
		btnOkay.setBounds(120, 210, 117, 25);

		contentPane.add(btnOkay);
	}
}
