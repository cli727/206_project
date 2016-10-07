package VoxSpell;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;

public class ShowAllCourseWordsView extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ShowAllCourseWordsView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 30, 403, 162);
		contentPane.add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"djkls", "sfdshkds", "fdjksjfsl", "fjkds"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = new JLabel("Course Name:");
		lblNewLabel.setBounds(10, 10, 242, 15);
		contentPane.add(lblNewLabel);
		
		JButton btnOkay = new JButton("Okay");
		btnOkay.setBounds(120, 210, 117, 25);
		contentPane.add(btnOkay);
	}
}
