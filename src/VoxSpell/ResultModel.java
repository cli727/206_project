package VoxSpell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;

import VoxSpell.HiddenFilesModel.StatsFile;


public class ResultModel extends AbstractTableModel {
	private HiddenFilesModel _hiddenFilesModel;
	private ArrayList<ArrayList<Object>> _database;
	
	//private ArrayList<JCheckBox> _listOfButtons;
	
	private ArrayList<Boolean> _listOfButtons;
	private ArrayList<String> _listOfWords;
	private ArrayList<Integer> _attemptCounts; 
	protected String[] _databaseHeaderNames = { "Revise Later", "Word", "Attempts" };
	
	protected ResultModel(ArrayList<String> listOfWords, ArrayList<Integer> attemptCounts) {
		_hiddenFilesModel = HiddenFilesModel.getInstance();
		
		_database= new ArrayList<ArrayList<Object>>();//database can contain Strings, integers or JCheckBox components
		
		_listOfWords = listOfWords;
		_attemptCounts = attemptCounts;
		
		_listOfButtons = new ArrayList<Boolean>();
		
		//set boolean values according to attempty counts
		for (int i = 0; i < _listOfWords.size();i++){
			_listOfButtons.add(selectCheckBox(i));
		}
		
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
	 * For each word quizzed so far, get number of attempts
	 */
	private void createDatabase() {
		
		for (int i = 0; i < _listOfWords.size(); i++) {
			ArrayList<Object> row = new ArrayList<Object>();
			
			//add JCheckBox button
			row.add(_listOfButtons.get(i));
			//add word
			row.add(_listOfWords.get(i));
			//add attemp counts for this word
			row.add(_attemptCounts.get(i));
			
			_database.add(row);
		}
	}
	
	/**
	 * Method to decide whether to set a 'revise later' checkbox as selected on result Table view
	 * @param index
	 * @return
	 */
	public boolean selectCheckBox(int index) {
		if (_attemptCounts.get(index) > 1){
			return true;
		}
		return false;
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
		
		ArrayList<Object> row =  _database.get(rowIndex);
		return row.get(columnIndex);
	}
	
	@Override
    public Class<?> getColumnClass(int col) {
        if (col == 0) {
            return Boolean.class;
        }
        return super.getColumnClass(col);
    }
	
	//User should be able to edit the first col only
	@Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0;
    }
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		 _database.get(row).set(col, value);
	}

	//method that writes all selected words into review file using hidden files manager
	private void keepRecordOfSelectedWords(){
		
	}
	
}
