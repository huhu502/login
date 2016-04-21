package com.example.checkmobilecommect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.example.Utils.MyHttpConnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	String strPattern ="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	private EditText mEmail,mPwd,mRe_pwd,mName;
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
		mName=(EditText) findViewById(R.id.name);
		mPwd=(EditText)findViewById(R.id.pwd);
		mRe_pwd=(EditText)findViewById(R.id.reback_pwd);
		mbtn=(Button)findViewById(R.id.register_btn);
		mbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkinfo()) {
				  regist.start();
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

	public boolean checkinfo() {
		if((!isEmail(mEmail.getText().toString()))&&(!isPwd(mPwd.getText().toString()))){
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("亲，邮箱格式不正确并且密码位数不足六位。").create().show();
			return false;
		}
		else if(!isEmail(mEmail.getText().toString())){
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("亲，邮箱格式不正确。").create().show();
			return false;
		}else if(!isPwd(mPwd.getText().toString())){
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("亲，密码位数不足六位。").create().show();
			return false;
		}else if(!ifPwdIsSame(mRe_pwd.getText().toString())){
			new AlertDialog.Builder(RegisterActivity.this)
			.setTitle("亲，两次密码不相同。").create().show();
			return false;
		}
		return true;
		
	}
	 Thread regist=new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
		 	MyHttpConnect post=new MyHttpConnect(MyURL.Register_URL);
		   	post.addPostValue("Name", mName.getText().toString());
		   	post.addPostValue("Pass", mPwd.getText().toString());
		    String result= post.Startpost();
		   	//解析json
		    JSONObject json=JSONObject.parseObject(result);
		    String regist=json.getString("register");
		    if (regist.equals("success")) {
				//注册成功消息
		    	handler.sendEmptyMessage(1);
			}else{
				//注册失败
				handler.sendEmptyMessage(2);
			}
		    
		}
	});
	 
	 Handler handler=new Handler(){
		 @Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			 switch (msg.what) {
			case 1:
				ShowToast("注册成功！");
				finish();
				break;
			case 2:
				ShowToast("注册失败！");
				break;
		
			default:
				break;
			}
		}
	 };
	protected void ShowToast(String show_msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), show_msg, Toast.LENGTH_SHORT).show();
	}
	
}
