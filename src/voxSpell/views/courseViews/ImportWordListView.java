package voxSpell.views.courseViews;

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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import voxSpell.views.ButtonFactory;
import voxSpell.views.Card;
import voxSpell.views.VoxSpellGui;
import voxSpell.models.hiddenFilesManager.HiddenFilesModel;

/**
 * A Singleton class that shows the screen for user to upload courses. It is a Card object.
 * @author chen
 *
 */
public class ImportWordListView implements Card , ActionListener{

	private JLabel _labelHeading;
	private JLabel _labelMessage;
	private JLabel _exampleHeading;
	private JPanel _exampleImgPanel;

	private JButton _btnImport;
	private JButton _btnBack;

	private Image _example;

	private JFileChooser fc;
	private HiddenFilesModel _hiddenFilesModel;

	private static ImportWordListView _importWordListView;

	private ImportWordListView(){
		//constructor, read example word list image

		try {
			_example = ImageIO.read(new File("./media/wordlistExample.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_exampleImgPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.drawImage(_example, 0, 0,226,408, null);
			}
		};

		_exampleImgPanel.setBackground(Color.white);

		_labelHeading = new JLabel("Customised course format...");
		_labelHeading.setFont(new Font("SansSerif", Font.ITALIC,30));

		_exampleHeading= new JLabel("Example:");
		_exampleHeading.setFont(new Font("SansSerif", Font.BOLD,15));;

		_labelMessage= new JLabel("<html>Please make sure your wordlist meets the <BR>following requirements:"
				+ "<BR><BR><BR> 1) It must contain at least one subgroup. "
				+ "<BR><BR> 2) Each subgroup must begin with '%', <BR> "
				+ "followed by its name. (i.e. %Subgroup One)"
				+ "<BR><BR> 3) The File must begin with the name of <BR>a subgroup."
				+ "<BR><BR> 4) It must not be an empty file."
				+ "<BR><BR> 5) It must not contain an empty level."
				+"<BR><BR>6) It should not be named 'KET' or 'IELTS', <BR> as those are reserved names <BR><BR>" 
				+ "<BR><BR>The image to the left is an example of a wordlist <BR>of 3 subgroups.");

		ButtonFactory btnFactory = new ButtonFactory();
		_btnImport = btnFactory.getButton("./media/upload_course.png","./media/upload_course_hover.png");
		_btnImport.setBackground(Color.white);

		_btnBack = btnFactory.getButton("./media/back_whitebg.png", "./media/back_hover.png");
		_btnBack.setBackground(Color.white);

		_hiddenFilesModel = HiddenFilesModel.getInstance();
	}

	public static synchronized ImportWordListView getInstance(){

		if (_importWordListView == null){
			_importWordListView  = new ImportWordListView();
		}
		return _importWordListView ;
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
		c.gridwidth = 4;
		c.gridheight = 1;
		c.insets = new Insets(0,-50,20,0);
		mainPanel.add(_labelHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.insets = new Insets(10,0,2,0);
		mainPanel.add(_exampleHeading, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 3;
		c.ipadx = 226;
		c.ipady = 408;
		c.insets = new Insets(0,0,0,0);
		mainPanel.add(_exampleImgPanel, c);


		c.ipadx = 0;
		c.ipady = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 2;
		c.insets = new Insets(0,40,0,0);
		mainPanel.add(_labelMessage, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(30,50,0,0);
		mainPanel.add(_btnImport, c);
		_btnImport.addActionListener(this);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(30,40,0,0);
		mainPanel.add(_btnBack, c);
		_btnBack.addActionListener(this);

		return mainPanel;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnBack){
			VoxSpellGui.showCourseChooser("KET");
		}else if (e.getSource() == _btnImport){
			/**
			 * Code copied and modified from oracle jfile chooser demo:
			 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
			 */

			//Create a file chooser
			fc = new JFileChooser();

			//set file chooser to open from current directory instead of from home
			File workingDirectory = new File(System.getProperty("user.dir"));
			fc.setCurrentDirectory(workingDirectory);

			int returnVal = fc.showOpenDialog(VoxSpellGui.getFrame());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				//This is where a real application would open the file.


				if( ! _hiddenFilesModel.copyToCourse(file.getPath(),file.getName())){
					//copyToCourse returned false, the file did not match with the required format
					//show pop up
					String message = "Your file did not match the format specified! \n" + 
							"Please check again before you import this file. \n";

					JOptionPane.showMessageDialog(VoxSpellGui.getFrame(), message, 
							"Format Not Met", JOptionPane.INFORMATION_MESSAGE);
				}else{
					//successful, inform user using pop up pane
					String message = "Your file uploaded successfully! \n" + 
							"You can now view it under 'My Course'. \n";

					JOptionPane.showMessageDialog(VoxSpellGui.getFrame(), message, 
							"Upload Successful", JOptionPane.INFORMATION_MESSAGE);
				}

			} else {
				//cancelled do nothing
			}


		}
	}

}
