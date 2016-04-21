package com.example.checkmobilecommect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.Utils.HttpUtils;
import com.example.Utils.MyHttpConnect;
import com.example.been.User;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText mUserName;
	private EditText mPassword;
	private boolean isHidden=true,isCheck;
	private String filename="info.txt";
	HttpClient client = new DefaultHttpClient();
	private Button bt_pwd_eye,mLogin;
	private String name,pwd;
//	private String url="http://localhost:8089/ServerTest/login.jsp";
	
	String TAG = "LoginActivity";
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
   
	private int nState=-1;
	
	public void myUserClear(View view){
		mUserName.setText("");
	}
	public void myPwdClear(View view){
		mPassword.setText("");
	}
	//点击注册
	public void myRegister(View view){
		startActivity(new Intent(this,RegisterActivity.class));
	}
	//登陆按钮
	public void myLogin(){
		mLogin=(Button)findViewById(R.id.login);
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				savePassword();
				PostConnect();
			}
		});
	}
	//点击忘记密码
	public void myReback(View view){
		startActivity(new Intent(this,ReBackActivity.class));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUserName=(EditText)findViewById(R.id.username);
		mPassword=(EditText)findViewById(R.id.password);
		checkconn();
		
		setNameAndPwd();
		PostConnect();
		init();
		myLogin();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	private void checkconn() {
		// TODO Auto-generated method stub
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "网络状态已经改变");
				connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d("mark", "当前网络名称：" + name);
					if (name == "WIFI") {
						netState.sendEmptyMessage(State_wifi);
					} else {
						netState.sendEmptyMessage(State_mobile);
					}

				} else {
					Log.d("mark", "没有可用网络");
					netState.sendEmptyMessage(State_none);
				}
			}
		}
	};

	private final int State_wifi = 100, State_mobile = 101, State_none = 102;
	Handler netState = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
				Log.v(TAG, "接收到了消息100");
			    nState=1;
				break;
			case 101:
				Log.v(TAG, "接收到了消息101");
			    nState=2;
				break;
			case 102:
				Log.v(TAG, "接收到了消息102");
				Builder builder = new Builder(LoginActivity.this);
				builder.setTitle("网络连接状态");
				builder.setMessage("当前未连接任何网络，请进行设置");
				;
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.v(TAG, "取消按钮被点击");
					}

				});
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
				builder.create().show();
				break;

			default:
				break;
			}
		};
	};

	//保存密码
	private void savePassword(){
		RadioButton mRadioButton=(RadioButton)findViewById(R.id.RadioButton1);
		isCheck=mRadioButton.isChecked();
		 name=mUserName.getText().toString();
		 pwd=mPassword.getText().toString();
		 System.out.println("aaa "+name+"bbb "+pwd+"ccc "+isCheck);
		//判断sdk是否存在
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String path=Environment.getExternalStorageDirectory().getAbsolutePath();
			try {
				PrintWriter out=new PrintWriter(new FileOutputStream(path+"/"+filename));
				out.println(name);
				out.println(pwd);
				out.println(isCheck);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(LoginActivity.this,"sd卡不可用", 0).show();
		}
		
	}
	//记住密码后有默认的用户名和密码
	private void setNameAndPwd(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+filename);
			try {
				BufferedReader br=new BufferedReader(new FileReader(file));
				StringBuffer sb=new StringBuffer();
				String mName=br.readLine();
				String mPwd=br.readLine();
				String check=br.readLine();
				System.out.println("----"+mName+"111"+mPwd+"dsds"+check);
				if(check.equals("true")){
					mUserName.setText(mName);
					mPassword.setText(mPwd);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			Toast.makeText(LoginActivity.this,"sd卡不可用", 0).show();
		}
	}
	//点击后密码显示或者隐藏
	 private void init(){
		 bt_pwd_eye=(Button) findViewById(R.id.bt_pwd_eye);
		 bt_pwd_eye.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
		    if (isHidden) {
		     //设置EditText文本为可见的
		    	mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		    } else {
		     //设置EditText文本为隐藏的
		    	mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		    }
		    isHidden = !isHidden;
		    //刷新界面
		    mPassword.postInvalidate();
		    //切换后将EditText光标置于末尾
		    CharSequence charSequence = mPassword.getText();
		    if (charSequence instanceof Spannable) {
		     Spannable spanText = (Spannable) charSequence;
		     Selection.setSelection(spanText, charSequence.length());
		    }
		   }
		  });
	}
	private void PostConnect(){
		
		
		new Thread(){
			public void run(){
				
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Name", name));
				params.add(new BasicNameValuePair("Pass", pwd));
				HttpResponse res;
				try {
					res = HttpUtils.send(client, MyURL.Login_URL, params);
					if (res.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = res.getEntity();
						String msg = EntityUtils.toString(entity,HTTP.UTF_8);
						System.out.println("--------msg"+msg);
						//解析json
						JSONObject json=JSONObject.parseObject(msg);
						String login=json.getString("login");
						if (login.equals("success")) {
							JSONObject ujson=json.getJSONObject("user");
							String name=ujson.getString("name");
							int id=ujson.getIntValue("id");
							
							Intent intent=new Intent(LoginActivity.this,SecondActivity.class);
							intent.putExtra("name", name);
							intent.putExtra("id", id);
							
							startActivity(intent);
							finish();
						}
					}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
			}.start();
		
		
	}
}

