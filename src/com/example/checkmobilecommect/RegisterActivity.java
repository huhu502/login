package com.example.checkmobilecommect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	String strPattern ="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	private EditText mEmail,mPwd,mRe_pwd;
	private Button mbtn;
	private int flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		CheckNumber();
	}
	private void CheckNumber(){
		mEmail=(EditText)findViewById(R.id.email);
		mPwd=(EditText)findViewById(R.id.pwd);
		mRe_pwd=(EditText)findViewById(R.id.reback_pwd);
		mbtn=(Button)findViewById(R.id.register_btn);
		mbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if((!isEmail(mEmail.getText().toString()))&&(!isPwd(mPwd.getText().toString()))){
					new AlertDialog.Builder(RegisterActivity.this)
					.setTitle("亲，邮箱格式不正确并且密码位数不足六位。").create().show();
				}
				else if(!isEmail(mEmail.getText().toString())){
					new AlertDialog.Builder(RegisterActivity.this)
					.setTitle("亲，邮箱格式不正确。").create().show();
				}else if(!isPwd(mPwd.getText().toString())){
					new AlertDialog.Builder(RegisterActivity.this)
					.setTitle("亲，密码位数不足六位。").create().show();
				}else if(!ifPwdIsSame(mRe_pwd.getText().toString())){
					new AlertDialog.Builder(RegisterActivity.this)
					.setTitle("亲，两次密码不相同。").create().show();
				}
				
			}
		});
		
	}
	private boolean isEmail(String inputText){
		Pattern p=Pattern.compile(strPattern);
		Matcher m=p.matcher(inputText);
		return m.matches();
	}
	private boolean isPwd(String inputText){
		if(inputText.length()>=6)
		return true;
		else 
			return false;
	}
	private boolean ifPwdIsSame(String inputText){
		if(inputText.equals(mPwd.getText().toString()))
		return true;
		else 
			return false;
	}

	
}
