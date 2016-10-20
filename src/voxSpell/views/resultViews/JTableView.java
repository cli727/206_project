package voxSpell.views.resultViews;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Panels that show a JTable should all extend this class, to set their tabel model
 * @author chen
 *
 */
public abstract class JTableView {
	protected AbstractTableModel _model;
	
	protected JTable _resultTable;
	protected JPanel _tablePanel;

	public void setModel(AbstractTableModel resultModel){
		_model = resultModel;
	}

}
