/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brunno
 */
public class MeanAndDeviation {
    int id;
    List<Double> values;
    
    public MeanAndDeviation(int id){
	this.id = id;
	values = new ArrayList<Double>();
    }
    
    public int getId(){ return id; }
    
    public void addValue(double value){
	values.add(value);
    }
    
    public double getMean(){
	double mean = 0;
	for (Double d : values)
	    mean += d;
	return mean/values.size();
    }
    
    public double getDeviation(){
	double dev = 0, mean = getMean();
	for (Double d : values)
	    dev += (d - mean)*(d - mean);
	return dev/(values.size() - 1);
    }
    
    public boolean equals(Object m){
	return ((MeanAndDeviation)m).getId() == id ? true : false;
    }
    
}
