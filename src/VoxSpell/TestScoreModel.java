package VoxSpell;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TestScoreModel extends AbstractTableModel{
	private HiddenFilesModel _hiddenFilesModel;
	private String[] _scoreDatabaseHeaderNames = { "Word", "Corredt","Incorrect"};

	private String _courseName;
	private ArrayList<ArrayList<Object>> _database;
	private ArrayList<String> _listOfWords;
	private ArrayList<Integer> _correctCountList;
	private ArrayList<Integer> _incorrectCountList;


	protected TestScoreModel(String courseName) {
		_hiddenFilesModel = HiddenFilesModel.getInstance();
		_courseName = courseName;

		_database= new ArrayList<ArrayList<Object>>();//database can contain Strings, integers or JCheckBox components

		_listOfWords = new ArrayList<String>();
		_correctCountList = new ArrayList<Integer>();
		_incorrectCountList = new ArrayList<Integer>();
		
		createDatabase();
	}


	protected void createDatabase() {

		_listOfWords = _hiddenFilesModel.getHistroyWords(_courseName);

		_correctCountList = _hiddenFilesModel.getCorrectIncorrectCount(_courseName, true);//true for correct
		
		_incorrectCountList = _hiddenFilesModel.getCorrectIncorrectCount(_courseName, false);//false for incorrect



		for (int i = 0; i < _listOfWords.size(); i++) {
			ArrayList<Object> row = new ArrayList<Object>();

			//add word
			row.add(_listOfWords.get(i));

			//add correct
			row.add(_correctCountList.get(i));
			
			//add incorrect
			row.add(_incorrectCountList.get(i));

			_database.add(row);
		}
	}

	@Override
	public String getColumnName(int col) {

		return _scoreDatabaseHeaderNames[col];
	}

	@Override
	public int getColumnCount() {

		return _scoreDatabaseHeaderNames.length;
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

		return String.class.getClass();//all columns are of class string
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}


	public boolean emptyStats() {
		if(_listOfWords.isEmpty()){
			return true;
		}
		return false;
	}
}
