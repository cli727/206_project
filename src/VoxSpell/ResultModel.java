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

	private String _courseName;
	private String _level;

	private ArrayList<Boolean> _listOfButtons;
	private ArrayList<String> _listOfWords;
	private ArrayList<Integer> _attemptCounts; 
	protected String[] _databaseHeaderNames = { "Revise Later", "Word", "Attempts" };
	protected String[] _reviewDatabaseHeaderNames = { "Remembered", "Word", "Attempts" };

	protected ResultModel(ArrayList<String> listOfWords, ArrayList<Integer> attemptCounts, String level,String courseName) {
		_hiddenFilesModel = HiddenFilesModel.getInstance();

		_level = level;
		_courseName = courseName;

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
	private boolean selectCheckBox(int index) {

		if (VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){
			//if practice mode, automatically select items that are likely to be added to review
			if (_attemptCounts.get(index) > 1){
				return true;
			}
			return false;
		}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){
			//if review mode, automatically select items that are likely to be removed from review
			if (_attemptCounts.get(index) <= 1){
				return true;
			}
			return false;
		}
		//should not be executed
		return false;
	}

	@Override
	public String getColumnName(int col) {
		if (VoxSpellGui.STATUS.equals(VoxSpellGui.NEW)){

			return _databaseHeaderNames[col];
		}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){

			return _reviewDatabaseHeaderNames[col];
		}
		return null;
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
		System.out.println("set row: " + row + "col : " + col + " to: " + value);

		//update boolean value in both database and listOfButtons when user changes the cells
		_database.get(row).set(col, value); 
		_listOfButtons.set(row, (Boolean) value);
	}

	//method that writes all selected words into review file using hidden files manager
	public void keepRecordOfSelectedWords(){
		
		if (VoxSpellGui.STATUS.equals(VoxSpellGui.NEW )){
			//if practice mode, add selected items to review list
			for (int i = 0; i < _listOfButtons.size(); i ++){

				if (_listOfButtons.get(i)){
					//add this word from listOfWords to review file 
					_hiddenFilesModel.addWordToReviewWordsFile(_listOfWords.get(i), _level, _courseName);
				}
			}
		}else if (VoxSpellGui.STATUS.equals(VoxSpellGui.REVIEW)){
			//if review mode, delete selected items from review list
			for (int i = 0; i < _listOfButtons.size(); i ++){

				if (_listOfButtons.get(i)){
					//remove this word from its corresponding review file 
					_hiddenFilesModel.removeWordFromReviewWordsFile(_listOfWords.get(i), _level, _courseName);
				}
			}
		}
	}

}
