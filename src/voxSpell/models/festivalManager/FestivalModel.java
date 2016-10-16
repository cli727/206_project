package voxSpell.models.festivalManager;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JComboBox;

import voxSpell.models.hiddenFilesManager.HiddenFilesModel;

public class FestivalModel implements ItemListener {

	public enum Voice {
		AMERICAN, NEW_ZEALAND
	}

	public static Voice _currentVoice = Voice.AMERICAN; //default voice is American

	private final String _masteredSpeechSCMCmd = "(SayText \"Correct!\")";
	private final Path _masteredSpeechSCMFilePath = Paths.get("./.festival/masteredSpeech.scm");
	private final Path _failedSpeechSCMFilePath = Paths.get("./.festival/failedSpeech.scm");
	private final String _failedSpeechSCMCmd = "(SayText \"Incorrect!\")";
	
	private static FestivalModel _festivalModel;
	private ExecutorService _executorService;
	
	private FestivalModel() {
		_executorService = Executors.newFixedThreadPool(1);
		createTemplateSCMFilesForFestivalSpeech();
	}
	
	public static FestivalModel getInstance() {
		if (_festivalModel == null) {
			_festivalModel = new FestivalModel();
		}
		return _festivalModel;
	}

	public void speakCurrentWord(String currentWord) {
		String speechSCMCmd = "(SayText \"" + currentWord + "\")";
		String tempFilePath = createAndGetTempSCMFileForFestivalSpeech(speechSCMCmd).toString();
		String accentVoiceFilePath = getAccentVoiceFilePathToUse().toString();

		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + tempFilePath;
		Runnable workerThread = new FestivalModelWorker(festivalCommand);
		_executorService.execute(workerThread);
	}

	public void relistenWord(String currentWord) {
		String speechSCMCmd = "(SayText \"" + currentWord + "\")";
		String tempFilePath = createAndGetTempSCMFileForFestivalSpeech(speechSCMCmd).toString();
		String accentVoiceFilePath = getAccentVoiceFilePathToUse().toString();
		
		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + HiddenFilesModel._slowPacedVoiceFilePath.toString() + " " + tempFilePath;
		Runnable workerThread = new FestivalModelWorker(festivalCommand);
		_executorService.execute(workerThread);	
	}
	public void correctVoice(){
		String accentVoiceFilePath = getAccentVoiceFilePathToUse().toString();
		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + _masteredSpeechSCMFilePath;
		Runnable workerThread = new FestivalModelWorker(festivalCommand);
		_executorService.execute(workerThread);
	}

	public void faultedVoice(String currentWord){
		String speechSCMCmd = "(SayText \"Incorrect! Try again. \")";
		String tempFilePath = createAndGetTempSCMFileForFestivalSpeech(speechSCMCmd).toString();
		String accentVoiceFilePath = getAccentVoiceFilePathToUse().toString();

		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + tempFilePath;
		Runnable workerThread = new FestivalModelWorker(festivalCommand);
		_executorService.execute(workerThread);
	}

	public void failedVoice(){
		String accentVoiceFilePath = getAccentVoiceFilePathToUse().toString();

		String festivalCommand = "festival -b " + accentVoiceFilePath + " " + _failedSpeechSCMFilePath;
		Runnable workerThread = new FestivalModelWorker(festivalCommand);
		_executorService.execute(workerThread);

	}

	//When user selects a different voice with the JComboBox, _currentVoice field is updated to the selected voice.
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable() instanceof JComboBox) {
			JComboBox<?> item = (JComboBox<?>) e.getItemSelectable();

			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (item.getSelectedItem().equals("American (default)")) {
					_currentVoice = Voice.AMERICAN;
				}
				else {
					_currentVoice = Voice.NEW_ZEALAND;
				}
			}
		}		
	}

	private Path getAccentVoiceFilePathToUse() {

		Path accentVoiceFilePath = null;
		if (_currentVoice == Voice.AMERICAN) {
			accentVoiceFilePath = HiddenFilesModel._americanVoiceFilePath;
		}
		else if (_currentVoice == Voice.NEW_ZEALAND) {
			accentVoiceFilePath = HiddenFilesModel._newZealandVoiceFilePath;
		}

		return accentVoiceFilePath;
	}

	private void createTemplateSCMFilesForFestivalSpeech() {
		//Create the template SCM files in hidden .festival folder (located in working directory)
		//Create them only if they don't exist
		//Assumption: if they already exist, it means they were previously created using this method
		try {
			if (Files.notExists(_masteredSpeechSCMFilePath)) {
				Files.createFile(_masteredSpeechSCMFilePath);
				//Write the SCM code that makes Festival say the speech
				//For mastered
				ArrayList<String> masteredSpeech = new ArrayList<String>();
				masteredSpeech.add(_masteredSpeechSCMCmd);
				Files.write(_masteredSpeechSCMFilePath, masteredSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);

			}

			if (Files.notExists(_failedSpeechSCMFilePath)) {
				Files.createFile(_failedSpeechSCMFilePath);
				//For failed
				ArrayList<String> failedSpeech = new ArrayList<String>();
				failedSpeech.add(_failedSpeechSCMCmd);
				Files.write(_failedSpeechSCMFilePath, failedSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Path createAndGetTempSCMFileForFestivalSpeech(String speech) {
		try {
			//Create the temp SCM file in hidden .festival folder (located in working directory)			
			Path festivalFolderPath = Paths.get("./.festival");
			Path tempSCMFilePath = Files.createTempFile(festivalFolderPath, "tempSCMFileForFestivalSpeech", ".scm");
			tempSCMFilePath.toFile().deleteOnExit(); //delete the temp file when application is closed.

			//Write the SCM code that makes Festival say the speech
			ArrayList<String> festivalSpeech = new ArrayList<String>();
			festivalSpeech.add(speech);
			Files.write(tempSCMFilePath, festivalSpeech, StandardCharsets.ISO_8859_1, StandardOpenOption.WRITE);			

			//return result for passing it as a file input for Festival
			return tempSCMFilePath;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class FestivalModelWorker implements Runnable {

		String _festivalCommand;
		
		public FestivalModelWorker(String festivalCommand) {
			_festivalCommand = festivalCommand;
		}

		@Override
		public void run() {
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", _festivalCommand);
			Process process;
			try {
				process = pb.start();
				process.waitFor();
				process.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
