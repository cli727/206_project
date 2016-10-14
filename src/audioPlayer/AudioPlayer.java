package audioPlayer;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioPlayer {
	public void playAudio(String filePath){
		
		/**
		 * Code copied and modified from stackOverflow:
		 * http://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
		 */
		
		/**
		 * correct : https://www.freesound.org/people/ertfelda/sounds/243701/
		 * incorrect: https://www.freesound.org/people/rhodesmas/sounds/342756/
		 */
		try {
		    File feedback = new File(filePath);
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;
		    final Clip clip;

		    stream = AudioSystem.getAudioInputStream(feedback);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.addLineListener(new LineListener() {
	            @Override
	            public void update(LineEvent event)
	            {
	                if (event.getType() == LineEvent.Type.STOP)
	                    clip.close();
	            }
	        });
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
