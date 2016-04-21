package com.example.been;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3218376320832239617L;
	private int userid;
	private String name;
	private String password;
	@Override
	public String toString() {
		return "User [userid=" + userid + ", name=" + name + ", password="
				+ password + "]";
	}
	public int getUserid(){
		return userid;
	}
	public void setUserid(int i){
		this.userid=i;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password=password;
	}

}
