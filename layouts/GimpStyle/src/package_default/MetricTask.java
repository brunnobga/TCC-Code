/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import entity.Media;
import entity.Metric;

/**
 *
 * @author brunno
 */
public class MetricTask {
    private Media video, reference;
    private Metric metric;
    
    public MetricTask(Media video, Media reference, Metric metric){
	this.video = video;
	this.reference = reference;
	this.metric = metric;
    }

    public Metric getMetric() {
	return metric;
    }

    public Media getReference() {
	return reference;
    }

    public Media getVideo() {
	return video;
    }
    
}
