package com.example.checkmobilecommect;

import java.io.IOException;
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
import org.json.JSONObject;

import com.example.Utils.HttpUtils;
import com.example.Utils.MyHttpConnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText mUserName;
	private EditText mPassword;
	private boolean isHidden=true;
	HttpClient client = new DefaultHttpClient();
	private Button bt_pwd_eye,mLogin;
	private String url="http://localhost:8089/ServerTest/login.jsp";
	public void myUserClear(View view){
		mUserName.setText("");
	}
	public void myPwdClear(View view){
		mPassword.setText("");
	}
	//���ע��
	public void myRegister(View view){
		startActivity(new Intent(this,RegisterActivity.class));
	}
	//��½��ť
	public void myLogin(){
		mLogin=(Button)findViewById(R.id.login);
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	//�����������
	public void myReback(View view){
		startActivity(new Intent(this,ReBackActivity.class));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PostConnect();
		init();
	}
	
	//�����������ʾ��������
	 private void init(){
		 bt_pwd_eye=(Button) findViewById(R.id.bt_pwd_eye);
		 bt_pwd_eye.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View v) {
		    if (isHidden) {
		     //����EditText�ı�Ϊ�ɼ���
		    	mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		    } else {
		     //����EditText�ı�Ϊ���ص�
		    	mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
		    }
		    isHidden = !isHidden;
		    //ˢ�½���
		    mPassword.postInvalidate();
		    //�л���EditText�������ĩβ
		    CharSequence charSequence = mPassword.getText();
		    if (charSequence instanceof Spannable) {
		     Spannable spanText = (Spannable) charSequence;
		     Selection.setSelection(spanText, charSequence.length());
		    }
		   }
		  });
	}
	private void PostConnect(){
		mUserName=(EditText)findViewById(R.id.username);
		mPassword=(EditText)findViewById(R.id.password);
		
		new Thread(){
			public void run(){
				
				String name=mUserName.getText().toString();
				String pwd=mPassword.getText().toString();
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", name));
				params.add(new BasicNameValuePair("pwd", pwd));
				HttpResponse res;
				try {
					res = HttpUtils.send(client, url, params);
					if (res.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = res.getEntity();
						String msg = EntityUtils.toString(entity,HTTP.UTF_8);
						System.out.println("--------msg"+msg);
					}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
			}.start();
		
		
	}
}
