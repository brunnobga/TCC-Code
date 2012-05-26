package utils;

import java.util.ArrayList;
import java.util.Calendar;

import entity.Metric;
import entity.Session;

public class SessionUtils {
	
	@SuppressWarnings("rawtypes")
	public static Session buildSession(ArrayList args) {
		if(args != null) {
			Session s = new Session();
			s.setTitle(String.valueOf(args.get(0)));
			s.setType((Integer)(args.get(1)));
			s.setMetric((Metric)(args.get(2)));
			s.setDescription(String.valueOf(args.get(4)));
			s.setSpectorsCount((Integer)(args.get(3)));
			s.setStartDate(Calendar.getInstance().getTime());
			s.setState(0);
			return s;
		} else {
			return null;
		}
	}
}
