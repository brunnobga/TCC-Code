/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import entity.Media;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author caio
 */
public class CustomTableModel extends AbstractTableModel {

	public static final int VIDEO_DATA = 0;
	public static final int ARTIFACT_DATA = 1;
	public static final int DEVICE_DATA = 2;

	private int dataType;

	private String[][] columnNames = {
		{"!", "Title", "format", "description"},
		{""}
	};
	private Object[][] data;

	public CustomTableModel(int dataType, ArrayList queryData) {
		int i;
		this.dataType = dataType;
		data = new Object[queryData.size()][columnNames[dataType].length];
		switch(dataType){
			case VIDEO_DATA:
				for(i = 0; i < queryData.size(); i++){
					Media a = (Media) queryData.get(i);
					data[i][0] = false;
					data[i][1] = a.getTitle();
					data[i][2] = a.getFormat();
					data[i][3] = a.getDescription();
				}
				break;
			default:
				break;
		}
	}

	public void setTableData(){
	}

	public int getColumnCount() {
		return columnNames[dataType].length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[dataType][col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		if (col > 0) {
			return false;
		} else {
			return true;
		}
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i = 0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j = 0; j < numCols; j++) {
				System.out.print("  " + data[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}
}