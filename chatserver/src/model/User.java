package model;

import java.io.Serializable;

public class User implements Serializable{
	
	private String userId;
	private String name;
	private String department;
	transient private String password;
		
	public User(String userId, String name, String department, String password) {
		super();
		this.userId = userId;
		this.name = name;
		this.department = department;
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return this.department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString() {
		return name+" ("+department+")";
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(((User)o).getUserId().equals( this.getUserId()))
			return true;
		return false;
	}

}
