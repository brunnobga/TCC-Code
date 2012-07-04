package dao;

import org.hibernate.Session;

import entity.User;

public class UserDAO extends DAO<User>{
	
	private Session s;
	
	public UserDAO(Session s){
		super(s, User.class);
		this.s = s;
	}

}
