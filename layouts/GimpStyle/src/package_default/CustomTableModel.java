/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import entity.Device;
import entity.Media;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author caio
 */
public class CustomTableModel extends DefaultTableModel {

	public static final int VIDEO_DATA_SELECT = 0;
	public static final int VIDEO_DATA = 1;
	public static final int ARTIFACT_DATA = 2;
	public static final int DEVICE_DATA = 3;
        public static final int METRIC_TASK = 4;
        public static final int GERADOR_TASK = 5;
        public static final int RAFFLE_FILES = 6;
        public static final int USER_DATA = 7;

	private int dataType;
	private ArrayList currentData;

	private static String[][] columnNames = {
		{"!", "Título", "Formato", "Descrição"},
		{"Título", "Formato", "Descrição"},
		{""},
                {"Nome", "Porta"},
                {"Vídeo", "Referência", "Metrica"},
                {"Vídeo", "Artefato", "Raffle", "Parâmetros"},
                {"Arquivos Raffle"},
                {"Nome", "Login", "Email"},
	};
	private Object[][] data;

	public CustomTableModel(int dataType, ArrayList queryData) {
	    this.dataType = dataType;
	    refresh(queryData);
	}

	public void refresh(ArrayList newData){
		this.currentData = newData;
		int i;
		data = new Object[newData.size()][columnNames[dataType].length];
		switch(dataType){
			case VIDEO_DATA_SELECT:
				for(i = 0; i < newData.size(); i++){
					Media a = (Media) newData.get(i);
					data[i][0] = false;
					data[i][1] = a.getTitle();
					data[i][2] = a.getFormat();
					data[i][3] = a.getDescription();
				}
				break;
			case VIDEO_DATA:
				for(i = 0; i < newData.size(); i++){
					Media a = (Media) newData.get(i);
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
			case DEVICE_DATA:
			    for(i = 0; i < newData.size(); i++){
				Device a = (Device) newData.get(i);
				data[i][0] = a.getName();
				data[i][1] = a.getAddress();
			    }
			    break;
                        case GERADOR_TASK:
				for(i = 0; i < newData.size(); i++){
					GeradorTask a = (GeradorTask) newData.get(i);
					data[i][0] = a.getVideo().getTitle();
                                        data[i][1] = a.getArtefato();
                                        data[i][2] = a.getRaffleFile();
                                        String param = "";
                                        java.util.List<String> paramList = a.getParams();
                                        for(int j = 0; j < paramList.size(); ++j){
                                            param += paramList.get(i) + " ";
                                        }
                                        data[i][3] = a.getParams();
				}
				break;
                        case RAFFLE_FILES:
                                for(i = 0; i < newData.size(); i++){
                                        data[i][0] = newData.get(i);
                                }
                                break;
                        case USER_DATA:
				for(i = 0; i < newData.size(); i++){
					entity.User a = (entity.User) newData.get(i);
					data[i][0] = a.getName();
					data[i][1] = a.getLogin();
					data[i][2] = a.getContactEmail();
				}
                                break;
			default:
				break;
		}
	}
        
        public Object getAuxData(int a){
            return currentData.get(a);
        }

	public int getColumnCount() {
	    if(columnNames == null) return 0;
	    return columnNames[dataType].length;
	}

	public int getRowCount() {
	    if(data == null) return 0;
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
            switch(dataType){
                case RAFFLE_FILES:
                    return false;
                default:
                    if (col > 0) {
                            return false;
                    } else {
                            return true;
                    }
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
