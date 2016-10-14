package videoPlayer;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import VoxSpell.HiddenFilesModel;
import VoxSpell.VoxSpellGui;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
/**
 * A video player that has an embedded media player using the vlcj external library
 * @author chen
 *
 */
public class VideoPlayer implements ActionListener,MouseListener{

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer video;

	private final String videoOutput;

	private JPanel vlcButtons;
	private JPanel ffmpegButtons;

	private final JFrame frame;
	private JDialog dialog;
	/*
	 * Example downloaded from the ACP exercise that Nasser gave
	 */
	private JButton pauseBtn;
	private JButton btnMute;
	private JButton btnSkip;
	private JButton btnSkipBack;
	private JLabel labelTime;

	private JButton fasterBtn;
	private HiddenFilesModel _hiddenFilesModel;
	private JButton slowerBtn;
	private JLabel versionLabel;
	private JButton normalBtn;
	private JButton invertBtn;

	public VideoPlayer() {
		videoOutput = ".output.mpg";
		_hiddenFilesModel = HiddenFilesModel.getInstance();

		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/usr/lib/" 
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);


		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		mediaPlayerComponent.getVideoSurface().addMouseListener(this);

		video = mediaPlayerComponent.getMediaPlayer();

		video.addMediaPlayerEventListener(

				new MediaPlayerEventAdapter() {
					@Override
					public void finished (MediaPlayer mediaPlayer) {
						_hiddenFilesModel.deleteVideoFile(videoOutput);
					}
				});

		frame = new JFrame("Reward Video");
		frame.setAlwaysOnTop(true);
		//if the video frame is closed, stop video, delete the produced video
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				video.stop();
				_hiddenFilesModel.deleteVideoFile(videoOutput);
				frame.dispose();
				VoxSpellGui.getFrame().setEnabled(true);
			}
		});

		frame.setLayout(new BorderLayout());

		frame.setContentPane(mediaPlayerComponent);

		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.CENTER); 

		vlcButtons = new JPanel();
		vlcButtons.setLayout(flowLayout);
		vlcButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		ffmpegButtons = new JPanel();
		ffmpegButtons.setLayout(flowLayout);
		ffmpegButtons.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		addButtons();
	}

	public void addButtons(){

		// add play button
		pauseBtn = new JButton("Play");
		pauseBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				video.play();
			}

		});
		vlcButtons.add(pauseBtn);

		// add stop button
		pauseBtn = new JButton("Stop");
		pauseBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
			}

		});
		vlcButtons.add(pauseBtn);

		// add pause button
		pauseBtn = new JButton("Pause");
		pauseBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();
			}

		});
		vlcButtons.add(pauseBtn);

		//add mute button
		btnMute = new JButton("Mute");
		btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.mute();
			}
		});
		vlcButtons.add(btnMute);

		//add skip backward button
		btnSkipBack = new JButton("<<Backward");
		btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(-5000);
			}
		});
		vlcButtons.add(btnSkipBack);


		// add skip forward button
		btnSkip = new JButton("Forward>>");
		btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(5000);
			}
		});
		vlcButtons.add(btnSkip);

		labelTime = new JLabel("0 seconds");
		vlcButtons.add(labelTime);

		Timer timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = (long)(video.getTime()/1000.0);
				labelTime.setText(String.valueOf(time));
			}
		});
		timer.start();

		//add vlcButtons panel to frame
		frame.add(vlcButtons,BorderLayout.NORTH);


		//add more buttons for manipulating the video if last level i.e. SPECIAL reward video 
		// using ffmpeg
		versionLabel = new JLabel("Versions: ");
		ffmpegButtons.add(versionLabel);

		normalBtn = new JButton("Original");
		normalBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playVideo("big_buck_bunny_1_minute.avi");
			}
		});
		ffmpegButtons.add(normalBtn);

		fasterBtn = new JButton("X2 Faster");
		fasterBtn.addActionListener(this);
		ffmpegButtons.add(fasterBtn);

		slowerBtn = new JButton("X2 Slower");
		slowerBtn.addActionListener(this);
		ffmpegButtons.add(slowerBtn);

		invertBtn = new JButton("Inverted Colour");
		invertBtn.addActionListener(this);
		ffmpegButtons.add(invertBtn);

		frame.add(ffmpegButtons, BorderLayout.SOUTH);

		frame.setLocation(100, 100);
		frame.setSize(800, 550);
		frame.setContentPane(mediaPlayerComponent);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * Plays the video with a given file name
	 * @param filename
	 */
	public void playVideo(String filename){
		//play video
		video.playMedia(filename);
		//frame.setAlwaysOnTop(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		video.pause();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = null;
		_hiddenFilesModel.deleteVideoFile(videoOutput);

		//Reference source:
		//https://trac.ffmpeg.org/wiki/How%20to%20speed%20up%20/%20slow%20down%20a%20video

		if (e.getSource() == fasterBtn){

			command = "ffmpeg -i big_buck_bunny_1_minute.avi "
					+ "-filter_complex \"[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]\" -map \"[v]\" -map \"[a]\" "
					+ videoOutput;

		}else if (e.getSource() == slowerBtn){

			command = "ffmpeg -i big_buck_bunny_1_minute.avi "
					+ "-filter_complex \"[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]\" -map \"[v]\" -map \"[a]\" "
					+ videoOutput;

		}else if (e.getSource() == invertBtn){
			command = "ffmpeg -i big_buck_bunny_1_minute.avi -vf lutrgb=\"r=negval:g=negval:b=negval\" "
					+ videoOutput;
		}

		// Executes the ffmpeg processes inside a worker thread so the GUI does not freeze while ffmpeg is used  for processing the video

		new Worker(command).execute();;

		//a pop up pane  that tells the user the video is under process, expect the user to wait til process is finished
		JOptionPane optionPane = new JOptionPane("Just A Moment...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		dialog = new JDialog();
		dialog.setTitle("Loading");
		dialog.setModal(true);

		dialog.setContentPane(optionPane);
		dialog.setLocationRelativeTo(optionPane);
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();

		dialog.setVisible(true);
	}

	/**
	 * Swing Worker class that takes a ffmpeg command and put the manipulating process inside doInBackground
	 * @author chen
	 *
	 */
	class Worker extends SwingWorker<Void, Void>{
		String command;
		protected Worker(String command){
			this.command = command;
		}

		@Override
		protected Void doInBackground() throws Exception {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

			Process process;
			try {
				process = pb.start();
				process.waitFor(); 
				process.destroy();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			return null;
		}

		@Override
		protected void done() {
			//close dialog
			dialog.dispose();
			//play the manipulated video
			playVideo(videoOutput);
		}
	}
}

