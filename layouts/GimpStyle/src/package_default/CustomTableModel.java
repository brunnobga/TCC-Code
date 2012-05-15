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

	public static final int VIDEO_DATA_SELECT = 0;
	public static final int VIDEO_DATA = 1;
	public static final int ARTIFACT_DATA = 2;
	public static final int DEVICE_DATA = 3;
        public static final int METRIC_TASK = 4;

	private int dataType;
	private ArrayList currentData;

	private String[][] columnNames = {
		{"!", "Title", "Format", "Description"},
		{"Title", "Format", "Description"},
		{""},
                {""},
                {"Video", "Reference", "Metric"}
	};
	private Object[][] data;
        private int[] auxData;

	public CustomTableModel(int dataType, ArrayList queryData) {
		this.dataType = dataType;
		refresh(queryData);
	}

	public void refresh(ArrayList newData){
		this.currentData = newData;
		int i;
		data = new Object[newData.size()][columnNames[dataType].length];
                auxData = new int[newData.size()];
		switch(dataType){
			case VIDEO_DATA_SELECT:
				for(i = 0; i < newData.size(); i++){
					Media a = (Media) newData.get(i);
                                        auxData[i] = a.getId();
					data[i][0] = false;
					data[i][1] = a.getTitle();
					data[i][2] = a.getFormat();
					data[i][3] = a.getDescription();
				}
				break;
			case VIDEO_DATA:
				for(i = 0; i < newData.size(); i++){
					Media a = (Media) newData.get(i);
                                        auxData[i] = a.getId();
					data[i][0] = a.getTitle();
					data[i][1] = a.getFormat();
					data[i][2] = a.getDescription();
				}
				break;
                        case METRIC_TASK:
				for(i = 0; i < newData.size(); i++){
					MetricTask a = (MetricTask) newData.get(i);
					data[i][0] = a.getVideo().getTitle();
					data[i][1] = a.getReference().getTitle();
					data[i][2] = a.getMetric().getName();
				}
				break;
			default:
				break;
		}
	}
        
        public Object getAuxData(int a){
            return currentData.get(a);
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

	public void printDebugData() {
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
