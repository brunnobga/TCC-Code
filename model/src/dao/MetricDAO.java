package dao;

import org.hibernate.Session;

import entity.Metric;

public class MetricDAO extends DAO<Metric> {
	
	private Session s;
	
	public MetricDAO(Session s) {
		super(s, Metric.class);
		this.s = s;
	}
}
