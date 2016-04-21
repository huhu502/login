package com.example.checkmobilecommect;

public class MyURL {
	public static final String localhost="http://192.168.199.129:8080";
	public static final String ServerName="/MyWebServer";
	public static final String Login_URL=localhost+ServerName+"/LoginServer?flag=1";
	public static final String Register_URL=localhost+ServerName+"/LoginServer?flag=2&type=1";
	public static final String Get_version=localhost+ServerName+"/version.txt";
}
