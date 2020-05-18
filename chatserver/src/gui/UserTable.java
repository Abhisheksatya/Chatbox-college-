package gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserTable extends JTable {
	
	public UserTable(DefaultTableModel tableModel) {
		super(tableModel);
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
