package VoxSpell;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import VoxSpell.HiddenFilesModel.StatsFile;

/**
 * A Table Model that contains all the relevant statistics for the SpellingAidApp.
 * Statistics are shown in the ascending alphabetical order of quizzed words.
 * @author echa232
 *
 */
@SuppressWarnings("serial")
public class StatsModel extends AbstractTableModel {
	private HiddenFilesModel _hiddenFilesModel;
	private Vector<Vector<String>> _database = new Vector<Vector<String>>();
	private Vector<List<String>> _listsOfWords = new Vector<List<String>>();
	protected String[] _databaseHeaderNames = { "Word", "Mastered", "Faulted", "Failed" };
	
	protected StatsModel() {
		_hiddenFilesModel = HiddenFilesModel.getInstance();
		//getStatsFilesAsWordLists();
		createDatabase();
	}
	
	/**
	 * Get each mastered/faulted/failed words file and store them in _listsOfWords as List<String>
	 */
	private void getStatsFilesAsWordLists() {
		_listsOfWords.add(_hiddenFilesModel.getWordsFromStatsFile(StatsFile.MASTERED_WORDS));
		_listsOfWords.add(_hiddenFilesModel.getWordsFromStatsFile(StatsFile.FAULTED_WORDS));
		_listsOfWords.add(_hiddenFilesModel.getWordsFromStatsFile(StatsFile.FAILED_WORDS));
	}
	
	/**
	 * For each word quizzed so far, count the number of times word has been mastered/faulted/failed.
	 */
	private void createDatabase() {
		Vector<String> allQuizzedWords = getAllQuizzedWords();
		for (int i = 0; i < allQuizzedWords.size(); i++) {
			Vector<String> wordStats = new Vector<String>();
			
			String word = allQuizzedWords.get(i);
			wordStats.add(word);
			
			for (int j = 0; j < _listsOfWords.size(); j++) {
				Integer count = Collections.frequency(_listsOfWords.get(j), word);
				wordStats.add(count.toString());
			}
			
			_database.add(wordStats);
		}
	}
	
	/**
	 * Collect all unique words quizzed so far.
	 */
	private Vector<String> getAllQuizzedWords() {
		Vector<String> allQuizzedWords = new Vector<String>();	

		for (int i = 0; i < _listsOfWords.size(); i++) {
			List<String> wordList = _listsOfWords.get(i);
			for (int j = 0; j < wordList.size(); j++) {
				String word = wordList.get(j);
				//only add to allQuizzedWords if the word doesn't already exists
				if (!(allQuizzedWords.contains(word))) {
					allQuizzedWords.addElement(word);
				}
			}
		}
		//sort allQuizzedWords in alphabetical order
		Collections.sort(allQuizzedWords, String.CASE_INSENSITIVE_ORDER);
		return allQuizzedWords;
	}

	@Override
	public String getColumnName(int col) {
		return _databaseHeaderNames[col];
	}
	
	@Override
	public int getColumnCount() {
		return _databaseHeaderNames.length;
	}

	@Override
	public int getRowCount() {
		return _database.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector<String> rowData = _database.get(rowIndex);
		String value = rowData.get(columnIndex);
		return value;
	}
}
