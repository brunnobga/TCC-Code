/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package package_default;

import entity.Metric;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author brunno
 */
public class CustomComboBoxModel implements ComboBoxModel {

	public static final int METRIC_DATA = 0;
	public static final int MEDIATYPE_DATA = 1;

	private int dataType;
	private Object[] data;
	private ArrayList currentData;
	private int selectedIndex;
	private int[] auxData;

	public CustomComboBoxModel(int dataType, ArrayList queryData){
		this.dataType = dataType;
		refresh(queryData);
	}

	public void refresh(ArrayList newData){
		this.currentData = newData;
		int i;
		data = new Object[newData.size()];
		auxData = new int[newData.size()];
		switch(dataType){
			case METRIC_DATA:
				for(i = 0; i < newData.size(); i++){
					Metric a = (Metric) newData.get(i);
					data[i] = a.getName();
					auxData[i] = a.getId();
				}
				break;
			default:
				break;
		}
	}
	
	public Object getAuxData(int i){
	    return currentData.get(i);
	}

	public void setSelectedItem(Object anItem) {
		for(int i = 0; i < data.length; i++)
			if(anItem.toString().equals(data[i])) selectedIndex = i;
		return;
	}

	public Object getSelectedItem() {
		return data[selectedIndex];
	}

	public int getSize() {
		return data.length;
	}

	public Object getElementAt(int index) {
		return data[index];
	}

	public void addListDataListener(ListDataListener l) {
		return;
	}

	public void removeListDataListener(ListDataListener l) {
		return;
	}

}
