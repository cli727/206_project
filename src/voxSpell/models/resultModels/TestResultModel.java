package voxSpell.models.resultModels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * This class extends ResultModel
 * The test result models does not have the functionality to add words to the review list
 * It shows the result of the test indicating the words that was spelt correct/incorrect
 * It also shows whether user has passes/failed the test
 * @author chen
 *
 */
public class TestResultModel extends AbstractTableModel  {

	protected String[] _testDatabaseHeaderNames = { "Word", "Result"};
	private int _correctNumber;
	private ArrayList<ArrayList<Object>> _database;
	private ArrayList<String> _listOfWords;
	private ArrayList<String> _correctCountList;
	

	public TestResultModel(ArrayList<String> listOfWords, ArrayList<String> correctCountList) {

		_database= new ArrayList<ArrayList<Object>>();//database can contain Strings, integers or JCheckBox components

		_listOfWords = listOfWords;
		_correctCountList = correctCountList;

		createDatabase();
	}

	
	protected void createDatabase() {
		
		_correctNumber = 0;
		
		ArrayList<String> _ifCorrect = new ArrayList<String>();

		for (int j = 0; j < _correctCountList.size(); j ++){

			if (Integer.parseInt(_correctCountList.get(j)) == 0){
				//Incorrect
				_ifCorrect.add("Incorrect");
			}else{
				//Correct
				_ifCorrect.add("Correct");
				
				_correctNumber ++;
			}

		}


		for (int i = 0; i < _listOfWords.size(); i++) {
			ArrayList<Object> row = new ArrayList<Object>();

			//add word
			row.add(_listOfWords.get(i));


			//add incorrect/correct
			row.add(_ifCorrect.get(i));

			_database.add(row);
		}
	}
	
	public int getCorrectNumber(){
		return _correctNumber;
	}


	@Override
	public String getColumnName(int col) {
		 
		return _testDatabaseHeaderNames[col];
	}

	@Override
	public int getColumnCount() {

		return _testDatabaseHeaderNames.length;
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
		
		return String.class.getClass();//both columns are of class string
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
