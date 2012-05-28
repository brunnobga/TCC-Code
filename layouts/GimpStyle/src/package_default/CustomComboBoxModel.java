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

    public static final int SMETRIC_DATA = 0;
    public static final int OMETRIC_DATA = 1;
    private int dataType;
    private Object[] data;
    private ArrayList currentData;
    private int selectedIndex;

    public CustomComboBoxModel(int dataType, ArrayList queryData) {
	this.dataType = dataType;
	refresh(queryData);
    }

    public void refresh(ArrayList newData) {
	int i;
	if(dataType == SMETRIC_DATA){
	    currentData = new ArrayList();
	    for (Object metric : newData) {
		Metric a = (Metric) metric;
		if(a.getType() == 0) currentData.add(metric);
	    }
	} else if(dataType == OMETRIC_DATA){
	    currentData = new ArrayList();
	    for (Object metric : newData) {
		Metric a = (Metric) metric;
		if(a.getType() == 1) currentData.add(metric);
	    }
	} else this.currentData = newData;
	data = new Object[currentData.size()];
	switch (dataType) {
	    case SMETRIC_DATA:
		for (i = 0; i < currentData.size(); i++) {
		    Metric a = (Metric) currentData.get(i);
		    data[i] = a.getName();
		}
		break;
	    case OMETRIC_DATA:
		for (i = 0; i < currentData.size(); i++) {
		    Metric a = (Metric) currentData.get(i);
		    data[i] = a.getName();
		}
		break;
	    default:
		break;
	}
    }

    public Object getAuxData(int i) {
	return currentData.get(i);
    }

    public void setSelectedItem(Object anItem) {
	for (int i = 0; i < data.length; i++) {
	    if (anItem.toString().equals(data[i])) {
		selectedIndex = i;
	    }
	}
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
